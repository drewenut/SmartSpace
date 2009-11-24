package de.ilimitado.smartspace;

import de.ilimitado.smartspace.persistance.ValueMap;
import de.ilimitado.smartspace.persistance.ValueMapContainer;
import de.ilimitado.smartspace.registry.ScanSampleProvider;

public class LFPT extends FPT{

	public static final String VALUE_POS_X = "positionX";
	public static final String VALUE_POS_Y = "positionY";
	public static final String VALUE_FLOOR = "floor";
	public static final String VALUE_NAME = "igp_name";
	
	private IGeoPoint indoorGP;

	public LFPT(long creationTime, double orientation, IGeoPoint iGP) {
		super(creationTime, orientation);
		this.indoorGP = iGP;
	}
	
	public LFPT(ValueMapContainer values, ScanSampleProvider sSP) {
		super(values, sSP);
		fromValue(values);
	}
	
	public IGeoPoint getIndoorGP() {
		return indoorGP;
	}
	
	@Override
	public String getID(){
		return indoorGP.position_x +""+ indoorGP.position_y + super.getID();
	}
	
	@Override
	public ValueMapContainer toValue() {
		ValueMapContainer vmContainer = super.toValue();
		ValueMap defaultData = (ValueMap) vmContainer.get(SensorDataSample.VALUE_MAP_DEFAULT_DATA);
		defaultData.putInteger(VALUE_POS_X, indoorGP.position_x);
		defaultData.putInteger(VALUE_POS_Y, indoorGP.position_y);
		return vmContainer;
	}
	
	private void fromValue(ValueMapContainer values) {
		ValueMap defaultData = (ValueMap) values.get(SensorDataSample.VALUE_MAP_DEFAULT_DATA);
		indoorGP = new IGeoPoint(defaultData.getAsInteger(VALUE_POS_X), 
									  defaultData.getAsInteger(VALUE_POS_Y));
	}
	
	@Override
	public String toString() {
		return "LFPT(" + indoorGP + "): " + super.toString();
	}

}
