package de.ilimitado.smartspace;

import java.util.ArrayList;
import java.util.List;

import de.ilimitado.smartspace.utils.L;


public abstract class AbstractSyncableSensorHandler extends AbstractSensorHandler implements Syncable{

	private static final String LOG_TAG = "AbstractSyncableSensorHandler";
	
	protected EventSynchronizer eventSynchronizer = null;
	@SuppressWarnings("unchecked")
	protected ArrayList<SensorEvent> sensorEventThresholdQueue = new ArrayList<SensorEvent>();
	
	public AbstractSyncableSensorHandler(String associatedEventID, EventSynchronizer evtSync) {
		super(associatedEventID);
		setSyncronizer(evtSync);
	}
	
	@Override
	public void setSyncronizer(EventSynchronizer sSync) {
		this.eventSynchronizer = sSync;
	}
	
	@Override
	public boolean isSyncable() {
		return true;
	}
	
	@SuppressWarnings("unchecked")
	protected List<List<?>> translateEvents() {
		int queueSize = sensorEventThresholdQueue.size();
		List<List<?>> eventData = new ArrayList<List<?>>(queueSize);
		for(int item = 0; item < queueSize; item++) {
			SensorEvent sEvt = sensorEventThresholdQueue.get(item);
			eventData.add((List<?>) sEvt.getEventHandle());
		}
		return eventData;
	}

	@SuppressWarnings("unchecked")
	public void commitData(){
		List<List<?>> eventData = translateEvents();
		L.d(LOG_TAG, "Sensor handler commit data: " + getAssociatedEventID());
		eventSynchronizer.receiveSensorData(associatedEventID, eventData);
		sensorEventThresholdQueue = new ArrayList<SensorEvent>();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void handleEvent(SensorEvent evt) {
		sensorEventThresholdQueue.add(evt);
		int size = sensorEventThresholdQueue.size();
		if(size == SENSOR_EVENT_CONSTRAINT_THRESHOLD){
			L.d(LOG_TAG, "Sensor handler ready: " + getAssociatedEventID());
			eventSynchronizer.isReady(associatedEventID, this);
		}
	}
}
