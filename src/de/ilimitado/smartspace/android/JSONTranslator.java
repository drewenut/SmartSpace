package de.ilimitado.smartspace.android;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

public class JSONTranslator {
	private static final String JSON_80211 = "wifi";
	private static final String JSON_GSM = "gsm";
	
	protected final String[] privateFiles;
	private ArrayList<String> bucketGSM = new ArrayList<String>();
	private ArrayList<String> bucket80211 = new ArrayList<String>();
	private HashMap<String, ArrayList<String>> posMapGSM = null;
	private HashMap<String, ArrayList<String>> posMap80211 = null;

	public JSONTranslator(String[] privateFiles) {
		this.privateFiles = privateFiles;
	}

	public void  sortFiles() {
		sortSensor();
		sortPosition();
	}
	
	private void sortSensor() {
		int fileCount = (privateFiles != null ? privateFiles.length : 0);
	    
	    for( int index = 0 ; index < fileCount ; index++ ) {
		    String fileName = privateFiles[index];
		    String[] fileMeta = fileName.split("-");
		    String sensorType = fileMeta[1];
		    
		    if(sensorType.equals(JSON_GSM))
		    	bucketGSM.add(fileName);
		    if(sensorType.equals(JSON_80211))
		    	bucket80211.add(fileName);
	    }
	}
	
	private void sortPosition() {
		if(!bucketGSM.isEmpty()) {
			posMapGSM = new HashMap<String, ArrayList<String>>();
			for(String fileName : bucketGSM) {
				String[] fileMeta = fileName.split("-");
			    String curPosition = fileMeta[0];
			    if(!posMapGSM.containsKey(curPosition))
			    	posMapGSM.put(curPosition, new ArrayList<String>());
			    
			    posMapGSM.get(curPosition).add(fileName);
			}
		}
		
		if(!bucket80211.isEmpty()) {
			posMap80211 = new HashMap<String, ArrayList<String>>();
			for(String fileName : bucket80211) {
				String[] fileMeta = fileName.split("-");
			    String curPosition = fileMeta[0];
			    if(!posMap80211.containsKey(curPosition))
			    	posMap80211.put(curPosition, new ArrayList<String>());
			    
			    posMap80211.get(curPosition).add(fileName);
			}
		}
	}

	public ArrayList<ArrayList<String>> getFiles() {
	    ArrayList<ArrayList<String>> fileList = new ArrayList<ArrayList<String>>();
		
	    if(posMap80211 != null && !posMap80211.isEmpty()) {
		    for(ArrayList<String> singleList : posMap80211.values()) {
		    	fileList.add(singleList);
		    }
	    }
	    
	    if(posMapGSM != null && !posMapGSM.isEmpty()) {
		    for(ArrayList<String> singleList : posMapGSM.values()) {
		    	fileList.add(singleList);
		    }
	    }
		
	    return fileList;
	}
	
	public static JSONArray getJSON(ArrayList<String> files, Context ctx) throws Exception {
		JSONArray flotConfig = new JSONArray();
	    for( String fileName : files ) {
		    String name = fileName.substring(0, fileName.length()-4);
		    String[] fileMeta = name.split("-");
		    String CellID = fileMeta[2];
		    
		    JSONArray plotValues = new JSONArray();
		    
		    JSONObject flotData = new JSONObject();
		    flotData.put("label", CellID);
		    
		    FileInputStream fStream = ctx.openFileInput(fileName);
		    DataInputStream inStream = new DataInputStream(fStream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(inStream));
		    String line;
		    //skip first Line
		    int lineCount = 1;
		    while ((line = br.readLine()) != null)   {
		       String[] values = line.split(";");
		       if(values.length >= 2 && lineCount > 1) {
		    	   JSONArray lineValues = new JSONArray();
		    	   lineValues.put(values[0]);
		    	   lineValues.put(values[1]);
		    	   plotValues.put(lineValues);
		       }
		       lineCount++;
		    }
		    flotData.put("data", plotValues);
		    flotConfig.put(flotData);
		    inStream.close();
	    }
	    return flotConfig;
   	}
}
