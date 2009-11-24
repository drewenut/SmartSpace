package de.ilimitado.smartspace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import de.ilimitado.smartspace.persistance.Component;
import de.ilimitado.smartspace.persistance.Composite;
import de.ilimitado.smartspace.persistance.ValueMap;
import de.ilimitado.smartspace.persistance.ValueMapContainer;
import de.ilimitado.smartspace.registry.ScanSampleProvider;

public abstract class SensorDataSample implements Composite{

	public static final String VALUE_MAP_DEFAULT_DATA = "defaultData";
	public static final String VALUE_CREATION_TIME = "creationTime";
	public static final String VALUE_ID = "ID";
	
	protected long creationTime;
	protected HashMap<String, ScanSampleList> dataSampleMap = new HashMap<String, ScanSampleList>();

	public SensorDataSample(long creationTime) {
		this.creationTime = creationTime;
	}
	
	public SensorDataSample(ValueMapContainer values, ScanSampleProvider sSP) {
		getDefaultDataFromValueMapContainer(values);
		getScanSamplesFromValueMapContainer(values, sSP);
	}

	public long getCreationTime() {
		return creationTime;
	}
	
	@Override
	public void add(String key, Component scnS){
		dataSampleMap.put(key, (ScanSampleList) scnS);
	}
	
	@Override
	public Component get(String key){
		if(dataSampleMap.containsKey(key))
			return dataSampleMap.get(key);
		else
			return new ScanSampleList();
	}
	
	@Override
	public ValueMapContainer toValue(){
		ValueMapContainer vmContainer = new ValueMapContainer();
		vmContainer.put(VALUE_MAP_DEFAULT_DATA, createValueMapWithDefaultData());
		vmContainer = putDataSamplesIntoValueMapContainer(vmContainer);
		return vmContainer;
	}
	
	private ValueMap createValueMapWithDefaultData() {
		ValueMap emptyValueMapWithDefaultData = new ValueMap();
		emptyValueMapWithDefaultData.putLong(VALUE_CREATION_TIME, creationTime);
		return emptyValueMapWithDefaultData;
	}
	
	private ValueMapContainer putDataSamplesIntoValueMapContainer(ValueMapContainer vmContainer) {
		for(String scanSampleListID : dataSampleMap.keySet()){
			ScanSampleList sSL = dataSampleMap.get(scanSampleListID);
			ValueMapContainer sslValues = sSL.toValue();
			vmContainer.put(scanSampleListID, sslValues);
		}
		return vmContainer;
	}
	
	public HashMap<String, ScanSampleList> getScanSampleLists() {
		return dataSampleMap;
	}

	
	@Override
	public List<Double> ToArray() {
		ArrayList<Double> flatenedNumbers = new ArrayList<Double>();
		for(Component scanSample : dataSampleMap.values()){
			flatenedNumbers.addAll(scanSample.ToArray());
		}
		return flatenedNumbers;
	}
	
	private void getDefaultDataFromValueMapContainer(ValueMapContainer values) {
		ValueMap defaultData = (ValueMap) values.get(VALUE_MAP_DEFAULT_DATA);
		creationTime = defaultData.getAsLong(VALUE_CREATION_TIME);
	}

	private void getScanSamplesFromValueMapContainer(ValueMapContainer values, ScanSampleProvider sSP) {
		for (ValueMapContainer container : values.getValues()){
			if(container.getKey().equals(VALUE_MAP_DEFAULT_DATA)) 
				continue;
			else{
				ScanSampleList scanSampleList = new ScanSampleList(container, sSP);
				dataSampleMap.put(container.getKey(), scanSampleList);
			}
		}
	}

	public String getID() {
		return "";
	}
	
	public void merge(SensorDataSample sDataSplToMerge){
		creationTime = Math.max(sDataSplToMerge.creationTime, creationTime);
		Set<String> keySet = dataSampleMap.keySet();
		for(String scanSampleListID : keySet){
			ScanSampleList sSplList = dataSampleMap.get(scanSampleListID);
			if(sDataSplToMerge.dataSampleMap.containsKey(scanSampleListID)){
				ScanSampleList sSplListToMerge = (ScanSampleList) sDataSplToMerge.dataSampleMap.get(scanSampleListID);
				sSplList.merge(sSplListToMerge);
				sDataSplToMerge.dataSampleMap.remove(scanSampleListID);
				dataSampleMap.remove(scanSampleListID);
				dataSampleMap.put(scanSampleListID, sSplList);
			}
		}
		dataSampleMap.putAll(sDataSplToMerge.dataSampleMap);
	}
	
	@Override
	public String toString() {
		String toString = "";
		for(String scanSampleListID : dataSampleMap.keySet()){
			ScanSampleList sSplList = dataSampleMap.get(scanSampleListID);
			toString += scanSampleListID +": "+ sSplList.toString();
		}
		return toString;
	}
}
