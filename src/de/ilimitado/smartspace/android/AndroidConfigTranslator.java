package de.ilimitado.smartspace.android;

import java.util.HashMap;

import android.content.SharedPreferences;
import de.ilimitado.smartspace.config.ConfigTranslator;
import de.ilimitado.smartspace.config.Configuration;
import de.ilimitado.smartspace.config.DataProcessorCommands;
import de.ilimitado.smartspace.config.Sensing;
import de.ilimitado.smartspace.config.Persistance;
import de.ilimitado.smartspace.config.Localization;
import de.ilimitado.smartspace.config.Scanner80211PassiveConfig;
import de.ilimitado.smartspace.config.Sensor80211;

public class AndroidConfigTranslator implements ConfigTranslator{
	
	private static AndroidConfigTranslator instance;
	private SharedPreferences androidPreferences;

	protected AndroidConfigTranslator(SharedPreferences preferences) {
		this.androidPreferences = preferences;
	}
	
	public static AndroidConfigTranslator getInstance(SharedPreferences preferences) {
		return instance == null 
				? instance = new AndroidConfigTranslator(preferences)
				: instance;
	}
	
	public void translate() {
		Localization algos = getPositionAlgorithms();
		Persistance persConf = getPersistanceConfig();
		Sensing fptColl = getFPTCollection();
		Sensor80211 snsCfg80211 = getSensorConfig80211();

		Configuration.createConfiguration(algos, persConf, fptColl, snsCfg80211);
	}

	private Localization getPositionAlgorithms() {
		boolean useEuklid = androidPreferences.getBoolean("position_algorithm_euklidean", true);
		Localization algos = new Localization(useEuklid);
		return algos;
	}

	private Persistance getPersistanceConfig() {
		int lfptPM = androidPreferences.getInt("persistance_config_mode", 0);
		int refreshInterval = androidPreferences.getInt("persistance_config_refreshInterval", 60000);
		String lfptPersistanceDBName = androidPreferences.getString("persistance_config_lfptPersistanceDBName", "RadioMap");
		int lfptPersistanceBufferSize = androidPreferences.getInt("persistance_config_lfptPersistanceBufferSize", 10);
		Persistance persConf = new Persistance(lfptPM, refreshInterval, lfptPersistanceDBName, lfptPersistanceBufferSize);
		return persConf;
	}

	private Sensing getFPTCollection() {
		int oriQuantCount = androidPreferences.getInt("fpt_collection_orientation_quant_count", 4);
		Sensing fptColl = new Sensing(oriQuantCount);
		return fptColl;
	}

	private Sensor80211 getSensorConfig80211() {
		String sensor80211name = androidPreferences.getString("sensor_config_80211_sensor_name", "Sensor80211");
		boolean sensor80211isActive = androidPreferences.getBoolean("sensor_config_80211_sensor_is_active", true);
		Scanner80211PassiveConfig scn80211passive = getScanner80211PassiveConfig();
		Sensor80211 snsCfg80211 = new Sensor80211(sensor80211name, sensor80211isActive, scn80211passive);
		return snsCfg80211;
	}

	private Scanner80211PassiveConfig getScanner80211PassiveConfig() {
		String passive80211Name = androidPreferences.getString("scanner_config_80211_passive_scanner_name", "Sensor80211ScannerPassive");
		boolean passive80211isActive = androidPreferences.getBoolean("scanner_config_80211_passive_scanner_is_active", true);
		boolean passive80211synchronize = androidPreferences.getBoolean("scanner_config_80211_passive_scanner_synchronize", true);
		int passive80211threshold = androidPreferences.getInt("scanner_config_80211_passive_threshold", 10);
		long passive80211timeout = androidPreferences.getLong("scanner_config_80211_passive_timeout", 1000);
		DataProcessorCommands passive80211dPCommands = getDataProcessCommands();
		Scanner80211PassiveConfig scn80211passive = new Scanner80211PassiveConfig(passive80211Name, passive80211isActive, passive80211synchronize, passive80211threshold, passive80211timeout, passive80211dPCommands);
		return scn80211passive;
	}

	private DataProcessorCommands getDataProcessCommands() {
		boolean passive80211MeanCommand80211 = androidPreferences.getBoolean("scanner_config_80211_passive_commands_mean_command", true);
		HashMap<String, Boolean> dataCommands = new HashMap<String, Boolean>();
		dataCommands.put("MeanCommand80211", passive80211MeanCommand80211);
		DataProcessorCommands passive80211dPCommands = new DataProcessorCommands(dataCommands);
		return passive80211dPCommands;
	}

}
