/**
 * 
 */
package de.ilimitado.smartspace.config;


public class FPTCollection{
	public final int orientationQuantizationCount;
	public final int scanSample80211ID = 0x0000;
	public final LFPT lfpt = new LFPT(); 
	
	public FPTCollection(int oriQuantCount) {
		this.orientationQuantizationCount = oriQuantCount;
	}
	
	public class LFPT{
		public final int SYNCHRONIZER_THREADPOOL_SIZE = 4;
	}
	
}