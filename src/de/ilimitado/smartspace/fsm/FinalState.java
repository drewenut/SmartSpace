package de.ilimitado.smartspace.fsm;

import de.ilimitado.smartspace.Dependencies;
import de.ilimitado.smartspace.SensorManager;

public class FinalState implements State {

	private SensorManager sensorManager;

	@Override
	public void enterState(Dependencies context) {
		sensorManager = context.sensorDependencies.sensorManager;
	}
	
	@Override
	public void doActivity() {
		if(sensorManager.sensorsStarted()){
			sensorManager.stopSensors();
		}
	}

	@Override
	public void exitState() { }

	@Override
	public State switchNextState(boolean pos, boolean mtn) {
		return this;
	}

}
