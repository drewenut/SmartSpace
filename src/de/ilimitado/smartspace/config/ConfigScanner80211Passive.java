/**
 * 
 */
package de.ilimitado.smartspace.config;


public class ConfigScanner80211Passive extends AbstractScannerConfig {
	private final static String Event_ID = "Scanner80211Passive";
	
	public ConfigScanner80211Passive(String name, boolean isActive, boolean synchronize, int thres,
			long timeoutInMS, ConfigDataCommands dp) {
		super(Event_ID, name, isActive, synchronize, thres, timeoutInMS, dp);
		if(isActive)
			ConfigSensor80211.scanners.add(this);
	}
}