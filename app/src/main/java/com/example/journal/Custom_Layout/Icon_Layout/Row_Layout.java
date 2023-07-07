package com.example.journal.Custom_Layout.Icon_Layout;

import android.content.Context;
import android.view.ViewGroup;

import com.example.journal.Custom_Layout.Utility.Image_Processing;
import com.example.journal.Custom_Layout.Utility.ViewFactory;

import java.util.ArrayList;

public class Row_Layout {
    static int count = 0;
    int id;
    ArrayList<Icon> icons = new ArrayList<>();
    Group_Layout group;
    int icon_width = Image_Processing.icon_draw_width;
    public Row_Layout(Group_Layout group){
        this.group = group;
    }

    public Context context(){
        return group.context;
    }

    public int row(){
        return group.rows.indexOf(this);
    }

    public void add(Icon icon){
        id = count;
        count++;
        icons.add(icon);
        icon.view.setLayoutParams(ViewFactory.createLayoutParams(0, -1, -1, 0, icon_width, icon_width));
        icon.view.setElevation(30);
        //icon_layout().addView(icon.view);
        icon.add_to_row(this);
    }

    public boolean can_fit(int space, int max){

        if(space + width() > max)
            return false;
        return true;
    }

    public boolean overflowing(int max){
        if(max < width())
            return true;
        return false;
    }

    public int width(){
        int current_width = 0;
        for(int i = 0; i < icons.size(); i++){
            current_width += icons.get(i).width();
        }
        return current_width;
    }
    public ViewGroup icon_layout(){
        return group.layout;
    }

    public void add(Icon icon, int index){
        icons.add(index, icon);
        icon.view.setLayoutParams(ViewFactory.createLayoutParams(0, -1, -1, 0, icon_width, icon_width));
        //icon_layout().addView(icon.view);
        icon.add_to_row(this);
    }

    public Icon get(int index){
        return icons.get(index);
    }

    public float height(){
        int height = 0;
        for(int i = 0; i < icons.size(); i++){
            if(icons.get(i).view.getHeight() > height)
                height = icons.get(i).view.getHeight();
        }
        return height;
    }

    public Group_Manager parent(){
        return group.parent();
    }

    public boolean remove(Icon icon){
        icon.remove_from_row();
        //icon_layout().removeView(icon.view);
        if(icons.contains(icon)){
            icons.remove(icon);
            if(icons.size() == 0)
                group.rows.remove(this);
            return true;
        }

        return false;
    }

    public Icon remove(int index){
        Icon icon = icons.remove(index);
        //icon_layout().removeView(icon.view);
        if(icon != null)
            icon.remove_from_row();
        return icon;
    }

    public int size(){
        return icons.size();
    }
}
