package com.example.journal.Custom_Layout;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class Icon_Describer {
    Drawable_Describer drawable_describer;
    public Icon_Describer(Drawable_Describer drawable_describer){
        this.drawable_describer = drawable_describer;
    }

    public static Icon_Describer parse_data(String name, String id){
        return new Icon_Describer(new Drawable_Describer(name, Integer.parseInt(id)));
    }

    public ArrayList<String> data(){
        ArrayList<String> result = new ArrayList<>();
        result.add("icon");
        result.add(drawable_describer.name);
        result.add(""+drawable_describer.id);
        return result;
    }
}
