package com.example.journal.Custom_Layout;

import android.graphics.drawable.Drawable;

public class Drawable_With_Data {
    Drawable drawable;
    String name;
    int id;
    public Drawable_With_Data(Drawable drawable, String name, int id){
        this.drawable = drawable;
        this.name = name;
        this.id = id;
    }

    public Drawable_Describer get_describer(){
        return new Drawable_Describer(name, id);
    }
}
