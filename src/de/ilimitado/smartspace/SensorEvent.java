package de.ilimitado.smartspace;


public class SensorEvent<E> {
	private final E eventHandle;
	private final String eventType;

	public SensorEvent(E handle, String eventType) {
		this.eventHandle = handle;
		this.eventType = eventType;
	}

	public String getEventType() {
		return eventType;
	}

	public E getEventHandle() {
		return eventHandle;
	}
}
