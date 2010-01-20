package de.ilimitado.smartspace;

import java.util.HashMap;


public class SensorFusionator{
	
	private SensorDataSample fpt;
	
	public SensorFusionator(SensorDataSample fpt) {
		this.fpt = fpt;
	}

	public void fusionate(HashMap<String, ScanSampleList> resultSamples) {
        for(String key : resultSamples.keySet()){
        	fpt.add(key, resultSamples.get(key));
        }
	}

	public SensorDataSample getResult() {
		return fpt;
	}

}
