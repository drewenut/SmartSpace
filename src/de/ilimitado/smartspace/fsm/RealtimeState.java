package de.ilimitado.smartspace.fsm;

import de.ilimitado.smartspace.Dependencies;
import de.ilimitado.smartspace.android.AndroidLocalDBPersistanceStrategy;
import de.ilimitado.smartspace.persistance.LFPTPersistanceStrategy;
import de.ilimitado.smartspace.persistance.PersistanceManager;
import de.ilimitado.smartspace.persistance.ScanSampleDBPersistanceProvider;
import de.ilimitado.smartspace.registry.Registry;
import de.ilimitado.smartspace.utils.L;

public class RealtimeState extends SensingState {
	
	private RTFPTSyncStrategy syncStrategy;
	private LFPTPersistanceStrategy persGWStrategy;


	@Override
	public void enterState(Dependencies dep) {
		L.d(LOG_TAG, "Entering Realtime state...");
		super.enterState(dep);
		syncStrategy = new RTFPTSyncStrategy(dep,  syncSet, dataProcessors);
		persGWStrategy = new AndroidLocalDBPersistanceStrategy((ScanSampleDBPersistanceProvider) dep.sensorDependencies.registry.get(Registry.SENSOR_SCANSAMPLE_DBPERS_PROVIDER), dep.androidDependencies);
	}

	@Override
	public void doActivity() {
		evtSync.setStrategy(syncStrategy);
		persMngr.setStrategy(PersistanceManager.GATEWAY_LFPT, persGWStrategy);
		super.doActivity();
	}
	
	@Override
	public void exitState() {
		super.exitState();
		L.d(LOG_TAG, "Exiting Realtime state...");
	}

	@Override
	public State switchNextState(boolean pos, boolean mtn) {
	 
		if(mtn) {
			L.sd(LOG_TAG, "Switching to Inertial state...");
			return new InertialState();
		}
		else if(pos && !mtn) {
			L.sd(LOG_TAG, "Switching to Learning state...");
			return new LearningState();
		}
		
		//if(!pos && !mtn) 
		return this;
		
	}

}
