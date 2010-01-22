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
import de.ilimitado.smartspace.positioning.iLocationManager;

public class RawDataHandlerGSM extends AbstractSensorHandler {
	
	private HashMap<String, ArrayList<ScanResultGSM>> gsmEventBuffer = new HashMap<String, ArrayList<ScanResultGSM>>();
	private static final double MS_SEC_TO_SEC = 1e-3;
	//TODO put in Configuration Object...
	//just take every 10th value from sensor data stream...
	private static final int VALUE_COUNT_THRESHOLD = 10; 
	
	private final PersistanceManager persMgr;
	private iLocationManager locMngr;
	private int valueCount = 0;
	

	public RawDataHandlerGSM(String associatedSensorID, String associatedEventID, PersistanceManager persMgr, iLocationManager locMngr) {
		super(associatedSensorID, associatedEventID);
		this.persMgr = persMgr;
		this.locMngr = locMngr;
	}

	@Override
	public void handleEvent(SensorEvent<Collection> evt) {
		List<ScanResultGSM> cellList =  (List<ScanResultGSM>) evt.getEventHandle();
		if(cellList != null && valueCount++ >= VALUE_COUNT_THRESHOLD) {
			Log.d(LOG_TAG, "Adding 80211 scan result to wifi event cache...");
			for(ScanResultGSM scnRes : cellList) {
				String cellID = scnRes.CID;
				if(!gsmEventBuffer.containsKey(cellID)) {
					gsmEventBuffer.put(cellID, new ArrayList<ScanResultGSM>());
				}
				gsmEventBuffer.get(cellID).add(scnRes);
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
		Set<String> cells = gsmEventBuffer.keySet();
		if(!cells.isEmpty()) {
			for(String cellID : cells) {
				StringBuffer eventDatabuffer = new StringBuffer();
				long lasttime = 0;
				double time = 0;
				
				eventDatabuffer.append("time;");
				eventDatabuffer.append("cell"+ cellID + ";");
				eventDatabuffer.append("\n");
				
				ArrayList<ScanResultGSM> cellsList = gsmEventBuffer.get(cellID);
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
				
				FileGateway fileGW = (FileGateway) persMgr.get(PersistanceManager.GATEWAY_FILE_SYSTEM);
				fileGW.save((locMngr.getCurrentPosition()).name + "-gsm-"+ cellID, eventDatabuffer);
			}
		}
	}
}