package de.ilimitado.smartspace.sensor.sensor80211;

import de.ilimitado.smartspace.AbstractSyncableSensorHandler;
import de.ilimitado.smartspace.EventSynchronizer;

public class EventHandler80211 extends AbstractSyncableSensorHandler{

public EventHandler80211(String associatedSensorID, String associatedEventID, EventSynchronizer evtSync) {
		super(associatedSensorID, associatedEventID, evtSync);
	}
}
