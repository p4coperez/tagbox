package com.p4coperez.tagbox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;

import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost;

import android.widget.AbsListView.MultiChoiceModeListener;

public class MainActivity extends Activity {
	private static final int STATIC_INTEGER_VALUE_CONFIG_UPDATE = 4357;
	private static final int STATIC_INTEGER_VALUE_CONFIG_ACTUAL = 0000;
	private static final String STATIC_STRING_VALUE_CONFIG_TAGBOX = "tagbox";
	private static final String STATIC_STRING_VALUE_CONFIG_TAGBOX_GROUP = "/";
	private static final String STATIC_STRING_APP_DROPSYNC = "com.ttxapps.dropsync";

	TextView tvPath;
	TextView tvPathGroup;
	TextView tv2;
	ArrayList <String> checkedValue;
	ListView listApps;
	AdapterElements listviewadapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		listApps = (ListView) findViewById(android.R.id.list);
		List<Elemento> listelements = new ArrayList<Elemento>();
		Elemento elemento;

		//ListView path of Elements
		tvPath = (TextView) findViewById(R.id.textViewRouteConfig);
		tvPathGroup = (TextView) findViewById(R.id.textViewDirConfig);
		File tarjeta = Environment.getExternalStorageDirectory();
		File dir = new File("");
		
		//Get path from extras 
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());		
		tvPath.setText(preferences.getString("route", STATIC_STRING_VALUE_CONFIG_TAGBOX));		
		tvPathGroup.setText(STATIC_STRING_VALUE_CONFIG_TAGBOX_GROUP);
		
		final TabHost tabs=(TabHost)findViewById(R.id.tabhost);
	    
	    tabs.setup();
		
		Bundle extras = getIntent().getExtras();
		if (extras != null){
			tvPath.setText(extras.getString("path"));
			tvPathGroup.setText(extras.getString("pathgroup"));
		}
		
		Intent p = new Intent(this, Configuration.class);
		p.putExtra("itemSelected",false);
		p.putExtra("pathgroup",tvPathGroup.getText().toString());
		startActivityForResult(p,STATIC_INTEGER_VALUE_CONFIG_ACTUAL);
		
		
		dir = new File(tarjeta.getAbsolutePath()+"/"+tvPath.getText().toString());

		File[] dirgroup = dir.listFiles();
		
		if (dirgroup != null) {
		
		for (int i = 0; i < dirgroup.length; i++) {
			//Get the text file
			File dirgroupitem = new File(dirgroup[i].toString());
			
			//Code for Group of Elements
			if (!dirgroupitem.isHidden() & dirgroupitem.isDirectory() ) {
				// create new tabs of directories
		        TabHost.TabSpec spec=tabs.newTabSpec(dirgroupitem.getName());
		        
		       spec.setContent(new TabHost.TabContentFactory() {
		          @Override
				public View createTabContent(String tag) {
		            return(listApps);
		          }
		        });
		        spec.setIndicator(dirgroupitem.getName());
		        tabs.addTab(spec);

			}
		
		}
		}
		
		//-----------------------------------------------------------------------

	    
	    if (!tvPathGroup.equals(tabs.getCurrentTabTag()) || tvPathGroup != null){
	    	tabs.setCurrentTabByTag(tvPathGroup.getText().toString());
	    }
	    
	    tabs.setOnTabChangedListener(new OnTabChangeListener() {

	    	@Override
	    	public void onTabChanged(String tabId) {
	    	// Code testing group items on click
	    	//int i = tabs.getCurrentTab();
	    	//Toast.makeText(MainActivity.this, "Tab Clicked: "+i+" - tabsCurrent: "+tabs.getCurrentTabTag() + "-> tvPathGroup: "+tvPathGroup.getText().toString(), Toast.LENGTH_SHORT).show();
	        tvPathGroup.setText(tabs.getCurrentTabTag());
	        updateview(tvPathGroup.getText().toString());
	    	}
	    });
	
		//-----------------------------------------------------------------------
		dir = new File(tarjeta.getAbsolutePath()+"/"+tvPathGroup.getText().toString() +"/"+tvPath.getText().toString());

		File[] filelist = dir.listFiles();
		
		if (filelist != null) {
		StringBuilder text;
		
		for (int i = 0; i < filelist.length; i++) {
			//Get the text file
			File file = new File(filelist[i].toString());
			
			//Elements to show
			if (!file.isHidden() & !file.isDirectory() ) {
				//Read text from file
				text = new StringBuilder();

				try {
				    BufferedReader br = new BufferedReader(new FileReader(file));
				    String line;

				    while ((line = br.readLine()) != null) {
				        text.append(line);
				        text.append('\n');
				    }
				    
				    //Get element to show on AdapterElements
				    elemento = new Elemento(getResources().getDrawable(R.drawable.ic_pencil),file.getName(), text.toString(),false);
			        //elemento = new Elemento(getResources().getDrawable(R.drawable.ic_launcher),file.getName(), text.substring(6).toString(),new Boolean (text.substring(0, 5).toString()));
			        listelements.add(elemento);
				 
				}
				catch (IOException e) {
				    //You'll need to add proper error handling here
				}
				
			}

		}
		
		} // end if filelist != null
		
		//AdapterElements adapter = new AdapterElements(this, arrayelements);
		listviewadapter = new AdapterElements(this,R.layout.element, listelements);
		listApps.setAdapter(listviewadapter);
		listApps.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
				
		
		// Capture ListView item click
        listApps.setMultiChoiceModeListener(new MultiChoiceModeListener() {
 
            @Override
            public void onItemCheckedStateChanged(ActionMode mode,
                    int position, long id, boolean checked) {
                // Capture total checked items
                final int checkedCount = listApps.getCheckedItemCount();
                // Set the CAB title according to total checked items
                String selecttext= getString(R.string.selected);
                mode.setTitle(checkedCount + " " +selecttext);
                // Calls toggleSelection method from AdapterElements Class
                listviewadapter.toggleSelection(position);
                // Set checked
                listviewadapter.getElemento().get(position).setChecked(checked);

            }
 
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                case R.id.archive:
                    // Calls getSelectedIds method from AdapterElements Class
                    SparseBooleanArray selected = listviewadapter
                            .getSelectedIds();
                    // Captures all selected ids with a loop
                    for (int i = (selected.size() - 1); i >= 0; i--) {
                        if (selected.valueAt(i)) {
                            Elemento selecteditem = listviewadapter
                                    .getItem(selected.keyAt(i));
                            // Remove selected items following the ids
                            listviewadapter.remove(selecteditem);
                        }
                    }
                    // Close CAB
                    mode.finish();
                    updateview(tvPathGroup.getText().toString());
                    return true;
                default:
                    return false;
                }
            }
 
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.activity_archive, menu);
       		
                return true;
            }
 
            @Override
            public void onDestroyActionMode(ActionMode mode) {
            	// Calls getSelectedIds method from AdapterElements Class
                SparseBooleanArray selected = listviewadapter
                        .getSelectedIds();
                // Captures all selected ids with a loop
                for (int i = (selected.size() - 1); i >= 0; i--) {
                    if (selected.valueAt(i)) {
                        Elemento selecteditem = listviewadapter
                                .getItem(selected.keyAt(i));
                        // Remove selected items following the ids
                     // Set checked
                        selecteditem.setChecked(false);
                    }
                }
                
                listviewadapter.removeSelection();
                // all file set checked to false
            }
 
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub
            	//listviewadapter.removeSelection();
                return false;
            }
        });

		// Button Sync
		Button buttonSync = (Button) findViewById(R.id.buttonSync);
		
		buttonSync.setOnClickListener (new OnClickListener() {
			@Override
			public void onClick(View view) {
				//Toast.makeText(MainActivity.this, "Button Sync Clicked", Toast.LENGTH_SHORT).show();
				sync(view);
			}
		});
		
		//Button Archive
        Button buttonArchive = (Button) findViewById(R.id.buttonArchive);
        
		buttonArchive.setOnClickListener (new OnClickListener() {
			@Override
			public void onClick(View view) {
				//Toast.makeText(MainActivity.this, "Button Archive Clicked", Toast.LENGTH_SHORT).show();
				
				archive(view,tvPathGroup.getText().toString());
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Menu Button Sync, Archive, Configuration and About To
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
	
		
		return true;
	}
	 
	
	@Override
	public boolean onOptionsItemSelected (MenuItem item) {
		switch (item.getItemId()) {
		case R.id.new_item:
			// custom dialog
			final Dialog dialog = new Dialog(this);
			dialog.setContentView(R.layout.new_item);
			String new_itemtext= getString(R.string.new_item);
			dialog.setTitle(new_itemtext);
			
			Button dialogButton = (Button) dialog.findViewById(R.id.buttonItemButton);
						// if button is clicked, close the custom dialog
						dialogButton.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// get the custom dialog components - texts
								EditText textitemname = (EditText) dialog.findViewById(R.id.editTextItemName);							
								EditText textitemcontent = (EditText) dialog.findViewById(R.id.editTextItemContent);				
																
								if (add_item(textitemname.getText().toString(),textitemcontent.getText().toString())){
									updateview(tvPathGroup.getText().toString());
								}
								
								dialog.dismiss();
								
								
							}
						});
			 
			 dialog.show();
			break;
			
		case R.id.config:
			Intent p = new Intent(this, Configuration.class);
			p.putExtra("itemSelected",true);
			p.putExtra("pathgroup",tvPathGroup.getText().toString());
			startActivityForResult(p,STATIC_INTEGER_VALUE_CONFIG_UPDATE);
			break;
		
		case R.id.aboutto:
			Intent i = new Intent(this, AboutTo.class);
			startActivity(i);
			break;
			
		}
		
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
 
		if (requestCode==STATIC_INTEGER_VALUE_CONFIG_UPDATE && resultCode== RESULT_OK ){ 
			
			updateview(data.getStringExtra("pathgroup"));
		}
		if (requestCode==STATIC_INTEGER_VALUE_CONFIG_ACTUAL && resultCode== RESULT_OK && data.getStringExtra("path")!="" && data.getStringExtra("pathgroup")!=""  ){
			tvPath = (TextView) findViewById(R.id.textViewRouteConfig);
			tvPath.setText(data.getStringExtra("path"));
			tvPathGroup = (TextView) findViewById(R.id.textViewDirConfig);
			tvPathGroup.setText(data.getStringExtra("pathgroup")); 
			// only for testing path route : to delete
			//tv2 = (TextView) findViewById(R.id.textViewCache);
			//tv2.setText(data.getStringExtra("path"));
		}
	}

	public void updateview (String tvPathGroup){
		Intent refresh = new Intent(this, MainActivity.class);

		// Get path update
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		refresh.putExtra("path",preferences.getString("route", STATIC_STRING_VALUE_CONFIG_TAGBOX));
		refresh.putExtra("pathgroup",tvPathGroup);
	    startActivity(refresh);
	    this.finish();
		
	}
	
	protected void launchApp(String packageName) {
		String app_not_foundtext= getString(R.string.app_not_found);
		Intent mIntent = getPackageManager().getLaunchIntentForPackage(
				packageName);
		if (mIntent != null) {
			try {
				super.startActivity(mIntent);
					
			} catch (ActivityNotFoundException err) {
				
				Toast.makeText(MainActivity.this,app_not_foundtext, Toast.LENGTH_SHORT).show();
			}
		}
		else{
			Toast.makeText(MainActivity.this,app_not_foundtext, Toast.LENGTH_SHORT).show();

		}

	}
	
	public void sync(View v) {
		
		String synctext= getString(R.string.sync);
		Toast.makeText(MainActivity.this, synctext.toString() , Toast.LENGTH_SHORT).show();

		launchApp(STATIC_STRING_APP_DROPSYNC);
		
		// only for testing path route : to delete
		//tv2 = (TextView) findViewById(R.id.textViewCache);
		//tv2.setText("sync: "+((TextView) findViewById(R.id.textViewRouteConfig)).getText().toString());
			
	}
	
	public void archive(View v, String tvPathGroup) {
		String archivetext= getString(R.string.archive);
		// Calls getSelectedIds method from AdapterElements Class
        SparseBooleanArray selected = listviewadapter
                .getSelectedIds();
        // Captures all selected ids with a loop
        for (int i = (selected.size() - 1); i >= 0; i--) {
            if (selected.valueAt(i)) {
                Elemento selecteditem = listviewadapter
                        .getItem(selected.keyAt(i));
                // Remove selected items following the ids
                listviewadapter.remove(selecteditem);
                // Delete Files
                if (delete_item(selecteditem.getName())){
                	
            		Toast.makeText(MainActivity.this, archivetext , Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (selected.size()==0){
        	Toast.makeText(MainActivity.this, archivetext , Toast.LENGTH_SHORT).show();
        }
        // only for testing path route : to delete
		//tv2 = (TextView) findViewById(R.id.textViewCache);
		//tv2.setText("archive: "+((TextView) findViewById(R.id.textViewRouteConfig)).getText().toString());
		
        updateview(tvPathGroup);
		

	}
	
	protected boolean delete_item (String filename){
		File tarjeta = Environment.getExternalStorageDirectory();
		File tagfile = new File(tarjeta.getAbsolutePath()+"/"+tvPathGroup.getText().toString()+"/"+tvPath.getText().toString()+"/"+filename);

		boolean deleted = tagfile.delete();
		if (deleted){
			//Toast.makeText(this, "File deleted: " + tagfile.getName(),Toast.LENGTH_SHORT).show();
		}
		else{
			String errordeleteitemtext= getString(R.string.errordeleteitem);
			Toast.makeText(this, errordeleteitemtext+ " "+ tagfile.getName(),Toast.LENGTH_SHORT).show();
		}
		return deleted;
	}
	
	protected boolean add_item (String filename,String contenido){
		File tarjeta = Environment.getExternalStorageDirectory();
		File tagfile = new File(tarjeta.getAbsolutePath()+"/"+tvPathGroup.getText().toString()+"/"+tvPath.getText().toString()+"/"+filename);
		boolean added = false;
		try {
			
			OutputStreamWriter osw = new OutputStreamWriter(
					new FileOutputStream(tagfile));
			osw.write(contenido);
			osw.flush();
			osw.close();
			added = true;
			//Toast.makeText(this, "File added: "+ tagfile.getName(),Toast.LENGTH_SHORT).show();
			
			//iv1.setImageResource(R.id.imageButtonArchive);
		} catch (IOException e) {
			String erroradditemtext= getString(R.string.erroradditem);
			Toast.makeText(this, erroradditemtext + " " + tagfile.getName(),Toast.LENGTH_SHORT).show();
		}
		return added;
	}

}
