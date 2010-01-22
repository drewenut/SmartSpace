package de.ilimitado.smartspace.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import de.ilimitado.smartspace.LFPT;
import de.ilimitado.smartspace.RTFPT;
import de.ilimitado.smartspace.ScanSample;
import de.ilimitado.smartspace.ScanSampleList;

public class EuclideanDistance {

	public static double calculateDistance(Double[] point1, Double[] point2) {
		return Math.sqrt(calculateSquareDistance(point1, point2));
	}

	private static Double calculateSquareDistance(Double[] point1, Double[] point2) {
		if (point1.length == point2.length) {
			Double sum = 0D;
			for (int i = 0; i < point1.length; i++) {
				sum = sum + (point2[i] - point1[i]) * (point2[i] - point1[i]);
			}
			return sum;
		} else {
			throw new IllegalArgumentException("Euclidean distance Exception, here is what i know: array lengths were not equal");
		}
	}
  
	public static class ArrayLengthMatcher {

		private static ConcurrentHashMap<String, ScanSampleList> allRtSamples;
		private static ConcurrentHashMap<String, ScanSampleList> allLFPTSamples;
		private static ScanSampleList rtSamples;
		private static ScanSampleList LFPTSamples;
		private static DataSet<ScanSample> rtDataSet;
		private static DataSet<ScanSample> LFPTDataSet;

		public static List<List<Double>> match(RTFPT rtFPT, LFPT rmLFPT) {
			resetInvariants(rtFPT, rmLFPT);
			complementAllSamples();
			addAllSamplesToResultData();
			return getData();
		}

		private static void resetInvariants(RTFPT rtFPT, LFPT rmLFPT) {
			allRtSamples = rtFPT.getScanSampleLists();
			allLFPTSamples = rmLFPT.getScanSampleLists();
			rtDataSet = new DataSet<ScanSample>();
			LFPTDataSet = new DataSet<ScanSample>();
			rtSamples = new ScanSampleList();
			LFPTSamples = new ScanSampleList();
		}

		private static void complementAllSamples() {
			complementRtSamplesWithLFPTSamples();
			complementLFPTSamplesWithRtSamples();
		}
		private static void complementRtSamplesWithLFPTSamples() {
			for (String sampleID : allRtSamples.keySet()) {
				if (!allLFPTSamples.containsKey(sampleID)) {
					allLFPTSamples.put(sampleID, new ScanSampleList());
				}
			}
		}
		private static void complementLFPTSamplesWithRtSamples() {
			for (String sampleID : allLFPTSamples.keySet()) {
				if (!allRtSamples.containsKey(sampleID)) {
					allRtSamples.put(sampleID, new ScanSampleList());
				}
			}
		}

		private static void addAllSamplesToResultData() {
			for (String sampleID : allLFPTSamples.keySet()) {
				LFPTSamples = allLFPTSamples.get(sampleID);
				rtSamples = allRtSamples.get(sampleID);
				addAllRtSamplesToResultData();
				addAllLFPTSamplesToResultData();
			}
		}
		private static void addAllRtSamplesToResultData() {
			for (ScanSample splToAdd : rtSamples) {
				if (LFPTSamples.containsSample(splToAdd.getSampleID())) {
					ScanSample correspondingSpl = (ScanSample) LFPTSamples.get(splToAdd.getSampleID());
					rtDataSet.add(splToAdd);
					LFPTDataSet.add(correspondingSpl);
				} else {
					LFPTDataSet.add(splToAdd.getDefaultScanSample());
					rtDataSet.add(splToAdd);
				}
			}
		}
		private static void addAllLFPTSamplesToResultData() {
			for (ScanSample splToAdd : LFPTSamples) {
				if (rtSamples.containsSample(splToAdd.getSampleID())) {
					ScanSample correspondingSpl = (ScanSample) rtSamples.get(splToAdd.getSampleID());
					LFPTDataSet.add(splToAdd);
					rtDataSet.add(correspondingSpl);
				} else {
					LFPTDataSet.add(splToAdd);
					rtDataSet.add(splToAdd.getDefaultScanSample());
				}
			}
		}
		
		private static List<List<Double>> getData() {
			
			List<Double> rtData = new ArrayList<Double>();
			for (ScanSample sSpl: rtDataSet) {
				rtData.addAll(sSpl.ToArray());
			}
			
			List<Double> LFPTData = new ArrayList<Double>();
			for (ScanSample sSpl: LFPTDataSet) {
				LFPTData.addAll(sSpl.ToArray());
			}
			
			List<List<Double>> data = new ArrayList<List<Double>>();
			data.add(rtData);
			data.add(LFPTData);
			return data;
		}
		
	}

}