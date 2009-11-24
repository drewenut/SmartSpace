/**
 * 
 */
package de.ilimitado.smartspace.config;

import java.util.HashMap;

public class DataProcessorCommands {
	
	public final HashMap<String, Boolean> dataCommands;

	public DataProcessorCommands(HashMap<String, Boolean> dataCommands) {
		this.dataCommands = dataCommands;
	}
	
	HashMap<String, Boolean> getDataProcessorCommands(){
		return dataCommands;
	}
}