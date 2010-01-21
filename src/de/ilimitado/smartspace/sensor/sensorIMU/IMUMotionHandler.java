package de.ilimitado.smartspace.sensor.sensorIMU;

import java.util.ArrayList;
import java.util.Collection;

import android.util.Log;
import de.ilimitado.smartspace.AbstractSensorHandler;
import de.ilimitado.smartspace.MotionDetector;
import de.ilimitado.smartspace.SensorEvent;

public class IMUMotionHandler extends AbstractSensorHandler {
	
	private final MotionDetector motDet;
	private ArrayList<ScanResultIMU> motionEventBuffer = new ArrayList<ScanResultIMU>();
	
	//TODO put in Configuration Object...
	//just take every 10th value from sensor data stream...
	private static final int VALUE_COUNT_THRESHOLD = 3; 
	
	private int valueCount = 0;

	public IMUMotionHandler(String associatedSensorID, String associatedEventID, MotionDetector motDet) {
		super(associatedSensorID, associatedEventID);
		this.motDet = motDet;
	}

	@Override
	public void handleEvent(SensorEvent<Collection> evt) {
		ScanResultIMU mtnEvt =  (ScanResultIMU) evt.getEventHandle();
		if(mtnEvt != null && valueCount++ <= VALUE_COUNT_THRESHOLD) {
			Log.d(LOG_TAG, "Adding Motion Event to motion event cache...");
		    motionEventBuffer.add(mtnEvt);
		}
		else {
			try {
				motDet.getQueue().put((ArrayList<ScanResultIMU>) motionEventBuffer.clone());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			finally{
				motionEventBuffer = new ArrayList<ScanResultIMU>();
				valueCount = 0;
			}
		}
	}
}