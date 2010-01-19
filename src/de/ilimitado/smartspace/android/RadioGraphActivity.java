package de.ilimitado.smartspace.android;

import java.util.ArrayList;

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
        
        JSCallback handler = new JSCallback ();

        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(handler, "graphPlotter");
        webView.loadUrl("file:///android_asset/flot/html/graphPlotter.html");
        webView.getSettings().setBuiltInZoomControls(true);
	}
	
	public class JSCallback {

        /**
         * JSON-Flot-Config-Format:
			[{
			 "label": "Label",
			 "data": [[x1,y1],[timestamp,rss],[x3,y3],[]....]},
		     {
			 "label": "Label",
			 "data": [[x1,y1],[timestamp,rss],[x3,y3],[]....]},
			 {...}]
         */
		public void loadGraph() {
        	String[] privateFiles = fileList(); 
        	JSONTranslator jsonTransl = new JSONTranslator(privateFiles);
        	jsonTransl.sortFiles();
        	for(ArrayList<String> sortedFileList : jsonTransl.getFiles()) {
        		JSONArray flotConfig;
        		JSONObject graphMeta = new JSONObject();
        		String[] fileMeta = sortedFileList.get(0).split("-");
    		    String title = fileMeta[0];
    		    String sensor = fileMeta[1];
    		   
				try {
					graphMeta.put("graphtitle", title);
					graphMeta.put("sensor", sensor);
					flotConfig = JSONTranslator.getJSON(sortedFileList, RadioGraphActivity.this);
	    			webView.loadUrl("javascript:plot(" + flotConfig.toString() + ", " + graphMeta.toString() + ")");
				} catch (Exception e) {
					String message = "Error while building flot JSON. Here is what i know: " + e.getMessage();
					Log.e(LOG_TAG, message);
					Toast.makeText(RadioGraphActivity.this, message, Toast.LENGTH_SHORT);
			    	e.printStackTrace();
				}
        	}
        }
	}
}
