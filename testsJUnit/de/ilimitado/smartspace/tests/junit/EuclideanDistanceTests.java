package de.ilimitado.smartspace.tests.junit;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import de.ilimitado.smartspace.IGeoPoint;
import de.ilimitado.smartspace.LFPT;
import de.ilimitado.smartspace.RTFPT;
import de.ilimitado.smartspace.ScanSampleList;
import de.ilimitado.smartspace.sensor.sensor80211.RSS;
import de.ilimitado.smartspace.sensor.sensor80211.ScanSample80211;
import de.ilimitado.smartspace.tests.junit.config.MockConfigTranslator;
import de.ilimitado.smartspace.utils.EuclideanDistance;

public class EuclideanDistanceTests extends TestCase {

	@Before()
	public void setUp() throws Exception {
		MockConfigTranslator.getInstance().translate();
	}

	@Test
	public void testEuclidianDistanceCalculation() {
		Double[] valuePoint1 = new Double[5];
		valuePoint1[0] = 10.0;
		valuePoint1[1] = 10.0;
		valuePoint1[2] = 10.0;
		valuePoint1[3] = 10.0;
		valuePoint1[4] = 10.0;

		Double[] valuePoint2 = new Double[5];
		valuePoint2[0] = 20.0;
		valuePoint2[1] = 20.0;
		valuePoint2[2] = 20.0;
		valuePoint2[3] = 20.0;
		valuePoint2[4] = 20.0;

		double distance;
		try {
			distance = EuclideanDistance.calculateDistance(valuePoint1, valuePoint2);
			double roundedDistance = Math.round(distance);
			double expectedDistance = Math.round(22.360679775);
			assertEquals(expectedDistance, roundedDistance);
		} catch (Exception e) {
			fail("No exception expected, but got: " + e.getMessage());
		}
	}

	@Test
	public void testEuclidianDistanceDifferentArraySizesException() {
		Double[] valuePoint1 = new Double[2];
		valuePoint1[0] = 10.0;
		valuePoint1[1] = 10.0;

		Double[] valuePoint2 = new Double[1];
		valuePoint2[0] = 20.0;

		try {
			EuclideanDistance.calculateDistance(valuePoint1, valuePoint2);
			fail("Illegal Argument Exception expected!");
		} catch (Exception e) {
		}
	}

	
	@Test
	public void testArrayLengthMatcherWithoutAPs() {
		RTFPT rtFPT = new RTFPT(123456789, 5);
		LFPT rmLFPT = new LFPT(123456789,5,new IGeoPoint(1,2));

		List<List<Double>> matchedArrays = EuclideanDistance.ArrayLengthMatcher.match(rtFPT, rmLFPT);
		
		assertEquals(2, matchedArrays.size());
		
		List<Double> rtData = matchedArrays.get(0);
		List<Double> LFPTData = matchedArrays.get(1);
		assertEquals(0, rtData.size());
		assertEquals(0, LFPTData.size());
	}

	@Test
	public void testArrayLengthMatcherWitDifferentAPsInRTsample() {
		RTFPT rtFPT = new RTFPT(123456789, 5);
		ScanSampleList rtSSplList = new ScanSampleList();
		ScanSample80211 sSpl1 = new ScanSample80211("res1", "00:00:00:00:01", new RSS(10), 2411);
		ScanSample80211 sSpl2 = new ScanSample80211("res2", "00:00:00:00:02", new RSS(20), 2412);
		ScanSample80211 sSpl3 = new ScanSample80211("res3", "00:00:00:00:03", new RSS(30), 2413);
		rtSSplList.add(sSpl1);
		rtSSplList.add(sSpl2);
		rtSSplList.add(sSpl3);
		rtFPT.add("TestList1", rtSSplList);
		
		LFPT rmLFPT = new LFPT(123456789,5,new IGeoPoint(1,2));
		ScanSampleList LFPTSSplList = new ScanSampleList();
		ScanSample80211 sSpl4 = new ScanSample80211("res1", "00:00:00:00:01", new RSS(40), 2414);
		ScanSample80211 sSpl5 = new ScanSample80211("res2", "00:00:00:00:02", new RSS(50), 2415);
		LFPTSSplList.add(sSpl4);
		LFPTSSplList.add(sSpl5);
		rmLFPT.add("TestList1", LFPTSSplList);

		List<List<Double>> matchedArrays = EuclideanDistance.ArrayLengthMatcher.match(rtFPT, rmLFPT);
		
		assertEquals(2, matchedArrays.size());
		
		List<Double> rtData = matchedArrays.get(0);
		List<Double> LFPTData = matchedArrays.get(1);
		assertEquals(12, rtData.size());
		assertEquals(12, LFPTData.size());
		
		double[] rtArray = covertToDoubleArray(rtData);
		double[] LFPTArray = covertToDoubleArray(LFPTData);

		double[] expectedRtArray = {10.0, 0.0, 0.0, 0.0, 
				 					20.0, 0.0, 0.0, 0.0, 
				 					30.0, 0.0, 0.0, 0.0}; 

		double[] expectedLFPTArray = {  40.0, 0.0, 0.0, 0.0, 
										50.0, 0.0, 0.0, 0.0, 
									  -120.0, 0.0, 0.0, 0.0};
		
		for (int i = 0; i < expectedLFPTArray.length; i++) {
			assertEquals(expectedLFPTArray[i], LFPTArray[i]);
		}
		
		for (int i = 0; i < expectedRtArray.length; i++) {
			assertEquals(expectedRtArray[i], rtArray[i]);
		}		
		
	}
	
	@Test
	public void testArrayLengthMatcherWithNoAPsInLFPT() {
		RTFPT rtFPT = new RTFPT(123456789, 5);
		ScanSampleList rtSSplList = new ScanSampleList();
		ScanSample80211 sSpl1 = new ScanSample80211("res1", "00:00:00:00:01", new RSS(10), 2411);
		ScanSample80211 sSpl2 = new ScanSample80211("res2", "00:00:00:00:02", new RSS(20), 2412);
		ScanSample80211 sSpl3 = new ScanSample80211("res3", "00:00:00:00:03", new RSS(30), 2413);
		rtSSplList.add(sSpl1);
		rtSSplList.add(sSpl2);
		rtSSplList.add(sSpl3);
		rtFPT.add("TestList1", rtSSplList);
		
		LFPT rmLFPT = new LFPT(123456789,5,new IGeoPoint(1,2));
		ScanSampleList LFPTSSplList = new ScanSampleList();
		rmLFPT.add("TestList1", LFPTSSplList);

		List<List<Double>> matchedArrays = EuclideanDistance.ArrayLengthMatcher.match(rtFPT, rmLFPT);
		
		assertEquals(2, matchedArrays.size());
		
		List<Double> rtData = matchedArrays.get(0);
		List<Double> LFPTData = matchedArrays.get(1);
		assertEquals(12, rtData.size());
		assertEquals(12, LFPTData.size());
		
		double[] rtArray = covertToDoubleArray(rtData);
		double[] LFPTArray = covertToDoubleArray(LFPTData);

		double[] expectedRtArray = {10.0, 0.0, 0.0, 0.0, 
				 					20.0, 0.0, 0.0, 0.0, 
				 					30.0, 0.0, 0.0, 0.0}; 

		double[] expectedLFPTArray = {-120.0, 0.0, 0.0, 0.0,
									  -120.0, 0.0, 0.0, 0.0,
									  -120.0, 0.0, 0.0, 0.0};
		
		for (int i = 0; i < expectedLFPTArray.length; i++) {
			assertEquals(expectedLFPTArray[i], LFPTArray[i]);
		}
		
		for (int i = 0; i < expectedRtArray.length; i++) {
			assertEquals(expectedRtArray[i], rtArray[i]);
		}		
		
	}

	@Test
	public void testArrayLengthMatcherWithNoAPsInRT() {
		RTFPT rtFPT = new RTFPT(123456789, 5);
		ScanSampleList rtSSplList = new ScanSampleList();
		rtFPT.add("TestList1", rtSSplList);
		
		LFPT rmLFPT = new LFPT(123456789,5,new IGeoPoint(1,2));
		ScanSampleList LFPTSSplList = new ScanSampleList();
		ScanSample80211 sSpl4 = new ScanSample80211("res4", "00:00:00:00:04", new RSS(40), 2414);
		ScanSample80211 sSpl5 = new ScanSample80211("res2", "00:00:00:00:02", new RSS(50), 2415);
		ScanSample80211 sSpl6 = new ScanSample80211("res6", "00:00:00:00:06", new RSS(60), 2416);
		LFPTSSplList.add(sSpl4);
		LFPTSSplList.add(sSpl5);
		LFPTSSplList.add(sSpl6);
		rmLFPT.add("TestList1", LFPTSSplList);

		List<List<Double>> matchedArrays = EuclideanDistance.ArrayLengthMatcher.match(rtFPT, rmLFPT);
		
		assertEquals(2, matchedArrays.size());
		
		List<Double> rtData = matchedArrays.get(0);
		List<Double> LFPTData = matchedArrays.get(1);
		assertEquals(12, rtData.size());
		assertEquals(12, LFPTData.size());
		
		double[] rtArray = covertToDoubleArray(rtData);
		double[] LFPTArray = covertToDoubleArray(LFPTData);

		double[] expectedRtArray = {-120.0, 0.0, 0.0, 0.0, 
									-120.0, 0.0, 0.0, 0.0,
				 				    -120.0, 0.0, 0.0, 0.0}; 

		double[] expectedLFPTArray = {  40.0, 0.0, 0.0, 0.0, 
										50.0, 0.0, 0.0, 0.0, 
									  	60.0, 0.0, 0.0, 0.0};
		
		for (int i = 0; i < expectedLFPTArray.length; i++) {
			assertEquals(expectedLFPTArray[i], LFPTArray[i]);
		}
		
		for (int i = 0; i < expectedRtArray.length; i++) {
			assertEquals(expectedRtArray[i], rtArray[i]);
		}		
		
	}

	@Test
	public void testArrayLengthMatcherWitDifferentAPsInEachFPT() {
		RTFPT rtFPT = new RTFPT(123456789, 5);
		ScanSampleList rtSSplList = new ScanSampleList();
		ScanSample80211 sSpl1 = new ScanSample80211("res1", "00:00:00:00:01", new RSS(10), 2411);
		ScanSample80211 sSpl2 = new ScanSample80211("res2", "00:00:00:00:02", new RSS(20), 2412);
		ScanSample80211 sSpl3 = new ScanSample80211("res3", "00:00:00:00:03", new RSS(30), 2413);
		rtSSplList.add(sSpl1);
		rtSSplList.add(sSpl2);
		rtSSplList.add(sSpl3);
		rtFPT.add("TestList1", rtSSplList);
		
		LFPT rmLFPT = new LFPT(123456789,5,new IGeoPoint(1,2));
		ScanSampleList LFPTSSplList = new ScanSampleList();
		ScanSample80211 sSpl4 = new ScanSample80211("res4", "00:00:00:00:04", new RSS(40), 2414);
		ScanSample80211 sSpl5 = new ScanSample80211("res2", "00:00:00:00:02", new RSS(50), 2415);
		ScanSample80211 sSpl6 = new ScanSample80211("res6", "00:00:00:00:06", new RSS(60), 2416);
		LFPTSSplList.add(sSpl4);
		LFPTSSplList.add(sSpl5);
		LFPTSSplList.add(sSpl6);
		rmLFPT.add("TestList1", LFPTSSplList);

		List<List<Double>> matchedArrays = EuclideanDistance.ArrayLengthMatcher.match(rtFPT, rmLFPT);
		
		assertEquals(2, matchedArrays.size());
		
		List<Double> rtData = matchedArrays.get(0);
		List<Double> LFPTData = matchedArrays.get(1);
		assertEquals(20, rtData.size());
		assertEquals(20, LFPTData.size());
		
		double[] rtArray = covertToDoubleArray(rtData);
		double[] LFPTArray = covertToDoubleArray(LFPTData);

		double[] expectedRtArray = {10.0, 0.0, 0.0, 0.0, 
				 					20.0, 0.0, 0.0, 0.0, 
				 					30.0, 0.0, 0.0, 0.0, 
				 				  -120.0, 0.0, 0.0, 0.0, 
				 				  -120.0, 0.0, 0.0, 0.0}; 

		double[] expectedLFPTArray = {-120.0, 0.0, 0.0, 0.0, 
										50.0, 0.0, 0.0, 0.0, 
									  -120.0, 0.0, 0.0, 0.0, 
									  	40.0, 0.0, 0.0, 0.0, 
									  	60.0, 0.0, 0.0, 0.0};
		
		for (int i = 0; i < expectedLFPTArray.length; i++) {
			assertEquals(expectedLFPTArray[i], LFPTArray[i]);
		}
		
		for (int i = 0; i < expectedRtArray.length; i++) {
			assertEquals(expectedRtArray[i], rtArray[i]);
		}		
		
	}

	@Test
	public void testArrayLengthMatcherWith2DifferentScanners() {
		RTFPT rtFPT = new RTFPT(123456789, 5);
		ScanSampleList rtSSplList = new ScanSampleList();
		ScanSample80211 sSpl1 = new ScanSample80211("res1", "00:00:00:00:01", new RSS(10), 2411);
		ScanSample80211 sSpl2 = new ScanSample80211("res2", "00:00:00:00:02", new RSS(20), 2412);
		ScanSample80211 sSpl3 = new ScanSample80211("res3", "00:00:00:00:03", new RSS(30), 2413);
		rtSSplList.add(sSpl1);
		rtSSplList.add(sSpl2);
		rtSSplList.add(sSpl3);
		rtFPT.add("TestScanner1", rtSSplList);
		rtSSplList = new ScanSampleList();
		sSpl1 = new ScanSample80211("resX1", "00:00:00:00:11", new RSS(100), 2411);
		sSpl2 = new ScanSample80211("resX2", "00:00:00:00:12", new RSS(200), 2412);
		sSpl3 = new ScanSample80211("resX3", "00:00:00:00:13", new RSS(300), 2413);
		rtSSplList.add(sSpl1);
		rtSSplList.add(sSpl2);
		rtSSplList.add(sSpl3);
		rtFPT.add("TestScanner2", rtSSplList);
		
		LFPT rmLFPT = new LFPT(123456789,5,new IGeoPoint(1,2));
		ScanSampleList LFPTSSplList = new ScanSampleList();
		ScanSample80211 sSpl4 = new ScanSample80211("res4", "00:00:00:00:04", new RSS(40), 2414);
		ScanSample80211 sSpl5 = new ScanSample80211("res2", "00:00:00:00:02", new RSS(50), 2415);
		ScanSample80211 sSpl6 = new ScanSample80211("res6", "00:00:00:00:06", new RSS(60), 2416);
		LFPTSSplList.add(sSpl4);
		LFPTSSplList.add(sSpl5);
		LFPTSSplList.add(sSpl6);
		rmLFPT.add("TestScanner1", LFPTSSplList);
		LFPTSSplList = new ScanSampleList();
		sSpl4 = new ScanSample80211("resX4", "00:00:00:00:14", new RSS(400), 2414);
		sSpl5 = new ScanSample80211("resX2", "00:00:00:00:12", new RSS(500), 2415);
		sSpl6 = new ScanSample80211("resX6", "00:00:00:00:16", new RSS(600), 2416);
		LFPTSSplList.add(sSpl4);
		LFPTSSplList.add(sSpl5);
		LFPTSSplList.add(sSpl6);
		rmLFPT.add("TestScanner2", LFPTSSplList);
		
		List<List<Double>> matchedArrays = EuclideanDistance.ArrayLengthMatcher.match(rtFPT, rmLFPT);
		
		assertEquals(2, matchedArrays.size());
		
		List<Double> rtData = matchedArrays.get(0);
		List<Double> LFPTData = matchedArrays.get(1);
		assertEquals(40, rtData.size());
		assertEquals(40, LFPTData.size());
		
		double[] rtArray = covertToDoubleArray(rtData);
		double[] LFPTArray = covertToDoubleArray(LFPTData);

		double[] expectedRtArray = {10.0, 0.0, 0.0, 0.0, 
				 					20.0, 0.0, 0.0, 0.0, 
				 					30.0, 0.0, 0.0, 0.0, 
				 				  -120.0, 0.0, 0.0, 0.0, 
				 				  -120.0, 0.0, 0.0, 0.0,
				 				   100.0, 0.0, 0.0, 0.0, 
				 				   200.0, 0.0, 0.0, 0.0, 
				 				   300.0, 0.0, 0.0, 0.0, 
				 				  -120.0, 0.0, 0.0, 0.0, 
				 				  -120.0, 0.0, 0.0, 0.0}; 

		double[] expectedLFPTArray = {-120.0, 0.0, 0.0, 0.0, 
										50.0, 0.0, 0.0, 0.0, 
									  -120.0, 0.0, 0.0, 0.0, 
									  	40.0, 0.0, 0.0, 0.0, 
									  	60.0, 0.0, 0.0, 0.0,
									  -120.0, 0.0, 0.0, 0.0, 
									   500.0, 0.0, 0.0, 0.0, 
									  -120.0, 0.0, 0.0, 0.0, 
									   400.0, 0.0, 0.0, 0.0, 
									   600.0, 0.0, 0.0, 0.0};
		
		for (int i = 0; i < expectedLFPTArray.length; i++) {
			assertEquals(expectedLFPTArray[i], LFPTArray[i]);
		}
		
		for (int i = 0; i < expectedRtArray.length; i++) {
			assertEquals(expectedRtArray[i], rtArray[i]);
		}		
		
	}

	
	//Copied from SimpleEuclideanDistanceProvider
	private double[] covertToDoubleArray(List<Double> dl){
		double[] values = new double[dl.size()];
		for (int i = 0; i < dl.size(); i++) {
			values[i] = dl.get(i);
		}
		return values;
	}
	
}
