package de.ilimitado.smartspace.android;

import java.security.InvalidParameterException;
import java.util.ArrayList;

import android.database.Cursor;
import de.ilimitado.smartspace.LFPT;
import de.ilimitado.smartspace.SensorDataSample;
import de.ilimitado.smartspace.persistance.ScanSampleDBPersistance;
import de.ilimitado.smartspace.positioning.IGeoPoint;

public class LFPTDBPersistance implements ScanSampleDBPersistance {

	//TODO count numbers of updates per access point by incrementing on update
//	private static final String VALUE_LFPT_UPDATE_COUNT = "updateCount";

	@Override
	public String getTableName() {
		return LFPT.VALUE_MAP_DEFAULT_DATA;
	}
	
	@Override
	public String getSQLTableCreateQuery() {
		return "CREATE TABLE IF NOT EXISTS " + getTableName() + 
		"( " +
			LFPT.VALUE_ID			 + " STRING KEY, " +	
			LFPT.VALUE_CREATION_TIME + " LONG, " +
			LFPT.VALUE_POS_X         + " INTEGER, " +
			LFPT.VALUE_POS_Y         + " INTEGER, " +
			LFPT.VALUE_FLOOR         + " INTEGER, " +
			LFPT.VALUE_NAME          + " STRING, " +
			LFPT.VALUE_ORIENTATION   + " INTEGER " +
//			VALUE_LFPT_UPDATE_COUNT  + " INTEGER  " + 
		")";
	}
	
	@Override
	public ArrayList<String> getSQLInsertQuerys(SensorDataSample sdSpl) {
		LFPT lfpt = (LFPT) sdSpl;
		IGeoPoint iGp = lfpt.getIndoorGP();
		String sqlInsertQuery = "INSERT INTO " + getTableName() + 
								"( " +
									LFPT.VALUE_ID 			 + ", " +
									LFPT.VALUE_CREATION_TIME + ", " +
									LFPT.VALUE_POS_X         + ", " +
									LFPT.VALUE_POS_Y         + ", " +
									LFPT.VALUE_FLOOR         + ", " +
									LFPT.VALUE_NAME          + ", " +
									LFPT.VALUE_ORIENTATION   + " " +
//									VALUE_LFPT_UPDATE_COUNT  + " " + 
								" ) VALUES ( " +
									"'" + lfpt.getID() + "', "+
									lfpt.getCreationTime() + ", " +
									iGp.position_x + ", " +
									iGp.position_y + ", " +
									iGp.floor      + ", " +
									"'" + iGp.name + "', " +
									lfpt.getQuantizedOrientation() + " " +
//									"1" +
								" )";
		ArrayList<String> queryList = new ArrayList<String>(1);
		queryList.add(sqlInsertQuery);
		return queryList;
	}
	
	public ArrayList<String> getSQLUpdateQuerys(SensorDataSample sdSpl){
		LFPT lfpt = (LFPT) sdSpl;
		IGeoPoint iGp = lfpt.getIndoorGP();
		String sqlInsertQuery = "UPDATE " + getTableName() + 
								" SET " +
									LFPT.VALUE_CREATION_TIME + "="  + lfpt.getCreationTime()  + ", " +
									LFPT.VALUE_POS_X         + "="  + iGp.position_x  + ", " +
									LFPT.VALUE_POS_Y         + "="  + iGp.position_y  + ", " +
									LFPT.VALUE_FLOOR         + "="  + iGp.floor       + ", " +
									LFPT.VALUE_NAME          + "='" + iGp.name        + "', "+
									LFPT.VALUE_ORIENTATION   + "="  + lfpt.getQuantizedOrientation() + " " +
//									VALUE_LFPT_UPDATE_COUNT  + "="  + getTableName()+"."+VALUE_LFPT_UPDATE_COUNT + "+1 " +
								" WHERE " +
									LFPT.VALUE_ID + "='" + sdSpl.getID() + "'";
		ArrayList<String> queryList = new ArrayList<String>(1);
		queryList.add(sqlInsertQuery);
		return queryList;
	}

	@Override
	public String getSQLSelectQuery(String sdSplID) {
		return "SELECT " +
		LFPT.VALUE_CREATION_TIME + ", " +
		LFPT.VALUE_POS_X         + ", " +
		LFPT.VALUE_POS_Y         + ", " +
		LFPT.VALUE_FLOOR         + ", " +
		LFPT.VALUE_NAME          + ", " +
		LFPT.VALUE_ORIENTATION   + " " + 
		" FROM " + getTableName() + " WHERE " + SensorDataSample.VALUE_ID + "='" + sdSplID + "'";
	}

	@Override
	public SensorDataSample fill(SensorDataSample sdSpl, Object values) {
		Cursor cursor = (Cursor) values;
		if(cursor.moveToFirst()){
			int creationTime = cursor.getInt(cursor.getColumnIndex(LFPT.VALUE_CREATION_TIME));
			int posX = cursor.getInt(cursor.getColumnIndex(LFPT.VALUE_POS_X));
			int posY = cursor.getInt(cursor.getColumnIndex(LFPT.VALUE_POS_Y));
			int floor = cursor.getInt(cursor.getColumnIndex(LFPT.VALUE_FLOOR));
			String name = cursor.getString(cursor.getColumnIndex(LFPT.VALUE_NAME));
			int orientation = cursor.getInt(cursor.getColumnIndex(LFPT.VALUE_ORIENTATION));
			IGeoPoint iGp = new IGeoPoint(name, posX, posY, floor);
			return new LFPT(creationTime, orientation, iGp);
		} else {
			throw new InvalidParameterException("Values must containt a Android Cursor Objekt with LFPT data but was empty!");
		}
	}
	
}
