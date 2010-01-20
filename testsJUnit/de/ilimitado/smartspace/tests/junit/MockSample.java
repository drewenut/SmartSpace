package de.ilimitado.smartspace.tests.junit;

import java.util.List;

import de.ilimitado.smartspace.ScanSample;
import de.ilimitado.smartspace.persistance.Component;
import de.ilimitado.smartspace.persistance.ValueMap;
import de.ilimitado.smartspace.persistance.ValueMapContainer;

public class MockSample implements ScanSample {

	public static final String STRING_VALUE = "String";
	public static final int INTEGER_VALUE = 10;
	public static final String INTEGER_KEY = "MockInteger";
	public static final String STRING_KEY = "MockString";

	public String string;
	public int integer;
	
	@Override
	public String getSampleID() {
		return "MockSample";
	}

	@Override
	public ValueMapContainer toValue() {
		ValueMap valueMap = new ValueMap();
		valueMap.setKey("MockSample");
		valueMap.putString(STRING_KEY, STRING_VALUE);
		valueMap.putInteger(INTEGER_KEY, INTEGER_VALUE);
		return valueMap;
	}

	@Override
	public void fromValue(ValueMapContainer values) {
		ValueMap valueMap =  (ValueMap) values;
		string = valueMap.getAsString(STRING_KEY);
		integer = valueMap.getAsInteger(INTEGER_KEY);
	}

	@Override
	public void add(String key, Component value) {
	}

	@Override
	public Component get(String key) {
		return null;
	}

	@Override
	public List<Double> ToArray() {
		return null;
	}

	@Override
	public void merge(ScanSample sSpl) {
	}

	@Override
	public boolean mergeable(ScanSample sSpl) {
		return false;
	}

	@Override
	public boolean isMerged() {
		return false;
	}

	@Override
	public void setMerged(boolean isMerged) {
		
	}

	@Override
	public ScanSample getDefaultScanSample() {
		return null;
	}

}
