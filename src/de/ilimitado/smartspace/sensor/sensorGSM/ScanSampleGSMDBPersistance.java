package de.ilimitado.smartspace.sensor.sensorGSM;

import java.util.ArrayList;

import android.database.Cursor;
import de.ilimitado.smartspace.LFPT;
import de.ilimitado.smartspace.RSS;
import de.ilimitado.smartspace.ScanSample;
import de.ilimitado.smartspace.ScanSampleList;
import de.ilimitado.smartspace.SensorDataSample;
import de.ilimitado.smartspace.config.Configuration;
import de.ilimitado.smartspace.persistance.ScanSampleDBPersistance;

public class ScanSampleGSMDBPersistance implements ScanSampleDBPersistance {

	@Override
	public String getTableName() {
		return Configuration.getInstance().sensorGSM.scannerGSM_RSS.ID;
	}
	
	@Override
	public String getSQLTableCreateQuery() {
		return "CREATE TABLE IF NOT EXISTS " + getTableName() + 
		"( " +
			SensorDataSample.VALUE_ID 			 + " STRING KEY, "  +
			LFPT.VALUE_NAME 					 + " STRING, "  +
			ScanSampleGSM.VALUE_CID           	 + " STRING, "  +
			ScanSampleGSM.VALUE_PROVIDER         + " STRING, "  +
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
									ScanSampleGSM.VALUE_CID + ", " +
									ScanSampleGSM.VALUE_PROVIDER + ", " +
									RSS.VALUE_RSS_MEAN + ", " +
									RSS.VALUE_RSS_VARIANCE + ", " +
									RSS.VALUE_RSS_DEVIATION + ", " +
									RSS.VALUE_RSS_SSD + " " +
								" ) VALUES ";
		
		for(ScanSample sSpl : sSPlList){
			ScanSampleGSM sSplGSM = (ScanSampleGSM) sSpl;
			 queryList.add(sqlInsertQueryPrefix + 
					 			"( " +
					 				"'" + sdSpl.getID() + "', " + 
					 				"'" + ((LFPT)sdSpl).getIndoorGP().name + "', " +
									"'" + sSplGSM.CID + "', " +
									"'" + sSplGSM.PROVIDER + "', " +
									sSplGSM.rss.mean + ", " +
									sSplGSM.rss.variance + ", " +
									sSplGSM.rss.deviation + ", " +
									sSplGSM.rss.ssd + " " +
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
			ScanSampleGSM sSplGSM = (ScanSampleGSM) sSpl;
			if(sSpl.isMerged())
				queryList.add(	
					"UPDATE " + getTableName() + 
					" SET " +
						LFPT.VALUE_NAME						  + "='"  + ((LFPT)sdSpl).getIndoorGP().name  + "', " +
						ScanSampleGSM.VALUE_CID 			  + "='" + sSplGSM.CID + "', " +
						RSS.VALUE_RSS_MEAN 			+ "=" + sSplGSM.rss.mean + ", " +
						RSS.VALUE_RSS_VARIANCE 		+ "=" + sSplGSM.rss.variance + ", " +
						RSS.VALUE_RSS_DEVIATION 	+ "=" + sSplGSM.rss.deviation + ", " +
						RSS.VALUE_RSS_SSD 			+ "=" + sSplGSM.rss.ssd + " " +
					" WHERE " + SensorDataSample.VALUE_ID + "='" + sdSpl.getID() + "' AND " + ScanSampleGSM.VALUE_CID + "='" + sSplGSM.CID + "'");
			else
				queryList.add(
						"INSERT INTO " + getTableName() + 
						"( " +
							SensorDataSample.VALUE_ID  + ", " +	
							LFPT.VALUE_NAME + ", " +	
							ScanSampleGSM.VALUE_CID + ", " +
							ScanSampleGSM.VALUE_PROVIDER + ", " +
							RSS.VALUE_RSS_MEAN + ", " +
							RSS.VALUE_RSS_VARIANCE + ", " +
							RSS.VALUE_RSS_DEVIATION + ", " +
							RSS.VALUE_RSS_SSD + " " +
						" ) VALUES " +
			 			" ( " +
			 				"'" + sdSpl.getID() + "', " +
			 				"'" + ((LFPT)sdSpl).getIndoorGP().name  + "', " +
							"'" + sSplGSM.CID + "', " +
							"'" + sSplGSM.PROVIDER + "', " +
							sSplGSM.rss.mean + ", " +
							sSplGSM.rss.variance + ", " +
							sSplGSM.rss.deviation + ", " +
							sSplGSM.rss.ssd + " " +
					  	" )");

		}
		return queryList;
	}

	@Override
	public String getSQLSelectQuery(String sdSplID) {
		return "SELECT " +
			ScanSampleGSM.VALUE_CID + ", " +
			ScanSampleGSM.VALUE_PROVIDER + ", " +
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
				String cid = cursor.getString(cursor.getColumnIndex(ScanSampleGSM.VALUE_CID));
				String provider = cursor.getString(cursor.getColumnIndex(ScanSampleGSM.VALUE_PROVIDER));
				double mean = cursor.getDouble(cursor.getColumnIndex(RSS.VALUE_RSS_MEAN));
				double dev = cursor.getDouble(cursor.getColumnIndex(RSS.VALUE_RSS_DEVIATION));
				double var = cursor.getDouble(cursor.getColumnIndex(RSS.VALUE_RSS_VARIANCE));
				double ssd = cursor.getDouble(cursor.getColumnIndex(RSS.VALUE_RSS_SSD));
				RSS rss = new RSS(mean, var, dev, ssd);
				ScanSampleGSM sSpl = new ScanSampleGSM(cid, provider, rss);
				sSplList.add(sSpl);
			} while(cursor.moveToNext());
			sdSpl.add(getTableName(), sSplList);
		}
		return sdSpl;
	}

}
