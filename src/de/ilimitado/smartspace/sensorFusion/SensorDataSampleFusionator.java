package de.ilimitado.smartspace.sensorFusion;

import java.util.HashMap;

import de.ilimitado.smartspace.ScanSampleList;
import de.ilimitado.smartspace.SensorDataSample;

public class SensorDataSampleFusionator implements SensorFusionator {
	
	private SensorDataSample fpt;
	
	public SensorDataSampleFusionator(SensorDataSample fpt) {
		this.fpt = fpt;
	}

	@Override
	public void fusionate(HashMap<String, ScanSampleList> resultSamples) {
        for(String key : resultSamples.keySet()){
        	fpt.add(key, resultSamples.get(key));
        }
	}

	@Override
	public SensorDataSample getResult() {
		return fpt;
	}

}
