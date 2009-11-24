package de.ilimitado.smartspace.android;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class EditPreferences extends PreferenceActivity{
	
	private static final int SAVE = 0;
	private static final int RESTORE_DEFAULTS = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
    	menu.add(0, SAVE, 0, R.string.save)
        		.setIcon(android.R.drawable.ic_menu_save);
    	menu.add(0, RESTORE_DEFAULTS, 0, R.string.restore_defaults)
		.setIcon(android.R.drawable.ic_menu_revert);
    	return true;	
    }
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case SAVE:
        		showMessage(R.string.settings_saved);
            	finish();
            	return true;
            case RESTORE_DEFAULTS:
        		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        		preferences.edit().clear().commit();
        		showMessage(R.string.restored_default_settings);
        		finish();
                return true;
        }
        return false;
    }

	private void showMessage(int message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}
	
}
