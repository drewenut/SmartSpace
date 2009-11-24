package de.ilimitado.smartspace;

import java.util.concurrent.LinkedBlockingQueue;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.PowerManager;
import de.ilimitado.smartspace.android.AndroidConfigTranslator;
import de.ilimitado.smartspace.fsm.FSM;
import de.ilimitado.smartspace.persistance.PersistanceManager;
import de.ilimitado.smartspace.registry.Registry;

public final class SmartSpaceFramework extends Thread{
	
	public final static int INDOOR_POSITION_PROVIDER = 0;
	
	private static IndoorPositionManager iPosMngr;
	private static FSM appStateMachine;
	private Context androidCtx;
	private boolean stayAlive = true;
	
	public SmartSpaceFramework(Context androidCtx) {
		super("SmartSpaceMain");
		this.androidCtx = androidCtx;
	}
	@Override
	public void run() {
		try {
			bootstrap();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		while(stayAlive) {
			//have Fun
		}
		//!stayAlive ---> RIP
		interrupt();
	}
	
	
	public void bootstrap() throws Exception {
		SharedPreferences androidPreferences = androidCtx.getSharedPreferences("smartSpace", 1);
		AndroidConfigTranslator.getInstance(androidPreferences).translate();
		LinkedBlockingQueue<SensorEvent<?>> systemRawDataQueue = new LinkedBlockingQueue<SensorEvent<?>>();
		MotionDetector mtnDet = new MotionDetector();
		Registry reg = new Registry();
		PersistanceManager persMngr = new PersistanceManager();
		SensorManager sMng = new SensorManager();
		final PowerManager pm = (PowerManager) androidCtx.getSystemService(Context.POWER_SERVICE);
		sMng.setPowerManager(pm);
		PositionManager posMngr = new PositionManager();
		IndoorPositionManager iPosMngr= new IndoorPositionManager(posMngr, sMng);
		SmartSpaceFramework.iPosMngr = iPosMngr;
		EventSynchronizer evtSync = new EventSynchronizer();
		SensingReactor sReact = new SensingReactor(reg, systemRawDataQueue);
		SensorDependencies sDep = new SensorDependencies(sReact, evtSync, sMng, systemRawDataQueue, reg);
		Dependencies appDep = new Dependencies(androidCtx, sDep, mtnDet, persMngr, posMngr);
		SensorLoader sensorLoader = new SensorLoader(appDep);
		sensorLoader.loadSensors();
		appDep.sensorDependencies.sensorManager.initSensors();
		appStateMachine = new FSM(new InitialState(), appDep); 
	}
	
	public static void kill(){
		appStateMachine.killFSM();
	}
	
	public void die(){
		stayAlive = false;
	}
	
	public static IndoorPositionManager getSystemService(int service){
		switch (service) {
		case INDOOR_POSITION_PROVIDER:
			return iPosMngr;
		default:
			return null;
		}
	}
}
