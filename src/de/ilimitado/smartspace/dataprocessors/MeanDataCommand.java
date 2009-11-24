package de.ilimitado.smartspace.dataprocessors;

public abstract class MeanDataCommand<I, O> implements DataCommand<I, O> {
	protected I stdIn;
	protected O stdOut;
	
	public void execute() {
		computeMean(stdIn);
		writeToStdOut();
	}

	protected I getStdIn() {
		return stdIn;
	}
	
	protected O getStdOut() {
		return stdOut;
	}
	
	public void setStdIn(I stdIn) {
		this.stdIn = stdIn;
	}
	
	public void setStdOut(O stdOut){
		this.stdOut = stdOut;
	}
	
	protected abstract void writeToStdOut();
	protected abstract void computeMean(I sensorData);
}
