package de.ilimitado.smartspace.tests.junit.easymock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.ilimitado.smartspace.EventSynchronizer;
import de.ilimitado.smartspace.SensorEvent;
import de.ilimitado.smartspace.Syncable;


public abstract class AbstractSyncableSensorHandlerTestable extends AbstractSensorHandlerTestable implements Syncable{

	protected TestableEventSynchro eventSynchronizer = null;
	@SuppressWarnings("unchecked")
	protected ArrayList<SensorEvent> sensorEventThresholdQueue = new ArrayList<SensorEvent>();
	
	protected boolean isReady = false;

	public AbstractSyncableSensorHandlerTestable(String associatedEventID, TestableEventSynchro evtSync) {
		super(associatedEventID);
		setSyncronizer(evtSync);
	}
	
	@Override
	public void setSyncronizer(EventSynchronizer sSync) {
	}
	
	public void setSyncronizer(TestableEventSynchro sSync) {
		this.eventSynchronizer = sSync;
	}
	
	@Override
	public boolean isSyncable() {
		return true;
	}
	
	protected List<List<?>> translateEvents() {
		int queueSize = sensorEventThresholdQueue.size();
		List<List<?>> eventData = new ArrayList<List<?>>(queueSize);
		for(int item = 0; item < queueSize; item++) {
			SensorEvent sEvt = sensorEventThresholdQueue.get(item);
			eventData.add((List<?>) sEvt.getEventHandle());
		}
		return eventData;
	}

	public void commitData(){
		List<List<?>> eventData = translateEvents();
		eventSynchronizer.receiveSensorData(associatedEventID, eventData);
		sensorEventThresholdQueue = new ArrayList<SensorEvent>();
	}
	
	@Override
	public void handleEvent(SensorEvent evt) {
		int itemCountAfterAdd = sensorEventThresholdQueue.size()+1;
		
		if(itemCountAfterAdd < SENSOR_EVENT_CONSTRAINT_THRESHOLD || isReady){
			sensorEventThresholdQueue.add(evt);
		}
		else if(itemCountAfterAdd >= SENSOR_EVENT_CONSTRAINT_THRESHOLD){
			eventSynchronizer.isReady(associatedEventID, this);
			isReady = true;
		}
	}
	
}
