package de.ilimitado.smartspace;

import java.util.ArrayList;
import java.util.List;

import de.ilimitado.smartspace.positioning.Accuracy;
import de.ilimitado.smartspace.positioning.IGeoPoint;
import de.ilimitado.smartspace.positioning.WeightedIGeoPoint;

public class PositionManager {
	
	private ArrayList<IndoorPositionListener> listeners = new ArrayList<IndoorPositionListener>();
	private IGeoPoint currPos = new IGeoPoint(0, 0);
	private Accuracy currAcc = new Accuracy(Accuracy.DEFAULT_ACCURACY);
	private ArrayList<IGeoPoint> iGPRankedList = new ArrayList<IGeoPoint>(0);
	
	public PositionManager() { }
	
	public IGeoPoint getCurrentPosition() {
		return currPos;
	}
	
	public List<IGeoPoint> getCurrentPositionList(){
		return iGPRankedList;
	}
	
	public void registerListener(IndoorPositionListener iPP){
		listeners.add(iPP);
	}
	
	public void unregisterListener(IndoorPositionListener iPP){
		if(listeners.contains(iPP))
			listeners.remove(iPP);
	}
	
	public boolean listenersEmpty(){
		return listeners.isEmpty();
	}
	
	private void notifyLocationChanged(List<IGeoPoint> iGPList, Accuracy acc){
		for(IndoorPositionListener listener : listeners){
			listener.onLocationChanged(iGPList, acc);
		}
	}
	
	private void notifyLocationChanged(IGeoPoint iGP, Accuracy acc){
		for(IndoorPositionListener listener : listeners){
			listener.onLocationChanged(iGP, acc);
		}
	}
	
	private void notifyStateChanged(int state){
		for(IndoorPositionListener listener : listeners){
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

	public void enqueueEstimatedPosition(List<WeightedIGeoPoint> positionRanking, Accuracy acc) {
		//TODO handle empty list (example if database is empty...)
		setPosition(positionRanking.get(0).igp, acc);
		iGPRankedList = new ArrayList<IGeoPoint>(positionRanking.size()); 
		for (WeightedIGeoPoint wGP : positionRanking) {
			iGPRankedList.add(wGP.igp);
		}
		notifyLocationChanged(iGPRankedList, acc);
	}
}
