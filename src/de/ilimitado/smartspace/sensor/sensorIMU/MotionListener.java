package de.ilimitado.smartspace.sensor.sensorIMU;

import java.util.ArrayList;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;

public class MotionListener implements SensorEventListener {
	private static final String LOG_TAG = "IMUSensorListener";
	
	private final double NANO_SEC_TO_SEC = 1e-9;
	
	private ArrayList<InertialEvent> imuEventCache = new ArrayList<InertialEvent>();
	private Handler shakeHandler;
	
	public MotionListener(Handler shakeHandler) {
		this.shakeHandler = shakeHandler;
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		Log.d(LOG_TAG, "onAccuracyChanged changed...");
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
			Log.d(LOG_TAG, "OnSensor changed...");
			Sensor sensor = event.sensor;
            int type = sensor.getType();
            if(type == Sensor.TYPE_ACCELEROMETER || type == Sensor.TYPE_MAGNETIC_FIELD){
            	if(imuEventCache.size() <= ShakeDetector.EVENT_THRESHOLD )
            		imuEventCache.add(new InertialEvent(event));
            	else
            		detectShake();
            		imuEventCache.clear();
            }
	}
	
	private void detectShake() {
		
        float[] R = new float[16];
        float[] I = new float[16];
		
		long lasttime = 0;
		double time = 0;
		
		ArrayList<InertialEvent> sensorEvents = (ArrayList<InertialEvent>) imuEventCache.clone();
		
		float[] accelerometerSensorData = null;
		float[] magneticFieldSensorData = null;
		
		double currentAcc = 0d;
		
		for (InertialEvent event : sensorEvents) {

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
		
		if(currentAcc > ShakeDetector.SHAKE_SENSIVITY)
			shakeHandler.sendEmptyMessage(0);
	}
}
