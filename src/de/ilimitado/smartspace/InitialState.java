package de.ilimitado.smartspace;

import de.ilimitado.smartspace.fsm.InertialState;
import de.ilimitado.smartspace.fsm.LearningState;
import de.ilimitado.smartspace.fsm.RealtimeState;
import de.ilimitado.smartspace.fsm.State;

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
