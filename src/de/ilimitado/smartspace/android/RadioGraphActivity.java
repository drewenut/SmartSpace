package de.ilimitado.smartspace.android;

import java.util.ArrayList;

import org.json.JSONArray;

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
        	String[] privateFiles = fileList(); 
        	JSONTranslator jsonTransl = new JSONTranslator(privateFiles);
        	jsonTransl.sortFiles();
        	int count = 0;
        	for(ArrayList<String> files : jsonTransl.getFiles()) {
        		JSONArray flotConfig;
				try {
					flotConfig = JSONTranslator.getJSON(files, RadioGraphActivity.this);
					String floatContents = flotConfig.toString();
	    			webView.loadUrl("javascript:plot(" + floatContents + ", plot"+ count++ +")");
				} catch (Exception e) {
					Log.e(LOG_TAG, e.getMessage());
					Toast.makeText(RadioGraphActivity.this, "Error while building flot JSON configArray", Toast.LENGTH_SHORT);
			    	e.printStackTrace();
				}
        	}
        }
	}
}
