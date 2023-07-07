package com.example.journal.Custom_Layout.Data_Structure;

import android.graphics.drawable.Drawable;

import com.example.journal.Custom_Layout.Describer.Drawable_Describer;

public class Drawable_With_Data {
    public Drawable drawable;
    String name;
    public int id;
    public Drawable_With_Data(Drawable drawable, String name, int id){
        this.drawable = drawable;
        this.name = name;
        this.id = id;
    }

    public Drawable_Describer get_describer(){
        return new Drawable_Describer(name, id);
    }
}
