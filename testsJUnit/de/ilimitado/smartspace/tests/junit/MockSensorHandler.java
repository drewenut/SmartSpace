package de.ilimitado.smartspace.tests.junit;

import java.util.ArrayList;
import java.util.List;

import de.ilimitado.smartspace.AbstractSyncableSensorHandler;
import de.ilimitado.smartspace.EventSynchronizer;
import de.ilimitado.smartspace.SensorEvent;

public class MockSensorHandler extends AbstractSyncableSensorHandler {
	public final static String ASSOC_EVENT_ID_MOCK_HANDLER_1 = "Mock_Sensor_Handler1";
	public final static String ASSOC_EVENT_ID_MOCK_HANDLER_2 = "Mock_Sensor_Handler2";
	private int dataCommitedCalls = 0;
	private int handleEventCalls;
	
	public MockSensorHandler(String associatedSensorID, String associatedEventID, EventSynchronizer evtSync) {
		super(associatedSensorID, associatedEventID, evtSync);
	}
	
	public int getEventQueueSize(){
		return sensorEventThresholdQueue.size();
	}
	
	public int getCommitDataCalls(){
		return dataCommitedCalls;
	}
	
	public int getHandleEventCalls(){
		return dataCommitedCalls;
	}
	
	@Override
	public void commitData(){
		dataCommitedCalls ++;
		List<List<?>> eventData = translateEvents();
		eventSynchronizer.receiveSensorData(associatedEventID, eventData);
		sensorEventThresholdQueue = new ArrayList<SensorEvent>();
	}
}
