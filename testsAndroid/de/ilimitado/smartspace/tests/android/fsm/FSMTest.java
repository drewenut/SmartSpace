package de.ilimitado.smartspace.tests.android.fsm;

import java.util.concurrent.LinkedBlockingQueue;

import android.content.Context;
import android.os.PowerManager;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import de.ilimitado.smartspace.Dependencies;
import de.ilimitado.smartspace.EventSynchronizer;
import de.ilimitado.smartspace.MotionDetector;
import de.ilimitado.smartspace.IndoorLocationManager;
import de.ilimitado.smartspace.SensingReactor;
import de.ilimitado.smartspace.SensorDependencies;
import de.ilimitado.smartspace.SensorEvent;
import de.ilimitado.smartspace.SensorManager;
import de.ilimitado.smartspace.fsm.FSM;
import de.ilimitado.smartspace.fsm.FinalState;
import de.ilimitado.smartspace.fsm.InertialState;
import de.ilimitado.smartspace.fsm.LearningState;
import de.ilimitado.smartspace.fsm.RealtimeState;
import de.ilimitado.smartspace.fsm.State;
import de.ilimitado.smartspace.persistance.PersistanceManager;
import de.ilimitado.smartspace.registry.Registry;
import de.ilimitado.smartspace.tests.junit.config.MockConfigTranslator;

public class FSMTest extends AndroidTestCase{

	private FSM fsm;
	private Dependencies appDep;

	public void setUp() throws Exception {
		MockConfigTranslator.getInstance().translate();
		LinkedBlockingQueue<SensorEvent<?>> systemRawDataQueue = new LinkedBlockingQueue<SensorEvent<?>>();
		MotionDetector mtnDet = new MotionDetector();
		Registry reg = new Registry();
		PersistanceManager persMngr = new PersistanceManager(getContext());
		SensorManager sMng = new SensorManager();
		final PowerManager pm = (PowerManager) getContext().getSystemService(Context.POWER_SERVICE);
		sMng.setPowerManager(pm);
		IndoorLocationManager posMngr = new IndoorLocationManager();
		EventSynchronizer evtSync = new EventSynchronizer();
		SensingReactor sReact = new SensingReactor(reg, systemRawDataQueue);
		SensorDependencies sDep = new SensorDependencies(sReact, evtSync, sMng, systemRawDataQueue, reg);
		appDep = new Dependencies(getContext(), sDep, mtnDet, persMngr, posMngr);
	}

	@SmallTest
	public void testFSM() {
		State state = new MockInitialState();
		fsm = new FSM(state, appDep);
		state = fsm.getCurrentState();
		assertTrue(state instanceof RealtimeState);
	}
	
	@SmallTest
	public void testKillFSM() {
		State state = new MockInitialState();
		fsm = new FSM(state, appDep);
		fsm.killFSM();
		state = fsm.getCurrentState();
		assertTrue(state instanceof FinalState);
	}

	@SmallTest
	public void testFixedPositionAction() {
		State state = new MockInitialState();
		fsm = new FSM(state, appDep);
		fsm.fixedPositionAction(true);
		state = fsm.getCurrentState();
		assertTrue(state instanceof LearningState);
	}

	@SmallTest
	public void testMotionAction() {
		State state = new MockLearningState();
		fsm = new FSM(state, appDep);
		fsm.motionAction(true);
		state = fsm.getCurrentState();
		assertTrue(state instanceof InertialState);
	}
	
	@SmallTest
	public void testAllStateTransitions() {
		State initialState = new MockInitialState();
		fsm = new FSM(initialState, appDep);
		//!fpos && !mtn
		assertTrue(fsm.getCurrentState() instanceof RealtimeState);
		
		fsm.fixedPositionAction(true);
		State state = fsm.getCurrentState();
		//fpos && !mtn
		assertTrue(state instanceof LearningState);
		
		fsm.motionAction(true);
		state = fsm.getCurrentState();
		//fpos && mtn 
		assertTrue(state instanceof InertialState);
	
		fsm.motionAction(false);
		state = fsm.getCurrentState();
		//fpos && !mtn
		assertTrue(state instanceof LearningState);
		
		fsm.fixedPositionAction(false);
		state = fsm.getCurrentState();
		//!fpos && !mtn
		assertTrue(state instanceof RealtimeState);
		
		fsm.motionAndFixedPositionAction(false, false);
		state = fsm.getCurrentState();
		//!fpos && !mtn
		assertTrue(state instanceof RealtimeState);
		
		fsm.motionAction(true);
		state = fsm.getCurrentState();
		//!fpos && mtn 
		assertTrue(state instanceof InertialState);
	}
	
}