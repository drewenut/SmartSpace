package de.ilimitado.smartspace.dataprocessors;


public interface DataCommand<I, O> {
	public void execute();
	public void setStdIn(I stdIn);
	public void setStdOut(O stdOut);
}
