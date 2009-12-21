package de.ilimitado.smartspace;


public class SensorEvent<E> {
	private final E eventHandle;
	private final String eventType;
	private final String eventSensor;

	public SensorEvent(E handle, String eventType, String eventSensor) {
		this.eventHandle = handle;
		this.eventType = eventType;
		this.eventSensor = eventSensor;
	}

	public String getEventSensor() {
		return eventSensor;
	}
	
	public String getEventType() {
		return eventType;
	}

	public E getEventHandle() {
		return eventHandle;
	}
}
