package de.ilimitado.smartspace.sensor.sensorIMU;

import android.hardware.SensorEvent;

public class ScanResultIMU{

    public final float[] values;
    
    public final long timestamp;

	public int sensorType;

	
	ScanResultIMU(SensorEvent event) {
		this.values = event.values.clone();
		this.sensorType = event.sensor.getType();
		this.timestamp = event.timestamp;
	}
	
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("Sensor Type: " + Integer.toString(sensorType))
          .append("Timestamp: " + Long.toString(timestamp))
          .append("Values: [");
        
        for(int index=0; index<values.length ; index++) {
        	sb.append(Float.toString(values[index]));
        	if(index < values.length-1)
        		sb.append(",");
        	else
        		sb.append("]");
        }
        return sb.toString();
    }
}
