package de.ilimitado.smartspace.tests.android;

import java.util.ArrayList;
import java.util.List;

import android.test.AndroidTestCase;
import de.ilimitado.smartspace.EventSynchronizer;
import de.ilimitado.smartspace.SensorEvent;
import de.ilimitado.smartspace.tests.junit.MockSensorHandler;
import de.ilimitado.smartspace.tests.junit.MockSyncStrategy;
import de.ilimitado.smartspace.tests.junit.config.MockConfigTranslator;

public class EventSynchronizerTests extends AndroidTestCase {

	private EventSynchronizer eventSync;
	private MockSyncStrategy syncStrategy;
	private MockSensorHandler eventHandler1;
	private MockSensorHandler eventHandler2;

	public void setUp() {
		MockConfigTranslator.getInstance().translate();
		eventSync = new EventSynchronizer();
		ArrayList<String> syncSet = new ArrayList<String>();
		syncSet.add(MockSensorHandler.ASSOC_EVENT_ID_MOCK_HANDLER_1);
		syncSet.add(MockSensorHandler.ASSOC_EVENT_ID_MOCK_HANDLER_2);
		syncStrategy = new MockSyncStrategy(syncSet);
		eventHandler1 = new MockSensorHandler(MockSensorHandler.ASSOC_EVENT_ID_MOCK_HANDLER_1, eventSync);
		eventHandler2 = new MockSensorHandler(MockSensorHandler.ASSOC_EVENT_ID_MOCK_HANDLER_2, eventSync);
		List<Number> constraintsEvtHdl1 = new ArrayList<Number>(2);
		constraintsEvtHdl1.add(10);
		constraintsEvtHdl1.add(1000);
		List<Number> constraintsEvtHdl2 = new ArrayList<Number>(2);
		constraintsEvtHdl2.add(5);
		constraintsEvtHdl2.add(500);
		eventHandler1.setEventConstraints(constraintsEvtHdl1);
		eventHandler2.setEventConstraints(constraintsEvtHdl2);
		eventSync.setStrategy(syncStrategy);
	}
	
	public void testCommitDataAfterReadyToCommit() {
		eventSync.startSync();
		Thread evtSyncWorker = new Thread(eventSync);
		evtSyncWorker.start();
		for(int i = 0; i<16; i++) {
        	eventHandler1.handleEvent(new SensorEvent(new ArrayList(), MockSensorHandler.ASSOC_EVENT_ID_MOCK_HANDLER_1));
        	eventHandler2.handleEvent(new SensorEvent(new ArrayList(), MockSensorHandler.ASSOC_EVENT_ID_MOCK_HANDLER_2));
        }
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		assertEquals(6, eventHandler1.getEventQueueSize());
		assertEquals(7, eventHandler2.getEventQueueSize());
		assertEquals(1, eventHandler1.getCommitDataCalls());
		assertEquals(1, eventHandler2.getCommitDataCalls());
		assertEquals(1, syncStrategy.getProcessDataCalls());
		assertEquals(1, syncStrategy.getFusionateSamplesCalls());
		assertEquals(1, syncStrategy.getDeploySampleDataCalls());
		
		eventSync.stopSync();
		evtSyncWorker.interrupt();
	}
}