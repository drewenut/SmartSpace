package de.ilimitado.smartspace.fsm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import de.ilimitado.smartspace.AbstractSensorHandler;
import de.ilimitado.smartspace.Dependencies;
import de.ilimitado.smartspace.EventSynchronizer;
import de.ilimitado.smartspace.ScanSampleList;
import de.ilimitado.smartspace.SensingReactor;
import de.ilimitado.smartspace.SensorManager;
import de.ilimitado.smartspace.config.Configuration;
import de.ilimitado.smartspace.config.ConstraintsMap;
import de.ilimitado.smartspace.persistance.PersistanceManager;
import de.ilimitado.smartspace.registry.DataCommandProvider;
import de.ilimitado.smartspace.registry.Registry;
import de.ilimitado.smartspace.registry.SensorHandlerProvider;
import de.ilimitado.smartspace.sensing.DataProcessor;
import de.ilimitado.smartspace.sensing.DataProcessorResolver;

public abstract class SensingState implements State {

	protected SensorManager sMngr;
	protected PersistanceManager persMngr;
	protected EventSynchronizer evtSync;
	protected SensingReactor sReactor;
	protected DataCommandProvider dCP;
	protected List<String> syncSet;
	protected HashMap<String, DataProcessor<ScanSampleList>> dataProcessors;
	protected Collection<ArrayList<AbstractSensorHandler>> registeredEventHandlers;
	protected Thread syncWorker = null;
	protected Thread sReactorWorker = null;
	protected SensorHandlerProvider sHdlProv;
	
	@Override
	public void enterState(Dependencies dep) {
		//Might be a good idea to clean up before entering new state
		System.gc();
		sMngr = dep.sensorDependencies.sensorManager;
		persMngr = dep.persistanceManager;
		evtSync = dep.sensorDependencies.eventSnychronizer;
		sReactor = dep.sensorDependencies.reactor;
		sHdlProv = (SensorHandlerProvider) dep.sensorDependencies.registry.get(Registry.SENSOR_HANDLER_PROVIDER);
		//TODO Refactored, but now Methods on sReactor.getActiveEventHandlers() needs to be removed
		registeredEventHandlers = sHdlProv.getValues();
		syncSet = Configuration.getInstance().getSyncList();
		dCP= (DataCommandProvider) dep.sensorDependencies.registry.get(Registry.SENSOR_DATA_CMD_PROVIDER);
		dataProcessors = DataProcessorResolver.getInstance(dCP).getDataProcessors();
		setHandlerConstraints();
	}
	
	private void setHandlerConstraints() {
		ConstraintsMap<String, List<Number>> constraints = Configuration.getInstance().getConstraints();
		for(ArrayList<AbstractSensorHandler> eventHandlers : registeredEventHandlers){
			for(AbstractSensorHandler handler : eventHandlers) {
				if(constraints.containsKey(handler.getAssociatedEventID()) && handler.isSyncable())
					handler.setEventConstraints(constraints.get(handler.getAssociatedEventID()));
			}
		}
	}
	
	@Override
	public void doActivity() {
		evtSync.startSync(); 
		sReactor.startDispatching(); 
		persMngr.startPersistance(PersistanceManager.GATEWAY_LFPT);
		
		syncWorker = new Thread(evtSync, "EventSynchronizer");
		syncWorker.start();
	
		sReactorWorker = new Thread(sReactor, "SensingReactor");
		sReactorWorker.start();
		
		if(!sMngr.sensorsStarted()){
			sMngr.startSensors();
		}
	}

	@Override
	public void exitState() {
		evtSync.stopSync();
		sReactor.stopDispatching();
		persMngr.stopPersistance(PersistanceManager.GATEWAY_LFPT);
		
		if (syncWorker != null)
			syncWorker.interrupt();
		if (sReactorWorker != null)
			sReactorWorker.interrupt();
		
		syncWorker = null;
		sReactorWorker = null;

		if(sMngr.sensorsStarted()){
			sMngr.stopSensors();
		}
	}

	@Override
	public abstract State switchNextState(boolean pos, boolean mtn);
}
