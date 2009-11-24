package de.ilimitado.smartspace.tests.android;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import de.ilimitado.smartspace.IGeoPoint;
import de.ilimitado.smartspace.android.SmartSpace;

public class SmartSpaceTests extends ActivityUnitTestCase<SmartSpace>{
	
	private Intent mStartIntent;
	private Button sendGeoPoint;

	public SmartSpaceTests(Class<SmartSpace> activityClass) {
		super(activityClass);
	}

	
	@Override
    protected void setUp() throws Exception {
        super.setUp();

        mStartIntent = new Intent(Intent.ACTION_MAIN);
    }
	
	public void testPerformHTTPRequest() {
		final SmartSpace smartSpace = startActivity(mStartIntent, null, null);
		
		sendGeoPoint = new Button(smartSpace);
		sendGeoPoint.setText("Send GeoPoint to server");
		sendGeoPoint.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				IGeoPoint mockGP = new IGeoPoint("sid", 1, 1, 1);
				smartSpace.performRequest(mockGP);
			}
		});
		
		TextView debugConsole = smartSpace.getDebugConsole();
		
		assertNotNull(smartSpace);
		assertNotNull(sendGeoPoint);
		assertNotNull(debugConsole);
		assertEquals("200", debugConsole.getText());
	}
}
