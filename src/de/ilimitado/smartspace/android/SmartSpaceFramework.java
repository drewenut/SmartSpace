package de.ilimitado.smartspace.android;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import de.ilimitado.smartspace.AbstractSensorDevice;
import de.ilimitado.smartspace.Dependencies;
import de.ilimitado.smartspace.EventSynchronizer;
import de.ilimitado.smartspace.MotionDetector;
import de.ilimitado.smartspace.SSFLocationManager;
import de.ilimitado.smartspace.SensingReactor;
import de.ilimitado.smartspace.SensorDependencies;
import de.ilimitado.smartspace.SensorEvent;
import de.ilimitado.smartspace.SensorLoader;
import de.ilimitado.smartspace.SensorManager;
import de.ilimitado.smartspace.config.Configuration;
import de.ilimitado.smartspace.fsm.FSM;
import de.ilimitado.smartspace.fsm.InitialState;
import de.ilimitado.smartspace.persistance.PersistanceManager;
import de.ilimitado.smartspace.positioning.iLocationManager;
import de.ilimitado.smartspace.registry.Registry;
import de.ilimitado.smartspace.sensor.sensor80211.SensorDevice80211;
import de.ilimitado.smartspace.sensor.sensorGSM.SensorDeviceGSM;
import de.ilimitado.smartspace.sensor.sensorIMU.SensorDeviceIMU;
import de.ilimitado.smartspace.utils.L;
import de.ilimitado.smartspace.utils.LogListener;

public final class SmartSpaceFramework extends Service{
	
	public final static int INDOOR_POSITION_PROVIDER = 0;
	public final static int SCIENCE_MODE = 0;
	public final static int PROLIFERATION_MODE = 1;
	
	private SSFLocationManager locationManager;
	private FSM appStateMachine;
	
	private final IBinder ssf = new SSFBinder();

	private Dependencies appDep;
	
	public class SSFBinder extends Binder {
		
		SmartSpaceFramework getSSF() {
			return SmartSpaceFramework.this;
		}
	}
	
	@Override
	public void onCreate() { }
	
	@Override
	public IBinder onBind(Intent intent) {
		return ssf;
	}
	
	public void bootstrap() {
		AndroidConfigTranslator.getInstance(this).translate();
		LinkedBlockingQueue<SensorEvent<?>> systemRawDataQueue = new LinkedBlockingQueue<SensorEvent<?>>();
		Registry reg = new Registry();
		PersistanceManager persMngr = new PersistanceManager(this);
		SensorManager sMng = new SensorManager();
		final PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
		sMng.setPowerManager(pm);
		iLocationManager indrLocMngr = new iLocationManager();
		locationManager = new SSFLocationManager(indrLocMngr);
		EventSynchronizer evtSync = new EventSynchronizer();
		SensingReactor sReact = new SensingReactor(reg, systemRawDataQueue);
		MotionDetector mtnDet = new MotionDetector();
		SensorDependencies sDep = new SensorDependencies(sReact, evtSync, sMng, systemRawDataQueue, reg);
		appDep = new Dependencies(this, sDep, mtnDet, persMngr, indrLocMngr);
		ArrayList<AbstractSensorDevice> sensorDevices = new ArrayList<AbstractSensorDevice>();
		//TODO if no sensors are selected in the config object SmartSpace has to be shut down or at least user must be notified...
		if(Configuration.getInstance().sensor80211.isActive)
			sensorDevices.add(new SensorDevice80211(appDep));
		if(Configuration.getInstance().sensorGSM.isActive)
			sensorDevices.add(new SensorDeviceGSM(appDep));
		if(Configuration.getInstance().sensorIMU.isActive)
			sensorDevices.add(new SensorDeviceIMU(appDep));
		SensorLoader sensorLoader = new SensorLoader(appDep, sensorDevices);
		sensorLoader.loadSensors();
		appDep.sensorDependencies.sensorManager.initSensors();
	}
	
	public void kill(){
		appStateMachine.killFSM();
		Configuration.destroyConfiguration();
	}
	
	public SSFLocationManager getLocationManager(int service) {
		switch (service) {
		case INDOOR_POSITION_PROVIDER:
			return locationManager;
		default:
			return null;
		}
	}
	
	public void registerLogListener(LogListener logL) {
		L.registerListener(logL);
	}
	
	public void unregisterLogListener(LogListener logL) {
		L.unregisterListener(logL);
	}

	public void start() {
		bootstrap();
		appStateMachine = new FSM(new InitialState(), appDep);
	}
}
