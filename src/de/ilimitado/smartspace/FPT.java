package de.ilimitado.smartspace;

import de.ilimitado.smartspace.config.Configuration;
import de.ilimitado.smartspace.persistance.ValueMap;
import de.ilimitado.smartspace.persistance.ValueMapContainer;
import de.ilimitado.smartspace.registry.ScanSampleProvider;

public class FPT extends SensorDataSample{

	public static final String VALUE_ORIENTATION = "Orientation";

	protected static final int ORIENTATION_COUNT = Configuration.getInstance().fptCollection.orientationQuantizationCount;
	private int quantizedOrientation;

	public FPT(long creationTime, double orientation) {
		super(creationTime);
		quantizedOrientation = setQuantizedOrientation(setQuantizedOrientation(orientation));
	}
	
	public FPT(ValueMapContainer values, ScanSampleProvider sSP) {
		super(values, sSP);
		fromValue(values);
	}

	private int setQuantizedOrientation(double orientation) {
		int step = 360/ORIENTATION_COUNT;
		for(int quantizedOrientation = 0; quantizedOrientation < 360; quantizedOrientation+=step){
			if(orientation >= quantizedOrientation && orientation < quantizedOrientation + step)
				return quantizedOrientation;
		}
		return 0;
	}

	public int getQuantizedOrientation() {
		return quantizedOrientation;
	}

	@Override
	public ValueMapContainer toValue() {
		ValueMapContainer vmContainer = super.toValue(); ;
		ValueMap fingerprintData = (ValueMap) vmContainer.get(SensorDataSample.VALUE_MAP_DEFAULT_DATA);
		fingerprintData.putInteger(VALUE_ORIENTATION, getQuantizedOrientation());
		return vmContainer;
	}
	
	private void fromValue(ValueMapContainer values) {
		ValueMap defaultData = (ValueMap) values.get(SensorDataSample.VALUE_MAP_DEFAULT_DATA);
		quantizedOrientation = defaultData.getAsInteger(VALUE_ORIENTATION);
	}

	@Override
	public String getID() {
		return getQuantizedOrientation() + super.getID();
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
	
}