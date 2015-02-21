package com.yy.appweather;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
  
import android.preference.Preference;  
import android.preference.PreferenceManager;



public class SettingActivity extends PreferenceActivity 
	implements Preference.OnPreferenceChangeListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_general);
		bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_location_key)));
		
	}
	
	private void bindPreferenceSummaryToValue(Preference preference) {
		// TODO Auto-generated method stub
		preference.setOnPreferenceChangeListener(this);
		
		onPreferenceChange(preference, 
				PreferenceManager
					.getDefaultSharedPreferences(preference.getContext())
					.getString(preference.getKey(), ""));
		
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onPostCreate(savedInstanceState);
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		// TODO Auto-generated method stub
		String stringValue = newValue.toString();
		
		if(preference instanceof ListPreference){
			ListPreference listPreference = (ListPreference) preference;
			int prefIndex = listPreference.findIndexOfValue(stringValue);
			if(prefIndex >= 0){
				preference.setSummary(listPreference.getEntries()[prefIndex]);
			}
		}else{
			preference.setSummary(stringValue);
		}
		return true;
	}
}
