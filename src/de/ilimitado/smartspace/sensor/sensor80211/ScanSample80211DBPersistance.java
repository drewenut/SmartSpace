package de.ilimitado.smartspace.sensor.sensor80211;

import java.util.ArrayList;

import android.database.Cursor;
import de.ilimitado.smartspace.LFPT;
import de.ilimitado.smartspace.ScanSample;
import de.ilimitado.smartspace.ScanSampleList;
import de.ilimitado.smartspace.SensorDataSample;
import de.ilimitado.smartspace.config.Configuration;
import de.ilimitado.smartspace.persistance.ScanSampleDBPersistance;

public class ScanSample80211DBPersistance implements ScanSampleDBPersistance {

	@Override
	public String getTableName() {
		return Configuration.getInstance().sensorConfig80211.scanner80211PassiveConf.ID;
	}
	
	@Override
	public String getSQLTableCreateQuery() {
		return "CREATE TABLE IF NOT EXISTS " + getTableName() + 
		"( " +
			SensorDataSample.VALUE_ID 			 + " STRING KEY, "  +
			LFPT.VALUE_NAME 					 + " STRING, "  +
			ScanSample80211.VALUE_SSID           + " STRING, "  +
			ScanSample80211.VALUE_MAC            + " STRING, "  +
			ScanSample80211.VALUE_MEAN_FREQUENCY + " INTEGER, " +
			RSS.VALUE_RSS_MEAN                   + " DOUBLE, "  +
			RSS.VALUE_RSS_VARIANCE               + " DOUBLE, "  +
			RSS.VALUE_RSS_DEVIATION              + " DOUBLE, "  +
			RSS.VALUE_RSS_SSD                    + " DOUBLE "   +
		")";
	}
	
	@Override
	public ArrayList<String> getSQLInsertQuerys(SensorDataSample sdSpl) {
		
		ScanSampleList sSPlList = (ScanSampleList) sdSpl.get(getTableName());
		ArrayList<String> queryList = new ArrayList<String>(sSPlList.size());
		 
		if(sSPlList.isEmpty()) return queryList;
		
		String sqlInsertQueryPrefix = "INSERT INTO " + getTableName() + 
								"( " +
									SensorDataSample.VALUE_ID  + ", " +
									LFPT.VALUE_NAME + ", " +	
									ScanSample80211.VALUE_SSID + ", " +
									ScanSample80211.VALUE_MAC + ", " +
									ScanSample80211.VALUE_MEAN_FREQUENCY + ", " +
									RSS.VALUE_RSS_MEAN + ", " +
									RSS.VALUE_RSS_VARIANCE + ", " +
									RSS.VALUE_RSS_DEVIATION + ", " +
									RSS.VALUE_RSS_SSD + " " +
								" ) VALUES ";
		
		for(ScanSample sSpl : sSPlList){
			ScanSample80211 sSpl80211 = (ScanSample80211) sSpl;
			 queryList.add(sqlInsertQueryPrefix + 
					 			"( " +
					 				"'" + sdSpl.getID() + "', " + 
					 				"'" + ((LFPT)sdSpl).getIndoorGP().name + "', " +
									"'" + sSpl80211.SSID + "', " +
									"'" + sSpl80211.MAC + "', " +
									sSpl80211.meanFrequency + ", " +
									sSpl80211.rss.mean + ", " +
									sSpl80211.rss.variance + ", " +
									sSpl80211.rss.deviation + ", " +
									sSpl80211.rss.ssd + " " +
							  	" )");
		}
		return queryList;
	}
	
	@Override
	public ArrayList<String> getSQLUpdateQuerys(SensorDataSample sdSpl) {

		ScanSampleList sSPlList = (ScanSampleList) sdSpl.get(getTableName());
		ArrayList<String> queryList = new ArrayList<String>(sSPlList.size());
		 
		if(sSPlList.isEmpty()) return queryList;
		
		for(ScanSample sSpl : sSPlList){
			ScanSample80211 sSpl80211 = (ScanSample80211) sSpl;
			if(sSpl.isMerged())
				queryList.add(	
					"UPDATE " + getTableName() + 
					" SET " +
						LFPT.VALUE_NAME						  + "='"  + ((LFPT)sdSpl).getIndoorGP().name  + "', " +
						ScanSample80211.VALUE_SSID 			  + "='" + sSpl80211.SSID + "', " +
						ScanSample80211.VALUE_MEAN_FREQUENCY  + "="  + sSpl80211.meanFrequency + ", " +
						RSS.VALUE_RSS_MEAN 			+ "=" + sSpl80211.rss.mean + ", " +
						RSS.VALUE_RSS_VARIANCE 		+ "=" + sSpl80211.rss.variance + ", " +
						RSS.VALUE_RSS_DEVIATION 	+ "=" + sSpl80211.rss.deviation + ", " +
						RSS.VALUE_RSS_SSD 			+ "=" + sSpl80211.rss.ssd + " " +
					" WHERE " + SensorDataSample.VALUE_ID + "='" + sdSpl.getID() + "' AND " + ScanSample80211.VALUE_MAC + "='" + sSpl80211.MAC + "'");
			else
				queryList.add(
						"INSERT INTO " + getTableName() + 
						"( " +
							SensorDataSample.VALUE_ID  + ", " +	
							LFPT.VALUE_NAME + ", " +	
							ScanSample80211.VALUE_SSID + ", " +
							ScanSample80211.VALUE_MAC + ", " +
							ScanSample80211.VALUE_MEAN_FREQUENCY + ", " +
							RSS.VALUE_RSS_MEAN + ", " +
							RSS.VALUE_RSS_VARIANCE + ", " +
							RSS.VALUE_RSS_DEVIATION + ", " +
							RSS.VALUE_RSS_SSD + " " +
						" ) VALUES " +
			 			" ( " +
			 				"'" + sdSpl.getID() + "', " +
			 				"'" + ((LFPT)sdSpl).getIndoorGP().name  + "', " +
							"'" + sSpl80211.SSID + "', " +
							"'" + sSpl80211.MAC + "', " +
							sSpl80211.meanFrequency + ", " +
							sSpl80211.rss.mean + ", " +
							sSpl80211.rss.variance + ", " +
							sSpl80211.rss.deviation + ", " +
							sSpl80211.rss.ssd + " " +
					  	" )");

		}
		return queryList;
	}

	@Override
	public String getSQLSelectQuery(String sdSplID) {
		return "SELECT " +
			ScanSample80211.VALUE_SSID + ", " +
			ScanSample80211.VALUE_MAC + ", " +
			ScanSample80211.VALUE_MEAN_FREQUENCY + ", " +
			RSS.VALUE_RSS_MEAN + ", " +
			RSS.VALUE_RSS_VARIANCE + ", " +
			RSS.VALUE_RSS_DEVIATION + ", " +
			RSS.VALUE_RSS_SSD + " " +
		" FROM " + getTableName() + " WHERE " + SensorDataSample.VALUE_ID + "='" + sdSplID + "'";
	}

	@Override
	public SensorDataSample fill(SensorDataSample sdSpl, Object values) {
		Cursor cursor = (Cursor) values;
		ScanSampleList sSplList = new ScanSampleList();
		if(cursor.moveToFirst()){
			do {
				String ssid = cursor.getString(cursor.getColumnIndex(ScanSample80211.VALUE_SSID));
				String bssid = cursor.getString(cursor.getColumnIndex(ScanSample80211.VALUE_MAC));
				int frequency = cursor.getInt(cursor.getColumnIndex(ScanSample80211.VALUE_MEAN_FREQUENCY));
				double mean = cursor.getDouble(cursor.getColumnIndex(RSS.VALUE_RSS_MEAN));
				double dev = cursor.getDouble(cursor.getColumnIndex(RSS.VALUE_RSS_DEVIATION));
				double var = cursor.getDouble(cursor.getColumnIndex(RSS.VALUE_RSS_VARIANCE));
				double ssd = cursor.getDouble(cursor.getColumnIndex(RSS.VALUE_RSS_SSD));
				RSS rss = new RSS(mean, var, dev, ssd);
				ScanSample80211 sSpl = new ScanSample80211(ssid, bssid, rss, frequency);
				sSplList.add(sSpl);
			} while(cursor.moveToNext());
			sdSpl.add(getTableName(), sSplList);
		}
		return sdSpl;
	}

}
