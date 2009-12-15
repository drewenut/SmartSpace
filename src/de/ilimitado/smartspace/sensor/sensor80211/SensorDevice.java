package de.ilimitado.smartspace.sensor.sensor80211;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
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

	private final String SENSOR_80211_AP_SCAN_EVENT_ID;
	private final String SENSOR_80211_AP_SCAN_EVENT_NAME;
	private WifiManager wifiManager = null;

	private boolean prevWifiState = false;

	public SensorDevice(Dependencies appDeps) {
		super(appDeps);
		SENSOR_80211_AP_SCAN_EVENT_ID = Configuration.getInstance().sensor80211.scanner80211.ID;
		SENSOR_80211_AP_SCAN_EVENT_NAME = Configuration.getInstance().sensor80211.scanner80211.NAME;
	}

	@Override
	public boolean deviceAvailable() {
		this.wifiManager = (WifiManager) androidCtx.getSystemService(Context.WIFI_SERVICE);
		return wifiManager == null ? false : true;
	}

	@Override
	public void initDevice() {
		if (!wifiManager.isWifiEnabled())
			enableWifi();

		if (deviceAvailable()) {
			createRunnables();
		}
	}
	
	@Override
	public void createRunnables() {
		if(Configuration.getInstance().sensor80211.scanner80211.isActive) {
			sensorRunnables.add(new Scanner80211Passive());
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
			dep.sensorDependencies.reactor.registerHandler(SENSOR_80211_AP_SCAN_EVENT_ID, new SensorEventHandler80211(SENSOR_80211_AP_SCAN_EVENT_ID, dep.sensorDependencies.eventSnychronizer));
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
			sSReg.putItem(SENSOR_80211_AP_SCAN_EVENT_ID, ScanSample80211.class);
		}
	}
	
	@Override
	public void registerDBPersistance(ScanSampleDBPersistanceProvider sSplDBPers) {
		if(Configuration.getInstance().sensor80211.scanner80211.isActive) {
			sSplDBPers.putItem(SENSOR_80211_AP_SCAN_EVENT_ID, ScanSample80211DBPersistance.class);
		}
	}

	public String toString(){
		return "sensor_80211";
	}

	public void disableWifi() {
		if (wifiManager.isWifiEnabled()) {
			prevWifiState = true;
			wifiManager.setWifiEnabled(false);
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
			wifiManager.setWifiEnabled(true);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// nothing
			}

			Log.d(LOG_TAG, "Wifi started!");
		}
	}

	class Scanner80211Passive implements Runnable {
		private WifiReceiver receiverWifi;
		private IntentFilter intentFilter;
		private volatile boolean receiverRegistered;

		public Scanner80211Passive() {
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
					wifiManager.startScan();
				}
			}
		}
		
		public String getEventID(){
			return SENSOR_80211_AP_SCAN_EVENT_ID;
		}
		
		public String getEventName(){
			return SENSOR_80211_AP_SCAN_EVENT_NAME;
		}

		private void advertiseScanResults() {
			List<ScanResult> wifiList = wifiManager.getScanResults();
			List<ScanResult80211> apList = new ArrayList<ScanResult80211>(wifiList.size());
			for(ScanResult scnRes : wifiList){
				apList.add(new ScanResult80211(scnRes.SSID, 
											   scnRes.BSSID, 
											   scnRes.capabilities, 
											   scnRes.level, 
											   scnRes.frequency));
			}
			try {
				systemRawDataQueue.put(new SensorEvent<ScanResult80211>(apList,getEventID()));
			} catch (InterruptedException e) {
				//Do nothing, we are just dropping last measurement.
				e.printStackTrace();
			}
			L.d(LOG_TAG, "SensorEvent<ScanResult80211> added, current systemRawDataQueue Size "+systemRawDataQueue.size());
			wifiManager.startScan();
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