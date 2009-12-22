package de.ilimitado.smartspace.android;

import java.util.HashMap;

import android.content.SharedPreferences;
import de.ilimitado.smartspace.config.ConfigScannerGSMCell;
import de.ilimitado.smartspace.config.ConfigSensorGSM;
import de.ilimitado.smartspace.config.ConfigTranslator;
import de.ilimitado.smartspace.config.Configuration;
import de.ilimitado.smartspace.config.ConfigDataCommands;
import de.ilimitado.smartspace.config.ConfigSensing;
import de.ilimitado.smartspace.config.ConfigPersistence;
import de.ilimitado.smartspace.config.ConfigLocalization;
import de.ilimitado.smartspace.config.ConfigScannerGSMCell;
import de.ilimitado.smartspace.config.ConfigSensor80211;

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
		ConfigLocalization algos = getPositionAlgorithms();
		ConfigPersistence persConf = getPersistanceConfig();
		ConfigSensing sensingConf = getFPTCollection();
		ConfigSensor80211 snsCfg80211 = getSensorConfig80211();
		ConfigSensorGSM snsCfgGSM = getSensorConfig_GSM();

		Configuration.createConfiguration(algos, persConf, sensingConf, snsCfg80211, snsCfgGSM);
	}

	private ConfigLocalization getPositionAlgorithms() {
		boolean useEuklid = androidPreferences.getBoolean("position_algorithm_euklidean", true);
		ConfigLocalization algos = new ConfigLocalization(useEuklid);
		return algos;
	}

	private ConfigPersistence getPersistanceConfig() {
		int lfptPM = androidPreferences.getInt("persistance_config_mode", 0);
		int refreshInterval = androidPreferences.getInt("persistance_config_refreshInterval", 60000);
		String lfptPersistanceDBName = androidPreferences.getString("persistance_config_lfptPersistanceDBName", "RadioMap");
		int lfptPersistanceBufferSize = androidPreferences.getInt("persistance_config_lfptPersistanceBufferSize", 10);
		ConfigPersistence persConf = new ConfigPersistence(lfptPersistanceDBName, lfptPM, lfptPersistanceBufferSize, refreshInterval);
		return persConf;
	}

	private ConfigSensing getFPTCollection() {
		int oriQuantCount = androidPreferences.getInt("fpt_collection_orientation_quant_count", 4);
		ConfigSensing fptColl = new ConfigSensing(oriQuantCount);
		return fptColl;
	}

	private ConfigSensor80211 getSensorConfig80211() {
		String sensor80211name = androidPreferences.getString("sensor_config_80211_sensor_name", "Sensor80211");
		boolean sensor80211isActive = androidPreferences.getBoolean("sensor_config_80211_sensor_is_active", true);
		ConfigScannerGSMCell scn80211passive = getScanner80211PassiveConfig();
		ConfigSensor80211 snsCfg80211 = new ConfigSensor80211(sensor80211name, sensor80211isActive, scn80211passive);
		return snsCfg80211;
	}

	private ConfigScannerGSMCell getScanner80211PassiveConfig() {
		String passive80211Name = androidPreferences.getString("scanner_config_80211_passive_scanner_name", "Sensor80211ScannerPassive");
		boolean passive80211isActive = androidPreferences.getBoolean("scanner_config_80211_passive_scanner_is_active", true);
		boolean passive80211synchronize = androidPreferences.getBoolean("scanner_config_80211_passive_scanner_synchronize", true);
		int passive80211threshold = androidPreferences.getInt("scanner_config_80211_passive_threshold", 10);
		long passive80211timeout = androidPreferences.getLong("scanner_config_80211_passive_timeout", 1000);
		ConfigDataCommands passive80211dPCommands = getScanner80211DataProcessCommands();
		ConfigScannerGSMCell scn80211passive = new ConfigScannerGSMCell(passive80211Name, passive80211isActive, passive80211synchronize, passive80211threshold, passive80211timeout, passive80211dPCommands);
		return scn80211passive;
	}

	private ConfigDataCommands getScanner80211DataProcessCommands() {
		boolean passive80211MeanCommand80211 = androidPreferences.getBoolean("scanner_config_80211_passive_commands_mean_command", true);
		HashMap<String, Boolean> dataCommands = new HashMap<String, Boolean>();
		dataCommands.put("MeanCommand80211", passive80211MeanCommand80211);
		ConfigDataCommands passive80211dPCommands = new ConfigDataCommands(dataCommands);
		return passive80211dPCommands;
	}
	
	private ConfigSensorGSM getSensorConfig_GSM() {
		String sensorGSMname = androidPreferences.getString("sensor_config_gsm_sensor_name", "sensorGSM");
		boolean sensorGSMisActive = androidPreferences.getBoolean("sensor_config_gsm_sensor_is_active", true);
		ConfigScannerGSMCell GSMactiveCell = getScannerGSMActiveCellConfig();
		ConfigScannerGSMCell GSMNeighborCells = getScannerGSMNeighborCellsConfig();
		ConfigSensorGSM snsCfgGSM = new ConfigSensorGSM(sensorGSMname, sensorGSMisActive, GSMactiveCell);
		return snsCfgGSM;
	}

	private ConfigScannerGSMCell getScannerGSMActiveCellConfig() {
		String scnGSMName = androidPreferences.getString("scanner_config_gsm_active_cell_scanner_name", "scannerGsmRss");
		boolean scnGSMisActive = androidPreferences.getBoolean("scanner_config_gsm_active_cell_scanner_is_active", true);
		boolean scnGSMSynchronize = androidPreferences.getBoolean("scanner_config_gsm_active_cell_scanner_synchronize", true);
		int scnGSMThreshold = androidPreferences.getInt("scanner_config_gsm_active_cell_threshold", 10);
		long scnGSMTimeout = androidPreferences.getLong("scanner_config_gsm_active_cell_timeout", 1000);
		ConfigDataCommands scnGSMCommands = getGSM_DataProcessCommands();
		ConfigScannerGSMCell scnGSM = new ConfigScannerGSMCell(scnGSMName, scnGSMisActive, scnGSMSynchronize, scnGSMThreshold, scnGSMTimeout, scnGSMCommands);
		return scnGSM;
	}
	
	private ConfigScannerGSMCell getScannerGSMNeighborCellsConfig() {
		String scnGSMName = androidPreferences.getString("scanner_config_gsm_neighbor_cells_scanner_name", "scannerGsmRss");
		boolean scnGSMisActive = androidPreferences.getBoolean("scanner_config_gsm_neighbor_cells_scanner_is_active", true);
		boolean scnGSMSynchronize = androidPreferences.getBoolean("scanner_config_gsm_neighbor_cells_scanner_synchronize", true);
		int scnGSMThreshold = androidPreferences.getInt("scanner_config_gsm_gsm_neighbor_cells_threshold", 10);
		long scnGSMTimeout = androidPreferences.getLong("scanner_config_gsm_gsm_neighbor_cells_timeout", 1000);
		int scnGSMRefreshInterval = androidPreferences.getInt("scanner_config_gsm_neighbor_cells_scanner_refresh_interval", 1000);
		ConfigDataCommands scnGSMCommands = getGSM_DataProcessCommands();
		ConfigScannerGSMCell scnGSM = new ConfigScannerGSMCell(scnGSMName, scnGSMisActive, scnGSMSynchronize, scnGSMThreshold, scnGSMTimeout, scnGSMCommands, scnGSMRefreshInterval);
		//TODO
		return scnGSM;
	}

	private ConfigDataCommands getGSM_DataProcessCommands() {
		boolean gsm_RSS_MeanCommand = androidPreferences.getBoolean("scanner_config_gsm_commands_mean_command", true);
		HashMap<String, Boolean> dataCommands = new HashMap<String, Boolean>();
		dataCommands.put("MeanCommandGSM", gsm_RSS_MeanCommand);
		ConfigDataCommands gsmRSSdPCommands = new ConfigDataCommands(dataCommands);
		return gsmRSSdPCommands;
	}
}