package de.ilimitado.smartspace.sensor.sensor80211;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.util.Log;
import de.ilimitado.smartspace.AbstractSensorHandler;
import de.ilimitado.smartspace.SensorEvent;
import de.ilimitado.smartspace.persistance.FileGateway;
import de.ilimitado.smartspace.persistance.PersistanceManager;

public class RawDataHandler80211 extends AbstractSensorHandler {
	
	private HashMap<String, ArrayList<ScanResult80211>> wifiEventCache = new HashMap<String, ArrayList<ScanResult80211>>();
	private static final double MS_SEC_TO_SEC = 1e-3;
	//TODO put in Configuration Object...
	//just take every 20th value from sensor data stream...
	private static final int VALUE_COUNT_THRESHOLD = 20; 
	
	private final PersistanceManager persistanceMgr;
	private int valueCount = 0;
	

	public RawDataHandler80211(String associatedSensorID, String associatedEventID, PersistanceManager persMgr) {
		super(associatedSensorID, associatedEventID);
		this.persistanceMgr = persMgr;
	}

	@Override
	public void handleEvent(SensorEvent<Collection> evt) {
		List<ScanResult80211> wifiList =  (List<ScanResult80211>) evt.getEventHandle();
		if(wifiList != null && valueCount++ >= VALUE_COUNT_THRESHOLD) {
			Log.d(LOG_TAG, "Adding 80211 scan result to wifi event cache...");
			for(ScanResult80211 scnRes : wifiList) {
				String ap = scnRes.SSID;
				if(!wifiEventCache.containsKey(ap)) {
					wifiEventCache.put(ap, new ArrayList<ScanResult80211>());
				}
				wifiEventCache.get(ap).add(scnRes);
			}
			valueCount = 0;
		}
	}
	
	/**
	 * Called from SensingReactor before he shuts itself down! 
	 * Any Data that you Cache in handlers must be saved here!
	 */

	@Override
	public void onShutdown() {
		postProcessWifiData();
	}
	
	private void postProcessWifiData() {
		Set<String> activeAPs = wifiEventCache.keySet();
		for(String ap : activeAPs) {
			StringBuffer eventDatabuffer = new StringBuffer();
			long lasttime = 0;
			double time = 0;
			
			eventDatabuffer.append("time;");
			eventDatabuffer.append("802.11"+ ap + ";");
//				wifiBuffer.append("802.11;");
			eventDatabuffer.append("\n");
			
			ArrayList<ScanResult80211> wifiAps = wifiEventCache.get(ap);
			for(ScanResult80211 scnRes : wifiAps) {
				if (lasttime == 0)
					lasttime = scnRes.timestamp;
				double deltaTime = ((scnRes.timestamp - lasttime) * MS_SEC_TO_SEC);
				time  += deltaTime;
				lasttime = scnRes.timestamp;
				
				eventDatabuffer.append(time + ";");
				eventDatabuffer.append(Integer.toString(scnRes.level) + ";");
				eventDatabuffer.append("\n");
			}
			
			FileGateway fileGW = (FileGateway) persistanceMgr.get(PersistanceManager.GATEWAY_FILE_SYSTEM);
			fileGW.save("wifi-log-"+ ap, eventDatabuffer);
		}
	}
}
