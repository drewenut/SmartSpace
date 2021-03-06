package de.ilimitado.smartspace.sensor.sensorGSM;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import de.ilimitado.smartspace.RSS;
import de.ilimitado.smartspace.ScanSampleList;
import de.ilimitado.smartspace.sensing.MeanDataCommand;

public class MeanCommandGSM extends MeanDataCommand<Collection<?>, ScanSampleList>{
	HashMap<String, List<Integer>> meanValuesMap = new HashMap<String, List<Integer>>();
	HashMap<String, ScanResultGSM> prototypeMap = new HashMap<String, ScanResultGSM>();
	
	@SuppressWarnings("unchecked")
	@Override
	protected void computeMean(Collection<?> scanResults80211) {
		List<List<ScanResultGSM>> scanResults80211list = (List<List<ScanResultGSM>>) scanResults80211;
		sortScanResults(scanResults80211list);
		processScanResults();
	}
	
	@Override
	protected void writeToStdOut() {
		for(String scnResID : prototypeMap.keySet()){
			ScanResultGSM scnRes = prototypeMap.get(scnResID);
			ScanSampleGSM sSpl = new ScanSampleGSM(scnRes.CID, scnRes.PROVIDER, new RSS(scnRes.level));
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
			List<List<ScanResultGSM>> scanResults80211list) {
		for (List<ScanResultGSM> result : scanResults80211list){
			for (ScanResultGSM scanRes : result) {
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

	private void createNewScnResEntries(ScanResultGSM scanRes) {
		List<Integer> scnResValues = new ArrayList<Integer>();
		scnResValues.add(scanRes.level);
		String ID = scanRes.CID;
		meanValuesMap.put(ID, scnResValues);
		prototypeMap.put(ID, scanRes);
	}

	private boolean addRSSLevel(ScanResultGSM scanRes) {
		if(meanValuesMap.containsKey(scanRes.CID)) {
			List<Integer> scnResValues = meanValuesMap.get(scanRes.CID);
			return scnResValues.add(scanRes.level);
		}
		return false;
	}
}
