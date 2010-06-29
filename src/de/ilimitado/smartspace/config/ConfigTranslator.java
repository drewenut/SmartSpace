package de.ilimitado.smartspace.config;

public abstract class ConfigTranslator {

	protected ConfigLocalization algos;
	protected ConfigPersistence persConf;
	protected ConfigSensing sensingConf;
	protected ConfigSensor80211 snsCfg80211;
	protected ConfigSensorGSM snsCfgGSM;
	protected ConfigSensorIMU snsCfgIMU;

	public void create() {
		Configuration.createConfiguration(algos, persConf, sensingConf,
				snsCfg80211, snsCfgGSM, snsCfgIMU);
	}
	public abstract void translate();
}
