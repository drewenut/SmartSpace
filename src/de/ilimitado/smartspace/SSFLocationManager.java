package de.ilimitado.smartspace;

import de.ilimitado.smartspace.positioning.Accuracy;
import de.ilimitado.smartspace.positioning.IGeoPoint;


public class SSFLocationManager {

	private final SensorManager sMngr;
	private final PositionManager posMngr;
	
	public SSFLocationManager(PositionManager posMngr, SensorManager sMngr) {
		this.posMngr = posMngr;
		this.sMngr = sMngr;
	}
	
	public IGeoPoint getCurrentPosition() {
		return posMngr.getCurrentPosition();
	}
	
	public void setCurrentPosition(IGeoPoint iGP, Accuracy acc) {
		posMngr.setPosition(iGP, acc);
	}
	
	public void registerListener(SSFLocationListener iPP){
		posMngr.registerListener(iPP);
	}
	
	public void unregisterListener(SSFLocationListener iPP){
		posMngr.unregisterListener(iPP);
	}
}
