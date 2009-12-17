package de.ilimitado.smartspace.sensor.sensorGSM;

import java.util.ArrayList;
import java.util.List;

import de.ilimitado.smartspace.RSS;
import de.ilimitado.smartspace.ScanSample;
import de.ilimitado.smartspace.persistance.Component;
import de.ilimitado.smartspace.persistance.ValueMap;
import de.ilimitado.smartspace.persistance.ValueMapContainer;
import de.ilimitado.smartspace.utils.L;

public class ScanSampleGSM implements ScanSample{
	
	public static final String VALUE_CID = "CID";
	public static final String VALUE_PROVIDER = "PROVIDER";
	private static final String LOG_TAG = "ScanSampleGSm";

	public String CID;
	public String PROVIDER;
	public RSS rss;
	public int meanFrequency;
	private boolean isMerged = true;
	
	public ScanSampleGSM(String CID, String PROVIDER, RSS rss) {
		this.CID = CID;
		this.PROVIDER = PROVIDER;
		this.rss = rss;
	}
	
	@Override
	public boolean equals(Object o) {
		if((o instanceof ScanSampleGSM)){
			ScanSampleGSM scanSplGSM = (ScanSampleGSM) o;
			return (CID.equals(scanSplGSM.CID)) ? true : false;
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		String none = "<empty>";

		
		
		sb.append("CID: ")
		  .append(CID == null ? none : CID)
		  .append(", PROVIDER: ")
		  .append(PROVIDER == null ? none : PROVIDER)
		  .append(", RSS: ")
		  .append(rss.toString());

		return sb.toString();
	}

	@Override
	public ValueMapContainer toValue() {
		ValueMap scanSampleValues = new ValueMap();
		scanSampleValues.setKey(getSampleID());
		
		ValueMap rssValues = (ValueMap) rss.toValue();
		scanSampleValues.merge(rssValues);
		
		scanSampleValues.putString(VALUE_CID, CID);
		scanSampleValues.putString(VALUE_PROVIDER, PROVIDER);
		return scanSampleValues;
	}

	@Override
	public String getSampleID() {
		return PROVIDER+"-"+CID;
	}

	@Override
	public void fromValue(ValueMapContainer vmc) {
		ValueMap values = (ValueMap) vmc;
		CID = values.getAsString(VALUE_CID);
		PROVIDER = values.getAsString(VALUE_PROVIDER);
		rss = new RSS();
		rss.fromValue(values);
	}

	@Override
	public void add(String key, Component value) {
		throw new UnsupportedOperationException("You can not add a component to a leaf!");
	}

	@Override
	public Component get(String key) {
		throw new UnsupportedOperationException("You can not get a component from a leaf!");
	}

	@Override
	public List<Double> ToArray() {
		ArrayList<Double> sampleNumbers = new ArrayList<Double>();
		sampleNumbers.addAll(rss.ToArray());
		return sampleNumbers;
	}
	
	@Override
	public void merge(ScanSample sSpl) {
		ScanSampleGSM sSplGSM = (ScanSampleGSM) sSpl;
		rss.mean = (sSplGSM.rss.mean + rss.mean) / 2;
		rss.variance = (sSplGSM.rss.variance + rss.variance) / 2;
		rss.deviation = (sSplGSM.rss.deviation + rss.deviation) / 2;
		rss.ssd = (sSplGSM.rss.ssd + rss.ssd) / 2;
		//TODO Log until variance and deviation are computed and don't forget to remove LOG_TAG
		L.d(LOG_TAG, "Merged " + CID + " mean diff: " + (rss.mean - sSplGSM.rss.mean)); 
	}

	@Override
	public boolean mergeable(ScanSample sSpl) {
		return equals(sSpl);
	}

	@Override
	public boolean isMerged() {
		return isMerged;
	}

	@Override
	public void setMerged(boolean isMerged) {
		this.isMerged = isMerged;
	}

	@Override
	public ScanSample getDefaultScanSample() {
		return new ScanSampleGSM(CID, PROVIDER, new RSS());
	}
}