package de.ilimitado.smartspace.fsm;

import de.ilimitado.smartspace.Dependencies;

public interface State {
	public void enterState(Dependencies dep);
	public void doActivity();
	public void exitState();
	public State switchNextState(boolean pos, boolean mtn);
}
