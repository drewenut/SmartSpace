/**
 * 
 */
package de.ilimitado.smartspace.config;


public class ConfigScannerActive extends AbstractScannerConfig {
	private final static String Event_ID = "ScannerGSMActiveCell";
	
	public ConfigScannerActive(String name, boolean isActive, boolean synchronize, int thres,
			long timeoutInMS, ConfigDataCommands dp) {
		super(Event_ID, name, isActive, synchronize, thres, timeoutInMS, dp);
		if(isActive)
			ConfigSensorGSM.scanners.add(this);
	}
}