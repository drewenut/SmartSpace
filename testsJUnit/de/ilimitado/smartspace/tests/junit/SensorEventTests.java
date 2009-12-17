package de.ilimitado.smartspace.tests.junit;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import de.ilimitado.smartspace.SensorEvent;
import de.ilimitado.smartspace.config.Configuration;
import de.ilimitado.smartspace.sensor.sensor80211.ScanResultGSM;
import de.ilimitado.smartspace.tests.junit.config.MockConfigTranslator;

public class SensorEventTests extends TestCase{
	
	private String SENSOR_80211_AP_SCAN_EVENT_ID;
	private ArrayList<ScanResultGSM> apList;

	@Before
	public void setUp() {
		MockConfigTranslator.getInstance().translate();
		SENSOR_80211_AP_SCAN_EVENT_ID = Configuration.getInstance().sensor80211.scanner80211.ID;
		apList = new ArrayList<ScanResultGSM>(3);
		apList.add(new ScanResultGSM("ap1", 
									   "00:00:00:00:01", 
									   "wpa2, wep", 
									   -80, 
									   2400));
	}
	
	@Test
	public void testSensorEvent() {
		SensorEvent<ScanResultGSM> sensorEvent = new SensorEvent<ScanResultGSM>(apList, SENSOR_80211_AP_SCAN_EVENT_ID);
		assertEquals(SENSOR_80211_AP_SCAN_EVENT_ID, sensorEvent.getEventType());
		assertEquals(apList, sensorEvent.getEventHandle());
	}
}

