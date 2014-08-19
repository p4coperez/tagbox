package com.p4coperez.tagbox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ListActivity {
	private static final int STATIC_INTEGER_VALUE_CONFIG_UPDATE = 4357;
	private static final int STATIC_INTEGER_VALUE_CONFIG_ACTUAL = 0000;
	private static final String STATIC_STRING_VALUE_CONFIG_TAGBOX = "tagbox";

	TextView tvPath;
	TextView tv2;
	ArrayList <String> checkedValue;
	ListView listApps;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Intent p = new Intent(this, Configuration.class);
		p.putExtra("itemSelected",false);
		startActivityForResult(p,STATIC_INTEGER_VALUE_CONFIG_ACTUAL);
		

		
		listApps = (ListView) findViewById(android.R.id.list);
		ArrayList<Elemento> arrayelements = new ArrayList<Elemento>();
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
			File file = new File(tarjeta.getAbsolutePath()+"/"+tvPath.getText().toString(),filelist[i].toString());
			
			//Code for testing   ListView - Adapter Element getView()
			if (!file.isHidden()) {
			elemento = new Elemento(getResources().getDrawable(R.drawable.ic_launcher),filelist[i].toString(), "5",false);
	        arrayelements.add(elemento);
			}
			
			if (file.isFile() && !file.isHidden()) {
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
			        elemento = new Elemento(getResources().getDrawable(R.drawable.ic_launcher),filelist[i].toString(), text.toString(),false);
			        arrayelements.add(elemento);
				 
				}
				catch (IOException e) {
				    //You'll need to add proper error handling here
				}
				
			}

		}
		
		} // end if filelist != null
		
		AdapterElements adapter = new AdapterElements(this, arrayelements);
		listApps.setAdapter(adapter);
		
		//listApps.setOnItemClickListener((OnItemClickListener) this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Menu Configuration and About To
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		
		// Buttons Sync and Archive
		Button buttonSync = (Button) findViewById(R.id.buttonSync);
		Button buttonArchive = (Button) findViewById(R.id.buttonArchive);
		
		buttonSync.setOnClickListener (new OnClickListener() {
			public void onClick(View view) {
				Toast.makeText(MainActivity.this, "Button Clicked", Toast.LENGTH_SHORT).show();
				sync(view);
			}
		});
		
		buttonArchive.setOnClickListener (new OnClickListener() {
			public void onClick(View view) {
				Toast.makeText(MainActivity.this, "Button Clicked", Toast.LENGTH_SHORT).show();
				archive(view);
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
			Intent refresh = new Intent(this, MainActivity.class);
			tvPath = (TextView) findViewById(R.id.textViewRouteConfig);
			// Get path update
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			refresh.putExtra("path",preferences.getString("route", "sdcard"));
		    startActivity(refresh);
		    this.finish();
		}
		if (requestCode==STATIC_INTEGER_VALUE_CONFIG_ACTUAL && resultCode== RESULT_OK && data.getStringExtra("path")!="" ){
			tvPath = (TextView) findViewById(R.id.textViewRouteConfig);
			tvPath.setText(data.getStringExtra("path")); 
			// only for testing path route : to delete
			tv2 = (TextView) findViewById(R.id.textViewCache);
			tv2.setText(data.getStringExtra("path"));
		}
	}

	
	public void sync(View v) {
		tv2 = (TextView) findViewById(R.id.textViewCache);
		tv2.setText("sync: "+((TextView) findViewById(R.id.textViewRouteConfig)).getText().toString());
		
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
	
	public void archive(View v) {
		tv2 = (TextView) findViewById(R.id.textViewCache);
		tv2.setText("archive: "+((TextView) findViewById(R.id.textViewRouteConfig)).getText().toString());
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
