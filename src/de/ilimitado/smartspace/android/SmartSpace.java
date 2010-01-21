package de.ilimitado.smartspace.android;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import de.ilimitado.smartspace.iLocationListener;
import de.ilimitado.smartspace.SSFLocationManager;
import de.ilimitado.smartspace.config.Configuration;
import de.ilimitado.smartspace.positioning.Accuracy;
import de.ilimitado.smartspace.positioning.IGeoPoint;
import de.ilimitado.smartspace.utils.LogListener;

public class SmartSpace extends Activity {

	private static final String LOG_TAG = "SmartSpaceFramework";
	
	private static final String INPUT_METHOD_QR = "0x0000";
	private static final String INPUT_METHOD_DIALOG = "0x0001";
	private static final int DIALOG_INPUT_METHOD = 0x0002;
	
	private static final int MENU_PREFERENCES = 0x0010;
	private static final int MENU_RADIO_GRAPH = 0x0011;
	private static final int MENU_SERVER_PUSH = 0x0012;
	private static final int MENU_FLUSH = 0x0013;
	private static final int MENU_TRACK = 0x0014;
	private static final int MENU_LEARN = 0x0015;

	protected static final int MSG_HTTP = 0x0100;
	protected static final int MSG_SSF_LOC = 0x0101;
	protected static final int MSG_LOG = 0x0102;
	
	private SSFLocationManager locationMngr;
	private ScrollView consoleWrapper;
	private TextView console;
	private boolean learningActive = false;
	private boolean rtActive = false;
	private SmartSpaceFramework.SSFBinder ssfBinder;
	private SmartSpaceFramework ssf;
	private IGeoPoint currentLocation = new IGeoPoint("default", 0, 0, 0);
	private SharedPreferences sharedPreferences;
	private Handler handler = new Handler() { 
		

		@Override
		public void handleMessage(final Message msg) {
			switch (msg.what) {
			case MSG_HTTP:
				HttpResponse response = (HttpResponse) msg.obj;
				StatusLine status = response.getStatusLine();
				String result = Integer.toString(status.getStatusCode());
				appendConsoleText(getString(R.string.html_request_status_code) + result);
				break;
			case MSG_SSF_LOC:
				IGeoPoint currentlocation =  (IGeoPoint) msg.obj;
				appendConsoleText(getString(R.string.new_location) + currentlocation.toString());
				break;
			case MSG_LOG:
				appendConsoleText(getString(R.string.log_msg) + ((String) msg.obj));
				break;
			default:
				break;
			}
		}
	};
	
	private iLocationListener iLocL = new iLocationListener(){

		@Override
		public void onLocationChanged(IGeoPoint location, Accuracy acc) {
			handler.sendMessage(handler.obtainMessage(MSG_SSF_LOC, location));
			
			if(!currentLocation.equals(location)) {
				performHTTPRequest(location);
				currentLocation = location;
			}
			Log.d(LOG_TAG, "We got new position: " + location.toString());
		}

		@Override
		public void onStateChanged(int state) { }

		@Override
		public void onLocationChanged(List<IGeoPoint> list, Accuracy acc) { }
		
	};
	
	private LogListener logL = new LogListener() {
		
		@Override
		public void onLogMessage(String tag, String msg) {
			String logMsg = tag + " " + msg;
			handler.sendMessage(handler.obtainMessage(MSG_LOG, logMsg));
		}
	};
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		createService();
		setupView();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		killService();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_LEARN, 0, R.string.menu_learn);
		menu.add(0, MENU_TRACK, 1, R.string.menu_track);
		menu.add(0, MENU_FLUSH, 2, R.string.menu_flush);
		menu.add(1, MENU_RADIO_GRAPH, 0, R.string.menu_radio_graph).setIcon(android.R.drawable.ic_menu_recent_history);
		menu.add(1, MENU_PREFERENCES, 1, R.string.menu_preferences).setIcon(android.R.drawable.ic_menu_preferences);
		menu.add(1, MENU_SERVER_PUSH, 2, R.string.menu_server_push);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		if(learningActive) {
			(menu.findItem(MENU_TRACK)).setEnabled(false);
			(menu.findItem(MENU_FLUSH)).setEnabled(false);
			(menu.findItem(MENU_RADIO_GRAPH)).setEnabled(false);
			(menu.findItem(MENU_PREFERENCES)).setEnabled(false);
			(menu.findItem(MENU_SERVER_PUSH)).setEnabled(false);
		}
		else if(rtActive) {
			(menu.findItem(MENU_LEARN)).setEnabled(false);
			(menu.findItem(MENU_FLUSH)).setEnabled(false);
			(menu.findItem(MENU_RADIO_GRAPH)).setEnabled(false);
			(menu.findItem(MENU_PREFERENCES)).setEnabled(false);
			(menu.findItem(MENU_SERVER_PUSH)).setEnabled(false);
		}
		else
		{
			(menu.findItem(MENU_LEARN)).setEnabled(true);
			(menu.findItem(MENU_TRACK)).setEnabled(true);
			(menu.findItem(MENU_FLUSH)).setEnabled(true);
			(menu.findItem(MENU_RADIO_GRAPH)).setEnabled(true);
			(menu.findItem(MENU_PREFERENCES)).setEnabled(true);
			(menu.findItem(MENU_SERVER_PUSH)).setEnabled(true);
		}
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		super.onMenuItemSelected(featureId, item);
		switch (item.getItemId()) {
			case MENU_PREFERENCES:
				openPreferencesScreen();
				break;
			case MENU_RADIO_GRAPH:
				startActivity(new Intent(this, RadioGraphActivity.class));
				break;
			case MENU_LEARN:
				toogleLearn(item);
				break;
			case MENU_TRACK:
				toogleTrack(item);
				break;
			case MENU_FLUSH:
				flushStorage();
				break;
			case MENU_SERVER_PUSH:
				performHTTPRequest(currentLocation);
				break;
			default:
				break;
		}
		return true;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
	    Dialog dialog;
	    switch(id) {
	    case DIALOG_INPUT_METHOD:
	        return createInputDialog();
	    default:
	        dialog = null;
	    }
	    return dialog;
	}
	
	private void flushStorage() {
		new AlertDialog.Builder(SmartSpace.this)
        .setTitle(R.string.flush_db_warning)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setMessage(R.string.flush_db_confirm)
        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	AndroidConfigTranslator.getInstance(getSharedPreferences("smartSpace", 1)).translate();
    				boolean flushed = deleteDatabase(Configuration.getInstance().persistence.dbName);
    				if(flushed)
    					appendConsoleText(R.string.flush_db_success);
    				else
    					appendConsoleText(R.string.flush_db_fail);
    				
    				flushFiles();
                }
        })
        .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	appendConsoleText(getString(R.string.flush_db_cancel));
                    flushFiles();
                }
        }).create().show();
	}
	
	private void flushFiles() {
		final String[] privateFiles = fileList();
		if(privateFiles.length > 0) {
			new AlertDialog.Builder(SmartSpace.this)
	        .setTitle(R.string.flush_file_warning)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setMessage(R.string.flush_file_confirm)
	        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	boolean allFilesDeleted = true;
	                	for(String file : privateFiles) {
	                		allFilesDeleted &= deleteFile(file);
	                	}
	    				if(allFilesDeleted)
	    					appendConsoleText(R.string.flush_file_success);
	    				else
	    					appendConsoleText(R.string.flush_file_fail 
	    									 + "The following files could not be deleted: " 
	    									 + privateFiles.toString());
	                }
	        })
	        .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	appendConsoleText(getString(R.string.flush_file_cancel));
	                }
	        }).create().show();
		}
	}
	
	private void toogleTrack(MenuItem item) {
		if(!rtActive) {
			ssfStart();
			locationMngr.registerListener(iLocL);
			appendConsoleText(R.string.start_track);
			item.setTitle(R.string.menu_stop_track);
			rtActive = true;
		}
		else if(rtActive){
			locationMngr.unregisterListener(iLocL);
			ssfKill();	
			rtActive = false;
			appendConsoleText(R.string.stop_track);
			item.setTitle(R.string.menu_track);
		}
	}

	private void toogleLearn(MenuItem item) {
		if(!learningActive) {
			String inputMethod = chooseInputMethod();
			
			if(inputMethod.equals(SmartSpace.INPUT_METHOD_DIALOG)) {
				showDialog(DIALOG_INPUT_METHOD);
			}
			else if (inputMethod.equals(SmartSpace.INPUT_METHOD_QR)) {
				startQRReader();
			}
            item.setTitle(R.string.menu_stop_learn);
    	}
    	else if(learningActive) {
    		ssfKill();
    		learningActive = false;
    		item.setTitle(R.string.menu_learn);
    		appendConsoleText(R.string.stop_recording);
    	}
	}
	
	

	private void startQRReader() {
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
		startActivityForResult(intent, 0);
	}

	private Dialog createInputDialog() {
		final Dialog dialog = new Dialog(this);
		
		dialog.setContentView(R.layout.dialog_location_input);
		dialog.setTitle(R.string.input_method_dialog_title);
		
		TextView labelName = (TextView) dialog.findViewById(R.id.label_symbolic_loc_name);
		labelName.setText(R.string.input_method_dialog_name);
		
		final EditText symName = (EditText) dialog.findViewById(R.id.symbolic_loc_name);
		symName.setText(currentLocation.name);
		
		TextView labelXCord = (TextView) dialog.findViewById(R.id.label_loc_xCoord);
		labelXCord.setText(R.string.input_method_dialog_xCoord);
		
		final EditText xCoord = (EditText) dialog.findViewById(R.id.loc_xCoord);
		xCoord.setText(Integer.toString(currentLocation.position_x));
		
		TextView labelYCord = (TextView) dialog.findViewById(R.id.label_loc_yCoord);
		labelYCord.setText(R.string.input_method_dialog_yCoord);
		
		final EditText yCoord = (EditText) dialog.findViewById(R.id.loc_yCoord);
		yCoord.setText(Integer.toString(currentLocation.position_y));
		
		TextView labelZCord = (TextView) dialog.findViewById(R.id.label_loc_zCoord);
		labelZCord.setText(R.string.input_method_dialog_zCoord);
		
		final EditText zCoord = (EditText) dialog.findViewById(R.id.loc_zCoord);
		zCoord.setText(Integer.toString(currentLocation.floor));
		
		final Button learn = (Button) dialog.findViewById(R.id.learn_button);
		
		learn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				IGeoPoint iGP = new IGeoPoint(symName.getText().toString(), 
										  Integer.parseInt(xCoord.getText().toString()),
										  Integer.parseInt(yCoord.getText().toString()),
										  Integer.parseInt(zCoord.getText().toString()));
				Accuracy acc = new Accuracy(Accuracy.HIGH_ACCURACY);
            	startScan(iGP, acc);
            	dialog.dismiss();
            	appendConsoleText(getString(R.string.input_method_dialog_success) + iGP.toString());
            	learningActive = true;
                appendConsoleText(R.string.start_recording);
			}
		});
		
		return dialog;
	}

	private String chooseInputMethod() {
		return sharedPreferences.getString("input_method", SmartSpace.INPUT_METHOD_DIALOG);
	}

	private void openPreferencesScreen() {
		Intent intent = new Intent(this, EditPreferences.class);
		startActivity(intent);
	}
	
	//Push IGEOPoint to server
	public void performHTTPRequest(final IGeoPoint iGP) {
		
		final ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
		
			public String handleResponse(HttpResponse response) {
				handler.sendMessage(handler.obtainMessage(MSG_HTTP, response));
				return "";
			}
		};
		
		new Thread("HTTPRequest") { 
			@Override
			public void run() {
				try {
					DefaultHttpClient client = new DefaultHttpClient();
					client.getParams().setParameter("http.useragent","Smart Space Framework");
					
					URL serverUri = new URL("http://www.ilimitado.de/labs/SmartSpaceServer/de/ilimitado/smartspace/commit.php");
					
					List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>(1);
					formparams.add(new BasicNameValuePair("position", iGP.toJSON()));
					UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
					
					HttpPost postReq = new HttpPost(serverUri.toURI());
					postReq.setEntity(entity);
					
					client.execute(postReq, responseHandler);
				} catch (ClientProtocolException e) {
					Log.e(LOG_TAG, "ClientProtocolException..., here is what i know:  ", e);
					e.printStackTrace();
				} catch (IOException e) {
					Log.e(LOG_TAG, "IOException..., here is what i know:  ", e);
					e.printStackTrace();
				}
				catch (URISyntaxException e) {
					Log.e(LOG_TAG, "URISyntaxException..., here is what i know:  ", e);
					e.printStackTrace();
				}
				catch (JSONException e) {
					Log.e(LOG_TAG, "JSONException..., here is what i know:  ", e);
					e.printStackTrace();
				}
			}
		}.start();
	}

	
	private ServiceConnection ssfServiceConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			ssfKill();
			ssfBinder= null;
			ssf = null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			ssfBinder = (SmartSpaceFramework.SSFBinder) service;
			ssf = ssfBinder.getSSF();
		}

	};
	
	private void createService() {
		Intent ssfServiceIntent = new Intent(getApplicationContext(), SmartSpaceFramework.class);
		bindService(ssfServiceIntent, ssfServiceConnection, Context.BIND_AUTO_CREATE);
	}
	
	private void killService() {
		// TODO
		// unbindService(ssfServiceConnection);
	}
	
	private void ssfStart() {
		try {
			ssf.registerLogListener(logL);
			ssf.start();
			locationMngr = ssf.getLocationManager(SmartSpaceFramework.INDOOR_POSITION_PROVIDER);
		}
		catch (Exception e) {
			showMessage(e.getMessage());
			Log.d(LOG_TAG, "Unexpected error - Here is what I know: "
					+ e.getMessage());
		}
	}
	
	private void ssfKill() {
		ssf.unregisterLogListener(logL);
		ssf.kill();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void setupView() {
		LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainWrapper);
		consoleWrapper = new ScrollView(this);
		console = new TextView(this);
		console.setTextColor(Color.GREEN);
		appendConsoleText(R.string.welcome);
		appendConsoleText(R.string.have_fun);
		appendConsoleText(R.string.options_howto);
		appendConsoleText(R.string.no_location);
		appendConsoleText(R.string.prompt);
		consoleWrapper.addView(console);
		mainLayout.addView(consoleWrapper);
	}
	
	private void showMessage(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
				.show();
	}
	
	private void appendConsoleText(int resID) {
		appendConsoleText(getString(resID));
	}
	
	private void appendConsoleText(String text) {
		if(text.equals(getString(R.string.prompt)))
			text = "";
		
		console.append("> " + text + "\n");
		consoleWrapper.scrollTo(0, console.getHeight()+10);
	}
	
	//Called by ZXing QR-Code Scanner
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                try {
                	IGeoPoint iGP = IGeoPoint.fromGeoUri(contents, IGeoPoint.CONVERT_TYPE_STRING);
                    Accuracy acc = new Accuracy(Accuracy.HIGH_ACCURACY);
                	startScan(iGP, acc);
                	appendConsoleText(getString(R.string.zxing_success) + ": "+ iGP.toString());
        			learningActive = true;
                    appendConsoleText(R.string.start_recording);
				} catch (Exception e) {
					appendConsoleText(getString(R.string.zxing_fail) + ": " + e.getMessage());
					e.printStackTrace();
				}
            } else if (resultCode == RESULT_CANCELED) {
            	appendConsoleText(R.string.zxing_fail);
            }
        }
    }

	private void startScan(IGeoPoint iGP, Accuracy acc) {
		ssfStart();
//		locationMngr.setCurrentPosition(iGP, acc);
	}
	
	public TextView getDebugConsole() {
		return console;
	}
}