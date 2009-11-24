package de.ilimitado.smartspace;

import de.ilimitado.smartspace.persistance.Composite;
import de.ilimitado.smartspace.persistance.ValueMapContainer;


public interface ScanSample extends Composite{

	/**
	 * @Precondition First call mergeable() to make sure, this method will work.
	 */
	public void merge(ScanSample sSpl);
	public boolean mergeable(ScanSample sSpl);

	/**
	 * @return must always return true, except setMerged(false) is called from ScanSampleList.merge();
	 */
	boolean isMerged();
	void setMerged(boolean isMerged);
	
	/**
	 * @return each instance must return a unique id like MAC or CellID
	 */
	public String getSampleID();

	public void fromValue(ValueMapContainer values);
	public ScanSample getDefaultScanSample();

}
