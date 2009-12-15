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
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import de.ilimitado.smartspace.SSFLocationListener;
import de.ilimitado.smartspace.SSFLocationManager;
import de.ilimitado.smartspace.config.Configuration;
import de.ilimitado.smartspace.positioning.Accuracy;
import de.ilimitado.smartspace.positioning.IGeoPoint;

public class SmartSpace extends Activity {

	private static final String LOG_TAG = "SmartSpaceFramework";
	protected static final int MSG_HTTP = 0x0001;
	protected static final int MSG_SSF_LOC = 0x0002;
	private SSFLocationManager posMngr;
	private CharSequence NO_POSITION = "no position available";
	private Button learnButton;
	private ScrollView consoleWrapper;
	private TextView console;
	private boolean learningActive = false;
	private boolean rtActive = false;
	private Button realTimeButton;
	private Button sendGeoPoint;
	private Button flushDatabase;
	private SmartSpaceFramework.SSFBinder ssfBinder;
	private SmartSpaceFramework ssf;
	private IGeoPoint currentPosition = new IGeoPoint("na", 0, 0, 0);
	private Handler handler = new Handler() { 
		
		@Override
		public void handleMessage(final Message msg) {
			switch (msg.what) {
			case MSG_HTTP:
				HttpResponse response = (HttpResponse) msg.obj;
				StatusLine status = response.getStatusLine();
				String result = Integer.toString(status.getStatusCode());
				console.append("> STATUS_CODE: ");
				console.append(result);
				console.append("\n");
				break;
			case MSG_SSF_LOC:
				String currPos = msg.getData().getString("CUR_POS");
				console.append("> ");
				console.append(currPos);
				console.append("\n");
				break;
			default:
				break;
			}
		}
	};
	
	private SSFLocationListener indrPosList = new SSFLocationListener(){

		@Override
		public void onLocationChanged(IGeoPoint location, Accuracy acc) {
			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putString("CUR_POS", "We got new position: " + location.toString());
			message.setData(bundle);
			handler.sendMessage(handler.obtainMessage(MSG_SSF_LOC, location));
			if(!currentPosition.equals(location)) {
				performHTTPRequest(location);
				currentPosition = location;
			}
			Log.d(LOG_TAG, "We got new position: " + location.toString());
		}

		@Override
		public void onStateChanged(int state) { }

		@Override
		public void onLocationChanged(List<IGeoPoint> list, Accuracy acc) { }
		
	};
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setupView();
	}

	
	private ServiceConnection ssfServiceConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			ssf.kill();
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			ssfBinder = (SmartSpaceFramework.SSFBinder) service;
			ssf = ssfBinder.getSSF();
			try {
				ssf.start();
			}
			catch (Exception e) {
				showMessage(e.getMessage());
				Log.d(LOG_TAG, "Unexpected error - Here is what I know: "
						+ e.getMessage());
			}
		}
	};
	
	private void createSmartspaceFramework() {
		Intent ssfServiceIntent = new Intent(getApplicationContext(), SmartSpaceFramework.class);
		bindService(ssfServiceIntent, ssfServiceConnection, Context.BIND_AUTO_CREATE);
		posMngr = ssf.getLocationManager(SmartSpaceFramework.INDOOR_POSITION_PROVIDER);
	}
	
	private void destroySmartspaceFramework() {
		unbindService(ssfServiceConnection);
		ssfBinder= null;
		ssf = null;
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                try {
            		createSmartspaceFramework();
                	IGeoPoint iGP = IGeoPoint.fromGeoUri(contents, IGeoPoint.CONVERT_TYPE_STRING);
                    Accuracy acc = new Accuracy(Accuracy.HIGH_ACCURACY);
                	posMngr.setCurrentPosition(iGP, acc);
                	Toast.makeText(this, iGP.toString(), Toast.LENGTH_SHORT).show();
                	Toast.makeText(this, "Starting Recording...", Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Toast.makeText(this, "Fail " + e.getMessage(), Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
            } else if (resultCode == RESULT_CANCELED) {
            	Toast.makeText(this, "Fail", Toast.LENGTH_LONG).show();
            }
        }
    }
	
	private void showMessage(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
				.show();
	}
		
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

	public TextView getDebugConsole() {
		return console;
	}
	
	private void setupView() {
		LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainWrapper);
		consoleWrapper = new ScrollView(this);
		console = new TextView(this);
		console.setTextColor(Color.GREEN);
		console.append("> ");
		console.append(NO_POSITION);
		console.append("\n");
		consoleWrapper.addView(console);
		
		final TextView stateChangedView = new TextView(this);
		stateChangedView.setText("");
			
        final String defaultLBText = "Learn Some!";
		learnButton = new Button(this);
        learnButton.setText(defaultLBText);
        learnButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	if(!learningActive) {
            		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                    startActivityForResult(intent, 0);
                    learningActive = true;
                    learnButton.setText("Stop Learning...");
            	}
            	else if(learningActive) {
            		destroySmartspaceFramework();
            		learningActive = false;
                    learnButton.setText(defaultLBText);
            	}
            }
        });
        
        realTimeButton = new Button(this);
		realTimeButton.setText("Go Real Time...");
		realTimeButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(!rtActive) {
					createSmartspaceFramework();
					posMngr.registerListener(indrPosList);
					realTimeButton.setText("Tracking now...click to Stop!");
					rtActive = true;
				}
				else if(rtActive){
					posMngr.unregisterListener(indrPosList);
					destroySmartspaceFramework();	
					rtActive = false;
					realTimeButton.setText("RT Positioning");
				}
			}
		});	
		
		sendGeoPoint = new Button(this);
		sendGeoPoint.setText("Push iGeoPoint to server");
		sendGeoPoint.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				performHTTPRequest(currentPosition);
			}
			
		});	
		
		flushDatabase = new Button(this);
		flushDatabase.setText("Flush Database");
		flushDatabase.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
                new AlertDialog.Builder(SmartSpace.this)
                .setTitle("Warning!")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Do you realy want to flush the whole database? All FPTs will be lost!")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        	AndroidConfigTranslator.getInstance(getSharedPreferences("smartSpace", 1)).translate();
            				boolean flushed = deleteDatabase(Configuration.getInstance().persistence.dbName);
            				if(flushed)
            					showMessage("Database flushed successfully.");
            				else
            					showMessage("Error while flushing database.");
                        }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            showMessage("Cancelled flushing database");
                        }
                }).create().show();
				
			}
			
		});	
		
		LinearLayout learnAndRealtimeButtons = new LinearLayout(this);
		learnAndRealtimeButtons.setWeightSum(1.0f);
		learnAndRealtimeButtons.setOrientation(LinearLayout.HORIZONTAL);
		learnAndRealtimeButtons.addView(learnButton);
		learnAndRealtimeButtons.addView(realTimeButton);
		
		LinearLayout pushAndFlushButtons = new LinearLayout(this);
		pushAndFlushButtons.setOrientation(LinearLayout.HORIZONTAL);
		pushAndFlushButtons.addView(sendGeoPoint);
		pushAndFlushButtons.addView(flushDatabase);
		
		mainLayout.addView(learnAndRealtimeButtons);
		mainLayout.addView(pushAndFlushButtons);
		mainLayout.addView(consoleWrapper);
	}
}