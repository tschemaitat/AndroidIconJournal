package com.example.journal.Custom_Layout;

import android.view.MotionEvent;

import java.util.ArrayList;

public class Drag_Manager {
    ArrayList<Drag> current_drags = new ArrayList<>();

    public Drag_Manager(){

    }

    public void log_event(Drag drag, MotionEvent event){
        if(!current_drags.contains(drag)){
            current_drags.add(drag);
        }
        release_finished_drags();
    }

    public void log_drag(Drag drag){
        if(!current_drags.contains(drag)){
            current_drags.add(drag);
        }
        release_finished_drags();
    }



    public void release_finished_drags(){
        for(int i = current_drags.size() - 1; i >= 0; i--){
            if(current_drags.get(i).finished == true){
                current_drags.remove(i);
            }
        }
        if(current_drags.size() > 1){
            System.out.println("multiple drags: ");
            for(int i = current_drags.size() - 1; i >= 0; i--){
                Drag drag = current_drags.get(i);
                System.out.println("("+drag.id+")times down event: " + drag.received_down_event + ", up event: " + drag.received_up_event + ", finished? " + drag.finished);
            }
            System.out.println("ending past drag manually");

            current_drags.get(0).end_drag(current_drags.get(0).icon.view);
            current_drags.get(0).groups.scroll.setScrollingEnabled(false);
            current_drags.remove(0);
        }
    }

    public void print_drag_statement(){
        release_finished_drags();
    }
}
