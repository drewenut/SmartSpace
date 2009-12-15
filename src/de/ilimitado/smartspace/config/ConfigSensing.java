/**
 * 
 */
package de.ilimitado.smartspace.config;


public class ConfigSensing{
	public final int orientationQuantizationCount;
	public final LFPT lfpt = new LFPT(); 
	
	public ConfigSensing(int oriQuantCount) {
		this.orientationQuantizationCount = oriQuantCount;
	}
	
	public class LFPT{
		public final int SYNCHRONIZER_THREADPOOL_SIZE = 4;
	}
	
}