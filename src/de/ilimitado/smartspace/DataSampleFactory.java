package de.ilimitado.smartspace;

import de.ilimitado.smartspace.positioning.IGeoPoint;


public class DataSampleFactory {
	
	private static final DataSampleFactory instance = null;

	private DataSampleFactory() { }
	
	public static DataSampleFactory getInstance() {
		return instance != null ? instance
								: new DataSampleFactory();
	}
	public LFPT makeLFPT(IGeoPoint iGP, double orientation){
		long makeTime = System.currentTimeMillis();
		return new LFPT(makeTime, orientation, iGP); 
	}
	
	public RTFPT makeRTSample(double orientation){
		long makeTime = System.currentTimeMillis();
		return new RTFPT(makeTime, orientation); 
	}
	
	public IMSDataSample makeIMSSample(){
		long makeTime = System.currentTimeMillis();
		return new IMSDataSample(makeTime); 
	}
}
