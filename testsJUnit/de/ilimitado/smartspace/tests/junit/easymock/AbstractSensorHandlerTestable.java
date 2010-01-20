package de.ilimitado.smartspace.tests.junit.easymock;

import java.util.Collection;
import java.util.List;

import de.ilimitado.smartspace.SensorEvent;


public abstract class AbstractSensorHandlerTestable{
	
	protected int SENSOR_EVENT_CONSTRAINT_THRESHOLD = 10;
	protected long SENSOR_EVENT_CONSTRAINT_TIMEOUT = 10000;

	protected final String associatedEventID;

	public AbstractSensorHandlerTestable(String associatedEventID) {
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

	public abstract void handleEvent(SensorEvent<Collection<?>> evt);
}
