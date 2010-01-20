package de.ilimitado.smartspace;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;
import de.ilimitado.ShakeDetector.IMUListener;
import de.ilimitado.smartspace.sensor.sensorIMU.MotionListener;

public class MotionDetector {
	public synchronized void stopIMUSensor() {
		if(isIMUActive.get()) {
			isIMUActive.set(false);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Log.e(LOG_TAG, "Thread interuppted while stopping IMU sensors...nothing written to File");
				e.printStackTrace();
			}
		}
	}
	
	private void startIMUSensor() {
		isIMUActive.set(true);
		new Thread("IMU-Thread"){

			@Override
			public void run() {
				sensorListener = new MotionListener(shakeHandler); 
				Sensor acc = sensorMngr.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
				sensorMngr.registerListener(sensorListener, acc, SensorManager.SENSOR_DELAY_NORMAL);

		    	while(isIMUActive.get() && !Thread.interrupted());
				
		    	sensorMngr.unregisterListener(sensorListener);
			}
		}.start();
	}
}
