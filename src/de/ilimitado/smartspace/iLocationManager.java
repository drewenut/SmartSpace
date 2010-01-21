package de.ilimitado.smartspace;

import java.util.ArrayList;
import java.util.List;

import de.ilimitado.smartspace.positioning.Accuracy;
import de.ilimitado.smartspace.positioning.IGeoPoint;
import de.ilimitado.smartspace.positioning.WeightedIGeoPoint;
import de.ilimitado.smartspace.utils.L;

public class iLocationManager {
	
	private ArrayList<iLocationListener> listeners = new ArrayList<iLocationListener>();
	private IGeoPoint currPos = new IGeoPoint(0, 0);
	private Accuracy currAcc = new Accuracy(Accuracy.DEFAULT_ACCURACY);
	private ArrayList<IGeoPoint> iGPRankedList = new ArrayList<IGeoPoint>(0);
	
	public iLocationManager() { }
	
	public IGeoPoint getCurrentPosition() {
		return currPos;
	}
	
	public List<IGeoPoint> getCurrentPositionList(){
		return iGPRankedList;
	}
	
	public void registerListener(iLocationListener iPP){
		listeners.add(iPP);
	}
	
	public void unregisterListener(iLocationListener iPP){
		if(listeners.contains(iPP))
			listeners.remove(iPP);
	}
	
	public boolean listenersEmpty(){
		return listeners.isEmpty();
	}
	
	private void notifyLocationChanged(List<IGeoPoint> iGPList, Accuracy acc){
		for(iLocationListener listener : listeners){
			listener.onLocationChanged(iGPList, acc);
		}
	}
	
	private void notifyLocationChanged(IGeoPoint iGP, Accuracy acc){
		for(iLocationListener listener : listeners){
			listener.onLocationChanged(iGP, acc);
		}
	}
	
	private void notifyStateChanged(int state){
		for(iLocationListener listener : listeners){
			listener.onStateChanged(state);
		}
	}
	
	public void setPosition(IGeoPoint iGP, Accuracy acc){
		if(currPos != iGP || currAcc != acc){	
			currPos = iGP;
			currAcc = acc;
			notifyLocationChanged(iGP, acc);
		}
	}

	public void enqueueEstimatedLocation(List<WeightedIGeoPoint> locationRanking, Accuracy acc) {
		if(locationRanking.size() > 0) {
			L.sd("iLocationManager", "Location Ranking:");
			for(WeightedIGeoPoint iGP : locationRanking) {
				L.sd("", iGP.toString());
			}
			setPosition(locationRanking.get(0).igp, acc);
			iGPRankedList = new ArrayList<IGeoPoint>(locationRanking.size()); 
			for (WeightedIGeoPoint wGP : locationRanking) {
				iGPRankedList.add(wGP.igp);
			}
			notifyLocationChanged(iGPRankedList, acc);
		}
	}
}
