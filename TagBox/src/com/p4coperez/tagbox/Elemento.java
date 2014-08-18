package com.p4coperez.tagbox;

import android.graphics.drawable.Drawable;

 
public class Elemento {
    protected Drawable icon;
    protected String name;
    protected String content;
    protected boolean checked;
    protected long id;
 
    public Elemento(Drawable elem_icon, String elem_name, String cargo, boolean check) {
        super();
        this.icon = elem_icon;
        this.name = elem_name;
        this.content = cargo;
        this.checked = check;
    }
 
    public Elemento(Drawable elem_icon, String elem_name, String elem_content, boolean elem_checked, long id) {
        super();
        this.icon = elem_icon;
        this.name = elem_name;
        this.content = elem_content;
        this.checked = elem_checked;
        this.id = id;
    }
 
    public Drawable getIcon() {
        return icon;
    }
 
    public void setIcon(Drawable elem_icon) {
        this.icon = elem_icon;
    }
 
    public String getName() {
        return name;
    }
 
    public void setName(String elem_name) {
        this.name = elem_name;
    }
 
    public String getContent() {
        return content;
    }
    
    public boolean getChecked() {
        return checked;
    }
 
    public void setContent(String elem_content) {
        this.content = elem_content;
    }
    
    public void setChecked(boolean elem_checked) {
        this.checked = elem_checked;
    }
 
    public long getId() {
        return id;
    }
 
    public void setId(long id) {
        this.id = id;
    }
}