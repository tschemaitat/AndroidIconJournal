package com.example.journal.Custom_Layout;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class Journal_Describer{
    ArrayList<Icon_Describer> icons = new ArrayList<>();
    ArrayList<Group_Describer> groups = new ArrayList<>();

    public Journal_Describer(){

    }

    public void add(Group_Describer group_describer){
        add_group(group_describer.name);
    }

    public void add_group(String name){
        groups.add(new Group_Describer(name));
    }

    public void add(Icon_Describer icon){
        groups.get(groups.size() - 1).add(icon);
        icons.add(icon);
    }

    public void add(int group, int index, Icon_Describer icon){
        groups.get(group).add(index, icon);
        icons.add(index, icon);
    }

    public void remove(int group, int index){
        Icon_Describer icon = groups.get(group).remove(index);
        icons.remove(index);
    }

    public Icon_Describer get(int group, int index){
        return groups.get(group).get(index);
    }

    public int icon_size(){
        return icons.size();
    }

    public ArrayList<String> data(){
        ArrayList<String> result = new ArrayList<>();
        result.add("journal");
        for(int i = 0; i < groups.size(); i++){
            result.addAll(groups.get(i).data());
        }
        return result;
    }

    public String data_string(){
        ArrayList<String> array = data();
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < array.size(); i++){
            builder.append(array.get(i)).append("\n");
        }
        return builder.toString();
    }
}
