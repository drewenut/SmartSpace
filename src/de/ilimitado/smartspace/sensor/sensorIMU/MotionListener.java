package de.ilimitado.smartspace.sensor.sensorIMU;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;
import de.ilimitado.smartspace.sensor.sensorIMU.SensorDeviceIMU.ScannerMotion;

public class MotionListener implements SensorEventListener {
	private static final String LOG_TAG = "MotionListener";
	private final SensorDeviceIMU.ScannerMotion scannerMotion;
	
	
	public MotionListener(ScannerMotion scannerMotion) {
		this.scannerMotion = scannerMotion;
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		Log.d(LOG_TAG, "onAccuracyChanged changed, current accurcy: " + Integer.toString(accuracy));
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
			Log.d(LOG_TAG, "onSensorChanged...");
			Sensor sensor = event.sensor;
            int type = sensor.getType();
            if(type == Sensor.TYPE_ACCELEROMETER || type == Sensor.TYPE_MAGNETIC_FIELD){
            	scannerMotion.postMotionEvent(event);
            }
	}
}
