package com.p4coperez.tagbox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
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

import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.AbsListView.MultiChoiceModeListener;

public class MainActivity extends Activity {
	private static final int STATIC_INTEGER_VALUE_CONFIG_UPDATE = 4357;
	private static final int STATIC_INTEGER_VALUE_CONFIG_ACTUAL = 0000;
	private static final String STATIC_STRING_VALUE_CONFIG_TAGBOX = "tagbox";

	TextView tvPath;
	TextView tv2;
	ArrayList <String> checkedValue;
	ListView listApps;
	AdapterElements listviewadapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Intent p = new Intent(this, Configuration.class);
		p.putExtra("itemSelected",false);
		startActivityForResult(p,STATIC_INTEGER_VALUE_CONFIG_ACTUAL);
		
		listApps = (ListView) findViewById(android.R.id.list);
		List<Elemento> listelements = new ArrayList<Elemento>();
		Elemento elemento;

		//ListView path of Elements
		tvPath = (TextView) findViewById(R.id.textViewRouteConfig);
		File tarjeta = Environment.getExternalStorageDirectory();
		File dir = new File("");
		
		//Get path from extras 
		tvPath.setText(STATIC_STRING_VALUE_CONFIG_TAGBOX);
		
		Bundle extras = getIntent().getExtras();
		
		if (extras != null){
			tvPath.setText(extras.getString("path"));
		}
		
		dir = new File(tarjeta.getAbsolutePath()+"/"+tvPath.getText().toString());

		File[] filelist = dir.listFiles();
		
		if (filelist != null) {
		StringBuilder text;
		
		for (int i = 0; i < filelist.length; i++) {
			//Get the text file
			File file = new File(filelist[i].toString());
			
			//Code for testing   ListView - Adapter Element getView()
			if (!file.isHidden() & file.isDirectory() ) {
			elemento = new Elemento(getResources().getDrawable(R.drawable.ic_launcher),file.getName(), "dir",false);
	        listelements.add(elemento);
			}
			
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
			        elemento = new Elemento(getResources().getDrawable(R.drawable.ic_launcher),file.getName(), text.toString(),false);
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
		listApps.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
				
		
		// Capture ListView item click
        listApps.setMultiChoiceModeListener(new MultiChoiceModeListener() {
 
            @Override
            public void onItemCheckedStateChanged(ActionMode mode,
                    int position, long id, boolean checked) {
                // Capture total checked items
                final int checkedCount = listApps.getCheckedItemCount();
                // Set the CAB title according to total checked items
                String selecttext= getString(R.string.select);
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
                // TODO Auto-generated method stub
                listviewadapter.removeSelection();
            }
 
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub
                return false;
            }
        });

	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Menu Button Sync, Archive, Configuration and About To
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		
		// Buttons Sync
		Button buttonSync = (Button) findViewById(R.id.buttonSync);
		
		buttonSync.setOnClickListener (new OnClickListener() {
			public void onClick(View view) {
				//Toast.makeText(MainActivity.this, "Button Sync Clicked", Toast.LENGTH_SHORT).show();
				sync(view,menu);
			}
		});
		
		//Button Archive
        Button buttonArchive = (Button) findViewById(R.id.buttonArchive);
        
		buttonArchive.setOnClickListener (new OnClickListener() {
			public void onClick(View view) {
				//Toast.makeText(MainActivity.this, "Button Archive Clicked", Toast.LENGTH_SHORT).show();
				archive(view,menu);
			}
		});
		return true;
	}
	 
	
	@Override
	public boolean onOptionsItemSelected (MenuItem item) {
		switch (item.getItemId()) {
		case R.id.config:
			Intent p = new Intent(this, Configuration.class);
			p.putExtra("itemSelected",true);
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
			updateview();
		}
		if (requestCode==STATIC_INTEGER_VALUE_CONFIG_ACTUAL && resultCode== RESULT_OK && data.getStringExtra("path")!="" ){
			tvPath = (TextView) findViewById(R.id.textViewRouteConfig);
			tvPath.setText(data.getStringExtra("path")); 
			// only for testing path route : to delete
			tv2 = (TextView) findViewById(R.id.textViewCache);
			tv2.setText(data.getStringExtra("path"));
		}
	}

	public void updateview (){
		Intent refresh = new Intent(this, MainActivity.class);
		tvPath = (TextView) findViewById(R.id.textViewRouteConfig);
		// Get path update
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		refresh.putExtra("path",preferences.getString("route", STATIC_STRING_VALUE_CONFIG_TAGBOX));
	    startActivity(refresh);
	    this.finish();
		
	}
	
	public void sync(View v, Menu menu) {
		
		String synctext= getString(R.string.sync);
		Toast.makeText(MainActivity.this, synctext.toString() , Toast.LENGTH_SHORT).show();
		
		updateview();
		
		
		//tv2 = (TextView) findViewById(R.id.textViewCache);
		//tv2.setText("sync: "+((TextView) findViewById(R.id.textViewRouteConfig)).getText().toString());
				
		/*
		tv1 = (TextView) findViewById(R.id.textView1);
		iv1 = (ImageView) findViewById(R.id.imageButtonSync);
		String filename = tv1.getText().toString();;
		try {
			File tarjeta = Environment.getExternalStorageDirectory();
			File file = new File(tarjeta.getAbsolutePath(), filename);

				// Restore file from Dropbox path on tv1
				FileInputStream fIn = new FileInputStream(file);
				InputStreamReader archivo = new InputStreamReader(fIn);
				BufferedReader br = new BufferedReader(archivo);
				String linea = br.readLine();
				String todo = "";
				while (linea != null) {
					todo = todo + linea + "\n";
					linea = br.readLine();
				}
				br.close();
				archivo.close();
				//iv1.setImageResource(R.id.imageButtonSync);
				
				// todo : show in listview
				
		} catch (IOException e) {
		}
		*/
	}
	
	public void archive(View v, Menu menu) {
		
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
        
		//tv2 = (TextView) findViewById(R.id.textViewCache);
		//tv2.setText("archive: "+((TextView) findViewById(R.id.textViewRouteConfig)).getText().toString());
		
        String archivetext= getString(R.string.archive);
		Toast.makeText(MainActivity.this, archivetext.toString() , Toast.LENGTH_SHORT).show();

		updateview();
		
        
        
		/*
		tv1 = (TextView) findViewById(R.id.textView1);
		iv1 = (ImageView) findViewById(R.id.imageButtonArchive);
		
		String filename = tv1.getText().toString();
		String contenido = "contenido";
		
		try {
			File tarjeta = Environment.getExternalStorageDirectory();
			File file = new File(tarjeta.getAbsolutePath(), filename);
			OutputStreamWriter osw = new OutputStreamWriter(
					new FileOutputStream(file));
			osw.write(contenido);
			osw.flush();
			osw.close();
			Toast.makeText(this, "Los datos fueron grabados correctamente",
					Toast.LENGTH_SHORT).show();
			
			//iv1.setImageResource(R.id.imageButtonArchive);
		} catch (IOException e) {
		}
		*/
		

	}

}
