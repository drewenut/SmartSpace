package de.ilimitado.smartspace.android;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
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
        webView.loadUrl("file:///android_asset/flot/html/graphPlotter.html");
	}
	
	public class JSCallback {

		public String getGraphTitle() {
			return "PDF";
		}  


        public void loadGraph() {
//			JSON-Flot-Config-Format:
//			[{
//			"label": "Label",
//			"data": [[x1,y1],[timestamp,rss],[x3,y3],[]....]
//			},
//        	{
//    			"label": "Label",
//    			"data": [[x1,y1],[timestamp,rss],[x3,y3],[]....]
//    			},{...}]
        	 
        	JSONArray flotConfig = getRawDataJSON();
        	
			String floatContents = flotConfig.toString();
			webView.loadUrl("javascript:plot(" + floatContents + ")");
		}


		private JSONArray getRawDataJSON() {
			JSONArray flotConfig = new JSONArray();
			try{
				String[] privateFiles = fileList();
			    int fileCount = (privateFiles != null ? privateFiles.length : 0);
			    
			    for( int index = 0 ; index < fileCount; index++) {
				    String fileName = privateFiles[index];
				    JSONArray plotValues = new JSONArray();
				    
				    JSONObject flotData = new JSONObject();
				    flotData.put("label", fileName);
				    
				    FileInputStream fStream = openFileInput(fileName);
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
		    } catch (Exception e) {
		    	Log.e(LOG_TAG, e.getMessage());
				Toast.makeText(RadioGraphActivity.this, "Error while building flot JSON configArray", Toast.LENGTH_SHORT);
		    	e.printStackTrace();
		    }
		    return flotConfig;
	   	}
	}
}
