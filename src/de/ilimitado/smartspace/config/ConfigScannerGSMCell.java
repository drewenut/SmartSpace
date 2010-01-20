/**
 * 
 */
package de.ilimitado.smartspace.config;


public class ConfigScannerGSMCell extends AbstractScannerConfig {
	private final static String Event_ID = "ScannerGSMActiveCell";
	
	public int refreshInterval = 1000;
	
	public ConfigScannerGSMCell(String name, boolean isActive, boolean synchronize, int thres,
			long timeoutInMS, ConfigDataCommands dp, int scnGSMRefreshInterval) {
		super(Event_ID, name, isActive, synchronize, thres, timeoutInMS, dp);
		this.refreshInterval = scnGSMRefreshInterval;
		if(isActive)
			ConfigSensorGSM.scanners.add(this);
	}
}