package de.ilimitado.smartspace.tests.junit.persistance;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import de.ilimitado.smartspace.IGeoPoint;
import de.ilimitado.smartspace.LFPT;
import de.ilimitado.smartspace.ScanSampleList;
import de.ilimitado.smartspace.config.Configuration;
import de.ilimitado.smartspace.sensor.sensor80211.RSS;
import de.ilimitado.smartspace.sensor.sensor80211.ScanSample80211;
import de.ilimitado.smartspace.sensor.sensor80211.ScanSample80211DBPersistance;
import de.ilimitado.smartspace.tests.junit.config.MockConfigTranslator;

public class ScanSample80211DBPersistanceTest {

	private ScanSample80211DBPersistance dbPers;

	@Before
	public void setUp() throws Exception {
		dbPers = new ScanSample80211DBPersistance();
		MockConfigTranslator.getInstance().translate();
	}

	@Test
	public void testGetTableName() {
		assertEquals(Configuration.getInstance().sensorConfig80211.scanner80211PassiveConf.ID, dbPers.getTableName());
	}

	@Test
	public void testGetSQLTableCreateQuery() {
		String tableCreateQuery = dbPers.getSQLTableCreateQuery();
		assertEquals("CREATE TABLE IF NOT EXISTS Scanner80211Passive( ID STRING KEY, igp_name STRING, SSID STRING, MAC STRING, MeanFrequency INTEGER, RSSMean DOUBLE, RSSVariance DOUBLE, RSSDeviation DOUBLE, RSSSSD DOUBLE )", tableCreateQuery);
	}

	@Test
	public void testGetSQLInsertQueryWithoutScanSample() {
		LFPT lfpt = new LFPT(123456789, 95, new IGeoPoint(3,7));
		ArrayList<String> insertQueryList = dbPers.getSQLInsertQuerys(lfpt);
		assertEquals(0, insertQueryList.size());
	}
	
	@Test
	public void testGetSQLInsertQueryWithScanSample() {
		LFPT lfpt = new LFPT(123456789, 95, new IGeoPoint("gPname",3,7));
		ScanSample80211 scnS1 = new ScanSample80211("spl1", "00:00:00:00:01", new RSS(10,20,30,40), 1010);
		ScanSampleList sSList = new ScanSampleList();
		sSList.add(scnS1);
		lfpt.add(Configuration.getInstance().sensorConfig80211.scanner80211PassiveConf.ID, sSList);
		ArrayList<String> insertQueryList = dbPers.getSQLInsertQuerys(lfpt);
		assertEquals(1, insertQueryList.size());
		String insertQuery = insertQueryList.get(0);
		assertEquals("INSERT INTO Scanner80211Passive( ID, igp_name, SSID, MAC, MeanFrequency, RSSMean, RSSVariance, RSSDeviation, RSSSSD  ) VALUES ( '3790', 'gPname', 'spl1', '00:00:00:00:01', 1010, 10.0, 20.0, 30.0, 40.0  )", insertQuery);
	}
	
	@Test
	public void testGetSQLInsertQueryWithMultipleScanSamples() {
		LFPT lfpt = new LFPT(123456789, 95, new IGeoPoint("iGpName",3,7));
		ScanSample80211 scnS1 = new ScanSample80211("spl1", "00:00:00:00:01", new RSS(10,20,30,40), 1010);
		ScanSample80211 scnS2 = new ScanSample80211("spl2", "00:00:00:00:02", new RSS(11,21,31,41), 1020);
		ScanSample80211 scnS3 = new ScanSample80211("spl3", "00:00:00:00:03", new RSS(15,25,35,45), 1050);
		ScanSampleList sSList = new ScanSampleList();
		sSList.add(scnS1);
		sSList.add(scnS2);
		sSList.add(scnS3);
		lfpt.add(Configuration.getInstance().sensorConfig80211.scanner80211PassiveConf.ID, sSList);
		ArrayList<String> insertQueryList = dbPers.getSQLInsertQuerys(lfpt);
		assertEquals(3, insertQueryList.size());
		String insertQuery1 = insertQueryList.get(0);
		assertEquals("INSERT INTO Scanner80211Passive( ID, igp_name, SSID, MAC, MeanFrequency, RSSMean, RSSVariance, RSSDeviation, RSSSSD  ) VALUES ( '3790', 'iGpName', 'spl1', '00:00:00:00:01', 1010, 10.0, 20.0, 30.0, 40.0  )", insertQuery1);
		String insertQuery2 = insertQueryList.get(1);
		assertEquals("INSERT INTO Scanner80211Passive( ID, igp_name, SSID, MAC, MeanFrequency, RSSMean, RSSVariance, RSSDeviation, RSSSSD  ) VALUES ( '3790', 'iGpName', 'spl2', '00:00:00:00:02', 1020, 11.0, 21.0, 31.0, 41.0  )", insertQuery2);
		String insertQuery3 = insertQueryList.get(2);
		assertEquals("INSERT INTO Scanner80211Passive( ID, igp_name, SSID, MAC, MeanFrequency, RSSMean, RSSVariance, RSSDeviation, RSSSSD  ) VALUES ( '3790', 'iGpName', 'spl3', '00:00:00:00:03', 1050, 15.0, 25.0, 35.0, 45.0  )", insertQuery3);
	}

	@Test
	public void testGetSQLSelectQuery() {
		LFPT lfpt = new LFPT(123456789, 95, new IGeoPoint(3,7));
		String selectQuery = dbPers.getSQLSelectQuery(lfpt.getID());
		assertEquals("SELECT SSID, MAC, MeanFrequency, RSSMean, RSSVariance, RSSDeviation, RSSSSD  FROM Scanner80211Passive WHERE ID='3790'", selectQuery);
	}
	
	@Test
	public void testGetSQLUpdateQueryWithScanSample() {
		LFPT lfpt = new LFPT(123456789, 95, new IGeoPoint("iGpName", 3,7));
		ScanSample80211 scnS1 = new ScanSample80211("spl1", "00:00:00:00:01", new RSS(10,20,30,40), 1010);
		ScanSampleList sSList = new ScanSampleList();
		sSList.add(scnS1);
		lfpt.add(Configuration.getInstance().sensorConfig80211.scanner80211PassiveConf.ID, sSList);
		ArrayList<String> updateQueryList = dbPers.getSQLUpdateQuerys(lfpt);
		assertEquals(1, updateQueryList.size());
		String updateQuery = updateQueryList.get(0);
		assertEquals("UPDATE Scanner80211Passive SET igp_name='iGpName', SSID='spl1', MeanFrequency=1010, RSSMean=10.0, RSSVariance=20.0, RSSDeviation=30.0, RSSSSD=40.0  WHERE ID='3790' AND MAC='00:00:00:00:01'", updateQuery);
	}
	
	@Test
	public void testGetSQLUpdateQueryWithMultipleScanSamples() {
		LFPT lfpt = new LFPT(123456789, 95, new IGeoPoint("iGpName",3,7));
		ScanSample80211 scnS1 = new ScanSample80211("spl1", "00:00:00:00:01", new RSS(10,20,30,40), 1010);
		ScanSample80211 scnS2 = new ScanSample80211("spl2", "00:00:00:00:02", new RSS(11,21,31,41), 1020);
		ScanSample80211 scnS3 = new ScanSample80211("spl3", "00:00:00:00:03", new RSS(15,25,35,45), 1050);
		ScanSampleList sSList = new ScanSampleList();
		sSList.add(scnS1);
		sSList.add(scnS2);
		sSList.add(scnS3);
		lfpt.add(Configuration.getInstance().sensorConfig80211.scanner80211PassiveConf.ID, sSList);
		ArrayList<String> updateQueryList = dbPers.getSQLUpdateQuerys(lfpt);
		assertEquals(3, updateQueryList.size());
		String updateQuery1 = updateQueryList.get(0);
		assertEquals("UPDATE Scanner80211Passive SET igp_name='iGpName', SSID='spl1', MeanFrequency=1010, RSSMean=10.0, RSSVariance=20.0, RSSDeviation=30.0, RSSSSD=40.0  WHERE ID='3790' AND MAC='00:00:00:00:01'", updateQuery1);
		String updateQuery2 = updateQueryList.get(1);
		assertEquals("UPDATE Scanner80211Passive SET igp_name='iGpName', SSID='spl2', MeanFrequency=1020, RSSMean=11.0, RSSVariance=21.0, RSSDeviation=31.0, RSSSSD=41.0  WHERE ID='3790' AND MAC='00:00:00:00:02'", updateQuery2);
		String updateQuery3 = updateQueryList.get(2);
		assertEquals("UPDATE Scanner80211Passive SET igp_name='iGpName', SSID='spl3', MeanFrequency=1050, RSSMean=15.0, RSSVariance=25.0, RSSDeviation=35.0, RSSSSD=45.0  WHERE ID='3790' AND MAC='00:00:00:00:03'", updateQuery3);
	}

}
