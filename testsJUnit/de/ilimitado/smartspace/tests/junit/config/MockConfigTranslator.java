package de.ilimitado.smartspace.tests.junit.config;

import java.util.HashMap;

import de.ilimitado.smartspace.config.ConfigScanner80211Passive;
import de.ilimitado.smartspace.config.ConfigTranslator;
import de.ilimitado.smartspace.config.Configuration;
import de.ilimitado.smartspace.config.ConfigDataCommands;
import de.ilimitado.smartspace.config.ConfigSensing;
import de.ilimitado.smartspace.config.ConfigPersistence;
import de.ilimitado.smartspace.config.ConfigLocalization;
import de.ilimitado.smartspace.config.ConfigScannerGSMCell;
import de.ilimitado.smartspace.config.ConfigSensor80211;

public class MockConfigTranslator implements ConfigTranslator{
	
	private static MockConfigTranslator instance;

	private MockConfigTranslator() {}
	
	public static MockConfigTranslator getInstance() {
		return instance == null 
				? instance = new MockConfigTranslator()
				: instance;
	}
	
	public void translate() {
		ConfigLocalization algos = getPositionAlgorithms();
		ConfigPersistence persConf = getPersistanceConfig();
		ConfigSensing fptColl = getFPTCollection();
		ConfigSensor80211 snsCfg80211 = getSensorConfig80211();

		Configuration.createConfiguration(algos, persConf, fptColl, snsCfg80211);
	}

	private ConfigLocalization getPositionAlgorithms() {
		boolean useEuklid = true;
		ConfigLocalization algos = new ConfigLocalization(useEuklid);
		return algos;
	}

	private ConfigPersistence getPersistanceConfig() {
		int lfptPM = 0;
		long refreshIntervall = 0;
		String dbName = "TestDB";
		int bufferSize = 10;
		ConfigPersistence persConf = new ConfigPersistence(dbName, lfptPM, bufferSize, refreshIntervall);
		return persConf;
	}

	private ConfigSensing getFPTCollection() {
		int oriQuantCount = 4;
		ConfigSensing fptColl = new ConfigSensing(oriQuantCount);
		return fptColl;
	}

	private ConfigSensor80211 getSensorConfig80211() {
		String sensor80211name = "sensor80211";
		boolean sensor80211isActive = true;
		ConfigScanner80211Passive scn80211passive = getScanner80211PassiveConfig();
		ConfigSensor80211 snsCfg80211 = new ConfigSensor80211(sensor80211name, sensor80211isActive, scn80211passive);
		return snsCfg80211;
	}

	private ConfigScanner80211Passive getScanner80211PassiveConfig() {
		String passive80211Name = "Scanner80211Passive";
		boolean passive80211isActive = true;
		boolean passive80211synchronize = true;
		int passive80211threshold = 20;
		long passive80211timeout = 1000;
		ConfigDataCommands passive80211dPCommands = getDataProcessCommands();
		ConfigScanner80211Passive scn80211passive = new ConfigScanner80211Passive(passive80211Name, passive80211isActive, passive80211synchronize, passive80211threshold, passive80211timeout, passive80211dPCommands);
		return scn80211passive;
	}

	private ConfigDataCommands getDataProcessCommands() {
		boolean passive80211MeanCommand80211 = true;
		HashMap<String, Boolean> dataCommands = new HashMap<String, Boolean>();
		dataCommands.put("MeanCommand80211", passive80211MeanCommand80211);
		ConfigDataCommands passive80211dPCommands = new ConfigDataCommands(dataCommands);
		return passive80211dPCommands;
	}

}
