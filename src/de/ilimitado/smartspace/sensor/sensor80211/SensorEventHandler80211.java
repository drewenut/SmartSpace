package de.ilimitado.smartspace.sensor.sensor80211;

import de.ilimitado.smartspace.AbstractSyncableSensorHandler;
import de.ilimitado.smartspace.EventSynchronizer;

public class SensorEventHandler80211 extends AbstractSyncableSensorHandler{

	public SensorEventHandler80211(String associatedEventID, EventSynchronizer evtSync) {
		super(associatedEventID, evtSync);
	}
}