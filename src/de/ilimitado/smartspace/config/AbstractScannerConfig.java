package de.ilimitado.smartspace.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AbstractScannerConfig{
	public final String ID;
	public final String NAME;
	
	public final boolean isActive;
	public final boolean synchronize;
	public final int sampleCollectionThreshold;
	public final long timeout;
	public final DataProcessorCommands dataProcessorCmds;
	
	protected AbstractScannerConfig(String ID, String name, boolean isActive, boolean synchronize, int sCThres, long timeoutInMS, DataProcessorCommands dP) {
		this.ID = ID;
		this.NAME = name;
		this.isActive = isActive;
		this.synchronize = synchronize;
		this.sampleCollectionThreshold = sCThres;
		this.timeout = timeoutInMS;
		this.dataProcessorCmds = dP;
	}
	
	HashMap<String, Boolean> getDataProcessors(){
		return dataProcessorCmds.getDataProcessorCommands();
	}
	
	List<Number> getSensingConstraints() {
		ArrayList<Number> constraints = new ArrayList<Number>();
		constraints.add(sampleCollectionThreshold);
		constraints.add(timeout);
		return constraints;
	}
	
	boolean isSynchronized(){
		return synchronize; 
	}
}
