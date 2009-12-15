package de.ilimitado.smartspace.config;

import java.util.HashMap;

public class ConfigDataCommands {
	
	public final HashMap<String, Boolean> dataCommands;

	public ConfigDataCommands(HashMap<String, Boolean> dataCommands) {
		this.dataCommands = dataCommands;
	}
	
	HashMap<String, Boolean> getDataProcessorCommands(){
		return dataCommands;
	}
}