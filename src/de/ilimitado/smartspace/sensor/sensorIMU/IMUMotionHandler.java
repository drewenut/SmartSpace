package de.ilimitado.smartspace.sensor.sensorIMU;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.util.Log;
import de.ilimitado.smartspace.AbstractSensorHandler;
import de.ilimitado.smartspace.IndoorLocationManager;
import de.ilimitado.smartspace.MotionDetector;
import de.ilimitado.smartspace.SensorEvent;
import de.ilimitado.smartspace.persistance.FileGateway;
import de.ilimitado.smartspace.persistance.PersistanceManager;

public class IMUMotionHandler extends AbstractSensorHandler {
	
	private final MotionDetector motDet;
	

	public IMUMotionHandler(String associatedSensorID, String associatedEventID, MotionDetector motDet) {
		super(associatedSensorID, associatedEventID);
		this.motDet = motDet;
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
	}
}