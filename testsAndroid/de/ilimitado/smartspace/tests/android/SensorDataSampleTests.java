package de.ilimitado.smartspace.tests.android;

import junit.framework.TestCase;
import android.test.suitebuilder.annotation.SmallTest;
import de.ilimitado.smartspace.ScanSample;
import de.ilimitado.smartspace.ScanSampleList;
import de.ilimitado.smartspace.SensorDataSample;
import de.ilimitado.smartspace.persistance.ValueMap;
import de.ilimitado.smartspace.persistance.ValueMapContainer;
import de.ilimitado.smartspace.registry.ScanSampleProvider;
import de.ilimitado.smartspace.sensor.sensor80211.RSS;
import de.ilimitado.smartspace.sensor.sensor80211.ScanSample80211;
import de.ilimitado.smartspace.tests.junit.MockSample;
import de.ilimitado.smartspace.tests.junit.config.MockConfigTranslator;

public class SensorDataSampleTests extends TestCase{

	private static final long TIMESTAMP = 123456789;

	public void setUp() throws Exception {
		MockConfigTranslator.getInstance().translate();
	}

	@SmallTest
	public void testSensorDataSampleConstructor(){
		SensorDataSample sds = new SensorDataSample(TIMESTAMP){};
		assertEquals(TIMESTAMP, sds.getCreationTime());
	}
	
	@SmallTest
	public void testToValueWithoutScanSamples() {
		SensorDataSample sds = new SensorDataSample(TIMESTAMP){};
		ValueMapContainer values = sds.toValue();
		assertTrue("Values must contain default data value map", values.containsKey(SensorDataSample.VALUE_MAP_DEFAULT_DATA));
		
		ValueMap valueMap = (ValueMap) values.get(SensorDataSample.VALUE_MAP_DEFAULT_DATA);
		assertTrue("Values must contain timestamp", valueMap.containsKey(SensorDataSample.VALUE_CREATION_TIME));
		assertEquals(TIMESTAMP, valueMap.getAsLong(SensorDataSample.VALUE_CREATION_TIME));
	}
	
	@SmallTest
	public void testAddScanSampleList() {
		SensorDataSample sds = new SensorDataSample(TIMESTAMP){};
		ScanSampleList ssl = new ScanSampleList();
		ssl.add("MockSample", new MockSample());
		sds.add("MockSamples", ssl);
		ScanSampleList sslFromSDS = (ScanSampleList) sds.get("MockSamples");
		assertEquals(ssl, sslFromSDS);
	}
	
	@SmallTest
	public void testToValueWithAScanSampleList() {
		SensorDataSample sds = new SensorDataSample(TIMESTAMP){};
		ScanSampleList ssl = new ScanSampleList();
		ssl.add("MockSample", new MockSample());
		sds.add("MockSamples", ssl);
		ValueMapContainer values = sds.toValue();
		assertTrue("ValueMapContainer must contain MockSample Container", values.containsKey("MockSamples"));

		ValueMap valueMapDefaultData = (ValueMap) values.get(SensorDataSample.VALUE_MAP_DEFAULT_DATA);
		assertTrue("Values must contain timestamp", valueMapDefaultData.containsKey(SensorDataSample.VALUE_CREATION_TIME));
		assertEquals(TIMESTAMP, valueMapDefaultData.getAsLong(SensorDataSample.VALUE_CREATION_TIME));
		
		ValueMapContainer valueMapContainerMockSample = values.get("MockSamples");
		assertTrue(valueMapContainerMockSample.containsKey("MockSample"));
		
		ValueMap valueMapMockSample = (ValueMap) valueMapContainerMockSample.get("MockSample");
		assertTrue(valueMapMockSample.containsKey(MockSample.INTEGER_KEY));
		assertEquals(MockSample.INTEGER_VALUE, valueMapMockSample.getAsInteger(MockSample.INTEGER_KEY));
		assertTrue(valueMapMockSample.containsKey(MockSample.STRING_KEY));
		assertEquals(MockSample.STRING_VALUE, valueMapMockSample.getAsString(MockSample.STRING_KEY));
	}
	
	@SmallTest
	public void testFromValueWithoutScanSampleList() {
		SensorDataSample sds = new SensorDataSample(TIMESTAMP){};
		ValueMapContainer values = sds.toValue();
		sds = new SensorDataSample(values, new ScanSampleProvider()) {};
		assertEquals(TIMESTAMP, sds.getCreationTime());
	}
	
	@SmallTest
	public void testFromValueWithoutAScanSampleList() {
		SensorDataSample sds = new SensorDataSample(TIMESTAMP){};
		ValueMapContainer values = sds.toValue();
		
		ValueMapContainer container = new ValueMapContainer();
		container.setKey("MockSample");
		ValueMap map = (ValueMap) new MockSample().toValue();
		container.put("MockSample1", map);
		values.put("MockSample", container);
		
		ScanSampleProvider sSP = new ScanSampleProvider();
		sSP.put("MockSample", MockSample.class);
		
		sds = new SensorDataSample(values, sSP) {};
		
		ScanSampleList ssl = (ScanSampleList) sds.get("MockSample");
		assertEquals(1, ssl.size());
		
		ScanSample sample = ssl.get(0);
		assertTrue(sample instanceof MockSample);
		assertEquals(MockSample.INTEGER_VALUE, ((MockSample) sample).integer);
		assertEquals(MockSample.STRING_VALUE, ((MockSample) sample).string);
		
		assertEquals(TIMESTAMP, sds.getCreationTime());
	}
	
	@SmallTest
	public void testMerge(){
		SensorDataSample sds1 = new SensorDataSample(123456789){};
		SensorDataSample sds2 = new SensorDataSample(987654321){};
		sds1.merge(sds2);
		assertEquals(987654321, sds1.getCreationTime());
		
		sds1 = new SensorDataSample(123456789){};
		sds2 = new SensorDataSample(987654321){};
		sds2.merge(sds1);
		assertEquals(987654321, sds2.getCreationTime());
	}

	@SmallTest
	public void testMergeWithSamples(){
		SensorDataSample sds1 = new SensorDataSample(123456789){};
		SensorDataSample sds2 = new SensorDataSample(987654321){};
		
		ScanSample80211 sSpl1 = new ScanSample80211("res1", "00:00:00:00:01", new RSS(10), 2460);
		ScanSample80211 sSpl2 = new ScanSample80211("res2", "00:00:00:00:02", new RSS(60), 2410);
		ScanSample80211 sSpl3 = new ScanSample80211("res2", "00:00:00:00:02", new RSS(20), 2420);
		ScanSample80211 sSpl4 = new ScanSample80211("res1", "00:00:00:00:01", new RSS(20), 2420);

		ScanSampleList sSplList1 = new ScanSampleList();
		sSplList1.add(sSpl1);
		sSplList1.add(sSpl2);
		
		ScanSampleList sSplList2 = new ScanSampleList();
		sSplList2.add(sSpl3);
		sSplList2.add(sSpl4);
		
		sds1.add("ScanSample80211", sSplList1);
		sds2.add("ScanSample80211", sSplList2);
		
		sds1.merge(sds2);
		assertEquals(987654321, sds1.getCreationTime());
		
		
		sSplList1 = (ScanSampleList) sds1.get("ScanSample80211");
		
		assertEquals(2, sSplList1.size());
		sSpl1 = (ScanSample80211) sSplList1.get(0);
		assertEquals("00:00:00:00:01", sSpl1.MAC);
		assertEquals("res1", sSpl1.SSID);
		assertEquals(2440, sSpl1.meanFrequency);
		assertEquals(15.0, sSpl1.rss.mean);
		assertEquals(0.0, sSpl1.rss.deviation);
		
		sSpl2 = (ScanSample80211) sSplList1.get(1);
		assertEquals("00:00:00:00:02", sSpl2.MAC);
		assertEquals("res2", sSpl2.SSID);
		assertEquals(2415, sSpl2.meanFrequency);
		assertEquals(40.0, sSpl2.rss.mean);
		assertEquals(0.0, sSpl2.rss.deviation);
		
	}
	
	@SmallTest
	public void testMergeWithSamplesOfDifferentScanners(){
		SensorDataSample sds1 = new SensorDataSample(123456789){};
		SensorDataSample sds2 = new SensorDataSample(987654321){};
		
		ScanSample80211 sSpl1 = new ScanSample80211("res1", "00:00:00:00:01", new RSS(10), 2460);
		ScanSample80211 sSpl2 = new ScanSample80211("res2", "00:00:00:00:02", new RSS(20), 2410);
		ScanSample80211 sSpl3 = new ScanSample80211("res2", "00:00:00:00:02", new RSS(30), 2420);
		ScanSample80211 sSpl4 = new ScanSample80211("res1", "00:00:00:00:01", new RSS(40), 2420);

		ScanSampleList sSplList1 = new ScanSampleList();
		sSplList1.add(sSpl1);
		sSplList1.add(sSpl2);
		
		ScanSampleList sSplList2 = new ScanSampleList();
		sSplList2.add(sSpl3);
		sSplList2.add(sSpl4);
		
		sds1.add("ScanSample80211", sSplList1);
		sds2.add("ScanSample80211Scanner2", sSplList2);
		
		sds1.merge(sds2);
		assertEquals(987654321, sds1.getCreationTime());
		
		
		sSplList1 = (ScanSampleList) sds1.get("ScanSample80211");
		assertEquals(2, sSplList1.size());
		
		sSpl1 = (ScanSample80211) sSplList1.get(0);
		assertEquals(10.0, sSpl1.rss.mean);
		sSpl2 = (ScanSample80211) sSplList1.get(1);
		assertEquals(20.0, sSpl2.rss.mean);
		
		sSplList2 = (ScanSampleList) sds1.get("ScanSample80211Scanner2");
		assertEquals(2, sSplList2.size());

		sSpl3 = (ScanSample80211) sSplList2.get(0);
		assertEquals(30.0, sSpl3.rss.mean);		
		sSpl4 = (ScanSample80211) sSplList2.get(1);
		assertEquals(40.0, sSpl4.rss.mean);
	}
}