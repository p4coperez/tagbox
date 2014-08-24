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
import android.graphics.Typeface;
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
	private static final String STATIC_STRING_CONFIG_FONTS ="fonts/mvboli.ttf";
	
	TextView tvPath;
	TextView tvPathGroup;
	ArrayList <String> checkedValue;
	ListView listApps;
	AdapterElements listviewadapter;
	TabHost tabs;
	Typeface face;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Font by default
		FontsOverride.setDefaultFont(this, "MONOSPACE", STATIC_STRING_CONFIG_FONTS);
		
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
		
		tabs=(TabHost)findViewById(R.id.tabhost);
	    
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
		
		
		// show groups
		
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
		     // set by default the first group tabs
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
		// set size text on Tabs Group
		/*for (int i = 0; i < tabs.getTabWidget().getChildCount(); i++) {
	        TextView tv = (TextView) tabs.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
	        tv.setTextSize(9);
	    }*/
		
		// get group to show after to archive
	    if (!tvPathGroup.equals(tabs.getCurrentTabTag()) && tvPathGroup.getText().toString() != STATIC_STRING_VALUE_CONFIG_TAGBOX_GROUP){
	    	tabs.setCurrentTabByTag(tvPathGroup.getText().toString());
	    }
	    else{
			p.putExtra("itemSelected",false);
			tabs.setCurrentTab(0);
			p.putExtra("pathgroup",tabs.getCurrentTabTag());
			tabs.setCurrentTabByTag(tabs.getCurrentTabTag());
			startActivityForResult(p,STATIC_INTEGER_VALUE_CONFIG_ACTUAL);
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
	    
	    // get tabs after configuration
	    if (tvPathGroup.getText().toString() == STATIC_STRING_VALUE_CONFIG_TAGBOX_GROUP){
	    	tvPathGroup.setText(tabs.getCurrentTabTag());
	    }
	    
		 // get elements
		dir = new File(tarjeta.getAbsolutePath() +"/"+tvPath.getText().toString()+"/"+tvPathGroup.getText().toString());

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
                        // Set checked to false
                        selecteditem.setChecked(false);
                    }
                }
                
                listviewadapter.removeSelection();
                // all file set checked to false
            }
 
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub
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
																
								if (add_item(textitemname.getText().toString(),textitemcontent.getText().toString(),tvPathGroup.getText().toString())){
									updateview(tvPathGroup.getText().toString());
								}
								
								dialog.dismiss();
								
								
							}
						});
			 
			 dialog.show();
			break;
			
		case R.id.new_group:
			// custom dialog
			final Dialog dialoggroup = new Dialog(this);
			dialoggroup.setContentView(R.layout.new_group);
			String new_grouptext= getString(R.string.new_group);
			dialoggroup.setTitle(new_grouptext);
			
			Button dialogButtonGroup = (Button) dialoggroup.findViewById(R.id.buttonGroupButton);
						// if button is clicked, close the custom dialog
						dialogButtonGroup.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// get the custom dialog components - texts
								EditText textgroupname = (EditText) dialoggroup.findViewById(R.id.editTextGroupName);							
												
																
								if (add_group(textgroupname.getText().toString())){
									// show new group by default
									//updateview(tvPathGroup.getText().toString());
									updateview(textgroupname.getText().toString());
								}
								
								dialoggroup.dismiss();
								
								
							}
						});
			 
			 dialoggroup.show();
			break;
			
		case R.id.delete_group:
			// custom dialog
			final Dialog dialoggroupdelete = new Dialog(this);
			dialoggroupdelete.setContentView(R.layout.delete_group);
			String delete_grouptext= getString(R.string.delete_group);
			dialoggroupdelete.setTitle(delete_grouptext);
			TextView textgroupname = (TextView) dialoggroupdelete.findViewById(R.id.editTextGroupNameDelete);							
			textgroupname.setText(tvPathGroup.getText().toString());
			
			Button dialogButtonGroupDelete = (Button) dialoggroupdelete.findViewById(R.id.buttonGroupButtonDelete);
						// if button is clicked, close the custom dialog
						dialogButtonGroupDelete.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// get the custom dialog components - texts
												
																
								if (delete_group(tvPathGroup.getText().toString())){
									// show none group by default
									//updateview(tvPathGroup.getText().toString());
									tabs.setCurrentTab(tabs.indexOfChild(v)+1);
									updateview(tabs.getCurrentTabTag());
								}
								
								dialoggroupdelete.dismiss();
								
								
							}
						});
			 
			 dialoggroupdelete.show();
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
                if (delete_item(selecteditem.getName(),tvPathGroup)){
                	
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
	
	protected boolean delete_item (String filename, String tvPathGroup){
		File tarjeta = Environment.getExternalStorageDirectory();
		File tagfile = new File(tarjeta.getAbsolutePath()+"/"+tvPath.getText().toString()+"/"+tvPathGroup+"/"+filename);

		boolean deleted = tagfile.delete();
		if (deleted){
			//String deleteitemtext= getString(R.string.deleteitem);
			//Toast.makeText(this, deleteitemtext+" " + tagfile.getName(),Toast.LENGTH_SHORT).show();
		}
		else{
			String errordeleteitemtext= getString(R.string.errordeleteitem);
			Toast.makeText(this, errordeleteitemtext+ " "+tarjeta.getAbsolutePath()+"/"+tvPath.getText().toString()+"/"+tvPathGroup+"/"+tagfile.getName(),Toast.LENGTH_SHORT).show();
		}
		return deleted;
	}
	
	protected boolean add_item (String filename,String contenido,String tvPathGroup ){
		File tarjeta = Environment.getExternalStorageDirectory();
		File tagfile = new File(tarjeta.getAbsolutePath()+"/"+tvPath.getText().toString()+"/"+tvPathGroup+"/"+filename);
		boolean added = false;
		try {
			
			OutputStreamWriter osw = new OutputStreamWriter(
					new FileOutputStream(tagfile));
			osw.write(contenido);
			osw.flush();
			osw.close();
			added = true;
			//String additemtext= getString(R.string.additem);
			//Toast.makeText(this, additemtext+" "+ tagfile.getName()+"  --- ruta:" +tarjeta.getAbsolutePath()+"/"+tvPath.getText().toString()+"/"+tvPathGroup+"/",Toast.LENGTH_SHORT).show();
			
			//iv1.setImageResource(R.id.imageButtonArchive);
		} catch (IOException e) {
			String erroradditemtext= getString(R.string.erroradditem);
			Toast.makeText(this, erroradditemtext + " " + tarjeta.getAbsolutePath()+"/"+tvPath.getText().toString()+"/"+tvPathGroup+"/"+tagfile.getName(),Toast.LENGTH_SHORT).show();
		}
		return added;
	}

	protected boolean add_group (String filedir ){
		File tarjeta = Environment.getExternalStorageDirectory();
		File dirfile = new File(tarjeta.getAbsolutePath()+"/"+tvPath.getText().toString()+"/"+filedir);
		boolean added = false;
		//try {
		if (!dirfile.exists()) {
	        if (!dirfile.mkdirs()) {
	        	String erroraddgrouptext= getString(R.string.erroraddgroup);
	    		Toast.makeText(this, erroraddgrouptext + " " + tarjeta.getAbsolutePath()+"/"+tvPath.getText().toString()+"/"+dirfile.getName(),Toast.LENGTH_SHORT).show();	
	        }
	        else{
	        	//String addgrouptext= getString(R.string.addgroup);
	        	//Toast.makeText(this, addgrouptext+" "+ dirfile.getName()+"  --- ruta:" +tarjeta.getAbsolutePath()+"/"+tvPath.getText().toString()+"/",Toast.LENGTH_SHORT).show();
	        	added = true;
	        }
		}
	        	
		return added;
	}
	
	protected boolean delete_group (String filedir){
		File tarjeta = Environment.getExternalStorageDirectory();
		File dirfile = new File(tarjeta.getAbsolutePath()+"/"+tvPath.getText().toString()+"/"+filedir);
		boolean deleted = false;
		//try {
		if (dirfile.exists()) {
	        if (!dirfile.delete()) {
	        	String errordeletegrouptext= getString(R.string.errordeletegroup);
	    		Toast.makeText(this, errordeletegrouptext + " " + tarjeta.getAbsolutePath()+"/"+tvPath.getText().toString()+"/"+dirfile.getName(),Toast.LENGTH_SHORT).show();	
	        }
	        else{
	        	//String deletegrouptext= getString(R.string.addgroup);
	        	//Toast.makeText(this, deletegrouptext+" "+ dirfile.getName()+"  --- ruta:" +tarjeta.getAbsolutePath()+"/"+tvPath.getText().toString()+"/",Toast.LENGTH_SHORT).show();
	        	deleted = true;
	        }
		}
	        	
		return deleted;
	}
	
}
