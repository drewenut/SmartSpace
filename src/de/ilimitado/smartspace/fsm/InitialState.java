package de.ilimitado.smartspace.fsm;

import de.ilimitado.smartspace.Dependencies;

public class InitialState implements State {

	@Override
	public void doActivity() { }

	@Override
	public void enterState(Dependencies context) {
		System.gc();
	}

	@Override
	public void exitState() { }

	@Override
	public State switchNextState(boolean pos, boolean mtn) {
		if (pos && mtn)
			return new InertialState();
		else if (pos && !mtn)
			return new LearningState();
		else if (!pos && mtn)
			return new InertialState();
		else  // !pos && !mtn
			return new RealtimeState();
	}
}
