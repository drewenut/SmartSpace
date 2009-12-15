package de.ilimitado.smartspace.sensor.sensor80211;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import de.ilimitado.smartspace.ScanSampleList;
import de.ilimitado.smartspace.sensing.MeanDataCommand;

public class MeanCommand80211 extends MeanDataCommand<Collection<?>, ScanSampleList>{
	HashMap<String, List<Integer>> meanValuesMap = new HashMap<String, List<Integer>>();
	HashMap<String, ScanResult80211> prototypeMap = new HashMap<String, ScanResult80211>();
	
	@SuppressWarnings("unchecked")
	@Override
	protected void computeMean(Collection<?> scanResults80211) {
		List<List<ScanResult80211>> scanResults80211list = (List<List<ScanResult80211>>) scanResults80211;
		sortScanResults(scanResults80211list);
		processScanResults();
	}
	
	@Override
	protected void writeToStdOut() {
		for(String scnResID : prototypeMap.keySet()){
			ScanResult80211 scnRes = prototypeMap.get(scnResID);
			ScanSample80211 sSpl = new ScanSample80211(scnRes.SSID, scnRes.BSSID, new RSS(scnRes.level), scnRes.frequency);
			if(!stdOut.contains(sSpl))
				stdOut.add(sSpl);
		}
	}

	private void processScanResults() {
		for(String scnResID : meanValuesMap.keySet()) {
			computeMean(scnResID);
		}
	}

	private void sortScanResults(
			List<List<ScanResult80211>> scanResults80211list) {
		for (List<ScanResult80211> result : scanResults80211list){
			for (ScanResult80211 scanRes : result) {
				if(!addRSSLevel(scanRes)) {
					createNewScnResEntries(scanRes);
				}
			}
		}
	}

	private void computeMean(String scnResID) {
		List<Integer> rssLevels = meanValuesMap.get(scnResID);
		int levelsSum = 0;
		int valueCount = rssLevels.size();
		for(Integer level : rssLevels){
			levelsSum += level;
		}
		int meanRSSlevel = levelsSum/valueCount;
		prototypeMap.get(scnResID).level = meanRSSlevel;
	}

	private void createNewScnResEntries(ScanResult80211 scanRes) {
		List<Integer> scnResValues = new ArrayList<Integer>();
		scnResValues.add(scanRes.level);
		String ID = scanRes.BSSID;
		meanValuesMap.put(ID, scnResValues);
		prototypeMap.put(ID, scanRes);
	}

	private boolean addRSSLevel(ScanResult80211 scanRes) {
		if(meanValuesMap.containsKey(scanRes.BSSID)) {
			List<Integer> scnResValues = meanValuesMap.get(scanRes.BSSID);
			return scnResValues.add(scanRes.level);
		}
		return false;
	}
}
