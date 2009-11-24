package de.ilimitado.smartspace;


public interface Syncable {
	public void setSyncronizer(EventSynchronizer sensorSynchronizer);
	public void commitData();
}
