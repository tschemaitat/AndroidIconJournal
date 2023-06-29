package com.example.journal.Custom_Layout;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.example.journal.Image_Processing;

import java.util.ArrayList;

public class User_Information {

    public static Journal_Describer get_journal(Context context){
        Journal_Describer journal = new Journal_Describer();
        ArrayList<Drawable> drawables = Image_Processing.drawables(context);
        journal.add_group("first group");
        for(int i = 0; i < drawables.size(); i++)
            journal.add(new Icon_Describer(drawables.get(i)));
        return journal;
    }
}
