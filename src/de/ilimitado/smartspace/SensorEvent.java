package de.ilimitado.smartspace;

import java.util.List;

public class SensorEvent<E> {
	private final List<E> eventHandle;
	private final String eventType;

	public SensorEvent(List<E> handle, String eventType) {
		this.eventHandle = handle;
		this.eventType = eventType;
	}

	public String getEventType() {
		return eventType;
	}

	public List<E> getEventHandle() {
		return eventHandle;
	}
}
