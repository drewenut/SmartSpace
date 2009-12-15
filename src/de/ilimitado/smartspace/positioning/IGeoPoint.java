package de.ilimitado.smartspace.positioning;

import org.json.JSONException;
import org.json.JSONObject;


public class IGeoPoint {

	public static final byte CONVERT_TYPE_STRING = 0x00;
	public static final byte CONVERT_TYPE_JSON = 0x01;
	
	private static final String DEFAULT_NAME = "";
	private static final int DEFAULT_FLOOR = -1;
	
	public final int position_x;
	public final int position_y;
	public final int floor;
	public final String name;
	
	public IGeoPoint(String name, int posX, int posY, int floor) {
		this.name = name;
		position_x = posX;
		position_y = posY;
		this.floor = floor;
	}
	
	public IGeoPoint(int posX, int posY, int floor) {
		this.name =  DEFAULT_NAME;
		position_x = posX;
		position_y = posY;
		this.floor = floor;
	}
	
	public IGeoPoint(String name, int posX, int posY) {
		this.name = name;
		position_x = posX;
		position_y = posY;
		this.floor = DEFAULT_FLOOR;
	}
	
	public IGeoPoint(int posX, int posY) {
		this.name =  DEFAULT_NAME;
		position_x = posX;
		position_y = posY;
		this.floor = DEFAULT_FLOOR;
	}
	
	public static IGeoPoint fromGeoUri(String iGPString, byte type) {
		if (type == CONVERT_TYPE_STRING)
			return parseFromUriString(iGPString);
		else if (type == CONVERT_TYPE_JSON)
			return parseFromJSON(iGPString);
		else
			throw new IllegalArgumentException("String could not be converted to iGeoPoint...");
	}
	
	@Override
	public String toString() {
		return "name:"+name+"|igeo:" + position_x + "," + position_y + "," + floor;
	}
	
	public String toGeoUri() {
		return toString();
	}
	
	public String toJSON() throws JSONException {
//		{"name": "$name","pos_x": "$pos_x","pos_y": "$pos_y","floor": "floor"}
		JSONObject jsonIGP = new JSONObject();
		jsonIGP.put("IGP_NAME", name);
		jsonIGP.put("IGP_POS_X", position_x);
		jsonIGP.put("IGP_POS_Y", position_y);
		jsonIGP.put("IGP_FLOOR", floor);
		
		return jsonIGP.toString();
	}
	
	private static IGeoPoint parseFromUriString(String iGPString) {
//		name:<name>|igeo:pos_x,pos_y,floor
		String[] lines = new String[2];
		String[] iGeoData = new String[3];
		lines = iGPString.split("\\|");
		String name = lines[0].substring(5); 
		String geo = lines[1].substring(5);
		iGeoData = geo.split("\\,");
		
		int posX  = Integer.parseInt(iGeoData[0]);
		int posY  = Integer.parseInt(iGeoData[1]);
		int floor = Integer.parseInt(iGeoData[2]);
		
		return new IGeoPoint(name, posX, posY, floor);
	}
	
	private static IGeoPoint parseFromJSON(String iGPJSON) {
		//TODO needed when we might switch to JSON format encoded QR-Codes
		return null;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof IGeoPoint) {
			return ((IGeoPoint) o).position_x == position_x 
			                            && ((IGeoPoint) o).position_y == position_y
			                            && ((IGeoPoint) o).floor == this.floor
			                            && ((IGeoPoint) o).name == this.name;
		}
		return false;
	}
}
