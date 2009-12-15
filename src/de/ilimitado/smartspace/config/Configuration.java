package de.ilimitado.smartspace.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.ilimitado.smartspace.ConstraintsMap;
import de.ilimitado.smartspace.dataprocessors.DataProcessorsMap;

public final class Configuration {
	
	public final ConfigLocalization localization;
	public final ConfigPersistence persistence;
	public final ConfigSensing sensing;
	public final ConfigSensor80211 sensor80211;
	private static Configuration instance = null;
	protected static List<AbstractSensorConfig> sensorConfigs = new ArrayList<AbstractSensorConfig>(); 
	
	private Configuration(	ConfigLocalization algos,
			   			  	ConfigPersistence configPersistence,
			   			  	ConfigSensing configSensing,
			   			  	ConfigSensor80211 config80211
			   			  	) {
		this.localization = algos;
		this.persistence = configPersistence;
		this.sensing = configSensing;
		this.sensor80211 = config80211;
	}
	
	public static void createConfiguration(	ConfigLocalization algos,
										   	ConfigPersistence persConf,
										   	ConfigSensing fptColl,
										   	ConfigSensor80211 snsCfg80211
											) {
		instance = new Configuration(algos, persConf, fptColl , snsCfg80211);
	}
	
	public static Configuration getInstance() {
		if(instance != null)
			return instance;
		else
			throw new RuntimeException("Configuration Object not initialized...Does the precondition hold? Did you call Configuration.createConfiguration() before calling getInstance()?");
	}
	
	public ConstraintsMap<String, List<Number>> getConstraintMap() {
		ConstraintsMap<String, List<Number>> cMap = new ConstraintsMap<String, List<Number>>();
		for(AbstractSensorConfig config : sensorConfigs){
			config.getSensorConstraints(cMap);
		}
		return cMap;
	}
	
	public DataProcessorsMap<String,HashMap<String, Boolean>> getDataProcessorList() {
		DataProcessorsMap<String,HashMap<String, Boolean>> dPMap = new DataProcessorsMap<String,HashMap<String, Boolean>>();
		for(AbstractSensorConfig config : sensorConfigs){
			dPMap = config.getSensorDataProcessorsMap(dPMap);
		}
		return dPMap;
	}
	
	public ArrayList<String> getSyncList(){
		ArrayList<String> syncList = new ArrayList<String>();
		for(AbstractSensorConfig config : sensorConfigs){
			config.getSensorSyncList(syncList);
		}
		return syncList; 
	}
}