package com.p4coperez.tagbox;
 
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
 
public class AdapterElements extends ArrayAdapter<Elemento> {
 
    protected Activity activity;
    protected ArrayList<Elemento> items;
    
    Context context;
    LayoutInflater inflater;
    List<Elemento> itemslist;
    private SparseBooleanArray mSelectedItemsIds;
 
public AdapterElements(Context context, int resourceId, List<Elemento> itemslist) {
        super(context, resourceId, itemslist);
        mSelectedItemsIds = new SparseBooleanArray();
        this.context = context;
        this.itemslist = itemslist;
        inflater = LayoutInflater.from(context);
    }

public static class ViewHolder {
    public ImageView icon;
    public TextView name;
    public TextView content;
    public CheckBox checked;
}

 /*
public AdapterElements(Activity activity, ArrayList<Elemento> items) {
        this.activity = activity;
        this.items = items;
}
 
    @Override
    public int getCount() {
        return items.size();
    }
 
    @Override
    public Elemento getItem(int arg0) {
        return items.get(arg0);
    }
 
    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }
 */

 @Override
 public View getView(int position, View convertView, ViewGroup parent) {
     // Objeto ViewHolder
     ViewHolder viewholder;

     // Generamos una convertView por motivos de eficiencia
     View v = convertView;

     //Asociamos el layout de la lista que hemos creado e incrustamos el ViewHolder
     if(convertView == null){
         //LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         v = inflater.inflate(R.layout.element, null);
         viewholder = new ViewHolder();
         viewholder.name = (TextView) v.findViewById(R.id.elementBox);
         viewholder.icon = (ImageView) v.findViewById(R.id.icon);
         viewholder.content = (TextView) v.findViewById(R.id.elementContent);
         viewholder.checked = (CheckBox) v.findViewById(R.id.checkBox);
         v.setTag(viewholder);
     } else {
         viewholder = (ViewHolder) v.getTag();
     }

     // Creamos un objeto directivo
     // Elemento elem = items.get(position);

     //Rellenamos la información utilizando el ViewHolder
     viewholder = (ViewHolder) v.getTag();
     viewholder.name.setText(itemslist.get(position).getName());
     viewholder.content.setText(itemslist.get(position).getContent());
     viewholder.icon.setImageDrawable(itemslist.get(position).getIcon());
     viewholder.checked.setChecked (itemslist.get(position).getChecked());

     // Retornamos la vista
     return v;
 }
 
 @Override
 public void remove(Elemento object) {
     itemslist.remove(object);
     notifyDataSetChanged();
 }

 public List<Elemento> getElemento() {
     return itemslist;
 }

 public void toggleSelection(int position) {
     selectView(position, !mSelectedItemsIds.get(position));
 }

 public void removeSelection() {
     mSelectedItemsIds = new SparseBooleanArray();
     notifyDataSetChanged();
 }

 public void selectView(int position, boolean value) {
     if (value)
         mSelectedItemsIds.put(position, value);
     else
         mSelectedItemsIds.delete(position);
     notifyDataSetChanged();
 }

 public int getSelectedCount() {
     return mSelectedItemsIds.size();
 }

 public SparseBooleanArray getSelectedIds() {
     return mSelectedItemsIds;
 }
 
}