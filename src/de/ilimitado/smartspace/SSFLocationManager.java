package de.ilimitado.smartspace;

import de.ilimitado.smartspace.positioning.Accuracy;
import de.ilimitado.smartspace.positioning.IGeoPoint;


public class SSFLocationManager {

	private final SensorManager sMngr;
	private final IndoorLocationManager indrLocMngr;
	
	public SSFLocationManager(IndoorLocationManager posMngr, SensorManager sMngr) {
		this.indrLocMngr = posMngr;
		this.sMngr = sMngr;
	}
	
	public IGeoPoint getCurrentPosition() {
		return indrLocMngr.getCurrentPosition();
	}
	
	public void setCurrentPosition(IGeoPoint iGP, Accuracy acc) {
		indrLocMngr.setPosition(iGP, acc);
	}
	
	public void registerListener(IndoorLocationListener iPP){
		indrLocMngr.registerListener(iPP);
	}
	
	public void unregisterListener(IndoorLocationListener iPP){
		indrLocMngr.unregisterListener(iPP);
	}
}
