package de.ilimitado.smartspace.sensor.sensor80211;

import java.util.ArrayList;
import java.util.List;

import de.ilimitado.smartspace.RSS;
import de.ilimitado.smartspace.ScanSample;
import de.ilimitado.smartspace.persistance.Component;
import de.ilimitado.smartspace.persistance.ValueMap;
import de.ilimitado.smartspace.persistance.ValueMapContainer;
import de.ilimitado.smartspace.utils.L;

public class ScanSample80211 implements ScanSample{
	
	public static final String VALUE_MEAN_FREQUENCY = "MeanFrequency";
	public static final String VALUE_SSID = "SSID";
	public static final String VALUE_MAC = "MAC";
	private static final String LOG_TAG = "ScanSample80211";

	public String SSID;
	public String MAC;
	public RSS rss;
	public int meanFrequency;
	private boolean isMerged = true;
	
	public ScanSample80211(String SSID, String BSSID, RSS rss, int frequency) {
		this.SSID = SSID;
		this.MAC = BSSID;
		this.rss = rss;
		this.meanFrequency = frequency;
	}
	
	@Override
	public boolean equals(Object o) {
		if((o instanceof ScanSample80211)){
			ScanSample80211 scanSample80211 = (ScanSample80211) o;
			return (MAC.equals(scanSample80211.MAC) && SSID.equals(scanSample80211.SSID)) ? true : false;
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		String none = "<empty>";

		
		
		sb.append("SSID: ").append(SSID == null ? none : SSID)
				.append(", MAC: ").append(MAC == null ? none : MAC).append(
						", RSS: ").append(rss.toString()).append(
						", Mean frequency: ").append(meanFrequency);

		return sb.toString();
	}

	@Override
	public ValueMapContainer toValue() {
		ValueMap scanSampleValues = new ValueMap();
		scanSampleValues.setKey(getSampleID());
		
		ValueMap rssValues = (ValueMap) rss.toValue();
		scanSampleValues.merge(rssValues);
		
		scanSampleValues.putInteger(VALUE_MEAN_FREQUENCY, meanFrequency);
		scanSampleValues.putString(VALUE_SSID, SSID);
		scanSampleValues.putString(VALUE_MAC, MAC);
		return scanSampleValues;
	}

	@Override
	public String getSampleID() {
		return MAC+"-"+SSID;
	}

	@Override
	public void fromValue(ValueMapContainer vmc) {
		ValueMap values = (ValueMap) vmc;
		meanFrequency = values.getAsInteger(VALUE_MEAN_FREQUENCY);
		SSID = values.getAsString(VALUE_SSID);
		MAC = values.getAsString(VALUE_MAC);
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
		ScanSample80211 sSpl80211 = (ScanSample80211) sSpl;
		meanFrequency = (sSpl80211.meanFrequency + meanFrequency) / 2;
		rss.mean = (sSpl80211.rss.mean + rss.mean) / 2;
		rss.variance = (sSpl80211.rss.variance + rss.variance) / 2;
		rss.deviation = (sSpl80211.rss.deviation + rss.deviation) / 2;
		rss.ssd = (sSpl80211.rss.ssd + rss.ssd) / 2;
		//TODO Log until variance and deviation are computed and don't forget to remove LOG_TAG
		L.d(LOG_TAG, "Merged " + SSID + " mean diff: " + (rss.mean - sSpl80211.rss.mean)); 
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
		return new ScanSample80211(SSID, MAC, new RSS(), meanFrequency);
	}

}
