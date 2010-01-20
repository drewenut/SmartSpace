package de.ilimitado.smartspace.tests.android.fsm;

import de.ilimitado.smartspace.Dependencies;
import de.ilimitado.smartspace.fsm.LearningState;
import de.ilimitado.smartspace.fsm.State;

public class MockLearningState extends LearningState {
	
	@Override
	public void doActivity() {
	}
	
	@Override
	public void enterState(Dependencies dep) {
	}
	
	@Override
	public void exitState() {
	}
	
	@Override
	public State switchNextState(boolean pos, boolean mtn) {
	 
		if(mtn)
			return new MockInertialState();
		else if(!pos && !mtn)
			return new MockRealtimeState();
		
		//if(pos && !mtn) 
		return this;
	}
	

}
