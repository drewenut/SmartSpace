package de.ilimitado.smartspace.config;

public class ConfigSensorGSM extends AbstractSensorConfig{
	private final static String SENSOR_ID = "SensorGSM";
	
	public final ConfigScannerGSMRSS scannerGSM_RSS;
	
	public  ConfigSensorGSM(String SENSOR_NAME, boolean isActive, ConfigScannerGSMRSS scnRSS) {
		super(SENSOR_ID, SENSOR_NAME, isActive);
		this.scannerGSM_RSS = scnRSS;
			if(isActive)
				Configuration.sensorConfigs.add(this);
	}
}
