package com.p4coperez.tagbox;
 
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
 
public class AdapterElements extends BaseAdapter{
 
    protected Activity activity;
    protected ArrayList<Elemento> items;

 
public AdapterElements(Activity activity, ArrayList<Elemento> items) {
        this.activity = activity;
        this.items = items;
}
 
    @Override
    public int getCount() {
        return items.size();
    }
 
    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }
 
    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }
 

 @Override
 public View getView(int position, View convertView, ViewGroup parent) {
     // Objeto ViewHolder
     ViewHolder viewholder;

     // Generamos una convertView por motivos de eficiencia
     View v = convertView;

     //Asociamos el layout de la lista que hemos creado e incrustamos el ViewHolder
     if(convertView == null){
         LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         v = inf.inflate(R.layout.element, null);
         viewholder = new ViewHolder();
         viewholder.name = (TextView) v.findViewById(R.id.elementBox);
         viewholder.icon = (ImageView) v.findViewById(R.id.icon);
         viewholder.content = (TextView) v.findViewById(R.id.elementContent);
         viewholder.checked = (CheckBox) v.findViewById(R.id.checkBox);
         v.setTag(viewholder);
     }

     // Creamos un objeto directivo
     Elemento elem = items.get(position);

     //Rellenamos la información utilizando el ViewHolder
     viewholder = (ViewHolder) v.getTag();
     viewholder.name.setText(elem.getName());
     viewholder.content.setText(elem.getContent());
     viewholder.icon.setImageDrawable(elem.getIcon());
     viewholder.checked.setChecked (elem.getChecked());

     // Retornamos la vista
     return v;
 }
 
 public static class ViewHolder {
	    public ImageView icon;
	    public TextView name;
	    public TextView content;
	    public CheckBox checked;
	}
 
 
}