package de.ilimitado.smartspace.android;

import java.util.ArrayList;
import java.util.Set;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import de.ilimitado.smartspace.LFPT;
import de.ilimitado.smartspace.RadioMap;
import de.ilimitado.smartspace.config.Configuration;
import de.ilimitado.smartspace.persistance.LFPTPersistanceStrategy;
import de.ilimitado.smartspace.persistance.ScanSampleDBPersistance;
import de.ilimitado.smartspace.persistance.ScanSampleDBPersistanceProvider;
import de.ilimitado.smartspace.registry.RegistryProviderException;

public class AndroidLocalDBPersistanceStrategy implements LFPTPersistanceStrategy{

	private static final String LOG_TAG = "AndroidLocalDBPersistanceStrategy";
	private static final String DATABASE_NAME = Configuration.getInstance().persitenceConfig.lfptPersistanceDBName;
	
	ScanSampleDBPersistanceProvider sSplDBPersProvider;
	private Context androidCtx;
	private RadioMap radioMap;
	
	public AndroidLocalDBPersistanceStrategy(ScanSampleDBPersistanceProvider sSplDBPersProvider, Context androidCtx) {
		this.sSplDBPersProvider = sSplDBPersProvider;
		this.androidCtx = androidCtx;
		radioMap = new RadioMap();
	}
	
	@Override
	public RadioMap getRadioMap() {
		return radioMap;
	}

	@Override
	public void loadRadioMap() { //TODO Refactor
		radioMap = new RadioMap();
		ScanSampleDBPersistance sSplDBPers;
		try {
			sSplDBPers = sSplDBPersProvider.getItem(LFPT.VALUE_MAP_DEFAULT_DATA);
			AndroidLFPTDBAdapter lfptAdapter = getDBAdapter(sSplDBPers);
			lfptAdapter.open();
			Cursor allLfpts = lfptAdapter.execSQL("SELECT "+LFPT.VALUE_ID+" FROM " + LFPT.VALUE_MAP_DEFAULT_DATA);
			if(allLfpts.moveToFirst()){
				do {
					Set<String> dbTableNames = sSplDBPersProvider.keySet();
					String lfptID = allLfpts.getString(allLfpts.getColumnIndex(LFPT.VALUE_ID));
					try {
						sSplDBPers = sSplDBPersProvider.getItem(LFPT.VALUE_MAP_DEFAULT_DATA);
						String sqlSelectQuery = sSplDBPers.getSQLSelectQuery(lfptID);
						Cursor lfptCursor = lfptAdapter.execSQL(sqlSelectQuery);
						LFPT fingerprint = (LFPT) sSplDBPers.fill(null, lfptCursor);
						lfptCursor.close();
						for(String databaseTableName : dbTableNames){
							if(databaseTableName == LFPT.VALUE_MAP_DEFAULT_DATA) continue;	
							sSplDBPers = sSplDBPersProvider.getItem(databaseTableName);
							AndroidLFPTDBAdapter sSplAdapter = getDBAdapter(sSplDBPers);
							sqlSelectQuery = sSplDBPers.getSQLSelectQuery(fingerprint.getID());
							sSplAdapter.open();
							Cursor cursor = sSplAdapter.execSQL(sqlSelectQuery);
							fingerprint = (LFPT) sSplDBPers.fill(fingerprint, cursor);
							cursor.close();
							sSplAdapter.close();
						}
						radioMap.add(fingerprint);
					}
					catch (RegistryProviderException e){
						Log.w(LOG_TAG , "Couldn't find a Persistance Provider for key: '" + LFPT.VALUE_MAP_DEFAULT_DATA + "'. Did you register it to ScanSamplePersistanceProvider? Here is what else i know: " + e.getMessage());
					}
				} while (allLfpts.moveToNext());
			}
			allLfpts.close();
			lfptAdapter.close();
		} catch (RegistryProviderException e) {
			Log.w(LOG_TAG , "Couldn't find a Persistance Provider for key: '" + LFPT.VALUE_MAP_DEFAULT_DATA + "'. Did you register it to ScanSamplePersistanceProvider? Here is what else i know: " + e.getMessage());
		}
	}

	@Override
	public void save(LFPT fingerprint) {
		LFPT lfptToMerge = loadCorrespondingLfpt(fingerprint);
		if(lfptToMerge != null){
			lfptToMerge.merge(fingerprint);
			Set<String> dbTableNames = sSplDBPersProvider.keySet();
			for(String databaseTableName : dbTableNames){
				try {
					ScanSampleDBPersistance sSplDBPers = sSplDBPersProvider.getItem(databaseTableName);
					AndroidLFPTDBAdapter adapter = getDBAdapter(sSplDBPers);
					ArrayList<String> sqlUpdateQueryList = sSplDBPers.getSQLUpdateQuerys(lfptToMerge);
					adapter.open();
					adapter.execMultipleSqlQuerys(sqlUpdateQueryList);
					adapter.close();
				} catch (RegistryProviderException e) {
					Log.w(LOG_TAG , "Couldn't find a Persistance Provider for key: '" + databaseTableName + "'. Did you register it to ScanSamplePersistanceProvider? Here is what else i know: " + e.getMessage());
				}
			}
		}
		else{
			Set<String> dbTableNames = sSplDBPersProvider.keySet();
			for(String databaseTableName : dbTableNames){
				try {
					ScanSampleDBPersistance sSplDBPers = sSplDBPersProvider.getItem(databaseTableName);
					AndroidLFPTDBAdapter adapter = getDBAdapter(sSplDBPers);
					ArrayList<String> sqlInsertQueryList = sSplDBPers.getSQLInsertQuerys(fingerprint);
					adapter.open();
					adapter.execMultipleSqlQuerys(sqlInsertQueryList);
					adapter.close();
				} catch (RegistryProviderException e) {
					Log.w(LOG_TAG , "Couldn't find a Persistance Provider for key: '" + databaseTableName + "'. Did you register it to ScanSamplePersistanceProvider? Here is what else i know: " + e.getMessage());
				}
			}
		}
	}

	private LFPT loadCorrespondingLfpt(LFPT fingerprint) {
		ScanSampleDBPersistance sSplDBPers;
		Set<String> dbTableNames = sSplDBPersProvider.keySet();
		try {
			sSplDBPers = sSplDBPersProvider.getItem(LFPT.VALUE_MAP_DEFAULT_DATA);
			String sqlSelectQuery = sSplDBPers.getSQLSelectQuery(fingerprint.getID());
			AndroidLFPTDBAdapter lfptAdapter = getDBAdapter(sSplDBPers);
			lfptAdapter.open();
			Cursor cursor = lfptAdapter.execSQL(sqlSelectQuery);
			if(cursor == null || cursor.getCount() == 0){
				cursor.close();
				lfptAdapter.close();
				return null;
			}
			LFPT lfptToMerge = (LFPT) sSplDBPers.fill(null, cursor);
			cursor.close();
			lfptAdapter.close();
			for(String databaseTableName : dbTableNames){
				if(databaseTableName == LFPT.VALUE_MAP_DEFAULT_DATA) continue;	
				sSplDBPers = sSplDBPersProvider.getItem(databaseTableName);
				AndroidLFPTDBAdapter sSplAdapter = getDBAdapter(sSplDBPers);
				sqlSelectQuery = sSplDBPers.getSQLSelectQuery(lfptToMerge.getID());
				sSplAdapter.open();
				Cursor dataCursor = sSplAdapter.execSQL(sqlSelectQuery);
				lfptToMerge = (LFPT) sSplDBPers.fill(lfptToMerge, dataCursor);
				dataCursor.close();
				sSplAdapter.close();
			}
			return lfptToMerge;
		}
		catch (RegistryProviderException e){
			Log.w(LOG_TAG , "Couldn't find a Persistance Provider for key: '" + LFPT.VALUE_MAP_DEFAULT_DATA + "'. Did you register it to ScanSamplePersistanceProvider? Here is what else i know: " + e.getMessage());
		}
		
		return null;
	}

	private AndroidLFPTDBAdapter getDBAdapter(ScanSampleDBPersistance sSplDBPers) {
		return new AndroidLFPTDBAdapter(androidCtx, DATABASE_NAME, sSplDBPers.getTableName(), sSplDBPers.getSQLTableCreateQuery());
	}
	
}
