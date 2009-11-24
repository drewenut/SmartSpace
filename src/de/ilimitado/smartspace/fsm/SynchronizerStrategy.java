package de.ilimitado.smartspace.fsm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.ilimitado.smartspace.ScanSampleList;
import de.ilimitado.smartspace.SensorDataSample;
import de.ilimitado.smartspace.config.Configuration;

public abstract class SynchronizerStrategy {
	
	protected static final int THREAD_POOL_SIZE = Configuration.getInstance().fptCollection.lfpt.SYNCHRONIZER_THREADPOOL_SIZE;
	protected Configuration config;
	protected List<String> syncSet;

	public SynchronizerStrategy(List<String> syncSet) {
		this.config = Configuration.getInstance();
		this.syncSet = syncSet;
	}
	
	public List<String> getSyncSet() {
		return syncSet;
	}
	
	public abstract HashMap<String, ScanSampleList> processData(Map<String, List<List<?>>> sensorData);
	
	public abstract SensorDataSample fusionate(HashMap<String, ScanSampleList> resultSamples);

	public abstract void deploySampleDataSample(SensorDataSample sDSpl);
}
