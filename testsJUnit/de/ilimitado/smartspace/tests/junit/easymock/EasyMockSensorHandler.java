package de.ilimitado.smartspace.tests.junit.easymock;


public class EasyMockSensorHandler extends AbstractSyncableSensorHandlerTestable {
	public final static String ASSOC_EVENT_ID_MOCK_HANDLER_1 = "Easy_Mock_Sensor_Handler1";
	public final static String ASSOC_EVENT_ID_MOCK_HANDLER_2 = "Easy_Mock_Sensor_Handler2";

	public EasyMockSensorHandler(String associatedEventID, TestableEventSynchro evtSync) {
		super(associatedEventID, evtSync);
	}
}