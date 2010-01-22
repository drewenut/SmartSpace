package de.ilimitado.smartspace.positioning;

import java.util.List;

public interface LocationProvider {
	
	public void calculateLocation(Object data);
	public List<?> getEstimatedLocations();
}
