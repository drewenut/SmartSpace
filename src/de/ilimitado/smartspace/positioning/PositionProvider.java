package de.ilimitado.smartspace.positioning;

import java.util.List;

public interface PositionProvider {
	
	public void calculatePosition(Object data);
	public List<?> getEstimatedPositions();
}
