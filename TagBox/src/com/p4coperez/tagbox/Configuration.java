package com.p4coperez.tagbox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.TextView;

public class Configuration extends PreferenceActivity {
	private static final String STATIC_STRING_VALUE_CONFIG_TAGBOX = "tagbox";
	TextView textviewPath;

	/** Called when the activity is first created */
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.configuration);
		//Preference pref = findPreference("route");
		Bundle extras = getIntent().getExtras();
			

	 	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	 	
	 	OnSharedPreferenceChangeListener listener = new OnSharedPreferenceChangeListener() {
	 		
	 		@Override
	 		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
	 			
	 			
	 			if(key.equals(getResources().getString(R.string.route))){
	 				 //Preference pref = findPreference("route");
	 				 Bundle extras = getIntent().getExtras();
	 				 
	 				 		
/*	 				//ThemeUtil.setTheme(PreferenceView.this, sharedPreferences.getString(key, ""));	 			    
	 				 // Create object of SharedPreferences.
	 				 //now get Editor
	 				 SharedPreferences.Editor editor = pref.getEditor();
	 				 //put your value
	 				 editor.putString("path", sharedPreferences.getString("route", "sdcard"));

	 				 //commits your edits
	 				 editor.commit();
	 				  // Return value path to MainActivity to sync with dropbox
*/					if (extras.getBoolean("itemSelected")){
						//pref.setSummary(sharedPreferences.getString("route", "sdcard"));
	 					Intent p = new Intent();
	 					p.putExtra("path", sharedPreferences.getString("route", STATIC_STRING_VALUE_CONFIG_TAGBOX));
	 					setResult(RESULT_OK,p);
	 					finish();
					}

	 				
	 			}
	 			
	 		}
	 	};
	 	
	 	
	 	    
	 	    Intent p = new Intent();
    
        	if (extras.getBoolean("itemSelected")== false){
        		
        		p.putExtra("path", preferences.getString("route", STATIC_STRING_VALUE_CONFIG_TAGBOX));
            	setResult(RESULT_OK,p);
            	//pref.setSummary(preferences.getString("route", "sdcard"));
            	finish();
        	}
        	else {
        		p.putExtra("path", preferences.getString("route", STATIC_STRING_VALUE_CONFIG_TAGBOX));
            	//pref.setSummary(preferences.getString("route", "sdcard"));
            	setResult(RESULT_OK,p);
            	preferences.registerOnSharedPreferenceChangeListener(listener); //To avoid GC issues
        		
  
        	}

        	
        	
	}

		
	
}
