package de.ilimitado.smartspace.tests.android.fsm;

import de.ilimitado.smartspace.fsm.RealtimeState;
import de.ilimitado.smartspace.fsm.State;

public class MockRealtimeState extends RealtimeState {

	@Override
	public State switchNextState(boolean pos, boolean mtn) {
	 
		if(mtn)
			return new MockInertialState();
		else if(pos && !mtn)
			return new MockLearningState();
		
		//if(!pos && !mtn) 
		return this;
		
	}

	
}
