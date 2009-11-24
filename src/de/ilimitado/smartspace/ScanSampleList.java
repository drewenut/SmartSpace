package de.ilimitado.smartspace;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.ilimitado.smartspace.persistance.Component;
import de.ilimitado.smartspace.persistance.Composite;
import de.ilimitado.smartspace.persistance.ValueMap;
import de.ilimitado.smartspace.persistance.ValueMapContainer;
import de.ilimitado.smartspace.registry.RegistryProviderException;
import de.ilimitado.smartspace.registry.ScanSampleProvider;
import de.ilimitado.smartspace.utils.L;


public class ScanSampleList extends CopyOnWriteArrayList<ScanSample> implements Composite{
	
	private static final long serialVersionUID = 1L;
	private static final String LOG_TAG = "ScanSampleList";
	
	public ScanSampleList() {
		super();
	}
	
	public ScanSampleList(int size) {
//		super(size);
	}
	
	public ScanSampleList(ValueMapContainer container, ScanSampleProvider sSP) {
		for (ValueMapContainer valueMap : container.getValues()) {
			ValueMap scanSampleData = (ValueMap) valueMap;
			try {
				ScanSample scanSample = sSP.getItem(container.getKey());
				scanSample.fromValue(scanSampleData);
				add(scanSample);
			} catch (RegistryProviderException e) {
				L.w(LOG_TAG, "Tried to get ScanSample instance for key "+scanSampleData.getKey()+" without success, here is what i know: " + e.getMessage(), e);
			}
		}
	}

	@Override
	public void add(String key, Component value) {
		super.add((ScanSample) value);
	}

	@Override
	public Component get(String key) {
		for(ScanSample sSpl : this){
			if(sSpl.getSampleID().equals(key))
				return sSpl;
		}
		return null;
	}

	@Override
	public ValueMapContainer toValue() {
		ValueMapContainer scanSampleContainer = new ValueMapContainer();
		for (ScanSample scanSample : this) {
			ValueMapContainer value = scanSample.toValue();
			scanSampleContainer.put(value.getKey(), value);
		}
		return scanSampleContainer;
	}

	@Override
	public List<Double> ToArray() {
		ArrayList<Double> flatenedNumbers = new ArrayList<Double>();
		for (ScanSample scnSPl : this) {
			flatenedNumbers.addAll(scnSPl.ToArray());
		}
		return flatenedNumbers;
	}

	public void merge(ScanSampleList sSplListToMerge) {
		if(size() != 0) 
			sSplListToMerge = mergeExistingScanSamples(sSplListToMerge);
		if(sSplListToMerge.size() != 0) 
			addNewScanSamples(sSplListToMerge);
	}
	
	public boolean containsSample(String key){
		for(ScanSample sSpl : this){
			if(sSpl.getSampleID().equals(key))
				return true;
		}
		return false;
	}

	private ScanSampleList mergeExistingScanSamples(ScanSampleList sSplListToMerge) {
		for(ScanSample sSpl : this){
			for (ScanSample sSplToMerge : sSplListToMerge) {
				if(sSpl.mergeable(sSplToMerge)){
					int index = indexOf(sSpl);
					sSpl.merge(sSplToMerge);
					set(index, sSpl);
					sSplListToMerge.remove(sSplToMerge);
					break;
				}
			}
		}
		return sSplListToMerge;
	}
	
	private void addNewScanSamples(ScanSampleList sSplListToMerge) {
		for (ScanSample sSplToAdd : sSplListToMerge){
			sSplToAdd.setMerged(false);
			add(sSplToAdd);
		}
	}

}
