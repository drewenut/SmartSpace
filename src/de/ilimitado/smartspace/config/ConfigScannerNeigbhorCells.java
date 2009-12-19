package de.ilimitado.smartspace.config;

public class ConfigScannerNeigbhorCells extends AbstractScannerConfig {
	private final static String Event_ID = "ScannerGSMNeigbhorCells";
	//Scan interval in ms
	private final int cellScanInterval;
	
	public ConfigScannerNeigbhorCells(String name, boolean isActive, boolean synchronize, int thres,
			long timeoutInMS, ConfigDataCommands dp) {
		super(Event_ID, name, isActive, synchronize, thres, timeoutInMS, dp);
		//Default value: 1 sec
		this.cellScanInterval = 1000;
		if(isActive)
			ConfigSensorGSM.scanners.add(this);
	}
	
	public ConfigScannerNeigbhorCells(String name, boolean isActive, boolean synchronize, int thres,
			long timeoutInMS, ConfigDataCommands dp, int cellScanInterval) {
		super(Event_ID, name, isActive, synchronize, thres, timeoutInMS, dp);
		this.cellScanInterval = cellScanInterval;
		if(isActive)
			ConfigSensorGSM.scanners.add(this);
	}
}
