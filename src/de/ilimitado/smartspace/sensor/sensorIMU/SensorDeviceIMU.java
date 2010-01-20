package de.ilimitado.smartspace.sensor.sensorIMU;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import de.ilimitado.smartspace.AbstractSensorDevice;
import de.ilimitado.smartspace.AbstractSensorHandler;
import de.ilimitado.smartspace.Dependencies;
import de.ilimitado.smartspace.SensorEvent;
import de.ilimitado.smartspace.android.SmartSpaceFramework;
import de.ilimitado.smartspace.config.Configuration;
import de.ilimitado.smartspace.persistance.ScanSampleDBPersistanceProvider;
import de.ilimitado.smartspace.registry.DataCommandProvider;
import de.ilimitado.smartspace.registry.ScanSampleProvider;
import de.ilimitado.smartspace.sensor.sensorGSM.EventHandlerGSM;
import de.ilimitado.smartspace.sensor.sensorGSM.GSMDBAdapter;
import de.ilimitado.smartspace.sensor.sensorGSM.MeanCommandGSM;
import de.ilimitado.smartspace.sensor.sensorGSM.RawDataHandlerGSM;
import de.ilimitado.smartspace.sensor.sensorGSM.ScanResultGSM;
import de.ilimitado.smartspace.sensor.sensorGSM.ScanSampleGSM;
import de.ilimitado.smartspace.utils.L;

public class SensorDeviceIMU extends AbstractSensorDevice {

	private final String GSM_CELL_SCAN_EVENT_ID;
	private final String IMU_SCAN_EVENT_NAME;
	
	private SensorManager sensorManager = null;
	private SensorEventListener IMUListener;
	
	public SensorDeviceIMU(Dependencies appDeps) {
		super(appDeps);
		
		//TODO add to Configuration Object
		IMU_SCAN_EVENT_NAME = "IMU";
		
		this.sensorManager = (SensorManager) getApplicationContext().getSystemService(SENSOR_SERVICE);
	}

	@Override
	public boolean deviceAvailable() {
		return (sensorManager == null) ? false : true;
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
			sensorRunnables.add(new ScannerMotion());
		}
	}
	
	@Override
	public void initSensorID() {
		//TODO add to Configuration Object
		SENSOR_ID = "IMU";
	}

	@Override
	public void initSensorName() {
		//TODO add to Configuration Object
		SENSOR_NAME = "IMUSensor";
	}
	
	@Override
	public void registerEvents(Dependencies dep) {
			sensorHandlers.add(new RawDataHandlerGSM(SENSOR_ID, GSM_CELL_SCAN_EVENT_ID, dep.persistanceManager, dep.positionManager));
		dep.sensorDependencies.reactor.registerHandler(GSM_CELL_SCAN_EVENT_ID, sensorHandlers);
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
			sSplDBPers.putItem(GSM_CELL_SCAN_EVENT_ID, GSMDBAdapter.class);
		}
	}
	
	private boolean isActive() {
		return Configuration.getInstance().sensorGSM.scannerGSMCells.isActive;
	}

	public String toString(){
		return "sensor_gsm";
	}
	
	private void initGSMListener() {
		
		gsmListener = new PhoneStateListener() {
			
			private int cellID = SensorDeviceIMU.NO_CID;
			//rssi in dBm
			private int cellRssi = SensorDeviceIMU.NO_SIGNAL;
			
			@Override
			public void onSignalStrengthChanged(int asu) {
				super.onSignalStrengthChanged(asu);
				Log.d(LOG_TAG, "GSM Current Cell RSS changed: " + Integer.toString(asu));
				
				if(asu != -1) {
					if(asu == 0) cellRssi = SensorDeviceIMU.NO_SIGNAL;
					else if (asu == 31) cellRssi = SensorDeviceIMU.VERY_GOOD_SIGNAL;
					else {
						cellRssi = -113 + 2*asu;
					}
				}
			}

			public void onCellLocationChanged(CellLocation location) {
				super.onCellLocationChanged(location);
				Log.d(LOG_TAG, "CellLocation changed");
				if (location.getClass() == GsmCellLocation.class) {
					GsmCellLocation currentLocation = (GsmCellLocation) location;
					cellID = currentLocation.getCid();
					if(cellID != SensorDeviceIMU.NO_CID)
						postCellData();
				}
			}
			
			private void postCellData() {
				activeCellScan = new ScanResultGSM(cellID, provider, cellRssi);
			}
		};
	}
	
	private synchronized void postSensorData() {
		try {
			long commitTime = System.currentTimeMillis();
			ArrayList<ScanResultGSM> cells = (ArrayList<ScanResultGSM>) neighborCellScan.clone();
			
			for(ScanResultGSM cell : cells) {
				cell.timestamp = commitTime;
			}
			activeCellScan.timestamp = commitTime;
			cells.add(activeCellScan);
			
			systemRawDataQueue.put(new SensorEvent<List<ScanResultGSM>>(cells, GSM_CELL_SCAN_EVENT_ID, SENSOR_ID));
			L.d(LOG_TAG, "SensorEvent<ScanResultGSM> added, current systemRawDataQueue Size " + systemRawDataQueue.size());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	

	class ScannerGSM implements Runnable {

		public AtomicBoolean isActive = new AtomicBoolean(false);
		private AtomicBoolean activeCellisActive = new AtomicBoolean(false);
		private AtomicBoolean neighborCellisActive = new AtomicBoolean(false);

		@Override
		public void run() {
			isActive.set(true);
			activeCellisActive.set(true);
			neighborCellisActive.set(true);
			
			try {
				(new NeigbhorCellsScan("GSM-neighbour-cells")).start();
				Thread.sleep(1000);
				(new ActiveCellScan("GSM-active-cell")).start();
			
				while (isActive.get()) {
					if (Thread.currentThread().isInterrupted()) {
						isActive.set(false);
						break;
					}
					if(syncedNeighborCellList.size() > 0 && activeCellScan != null)
						postSensorData();
					Thread.sleep(500);
					} 
				}	
			catch (InterruptedException e) {
					isActive.set(false);
					activeCellisActive.set(false);
					neighborCellisActive.set(false);
			}
		}
		
		public String getEventID(){
			return GSM_CELL_SCAN_EVENT_ID;
		}
		
		public String getEventName(){
			return IMU_SCAN_EVENT_NAME;
		}
		
		private class NeigbhorCellsScan extends Thread {
			private static final long SENSOR_GSM_NEIGHBOR_CELL_SCAN_INTERVALL = 1000;
			
			public NeigbhorCellsScan(String threadName) {
				super(threadName);
			}

			@Override
			public void run() {
				while (neighborCellisActive.get()) {
					if (Thread.currentThread().isInterrupted()) {
						neighborCellisActive.set(false);
						break;
					}
					try {
						startScan();
						sleep(SENSOR_GSM_NEIGHBOR_CELL_SCAN_INTERVALL);
					} catch (InterruptedException e) {
						neighborCellisActive.set(false);
					}
				}
			}
			
			private void startScan() {
				List<NeighboringCellInfo> neighborCells = telephonyManager.getNeighboringCellInfo();
				if(!neighborCells.isEmpty()) {
					syncedNeighborCellList.clear();
					for(NeighboringCellInfo cellInfo : neighborCells) {
						int cid = cellInfo.getCid();
						int rssi = -113 + 2 * cellInfo.getRssi();
						syncedNeighborCellList.add(new ScanResultGSM(cid, provider, rssi));
						Log.d(LOG_TAG, "GSM Neighbor Cell: " + Integer.toString(cid) +
								       "with rssi: " + Integer.toString(rssi) + 
								       " received");
					}
				}
			}
		}
		
		private class ActiveCellScan extends Thread {
			private boolean receiverRegistered;
			
			public ActiveCellScan(String threadName) {
				super(threadName);
			}
			@Override
			public void run() {
				while (activeCellisActive.get()) {
					if (Thread.currentThread().isInterrupted()) {
						activeCellisActive.set(false);
						unregisterReceiver();
						break;
					}
					if(!receiverRegistered){
						registerReceiver();
					}
				}
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