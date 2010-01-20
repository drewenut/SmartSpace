package de.ilimitado.smartspace.persistance;

public interface Visitor {
	void handleValueMap(ValueMap values, String valueMapContainerName);
	void handleValueMapContainer(ValueMapContainer valueMapContainer, String valueMapContainerName);
}
