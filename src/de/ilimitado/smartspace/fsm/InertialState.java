package de.ilimitado.smartspace.fsm;

import de.ilimitado.smartspace.Dependencies;
import de.ilimitado.smartspace.MotionDetector;
import de.ilimitado.smartspace.SensingReactor;
import de.ilimitado.smartspace.SensorManager;
import de.ilimitado.smartspace.config.Configuration;
import de.ilimitado.smartspace.utils.L;

public class InertialState implements State {
	
	protected final String LOG_TAG = "InertialState";
	
	private SensorManager sMngr;
	private MotionDetector mtnDetector;
	private SensingReactor sReactor;
	private Thread mtnDetectorWorker;
	private Thread sReactorWorker;

	@Override
	public void enterState(Dependencies dep) {
		//Might be a good idea to clean up before entering new state
		System.gc();
		L.d(LOG_TAG, "Entering " + LOG_TAG + " state...");
		sMngr = dep.sensorDependencies.sensorManager;
		mtnDetector = dep.motionDetector;
		sReactor = dep.sensorDependencies.reactor;
	}
	
	@Override
	public void doActivity() {
		mtnDetector.startMotionDetection();
		sReactor.startDispatching(); 
		
		mtnDetectorWorker = new Thread(mtnDetector, "Motion Detector");
		mtnDetectorWorker.start();
		
		sReactorWorker = new Thread(sReactor, "SensingReactor");
		sReactorWorker.start();
		
		if(!sMngr.sensorsStarted()){
			sMngr.startSensor(Configuration.getInstance().sensorIMU.ID);
		}
	}

	@Override
	public void exitState() {
		sReactor.stopDispatching();
		mtnDetector.stopMotionDetection();
		
		if (mtnDetectorWorker != null)
			mtnDetectorWorker.interrupt();
		if (sReactorWorker != null)
			sReactorWorker.interrupt();
		
		mtnDetectorWorker = null;
		sReactorWorker = null;

		if(sMngr.sensorsStarted()){
			sMngr.stopSensors();
		}
		L.d(LOG_TAG, "Exiting Realtime state...");
	}

	@Override
	public State switchNextState(boolean pos, boolean mtn) {
		if(!mtn) {
			L.sd(LOG_TAG, "Switching to Realtime state...");
			return new RealtimeState();
		}
		else {
			L.sd(LOG_TAG, "Self Transition Inertial state state...");
			return this;
		}
	}

}
