package de.ilimitado.smartspace.tests.android;

import junit.framework.TestCase;
import android.test.suitebuilder.annotation.SmallTest;
import de.ilimitado.smartspace.RSS;
import de.ilimitado.smartspace.persistance.ValueMap;
import de.ilimitado.smartspace.registry.RegistryProviderException;
import de.ilimitado.smartspace.sensor.sensor80211.ScanSampleGSM;
import de.ilimitado.smartspace.tests.junit.config.MockConfigTranslator;

public class ScanSample80211Tests extends TestCase{

	public void setUp() throws Exception {
		MockConfigTranslator.getInstance().translate();
	}

	@SmallTest
	public void testScanSample80211() {
		ScanSampleGSM sSpl = new ScanSampleGSM("res1", "00:00:00:00:01", new RSS(10), 2410);
		assertEquals("00:00:00:00:01", sSpl.MAC);
		assertEquals("res1", sSpl.SSID);
		assertEquals(2410, sSpl.meanFrequency);
		assertEquals(10.0, sSpl.rss.mean);
	}

	@SmallTest
	public void testEquals() {
		ScanSampleGSM sSpl1 = new ScanSampleGSM("res1", "00:00:00:00:01", new RSS(10), 2410);
		ScanSampleGSM sSpl2 = new ScanSampleGSM("res2", "00:00:00:00:02", new RSS(10), 2410);
		ScanSampleGSM sSpl3 = new ScanSampleGSM("res3", "00:00:00:00:01", new RSS(10), 2410);
		assertTrue(sSpl1.equals(sSpl1));
		assertFalse(sSpl1.equals(sSpl2));
		assertFalse(sSpl1.equals(sSpl3));
	}

	@SmallTest
	public void testToValue() {
		ScanSampleGSM sSpl = new ScanSampleGSM("res1", "00:00:00:00:01", new RSS(10), 2410);
		ValueMap vm = (ValueMap) sSpl.toValue();
		
		assertTrue(vm.containsKey(ScanSampleGSM.VALUE_SSID));
		assertTrue(vm.containsKey(ScanSampleGSM.VALUE_MAC));
		assertTrue(vm.containsKey(ScanSampleGSM.VALUE_MEAN_FREQUENCY));
		assertEquals("res1", vm.getAsString(ScanSampleGSM.VALUE_SSID));
		assertEquals("00:00:00:00:01", vm.getAsString(ScanSampleGSM.VALUE_MAC));
		assertEquals(2410, vm.getAsInteger(ScanSampleGSM.VALUE_MEAN_FREQUENCY));
		
		assertTrue(vm.containsKey(RSS.VALUE_RSS_MEAN));
		assertEquals(10.0, vm.getAsDouble(RSS.VALUE_RSS_MEAN));
	}

	@SmallTest
	public void testFromValue() throws RegistryProviderException {
		ScanSampleGSM sSpl = new ScanSampleGSM("res1", "00:00:00:00:01", new RSS(10), 2410);
		ValueMap values = (ValueMap) sSpl.toValue();
		sSpl = new ScanSampleGSM(null, null, null, 0); //Absichtlich "leeres" Objekt erzeugen
		sSpl.fromValue(values);
		assertEquals("00:00:00:00:01", sSpl.MAC);
		assertEquals("res1", sSpl.SSID);
		assertEquals(2410, sSpl.meanFrequency);
		assertEquals(10.0, sSpl.rss.mean);
	}
	
	@SmallTest
	public void testMergeable() {
		ScanSampleGSM sSpl1 = new ScanSampleGSM("res1", "00:00:00:00:01", new RSS(10), 2410);
		ScanSampleGSM sSpl2 = new ScanSampleGSM("res2", "00:00:00:00:02", new RSS(20), 2410);
		ScanSampleGSM sSpl3 = new ScanSampleGSM("res1", "00:00:00:00:01", new RSS(30), 2410);
		
		assertFalse(sSpl1.mergeable(sSpl2));
		assertFalse(sSpl2.mergeable(sSpl1));
		assertTrue(sSpl1.mergeable(sSpl3));
		assertTrue(sSpl3.mergeable(sSpl1));
	}
	
	@SmallTest
	public void testMerge() {
		ScanSampleGSM sSpl1 = new ScanSampleGSM("res1", "00:00:00:00:01", new RSS(10), 2410);
		ScanSampleGSM sSpl2 = new ScanSampleGSM("res1", "00:00:00:00:01", new RSS(30), 2430);
		sSpl1.merge(sSpl2);
		
		assertEquals("00:00:00:00:01", sSpl1.MAC);
		assertEquals("res1", sSpl1.SSID);
		assertEquals(2420, sSpl1.meanFrequency);
		assertEquals(20.0, sSpl1.rss.mean);
		assertEquals(0.0, sSpl1.rss.deviation);
	}
	
}
