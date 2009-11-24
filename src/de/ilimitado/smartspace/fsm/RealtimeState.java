package de.ilimitado.smartspace.fsm;

import de.ilimitado.smartspace.Dependencies;
import de.ilimitado.smartspace.android.AndroidLocalDBPersistanceStrategy;
import de.ilimitado.smartspace.persistance.LFPTPersistanceStrategy;
import de.ilimitado.smartspace.persistance.PersistanceManager;
import de.ilimitado.smartspace.persistance.ScanSampleDBPersistanceProvider;
import de.ilimitado.smartspace.registry.Registry;

public class RealtimeState extends SensingState {
	
	private RTFPTSyncStrategy syncStrategy;
	private LFPTPersistanceStrategy persGWStrategy;


	@Override
	public void enterState(Dependencies dep) {
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
	public State switchNextState(boolean pos, boolean mtn) {
	 
		if(mtn)
			return new InertialState();
		else if(pos && !mtn)
			return new LearningState();
		
		//if(!pos && !mtn) 
		return this;
		
	}

}
