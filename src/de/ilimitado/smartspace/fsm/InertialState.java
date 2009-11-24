package de.ilimitado.smartspace.fsm;

import de.ilimitado.smartspace.Dependencies;

public class InertialState implements State {

	@Override
	public void doActivity() {
		// TODO implement

	}

	@Override
	public void enterState(Dependencies context) {
		// TODO implement

	}

	@Override
	public void exitState() {
		// TODO implement

	}

	@Override
	public State switchNextState(boolean pos, boolean mtn) {
	 
		if(!pos && !mtn)
			return new RealtimeState();
		else if(pos && !mtn)
			return new LearningState();
		
		//if(mtn) 
		return this;
		
	}

}
