/**
 * 
 */
package de.ilimitado.smartspace.config;


public class Scanner80211PassiveConfig extends AbstractScannerConfig {
	private final static String Event_ID = "Scanner80211Passive";
	
	public Scanner80211PassiveConfig(String name, boolean isActive, boolean synchronize, int thres,
			long timeoutInMS, DataProcessorCommands dp) {
		super(Event_ID, name, isActive, synchronize, thres, timeoutInMS, dp);
		if(isActive)
			ConfigSensor80211.scanners.add(this);
	}
}