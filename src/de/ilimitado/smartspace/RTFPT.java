package de.ilimitado.smartspace;


public class RTFPT extends FPT{

	public RTFPT(long creationTime, double orientation) {
		super(creationTime, orientation);
	}
	
	@Override
	public String toString() {
		return "RTFPT: " + super.toString();
	}
	
}
