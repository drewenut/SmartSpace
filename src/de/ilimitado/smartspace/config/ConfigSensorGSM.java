package de.ilimitado.smartspace.config;

public class ConfigSensorGSM extends AbstractSensorConfig{
	private final static String SENSOR_ID = "SensorGSM";
	
	public final ConfigScannerActive scannerActiveCell;
	public final ConfigScannerNeigbhorCells scannerNeigbhorCells;

	public  ConfigSensorGSM(String SENSOR_NAME, boolean isActive, ConfigScannerActive scnActiveCell, ConfigScannerNeigbhorCells scnNeigbhorCells) {
		super(SENSOR_ID, SENSOR_NAME, isActive);
		this.scannerActiveCell = scnActiveCell;
		this.scannerNeigbhorCells = scnNeigbhorCells;
			if(isActive)
				Configuration.sensorConfigs.add(this);
	}
	
	public  ConfigSensorGSM(String SENSOR_NAME, boolean isActive, ConfigScannerActive scnActiveCell) {
		super(SENSOR_ID, SENSOR_NAME, isActive);
		this.scannerActiveCell = scnActiveCell;
		this.scannerNeigbhorCells = null;
			if(isActive)
				Configuration.sensorConfigs.add(this);
	}
	
	public  ConfigSensorGSM(String SENSOR_NAME, boolean isActive, ConfigScannerNeigbhorCells scnNeigbhorCells) {
		super(SENSOR_ID, SENSOR_NAME, isActive);
		this.scannerActiveCell = null;
		this.scannerNeigbhorCells = scnNeigbhorCells;
			if(isActive)
				Configuration.sensorConfigs.add(this);
	}
}
