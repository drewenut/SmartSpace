package de.ilimitado.smartspace.sensor.sensorGSM;

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

public class RawDataHandlerGSM extends AbstractSensorHandler {
	
	private HashMap<String, ArrayList<ScanResultGSM>> gsmEventCache = new HashMap<String, ArrayList<ScanResultGSM>>();
	private static final double MS_SEC_TO_SEC = 1e-3;
	//TODO put in Configuration Object...
	//just take every 10th value from sensor data stream...
	private static final int VALUE_COUNT_THRESHOLD = 10; 
	
	private final PersistanceManager persistanceMgr;
	private int valueCount = 0;
	

	public RawDataHandlerGSM(String associatedSensorID, String associatedEventID, PersistanceManager persMgr) {
		super(associatedSensorID, associatedEventID);
		this.persistanceMgr = persMgr;
	}

	@Override
	public void handleEvent(SensorEvent<Collection> evt) {
		List<ScanResultGSM> cellList =  (List<ScanResultGSM>) evt.getEventHandle();
		if(cellList != null && valueCount++ >= VALUE_COUNT_THRESHOLD) {
			Log.d(LOG_TAG, "Adding 80211 scan result to wifi event cache...");
			for(ScanResultGSM scnRes : cellList) {
				String cellID = scnRes.CID;
				if(!gsmEventCache.containsKey(cellID)) {
					gsmEventCache.put(cellID, new ArrayList<ScanResultGSM>());
				}
				gsmEventCache.get(cellID).add(scnRes);
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
		Set<String> cells = gsmEventCache.keySet();
		for(String cellID : cells) {
			StringBuffer eventDatabuffer = new StringBuffer();
			long lasttime = 0;
			double time = 0;
			
			eventDatabuffer.append("time;");
			eventDatabuffer.append("cell"+ cellID + ";");
			eventDatabuffer.append("\n");
			
			ArrayList<ScanResultGSM> cellsList = gsmEventCache.get(cellID);
			for(ScanResultGSM scnRes : cellsList) {
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
			fileGW.save("gsm-log-"+ cellID, eventDatabuffer);
		}
	}
}