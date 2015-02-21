package com.yy.appweather;

import android.os.Bundle;
import android.preference.PreferenceActivity;
  
import android.preference.Preference;  



public class SettingActivity extends PreferenceActivity 
	implements Preference.OnPreferenceChangeListener {

    /**
     * A placeholder fragment containing a simple view.
     */
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onPostCreate(savedInstanceState);
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		// TODO Auto-generated method stub
		return false;
	}
}
