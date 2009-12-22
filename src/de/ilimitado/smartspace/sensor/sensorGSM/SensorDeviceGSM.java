package de.ilimitado.smartspace.sensor.sensorGSM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import android.content.Context;
import android.os.Looper;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import de.ilimitado.smartspace.AbstractSensorDevice;
import de.ilimitado.smartspace.Dependencies;
import de.ilimitado.smartspace.SensorEvent;
import de.ilimitado.smartspace.config.Configuration;
import de.ilimitado.smartspace.persistance.ScanSampleDBPersistanceProvider;
import de.ilimitado.smartspace.registry.DataCommandProvider;
import de.ilimitado.smartspace.registry.ScanSampleProvider;
import de.ilimitado.smartspace.utils.L;

public class SensorDeviceGSM extends AbstractSensorDevice {

	private final String GSM_CELL_SCAN_EVENT_ID;
	private final String GSM_CELL_SCAN_EVENT_NAME;
	
	private static final int NO_SIGNAL = -113;
	private static final int VERY_GOOD_SIGNAL = -51;
	private static final int NO_CID = 0x000200;
	
	private TelephonyManager telephonyManager = null;
	private String provider;
	private volatile ScanResultGSM activeCellScan = null;
	private ArrayList<ScanResultGSM> neighborCellScan = new ArrayList<ScanResultGSM>();
	private List<ScanResultGSM> syncedNeighborCellList = Collections.synchronizedList(neighborCellScan);
	
	public SensorDeviceGSM(Dependencies appDeps) {
		super(appDeps);
		
		GSM_CELL_SCAN_EVENT_ID = Configuration.getInstance().sensorGSM.scannerGSMCells.ID;
		GSM_CELL_SCAN_EVENT_NAME = Configuration.getInstance().sensorGSM.scannerGSMCells.NAME;
		
		this.telephonyManager = (TelephonyManager) androidCtx.getSystemService(Context.TELEPHONY_SERVICE);
		this.provider = telephonyManager.getSimOperator();
	}

	@Override
	public boolean deviceAvailable() {
		return (telephonyManager == null || telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_UNKNOWN) ? false : true;
	}

	@Override
	public void initDevice() {
		if (deviceAvailable()) {
			createRunnables();
		}
	}
	
	@Override
	public void createRunnables() {
		if(isActive()) {
			sensorRunnables.add(new ScannerGSM());
		}
	}
	
	@Override
	public void initSensorID() {
		SENSOR_ID = config.sensorGSM.ID;
	}

	@Override
	public void initSensorName() {
		SENSOR_NAME = config.sensorGSM.NAME;
	}
	
	@Override
	public void registerEvents(Dependencies dep) {
		if(isActive()) {
			dep.sensorDependencies.reactor.registerHandler(GSM_CELL_SCAN_EVENT_ID, new EventHandlerGSM(GSM_CELL_SCAN_EVENT_ID, dep.sensorDependencies.eventSnychronizer));
		}
	}
	
	@Override
	public void registerProcessorCommands(DataCommandProvider dCProv) {
		
		if(isActive()) {
			dCProv.putItem("MeanCommandGSM", MeanCommandGSM.class);
		}
	}
	
	public void registerScanSamples(ScanSampleProvider sSReg) {
		if(isActive()) {
			sSReg.putItem(GSM_CELL_SCAN_EVENT_ID, ScanSampleGSM.class);
		}
	}
	
	@Override
	public void registerDBPersistance(ScanSampleDBPersistanceProvider sSplDBPers) {
		if(isActive()) {
			sSplDBPers.putItem(GSM_CELL_SCAN_EVENT_ID, ScanSampleGSMDBPersistance.class);
		}
	}
	
	private boolean isActive() {
		return Configuration.getInstance().sensorGSM.scannerGSMCells.isActive;
	}

	public String toString(){
		return "sensor_gsm";
	}
	

	class ScannerGSM implements Runnable {

		public AtomicBoolean isActive = new AtomicBoolean(false);
		ActiveCellScan activeCells = new ActiveCellScan();
		NeigbhorCellsScan neighbourCells = new NeigbhorCellsScan();

		@Override
		public void run() {
			isActive.set(true);
			while (isActive.get()) {
				if (Thread.currentThread().isInterrupted()) {
					isActive.set(false);
					activeCells.isActive.set(false);
					neighbourCells.isActive.set(false);
					break;
				}
				if(!activeCells.isActive.get())
					activeCells.start();
				if(!neighbourCells.isActive.get())
					neighbourCells.start();
			}
		}
		
		private synchronized void postSensorData() {
			try {
				if(neighborCellScan.size() > 0 && activeCellScan != null) {
					ArrayList<ScanResultGSM> cells = (ArrayList<ScanResultGSM>) neighborCellScan.clone();
					cells.add(activeCellScan);
					systemRawDataQueue.put(new SensorEvent<ScanResultGSM>(cells, getID()));
					L.d(LOG_TAG, "SensorEvent<ScanResultGSM> added, current systemRawDataQueue Size " + systemRawDataQueue.size());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		public String getEventID(){
			return GSM_CELL_SCAN_EVENT_ID;
		}
		
		public String getEventName(){
			return GSM_CELL_SCAN_EVENT_NAME;
		}
		
		private class NeigbhorCellsScan extends Thread {
			private static final long SENSOR_GSM_NEIGHBOR_CELL_SCAN_INTERVALL = 1000;
			
			private String provider = telephonyManager.getSimOperator();
			private ArrayList<ScanResultGSM> cellList = new ArrayList<ScanResultGSM>();
			private List<ScanResultGSM> syncedCellList = Collections.synchronizedList(cellList);

			public AtomicBoolean isActive = new AtomicBoolean(false);;

			@Override
			public void run() {
				isActive.set(true);
				while (isActive.get()) {
					if (Thread.currentThread().isInterrupted()) {
						isActive.set(false);
						break;
					}
					try {
						startScan();
						sleep(SENSOR_GSM_NEIGHBOR_CELL_SCAN_INTERVALL);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
			private void startScan() {
				List<NeighboringCellInfo> neighborCells = telephonyManager.getNeighboringCellInfo();
				if(neighborCells != null) {
					syncedNeighborCellList.clear();
					syncedCellList.add(activeCellScan);
					for(NeighboringCellInfo cellInfo : neighborCells) {
						int cid = cellInfo.getCid();
						int rssi = -113 + 2 * cellInfo.getRssi();
						syncedCellList.add(new ScanResultGSM(cid, provider, rssi));
					}
					postSensorData();
				}
			}
		}
		
		private class ActiveCellScan extends Thread {
			private boolean receiverRegistered;
			private PhoneStateListener gsmListener;
			private int currentCellID = SensorDeviceGSM.NO_CID;
			public AtomicBoolean isActive = new AtomicBoolean(false);

			@Override
			public void run() {
				Looper.prepare();
				isActive.set(true);
				initGSMListener();
				while (isActive.get()) {
					if (Thread.currentThread().isInterrupted()) {
						isActive.set(false);
						unregisterReceiver();
						break;
					}
					if(!receiverRegistered){
						registerReceiver();
					}
				}
			}
			
			private void initGSMListener() {
				
				gsmListener = new PhoneStateListener() {
					
					@Override
					public void onSignalStrengthChanged(int asu) {
						super.onSignalStrengthChanged(asu);
						//rssi in dBm
						int rssi = SensorDeviceGSM.NO_SIGNAL;
						Log.d(LOG_TAG, "GSM Current Cell RSS changed: " + Integer.toString(asu));
						
						if(asu != -1 && currentCellID != SensorDeviceGSM.NO_CID) {
							if(asu == 0) rssi = SensorDeviceGSM.NO_SIGNAL;
							else if (asu == 31) rssi = SensorDeviceGSM.VERY_GOOD_SIGNAL;
							else {
								rssi = -113 + 2*asu;
							}
							activeCellScan = new ScanResultGSM(currentCellID, provider, rssi);
							postSensorData();
						}
					}
		
					public void onCellLocationChanged(CellLocation location) {
						super.onCellLocationChanged(location);
						Log.d(LOG_TAG, "CellLocation changed");
						if (location.getClass() == GsmCellLocation.class) {
							GsmCellLocation currentLocation = (GsmCellLocation) location;
							currentCellID = currentLocation.getCid();
						}
					}
				};
			}
				
			private synchronized void registerReceiver() {
				telephonyManager.listen(gsmListener,
						PhoneStateListener.LISTEN_CELL_LOCATION
								| PhoneStateListener.LISTEN_SIGNAL_STRENGTH);
				receiverRegistered = true;
			}

			private synchronized void unregisterReceiver() {
				if (receiverRegistered)
					telephonyManager.listen(gsmListener,
							PhoneStateListener.LISTEN_NONE);
				receiverRegistered = false;
			}
		}
	}
}