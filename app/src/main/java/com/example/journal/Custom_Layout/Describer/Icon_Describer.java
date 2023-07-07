package com.example.journal.Custom_Layout.Describer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Icon_Describer {
    public Drawable_Describer drawable_describer;
    public Journal_Describer parent;
    public Icon_Describer(Drawable_Describer drawable_describer){
        this.drawable_describer = drawable_describer;

    }

    public int get_id(){
        return parent.icons.indexOf(this);
    }

    public static Icon_Describer make(String drawable_name, String drawable_id){
        return new Icon_Describer(new Drawable_Describer(drawable_name, Integer.parseInt(drawable_id)));
    }

    public static Icon_Describer parse_from_data(String[] data){
        return make(data[0], data[1]);
    }

    public static int num_attributes(){
        return 2;
    }

    public ArrayList<String> data(){
        ArrayList<String> result = new ArrayList<>();
        result.add("icon");
        result.add(drawable_describer.name);
        result.add(""+drawable_describer.id);
        return result;
    }

    public JSONObject get_json(){
        JSONObject jsonObject = new JSONObject();

        try {
            // Add some key-value pairs
            jsonObject.put("type", "icon");
            jsonObject.put("name", "default name");
            jsonObject.put("drawable_name", drawable_describer.name);
            jsonObject.put("drawable_id", drawable_describer.id);

            // Convert JSONObject to String
            String jsonString = jsonObject.toString();

            // Output the string to the console
            System.out.println(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static Icon_Describer parse(JSONObject json) throws JSONException{
        System.out.println("parsing icon json: " + json);
        System.out.println("got: " + json.get("drawable_name").toString()+", " + Integer.parseInt(json.get("drawable_id").toString()));
        Icon_Describer icon = new Icon_Describer(new Drawable_Describer(
                json.get("drawable_name").toString(),
                Integer.parseInt(json.get("drawable_id").toString())));
        return icon;
    }
}
