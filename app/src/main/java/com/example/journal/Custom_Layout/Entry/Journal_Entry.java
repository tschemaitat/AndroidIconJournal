package com.example.journal.Custom_Layout.Entry;

import com.example.journal.Custom_Layout.Describer.Group_Describer;
import com.example.journal.Custom_Layout.Describer.Icon_Describer;
import com.example.journal.Custom_Layout.Describer.Journal_Describer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Journal_Entry {
    Journal_Describer journal;
    ArrayList<Icon_Entry> icon_entries = new ArrayList<>();
    public Journal_Entry(Journal_Describer journal){
        this.journal = journal;
    }

    public void add_icon_entry(int icon_id, boolean on, String text){
        icon_entries.add(new Icon_Entry(icon_id, on, text));
    }
    public void add_icon_entry(Icon_Entry icon_entry){
        icon_entries.add(icon_entry);
    }

    public Icon_Entry get_entry(int id){
        return icon_entries.get(id);
    }

    public int size(){
        return icon_entries.size();
    }

    public JSONObject get_json(){
        JSONObject json = new JSONObject();
        JSONArray json_entries = new JSONArray();
        try{
            json.put("date", "7/4/23");
            json.put("journal_name", "default journal name");
            for(int i = 0; i < icon_entries.size(); i++){
                json_entries.put(icon_entries.get(i).get_json());
            }
            json.put("entries",json_entries);
        }catch(JSONException e){
            e.printStackTrace();
        }
        return json;
    }

    public static Journal_Entry parse(JSONObject json, Journal_Describer journal_describer){
        Journal_Entry journal_entry = new Journal_Entry(journal_describer);

        try {
            JSONArray entries = json.getJSONArray("entries");
            for(int i = 0; i < entries.length(); i++){
                JSONObject icon_json = entries.getJSONObject(i);
                Icon_Entry icon_entry = Icon_Entry.parse(icon_json);
                journal_entry.add_icon_entry(icon_entry);
            }


        } catch (JSONException e) {
            return null;
        }
        return journal_entry;
    }

    public String toString(){
        String result = "";
        for(int i = 0; i < icon_entries.size(); i++){
            if(i % 4 == 0 && i != 0)
                result += "\n";
            result += "\t"+icon_entries.get(i).on;

        }
        result += "\n";
        return result;
    }
}
