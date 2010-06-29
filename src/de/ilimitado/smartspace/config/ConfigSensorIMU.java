package de.ilimitado.smartspace.config;

import java.util.ArrayList;
import java.util.List;

public class ConfigSensorIMU extends AbstractSensorConfig{
	private final static String SENSOR_ID = "SensorIMU";
	
	public final ConfigScannerIMUMotion scannerMotion;
	protected static List<ConfigScannerIMUMotion> scanners = new ArrayList<ConfigScannerIMUMotion>(); 

	public  ConfigSensorIMU(String SENSOR_NAME, boolean isActive, ConfigScannerIMUMotion scnMotion) {
		super(SENSOR_ID, SENSOR_NAME, isActive);
		this.scannerMotion = scnMotion;
			if(isActive)
				Configuration.sensorConfigs.add(this);
	}
}
