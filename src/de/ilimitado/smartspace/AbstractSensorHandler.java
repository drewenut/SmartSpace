package de.ilimitado.smartspace;

import java.util.Collection;
import java.util.List;


public abstract class AbstractSensorHandler{
	
	protected int SENSOR_EVENT_CONSTRAINT_THRESHOLD = 10;
	protected long SENSOR_EVENT_CONSTRAINT_TIMEOUT = 10000;
	protected static final String LOG_TAG = "AbstractSensorHandler";

	protected final String associatedEventID;

	public AbstractSensorHandler(String associatedEventID) {
		this.associatedEventID = associatedEventID;
	}
	
	public boolean isSyncable(){
		return false;
	}
	
	public void setEventConstraints(List<Number> constraints) {
		SENSOR_EVENT_CONSTRAINT_THRESHOLD = constraints.get(0).intValue();
		SENSOR_EVENT_CONSTRAINT_TIMEOUT = constraints.get(1).longValue();
	}
	
	public String getAssociatedEventID(){
		return this.associatedEventID;
	}
	
	public void onShutdown() { }

	public abstract void handleEvent(SensorEvent<Collection> evt);

}
