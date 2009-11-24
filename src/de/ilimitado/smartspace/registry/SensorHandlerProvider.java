package de.ilimitado.smartspace.registry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import de.ilimitado.smartspace.AbstractSensorHandler;


public class SensorHandlerProvider extends HashMap<String, AbstractSensorHandler>{

	private static final long serialVersionUID = 1L;

	public boolean isRegistered(String eventID) {
		return (this.containsKey(eventID));
	}

	public AbstractSensorHandler addSensorHandler(String eventID,
			AbstractSensorHandler sensorHandler) {
		return this.put(eventID, sensorHandler);
	}

	public void removeSensorHandler(String eventID) {
		if (isRegistered(eventID))
			this.remove(eventID);
	}

	public AbstractSensorHandler getSensorHandler(String eventID) {
		return this.get(eventID);
	}
	
	public Set<String> getKeySet(){
		return this.keySet();
	}
	
	public Collection<AbstractSensorHandler> getValues(){
		return this.values();
	}
}
