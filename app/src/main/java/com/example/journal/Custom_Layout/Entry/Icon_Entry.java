package com.example.journal.Custom_Layout.Entry;

import com.example.journal.Custom_Layout.Describer.Group_Describer;
import com.example.journal.Custom_Layout.Describer.Icon_Describer;

import org.json.JSONException;
import org.json.JSONObject;

public class Icon_Entry {
    public int icon_id;
    public boolean on;
    public String text;
    public Icon_Entry(int icon_id, boolean on, String text){
        this.icon_id = icon_id;
        this.on = on;
        this.text = text;
    }

    public static Icon_Entry parse(JSONObject json) throws JSONException {
        return new Icon_Entry(json.getInt("icon_id"), json.getBoolean("on"), json.getString("text"));
    }

    public JSONObject get_json() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("icon_id", icon_id);
        json.put("on", on);
        json.put("text", text);

        return json;
    }
}
