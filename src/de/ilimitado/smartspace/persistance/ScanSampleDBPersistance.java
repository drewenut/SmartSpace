package de.ilimitado.smartspace.persistance;

import java.util.ArrayList;

import de.ilimitado.smartspace.SensorDataSample;

public interface ScanSampleDBPersistance {

	public String getTableName();
	
	public String getSQLTableCreateQuery();
	
	public ArrayList<String> getSQLInsertQuerys(SensorDataSample sdSpl);
	
	public ArrayList<String> getSQLUpdateQuerys(SensorDataSample sdSpl);

	public String getSQLSelectQuery(String sdSplID);

	public SensorDataSample fill(SensorDataSample sdSpl, Object values);
}
