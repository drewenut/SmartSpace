package de.ilimitado.smartspace.tests.android;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import de.ilimitado.smartspace.IGeoPoint;
import de.ilimitado.smartspace.LFPT;
import de.ilimitado.smartspace.RadioMap;
import de.ilimitado.smartspace.ScanSampleList;
import de.ilimitado.smartspace.android.AndroidLocalDBPersistanceStrategy;
import de.ilimitado.smartspace.android.LFPTDBPersistance;
import de.ilimitado.smartspace.config.Configuration;
import de.ilimitado.smartspace.persistance.PersistanceManager;
import de.ilimitado.smartspace.persistance.ScanSampleDBPersistanceProvider;
import de.ilimitado.smartspace.sensor.sensor80211.RSS;
import de.ilimitado.smartspace.sensor.sensor80211.ScanSample80211;
import de.ilimitado.smartspace.sensor.sensor80211.ScanSample80211DBPersistance;
import de.ilimitado.smartspace.tests.junit.config.MockConfigTranslator;

public class PersistanceManagerTest extends AndroidTestCase{

	private PersistanceManager persMngr;
	private int bufferSize;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		MockConfigTranslator.getInstance().translate();
		ScanSampleDBPersistanceProvider sSplDBPersProvider = new ScanSampleDBPersistanceProvider();
		sSplDBPersProvider.putItem(Configuration.getInstance().sensor80211.scanner80211.ID, ScanSample80211DBPersistance.class);
		sSplDBPersProvider.putItem(LFPT.VALUE_MAP_DEFAULT_DATA, LFPTDBPersistance.class);
		AndroidLocalDBPersistanceStrategy persStrat = new AndroidLocalDBPersistanceStrategy(sSplDBPersProvider, getContext());
		persMngr = new PersistanceManager();
		persMngr.setStrategy(PersistanceManager.GATEWAY_LFPT, persStrat);
		bufferSize = Configuration.getInstance().persistence.bufferSize;
		persMngr.startPersistance(PersistanceManager.GATEWAY_LFPT);
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		persMngr.stopPersistance(PersistanceManager.GATEWAY_LFPT);
		getContext().deleteDatabase(Configuration.getInstance().persistence.dbName);
	}
	
	@MediumTest
	public void testSave() throws InterruptedException {
		LFPT lfptToSave = new LFPT(123456789, 95, new IGeoPoint(10,20));		
		for(int i=0; i<=bufferSize; i++)
			persMngr.save(PersistanceManager.GATEWAY_LFPT, lfptToSave);
		Thread.sleep(1000);
		
		persMngr.load(PersistanceManager.GATEWAY_LFPT, "");
		RadioMap radioMap = (RadioMap) persMngr.get(PersistanceManager.GATEWAY_LFPT);
		
		assertEquals(1, radioMap.size());
		LFPT lfpt = radioMap.get(0);
		assertEquals(123456789, lfpt.getCreationTime());
		assertEquals(10, lfpt.getIndoorGP().position_x);
		assertEquals(20, lfpt.getIndoorGP().position_y);
		assertEquals(90, lfpt.getQuantizedOrientation());
	}
	
	@MediumTest
	public void testSaveWithScanSample() throws InterruptedException {
		for(int i=0; i<=bufferSize; i++){
			LFPT lfptToSave = new LFPT(123456789, 95, new IGeoPoint(3,7));
			ScanSample80211 scnSpl1 = new ScanSample80211("spl1", "00:00:00:00:01", new RSS(10,20,30,40), 1010);
			ScanSampleList sSList = new ScanSampleList();
			sSList.add(scnSpl1);
			lfptToSave.add(Configuration.getInstance().sensor80211.scanner80211.ID, sSList);
			persMngr.save(PersistanceManager.GATEWAY_LFPT, lfptToSave);
		}
		Thread.sleep(1000);
		
		persMngr.load(PersistanceManager.GATEWAY_LFPT, "");
		RadioMap radioMap = (RadioMap) persMngr.get(PersistanceManager.GATEWAY_LFPT);
		
		assertEquals(1, radioMap.size());
		LFPT lfpt = radioMap.get(0);
		assertEquals(123456789, lfpt.getCreationTime());
		assertEquals(3, lfpt.getIndoorGP().position_x);
		assertEquals(7, lfpt.getIndoorGP().position_y);
		assertEquals(90, lfpt.getQuantizedOrientation());
		
		ScanSampleList sSList = (ScanSampleList) lfpt.get(Configuration.getInstance().sensor80211.scanner80211.ID);
		assertEquals(1, sSList.size());
		ScanSample80211 scnSpl1 = (ScanSample80211) sSList.get(0);
		
		assertEquals("spl1", scnSpl1.SSID);
		assertEquals("00:00:00:00:01", scnSpl1.MAC);
		assertEquals(1010, scnSpl1.meanFrequency);
		assertEquals(10.0, scnSpl1.rss.mean);
		assertEquals(20.0, scnSpl1.rss.variance);
		assertEquals(30.0, scnSpl1.rss.deviation);
		assertEquals(40.0, scnSpl1.rss.ssd);
	}

	@MediumTest
	public void testSaveWith2ScanSamples() throws InterruptedException {
		
		for(int i=0; i<=bufferSize; i++){
			LFPT lfptToSave = new LFPT(123456789, 95, new IGeoPoint(3,7));
			ScanSample80211 scnSpl1 = new ScanSample80211("spl1", "00:00:00:00:01", new RSS(11,21,31,41), 1010);
			ScanSample80211 scnSpl2 = new ScanSample80211("spl2", "00:00:00:00:02", new RSS(12,22,32,42), 2020);
			ScanSampleList sSList = new ScanSampleList();
			sSList.add(scnSpl1);
			sSList.add(scnSpl2);
			lfptToSave.add(Configuration.getInstance().sensor80211.scanner80211.ID, sSList);
			persMngr.save(PersistanceManager.GATEWAY_LFPT, lfptToSave);
		}
		Thread.sleep(1000);
		
		persMngr.load(PersistanceManager.GATEWAY_LFPT, "");
		RadioMap radioMap = (RadioMap) persMngr.get(PersistanceManager.GATEWAY_LFPT);
		
		assertEquals(1, radioMap.size());
		LFPT lfpt = radioMap.get(0);
		assertEquals(123456789, lfpt.getCreationTime());
		assertEquals(3, lfpt.getIndoorGP().position_x);
		assertEquals(7, lfpt.getIndoorGP().position_y);
		assertEquals(90, lfpt.getQuantizedOrientation());
		
		ScanSampleList sSList = (ScanSampleList) lfpt.get(Configuration.getInstance().sensor80211.scanner80211.ID);
		assertEquals(2, sSList.size());
		
		ScanSample80211 scnSpl1 = (ScanSample80211) sSList.get(0);
		assertEquals("spl1", scnSpl1.SSID);
		assertEquals("00:00:00:00:01", scnSpl1.MAC);
		assertEquals(1010, scnSpl1.meanFrequency);
		assertEquals(11.0, scnSpl1.rss.mean);
		assertEquals(21.0, scnSpl1.rss.variance);
		assertEquals(31.0, scnSpl1.rss.deviation);
		assertEquals(41.0, scnSpl1.rss.ssd);
		
		ScanSample80211 scnSpl2 = (ScanSample80211) sSList.get(1);
		assertEquals("spl2", scnSpl2.SSID);
		assertEquals("00:00:00:00:02", scnSpl2.MAC);
		assertEquals(2020, scnSpl2.meanFrequency);
		assertEquals(12.0, scnSpl2.rss.mean);
		assertEquals(22.0, scnSpl2.rss.variance);
		assertEquals(32.0, scnSpl2.rss.deviation);
		assertEquals(42.0, scnSpl2.rss.ssd);
		
	}	
	
	@MediumTest
	public void testSaveWith2LFPTs() throws InterruptedException {
		LFPT fingerprint1 = new LFPT(123456789, 95, new IGeoPoint(10,20));
		LFPT fingerprint2 = new LFPT(987654321, 185, new IGeoPoint(30,50));
		
		for(int i=0; i<=bufferSize; i++)
			persMngr.save(PersistanceManager.GATEWAY_LFPT, fingerprint1);
		Thread.sleep(1000);
		for(int i=0; i<=bufferSize; i++)
			persMngr.save(PersistanceManager.GATEWAY_LFPT, fingerprint2);
		Thread.sleep(1000);
		
		persMngr.load(PersistanceManager.GATEWAY_LFPT, "");
		RadioMap radioMap = (RadioMap) persMngr.get(PersistanceManager.GATEWAY_LFPT);
		assertEquals(2, radioMap.size());
		
		LFPT lfpt1 = radioMap.get(0);
		assertEquals(123456789, lfpt1.getCreationTime());
		assertEquals(10, lfpt1.getIndoorGP().position_x);
		assertEquals(20, lfpt1.getIndoorGP().position_y);
		assertEquals(90, lfpt1.getQuantizedOrientation());
		
		LFPT lfpt2 = radioMap.get(1);
		assertEquals(987654321, lfpt2.getCreationTime());
		assertEquals(30, lfpt2.getIndoorGP().position_x);
		assertEquals(50, lfpt2.getIndoorGP().position_y);
		assertEquals(180, lfpt2.getQuantizedOrientation());
	}
	
	@MediumTest
	public void testSaveWith2LFPTsAnd2ScanSamples() throws InterruptedException {
		
		for(int i=0; i<=bufferSize; i++){
			LFPT lfptToSave1 = new LFPT(123456789, 95, new IGeoPoint(3,7));
			ScanSample80211 scnSpl1 = new ScanSample80211("spl1", "00:00:00:00:01", new RSS(11,21,31,41), 1010);
			ScanSample80211 scnSpl2 = new ScanSample80211("spl2", "00:00:00:00:02", new RSS(12,22,32,42), 2020);
			ScanSampleList sSList1 = new ScanSampleList();
			sSList1.add(scnSpl1);
			sSList1.add(scnSpl2);
			lfptToSave1.add(Configuration.getInstance().sensor80211.scanner80211.ID, sSList1);
			persMngr.save(PersistanceManager.GATEWAY_LFPT, lfptToSave1);
		}
		Thread.sleep(1000);
		
		for(int i=0; i<=bufferSize; i++){
			LFPT lfptToSave2 = new LFPT(987654321, 185, new IGeoPoint(30,50));
			ScanSample80211 scnSpl3 = new ScanSample80211("spl3", "00:00:00:00:03", new RSS(13,23,33,43), 3030);
			ScanSample80211 scnSpl4 = new ScanSample80211("spl4", "00:00:00:00:04", new RSS(14,24,34,44), 4040);
			ScanSampleList sSList2 = new ScanSampleList();
			sSList2.add(scnSpl3);
			sSList2.add(scnSpl4);
			lfptToSave2.add(Configuration.getInstance().sensor80211.scanner80211.ID, sSList2);
			persMngr.save(PersistanceManager.GATEWAY_LFPT, lfptToSave2);
		}
		Thread.sleep(1000);
		
		persMngr.load(PersistanceManager.GATEWAY_LFPT, "");
		RadioMap radioMap = (RadioMap) persMngr.get(PersistanceManager.GATEWAY_LFPT);
		
		assertEquals(2, radioMap.size());
		LFPT lfpt = radioMap.get(0);
		assertEquals(123456789, lfpt.getCreationTime());
		assertEquals(3, lfpt.getIndoorGP().position_x);
		assertEquals(7, lfpt.getIndoorGP().position_y);
		assertEquals(90, lfpt.getQuantizedOrientation());
		
		ScanSampleList sSList1 = (ScanSampleList) lfpt.get(Configuration.getInstance().sensor80211.scanner80211.ID);
		assertEquals(2, sSList1.size());
		
		ScanSample80211 scnSpl1 = (ScanSample80211) sSList1.get(0);
		assertEquals("spl1", scnSpl1.SSID);
		assertEquals("00:00:00:00:01", scnSpl1.MAC);
		assertEquals(1010, scnSpl1.meanFrequency);
		assertEquals(11.0, scnSpl1.rss.mean);
		assertEquals(21.0, scnSpl1.rss.variance);
		assertEquals(31.0, scnSpl1.rss.deviation);
		assertEquals(41.0, scnSpl1.rss.ssd);
		
		ScanSample80211 scnSpl2 = (ScanSample80211) sSList1.get(1);
		assertEquals("spl2", scnSpl2.SSID);
		assertEquals("00:00:00:00:02", scnSpl2.MAC);
		assertEquals(2020, scnSpl2.meanFrequency);
		assertEquals(12.0, scnSpl2.rss.mean);
		assertEquals(22.0, scnSpl2.rss.variance);
		assertEquals(32.0, scnSpl2.rss.deviation);
		assertEquals(42.0, scnSpl2.rss.ssd);
		
		
		lfpt = radioMap.get(1);
		assertEquals(987654321, lfpt.getCreationTime());
		assertEquals(30, lfpt.getIndoorGP().position_x);
		assertEquals(50, lfpt.getIndoorGP().position_y);
		assertEquals(180, lfpt.getQuantizedOrientation());
		
		ScanSampleList sSList2 = (ScanSampleList) lfpt.get(Configuration.getInstance().sensor80211.scanner80211.ID);
		assertEquals(2, sSList2.size());
		
		ScanSample80211 scnSpl3 = (ScanSample80211) sSList2.get(0);
		assertEquals("spl3", scnSpl3.SSID);
		assertEquals("00:00:00:00:03", scnSpl3.MAC);
		assertEquals(3030, scnSpl3.meanFrequency);
		assertEquals(13.0, scnSpl3.rss.mean);
		assertEquals(23.0, scnSpl3.rss.variance);
		assertEquals(33.0, scnSpl3.rss.deviation);
		assertEquals(43.0, scnSpl3.rss.ssd);
		
		ScanSample80211 scnSpl4 = (ScanSample80211) sSList2.get(1);
		assertEquals("spl4", scnSpl4.SSID);
		assertEquals("00:00:00:00:04", scnSpl4.MAC);
		assertEquals(4040, scnSpl4.meanFrequency);
		assertEquals(14.0, scnSpl4.rss.mean);
		assertEquals(24.0, scnSpl4.rss.variance);
		assertEquals(34.0, scnSpl4.rss.deviation);
		assertEquals(44.0, scnSpl4.rss.ssd);
	}	
}