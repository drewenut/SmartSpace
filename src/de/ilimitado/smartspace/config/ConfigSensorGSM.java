package de.ilimitado.smartspace.config;

import java.util.HashMap;

import de.ilimitado.smartspace.sensing.DataProcessorsMap;

public class ConfigSensorGSM extends AbstractSensorConfig{
	private final static String SENSOR_ID = "SensorGSM";
	private final HashMap<String, Boolean> dataCommands = new HashMap<String, Boolean>();
	
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
	
	DataProcessorsMap<String, HashMap<String, Boolean>> getSensorDataProcessors(DataProcessorsMap<String, HashMap<String, Boolean>> dPmap) {
		dPmap.put(SENSOR_ID, dataCommands.getDataProcessors());
		return dPmap;
	}
}
