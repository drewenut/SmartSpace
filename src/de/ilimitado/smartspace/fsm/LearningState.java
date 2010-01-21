package de.ilimitado.smartspace.fsm;

import de.ilimitado.smartspace.Dependencies;
import de.ilimitado.smartspace.android.AndroidLocalDBPersistanceStrategy;
import de.ilimitado.smartspace.persistance.PersistanceManager;
import de.ilimitado.smartspace.persistance.ScanSampleDBPersistanceProvider;
import de.ilimitado.smartspace.registry.Registry;
import de.ilimitado.smartspace.utils.L;


public class LearningState extends SensingState {
	
	private LFPTSyncStrategy syncStrategy;
	private AndroidLocalDBPersistanceStrategy persGWStrategy;

	@Override
	public void enterState(Dependencies dep) {
		L.d(LOG_TAG, "Entering Learning state...");
		super.enterState(dep);
		syncStrategy = new LFPTSyncStrategy(dep, syncSet, dataProcessors);
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
		L.d(LOG_TAG, "Exiting Learning state...");
	}
	
	@Override
	public State switchNextState(boolean pos, boolean mtn) {
	 
		if(mtn){ }
		//TODO switch off for now, Motion just used in RT State
//			return new InertialState();
		else if(!pos && !mtn) {
			L.sd(LOG_TAG, "Switching to Realtime state...");
			return new RealtimeState();
		}
		
		//if(pos && !mtn) 
		return this;
	}
}
