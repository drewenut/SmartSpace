package de.ilimitado.smartspace.tests.junit;

import org.junit.Test;

import de.ilimitado.smartspace.sensor.sensor80211.ScanResult80211;

import junit.framework.TestCase;

public class ScanResult80211Tests extends TestCase{
	
	@Test
	public void testScanResultEqualsAnother() {
		ScanResult80211 scnRes1 = new ScanResult80211("sid", "00:00:00:00:01", "[wpa2]", -55, 2400);
		ScanResult80211 scnRes2 = new ScanResult80211("sid", "00:00:00:00:01", "[wpa2]", -55, 2400);
		
		assertTrue(scnRes1.equals(scnRes2));
		assertTrue(scnRes2.equals(scnRes1));
	}
	
	@Test
	public void testScanResultSetUpInvariant() {
		ScanResult80211 scnRes1 = new ScanResult80211("sid", "00:00:00:00:01", "[wpa2]", -55, 2400);
		
		assertEquals("sid", scnRes1.SSID);
		assertEquals("00:00:00:00:01", scnRes1.BSSID);
		assertEquals("[wpa2]", scnRes1.capabilities);
		assertEquals(-55, scnRes1.level);
		assertEquals(2400, scnRes1.frequency);
	}
}
