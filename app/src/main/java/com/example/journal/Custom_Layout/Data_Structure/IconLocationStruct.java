package com.example.journal.Custom_Layout.Data_Structure;

import com.example.journal.Custom_Layout.Icon_Layout.Group_Layout;

public class IconLocationStruct {
    public Group_Layout group;
    public int icon_position;
    public IconLocationStruct(Group_Layout group, int icon_position){
        this.group = group;
        this.icon_position = icon_position;
    }

    public String toString(){
        return "group: " + group.id + ", pos: " + icon_position;
    }

    public boolean equals(IconLocationStruct loc){
        if(group == loc.group && icon_position == loc.icon_position)
            return true;
        return false;
    }
}
