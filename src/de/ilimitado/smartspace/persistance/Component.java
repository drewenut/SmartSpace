package de.ilimitado.smartspace.persistance;

import java.util.List;

public interface Component {
	public ValueMapContainer toValue();
	public List<Double> ToArray();
}
