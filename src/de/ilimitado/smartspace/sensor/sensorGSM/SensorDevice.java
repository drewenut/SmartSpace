package de.ilimitado.smartspace.sensor.sensorGSM;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
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
import de.ilimitado.smartspace.sensor.sensor80211.ScanSample80211DBPersistance;
import de.ilimitado.smartspace.utils.L;

public class SensorDevice extends AbstractSensorDevice {

	private final String SENSOR_GSM_AP_SCAN_EVENT_ID;
	private final String SENSOR_GSM_AP_SCAN_EVENT_NAME;
	private TelephonyManager telephonyManager = null;

	public SensorDevice(Dependencies appDeps) {
		super(appDeps);
		SENSOR_GSM_AP_SCAN_EVENT_ID = Configuration.getInstance().sensorGSM.scannerGSM_RSS.ID;
		SENSOR_GSM_AP_SCAN_EVENT_NAME = Configuration.getInstance().sensorGSM.scannerGSM_RSS.NAME;
	}

	@Override
	public boolean deviceAvailable() {
		this.telephonyManager = (TelephonyManager) androidCtx.getSystemService(Context.TELEPHONY_SERVICE);
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
			sensorRunnables.add(new ScannerGSMRSS());
		}
	}
	
	@Override
	public void initSensorID() {
		SENSOR_ID = config.sensor80211.ID;
	}

	@Override
	public void initSensorName() {
		SENSOR_NAME = config.sensor80211.NAME;
	}
	
	@Override
	public void registerEvents(Dependencies dep) {
		if(isActive()) {
			dep.sensorDependencies.reactor.registerHandler(SENSOR_GSM_AP_SCAN_EVENT_ID, new EventHandlerGSM(SENSOR_GSM_AP_SCAN_EVENT_ID, dep.sensorDependencies.eventSnychronizer));
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
			sSReg.putItem(SENSOR_GSM_AP_SCAN_EVENT_ID, ScanSampleGSM.class);
		}
	}
	
	@Override
	public void registerDBPersistance(ScanSampleDBPersistanceProvider sSplDBPers) {
		if(isActive()) {
			sSplDBPers.putItem(SENSOR_GSM_AP_SCAN_EVENT_ID, ScanSample80211DBPersistance.class);
		}
	}
	
	private boolean isActive() {
		return Configuration.getInstance().sensorGSM.scannerGSM_RSS.isActive;
	}

	public String toString(){
		return "sensor_80211";
	}

	class ScannerGSMRSS implements Runnable {
		private volatile boolean receiverRegistered;
		
		final int NO_SIGNAL = -113;
		final int VERY_GOOD_SIGNAL = -51;
		final int NO_CID = 0x000200;
		
		private PhoneStateListener gsmListener;
		private int currentCellID = NO_CID;
		private String provider = telephonyManager.getSimOperator();
		private volatile ScanResultGSM currentCellScan = null;
		private ArrayList<ScanResultGSM> cellList = new ArrayList<ScanResultGSM>();

		public ScannerGSMRSS() {
			initGSMListener();
		}

		@Override
		public void run() {
			while (true) {
				if (Thread.currentThread().isInterrupted()) {
					unregisterReceiver();
					break;
				}
				if(!receiverRegistered){
					registerReceiver();
					startScan();
					try {
						wait(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		private void startScan() {
			List<NeighboringCellInfo> neighborCells = telephonyManager.getNeighboringCellInfo();
			synchronized (currentCellScan) {
				if(neighborCells != null  && currentCellScan != null) {
					cellList.add(currentCellScan);
					for(NeighboringCellInfo cellInfo : neighborCells) {
						int cid = cellInfo.getCid();
						int rssi = -113 + 2 * cellInfo.getRssi();
						cellList.add(new ScanResultGSM(cid, provider, rssi));
					}
					try {
						systemRawDataQueue.put(new SensorEvent<ScanResultGSM>((ArrayList<ScanResultGSM>) cellList.clone(), getEventID()));
						cellList.clear();
					} catch (InterruptedException e) {
						//Do nothing, we are just dropping last measurement.
						e.printStackTrace();
					}
					L.d(LOG_TAG, "SensorEvent<ScanResultGSM> added, current systemRawDataQueue Size " + systemRawDataQueue.size());
				}
			}
		}
		
		public String getEventID(){
			return SENSOR_GSM_AP_SCAN_EVENT_ID;
		}
		
		public String getEventName(){
			return SENSOR_GSM_AP_SCAN_EVENT_NAME;
		}

		private void initGSMListener() {
			
			gsmListener = new PhoneStateListener() {
				
				@Override
				public void onSignalStrengthChanged(int asu) {
					super.onSignalStrengthChanged(asu);
					//rssi in dBm
					int rssi = NO_SIGNAL;
					Log.d(LOG_TAG, "GSM Current Cell RSS changed: " + Integer.toString(asu));
					
					if(asu != -1 && currentCellID != NO_CID) {
						if(asu == 0) rssi = NO_SIGNAL;
						else if (asu == 31) rssi = VERY_GOOD_SIGNAL;
						else {
							rssi = -113 + 2*asu;
						}
						currentCellScan = new ScanResultGSM(currentCellID, provider, rssi);
						notifyAll();
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
