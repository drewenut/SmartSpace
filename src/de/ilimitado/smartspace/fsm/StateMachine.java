package de.ilimitado.smartspace.fsm;

public interface StateMachine {
	public boolean checkForTransition();
	public void doTransition();
}
