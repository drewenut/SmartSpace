package de.ilimitado.smartspace.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.ilimitado.smartspace.sensing.DataProcessorsMap;

public abstract class AbstractSensorConfig{
	public final String ID;
	public String NAME;
	public final boolean isActive;
	protected static List<AbstractScannerConfig> scanners = new ArrayList<AbstractScannerConfig>(); 
	
	public  AbstractSensorConfig(String SENSOR_ID, String SENSOR_NAME, boolean isActive) {
		this.ID = SENSOR_ID;
		this.NAME = SENSOR_NAME;
		this.isActive = isActive;
	}
	
	ConstraintsMap<String, List<Number>> getSensorConstraints(ConstraintsMap<String, List<Number>> constraintMap){
		for(AbstractScannerConfig scanner : scanners){
			constraintMap.put(scanner.ID, scanner.getSensingConstraints());
		}
		return constraintMap;
	}

	DataProcessorsMap<String, HashMap<String, Boolean>> getSensorDataProcessors(DataProcessorsMap<String, HashMap<String, Boolean>> dPmap) {
		for(AbstractScannerConfig scanner : scanners){
			if(scanner.isActive)
				dPmap.put(scanner.ID, scanner.getDataProcessors());
		}
		return dPmap;
	}
	
	ArrayList<String> getSensorSyncList(ArrayList<String> syncList){
		for(AbstractScannerConfig scanner : scanners){
			if(scanner.isActive && scanner.isSynchronized())
				syncList.add(scanner.ID);
		}
		return syncList;
	}
}