package de.ilimitado.smartspace.tests.android.fsm;

import de.ilimitado.smartspace.fsm.InertialState;
import de.ilimitado.smartspace.fsm.State;

public class MockInertialState extends InertialState{

	@Override
	public State switchNextState(boolean pos, boolean mtn) {
	 
		if(!pos && !mtn)
			return new MockRealtimeState();
		else if(pos && !mtn)
			return new MockLearningState();
		
		//if(mtn) 
		return this;
		
	}
	
}
