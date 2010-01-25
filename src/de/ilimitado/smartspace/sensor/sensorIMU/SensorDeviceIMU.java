package de.ilimitado.smartspace.sensor.sensorIMU;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import de.ilimitado.smartspace.AbstractSensorDevice;
import de.ilimitado.smartspace.AbstractSensorHandler;
import de.ilimitado.smartspace.Dependencies;
import de.ilimitado.smartspace.SensorEvent;
import de.ilimitado.smartspace.config.Configuration;
import de.ilimitado.smartspace.persistance.ScanSampleDBPersistanceProvider;
import de.ilimitado.smartspace.registry.DataCommandProvider;
import de.ilimitado.smartspace.registry.ScanSampleProvider;
import de.ilimitado.smartspace.utils.L;

public class SensorDeviceIMU extends AbstractSensorDevice {

	protected final String IMU_SCAN_EVENT_MOTION_ID;
	protected final String IMU_SCAN_EVENT_MOTION_NAME;
	
	private SensorManager sensorManager = null;
	private SensorEventListener motionListener;
	
	public SensorDeviceIMU(Dependencies appDeps) {
		super(appDeps);
		
		IMU_SCAN_EVENT_MOTION_ID = Configuration.getInstance().sensorIMU.scannerMotion.ID;
		IMU_SCAN_EVENT_MOTION_NAME = Configuration.getInstance().sensorIMU.scannerMotion.NAME;
		
		this.sensorManager = (SensorManager) androidCtx.getSystemService(Context.SENSOR_SERVICE);
	}

	@Override
	public boolean deviceAvailable() {
		return (sensorManager == null) ? false : true;
	}

	@Override
	public void initDevice() {
		if (deviceAvailable()) {
			createRunnables();
		}
	}
	
	@Override
	public void createRunnables() {
		if(isActive()) {
			sensorRunnables.add(new ScannerMotion());
		}
	}
	
	@Override
	public void initSensorID() {
		SENSOR_ID = Configuration.getInstance().sensorIMU.ID;
	}

	@Override
	public void initSensorName() {
		SENSOR_NAME = Configuration.getInstance().sensorIMU.NAME;
	}
	
	@Override
	public void registerEvents(Dependencies dep) {
		ArrayList<AbstractSensorHandler> sensorHandlers = new ArrayList<AbstractSensorHandler>();	
		sensorHandlers.add(new IMUMotionHandler(SENSOR_ID, IMU_SCAN_EVENT_MOTION_ID, dep.motionDetector));
		dep.sensorDependencies.reactor.registerHandler(IMU_SCAN_EVENT_MOTION_ID, sensorHandlers);
	}
	
	@Override
	public void registerProcessorCommands(DataCommandProvider dCProv) { }
	
	public void registerScanSamples(ScanSampleProvider sSReg) { }
	
	@Override
	public void registerDBPersistance(ScanSampleDBPersistanceProvider sSplDBPers) {	}
	
	private boolean isActive() {
		return Configuration.getInstance().sensorIMU.scannerMotion.isActive;
	}

	public String toString(){
		return "sensor_imu";
	}
	
	class ScannerMotion implements Runnable {

		public AtomicBoolean isActive = new AtomicBoolean(false);
		protected boolean listenerRegistered = false;

		@Override
		public void run() {
			isActive.set(true);
			try {
				while (isActive.get()) {
					if(Thread.interrupted()) {
						unregisterListener();
						break;
					}
					if(!listenerRegistered)
						registerListener();
				}
				Thread.sleep(1000);
			}
			catch (InterruptedException e) {
					unregisterListener();
			}
		}
		
		public String getEventID(){
			return IMU_SCAN_EVENT_MOTION_ID;
		}
		
		public String getEventName(){
			return IMU_SCAN_EVENT_MOTION_NAME;
		}
		
		private synchronized void registerListener() {
			motionListener = new MotionListener(this); 
			Sensor acc = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
			sensorManager.registerListener(motionListener, acc, SensorManager.SENSOR_DELAY_NORMAL);
			Sensor mField = sensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD).get(0);
			sensorManager.registerListener(motionListener, mField, SensorManager.SENSOR_DELAY_NORMAL);
			listenerRegistered = true;
		}

		private synchronized void unregisterListener() {
			isActive.set(false);
			if (listenerRegistered)
				sensorManager.unregisterListener(motionListener);
			listenerRegistered = false;
		}
		
		public void postMotionEvent(android.hardware.SensorEvent event) {
			try {
				ScanResultIMU scnResIMU = new ScanResultIMU(event);
				SensorEvent<ScanResultIMU> sEvt = new SensorEvent<ScanResultIMU>(scnResIMU, IMU_SCAN_EVENT_MOTION_ID, SENSOR_ID);
				systemRawDataQueue.put(sEvt);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			L.d(LOG_TAG, "SensorEvent<ScanResultIMU> added, current systemRawDataQueue Size " + systemRawDataQueue.size());
		}
	}
}