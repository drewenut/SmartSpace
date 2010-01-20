package de.ilimitado.smartspace.config;


public class ConfigSensor80211 extends AbstractSensorConfig{
	private final static String SENSOR_ID = "Sensor80211";
	
	public final ConfigScanner80211Passive scanner80211;
	
	public  ConfigSensor80211(String SENSOR_NAME, boolean isActive, ConfigScanner80211Passive scn80211P) {
		super(SENSOR_ID, SENSOR_NAME, isActive);
		this.scanner80211 = scn80211P;
			if(isActive)
				Configuration.sensorConfigs.add(this);
	}
}