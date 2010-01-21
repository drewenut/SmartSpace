package de.ilimitado.smartspace.positioning;

import java.util.List;

public interface PositionProvider {
	
	public void calculateLocation(Object data);
	public List<?> getEstimatedLocations();
}
