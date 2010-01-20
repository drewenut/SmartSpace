package de.ilimitado.smartspace.config;


public class ConfigSensorGSM extends AbstractSensorConfig{
	private final static String SENSOR_ID = "SensorGSM";
	
	public final ConfigScannerGSMCell scannerGSMCells;

	public  ConfigSensorGSM(String SENSOR_NAME, boolean isActive, ConfigScannerGSMCell scannerGSMCells) {
		super(SENSOR_ID, SENSOR_NAME, isActive);
		this.scannerGSMCells = scannerGSMCells;
			if(isActive)
				Configuration.sensorConfigs.add(this);
	}
}
