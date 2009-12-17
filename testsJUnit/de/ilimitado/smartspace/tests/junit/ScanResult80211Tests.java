package de.ilimitado.smartspace.tests.junit;

import org.junit.Test;

import de.ilimitado.smartspace.sensor.sensor80211.ScanResultGSM;

import junit.framework.TestCase;

public class ScanResult80211Tests extends TestCase{
	
	@Test
	public void testScanResultEqualsAnother() {
		ScanResultGSM scnRes1 = new ScanResultGSM("sid", "00:00:00:00:01", "[wpa2]", -55, 2400);
		ScanResultGSM scnRes2 = new ScanResultGSM("sid", "00:00:00:00:01", "[wpa2]", -55, 2400);
		
		assertTrue(scnRes1.equals(scnRes2));
		assertTrue(scnRes2.equals(scnRes1));
	}
	
	@Test
	public void testScanResultSetUpInvariant() {
		ScanResultGSM scnRes1 = new ScanResultGSM("sid", "00:00:00:00:01", "[wpa2]", -55, 2400);
		
		assertEquals("sid", scnRes1.SSID);
		assertEquals("00:00:00:00:01", scnRes1.BSSID);
		assertEquals("[wpa2]", scnRes1.capabilities);
		assertEquals(-55, scnRes1.level);
		assertEquals(2400, scnRes1.frequency);
	}
}
