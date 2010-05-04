package de.ilimitado.smartspace.sensor.sensor80211;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import android.util.Log;
import de.ilimitado.smartspace.AbstractSensorHandler;
import de.ilimitado.smartspace.SensorEvent;
import de.ilimitado.smartspace.persistance.FileGateway;
import de.ilimitado.smartspace.persistance.PersistanceManager;
import de.ilimitado.smartspace.positioning.iLocationManager;
import de.ilimitado.smartspace.utils.L;

public class RawDataHandler80211 extends AbstractSensorHandler {
	
	private ConcurrentHashMap<String, ArrayList<ScanResult80211>> wifiEventCache = new ConcurrentHashMap<String, ArrayList<ScanResult80211>>();
	private static final double MS_SEC_TO_SEC = 1e-3;
	//TODO put in Configuration Object...
	//just take every 5th value from sensor data stream...
	private static final int VALUE_COUNT_THRESHOLD = 5; 
	
	private final PersistanceManager persistanceMgr;
	private int valueCount = 0;
	private final iLocationManager locMngr;
	

	public RawDataHandler80211(String associatedSensorID, String associatedEventID, PersistanceManager persMgr, iLocationManager locMngr) {
		super(associatedSensorID, associatedEventID);
		this.persistanceMgr = persMgr;
		this.locMngr = locMngr;
	}

	@Override
	public void handleEvent(SensorEvent<Collection> evt) {
		List<ScanResult80211> wifiList =  (List<ScanResult80211>) evt.getEventHandle();
		if(wifiList != null && valueCount++ >= VALUE_COUNT_THRESHOLD) {
			Log.d(LOG_TAG, "Adding 80211 scan result to wifi event cache...");
			for(ScanResult80211 scnRes : wifiList) {
				String apID = getID(scnRes.SSID + scnRes.BSSID);
				if(apID != null) {
					if(!wifiEventCache.containsKey(apID)) {
						wifiEventCache.put(apID, new ArrayList<ScanResult80211>());
					}
					wifiEventCache.get(apID).add(scnRes);
				}
				else {
					L.se(LOG_TAG, "ID for AP: " + apID + " could not be created...");
				}
			}
			valueCount = 0;
		}
	}
	
	/**
	 * Called from SensingReactor before he shuts itself down! 
	 * Any Data that you Cache in handlers must be saved here!
	 */

	public void onShutdown() {
		postProcessWifiData();
	}
	
	private void postProcessWifiData() {
		Set<String> activeAPs = wifiEventCache.keySet();
		for(String apID : activeAPs) {
			StringBuffer eventDatabuffer = new StringBuffer();
			long lasttime = 0;
			double time = 0;
			
			eventDatabuffer.append("time;");
			eventDatabuffer.append("802.11"+ apID + ";");
//				wifiBuffer.append("802.11;");
			eventDatabuffer.append("\n");
			
			ArrayList<ScanResult80211> wifiAps = wifiEventCache.get(apID);
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
			fileGW.save((locMngr.getCurrentPosition()).name + "-wifi-"+ apID, eventDatabuffer);
		}
	}
	
	private String getID(String str) {
	    
	    if (str == null) {
	        return null;
	    }

	    StringBuffer strBuff = new StringBuffer();
	    char c;
	    
	    for (int i = 0; i < str.length() ; i++) {
	        c = str.charAt(i);
	        
	        if (Character.isLetterOrDigit(c)) {
	            strBuff.append(c);
	        }
	    }
	    return strBuff.toString();
	} 
}
