package de.ilimitado.smartspace.tests.junit.easymock;

import java.util.List;

import de.ilimitado.smartspace.Syncable;

public interface TestableEventSynchro {
	public void isReady(String eventType, Syncable sensorHandler);
	public void receiveSensorData(String eventType, List<List<?>> sensorEventData);
}
