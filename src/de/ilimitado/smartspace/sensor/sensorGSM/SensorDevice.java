package de.ilimitado.smartspace.sensor.sensorGSM;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import de.ilimitado.smartspace.AbstractSensorDevice;
import de.ilimitado.smartspace.Dependencies;
import de.ilimitado.smartspace.SensorEvent;
import de.ilimitado.smartspace.config.Configuration;
import de.ilimitado.smartspace.persistance.ScanSampleDBPersistanceProvider;
import de.ilimitado.smartspace.registry.DataCommandProvider;
import de.ilimitado.smartspace.registry.ScanSampleProvider;
import de.ilimitado.smartspace.utils.L;

public class SensorDevice extends AbstractSensorDevice {

	private final String SENSOR_GSM_AP_SCAN_EVENT_ID;
	private final String SENSOR_GSM_AP_SCAN_EVENT_NAME;
	private TelephonyManager telephonyManager = null;
	private boolean prevWifiState = false;

	public SensorDevice(Dependencies appDeps) {
		super(appDeps);
		SENSOR_GSM_AP_SCAN_EVENT_ID = Configuration.getInstance().sensorGSM.scannerGSM_RSS.ID;
		SENSOR_GSM_AP_SCAN_EVENT_NAME = Configuration.getInstance().sensorGSM.scannerGSM_RSS.NAME;
	}

	@Override
	public boolean deviceAvailable() {
		this.telephonyManager = (TelephonyManager) androidCtx.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager == null ? false : true;
	}

	@Override
	public void initDevice() {
		if (deviceAvailable()) {
			createRunnables();
		}
	}
	
	@Override
	public void createRunnables() {
		if(Configuration.getInstance().sensorGSM.scannerGSM_RSS.isActive) {
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
		if(Configuration.getInstance().sensor80211.scanner80211.isActive) {
			dep.sensorDependencies.reactor.registerHandler(SENSOR_GSM_AP_SCAN_EVENT_ID, new SensorEventHandler80211(SENSOR_GSM_AP_SCAN_EVENT_ID, dep.sensorDependencies.eventSnychronizer));
		}
	}
	
	@Override
	public void registerProcessorCommands(DataCommandProvider dCProv) {
		
		if(Configuration.getInstance().sensor80211.scanner80211.isActive) {
			dCProv.putItem("MeanCommand80211", MeanCommand80211.class);
		}
	}
	
	public void registerScanSamples(ScanSampleProvider sSReg) {
		if(Configuration.getInstance().sensor80211.scanner80211.isActive) {
			sSReg.putItem(SENSOR_GSM_AP_SCAN_EVENT_ID, ScanSample80211.class);
		}
	}
	
	@Override
	public void registerDBPersistance(ScanSampleDBPersistanceProvider sSplDBPers) {
		if(Configuration.getInstance().sensor80211.scanner80211.isActive) {
			sSplDBPers.putItem(SENSOR_GSM_AP_SCAN_EVENT_ID, ScanSample80211DBPersistance.class);
		}
	}

	public String toString(){
		return "sensor_80211";
	}

	public void disableWifi() {
		if (telephonyManager.isWifiEnabled()) {
			prevWifiState = true;
			telephonyManager.setWifiEnabled(false);
			Log.d(LOG_TAG, "Wifi disabled!");
			// Waiting for interface-shutdown
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// nothing
			}
		}
	}

	public void enableWifi() {
		if (prevWifiState) {
			// Waiting for interface-restart
			telephonyManager.setWifiEnabled(true);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// nothing
			}

			Log.d(LOG_TAG, "Wifi started!");
		}
	}

	class ScannerGSMRSS implements Runnable {
		private WifiReceiver receiverWifi;
		private IntentFilter intentFilter;
		private volatile boolean receiverRegistered;

		public ScannerGSMRSS() {
			this.receiverWifi = new WifiReceiver();
			this.intentFilter = new IntentFilter();
		}

		// @Override
		public void run() {
			while (true) {
				if (Thread.currentThread().isInterrupted()) {
					unregisterReceiver();
					break;
				}
				if(!receiverRegistered){
					registerReceiver();
					telephonyManager.startScan();
				}
			}
		}
		
		public String getEventID(){
			return SENSOR_GSM_AP_SCAN_EVENT_ID;
		}
		
		public String getEventName(){
			return SENSOR_GSM_AP_SCAN_EVENT_NAME;
		}

		private void advertiseScanResults() {
			List<ScanResult> wifiList = telephonyManager.getScanResults();
			List<ScanResultGSM> apList = new ArrayList<ScanResultGSM>(wifiList.size());
			for(ScanResult scnRes : wifiList){
				apList.add(new ScanResultGSM(scnRes.SSID, 
											   scnRes.BSSID, 
											   scnRes.capabilities, 
											   scnRes.level, 
											   scnRes.frequency));
			}
			try {
				systemRawDataQueue.put(new SensorEvent<ScanResultGSM>(apList,getEventID()));
			} catch (InterruptedException e) {
				//Do nothing, we are just dropping last measurement.
				e.printStackTrace();
			}
			L.d(LOG_TAG, "SensorEvent<ScanResult80211> added, current systemRawDataQueue Size "+systemRawDataQueue.size());
			telephonyManager.startScan();
		}

		private synchronized void registerReceiver() {
			intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
			androidCtx.registerReceiver(receiverWifi, intentFilter);
			receiverRegistered = true;
		}

		private synchronized void unregisterReceiver() {
			if (receiverRegistered)
				androidCtx.unregisterReceiver(receiverWifi);
			receiverRegistered = false;
		}

		class WifiReceiver extends BroadcastReceiver {

			@Override
			public void onReceive(Context context, Intent intent) {
				final String action = intent.getAction();
				if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
					advertiseScanResults();
				}
			}
		}
	}
}
