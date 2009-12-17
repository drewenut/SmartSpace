package de.ilimitado.smartspace.sensor.sensorGSM;

import de.ilimitado.smartspace.AbstractSyncableSensorHandler;
import de.ilimitado.smartspace.EventSynchronizer;


public class EventHandlerGSM extends AbstractSyncableSensorHandler{

	public EventHandlerGSM(String associatedEventID, EventSynchronizer evtSync) {
		super(associatedEventID, evtSync);
	}
}