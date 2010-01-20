package de.ilimitado.smartspace.sensing;


public interface DataCommand<I, O> {
	public void execute();
	public void setStdIn(I stdIn);
	public void setStdOut(O stdOut);
}
