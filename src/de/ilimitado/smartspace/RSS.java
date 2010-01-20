package de.ilimitado.smartspace;

import java.util.ArrayList;
import java.util.List;

import de.ilimitado.smartspace.persistance.Component;
import de.ilimitado.smartspace.persistance.ValueMap;
import de.ilimitado.smartspace.persistance.ValueMapContainer;

public class RSS implements Component{

	//TODO Make values changeable via Configuration object	
	private static final double DEFAULT_RSS_MEAN = -120.0;
	private static final double DEFAULT_RSS_VARIANCE = 0.0;
	private static final double DEFAULT_RSS_DEVIATION = 0.0;
	private static final double DEFAULT_RSS_SSD = 0.0;
	
	public static final String VALUE_RSS_MEAN = "RSSMean";
	public static final String VALUE_RSS_DEVIATION = "RSSDeviation";
	public static final String VALUE_RSS_SSD = "RSSSSD";
	public static final String VALUE_RSS_VARIANCE = "RSSVariance";
	
	private static final double EMPTY = 0;
	
	public double mean;
	public double variance;
	public double deviation;
	public double ssd;

	public RSS(double mean, double var, double dev, double ssd) {
		this.mean = mean;
		this.variance = var;
		this.deviation = dev;
		this.ssd = ssd;
	}

	public RSS(double mean, double var, double dev) {
		this(mean, var, dev, EMPTY);
	}

	public RSS(double mean, double var) {
		this(mean, var, EMPTY, EMPTY);
	}

	public RSS(double mean) {
		this(mean, EMPTY, EMPTY, EMPTY);
	}

	public RSS() {
		this(DEFAULT_RSS_MEAN, DEFAULT_RSS_VARIANCE, DEFAULT_RSS_DEVIATION, DEFAULT_RSS_SSD);
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		String none = "<empty>";

		sb.append("RSS_Mean: ").append(mean == EMPTY ? none : mean).append(
				", RSS_Variance: ").append(variance == EMPTY ? none : variance)
				.append(", RSS_Deviation: ").append(
						deviation == EMPTY ? none : deviation).append(
						", RSS_SSD: ").append(ssd);

		return sb.toString();
	}

	@Override
	public ValueMapContainer toValue() {
		ValueMap rssValues = new ValueMap();
		rssValues.putDouble(VALUE_RSS_MEAN, mean);
		rssValues.putDouble(VALUE_RSS_DEVIATION, deviation);
		rssValues.putDouble(VALUE_RSS_SSD, ssd);
		rssValues.putDouble(VALUE_RSS_VARIANCE, variance);
		return rssValues;
	}

	@Override
	public List<Double> ToArray() {
		ArrayList<Double> rssNumbers = new ArrayList<Double>();
		rssNumbers.add(((Number) mean).doubleValue());
		rssNumbers.add(((Number) variance).doubleValue());
		rssNumbers.add(((Number) deviation).doubleValue());
		rssNumbers.add(((Number) ssd).doubleValue());
		return rssNumbers;
	}

	public void fromValue(ValueMapContainer vmc) {
		ValueMap rssValues = (ValueMap) vmc;
		mean = rssValues.getAsDouble(VALUE_RSS_MEAN);
		deviation = rssValues.getAsDouble(VALUE_RSS_DEVIATION);
		ssd = rssValues.getAsDouble(VALUE_RSS_SSD);
		variance = rssValues.getAsDouble(VALUE_RSS_VARIANCE);
	}

	public List<Double> getDefaultArray() {
		ArrayList<Double> rssNumbers = new ArrayList<Double>();
		rssNumbers.add(DEFAULT_RSS_MEAN);
		rssNumbers.add(DEFAULT_RSS_VARIANCE);
		rssNumbers.add(DEFAULT_RSS_DEVIATION);
		rssNumbers.add(DEFAULT_RSS_SSD);
		return rssNumbers;
	}
	
}
