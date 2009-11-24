package de.ilimitado.smartspace.persistance;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class ValueMapContainer{

	private HashMap<String, ValueMapContainer> containers;
	private String key = "";

	public ValueMapContainer() {
		this.containers = new HashMap<String, ValueMapContainer>();
	}
	
	public void put(String key, ValueMapContainer value) {
		containers.put(key, value);
		value.setKey(key);
	}
	
	public ValueMapContainer get(String key) {
		return containers.get(key);
	}
	
	public HashMap<String, ValueMapContainer> getData() {
		return containers;
	}
	
	public boolean containsKey(String key) {
		return containers.containsKey(key);
	}
	
	public Set<String> getKeySet() {
		return 	containers.keySet();
	}
	
	public Collection<ValueMapContainer> getValues() {
			return containers.values();
	}

	public Object remove(String key) {
		return containers.remove(key);
	}

	public int getSize() {
		return containers.size();
	}

	public String toString() {
		return containers.toString();
	}

	public void handleSaveRequest(Visitor visitor, String valueMapContainerName) {
		visitor.handleValueMapContainer(this, valueMapContainerName);
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

}
