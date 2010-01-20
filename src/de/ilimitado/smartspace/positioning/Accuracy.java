package de.ilimitado.smartspace.positioning;

public class Accuracy {

	public static final int DEFAULT_ACCURACY = 0;
	public static final int LOW_ACCURACY = -1;
	public static final int HIGH_ACCURACY = 1;
	
	
	public final int accuracy;	
	
	public Accuracy(int acc) {
		this.accuracy = acc;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Accuracy){
			return this.accuracy == ((Accuracy) o).accuracy;
		}
		return false;
	}

}
