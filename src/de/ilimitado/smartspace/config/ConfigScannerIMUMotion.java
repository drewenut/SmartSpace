package de.ilimitado.smartspace.config;

public class ConfigScannerIMUMotion {
	private final static String Event_ID = "IMU_Motion_ID";
	
	public final String ID;
	public final String NAME;
	
	public final boolean isActive;
	public final double MTN_SENSITIVITY;
	public final int QUEUE_CAPACITY;
	
	public ConfigScannerIMUMotion(String name, boolean isActive, double sensivity, int queueCapacity) {
		this.ID = Event_ID;
		this.NAME = name;
		this.isActive = isActive;
		this.MTN_SENSITIVITY = sensivity;
		this.QUEUE_CAPACITY = queueCapacity;
		if(isActive)
			ConfigSensorIMU.scanners.add(this);
	}
}