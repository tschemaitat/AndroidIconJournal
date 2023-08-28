package com.example.journal.Custom_Layout.Icon_Layout;

import android.view.MotionEvent;

public class Handle_Drag {
    Bottom_Sroll_Sheet sheet;
    int initial_height;
    float initial_cursor_y;
    public Handle_Drag(Bottom_Sroll_Sheet sheet){
        this.sheet = sheet;
    }

    public void handle_touch_event(MotionEvent event){
        if(event.getActionMasked() == MotionEvent.ACTION_DOWN){
            System.out.println("got action down, setting up: ");
            setup(event);
        }
        iterate(event);
        if(event.getActionMasked() == MotionEvent.ACTION_UP){
            last(event);
        }
    }

    public void iterate(MotionEvent event){
        int change_y = (int)(event.getRawY() - initial_cursor_y);
        int final_height = initial_height - change_y;
        sheet.set_height(final_height);
        System.out.println("change_y: " + change_y + ", setting heihgt to: " + final_height);
    }

    public void setup(MotionEvent event){
        initial_cursor_y = event.getRawY();
        initial_height = sheet.height;
        System.out.println("initial y: " + initial_cursor_y);
        System.out.println("initial hiehgt: " + initial_height);
    }

    public void last(MotionEvent event){

    }
}
