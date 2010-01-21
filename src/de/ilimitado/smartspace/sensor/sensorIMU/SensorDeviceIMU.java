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
		
		//TODO add to Configuration Object
		IMU_SCAN_EVENT_MOTION_ID = "IMU_Motion_ID";
		IMU_SCAN_EVENT_MOTION_NAME = "IMU_Motion";
		
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
		//TODO add to Configuration Object, when add update!!! reference in InertialState.enterState()...
		SENSOR_ID = "IMU";
	}

	@Override
	public void initSensorName() {
		//TODO add to Configuration Object
		SENSOR_NAME = "IMUSensor";
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
		//TODO add to Configuration Object
		return true;
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
					if(Thread.interrupted())
						unregisterListener();
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