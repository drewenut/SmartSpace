package de.ilimitado.smartspace.fsm;

import java.util.List;

import de.ilimitado.smartspace.Accuracy;
import de.ilimitado.smartspace.Dependencies;
import de.ilimitado.smartspace.IGeoPoint;
import de.ilimitado.smartspace.IndoorPositionListener;

public class FSM {
	
	boolean fPos = false;
	boolean mtn = false;
	private State currentState;
	private State nextState;
	private Dependencies dependencies;
	
	private IndoorPositionListener iPL = new IndoorPositionListener(){

		@Override
		public void onLocationChanged(IGeoPoint location, Accuracy acc) {
			if(acc.accuracy == Accuracy.HIGH_ACCURACY)		
				fixedPositionAction(true);
			else
				fixedPositionAction(false);
		}

		@Override
		public void onLocationChanged(List<IGeoPoint> list, Accuracy acc) {
			if(acc.accuracy == Accuracy.HIGH_ACCURACY && !list.isEmpty())		
				fixedPositionAction(true);
			else
				fixedPositionAction(false);
		}

		@Override
		public void onStateChanged(int state) {	}
		
	};

	
	public FSM(State initialState, Dependencies dep){
		this.dependencies = dep;
		this.currentState = initialState;
		this.nextState = null;
		dep.positionManager.registerListener(iPL);
		enterState();
		if(checkForTransition())
			doTransition();
	}
	
	public void fixedPositionAction(boolean fpos){
		if(this.fPos != fpos){
			this.fPos = fpos;
			if(checkForTransition()) 
				doTransition();
		}
	}
	
	public void motionAction(boolean mtn){
		if(this.mtn != mtn){
			this.mtn = mtn;
			if(checkForTransition()) 
				doTransition();
		}
	}
	
	public void motionAndFixedPositionAction(boolean mtn, boolean fpos){
		this.mtn = mtn;
		this.fPos = fpos;
		if(checkForTransition()) 
			doTransition();
	}
	
	public void killFSM(){
		nextState = new FinalState();
		doTransition();
	}
	
	private boolean checkForTransition(){
		nextState = currentState.switchNextState(fPos, mtn);
		return (nextState.getClass() == currentState.getClass()) ? false : true;
	}
	
	private void doTransition(){
		exitState();
		nextState.enterState(dependencies);
		currentState = nextState;
		currentState.doActivity();
	}
	
	public void doActivity(){
		this.currentState.doActivity();
	}
	
	private void enterState(){
		this.currentState.enterState(dependencies);
	}
	
	private void exitState(){
		this.currentState.exitState();
	}

	public State getCurrentState() {
		return currentState;
	}
	
	public State getNextState() {
		return nextState;
	}

}
