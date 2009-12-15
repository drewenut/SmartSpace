package de.ilimitado.smartspace.tests.junit.config;

import java.util.HashMap;

import de.ilimitado.smartspace.config.ConfigTranslator;
import de.ilimitado.smartspace.config.Configuration;
import de.ilimitado.smartspace.config.DataProcessorCommands;
import de.ilimitado.smartspace.config.Sensing;
import de.ilimitado.smartspace.config.Persistance;
import de.ilimitado.smartspace.config.Localization;
import de.ilimitado.smartspace.config.Scanner80211PassiveConfig;
import de.ilimitado.smartspace.config.Sensor80211;

public class MockConfigTranslator implements ConfigTranslator{
	
	private static MockConfigTranslator instance;

	private MockConfigTranslator() {}
	
	public static MockConfigTranslator getInstance() {
		return instance == null 
				? instance = new MockConfigTranslator()
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
		boolean useEuklid = true;
		Localization algos = new Localization(useEuklid);
		return algos;
	}

	private Persistance getPersistanceConfig() {
		int lfptPM = 0;
		long refreshIntervall = 0;
		String dbName = "TestDB";
		int bufferSize = 10;
		Persistance persConf = new Persistance(lfptPM, refreshIntervall, dbName, bufferSize);
		return persConf;
	}

	private Sensing getFPTCollection() {
		int oriQuantCount = 4;
		Sensing fptColl = new Sensing(oriQuantCount);
		return fptColl;
	}

	private Sensor80211 getSensorConfig80211() {
		String sensor80211name = "sensor80211";
		boolean sensor80211isActive = true;
		Scanner80211PassiveConfig scn80211passive = getScanner80211PassiveConfig();
		Sensor80211 snsCfg80211 = new Sensor80211(sensor80211name, sensor80211isActive, scn80211passive);
		return snsCfg80211;
	}

	private Scanner80211PassiveConfig getScanner80211PassiveConfig() {
		String passive80211Name = "Scanner80211Passive";
		boolean passive80211isActive = true;
		boolean passive80211synchronize = true;
		int passive80211threshold = 20;
		long passive80211timeout = 1000;
		DataProcessorCommands passive80211dPCommands = getDataProcessCommands();
		Scanner80211PassiveConfig scn80211passive = new Scanner80211PassiveConfig(passive80211Name, passive80211isActive, passive80211synchronize, passive80211threshold, passive80211timeout, passive80211dPCommands);
		return scn80211passive;
	}

	private DataProcessorCommands getDataProcessCommands() {
		boolean passive80211MeanCommand80211 = true;
		HashMap<String, Boolean> dataCommands = new HashMap<String, Boolean>();
		dataCommands.put("MeanCommand80211", passive80211MeanCommand80211);
		DataProcessorCommands passive80211dPCommands = new DataProcessorCommands(dataCommands);
		return passive80211dPCommands;
	}

}
