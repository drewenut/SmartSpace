package de.ilimitado.smartspace.fsm;

import de.ilimitado.smartspace.Dependencies;
import de.ilimitado.smartspace.MotionDetector;
import de.ilimitado.smartspace.SensorManager;
import de.ilimitado.smartspace.utils.L;

public class InertialState implements State {
	
	private static final String LOG_TAG = "InertialState";
	
	private SensorManager sMngr;
	private MotionDetector mtnDetector;
	private Thread mtnDetectorWorker;

	@Override
	public void enterState(Dependencies dep) {
		//Might be a good idea to clean up before entering new state
		System.gc();
		L.d(LOG_TAG, "Entering Inertial state...");
		sMngr = dep.sensorDependencies.sensorManager;
		mtnDetector = dep.motionDetector;
	}
	
	@Override
	public void doActivity() {
		mtnDetector.startMotionDetection();
		
		mtnDetectorWorker = new Thread(mtnDetector, "Motion Detector");
		mtnDetectorWorker.start();
		
		if(!sMngr.sensorsStarted()){
			sMngr.startSensor("IMU");
		}
	}

	@Override
	public void exitState() {
		mtnDetector.stopMotionDetection();
		
		if (mtnDetector != null)
			mtnDetectorWorker.interrupt();
		
		mtnDetector = null;

		if(sMngr.sensorsStarted()){
			sMngr.stopSensors();
		}
		L.d(LOG_TAG, "Exiting Realtime state...");
	}

	@Override
	public State switchNextState(boolean pos, boolean mtn) {
		if(!pos && !mtn) {
			L.d(LOG_TAG, "Switching to Realtime state...");
			return new RealtimeState();
		}
		else if(pos && !mtn) {
			L.d(LOG_TAG, "Switching to Learning state...");
			return new LearningState();
		}
		//if(mtn) 
		return this;
		
	}

}
