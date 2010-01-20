package de.ilimitado.smartspace.sensor.sensorIMU;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;
import de.ilimitado.smartspace.AbstractSensorHandler;
import de.ilimitado.smartspace.IndoorLocationManager;
import de.ilimitado.smartspace.MotionDetector;
import de.ilimitado.smartspace.SensorEvent;
import de.ilimitado.smartspace.persistance.FileGateway;
import de.ilimitado.smartspace.persistance.PersistanceManager;
import de.ilimitado.smartspace.sensor.sensorGSM.ScanResultGSM;

public class IMUMotionHandler extends AbstractSensorHandler {
	
	private final MotionDetector motDet;
	private ArrayList<ScanResultGSM> motionEventBuffer = new ArrayList<ScanResultGSM>();
	private final double NANO_SEC_TO_SEC = 1e-9;
	
	//TODO put in Configuration Object...
	//just take every 10th value from sensor data stream...
	private static final int VALUE_COUNT_THRESHOLD = 10; 
	
	private int valueCount = 0;

	public IMUMotionHandler(String associatedSensorID, String associatedEventID, MotionDetector motDet) {
		super(associatedSensorID, associatedEventID);
		this.motDet = motDet;
	}

	@Override
	public void handleEvent(SensorEvent<Collection> evt) {
		ScanResultGSM mtnEvt =  (ScanResultGSM) evt.getEventHandle();
		if(imuEvt != null && valueCount++ >= VALUE_COUNT_THRESHOLD) {
			Log.d(LOG_TAG, "Adding Motion Event to motion event cache...");
				if(!gsmEventCache.containsKey(cellID)) {
					gsmEventCache.put(cellID, new ArrayList<ScanResultGSM>());
				}
				gsmEventCache.get(cellID).add(scnRes);
			}
			valueCount = 0;
		}
	}
	
	private void detectShake() {
		
        float[] R = new float[16];
        float[] I = new float[16];
		
		long lasttime = 0;
		double time = 0;
		
		ArrayList<ScanResultIMU> sensorEvents = (ArrayList<ScanResultIMU>) imuEventCache.clone();
		
		float[] accelerometerSensorData = null;
		float[] magneticFieldSensorData = null;
		
		double currentAcc = 0d;
		
		for (ScanResultIMU event : sensorEvents) {

			if(event.sensorType == Sensor.TYPE_ACCELEROMETER)
				accelerometerSensorData = event.values;
			else if(event.sensorType == Sensor.TYPE_MAGNETIC_FIELD)
				magneticFieldSensorData = event.values;
			
			if(accelerometerSensorData != null && magneticFieldSensorData != null){
			
				if (lasttime == 0)
					lasttime = event.timestamp;
				double deltaTime = (event.timestamp - lasttime) * NANO_SEC_TO_SEC;
				time  += deltaTime;
				lasttime = event.timestamp;
	        	
				SensorManager.getRotationMatrix(R, I, accelerometerSensorData, magneticFieldSensorData);
				
	        	//Substract gravity from every axis
				float xAccWithoutG = accelerometerSensorData[0] - R[8] * SensorManager.GRAVITY_EARTH;
				float yAccWithoutG = accelerometerSensorData[1] - R[9] * SensorManager.GRAVITY_EARTH;
				float zAccWithoutG = accelerometerSensorData[2] - R[10] * SensorManager.GRAVITY_EARTH;

				//Vector Product to get acceleration sqrt(x² + y² + z²)
				double accWithoutG = Math.sqrt(xAccWithoutG * xAccWithoutG + yAccWithoutG * yAccWithoutG + zAccWithoutG * zAccWithoutG);

				currentAcc = (currentAcc + accWithoutG)/2; 
			}
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