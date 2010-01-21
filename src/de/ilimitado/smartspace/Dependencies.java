package de.ilimitado.smartspace;

import android.content.Context;
import de.ilimitado.smartspace.persistance.PersistanceManager;

public class Dependencies {

	public final SensorDependencies sensorDependencies;
	public final Context androidDependencies;
	
	public final MotionDetector motionDetector;
	public final PersistanceManager persistanceManager;
	public final iLocationManager positionManager;

	public Dependencies(Context androidDep, SensorDependencies sDep, MotionDetector mtnDet, PersistanceManager persMngr, iLocationManager posMngr) {
		this.sensorDependencies = sDep;
		this.androidDependencies = androidDep;
		this.motionDetector = mtnDet;
		this.persistanceManager = persMngr;
		this.positionManager = posMngr;
	}
}
