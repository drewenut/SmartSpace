package de.ilimitado.smartspace.android;

import java.util.concurrent.LinkedBlockingQueue;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import de.ilimitado.smartspace.Dependencies;
import de.ilimitado.smartspace.EventSynchronizer;
import de.ilimitado.smartspace.SSFLocationManager;
import de.ilimitado.smartspace.MotionDetector;
import de.ilimitado.smartspace.PositionManager;
import de.ilimitado.smartspace.SensingReactor;
import de.ilimitado.smartspace.SensorDependencies;
import de.ilimitado.smartspace.SensorEvent;
import de.ilimitado.smartspace.SensorLoader;
import de.ilimitado.smartspace.SensorManager;
import de.ilimitado.smartspace.fsm.FSM;
import de.ilimitado.smartspace.fsm.InitialState;
import de.ilimitado.smartspace.persistance.PersistanceManager;
import de.ilimitado.smartspace.registry.Registry;

public final class SmartSpaceFramework extends Service{
	
	public final static int INDOOR_POSITION_PROVIDER = 0;
	
	private SSFLocationManager iPosMngr;
	private FSM appStateMachine;
	
	private final IBinder ssf = new SSFBinder();
	
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
	
	private void bootstrap() throws Exception {
		SharedPreferences androidPreferences = this.getSharedPreferences("smartSpace", 1);
		AndroidConfigTranslator.getInstance(androidPreferences).translate();
		LinkedBlockingQueue<SensorEvent<?>> systemRawDataQueue = new LinkedBlockingQueue<SensorEvent<?>>();
		MotionDetector mtnDet = new MotionDetector();
		Registry reg = new Registry();
		PersistanceManager persMngr = new PersistanceManager();
		SensorManager sMng = new SensorManager();
		final PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
		sMng.setPowerManager(pm);
		PositionManager posMngr = new PositionManager();
		SSFLocationManager iPosMngr= new SSFLocationManager(posMngr, sMng);
		iPosMngr = iPosMngr;
		EventSynchronizer evtSync = new EventSynchronizer();
		SensingReactor sReact = new SensingReactor(reg, systemRawDataQueue);
		SensorDependencies sDep = new SensorDependencies(sReact, evtSync, sMng, systemRawDataQueue, reg);
		Dependencies appDep = new Dependencies(this, sDep, mtnDet, persMngr, posMngr);
		SensorLoader sensorLoader = new SensorLoader(appDep);
		sensorLoader.loadSensors();
		appDep.sensorDependencies.sensorManager.initSensors();
		appStateMachine = new FSM(new InitialState(), appDep); 
	}
	
	public void kill(){
		appStateMachine.killFSM();
	}
	
	public SSFLocationManager getLocationManager(int service) {
		switch (service) {
		case INDOOR_POSITION_PROVIDER:
			return iPosMngr;
		default:
			return null;
		}
	}

	public void start() {
		try {
			bootstrap();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}		
	}
}
