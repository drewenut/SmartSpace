package de.ilimitado.smartspace.android;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

public class RadioGraphActivity extends Activity {
	private static final String LOG_TAG = "RadioGraphActivity";
    
	private WebView webView;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graphview);        
        webView = (WebView) findViewById(R.id.graphwebview);
        
        JSCallback myhandler = new JSCallback ();

        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(myhandler, "testhandler");
        webView.loadUrl("file:///android_asset/flot/html/dynamic.html");
	}
	
	public class JSCallback {

		public String getGraphTitle() {
			return "This is my graph, baby!";
		}  


	        public void loadGraph() {
			JSONArray dataArray = new JSONArray();

			JSONObject data = new JSONObject();
			
			
			try {
//				{"label": p[x1(timestamp),y1(RSS)],[x2,y2],[x3,y3],[]....]}, 
//				{"data": p[x1(timestamp),y1(RSS)],[x2,y2],[x3,y3],[]....]},
//				{ "lines": { "show" : true }},
//				{ "points": { "show" : true }}
				data.put("data", getRawDataJSON().toString());
				data.put("lines", "{ \"show\" : true }"); 
				data.put("points", "{ \"show\" : true }");
				
				dataArray.put(data);
				webView.loadUrl("javascript:GotGraph(" + dataArray.toString() + ")");
			} catch (JSONException e) {
				Log.e(LOG_TAG, e.getMessage());
				Toast.makeText(RadioGraphActivity.this, "Error while building flot JSON configArray", Toast.LENGTH_SHORT);
			}		
		}


			private JSONObject getRawDataJSON() {
				try{
					StringBuilder configString = new StringBuilder();
					
					String[] privateFiles = fileList();
				    int fileCount = (privateFiles != null ? privateFiles.length : 0);
				    
				    for( int index = 0 ; index < fileCount; index++) {
					    FileInputStream fStream = openFileInput(privateFiles[index]);
					    DataInputStream inStream = new DataInputStream(fStream);
					    BufferedReader br = new BufferedReader(new InputStreamReader(inStream));
					    String line;
					    while ((line = br.readLine()) != null)   {
					       String[] values = line.split(";");
					    }
					    //Close the input stream
					    inStream.close();
				    }
					
				    }catch (Exception e){//Catch exception if any
				      System.err.println("Error: " + e.getMessage());
				    }
				  }
				return null;
			}
	}
}
