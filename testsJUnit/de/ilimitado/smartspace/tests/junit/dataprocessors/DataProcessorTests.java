package de.ilimitado.smartspace.tests.junit.dataprocessors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import de.ilimitado.smartspace.ScanSample;
import de.ilimitado.smartspace.ScanSampleList;
import de.ilimitado.smartspace.config.Configuration;
import de.ilimitado.smartspace.dataprocessors.DataCommandChain;
import de.ilimitado.smartspace.dataprocessors.DataProcessor;
import de.ilimitado.smartspace.dataprocessors.DataProcessorResolver;
import de.ilimitado.smartspace.registry.DataCommandProvider;
import de.ilimitado.smartspace.registry.Registry;
import de.ilimitado.smartspace.sensor.sensor80211.MeanCommand80211;
import de.ilimitado.smartspace.sensor.sensor80211.RSS;
import de.ilimitado.smartspace.sensor.sensor80211.ScanResult80211;
import de.ilimitado.smartspace.sensor.sensor80211.ScanSample80211;
import de.ilimitado.smartspace.tests.junit.config.MockConfigTranslator;

public class DataProcessorTests extends TestCase{
	private List<List<ScanResult80211>> data = new ArrayList<List<ScanResult80211>>();
	private Registry reg;
	
	@Before()
     public void setUp() {
		MockConfigTranslator.getInstance().translate();
     }

	@Test()
	 public void test80211MeanDataCmd() {
		List<ScanResult80211> set1 = new ArrayList<ScanResult80211>();
		set1.add(new ScanResult80211("res1", "00:00:00:00:01", "caps", 10, 2410));
		set1.add(new ScanResult80211("res2", "00:00:00:00:02", "caps", 10, 2420));
		set1.add(new ScanResult80211("res3", "00:00:00:00:03", "caps", 10, 2430));
		set1.add(new ScanResult80211("res4", "00:00:00:00:04", "caps", 10, 2440));
		
		List<ScanResult80211> set2 = new ArrayList<ScanResult80211>();
		set2.add(new ScanResult80211("res1", "00:00:00:00:01", "caps", 10, 2410));
		set2.add(new ScanResult80211("res2", "00:00:00:00:02", "caps", 10, 2420));
		set2.add(new ScanResult80211("res3", "00:00:00:00:03", "caps", 10, 2430));
		set2.add(new ScanResult80211("res4", "00:00:00:00:04", "caps", 10, 2440));
		
		data.add(set1);
		data.add(set2);
		
		ScanSampleList sSL = new ScanSampleList();
		MeanCommand80211 mCmd80211= new MeanCommand80211();
		mCmd80211.setStdIn(data);
		mCmd80211.setStdOut(sSL);
		mCmd80211.execute();
		assertEquals(4, sSL.size());

		ScanSample80211 sSpl1 = new ScanSample80211("res1", "00:00:00:00:01", new RSS(10), 2410);
		ScanSample80211 sSpl2 = new ScanSample80211("res2", "00:00:00:00:02", new RSS(10), 2420);
		ScanSample80211 sSpl3 = new ScanSample80211("res3", "00:00:00:00:03", new RSS(10), 2430);
		ScanSample80211 sSpl4 = new ScanSample80211("res4", "00:00:00:00:04", new RSS(10), 2440);
		
		assertEquals(true, sSL.contains(sSpl1));
		assertEquals(true, sSL.contains(sSpl2));
		assertEquals(true, sSL.contains(sSpl3));
		assertEquals(true, sSL.contains(sSpl4));
		
		for(ScanSample sampel : sSL){
			ScanSample80211 scnSpl = (ScanSample80211) sampel;
			assertEquals(10, scnSpl.rss.mean,0);
		}
	}
	
	@Test()
	 public void test80211MeanDataCmd2() {
		List<ScanResult80211> set1 = new ArrayList<ScanResult80211>();
		set1.add(new ScanResult80211("res1", "00:00:00:00:01", "caps", 10, 2410));
		set1.add(new ScanResult80211("res2", "00:00:00:00:02", "caps", 10, 2420));
		set1.add(new ScanResult80211("res3", "00:00:00:00:03", "caps", -10, 2430));
		set1.add(new ScanResult80211("res4", "00:00:00:00:04", "caps", 10, 2440));
		
		List<ScanResult80211> set2 = new ArrayList<ScanResult80211>();
		set2.add(new ScanResult80211("res1", "00:00:00:00:01", "caps", 10, 2410));
		set2.add(new ScanResult80211("res2", "00:00:00:00:02", "caps", 20, 2420));
		set2.add(new ScanResult80211("res3", "00:00:00:00:03", "caps", -30, 2430));
		set2.add(new ScanResult80211("res4", "00:00:00:00:04", "caps", 40, 2440));
		
		data.add(set1);
		data.add(set2);
		
		ScanSampleList sSL = new ScanSampleList();
		MeanCommand80211 mCmd80211= new MeanCommand80211();
		mCmd80211.setStdIn(data);
		mCmd80211.setStdOut(sSL);
		mCmd80211.execute();
		assertEquals(4, sSL.size());

		for(ScanSample sampel : sSL){
			ScanSample80211 scnSpl = (ScanSample80211) sampel;
			if(scnSpl.MAC.equals("00:00:00:00:01"))
				assertEquals(10.0, scnSpl.rss.mean);
			if(scnSpl.MAC.equals("00:00:00:00:02"))
				assertEquals(15.0, scnSpl.rss.mean);
			if(scnSpl.MAC.equals("00:00:00:00:03"))
				assertEquals(-20.0, scnSpl.rss.mean);
			if(scnSpl.MAC.equals("00:00:00:00:04"))
				assertEquals(25.0, scnSpl.rss.mean);
		}
	}
	
	@Test()
	 public void test80211MeanDataCmd3() {
		List<ScanResult80211> set1 = new ArrayList<ScanResult80211>();
		set1.add(new ScanResult80211("res1", "00:00:00:00:01", "caps", 10, 2410));
		set1.add(new ScanResult80211("res2", "00:00:00:00:02", "caps", 10, 2420));
		set1.add(new ScanResult80211("res3", "00:00:00:00:03", "caps", -10, 2430));
		
		
		List<ScanResult80211> set2 = new ArrayList<ScanResult80211>();
		set2.add(new ScanResult80211("res1", "00:00:00:00:01", "caps", 10, 2410));
		set2.add(new ScanResult80211("res2", "00:00:00:00:02", "caps", 20, 2420));
		set2.add(new ScanResult80211("res4", "00:00:00:00:04", "caps", 10, 2440));
		
		data.add(set1);
		data.add(set2);
		
		ScanSampleList sSL = new ScanSampleList();
		MeanCommand80211 mCmd80211= new MeanCommand80211();
		mCmd80211.setStdIn(data);
		mCmd80211.setStdOut(sSL);
		mCmd80211.execute();
		assertEquals(4, sSL.size());

		for(ScanSample sampel : sSL){
			ScanSample80211 scnSpl = (ScanSample80211) sampel;
			if(scnSpl.MAC.equals("00:00:00:00:01"))
				assertEquals(10.0, scnSpl.rss.mean);
			if(scnSpl.MAC.equals("00:00:00:00:02"))
				assertEquals(15.0, scnSpl.rss.mean);
			if(scnSpl.MAC.equals("00:00:00:00:03"))
				assertEquals(-10.0, scnSpl.rss.mean);
			if(scnSpl.MAC.equals("00:00:00:00:04"))
				assertEquals(10.0, scnSpl.rss.mean);
		}
	}
	
	@Test()
	 public void testDataCmdChain() {
		List<ScanResult80211> set1 = new ArrayList<ScanResult80211>();
		set1.add(new ScanResult80211("res1", "00:00:00:00:01", "caps", 10, 2410));
		set1.add(new ScanResult80211("res2", "00:00:00:00:02", "caps", 10, 2420));
		set1.add(new ScanResult80211("res3", "00:00:00:00:03", "caps", 10, 2430));
		set1.add(new ScanResult80211("res4", "00:00:00:00:04", "caps", 10, 2440));
		
		List<ScanResult80211> set2 = new ArrayList<ScanResult80211>();
		set2.add(new ScanResult80211("res1", "00:00:00:00:01", "caps", 10, 2410));
		set2.add(new ScanResult80211("res2", "00:00:00:00:02", "caps", 10, 2420));
		set2.add(new ScanResult80211("res3", "00:00:00:00:03", "caps", 10, 2430));
		set2.add(new ScanResult80211("res4", "00:00:00:00:04", "caps", 10, 2440));
		
		data.add(set1);
		data.add(set2);
		
		DataCommandChain<Collection<?>, ScanSampleList> dCC = new DataCommandChain<Collection<?>, ScanSampleList>();
		MeanCommand80211 mCmd80211= new MeanCommand80211();
		dCC.addCommand(mCmd80211);
		dCC.setStdIn(data);
		dCC.setStdOut(new ScanSampleList());
		dCC.executeCommands();
		ScanSampleList sSL = dCC.getStdOut();
		
		assertEquals(4, sSL.size());

		ScanSample80211 sSpl1 = new ScanSample80211("res1", "00:00:00:00:01", new RSS(10), 2410);
		ScanSample80211 sSpl2 = new ScanSample80211("res2", "00:00:00:00:02", new RSS(10), 2420);
		ScanSample80211 sSpl3 = new ScanSample80211("res3", "00:00:00:00:03", new RSS(10), 2430);
		ScanSample80211 sSpl4 = new ScanSample80211("res4", "00:00:00:00:04", new RSS(10), 2440);
		
		assertEquals(true, sSL.contains(sSpl1));
		assertEquals(true, sSL.contains(sSpl2));
		assertEquals(true, sSL.contains(sSpl3));
		assertEquals(true, sSL.contains(sSpl4));
		
		for(ScanSample sampel : sSL){
			ScanSample80211 scnSpl = (ScanSample80211) sampel;
			assertEquals(10.0, scnSpl.rss.mean);
		}
	}

	@Test()
	 public void testSensorDataProcessor() {
		List<ScanResult80211> set1 = new ArrayList<ScanResult80211>();
		set1.add(new ScanResult80211("res1", "00:00:00:00:01", "caps", 10, 2410));
		set1.add(new ScanResult80211("res2", "00:00:00:00:02", "caps", 10, 2420));
		set1.add(new ScanResult80211("res3", "00:00:00:00:03", "caps", 10, 2430));
		set1.add(new ScanResult80211("res4", "00:00:00:00:04", "caps", 10, 2440));
		
		List<ScanResult80211> set2 = new ArrayList<ScanResult80211>();
		set2.add(new ScanResult80211("res1", "00:00:00:00:01", "caps", 10, 2410));
		set2.add(new ScanResult80211("res2", "00:00:00:00:02", "caps", 10, 2420));
		set2.add(new ScanResult80211("res3", "00:00:00:00:03", "caps", 10, 2430));
		set2.add(new ScanResult80211("res4", "00:00:00:00:04", "caps", 10, 2440));
		
		data.add(set1);
		data.add(set2);
		
		DataCommandChain<Collection<?>, ScanSampleList> dCC = new DataCommandChain<Collection<?>, ScanSampleList>();
		MeanCommand80211 mCmd80211= new MeanCommand80211();
		dCC.addCommand(mCmd80211);
		
		DataProcessor<ScanSampleList> sDP = new DataProcessor<ScanSampleList>(ScanSampleList.class, dCC);
		
		sDP.readyProcessor(data);
		sDP.processData();
		ScanSampleList sSL = sDP.getProcessedData();
		
		assertEquals(4, sSL.size());

		ScanSample80211 sSpl1 = new ScanSample80211("res1", "00:00:00:00:01", new RSS(10), 2410);
		ScanSample80211 sSpl2 = new ScanSample80211("res2", "00:00:00:00:02", new RSS(10), 2420);
		ScanSample80211 sSpl3 = new ScanSample80211("res3", "00:00:00:00:03", new RSS(10), 2430);
		ScanSample80211 sSpl4 = new ScanSample80211("res4", "00:00:00:00:04", new RSS(10), 2440);
		
		assertEquals(true, sSL.contains(sSpl1));
		assertEquals(true, sSL.contains(sSpl2));
		assertEquals(true, sSL.contains(sSpl3));
		assertEquals(true, sSL.contains(sSpl4));
		
		for(ScanSample sampel : sSL){
			ScanSample80211 scnSpl = (ScanSample80211) sampel;
			assertEquals(10.0, scnSpl.rss.mean);
		}
		

	}
	
	@Test
	public void testDataProcessorResolverMeanCommand80211(){
		List<ScanResult80211> set1 = new ArrayList<ScanResult80211>();
		set1.add(new ScanResult80211("res1", "00:00:00:00:01", "caps", 10, 2410));
		set1.add(new ScanResult80211("res2", "00:00:00:00:02", "caps", 10, 2420));
		set1.add(new ScanResult80211("res3", "00:00:00:00:03", "caps", 10, 2430));
		set1.add(new ScanResult80211("res4", "00:00:00:00:04", "caps", 10, 2440));
		
		List<ScanResult80211> set2 = new ArrayList<ScanResult80211>();
		set2.add(new ScanResult80211("res1", "00:00:00:00:01", "caps", 10, 2410));
		set2.add(new ScanResult80211("res2", "00:00:00:00:02", "caps", 10, 2420));
		set2.add(new ScanResult80211("res3", "00:00:00:00:03", "caps", 10, 2430));
		set2.add(new ScanResult80211("res4", "00:00:00:00:04", "caps", 10, 2440));
		
		data.add(set1);
		data.add(set2);
		
		Registry reg = new Registry();
		DataCommandProvider dCP = (DataCommandProvider) reg.get(Registry.SENSOR_DATA_CMD_PROVIDER);
		if(Configuration.getInstance().sensorConfig80211.scanner80211PassiveConf.isActive) {
			dCP.putItem("MeanCommand80211", MeanCommand80211.class);
		}
		assertNotNull(dCP);
		HashMap<String, DataProcessor<ScanSampleList>> dataProcessors = DataProcessorResolver.getInstance(dCP).getDataProcessors();
		assertNotNull(dataProcessors);
	    assertTrue(!dataProcessors.isEmpty());
	    String scannerID = Configuration.getInstance().sensorConfig80211.scanner80211PassiveConf.ID;
	    assertTrue(dataProcessors.containsKey(scannerID));
	    
	    DataProcessor<ScanSampleList> sDP = dataProcessors.get(scannerID);
	    assertNotNull(sDP);
		
		sDP.readyProcessor(data);
		sDP.processData();
		ScanSampleList sSL = sDP.getProcessedData();
		
		assertEquals(4, sSL.size());

		ScanSample80211 sSpl1 = new ScanSample80211("res1", "00:00:00:00:01", new RSS(10), 2410);
		ScanSample80211 sSpl2 = new ScanSample80211("res2", "00:00:00:00:02", new RSS(10), 2420);
		ScanSample80211 sSpl3 = new ScanSample80211("res3", "00:00:00:00:03", new RSS(10), 2430);
		ScanSample80211 sSpl4 = new ScanSample80211("res4", "00:00:00:00:04", new RSS(10), 2440);
		
		assertEquals(true, sSL.contains(sSpl1));
		assertEquals(true, sSL.contains(sSpl2));
		assertEquals(true, sSL.contains(sSpl3));
		assertEquals(true, sSL.contains(sSpl4));
		
		for(ScanSample sampel : sSL){
			ScanSample80211 scnSpl = (ScanSample80211) sampel;
			assertEquals(10.0, scnSpl.rss.mean);
		}
	}
	
	@Test
	public void testMultithreadedCommandProcessingMeanCommand80211(){
		List<List<ScanResult80211>> data = new ArrayList<List<ScanResult80211>>();
		List<ScanResult80211> set1 = new ArrayList<ScanResult80211>();
		set1.add(new ScanResult80211("res1", "00:00:00:00:01", "caps", 10, 2410));
		set1.add(new ScanResult80211("res2", "00:00:00:00:02", "caps", 10, 2420));
		set1.add(new ScanResult80211("res3", "00:00:00:00:03", "caps", 10, 2430));
		set1.add(new ScanResult80211("res4", "00:00:00:00:04", "caps", 10, 2440));
		
		List<ScanResult80211> set2 = new ArrayList<ScanResult80211>();
		set2.add(new ScanResult80211("res1", "00:00:00:00:01", "caps", 10, 2410));
		set2.add(new ScanResult80211("res2", "00:00:00:00:02", "caps", 10, 2420));
		set2.add(new ScanResult80211("res3", "00:00:00:00:03", "caps", 10, 2430));
		set2.add(new ScanResult80211("res4", "00:00:00:00:04", "caps", 10, 2440));
		
		data.add(set1);
		data.add(set2);
		
		Registry reg = new Registry();
		DataCommandProvider dCP = (DataCommandProvider) reg.get(Registry.SENSOR_DATA_CMD_PROVIDER);
		if(Configuration.getInstance().sensorConfig80211.scanner80211PassiveConf.isActive) {
			dCP.putItem("MeanCommand80211", MeanCommand80211.class);
		}
		assertNotNull(dCP);
		HashMap<String, DataProcessor<ScanSampleList>> dataProcessors = DataProcessorResolver.getInstance(dCP).getDataProcessors();
		assertNotNull(dataProcessors);
	    assertTrue(!dataProcessors.isEmpty());
	    String scannerID = Configuration.getInstance().sensorConfig80211.scanner80211PassiveConf.ID;
	    assertTrue(dataProcessors.containsKey(scannerID));
	    	    
		ArrayList<ScanSampleList> resultSamples = new ArrayList<ScanSampleList>();
		
		ExecutorService es = Executors.newFixedThreadPool(5);
	    
		List<Future<DataProcessor<ScanSampleList>>> tasks = new ArrayList<Future<DataProcessor<ScanSampleList>>>();

	    for (DataProcessor<ScanSampleList> sDPr : dataProcessors.values()) {
	    	sDPr.readyProcessor(data);
	    	Future<DataProcessor<ScanSampleList>> future = es.submit(sDPr, sDPr);
		    tasks.add(future);
	    }
	    
	    try {
	      for (Future<DataProcessor<ScanSampleList>> future : tasks) {
	    	  DataProcessor<ScanSampleList> dataProcessorResult = future.get();
	    	  resultSamples.add(dataProcessorResult.getProcessedData());
	      }
	      es.shutdown();
	    } catch (ExecutionException e) {
	      throw new RuntimeException(e);
	    } catch (InterruptedException ie) {
	      throw new RuntimeException(ie);
	    }
	    
	    assertEquals(1, resultSamples.size());
	    ScanSampleList sSL = resultSamples.get(0);
	    assertNotNull(sSL);
		assertEquals(4, sSL.size());

		ScanSample80211 sSpl1 = new ScanSample80211("res1", "00:00:00:00:01", new RSS(10), 2410);
		ScanSample80211 sSpl2 = new ScanSample80211("res2", "00:00:00:00:02", new RSS(10), 2420);
		ScanSample80211 sSpl3 = new ScanSample80211("res3", "00:00:00:00:03", new RSS(10), 2430);
		ScanSample80211 sSpl4 = new ScanSample80211("res4", "00:00:00:00:04", new RSS(10), 2440);
		
		assertEquals(true, sSL.contains(sSpl1));
		assertEquals(true, sSL.contains(sSpl2));
		assertEquals(true, sSL.contains(sSpl3));
		assertEquals(true, sSL.contains(sSpl4));
		
		for(ScanSample sampel : sSL){
			ScanSample80211 scnSpl = (ScanSample80211) sampel;
			assertEquals(10.0, scnSpl.rss.mean);
		};
	}
}