package de.ilimitado.smartspace.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class QRInputMethod extends Activity {

	private Intent result;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// ...Implementierung...
	}

	private void startQRReader() {
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
		startActivityForResult(intent, 0);
	}
	/**
	 * Wird vom ZXing QR-Code Scanner aufgerufen
	 */
	public void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		result = new Intent();
		setResult(Activity.RESULT_CANCELED, result);
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				String contents = intent.getStringExtra("SCAN_RESULT");
				try {
					result.putExtra(SmartSpaceFramework.GEO_URI_STRING,
							contents);
					setResult(Activity.RESULT_OK, result);
					finish();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		finish();
	}

}