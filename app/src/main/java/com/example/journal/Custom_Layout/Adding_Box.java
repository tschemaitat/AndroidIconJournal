package com.example.journal.Custom_Layout;

public class Adding_Box {
    Rectanglef bounding_box;
    IconLocationStruct location;
    public Adding_Box(Rectanglef bounding_box, IconLocationStruct location){
        this.bounding_box = bounding_box;
        this.location = location;
    }

    public String toString(){
        return "box: "+bounding_box+", loc: " + location;
    }


}
