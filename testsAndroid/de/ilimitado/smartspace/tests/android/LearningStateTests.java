package de.ilimitado.smartspace.tests.android;

import java.util.concurrent.LinkedBlockingQueue;

import android.content.Context;
import android.os.PowerManager;
import android.test.AndroidTestCase;
import de.ilimitado.smartspace.Dependencies;
import de.ilimitado.smartspace.EventSynchronizer;
import de.ilimitado.smartspace.MotionDetector;
import de.ilimitado.smartspace.SensingReactor;
import de.ilimitado.smartspace.SensorDependencies;
import de.ilimitado.smartspace.SensorEvent;
import de.ilimitado.smartspace.SensorManager;
import de.ilimitado.smartspace.fsm.LearningState;
import de.ilimitado.smartspace.persistance.PersistanceManager;
import de.ilimitado.smartspace.positioning.iLocationManager;
import de.ilimitado.smartspace.registry.Registry;
import de.ilimitado.smartspace.tests.junit.config.MockConfigTranslator;


public class LearningStateTests extends AndroidTestCase{

	private Dependencies appDep;
	private LearningState lS;
	
	@Override
	public void setUp() {
		MockConfigTranslator.getInstance().translate();
		LinkedBlockingQueue<SensorEvent<?>> systemRawDataQueue = new LinkedBlockingQueue<SensorEvent<?>>();
		MotionDetector mtnDet = new MotionDetector();
		Registry reg = new Registry();
		PersistanceManager persMngr = new PersistanceManager(getContext());
		SensorManager sMng = new SensorManager();
		final PowerManager pm = (PowerManager) getContext().getSystemService(Context.POWER_SERVICE);
		sMng.setPowerManager(pm);
		iLocationManager posMngr = new iLocationManager();
		EventSynchronizer evtSync = new EventSynchronizer();
		SensingReactor sReact = new SensingReactor(reg, systemRawDataQueue);
		SensorDependencies sDep = new SensorDependencies(sReact, evtSync, sMng, systemRawDataQueue, reg);
		appDep = new Dependencies(getContext(), sDep, mtnDet, persMngr, posMngr);
		lS = new LearningState();
	}
	
	public void testLearningState() {
		lS.enterState(appDep);
		lS.doActivity();
		lS.exitState();
	}
}
