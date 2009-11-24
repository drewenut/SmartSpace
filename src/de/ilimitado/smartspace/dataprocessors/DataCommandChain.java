package de.ilimitado.smartspace.dataprocessors;

import java.util.ArrayList;
import java.util.Collection;


public class DataCommandChain<I extends Collection<?>, O>{
	
	protected I stdIn = null;
	protected O stdOut = null;
	private ArrayList<DataCommand<I, O>> dataCmdChain;
	
	public DataCommandChain() {
		dataCmdChain = new ArrayList<DataCommand<I, O>>();
	}
	
	public DataCommandChain<I,O> addCommand(DataCommand<I, O> cmd) {
		dataCmdChain.add(cmd);
		return this;
	}

	public void executeCommands(){
		for (DataCommand<I, O> cmd : dataCmdChain) {
			cmd.execute();
		}
	}
	
	public I getStdIn() {
		return stdIn;
	}

	public void setStdIn(I stdIn) {
		this.stdIn = stdIn;
		for (DataCommand<I, O> dC : dataCmdChain) {
			dC.setStdIn(stdIn);
		}
	}

	public O getStdOut() {
		return stdOut;
	}

	public void setStdOut(O stdOut) {
		this.stdOut = stdOut;
		for (DataCommand<I, O> dC : dataCmdChain) {
			dC.setStdOut(stdOut);
		}
	}
}
