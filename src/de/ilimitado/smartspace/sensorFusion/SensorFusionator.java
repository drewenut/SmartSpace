package de.ilimitado.smartspace.sensorFusion;

import java.util.HashMap;

import de.ilimitado.smartspace.ScanSampleList;
import de.ilimitado.smartspace.SensorDataSample;

public interface SensorFusionator {
	
	public void fusionate(HashMap<String, ScanSampleList>  resultSamples);
	public SensorDataSample getResult();
}
