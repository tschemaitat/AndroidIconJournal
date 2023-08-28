package com.example.journal.Custom_Layout.Describer;

import android.content.Context;

import com.example.journal.Custom_Layout.Utility.Drawable_Manager;
import com.example.journal.Custom_Layout.Utility.File_Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class User_Journals {
    public static String folder = "Journal_Data";
    public static String file_string = "Journal_Data/Journal_1.txt";

    private static Journal_Describer journal = null;

    public static void set_journal_normal(){
        Drawable_Describer drawable = Drawable_Manager.get_drawable(0).get_describer();
        journal = new Journal_Describer();
        journal.add_group("group uno");
        journal.add(new Icon_Describer("my title", drawable));
    }

    public static Journal_Describer get_journal(Context context){
        if(journal != null){
            return journal;
        }
        String file_data = File_Utilities.readFile_from_directory(context, file_string);
        //System.out.println("read data:\n***************\n" + file_data + "\n****************\n");
        System.out.println("getting journal describer from: " + file_string);
        Journal_Describer journal = parse_journal_json(file_data);
        //System.out.println("read journal: \n" + journal);
        if(journal == null){
            journal = new Journal_Describer();


            journal.add_group("first group");
            for(int i = 0; i < Drawable_Manager.size(); i++)
                journal.add(new Icon_Describer("my title", Drawable_Manager.get_drawable(i).get_describer()));
        }

        return journal;
    }

    public static Journal_Describer parse_journal_text(String data){

        String[] lines = data.split("\n");
        Journal_Describer journal_describer = new Journal_Describer();
        for(int i = 0; i < lines.length; i++){
            String current_line = lines[i];
            System.out.println("current line: " + current_line);
            switch(current_line){
                case "group":
                    String[] group_data = new String[Group_Describer.num_attributes()];

                    for(int parse = 0; parse < group_data.length; parse++){
                        i++;
                        group_data[parse] = lines[i];
                    }
                    journal_describer.add(Group_Describer.parse_data(group_data));
                    break;
                case "icon":
                    String[] icon_data = new String[Icon_Describer.num_attributes()];

                    for(int parse = 0; parse < icon_data.length; parse++){
                        i++;
                        icon_data[parse] = lines[i];
                    }
                    journal_describer.add(Icon_Describer.parse_from_data(icon_data));
                    break;
                case "journal":
                    break;
            }
        }
        return journal_describer;
    }

    public static Journal_Describer parse_journal_json(String data){
        if(data ==  ""){
            System.out.println("could not parse <journal describer> from empty string");
            return null;
        }
        Journal_Describer journal = null;
        try {
            JSONObject json = new JSONObject(data);
            journal = parse(json);

        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Could not parse journal describer");
            //throw new RuntimeException(e);
        }
        return journal;
    }

    public static Journal_Describer parse(JSONObject json)throws JSONException{
        //System.out.println(json);
        //System.out.println("getting journal from json");
        Journal_Describer journal = new Journal_Describer();
        JSONArray groups = json.getJSONArray("children");
        for(int i = 0; i < groups.length(); i++){
            //System.out.println("getting group from json: " + i);
            JSONObject group_json = groups.getJSONObject(i);
            JSONArray icons = group_json.getJSONArray("children");
            Group_Describer group = Group_Describer.parse(group_json);
            journal.add(group);
            for(int j = 0; j < icons.length(); j++){
                //System.out.println("getting icon from json: " + j);
                JSONObject icon_json = icons.getJSONObject(j);
                Icon_Describer icon = Icon_Describer.parse(icon_json);
                journal.add(icon);
            }
        }
        return journal;
    }

    public static Journal_Describer check_data_parse(Journal_Describer new_journal){
        return parse_journal_json(new_journal.get_json().toString());
    }

    public static void write_journal(Journal_Describer journal, Context context){

        File_Utilities.writeFile_from_directory_folder(context, journal.get_json().toString(), file_string);
    }


    public static void set_journal(Journal_Describer new_journal){
        journal = new_journal;
    }
}
