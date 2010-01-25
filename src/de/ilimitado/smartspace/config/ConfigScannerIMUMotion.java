package de.ilimitado.smartspace.config;

public class ConfigScannerIMUMotion {
	private final static String Event_ID = "ScannerIMUActiveCell";
	
	public final String ID;
	public final String NAME;
	
	public final boolean isActive;
	
	public ConfigScannerIMUMotion(String name, boolean isActive) {
		this.ID = Event_ID;
		this.NAME = name;
		this.isActive = isActive;
		if(isActive)
			ConfigSensorIMU.scanners.add(this);
	}
}