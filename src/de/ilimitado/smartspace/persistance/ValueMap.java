package de.ilimitado.smartspace.persistance;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class ValueMap extends ValueMapContainer {

	private HashMap<String, Object> data;
	private HashMap<String, String> datatype;
	
	public ValueMap() {
		this.data = new HashMap<String, Object>();
		this.datatype = new HashMap<String, String>();
	}

	public void putString(String key, String value) {
		data.put(key, value);
		datatype.put(key, "String");
	}

	public void putInteger(String key, int value) {
		data.put(key, value);
		datatype.put(key, "Integer");
	}

	public void putLong(String key, long value) {
		data.put(key, value);
		datatype.put(key, "Long");

	}

	public void putBoolean(String key, boolean value) {
		data.put(key, value);
		datatype.put(key, "Boolean");
	}

	public void putFloat(String key, float value) {
		data.put(key, value);
		datatype.put(key, "Float");
	}

	public void putDouble(String key, double value) {
		data.put(key, value);
		datatype.put(key, "Double");
	}
	
	public Object getAsObject(String key) {
		return data.get(key);
	}

	public HashMap<String, Object> getDataAsHashMap() {
		return data;
	}

	@Override
	public boolean containsKey(String key) {
		return data.containsKey(key);
	}

	@Override
	public Set<String> getKeySet() {
		return data.keySet();
	}

	public Collection<Object> getValuesAsCollection() {
		return data.values();
	}
	
	public HashMap<String, String> getDataTypeMap() {
		return datatype;
	}

	@Override
	public Object remove(String key) {
		return data.remove(key);
	}

	@Override
	public int getSize() {
		return data.size();
	}

	@Override
	public String toString() {
		return data.toString();
	}

	@Override
	public void handleSaveRequest(Visitor visitor, String valueMapContainerName) {
		visitor.handleValueMap(this, valueMapContainerName);
	}
	
	@Override
	public void put(String key, ValueMapContainer value) {
		throw new UnsupportedOperationException("A Component can not accept Containers.");
	}
	
	@Override
	public ValueMapContainer get(String key) {
		throw new UnsupportedOperationException("A Component can not accept Containers.");
	}

	public void merge(ValueMap values) {
		this.data.putAll(values.getDataAsHashMap());
	}

	public int getAsInteger(String key) {
		return ((Number) data.get(key)).intValue();
	}
	
	public long getAsLong(String key) {
		return ((Number) data.get(key)).longValue();
	}
	
	public String getAsString(String key) {
		return (String) data.get(key);
	}

	public double getAsDouble(String key) {
		return ((Number) data.get(key)).doubleValue();
	}
	
}