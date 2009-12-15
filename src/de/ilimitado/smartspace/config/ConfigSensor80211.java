/**
 * 
 */
package de.ilimitado.smartspace.config;


public class ConfigSensor80211 extends AbstractSensorConfig{
	private final static String SENSOR_ID = "Sensor80211";
	
	public final Scanner80211PassiveConfig scanner80211PassiveConf;
	
	public  ConfigSensor80211(String SENSOR_NAME, boolean isActive, Scanner80211PassiveConfig scn80211P) {
		super(SENSOR_ID, SENSOR_NAME, isActive);
		this.scanner80211PassiveConf = scn80211P;
			if(isActive)
				Configuration.sensorConfigs.add(this);
	}
}