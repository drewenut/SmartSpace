package de.ilimitado.smartspace.fsm;

import de.ilimitado.smartspace.Dependencies;
import de.ilimitado.smartspace.utils.L;

public class InitialState extends InertialState {
	//@Override
	protected final String LOG_TAG = "InitialState";
	
	@Override
	public void enterState(Dependencies dep) {
		super.enterState(dep);
		L.d(LOG_TAG, "Entering Initial state...");
	}

	@Override
	public void exitState() {
		super.exitState();
		L.d(LOG_TAG, "Exiting Initial state...");
	}

	@Override
	public State switchNextState(boolean pos, boolean mtn) {
		if (pos && mtn) {
			L.d(LOG_TAG, "Switching to Inertial state...");
			return new InertialState();
		}
		else if (pos && !mtn) {
			L.d(LOG_TAG, "Switching to Learning state...");
			return new LearningState();
		}
		else if (!pos && mtn) {
			L.d(LOG_TAG, "Switching to Inertial state...");
			return new InertialState();
		}
		else {
			L.d(LOG_TAG, "Self Transition Initial State...");
			// !pos && !mtn
			return this;
		}
	}
}
