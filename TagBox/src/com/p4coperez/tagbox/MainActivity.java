package com.p4coperez.tagbox;

/*
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ListActivity {
	private static final int STATIC_INTEGER_VALUE_CONFIG_UPDATE = 4357;
	private static final int STATIC_INTEGER_VALUE_CONFIG_ACTUAL = 0000;

	TextView tv1;
	ImageView iv1;
	ImageView iv2;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setListAdapter(null);
		
		//et1.setText(text1.getText().toString());
		//et1.setText(text2.getText().toString());
		
		/*SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		tv1 = (TextView) findViewById(R.id.textView1);
		tv1.setText(preferences.getString("route", "/sdcard/")); 
		*/
		Intent p = new Intent(this, Configuration.class);
		p.putExtra("itemSelected",false);
		startActivityForResult(p,STATIC_INTEGER_VALUE_CONFIG_ACTUAL);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
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
		    startActivity(refresh);
		    this.finish();
			//tv1 = (TextView) findViewById(R.id.textView1);
			//tv1.setText(data.getStringExtra("path"));
		}
		if (requestCode==STATIC_INTEGER_VALUE_CONFIG_ACTUAL && resultCode== RESULT_OK && data.getStringExtra("path")!="" ){
			tv1 = (TextView) findViewById(R.id.textView1);
			tv1.setText(data.getStringExtra("path")); 
		}
	}

	
	public void sync(View v) {
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
	}
	
	public void archive(View v) {
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
	}
/**
 	public void grabar(View v) {
 
		String nomarchivo = et1.getText().toString();
		String contenido = et2.getText().toString();
		try {
			File tarjeta = Environment.getExternalStorageDirectory();
			File file = new File(tarjeta.getAbsolutePath(), nomarchivo);
			OutputStreamWriter osw = new OutputStreamWriter(
					new FileOutputStream(file));
			osw.write(contenido);
			osw.flush();
			osw.close();
			Toast.makeText(this, "Los datos fueron grabados correctamente",
					Toast.LENGTH_SHORT).show();
			et1.setText("");
			et2.setText("");
		} catch (IOException ioe) {
		}
	}

	public void recuperar(View v) {
		String nomarchivo = et1.getText().toString();
		File tarjeta = Environment.getExternalStorageDirectory();
		File file = new File(tarjeta.getAbsolutePath(), nomarchivo);
		try {
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
			et2.setText(todo);

		} catch (IOException e) {
		}
	}
*/
}
