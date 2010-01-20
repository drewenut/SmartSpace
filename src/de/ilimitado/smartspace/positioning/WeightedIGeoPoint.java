package de.ilimitado.smartspace.positioning;


public class WeightedIGeoPoint implements Comparable<WeightedIGeoPoint> {
	public final IGeoPoint igp;
	public final double score;

	public WeightedIGeoPoint(IGeoPoint iGP, double score) {
		igp = iGP;
		this.score = score;
	}

	@Override
	public int compareTo(WeightedIGeoPoint another) {
		return  (int) (score - another.score);
	}
	
	@Override
	public String toString() {
		return "Score: " + score + "iGP: " + igp;
	}
	
}
