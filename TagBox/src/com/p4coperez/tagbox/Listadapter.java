package com.p4coperez.tagbox;
 
 
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
 
public class Listadapter extends BaseAdapter{
 
 String[] elementsName;
 String[] elementsText;
 Activity context;
 boolean[] elementsChecked;
 
 public Listadapter(Activity context, String[] itemsListElemetsName,
   String[] itemsListElemetsText, boolean[] itemsListElementsChecked) {
  super();
  this.context = context;
  this.elementsName = itemsListElemetsName;
  this.elementsText = itemsListElemetsText;
  this.elementsChecked = itemsListElementsChecked;
 }
 
 private class ViewHolder {
  TextView apkName;
  CheckBox ck1;
 }
 
 public int getCount() {
  return elementsName.length;
 }
 
 public Object getItemName(int position) {
  return elementsName[position];
 }
 
 public boolean getItemChecked(int position) {
	  return elementsChecked[position];
	 }
 
 public Object getItemText(int position) {
	  return elementsText[position];
	 }
 
 public long getItemId(int position) {
  return 0;
 }
 
 @Override
 public Object getItem(int position) {
  return null;
 }
 
 @Override
 public View getView(final int position, View convertView, ViewGroup parent) {
  final ViewHolder holder;
   
  LayoutInflater inflater = context.getLayoutInflater();
 
  if (convertView == null) {
   convertView = inflater.inflate(R.layout.element, null);
   holder = new ViewHolder();
 
   holder.apkName = (TextView) convertView
     .findViewById(R.id.elementBox);
   holder.ck1 = (CheckBox) convertView
     .findViewById(R.id.checkBox);
 
   convertView.setTag(holder);
 
  } else {
    
   holder = (ViewHolder) convertView.getTag();
  }
 
 
 
  /*Drawable appIcon = packageManager
    .getApplicationIcon(packageInfo.applicationInfo);*/
  String appName = (String) getItemName(position);
  boolean appChecked = (boolean) getItemChecked(position);
  /*appIcon.setBounds(0, 0, 40, 40);
  holder.apkName.setCompoundDrawables(appIcon, null, null, null);
  holder.apkName.setCompoundDrawablePadding(15);*/
  holder.apkName.setText(appName);
  holder.ck1.setChecked(appChecked);
 
  if (elementsChecked[position])
   holder.ck1.setChecked(true);
  else
   holder.ck1.setChecked(false);
 
  holder.ck1.setOnClickListener(new OnClickListener() {
   @Override
   public void onClick(View v) {
    // TODO Auto-generated method stub
    if (holder.ck1.isChecked())
    	elementsChecked[position] = true;
    else
    	elementsChecked[position] = false;
   }
  });
 
  return convertView;
 
 }
 
 
 
}