/**
 * 
 */
package de.ilimitado.smartspace.tests.android;

import java.util.Collections;

import junit.framework.TestCase;
import android.test.suitebuilder.annotation.SmallTest;
import de.ilimitado.smartspace.RSS;
import de.ilimitado.smartspace.ScanSample;
import de.ilimitado.smartspace.ScanSampleList;
import de.ilimitado.smartspace.persistance.ValueMap;
import de.ilimitado.smartspace.persistance.ValueMapContainer;
import de.ilimitado.smartspace.registry.ScanSampleProvider;
import de.ilimitado.smartspace.sensor.sensor80211.ScanSample80211;
import de.ilimitado.smartspace.tests.junit.MockSample;

public class ScanSampleListTests extends TestCase{

	@SmallTest
	public void testToValue() {
		ScanSampleList ssl = new ScanSampleList();
		ssl.add("MockSample", new MockSample());
		
		ValueMapContainer sslContainer = ssl.toValue();
		assertTrue(sslContainer.containsKey("MockSample"));
		
		ValueMap valueMapMockSample = (ValueMap) sslContainer.get("MockSample");
		assertTrue(valueMapMockSample.containsKey(MockSample.INTEGER_KEY));
		assertEquals(MockSample.INTEGER_VALUE, valueMapMockSample.getAsInteger(MockSample.INTEGER_KEY));
		assertTrue(valueMapMockSample.containsKey(MockSample.STRING_KEY));
		assertEquals(MockSample.STRING_VALUE, valueMapMockSample.getAsString(MockSample.STRING_KEY));
	}
	
	@SmallTest
	public void testFromValue() {
		ValueMapContainer container = new ValueMapContainer();
		container.setKey("MockSample");
		
		ValueMap map = (ValueMap) new MockSample().toValue();
		container.put("MockSample1", map);
		
		ScanSampleProvider sSP = new ScanSampleProvider();
		sSP.put("MockSample", MockSample.class);
		
		ScanSampleList ssl = new ScanSampleList(container,sSP);
		assertEquals(1, ssl.size());
		
		ScanSample sample = ssl.get(0);
		assertTrue(sample instanceof MockSample);
		assertEquals(MockSample.INTEGER_VALUE, ((MockSample) sample).integer);
		assertEquals(MockSample.STRING_VALUE, ((MockSample) sample).string);
	}

	@SmallTest
	public void testMerge(){
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
		
		sSplList1.merge(sSplList2);
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
	public void testMergeAndAddNew(){
		ScanSample80211 sSpl1 = new ScanSample80211("res1", "00:00:00:00:01", new RSS(10), 2460);
		ScanSample80211 sSpl2 = new ScanSample80211("res2", "00:00:00:00:02", new RSS(60), 2410);
		ScanSample80211 sSpl3 = new ScanSample80211("res2", "00:00:00:00:02", new RSS(20), 2420);
		ScanSample80211 sSpl4 = new ScanSample80211("res1", "00:00:00:00:01", new RSS(20), 2420);
		ScanSample80211 sSpl5 = new ScanSample80211("res3", "00:00:00:00:03", new RSS(30), 2430);

		ScanSampleList sSplList1 = new ScanSampleList();
		sSplList1.add(sSpl1);
		sSplList1.add(sSpl2);
		
		ScanSampleList sSplList2 = new ScanSampleList();
		sSplList2.add(sSpl3);
		sSplList2.add(sSpl4);
		sSplList2.add(sSpl5);
		
		sSplList1.merge(sSplList2);
		assertEquals(3, sSplList1.size());
		
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
		
		sSpl3 = (ScanSample80211) sSplList1.get(2);
		assertEquals("00:00:00:00:03", sSpl3.MAC);
		assertEquals("res3", sSpl3.SSID);
		assertEquals(2430, sSpl3.meanFrequency);
		assertEquals(30.0, sSpl3.rss.mean);
		assertEquals(0.0, sSpl3.rss.deviation);

	}	
	
	@SmallTest
	public void testMergeOnlyOne(){
		ScanSample80211 sSpl1 = new ScanSample80211("res1", "00:00:00:00:01", new RSS(10), 2460);
		ScanSample80211 sSpl2 = new ScanSample80211("res2", "00:00:00:00:02", new RSS(60), 2410);
		ScanSample80211 sSpl3 = new ScanSample80211("res2", "00:00:00:00:02", new RSS(20), 2420);

		ScanSampleList sSplList1 = new ScanSampleList();
		sSplList1.add(sSpl1);
		sSplList1.add(sSpl2);
		
		ScanSampleList sSplList2 = new ScanSampleList();
		sSplList2.add(sSpl3);
		
		sSplList1.merge(sSplList2);
		assertEquals(2, sSplList1.size());
		
		sSpl1 = (ScanSample80211) sSplList1.get(0);
		assertEquals("00:00:00:00:01", sSpl1.MAC);
		assertEquals("res1", sSpl1.SSID);
		assertEquals(2460, sSpl1.meanFrequency);
		assertEquals(10.0, sSpl1.rss.mean);
		assertEquals(0.0, sSpl1.rss.deviation);
		
		sSpl2 = (ScanSample80211) sSplList1.get(1);
		assertEquals("00:00:00:00:02", sSpl2.MAC);
		assertEquals("res2", sSpl2.SSID);
		assertEquals(2415, sSpl2.meanFrequency);
		assertEquals(40.0, sSpl2.rss.mean);
		assertEquals(0.0, sSpl2.rss.deviation);

	}
	
	@SmallTest
	public void testWith10EquivalentAPsInOrder(){
		ScanSampleList sSplList1 = new ScanSampleList();
		ScanSampleList sSplList2 = new ScanSampleList();
		for(int i=0; i<10; i++){
			sSplList1.add(new ScanSample80211("res"+i, "00:00:00:00:0"+i, new RSS(10*i), 2460+i));	
			sSplList2.add(new ScanSample80211("res"+i, "00:00:00:00:0"+i, new RSS(20*i), 2460+i));
		}
		
		sSplList1.merge(sSplList2);
		assertEquals(10, sSplList1.size());
		
		for(int i=0; i<10; i++){
			ScanSample80211 sSpl = (ScanSample80211) sSplList1.get(i);
			assertEquals("00:00:00:00:0"+i, sSpl.MAC);
			assertEquals("res"+i, sSpl.SSID);
			assertEquals(2460+i, sSpl.meanFrequency);
			assertEquals((10.0*i + 20*i)/2, sSpl.rss.mean);
			assertEquals(0.0, sSpl.rss.deviation);
		}
	}
	
	@SmallTest
	public void testWith10DifferentAPsInOrder(){
		ScanSampleList sSplList1 = new ScanSampleList();
		ScanSampleList sSplList2 = new ScanSampleList();
		for(int i=0; i<10; i++){
			sSplList1.add(new ScanSample80211("res"+i, "00:00:00:00:0"+i, new RSS(10*i), 2460+i));	
			sSplList2.add(new ScanSample80211("resXX"+i, "22:22:22:00:0"+i, new RSS(20*i), 2460+i));
		}
		
		sSplList1.merge(sSplList2);
		assertEquals(20, sSplList1.size());
		
		for(int i=0; i<10; i++){
			ScanSample80211 sSpl = (ScanSample80211) sSplList1.get(i);
			assertEquals("00:00:00:00:0"+i, sSpl.MAC);
			assertEquals("res"+i, sSpl.SSID);
			assertEquals(2460+i, sSpl.meanFrequency);
			assertEquals(10.0*i, sSpl.rss.mean);
			assertEquals(0.0, sSpl.rss.deviation);
		}
		for(int i=1; i<10; i++){
			ScanSample80211 sSpl = (ScanSample80211) sSplList1.get(i+10);
			assertEquals("22:22:22:00:0"+i, sSpl.MAC);
			assertEquals("resXX"+i, sSpl.SSID);
			assertEquals(2460+i, sSpl.meanFrequency);
			assertEquals(20.0*i, sSpl.rss.mean);
			assertEquals(0.0, sSpl.rss.deviation);
		}
	}

	@SmallTest
	public void testWith10EquivalentAPsInRandomOrder(){
		ScanSampleList sSplList1 = new ScanSampleList();
		ScanSampleList sSplList2 = new ScanSampleList();
		for(int i=0; i<10; i++){
			sSplList1.add(new ScanSample80211("res"+i, "00:00:00:00:0"+i, new RSS(10*i), 2460+i));	
			sSplList2.add(new ScanSample80211("res"+i, "00:00:00:00:0"+i, new RSS(20*i), 2460+i));
		}
		
		Collections.shuffle(sSplList2);
		
		sSplList1.merge(sSplList2);
		assertEquals(10, sSplList1.size());
		
		for(int i=0; i<10; i++){
			ScanSample80211 sSpl = (ScanSample80211) sSplList1.get(i);
			assertEquals("00:00:00:00:0"+i, sSpl.MAC);
			assertEquals("res"+i, sSpl.SSID);
			assertEquals(2460+i, sSpl.meanFrequency);
			assertEquals((10.0*i + 20*i)/2, sSpl.rss.mean);
			assertEquals(0.0, sSpl.rss.deviation);
		}
	}
	
	@SmallTest
	public void testWith8EquivalentAnd2DifferentAPsInRandomOrder(){
		ScanSampleList sSplList1 = new ScanSampleList();
		ScanSampleList sSplList2 = new ScanSampleList();
		for(int i=0; i<10; i++){
			sSplList1.add(new ScanSample80211("res"+i, "00:00:00:00:0"+i, new RSS(10*i), 2460+i));	
			sSplList2.add(new ScanSample80211("res"+i, "00:00:00:00:0"+i, new RSS(20*i), 2460+i));
		}
		
		sSplList1.remove(0);
		sSplList1.remove(5);
		
		sSplList2.remove(0);
		sSplList2.remove(5);
		sSplList2.add(new ScanSample80211("resXX1", "22:22:22:00:0", new RSS(40), 2460));
		sSplList2.add(new ScanSample80211("resXX2", "22:22:22:00:0", new RSS(60), 2460));
		
		Collections.shuffle(sSplList2);
		
		sSplList1.merge(sSplList2);
		assertEquals(10, sSplList1.size());
		
	}
	
	@SmallTest
	public void testWith8EquivalentAnd2DifferentAPsInRandomOrderMerge2With1(){
		ScanSampleList sSplList1 = new ScanSampleList();
		ScanSampleList sSplList2 = new ScanSampleList();
		for(int i=0; i<10; i++){
			sSplList1.add(new ScanSample80211("res"+i, "00:00:00:00:0"+i, new RSS(10*i), 2460+i));	
			sSplList2.add(new ScanSample80211("res"+i, "00:00:00:00:0"+i, new RSS(20*i), 2460+i));
		}
		
		sSplList2.remove(0);
		sSplList2.remove(5);
		sSplList2.add(new ScanSample80211("resXX1", "22:22:22:00:0", new RSS(40), 2460));
		sSplList2.add(new ScanSample80211("resXX2", "22:22:22:00:0", new RSS(60), 2460));
		
		Collections.shuffle(sSplList2);
		
		sSplList2.merge(sSplList1);
		assertEquals(12, sSplList2.size());
		
	}
	
	@SmallTest
	public void testMergeEquivalentLists(){
		ScanSampleList sSplList1 = new ScanSampleList();
		for(int i=0; i<10; i++){
			sSplList1.add(new ScanSample80211("res"+i, "00:00:00:00:0"+i, new RSS(10*i), 2460+i));	
		}
		
		sSplList1.merge(sSplList1);
		assertEquals(0, sSplList1.size());
	}
	
}
