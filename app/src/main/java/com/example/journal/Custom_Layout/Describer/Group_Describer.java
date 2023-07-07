package com.example.journal.Custom_Layout.Describer;

import androidx.constraintlayout.widget.Group;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Group_Describer {
    ArrayList<Icon_Describer> icons = new ArrayList<>();
    public String name;
    public Group_Describer(String name){
        this.name = name;
    }

    public static Group_Describer parse_data(String[] data){
        return new Group_Describer(data[0]);
    }

    public static int num_attributes(){
        return 1;
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

    public JSONObject get_json(){
        JSONObject jsonObject = new JSONObject();
        try {
            // Add some key-value pairs
            jsonObject.put("type", "group");
            jsonObject.put("name", "default name");

            JSONArray children = new JSONArray();
            for(int i = 0; i < icons.size(); i++){
                children.put(icons.get(i).get_json());
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

    public static Group_Describer parse(JSONObject json) throws JSONException{
        Group_Describer group = new Group_Describer(
                json.get("name").toString());
        return group;
    }
}
