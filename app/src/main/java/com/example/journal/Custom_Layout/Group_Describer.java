package com.example.journal.Custom_Layout;

import java.util.ArrayList;

public class Group_Describer {
    ArrayList<Icon_Describer> icons = new ArrayList<>();
    String name;
    public Group_Describer(String name){
        this.name = name;
    }

    public static Group_Describer parse_data(String name){
        return new Group_Describer(name);
    }

    public void add(Icon_Describer icon){
        icons.add(icon);
    }

    public void add(int index, Icon_Describer icon){
        icons.add(index, icon);
    }

    public Icon_Describer remove(int index){
        return icons.remove(index);
    }

    public Icon_Describer get(int index){
        return icons.get(index);
    }

    public int size(){
        return icons.size();
    }

    public ArrayList<String> data(){
        ArrayList<String> result = new ArrayList<>();
        result.add("group");
        result.add(name);
        for(int i = 0; i < icons.size(); i++){
            result.addAll(icons.get(i).data());
        }
        return result;
    }
}
