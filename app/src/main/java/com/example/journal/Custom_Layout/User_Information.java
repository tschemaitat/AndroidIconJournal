package com.example.journal.Custom_Layout;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.example.journal.Image_Processing;

import java.util.ArrayList;

public class User_Information {
    private static Journal_Describer journal = null;

    public static Journal_Describer get_journal(Context context){
        if(journal != null){
            return journal;
        }
        String file_data = File_Utilities.readFile(context);
        System.out.println("read data:\n" + file_data);

        Journal_Describer journal = parse_from_data(file_data);
        if(journal == null){
            journal = new Journal_Describer();


            journal.add_group("first group");
            for(int i = 0; i < Drawable_Manager.size(); i++)
                journal.add(new Icon_Describer(Drawable_Manager.get_drawable(i).get_describer()));
        }

        return journal;
    }

    public static Journal_Describer parse_from_data(String data){

        String[] lines = data.split("\n");
        Journal_Describer journal_describer = new Journal_Describer();
        for(int i = 0; i < lines.length; i++){
            String current_line = lines[i];
            System.out.println("current line: " + current_line);
            switch(current_line){
                case "group":
                    i++;
                    String name = lines[i];
                    journal_describer.add(Group_Describer.parse_data(name));
                    //System.out.println("added group: " + name);
                    break;
                case "icon":
                    i++;
                    String name_icon = lines[i];
                    i++;
                    String id_icon = lines[i];
                    journal_describer.add(Icon_Describer.parse_data(name_icon, id_icon));
                    //System.out.println
                    break;
                case "journal":
                    break;
            }
        }
        return journal_describer;
    }


    public static void set_journal(Journal_Describer new_journal){
        journal = new_journal;
    }
}
