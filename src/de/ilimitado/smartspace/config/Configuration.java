package de.ilimitado.smartspace.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.ilimitado.smartspace.ConstraintsMap;
import de.ilimitado.smartspace.dataprocessors.DataProcessorsMap;

public final class Configuration {
	
	public final Localization posAlgos;
	public final Persistance persitenceConf;
	public final Sensing fptCollection;
	public final Sensor80211 sensorConfig80211;
	private static Configuration instance = null;
	protected static List<AbstractSensorConfig> sensorConfigs = new ArrayList<AbstractSensorConfig>(); 
	
	private Configuration(	Localization algos,
			   			  	Persistance persConf,
			   			  	Sensing fptColl,
			   			  	Sensor80211 snsCfg80211
			   			  	) {
		this.posAlgos = algos;
		this.persitenceConf = persConf;
		this.fptCollection = fptColl;
		this.sensorConfig80211 = snsCfg80211;
	}
	
	public static void createConfiguration(	Localization algos,
										   	Persistance persConf,
										   	Sensing fptColl,
										   	Sensor80211 snsCfg80211
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