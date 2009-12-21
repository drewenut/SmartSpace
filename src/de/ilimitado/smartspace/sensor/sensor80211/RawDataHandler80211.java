package de.ilimitado.smartspace.sensor.sensor80211;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.util.Log;
import de.ilimitado.smartspace.AbstractSensorHandler;
import de.ilimitado.smartspace.SensorEvent;
import de.ilimitado.smartspace.android.FileManager;

public class RawDataHandler80211 extends AbstractSensorHandler {
	
	private HashMap<String, ArrayList<ScanResult80211>> wifiEventCache = new HashMap<String, ArrayList<ScanResult80211>>();
	private static final double MS_SEC_TO_SEC = 1e-3;
	private static final String LOG_TAG = "RawDataHandler80211";

	public RawDataHandler80211(String associatedEventID) {
		super(associatedEventID);
	}

	@Override
	public void handleEvent(SensorEvent<Collection> evt) {
		List<ScanResult80211> wifiList =  (List<ScanResult80211>) evt.getEventHandle();
		if(wifiList != null) {
			Log.d(LOG_TAG, "Adding 80211 scan result to wifi event cache...");
			for(ScanResult80211 scnRes : wifiList) {
				String ap = scnRes.SSID;
				if(!wifiEventCache.containsKey(ap)) {
					wifiEventCache.put(ap, new ArrayList<ScanResult80211>());
				}
				wifiEventCache.get(ap).add(scnRes);
			}
		}
	}
	
	@Override
	public void onShutdown() {
		postProcessWifiData();
	}
	
	private void postProcessWifiData() {
		Set<String> activeAPs = wifiEventCache.keySet();
		for(String ap : activeAPs) {
			StringBuffer eventDataCache = new StringBuffer();
			long lasttime = 0;
			double time = 0;
			
			eventDataCache.append("time;");
			eventDataCache.append("802.11"+ ap + ";");
//				wifiBuffer.append("802.11;");
			eventDataCache.append("\n");
			
			ArrayList<ScanResult80211> wifiAps = wifiEventCache.get(ap);
			for(ScanResult80211 scnRes : wifiAps) {
				if (lasttime == 0)
					lasttime = scnRes.timestamp;
				double deltaTime = ((scnRes.timestamp - lasttime) * MS_SEC_TO_SEC)/60;
				time  += deltaTime;
				lasttime = scnRes.timestamp;
				
				eventDataCache.append(time + ";");
				eventDataCache.append(Integer.toString(scnRes.level) + ";");
				eventDataCache.append("\n");
			}
			FileManager.writeFile("wifi-log-"+ ap, eventDataCache);
		}
	}
}
