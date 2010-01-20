package de.ilimitado.smartspace.positioning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.ilimitado.smartspace.LFPT;
import de.ilimitado.smartspace.RTFPT;
import de.ilimitado.smartspace.RadioMap;
import de.ilimitado.smartspace.utils.EuclideanDistance;
import de.ilimitado.smartspace.utils.L;

public class SimpleEuclideanDistanceProvider implements PositionProvider{
	
	private static final String LOG_TAG = "SimpleEuclideanDistanceProvider";
	
	private RadioMap radioMap;
	private ArrayList<WeightedIGeoPoint> positionRanking;

	public SimpleEuclideanDistanceProvider(RadioMap rm) {
		this.radioMap = rm;
	}

	public void calculatePosition(Object rtfpt) {
		RTFPT rtFPT = (RTFPT) rtfpt;
		positionRanking = new ArrayList<WeightedIGeoPoint>(radioMap.size());
		for (LFPT rmLFPT : radioMap) {
			List<List<Double>> equalSizedArrays = EuclideanDistance.ArrayLengthMatcher.match(rtFPT, rmLFPT);
			Double[] rtfptValues = convertToDoubleArray(equalSizedArrays.get(0));
			Double[] lfptValues = convertToDoubleArray(equalSizedArrays.get(1));
			double distance = EuclideanDistance.calculateDistance(rtfptValues, lfptValues);
			L.d(LOG_TAG, "Distance to "+ rmLFPT.getIndoorGP() +": " + distance);
			WeightedIGeoPoint wPoint = new WeightedIGeoPoint(rmLFPT.getIndoorGP(), distance);
			positionRanking.add(wPoint);
		};
		Collections.sort(positionRanking);
	}
	
	public List<WeightedIGeoPoint> getEstimatedPositions() {
		return positionRanking;
	}
	
	private Double[] convertToDoubleArray(List<Double> dl){
		Double[] values = new Double[dl.size()];
		for (int i = 0; i < dl.size(); i++) {
			values[i] = dl.get(i);
		}
		return values;
	}
}
