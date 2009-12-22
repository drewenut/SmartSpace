package de.ilimitado.smartspace.config;

import java.util.HashMap;
import java.util.List;

import de.ilimitado.smartspace.sensing.DataProcessorsMap;

public class ConfigSensorGSM extends AbstractSensorConfig{
	private final static String SENSOR_ID = "SensorGSM";
	
	private HashMap<String, Boolean> dataCommands = new HashMap<String, Boolean>();
	
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
		for(AbstractScannerConfig scanner : scanners){
			merge(scanner.getDataProcessors());
		}
		dPmap.put(SENSOR_ID, dataCommands);
		return dPmap;
	}
	
	private void merge(HashMap<String, Boolean> dataProcessors) {
			for(String processorName : dataProcessors.keySet()) {
				if(!dataCommands.containsKey(processorName))
					dataCommands.put(processorName, dataProcessors.get(processorName));
			}
	}

	ConstraintsMap<String, List<Number>> getSensorConstraints(ConstraintsMap<String, List<Number>> constraintMap){
		for(AbstractScannerConfig scanner : scanners){
			constraintMap.put(scanner.ID, scanner.getSensingConstraints());
		}
		return constraintMap;
	}
}
