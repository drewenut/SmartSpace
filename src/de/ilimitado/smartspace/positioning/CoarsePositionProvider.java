package de.ilimitado.smartspace.positioning;

import java.util.List;

public interface CoarsePositionProvider {
	
	public void calculatePosition(Object data);
	public List<?> getEstimatedPositions();
}
