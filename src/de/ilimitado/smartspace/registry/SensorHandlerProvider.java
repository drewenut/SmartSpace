package de.ilimitado.smartspace.registry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import de.ilimitado.smartspace.AbstractSensorHandler;


public class SensorHandlerProvider extends HashMap<String, ArrayList<AbstractSensorHandler>>{

	private static final long serialVersionUID = 1L;

	public boolean isRegistered(String eventID) {
		return (this.containsKey(eventID));
	}

	public ArrayList<AbstractSensorHandler> addSensorHandler(String eventID,
			ArrayList<AbstractSensorHandler> sensorHandlers) {
		return this.put(eventID, sensorHandlers);
	}

	public void removeSensorHandler(String eventID) {
		if (isRegistered(eventID))
			this.remove(eventID);
	}

	public ArrayList<AbstractSensorHandler> getSensorHandler(String eventID) {
		return this.get(eventID);
	}
	
	public Set<String> getKeySet(){
		return this.keySet();
	}
	
	public Collection<ArrayList<AbstractSensorHandler>> getValues(){
		return this.values();
	}
}
