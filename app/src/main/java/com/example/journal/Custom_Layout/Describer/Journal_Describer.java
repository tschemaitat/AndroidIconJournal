package com.example.journal.Custom_Layout.Describer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Journal_Describer{
    ArrayList<Icon_Describer> icons = new ArrayList<>();
    public ArrayList<Group_Describer> groups = new ArrayList<>();

    public Journal_Describer(){
    }

    public int get_num_icons(){
        return icons.size();
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
        icon.parent = this;
    }

    public void add(int group, int index, Icon_Describer icon){
        groups.get(group).add(index, icon);
        icons.add(index, icon);
        icon.parent = this;
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

    public JSONObject get_json(){
        JSONObject jsonObject = new JSONObject();
        try {
            // Add some key-value pairs
            jsonObject.put("type", "journal");
            jsonObject.put("name", "default name");

            JSONArray children = new JSONArray();
            for(int i = 0; i < groups.size(); i++){
                children.put(groups.get(i).get_json());
            }

            jsonObject.put("children", children);


            // Convert JSONObject to String
            String jsonString = jsonObject.toString();

            // Output the string to the console
            System.out.println(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public String toString(){
        String result = "";
        result += "journal heirarchy";
        for(int i = 0; i < groups.size(); i++){
            Group_Describer group = groups.get(i);
            result +=("\tgroup: " + group.name + "\n");
            for(int j = 0; j < group.size(); j++){
                Icon_Describer icon = group.get(j);
                result += ("\ticon: " + icon.get_id() + ", "+icon.drawable_describer.id + "\n");
            }
        }
        return result;
    }
}
