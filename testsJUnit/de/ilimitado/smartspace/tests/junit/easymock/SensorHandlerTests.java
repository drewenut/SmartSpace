package de.ilimitado.smartspace.tests.junit.easymock;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import de.ilimitado.smartspace.SensorEvent;
import de.ilimitado.smartspace.tests.junit.MockSyncStrategy;

public class SensorHandlerTests extends TestCase {

	private MockSyncStrategy syncStrategy;
	private EasyMockSensorHandler eventHandler1;
    private TestableEventSynchro evtSyncMock;
	private EasyMockSensorHandler eventHandler2;

	@Before
	public void setUp() throws Exception {
		evtSyncMock = createMock(TestableEventSynchro.class);
		eventHandler1 = new EasyMockSensorHandler(EasyMockSensorHandler.ASSOC_EVENT_ID_MOCK_HANDLER_1, evtSyncMock);
		eventHandler2 = new EasyMockSensorHandler(EasyMockSensorHandler.ASSOC_EVENT_ID_MOCK_HANDLER_2, evtSyncMock);
		List<Number> constraintsEvtHdl1 = new ArrayList<Number>(2);
		constraintsEvtHdl1.add(10);
		constraintsEvtHdl1.add(1000);
		List<Number> constraintsEvtHdl2 = new ArrayList<Number>(2);
		constraintsEvtHdl2.add(5);
		constraintsEvtHdl2.add(100);
		
		eventHandler1.setEventConstraints(constraintsEvtHdl1);
		eventHandler2.setEventConstraints(constraintsEvtHdl2);
	}
	
	@Test
	public void testIsReadyTrigerring() {
		evtSyncMock.isReady(EasyMockSensorHandler.ASSOC_EVENT_ID_MOCK_HANDLER_1, eventHandler1);
        replay(evtSyncMock);
        for(int i = 0; i<10; i++) {
        	eventHandler1.handleEvent(new SensorEvent(new ArrayList(), EasyMockSensorHandler.ASSOC_EVENT_ID_MOCK_HANDLER_1));
        }
        verify(evtSyncMock);
	}
	
	@Test
	public void testIsReadyTrigerring2() {
		evtSyncMock.isReady(EasyMockSensorHandler.ASSOC_EVENT_ID_MOCK_HANDLER_2, eventHandler2);
		evtSyncMock.isReady(EasyMockSensorHandler.ASSOC_EVENT_ID_MOCK_HANDLER_1, eventHandler1);
        replay(evtSyncMock);
        for(int i = 0; i<10; i++) {
        	eventHandler1.handleEvent(new SensorEvent(new ArrayList(), EasyMockSensorHandler.ASSOC_EVENT_ID_MOCK_HANDLER_1));
        	eventHandler2.handleEvent(new SensorEvent(new ArrayList(), EasyMockSensorHandler.ASSOC_EVENT_ID_MOCK_HANDLER_2));
        }
        verify(evtSyncMock);
	}
}