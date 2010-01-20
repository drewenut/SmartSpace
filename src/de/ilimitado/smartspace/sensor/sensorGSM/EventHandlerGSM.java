package de.ilimitado.smartspace.sensor.sensorGSM;

import de.ilimitado.smartspace.AbstractSyncableSensorHandler;
import de.ilimitado.smartspace.EventSynchronizer;


public class EventHandlerGSM extends AbstractSyncableSensorHandler{

	public EventHandlerGSM(String associatedSensorID, String associatedEventID, EventSynchronizer evtSync) {
		super(associatedSensorID, associatedEventID, evtSync);
	}

	@Override
	public void onShutdown() { }
}