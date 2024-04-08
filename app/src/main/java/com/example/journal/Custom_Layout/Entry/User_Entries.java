package com.example.journal.Custom_Layout.Entry;

import android.content.Context;

import com.example.journal.Custom_Layout.Describer.Journal_Describer;
import com.example.journal.Custom_Layout.Utility.File_Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class User_Entries {
    static String directory_name = "Journal-Data";
    static String file_name = "Journal-Data/journal-entry";
    private static Journal_Entry get_entry(Journal_Describer journal_describer, Calendar date, Context context){
        boolean printed_error = false;
        String entry_file_name = get_file_name(date, journal_describer);
        System.out.println("getting entry <journal: "+journal_describer.name+">, on date: <"+date_to_string(date)+">, from file: " + entry_file_name);
        String entry_data = File_Utilities.readFile_from_directory(context, entry_file_name);
        JSONObject json;
        try {
            json = new JSONObject(entry_data);
        } catch (JSONException e) {
            json = null;
            System.out.println("could not get json from: " + entry_file_name);
            System.out.println("**********\ndata: " + entry_data+"\n**************");
            printed_error = true;
        }
        Journal_Entry entry = null;
        if(json != null)
            entry = Journal_Entry.parse(json, journal_describer);
        if(entry == null){
            System.out.println("Could not parse entry from: " + entry_file_name);
            printed_error = true;
        }

        return entry;
    }

    public static String get_file_name(Calendar date, Journal_Describer journal){
        String journal_name = journal.name;
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH);
        int day = date.get(Calendar.DAY_OF_MONTH);
        return file_name+"_"+journal_name+"_"+year + "-"+month+"-"+day;
    }

    public static String get_file_name_grouped(Calendar date){
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH);
        //return file_name+"_"+year + "-"+month;
        return file_name+"_"+year;
    }

    public static String date_to_string(Calendar date){
        int day = date.get(Calendar.DAY_OF_MONTH);
        int month = date.get(Calendar.MONTH);
        int year = date.get(Calendar.YEAR);
        return month + ", " + day + ", " + year;
    }

    public static void rewrite_entries_new_journal(Journal_Describer old_journal, Journal_Describer new_journal, Context context, HashMap<Integer, Integer> icon_map){
        File directory_file = new File(context.getFilesDir(), directory_name);

        File[] files_in_directory = File_Utilities.files_in_directory(directory_file, context);
        String storage_dir_string = File_Utilities.get_directory_string(context);
        System.out.println("listing files in directory: " + storage_dir_string);
        for(int i = 0; i < files_in_directory.length; i++){
            System.out.println("\t" + files_in_directory[i].getPath());
        }
        for(int i = 0; i < files_in_directory.length; i++){
            System.out.println("getting entries from file: " + files_in_directory[i].getPath());
            ArrayList<Journal_Entry> old_entries = get_group_file_entries(old_journal, files_in_directory[i].getPath(), context);
            ArrayList<Journal_Entry> new_entries = new ArrayList<>();
            for(int j = 0; j < old_entries.size(); j++){
                Journal_Entry old_entry = old_entries.get(j);
                Journal_Entry new_entry = rewrite_entry(old_entry, new_journal, icon_map);
                new_entries.add(new_entry);


            }
            String file_in_directory_name = files_in_directory[i].getName();
            System.out.println("writing into: " + file_in_directory_name + ", from folder: " + directory_file.getPath());
            JSONArray json_array = get_json_array(new_entries);
            File_Utilities.writeFile(json_array.toString(), files_in_directory[i].getPath());
        }
    }

    public static Journal_Entry rewrite_entry(Journal_Entry old_entry, Journal_Describer new_journal, HashMap<Integer, Integer> icon_map){
        System.out.println("rewriting entry: " + old_entry.month + ", " + old_entry.day);
        Calendar date = Calendar.getInstance();
        date.set(old_entry.year, old_entry.month, old_entry.day);
        Journal_Entry new_entry = new Journal_Entry(new_journal, date);
        ArrayList<Icon_Entry> old_entries = old_entry.icon_entries;
        for(int new_icon_index = 0; new_icon_index < new_journal.icon_size(); new_icon_index++){

            if(icon_map.containsKey(new_icon_index)){
                int old_id = icon_map.get(new_icon_index);
                Icon_Entry old_icon_entry = null;
                for(int old = 0; old < old_entries.size(); old++){
                    if(old_entries.get(old).icon_id == old_id){
                        old_icon_entry = old_entries.get(old);
                        break;
                    }
                }
                if(old_icon_entry == null){
                    continue;
                }

                if(old_icon_entry.on){
                    new_entry.add_icon_entry(new_icon_index, old_icon_entry.on, old_icon_entry.text);
                    System.out.println("\tmoving icon entry from: " + old_icon_entry.icon_id + ", to: " + new_icon_index);
                }
            }
        }
        ArrayList<Icon_Entry> new_entries = new_entry.icon_entries;
        System.out.println("printing new entries");
        for(int i = 0; i < new_entries.size(); i++){
            Icon_Entry entry = new_entries.get(i);
            System.out.println("\t" + entry.icon_id + ", on: " + entry.on);
        }

        return new_entry;
    }

    public static ArrayList<Journal_Entry> get_group_file_entries(Journal_Describer journal_describer, Calendar date, Context context){
        String entry_file_name = get_file_name_grouped(date);
        System.out.println("getting entry <journal: "+journal_describer.name+">, on date: <"+date_to_string(date)+">, from file: " + entry_file_name);
        String data = File_Utilities.readFile_from_directory(context, entry_file_name);
        JSONArray array = null;
        try {
            array = new JSONArray(data);
        } catch (JSONException e) {
            System.out.println("Could not get entry ("+date_to_string(date)+"): file has no data");
            return null;
        }
        return parse_array(array, journal_describer);
    }

    public static ArrayList<Journal_Entry> get_group_file_entries(Journal_Describer journal_describer, String file_string, Context context){
        String data = File_Utilities.readFile(file_string);
        if(data.equals("Could not get entry (\"+file_string+\"): empty string")){
            return null;
        }
        JSONArray array = null;
        try {
            array = new JSONArray(data);
        } catch (JSONException e) {
            System.out.println("Could not get entry ("+file_string+"): cannot parse into json array");
            return null;
        }
        return parse_array(array, journal_describer);
    }


    public static Journal_Entry read_group_file(Journal_Describer journal_describer, Calendar date, Context context){
        ArrayList<Journal_Entry> entries = get_group_file_entries(journal_describer, date, context);
        int day = date.get(Calendar.DAY_OF_MONTH);
        int month = date.get(Calendar.MONTH);
        if(entries == null)
            return null;
        for(int i = 0; i < entries.size(); i++){
            if(entries.get(i).day == day && entries.get(i).month == month){
                return entries.get(i);
            }
        }

        return null;
    }

    public static void write_entry_grouped(Journal_Entry entry, Calendar date, Context context, Journal_Describer journal){
        System.out.println("writing entry: " + entry.day + " into group file");
        File directory = new File(context.getFilesDir(), directory_name);
        if(!directory.exists()){
            directory.mkdir();
        }
        String entry_file_name = get_file_name_grouped(date);
        String data = File_Utilities.readFile_from_directory(context, entry_file_name);
        if(data.equals("")){
            ArrayList<Journal_Entry> array = new ArrayList<>();
            array.add(entry);
            JSONArray one_entry_json = get_json_array(array);
            File_Utilities.writeFile_from_directory_folder(context, one_entry_json.toString(), entry_file_name);
            return;
        }
        JSONArray json = null;
        try {
            json = new JSONArray(data);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        int add_day = entry.day;
        ArrayList<Journal_Entry> entries = parse_array(json, journal);
        //System.out.println("going through entries days: ");
        for(int i = 0; i < entries.size(); i++){
            //System.out.println("day: " + entries.get(i).day);
            if(entries.get(i).day == entry.day && entries.get(i).month == entry.month){
                entries.remove(i);
                break;
            }
        }
//        entries.sort(new Comparator<Journal_Entry>() {
//            @Override
//            public int compare(Journal_Entry o1, Journal_Entry o2) {
//                int month_diff = o1.
//                return 0;
//            }
//        });
        //System.out.println("days after parsing: ");
        //for(int i = 0; i < entries.size(); i++)
            //System.out.println("day: " + entries.get(i).day);
        entries.add(entry);
        JSONArray json_array = get_json_array(entries);
        File_Utilities.writeFile_from_directory_folder(context, json_array.toString(), entry_file_name);
    }

    public static JSONArray get_json_array(ArrayList<Journal_Entry> entries){
        JSONArray json = new JSONArray();
        for(int i = 0; i < entries.size(); i++){
            json.put(entries.get(i).get_json());
        }
        return json;
    }

    public static ArrayList<Journal_Entry> parse_array(JSONArray json, Journal_Describer journal){
        ArrayList<Journal_Entry> entries = new ArrayList<>();
        for(int i = 0; i < json.length(); i++){
            JSONObject entry_json = null;
            try {
                entry_json = json.getJSONObject(i);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            Journal_Entry entry = Journal_Entry.parse(entry_json, journal);
            entries.add(entry);
        }
        return entries;
    }


    private static void write_entry(Journal_Describer journal, Journal_Entry entry, Calendar date, Context context){

        String data = entry.get_json().toString();
        String entry_file_name = get_file_name(date, journal);
        System.out.println("writing entry to: " + entry_file_name);
        System.out.println("writing data:\n" + data);
        File_Utilities.writeFile_from_directory_folder(context, data, entry_file_name);
    }
}
