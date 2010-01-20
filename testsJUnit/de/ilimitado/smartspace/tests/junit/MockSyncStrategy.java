package de.ilimitado.smartspace.tests.junit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.ilimitado.smartspace.ScanSampleList;
import de.ilimitado.smartspace.SensorDataSample;
import de.ilimitado.smartspace.fsm.SynchronizerStrategy;

public class MockSyncStrategy extends SynchronizerStrategy {

	private int processDataCalls;
	private int fusionateSamplesCalls;
	private int deploySampleDataCalls;

	public MockSyncStrategy(List<String> syncSet) {
		super(syncSet);
	}

	@Override
	public SensorDataSample fusionate(HashMap<String, ScanSampleList> resultSamples) {
		fusionateSamplesCalls++;
		return null;
	}

	public int getProcessDataCalls() {
		return processDataCalls;
	}

	public int getFusionateSamplesCalls() {
		return fusionateSamplesCalls;
	}
	
	public int getDeploySampleDataCalls() {
		return deploySampleDataCalls;
	}

	@Override
	public HashMap<String, ScanSampleList> processData(
			Map<String, List<List<?>>> sensorData) {
		processDataCalls++;
		return null;
	}

	@Override
	public void deploySampleDataSample(SensorDataSample spl) {
		deploySampleDataCalls++;
	}

}
