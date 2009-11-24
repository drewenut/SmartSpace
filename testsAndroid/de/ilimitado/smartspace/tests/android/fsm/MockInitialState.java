package de.ilimitado.smartspace.tests.android.fsm;

import de.ilimitado.smartspace.InitialState;
import de.ilimitado.smartspace.fsm.State;

public class MockInitialState extends InitialState {

	@Override
	public State switchNextState(boolean pos, boolean mtn) {
		if (pos && mtn)
			return new MockInertialState();
		else if (pos && !mtn)
			return new MockLearningState();
		else if (!pos && mtn)
			return new MockInertialState();
		else  // !pos && !mtn
			return new MockRealtimeState();
	}
	
}
