package de.ilimitado.smartspace.tests.android;

import android.test.AndroidTestCase;
import de.ilimitado.smartspace.LFPT;
import de.ilimitado.smartspace.RSS;
import de.ilimitado.smartspace.RadioMap;
import de.ilimitado.smartspace.ScanSampleList;
import de.ilimitado.smartspace.android.AndroidLocalDBPersistanceStrategy;
import de.ilimitado.smartspace.android.LFPTDBPersistance;
import de.ilimitado.smartspace.config.Configuration;
import de.ilimitado.smartspace.persistance.ScanSampleDBPersistanceProvider;
import de.ilimitado.smartspace.positioning.IGeoPoint;
import de.ilimitado.smartspace.sensor.sensor80211.ScanSample80211;
import de.ilimitado.smartspace.sensor.sensor80211.ScanSample80211DBPersistance;
import de.ilimitado.smartspace.tests.junit.config.MockConfigTranslator;

public class AndroidLocalDBPersistanceStrategyTest extends AndroidTestCase{

	private AndroidLocalDBPersistanceStrategy persStrat;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		MockConfigTranslator.getInstance().translate();
		ScanSampleDBPersistanceProvider sSplDBPersProvider = new ScanSampleDBPersistanceProvider();
		sSplDBPersProvider.putItem(Configuration.getInstance().sensor80211.scanner80211.ID, ScanSample80211DBPersistance.class);
		sSplDBPersProvider.putItem(LFPT.VALUE_MAP_DEFAULT_DATA, LFPTDBPersistance.class);
		persStrat = new AndroidLocalDBPersistanceStrategy(sSplDBPersProvider, getContext());
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		getContext().deleteDatabase(Configuration.getInstance().persistence.dbName);
	}
	
	public void testSave() {
		LFPT fingerprint = new LFPT(123456789, 95, new IGeoPoint(10,20));
		persStrat.save(fingerprint);
		persStrat.loadRadioMap();
		RadioMap radioMap = persStrat.getRadioMap();
		
		assertEquals(1, radioMap.size());
		LFPT lfpt = radioMap.get(0);
		assertEquals(123456789, lfpt.getCreationTime());
		assertEquals(10, lfpt.getIndoorGP().position_x);
		assertEquals(20, lfpt.getIndoorGP().position_y);
		assertEquals(90, lfpt.getQuantizedOrientation());
	}
	
	public void testSaveWithScanSample() {
		LFPT lfptToSave = new LFPT(123456789, 95, new IGeoPoint(3,7));
		ScanSample80211 scnSpl1 = new ScanSample80211("spl1", "00:00:00:00:01", new RSS(10,20,30,40), 1010);
		ScanSampleList sSList = new ScanSampleList();
		sSList.add(scnSpl1);
		lfptToSave.add(Configuration.getInstance().sensor80211.scanner80211.ID, sSList);

		persStrat.save(lfptToSave);
		persStrat.loadRadioMap();
		RadioMap radioMap = persStrat.getRadioMap();
		
		assertEquals(1, radioMap.size());
		LFPT lfpt = radioMap.get(0);
		assertEquals(123456789, lfpt.getCreationTime());
		assertEquals(3, lfpt.getIndoorGP().position_x);
		assertEquals(7, lfpt.getIndoorGP().position_y);
		assertEquals(90, lfpt.getQuantizedOrientation());
		
		sSList = (ScanSampleList) lfpt.get(Configuration.getInstance().sensor80211.scanner80211.ID);
		assertEquals(1, sSList.size());
		scnSpl1 = (ScanSample80211) sSList.get(0);
		
		assertEquals("spl1", scnSpl1.SSID);
		assertEquals("00:00:00:00:01", scnSpl1.MAC);
		assertEquals(1010, scnSpl1.meanFrequency);
		assertEquals(10.0, scnSpl1.rss.mean);
		assertEquals(20.0, scnSpl1.rss.variance);
		assertEquals(30.0, scnSpl1.rss.deviation);
		assertEquals(40.0, scnSpl1.rss.ssd);
	}

	public void testSaveWith2ScanSamples() {
		LFPT lfptToSave = new LFPT(123456789, 95, new IGeoPoint(3,7));
		ScanSample80211 scnSpl1 = new ScanSample80211("spl1", "00:00:00:00:01", new RSS(11,21,31,41), 1010);
		ScanSample80211 scnSpl2 = new ScanSample80211("spl2", "00:00:00:00:02", new RSS(12,22,32,42), 2020);
		ScanSampleList sSList = new ScanSampleList();
		sSList.add(scnSpl1);
		sSList.add(scnSpl2);
		lfptToSave.add(Configuration.getInstance().sensor80211.scanner80211.ID, sSList);

		persStrat.save(lfptToSave);
		persStrat.loadRadioMap();
		RadioMap radioMap = persStrat.getRadioMap();
		
		assertEquals(1, radioMap.size());
		LFPT lfpt = radioMap.get(0);
		assertEquals(123456789, lfpt.getCreationTime());
		assertEquals(3, lfpt.getIndoorGP().position_x);
		assertEquals(7, lfpt.getIndoorGP().position_y);
		assertEquals(90, lfpt.getQuantizedOrientation());
		
		sSList = (ScanSampleList) lfpt.get(Configuration.getInstance().sensor80211.scanner80211.ID);
		assertEquals(2, sSList.size());
		
		scnSpl1 = (ScanSample80211) sSList.get(0);
		assertEquals("spl1", scnSpl1.SSID);
		assertEquals("00:00:00:00:01", scnSpl1.MAC);
		assertEquals(1010, scnSpl1.meanFrequency);
		assertEquals(11.0, scnSpl1.rss.mean);
		assertEquals(21.0, scnSpl1.rss.variance);
		assertEquals(31.0, scnSpl1.rss.deviation);
		assertEquals(41.0, scnSpl1.rss.ssd);
		
		scnSpl2 = (ScanSample80211) sSList.get(1);
		assertEquals("spl2", scnSpl2.SSID);
		assertEquals("00:00:00:00:02", scnSpl2.MAC);
		assertEquals(2020, scnSpl2.meanFrequency);
		assertEquals(12.0, scnSpl2.rss.mean);
		assertEquals(22.0, scnSpl2.rss.variance);
		assertEquals(32.0, scnSpl2.rss.deviation);
		assertEquals(42.0, scnSpl2.rss.ssd);
		
	}	
	
	public void testSaveWith2LFPTs() {
		LFPT fingerprint1 = new LFPT(123456789, 95, new IGeoPoint(10,20));
		LFPT fingerprint2 = new LFPT(987654321, 185, new IGeoPoint(30,50));
		persStrat.save(fingerprint1);
		persStrat.save(fingerprint2);
		
		persStrat.loadRadioMap();
		RadioMap radioMap = persStrat.getRadioMap();
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
	
	public void testSaveWith2LFPTsAnd2ScanSamples() {
		LFPT lfptToSave1 = new LFPT(123456789, 95, new IGeoPoint(3,7));
		ScanSample80211 scnSpl1 = new ScanSample80211("spl1", "00:00:00:00:01", new RSS(11,21,31,41), 1010);
		ScanSample80211 scnSpl2 = new ScanSample80211("spl2", "00:00:00:00:02", new RSS(12,22,32,42), 2020);
		ScanSampleList sSList1 = new ScanSampleList();
		sSList1.add(scnSpl1);
		sSList1.add(scnSpl2);
		lfptToSave1.add(Configuration.getInstance().sensor80211.scanner80211.ID, sSList1);
		
		LFPT lfptToSave2 = new LFPT(987654321, 185, new IGeoPoint(30,50));
		ScanSample80211 scnSpl3 = new ScanSample80211("spl3", "00:00:00:00:03", new RSS(13,23,33,43), 3030);
		ScanSample80211 scnSpl4 = new ScanSample80211("spl4", "00:00:00:00:04", new RSS(14,24,34,44), 4040);
		ScanSampleList sSList2 = new ScanSampleList();
		sSList2.add(scnSpl3);
		sSList2.add(scnSpl4);
		lfptToSave2.add(Configuration.getInstance().sensor80211.scanner80211.ID, sSList2);

		persStrat.save(lfptToSave1);
		persStrat.save(lfptToSave2);
		persStrat.loadRadioMap();
		RadioMap radioMap = persStrat.getRadioMap();
		
		assertEquals(2, radioMap.size());
		LFPT lfpt = radioMap.get(0);
		assertEquals(123456789, lfpt.getCreationTime());
		assertEquals(3, lfpt.getIndoorGP().position_x);
		assertEquals(7, lfpt.getIndoorGP().position_y);
		assertEquals(90, lfpt.getQuantizedOrientation());
		
		sSList1 = (ScanSampleList) lfpt.get(Configuration.getInstance().sensor80211.scanner80211.ID);
		assertEquals(2, sSList1.size());
		
		scnSpl1 = (ScanSample80211) sSList1.get(0);
		assertEquals("spl1", scnSpl1.SSID);
		assertEquals("00:00:00:00:01", scnSpl1.MAC);
		assertEquals(1010, scnSpl1.meanFrequency);
		assertEquals(11.0, scnSpl1.rss.mean);
		assertEquals(21.0, scnSpl1.rss.variance);
		assertEquals(31.0, scnSpl1.rss.deviation);
		assertEquals(41.0, scnSpl1.rss.ssd);
		
		scnSpl2 = (ScanSample80211) sSList1.get(1);
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
		
		sSList2 = (ScanSampleList) lfpt.get(Configuration.getInstance().sensor80211.scanner80211.ID);
		assertEquals(2, sSList2.size());
		
		scnSpl3 = (ScanSample80211) sSList2.get(0);
		assertEquals("spl3", scnSpl3.SSID);
		assertEquals("00:00:00:00:03", scnSpl3.MAC);
		assertEquals(3030, scnSpl3.meanFrequency);
		assertEquals(13.0, scnSpl3.rss.mean);
		assertEquals(23.0, scnSpl3.rss.variance);
		assertEquals(33.0, scnSpl3.rss.deviation);
		assertEquals(43.0, scnSpl3.rss.ssd);
		
		scnSpl4 = (ScanSample80211) sSList2.get(1);
		assertEquals("spl4", scnSpl4.SSID);
		assertEquals("00:00:00:00:04", scnSpl4.MAC);
		assertEquals(4040, scnSpl4.meanFrequency);
		assertEquals(14.0, scnSpl4.rss.mean);
		assertEquals(24.0, scnSpl4.rss.variance);
		assertEquals(34.0, scnSpl4.rss.deviation);
		assertEquals(44.0, scnSpl4.rss.ssd);
	}
	
	public void testSaveWith2LFPTsAnd2ScanSamplesAndSaveTheLFPTsTwice() {
		LFPT lfptToSave1 = new LFPT(123456789, 95, new IGeoPoint(3,7));
		ScanSample80211 scnSpl1 = new ScanSample80211("spl1", "00:00:00:00:01", new RSS(11,21,31,41), 1010);
		ScanSample80211 scnSpl2 = new ScanSample80211("spl2", "00:00:00:00:02", new RSS(12,22,32,42), 2020);
		ScanSampleList sSList1 = new ScanSampleList();
		sSList1.add(scnSpl1);
		sSList1.add(scnSpl2);
		lfptToSave1.add(Configuration.getInstance().sensor80211.scanner80211.ID, sSList1);
		
		LFPT lfptToSave2 = new LFPT(987654321, 185, new IGeoPoint(30,50));
		ScanSample80211 scnSpl3 = new ScanSample80211("spl3", "00:00:00:00:03", new RSS(13,23,33,43), 3030);
		ScanSample80211 scnSpl4 = new ScanSample80211("spl4", "00:00:00:00:04", new RSS(14,24,34,44), 4040);
		ScanSampleList sSList2 = new ScanSampleList();
		sSList2.add(scnSpl3);
		sSList2.add(scnSpl4);
		lfptToSave2.add(Configuration.getInstance().sensor80211.scanner80211.ID, sSList2);

		persStrat.save(lfptToSave1);
		persStrat.save(lfptToSave2);
		persStrat.save(lfptToSave1); //should trigger update and merge but change no data
		persStrat.save(lfptToSave2); //should trigger update and merge but change no data
		persStrat.loadRadioMap();
		RadioMap radioMap = persStrat.getRadioMap();
		
		assertEquals(2, radioMap.size());
		LFPT lfpt = radioMap.get(0);
		assertEquals(123456789, lfpt.getCreationTime());
		assertEquals(3, lfpt.getIndoorGP().position_x);
		assertEquals(7, lfpt.getIndoorGP().position_y);
		assertEquals(90, lfpt.getQuantizedOrientation());
		
		sSList1 = (ScanSampleList) lfpt.get(Configuration.getInstance().sensor80211.scanner80211.ID);
		assertEquals(2, sSList1.size());
		
		scnSpl1 = (ScanSample80211) sSList1.get(0);
		assertEquals("spl1", scnSpl1.SSID);
		assertEquals("00:00:00:00:01", scnSpl1.MAC);
		assertEquals(1010, scnSpl1.meanFrequency);
		assertEquals(11.0, scnSpl1.rss.mean);
		assertEquals(21.0, scnSpl1.rss.variance);
		assertEquals(31.0, scnSpl1.rss.deviation);
		assertEquals(41.0, scnSpl1.rss.ssd);
		
		scnSpl2 = (ScanSample80211) sSList1.get(1);
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
		
		sSList2 = (ScanSampleList) lfpt.get(Configuration.getInstance().sensor80211.scanner80211.ID);
		assertEquals(2, sSList2.size());
		
		scnSpl3 = (ScanSample80211) sSList2.get(0);
		assertEquals("spl3", scnSpl3.SSID);
		assertEquals("00:00:00:00:03", scnSpl3.MAC);
		assertEquals(3030, scnSpl3.meanFrequency);
		assertEquals(13.0, scnSpl3.rss.mean);
		assertEquals(23.0, scnSpl3.rss.variance);
		assertEquals(33.0, scnSpl3.rss.deviation);
		assertEquals(43.0, scnSpl3.rss.ssd);
		
		scnSpl4 = (ScanSample80211) sSList2.get(1);
		assertEquals("spl4", scnSpl4.SSID);
		assertEquals("00:00:00:00:04", scnSpl4.MAC);
		assertEquals(4040, scnSpl4.meanFrequency);
		assertEquals(14.0, scnSpl4.rss.mean);
		assertEquals(24.0, scnSpl4.rss.variance);
		assertEquals(34.0, scnSpl4.rss.deviation);
		assertEquals(44.0, scnSpl4.rss.ssd);
	}	

	public void testSaveWith2LFPTsForSamePositionAndSameAPs() {
		LFPT lfptToSave1 = new LFPT(123456789, 195, new IGeoPoint(3,7));
		ScanSample80211 scnSpl1 = new ScanSample80211("spl1", "00:00:00:00:01", new RSS(11,11,11,11), 1111);
		ScanSample80211 scnSpl2 = new ScanSample80211("spl2", "00:00:00:00:02", new RSS(12,12,12,12), 1212);
		ScanSampleList sSList1 = new ScanSampleList();
		sSList1.add(scnSpl1);
		sSList1.add(scnSpl2);
		lfptToSave1.add(Configuration.getInstance().sensor80211.scanner80211.ID, sSList1);
		
		LFPT lfptToSave2 = new LFPT(987654321, 185, new IGeoPoint(3,7));
		ScanSample80211 scnSpl3 = new ScanSample80211("spl2", "00:00:00:00:02", new RSS(22,22,22,22), 2222);
		ScanSample80211 scnSpl4 = new ScanSample80211("spl1", "00:00:00:00:01", new RSS(21,21,21,21), 2121);
		ScanSampleList sSList2 = new ScanSampleList();
		sSList2.add(scnSpl3);
		sSList2.add(scnSpl4);
		lfptToSave2.add(Configuration.getInstance().sensor80211.scanner80211.ID, sSList2);

		persStrat.save(lfptToSave1);
		persStrat.save(lfptToSave2); //should trigger update and merge and also change data
		persStrat.loadRadioMap();
		RadioMap radioMap = persStrat.getRadioMap();
		
		assertEquals(1, radioMap.size());
		LFPT lfpt = radioMap.get(0);
		assertEquals(987654321, lfpt.getCreationTime());
		assertEquals(3, lfpt.getIndoorGP().position_x);
		assertEquals(7, lfpt.getIndoorGP().position_y);
		assertEquals(180, lfpt.getQuantizedOrientation());
		
		sSList1 = (ScanSampleList) lfpt.get(Configuration.getInstance().sensor80211.scanner80211.ID);
		assertEquals(2, sSList1.size());
		
		scnSpl1 = (ScanSample80211) sSList1.get(0);
		assertEquals("spl1", scnSpl1.SSID);
		assertEquals("00:00:00:00:01", scnSpl1.MAC);
		assertEquals(1616, scnSpl1.meanFrequency);
		assertEquals(16.0, scnSpl1.rss.mean);
		assertEquals(16.0, scnSpl1.rss.variance);
		assertEquals(16.0, scnSpl1.rss.deviation);
		assertEquals(16.0, scnSpl1.rss.ssd);
		
		scnSpl2 = (ScanSample80211) sSList1.get(1);
		assertEquals("spl2", scnSpl2.SSID);
		assertEquals("00:00:00:00:02", scnSpl2.MAC);
		assertEquals(1717, scnSpl2.meanFrequency);
		assertEquals(17.0, scnSpl2.rss.mean);
		assertEquals(17.0, scnSpl2.rss.variance);
		assertEquals(17.0, scnSpl2.rss.deviation);
		assertEquals(17.0, scnSpl2.rss.ssd);
		
	}	

	public void testSaveWith2LFPTsForSamePositionAndOneDifferentAP() {
		LFPT lfptToSave1 = new LFPT(123456789, 195, new IGeoPoint(3,7));
		ScanSample80211 scnSpl1 = new ScanSample80211("spl1", "00:00:00:00:01", new RSS(11,21,31,41), 1010);
		ScanSample80211 scnSpl2 = new ScanSample80211("spl2", "00:00:00:00:02", new RSS(12,22,32,42), 2020);
		ScanSampleList sSList1 = new ScanSampleList();
		sSList1.add(scnSpl1);
		sSList1.add(scnSpl2);
		lfptToSave1.add(Configuration.getInstance().sensor80211.scanner80211.ID, sSList1);
		
		LFPT lfptToSave2 = new LFPT(987654321, 185, new IGeoPoint(3,7));
		ScanSample80211 scnSpl3 = new ScanSample80211("spl3", "00:00:00:00:03", new RSS(13,23,33,43), 3030);
		ScanSample80211 scnSpl4 = new ScanSample80211("spl2", "00:00:00:00:02", new RSS(14,24,34,44), 4040);
		ScanSampleList sSList2 = new ScanSampleList();
		sSList2.add(scnSpl3);
		sSList2.add(scnSpl4);
		lfptToSave2.add(Configuration.getInstance().sensor80211.scanner80211.ID, sSList2);

		persStrat.save(lfptToSave1);
		persStrat.save(lfptToSave2); //should trigger update and merge and also change data
		persStrat.loadRadioMap();
		RadioMap radioMap = persStrat.getRadioMap();
		
		assertEquals(1, radioMap.size());
		LFPT lfpt = radioMap.get(0);
		assertEquals(987654321, lfpt.getCreationTime());
		assertEquals(3, lfpt.getIndoorGP().position_x);
		assertEquals(7, lfpt.getIndoorGP().position_y);
		assertEquals(180, lfpt.getQuantizedOrientation());
		
		sSList1 = (ScanSampleList) lfpt.get(Configuration.getInstance().sensor80211.scanner80211.ID);
		assertEquals(3, sSList1.size());
		
		scnSpl1 = (ScanSample80211) sSList1.get(0);
		assertEquals("spl1", scnSpl1.SSID);
		assertEquals("00:00:00:00:01", scnSpl1.MAC);
		assertEquals(1010, scnSpl1.meanFrequency);
		assertEquals(11.0, scnSpl1.rss.mean);
		assertEquals(21.0, scnSpl1.rss.variance);
		assertEquals(31.0, scnSpl1.rss.deviation);
		assertEquals(41.0, scnSpl1.rss.ssd);
		
		scnSpl2 = (ScanSample80211) sSList1.get(1);
		assertEquals("spl2", scnSpl2.SSID);
		assertEquals("00:00:00:00:02", scnSpl2.MAC);
		assertEquals(3030, scnSpl2.meanFrequency);
		assertEquals(13.0, scnSpl2.rss.mean);
		assertEquals(23.0, scnSpl2.rss.variance);
		assertEquals(33.0, scnSpl2.rss.deviation);
		assertEquals(43.0, scnSpl2.rss.ssd);
		
		scnSpl3 = (ScanSample80211) sSList1.get(2);
		assertEquals("spl3", scnSpl3.SSID);
		assertEquals("00:00:00:00:03", scnSpl3.MAC);
		assertEquals(3030, scnSpl3.meanFrequency);
		assertEquals(13.0, scnSpl3.rss.mean);
		assertEquals(23.0, scnSpl3.rss.variance);
		assertEquals(33.0, scnSpl3.rss.deviation);
		assertEquals(43.0, scnSpl3.rss.ssd);
		
	}
	
}