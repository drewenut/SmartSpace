package de.ilimitado.smartspace;

import de.ilimitado.smartspace.positioning.Accuracy;
import de.ilimitado.smartspace.positioning.IGeoPoint;
import de.ilimitado.smartspace.positioning.iLocationListener;
import de.ilimitado.smartspace.positioning.iLocationManager;


public class SSFLocationManager {

	private final iLocationManager indrLocMngr;
	
	public SSFLocationManager(iLocationManager posMngr) {
		this.indrLocMngr = posMngr;
	}
	
	public IGeoPoint getCurrentPosition() {
		return indrLocMngr.getCurrentPosition();
	}
	
	public void setCurrentPosition(IGeoPoint iGP, Accuracy acc) {
		indrLocMngr.setPosition(iGP, acc);
	}
	
	public void registerListener(iLocationListener iPP){
		indrLocMngr.registerListener(iPP);
	}
	
	public void unregisterListener(iLocationListener iPP){
		indrLocMngr.unregisterListener(iPP);
	}
}
