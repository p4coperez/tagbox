package com.p4coperez.tagbox;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.widget.TextView;

public class Configuration extends PreferenceActivity {
	TextView textviewPath;

	/** Called when the activity is first created */
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.configuration);
		   
		Preference pref = findPreference("route");
		pref.setSummary(pref.getSummary().toString()+" "+pref.getSharedPreferences().getString("route", "sdcard"));
	    // Return value path to MainActivity to sync with dropbox
	}

	
}
