package de.ilimitado.smartspace.sensor.sensorGSM;

import java.util.List;

import de.ilimitado.smartspace.ScanSample;
import de.ilimitado.smartspace.persistance.Component;
import de.ilimitado.smartspace.persistance.ValueMapContainer;

public class ScanSampleGSM implements ScanSample{

	@Override
	public void fromValue(ValueMapContainer values) {
		// TODO Auto-generated method stub
	}

	@Override
	public ScanSample getDefaultScanSample() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSampleID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isMerged() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void merge(ScanSample sSpl) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean mergeable(ScanSample sSpl) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setMerged(boolean isMerged) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void add(String key, Component value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Component get(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Double> ToArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueMapContainer toValue() {
		// TODO Auto-generated method stub
		return null;
	}

}
