package com.example.journal.Custom_Layout;

import java.util.ArrayList;

public class Icon_Debugger {
    ArrayList<Icon> icons = new ArrayList<>();
    Compute_Timer wiggles = new Compute_Timer("wiggle");
    Compute_Timer animations = new Compute_Timer("animation");
    public Icon_Debugger(){

    }

    public void log_icon(Icon icon){
        icons.add(icon);
    }

    public void log_wiggle(long start, long end){
        wiggles.log_time(start, end);
    }

    public void log_animation(long start, long end){
        animations.log_time(start, end);
    }

}
