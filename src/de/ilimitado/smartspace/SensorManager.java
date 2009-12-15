package de.ilimitado.smartspace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.PowerManager;
import android.util.Log;
import de.ilimitado.smartspace.utils.L;

public class SensorManager {

	private static final String LOG_TAG = "SSFSensorManager";

	private HashMap<String, AbstractSensorDevice> sensorDeviceList = null;
	private List<ThreadGroup> runningSensorsList = null;
	private PowerManager.WakeLock wakeLock = null;
	private boolean sensorsStarted = false;

	public SensorManager() {
		this.sensorDeviceList = new HashMap<String, AbstractSensorDevice>();
		this.runningSensorsList = new ArrayList<ThreadGroup>();
	}
	
	public void setPowerManager(PowerManager pwrMngr) {
        this.wakeLock = pwrMngr.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "SMART_SPACE"); 
	}

	public void addSensor(AbstractSensorDevice sensorInstance) {
		sensorDeviceList.put(sensorInstance.getID(), sensorInstance);
	}

	public void removeSensor(String ID) {
		sensorDeviceList.remove(ID);
	}
	
	public boolean sensorsStarted(){
		return sensorsStarted;
	}
	
	public HashMap<String, AbstractSensorDevice> getSensorDeviceList(){
		return sensorDeviceList;
	}
	
	public void startSensors() {
		acquireWakeLock();
		for (AbstractSensorDevice sensor : sensorDeviceList.values()) {
			List<Runnable> sensorRunnables = sensor.getSensorRunnables();
			ThreadGroup sensorDeviceRunnables = new ThreadGroup(sensor.getName());
			for (Runnable sensorRunnable : sensorRunnables) {
				new Thread(sensorDeviceRunnables, sensorRunnable, sensor.getName()).start();
			}
			runningSensorsList.add(sensorDeviceRunnables);
		}
		sensorsStarted = true;
	}

	public void stopSensors() {
		try {
			for (ThreadGroup runningSensor : runningSensorsList) {
				runningSensor.interrupt();
			}
		} catch (SecurityException e) {
			e.printStackTrace();
			L.e(LOG_TAG, "Ups ... an exception happend while trying to release WakeLock - Here is what I know: " + e.getMessage());
		}
		finally{
			releaseWakeLock();
			sensorsStarted = false;
		}
	}

	public SensorManager initSensors() {
		for (AbstractSensorDevice sensor : sensorDeviceList.values()) {
			sensor.initDevice();
		}
		return this;
	}

	public void releaseWakeLock() {
		if (this.wakeLock != null && this.wakeLock.isHeld()) {
			L.d(LOG_TAG, "Trying to release WakeLock NOW!");
			this.wakeLock.release();
		}
	}

	public void acquireWakeLock() {
		Log.d(LOG_TAG, "Trying to acquire WakeLock NOW!");
		this.wakeLock.acquire();
	}
}