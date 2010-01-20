package de.ilimitado.smartspace;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import de.ilimitado.smartspace.sensor.sensorIMU.ScanResultIMU;

public class MotionDetector {
	
	private SensorManager sensorManager;

	public MotionDetector(Context ctx) {
		sensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE); 
	}
	
	private final LinkedBlockingQueue<ArrayList<ScanResultIMU>> motionEventsQueue = new LinkedBlockingQueue<ArrayList<ScanResultIMU>>();

	public LinkedBlockingQueue<ArrayList<ScanResultIMU>> getQueue() {
		return motionEventsQueue;
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
}
