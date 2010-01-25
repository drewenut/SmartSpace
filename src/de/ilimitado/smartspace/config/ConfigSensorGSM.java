package de.ilimitado.smartspace.config;


public class ConfigSensorGSM extends AbstractSensorConfig{
	private final static String SENSOR_ID = "SensorGSM";
	
	public final ConfigScannerGSMCell scannerGSMCells;

	public String scanSelection;

	public  ConfigSensorGSM(String SENSOR_NAME, boolean isActive, ConfigScannerGSMCell scannerGSMCells, String scanSelection) {
		super(SENSOR_ID, SENSOR_NAME, isActive);
		this.scannerGSMCells = scannerGSMCells;
		this.scanSelection = scanSelection;
			if(isActive)
				Configuration.sensorConfigs.add(this);
	}
}