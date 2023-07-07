package com.example.journal.Custom_Layout.Entry;

import android.content.Context;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.journal.Custom_Layout.Describer.Journal_Describer;
import com.example.journal.Custom_Layout.Utility.File_Utilities;

import org.json.JSONException;
import org.json.JSONObject;

public class User_Entries {
    static String file_name = "Journal_Data/journal_entry.txt";
    public static Journal_Entry get_entry(Journal_Describer journal_describer, Context context){
        String entry_data = File_Utilities.readFile(context, file_name);
        JSONObject json;
        try {
            json = new JSONObject(entry_data);
        } catch (JSONException e) {
            json = null;
        }
        Journal_Entry entry = null;
        if(json != null)
            entry = Journal_Entry.parse(json, journal_describer);

        return entry;
    }

    public static void write_entry(Journal_Entry entry, Context context){
        String data = entry.get_json().toString();
        File_Utilities.writeFile(context, data, file_name);
    }
}
