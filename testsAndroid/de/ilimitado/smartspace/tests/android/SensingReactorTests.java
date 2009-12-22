package de.ilimitado.smartspace.tests.android;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import android.test.AndroidTestCase;
import de.ilimitado.smartspace.EventSynchronizer;
import de.ilimitado.smartspace.SensingReactor;
import de.ilimitado.smartspace.SensorEvent;
import de.ilimitado.smartspace.registry.Registry;
import de.ilimitado.smartspace.sensor.sensor80211.ScanResult80211;
import de.ilimitado.smartspace.tests.junit.MockSensorHandler;
import de.ilimitado.smartspace.tests.junit.MockSensorHandler2;

public class SensingReactorTests extends AndroidTestCase{
	
	private SensingReactor sReactor;
	private LinkedBlockingQueue<SensorEvent<?>> systemRawDataQueue;
	private MockSensorHandler2 mockHandler1;
	private MockSensorHandler2 mockHandler2;
	private Thread sReactorThread;
	
	public void setUp() {
		Registry reg = new Registry();
		EventSynchronizer evtSync = new EventSynchronizer();
		systemRawDataQueue = new LinkedBlockingQueue<SensorEvent<?>>();
		sReactor = new SensingReactor(reg, systemRawDataQueue);
		mockHandler1 = new MockSensorHandler2(MockSensorHandler2.ASSOC_EVENT_ID_MOCK_HANDLER_1, evtSync);
		sReactor.registerHandler(MockSensorHandler.ASSOC_EVENT_ID_MOCK_HANDLER_1, 
								  mockHandler1);
		mockHandler2 = new MockSensorHandler2(MockSensorHandler2.ASSOC_EVENT_ID_MOCK_HANDLER_2, evtSync);
		sReactor.registerHandler(MockSensorHandler.ASSOC_EVENT_ID_MOCK_HANDLER_2, 
				  mockHandler2);
	}

	public void testEventDispatchingScanner80211() {
		List<ScanResult80211> apList = new ArrayList<ScanResult80211>(3);
		apList.add(new ScanResult80211("ap1", 
									   "00:00:00:00:01", 
									   "wpa2, wep", 
									   -80, 
									   2400));
		
		apList.add(new ScanResult80211("ap2", 
				   "00:00:00:00:02", 
				   "wep", 
				   -75, 
				   2410));
		
		apList.add(new ScanResult80211("ap3", 
				   "00:00:00:00:03", 
				   "wpa2", 
				   -65, 
				   2405));
		
		List<ScanResult80211> apList2 = new ArrayList<ScanResult80211>(3);
		apList.add(new ScanResult80211("ap1", 
									   "00:00:00:00:11", 
									   "wpa2, wep", 
									   -80, 
									   2400));
		
		apList.add(new ScanResult80211("ap2", 
				   "00:00:00:00:012", 
				   "wep", 
				   -75, 
				   2410));
		
		apList.add(new ScanResult80211("ap3", 
				   "00:00:00:00:013", 
				   "wpa2", 
				   -65, 
				   2405));
		
        systemRawDataQueue.add(new SensorEvent<ScanResult80211>(apList, MockSensorHandler.ASSOC_EVENT_ID_MOCK_HANDLER_1));
		systemRawDataQueue.add(new SensorEvent<ScanResult80211>(apList2, MockSensorHandler.ASSOC_EVENT_ID_MOCK_HANDLER_2));
		systemRawDataQueue.add(new SensorEvent<ScanResult80211>(apList2, MockSensorHandler.ASSOC_EVENT_ID_MOCK_HANDLER_2));
		sReactorThread = new Thread(sReactor);
		sReactor.startDispatching();
		sReactorThread.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
        assertEquals(0, systemRawDataQueue.size());
        sReactor.stopDispatching();
        sReactorThread.interrupt();
	}
}