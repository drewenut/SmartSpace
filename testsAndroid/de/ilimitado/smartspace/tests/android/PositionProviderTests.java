package de.ilimitado.smartspace.tests.android;


import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import android.test.suitebuilder.annotation.SmallTest;

import de.ilimitado.smartspace.LFPT;
import de.ilimitado.smartspace.RTFPT;
import de.ilimitado.smartspace.RadioMap;
import de.ilimitado.smartspace.ScanSampleList;
import de.ilimitado.smartspace.config.Configuration;
import de.ilimitado.smartspace.positioning.IGeoPoint;
import de.ilimitado.smartspace.positioning.SimpleEuclideanDistanceProvider;
import de.ilimitado.smartspace.positioning.WeightedIGeoPoint;
import de.ilimitado.smartspace.sensor.sensor80211.RSS;
import de.ilimitado.smartspace.sensor.sensor80211.ScanSample80211;
import de.ilimitado.smartspace.tests.junit.config.MockConfigTranslator;

public class PositionProviderTests extends TestCase {

	public void setUp() throws Exception {
		MockConfigTranslator.getInstance().translate();
	}
	
	@SmallTest
	public void testPositionProvider80211TestInstance() {
		RadioMap rM = createRadioMapMock();
		SimpleEuclideanDistanceProvider pProv = new SimpleEuclideanDistanceProvider(rM);
		
		ScanSampleList RTsSL1 = new ScanSampleList();

		ScanSample80211 ap1_1 = new ScanSample80211("ap1", "00:00:00:00:01", new RSS(-64), 2410);
		ScanSample80211 ap1_2 = new ScanSample80211("ap2", "00:00:00:00:02", new RSS(-60), 2420);
		ScanSample80211 ap1_3 = new ScanSample80211("ap3", "00:00:00:00:03", new RSS(-55), 2430);
		ScanSample80211 ap1_4 = new ScanSample80211("ap4", "00:00:00:00:04", new RSS(-80), 2440);
		
		RTsSL1.add(ap1_1);
		RTsSL1.add(ap1_2);
		RTsSL1.add(ap1_3);
		RTsSL1.add(ap1_4);
		
		RTFPT rtSample1 = new RTFPT(System.currentTimeMillis(), 270.0);
		rtSample1.add(Configuration.getInstance().sensor80211.scanner80211.ID, RTsSL1);
		pProv.calculatePosition(rtSample1);
		List<WeightedIGeoPoint> weightedPositions = pProv.getEstimatedPositions();
		
		assertEquals(new IGeoPoint(1, 1), weightedPositions.get(0).igp);
	}
	
	@SmallTest
	public void testPositionProvider80211TestOtherAP() {
		RadioMap rM = createRadioMapMock();
		SimpleEuclideanDistanceProvider pProv = new SimpleEuclideanDistanceProvider(rM);
		
		ScanSampleList RTsSL1 = new ScanSampleList();

		ScanSample80211 ap3_1 = new ScanSample80211("ap1", "00:00:00:00:01", new RSS(-54), 2400);
		ScanSample80211 ap3_2 = new ScanSample80211("ap2", "00:00:00:00:02", new RSS(-40), 2410);
		ScanSample80211 ap3_3 = new ScanSample80211("ap3", "00:00:00:00:03", new RSS(-35), 2420);
		ScanSample80211 ap3_4 = new ScanSample80211("ap4", "00:00:00:00:04", new RSS(-50), 2430);
		
		RTsSL1.add(ap3_1);
		RTsSL1.add(ap3_2);
		RTsSL1.add(ap3_3);
		RTsSL1.add(ap3_4);
		
		RTFPT rtSample1 = new RTFPT(System.currentTimeMillis(), 270.0);
		rtSample1.add(Configuration.getInstance().sensor80211.scanner80211.ID, RTsSL1);
		pProv.calculatePosition(rtSample1);
		List<WeightedIGeoPoint> weightedPositions = pProv.getEstimatedPositions();
		
		assertEquals(new IGeoPoint(1, 2), weightedPositions.get(0).igp);
	}
	
	@SmallTest
	public void testPositionProvider80211TestOneAPOnly() {
		RadioMap rM = createRadioMapMock();
		SimpleEuclideanDistanceProvider pProv = new SimpleEuclideanDistanceProvider(rM);
		
		ScanSampleList RTsSL1 = new ScanSampleList();

		ScanSample80211 ap3_3 = new ScanSample80211("ap3", "00:00:00:00:03", new RSS(-35), 2420);
		
		RTsSL1.add(ap3_3);
		
		RTFPT rtSample1 = new RTFPT(System.currentTimeMillis(), 270.0);
		rtSample1.add(Configuration.getInstance().sensor80211.scanner80211.ID, RTsSL1);
		pProv.calculatePosition(rtSample1);
		List<WeightedIGeoPoint> weightedPositions = pProv.getEstimatedPositions();
		
		assertEquals(new IGeoPoint(2, 2), weightedPositions.get(0).igp);
	}


	private RadioMap createRadioMapMock() {
		ScanSampleList sSL1 = new ScanSampleList();

		ScanSample80211 ap1_1 = new ScanSample80211("ap1", "00:00:00:00:01", new RSS(-64), 2410);
		ScanSample80211 ap1_2 = new ScanSample80211("ap2", "00:00:00:00:02", new RSS(-60), 2420);
		ScanSample80211 ap1_3 = new ScanSample80211("ap3", "00:00:00:00:03", new RSS(-55), 2430);
		ScanSample80211 ap1_4 = new ScanSample80211("ap4", "00:00:00:00:04", new RSS(-80), 2440);
		
		sSL1.add(ap1_1);
		sSL1.add(ap1_2);
		sSL1.add(ap1_3);
		sSL1.add(ap1_4);
		
		LFPT lfpt1 = new LFPT(12345, 270, new IGeoPoint(1,1));
		lfpt1.add(Configuration.getInstance().sensor80211.scanner80211.ID, sSL1);
		
		
		
		ScanSampleList sSL2 = new ScanSampleList();

		ScanSample80211 ap2_1 = new ScanSample80211("ap1", "00:00:00:00:01", new RSS(-24), 2400);
		ScanSample80211 ap2_2 = new ScanSample80211("ap3", "00:00:00:00:03", new RSS(-30), 2410);
		ScanSample80211 ap2_3 = new ScanSample80211("ap2", "00:00:00:00:02", new RSS(-15), 2420);
		ScanSample80211 ap2_4 = new ScanSample80211("ap4", "00:00:00:00:04", new RSS(-20), 2430);
		
		sSL2.add(ap2_1);
		sSL2.add(ap2_2);
		sSL2.add(ap2_3);
		sSL2.add(ap2_4);
		
		LFPT lfpt2 = new LFPT(22345, 270, new IGeoPoint(2,1));
		lfpt2.add(Configuration.getInstance().sensor80211.scanner80211.ID, sSL2);
		
		
		
		ScanSampleList sSL3 = new ScanSampleList();

		ScanSample80211 ap3_4 = new ScanSample80211("ap1", "00:00:00:00:01", new RSS(-54), 2400);
		ScanSample80211 ap3_1 = new ScanSample80211("ap2", "00:00:00:00:02", new RSS(-40), 2410);
		ScanSample80211 ap3_2 = new ScanSample80211("ap3", "00:00:00:00:03", new RSS(-35), 2420);
		ScanSample80211 ap3_5 = new ScanSample80211("ap4", "00:00:00:00:04", new RSS(-50), 2430);
		ScanSample80211 ap3_3 = new ScanSample80211("ap5", "00:00:00:00:05", new RSS(-89), 2430);
		
		sSL3.add(ap3_1);
		sSL3.add(ap3_2);
		sSL3.add(ap3_3);
		sSL3.add(ap3_4);
		sSL3.add(ap3_5);
		
		LFPT lfpt3 = new LFPT(32345, 270, new IGeoPoint(1,2));
		lfpt3.add(Configuration.getInstance().sensor80211.scanner80211.ID, sSL3);
		
		
		
		ScanSampleList sSL4 = new ScanSampleList();

		ScanSample80211 ap4_1 = new ScanSample80211("ap3", "00:00:00:00:03", new RSS(-90), 2400);
		ScanSample80211 ap4_2 = new ScanSample80211("ap2", "00:00:00:00:02", new RSS(-95), 2410);
		ScanSample80211 ap4_3 = new ScanSample80211("ap1", "00:00:00:00:01", new RSS(-70), 2420);
		ScanSample80211 ap4_4 = new ScanSample80211("ap4", "00:00:00:00:04", new RSS(-85), 2430);
		
		sSL4.add(ap4_1);
		sSL4.add(ap4_2);
		sSL4.add(ap4_3);
		sSL4.add(ap4_4);
		
		LFPT lfpt4 = new LFPT(42345, 270, new IGeoPoint(2,2));
		lfpt4.add(Configuration.getInstance().sensor80211.scanner80211.ID, sSL4);
		
		RadioMap rM = new RadioMap();
		
		rM.add(lfpt1);
		rM.add(lfpt2);
		rM.add(lfpt3);
		rM.add(lfpt4);
		
		return rM;
	}
}

