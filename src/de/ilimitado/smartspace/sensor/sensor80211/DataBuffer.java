package de.ilimitado.smartspace.sensor.sensor80211;

public class DataBuffer {
	public StringBuffer buffer;
	public String name;

	public DataBuffer(String id) {
		this.buffer = new StringBuffer();
		this.name = id;
	}
}
