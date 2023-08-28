package com.example.journal.Custom_Layout.Icon_Layout;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.journal.Custom_Layout.Data_Structure.IconLocationStruct;
import com.example.journal.Custom_Layout.Data_Structure.Rectanglef;
import com.example.journal.MyAsyncTask;

import java.util.ArrayList;

public class Drag {

    private boolean time_to_drag = false;
    boolean invalid_drag = false;
    boolean vibrated = false;

    boolean popped = false;
    boolean has_popped_once = false;
    boolean outside = false;
    boolean moved = false;
    boolean finished = false;
    boolean saw_finished = false;
    boolean printed_out_scroll_boxes = false;

    int received_down_event = 0;
    int received_up_event = 0;

    IconLocationStruct starting_position = null;
    IconLocationStruct position_from_last_pop = null;

    Icon icon;
    Group_Manager groups;
    ViewGroup layout;
    MotionEvent event = null;
    Drag_Manager drag_manager;
    int id;
    static int count = 0;
    public Drag(Icon icon, Group_Manager groups, Drag_Manager drag_manager, ViewGroup layout, MotionEvent event){
        System.out.println("<drag> new drag");
        drag_manager.log_event(this, event);
        id = count;
        count++;
        this.drag_manager = drag_manager;
        this.event = event;
        this.icon = icon;
        this.groups = groups;
        this.layout = layout;
        //BackEndClock.get_clock().methods.add(this::iterate);
        super_iterate();
    }
    boolean once = false;
    ArrayList<Long> times = new ArrayList<>();
    ArrayList<Long> iterate_compute_times = new ArrayList<>();
    public synchronized void super_iterate(){

        boolean continuing;
        long start_time = System.currentTimeMillis();
        continuing = iterate();
        long end_time = System.currentTimeMillis();
        if(end_time - start_time > 3){
            //System.out.println("LONG ITERATE TIME: " + (end_time - start_time));
        }
        iterate_compute_times.add(Long.valueOf(end_time - start_time));
//        if(once){
//            continuing = iterate();
//            once = false;
//        }
//        else {
//            continuing = true;
//            once = true;
//        }

        //System.out.println("continue iterating? " + continuing);
        if(saw_finished){

            //System.out.println("loop even after we saw finished");
            throw new RuntimeException();
        }

        if(continuing && !finished){
            if(finished){
                throw new RuntimeException();
            }
            //System.out.println("setting post");
            icon.view.postOnAnimation(new Runnable() {
                @Override
                public void run() {
                    //System.out.println("icon post call");
                    super_iterate();
                }
            });
        }else{
            System.out.println("<drag> closing");
            saw_finished = true;
        }
    }

    public void log_time(){
        times.add(Long.valueOf(System.currentTimeMillis()));
    }

    public long average_time(){
        long total = 0;
        total = times.get(times.size() - 1) - times.get(0);
        total = total / times.size();
        return total;
    }

    public long average_compute_time(){
        long total = 0;
        for(int i = 0; i < iterate_compute_times.size(); i++){
            total += iterate_compute_times.get(i);
        }
        total = total / times.size();
        return total;
    }
    long iterate_count = 0;
    public synchronized boolean iterate(){
        set_time_to_drag();
        iterate_count++;
        if(iterate_count%60 == 0)
            //System.out.println("("+id+") iterate count: " + (iterate_count/60));

        if(finished){
            //System.out.println("returning false because finished is true");
            return false;
        }

        //System.out.println("iterating");
        //System.out.println("<consume_event> time to drag? " + time_to_drag);
        ViewGroup view = icon.view;
        view.getParent().bringChildToFront(view);
        if(!view.isClickable()) {
            //System.out.println("view is not clickable");
            return false;

        }
        log_time();

        if(invalid_drag && event.getActionMasked() != MotionEvent.ACTION_UP)
            return true;

        int[] view_raw = new int[2];
        view.getLocationOnScreen(view_raw);
        int view_rawX = view_raw[0];
        int view_rawY = view_raw[1];

        int x = (int) rawX - view_rawX;
        int y = (int) rawY - view_rawY;
        //System.out.println("mouse: " + x + ", " + y);

        //System.out.println("raw mouse: " + rawX + ", " + rawY);

        //System.out.println("view raw: " + view_rawX + ", " + view_rawY);
        layout.getLocationOnScreen(view_raw);
        int layout_rawX = view_raw[0];
        int layout_rawY = view_raw[1];
        groups.scroll.getLocationOnScreen(view_raw);
        int scroll_rawX = view_raw[0];
        int scroll_rawY = view_raw[1];
        //System.out.println("layout raw: " + layout_rawX + ", " + layout_rawY);

        float relative_from_parent_x = view_rawX - layout_rawX + x;
        float relative_from_parent_y = view_rawY - layout_rawY + y;

        float relative_from_scroll_x = view_rawX - scroll_rawX + x;
        float relative_from_scroll_y = view_rawY - scroll_rawY + y;
        //System.out.println("cursor relative to parent: "+ relative_from_parent_x + ", " + relative_from_parent_y);
        if((x > icon.width() || x < 0) || (y > icon.width() || y < 0)){
            //if(!outside)
                //System.out.println("outside now");
            outside = true;
        }

        if(time_to_drag && !vibrated && !invalid_drag){
            //System.out.println("vibrating");
            //System.out.println("time to drag? " + time_to_drag);
            groups.scroll.setScrollingEnabled(false);
            icon.vibrate(icon.context);
            vibrated = true;
        }

        if(outside && !time_to_drag && !invalid_drag){
            //System.out.println("outside before time to drag: invalid drag");
            invalid_drag = true;
        }
        //System.out.println(time_to_drag + ", " + vibrated + ", " + invalid_drag);


        if(time_to_drag && !invalid_drag && !popped){
            //System.out.println("setting wiggle");
            icon.set_wiggle_position(((int)relative_from_parent_x - (int)icon.position_x)-icon.width()/2, ((int)relative_from_parent_y - (int)icon.position_y) - icon.width()/2);
            //icon.set_wiggle_position((rawX - (int)icon.raw_true_position_x())-icon.width()/2, (rawY -(int)icon.raw_true_position_y()) - icon.width()/2);
        }
        //view is ready to be moved one it was vibrated
        if(vibrated && !popped){

            float x_distance = icon.get_cursor_distance_from_center(x);
            float y_distance = icon.get_cursor_distance_from_center(y);
            //System.out.println("x, y: " + x_distance +", " + y_distance);
            if(x_distance > 0.35 || y_distance > 0.35){

                //System.out.println("popped");
                //System.out.println("\tmouse: ("+x+", " + y + ") icon: (" + icon.layout_position_x + ", " + icon.layout_position_y+") mouse abs: (" + relative_from_parent_x + ", " + relative_from_parent_y+")");
                System.out.println();
                popped = true;

                position_from_last_pop = icon.get_position();
                if(!has_popped_once){
                    starting_position = position_from_last_pop;
                }
                icon.get_row().parent().remove_icon(icon);
                has_popped_once = true;
                moved = false;
            }
        }

        if(popped){
            when_popped(relative_from_parent_x, relative_from_parent_y, relative_from_scroll_x, relative_from_scroll_y);
        }

        //System.out.println("x: " + x +", y: " + y);
        //System.out.println(event.getActionMasked());

        return true;
    }

    private synchronized boolean process_event_tag(){
        View view = icon.view;
        switch (event.getActionMasked()) {
            //when the mouse clicks
            case MotionEvent.ACTION_DOWN:
                received_down_event++;
                if(finished){
                    throw new RuntimeException();
                }
                start_drag_setup(view);
                break;
            //mouse lets go
            case MotionEvent.ACTION_UP:
                received_up_event++;
                end_drag(view);
                finished = true;
                return false;
            case MotionEvent.ACTION_OUTSIDE:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            default:
                break;
        }
        return true;
    }

    long last_time = 0;
    int rawX;
    int rawY;
    public synchronized boolean consume_event(MotionEvent event){
//        if(last_time == 0){
//            last_time = System.currentTimeMillis();
//        }else{
//            long time = System.currentTimeMillis();
//            System.out.println("elapsed time: " + (time - last_time));
//            last_time = time;
//        }

        this.event = event;
        rawX = (int) event.getRawX();
        rawY = (int) event.getRawY();
        process_event_tag();
        drag_manager.log_event(this, event);
        return true;
    }

    public void when_popped(float relative_from_parent_x, float relative_from_parent_y, float relative_from_scroll_x, float relative_from_scroll_y){
        //System.out.println("teleporting: " + relative_from_parent_x + ", " + relative_from_parent_y);
        //view.setX(relative_from_parent_x - icon.width()/2);
        //view.setY(relative_from_parent_y - icon.width()/2);

        //set needs to be done by coordinates relative to its parent. the width is subtracted to center the icon onto the cursor
        //icon is dragged onto cursor while it is popped
        icon.set(relative_from_parent_x - icon.width()/2, relative_from_parent_y - icon.width()/2);


        //starting code for scrolling up or down
        boolean did_scroll_action = scroll_logic(relative_from_scroll_x, relative_from_scroll_y);
        //do not try to move icon into a spot if we are currently scrolling
        if(did_scroll_action)
            return;

        //set_print_delay_task();
        ArrayList<Adding_Box> boxes = groups.adding_boxes;
        if(boxes == null)
            return;
        for(int i = 0; i < boxes.size(); i++){
            if(boxes.get(i).bounding_box.inside(relative_from_parent_x, relative_from_parent_y)){
//                    if(boxes.get(i).location.equals(position_from_last_pop)){
//                        if(!printed_out_ignoring_bounding_box)
//                            System.out.println("ignoring adding box that put it in the same place");
//                        printed_out_ignoring_bounding_box = true;
//                        break region_popped;
//                    }
                move_view_into_box(boxes.get(i));
                return;
            }
        }
    }
    public boolean scroll_logic(float relative_from_scroll_x, float relative_from_scroll_y){
        int amount_scrolled = groups.scroll.getScrollY();
        //System.out.println("amount scrolled: " + amount_scrolled);
        int top_max_scroll_amount = (layout.getHeight() - groups.scroll.getHeight());
        //System.out.println("top max scroll amount: " + top_max_scroll_amount);
        //if we are scrolled to the top or bot, do not do logic about scrolling.


        float height = groups.scroll.getHeight();
        float top_move_region = height/4;
        float bot_move_region = height - top_move_region;
        Rectanglef top_region = new Rectanglef(0, top_move_region, 0, layout.getWidth());
        Rectanglef bot_region = new Rectanglef(bot_move_region, height, 0, layout.getWidth());
        if(!printed_out_scroll_boxes){
            //System.out.println("top box: " + top_region);
            //System.out.println("bot box: " + bot_region);
            printed_out_scroll_boxes = true;
        }
        int max_scroll = 50;
        int max_multiply = (int)top_move_region;
        //System.out.println("max multiply distance: " + max_multiply);
        //System.out.println("y: " + relative_from_scroll_y);
        //System.out.println("y borders: " + 0 + ", " + top_move_region + ", " + bot_move_region + ", " + height);
        //if(print_delay)
            //System.out.println("cursor relative to boxes: " + relative_from_scroll_x + ", " + relative_from_scroll_y);
        if(top_region.inside(relative_from_scroll_x, relative_from_scroll_y)){
            //if we are at the top and have no more space to scroll, return that we did nothing
            if(amount_scrolled < 1){
                return false;
            }

            float factor = (max_multiply - relative_from_scroll_y)/max_multiply;
            //System.out.println("factor: " + factor);
            factor = (float)Math.pow(factor, 2);
            groups.scroll.scroll_up(max_scroll*factor);
            //System.out.println("scrolling up: " + max_scroll*factor);
            return true;
        }
        if(bot_region.inside(relative_from_scroll_x, relative_from_scroll_y)){
            //if we are at the bottom and have no where else to scroll, return that we did nothing
            if(amount_scrolled > top_max_scroll_amount - 1){
                return false;
            }
            float factor = (relative_from_scroll_y - bot_move_region)/max_multiply;
            //System.out.println("factor: " + factor);
            factor = (float)Math.pow(factor, 2);
            groups.scroll.scroll_down(max_scroll*factor);
            //System.out.println("scrolling down" + max_scroll*factor);
            return true;
        }


        return false;
    }
    long drag_start;
    public void set_drag_time_async_task(){

        drag_start = System.currentTimeMillis();
        System.out.println("<drag> starting time: " + drag_start);
//        MyAsyncTask task = new MyAsyncTask("drag time", new Runnable() {
//            @Override
//            public void run() {
//
//                synchronized (this){
////                    try {
////                        this.wait(700);
////                    } catch (InterruptedException e) {
////                        throw new RuntimeException(e);
////                    }
//                }
//                set_time_to_drag();
//            }
//        });
//        task.execute();
    }
    public void set_time_to_drag(){
        long current_time = System.currentTimeMillis();
        if(drag_start == 0){
            drag_start = System.currentTimeMillis();
        }
        if(current_time - drag_start > 700){
            System.out.println("setting time to drag true");
            time_to_drag = true;
        }
    }
    public void start_drag_setup(View view){
        //System.out.println("~~~~~~~~~~~~~~~~~~~~~\n");
        //System.out.println("processing start drag tag");
        outside = false;
        //System.out.println("setting drag to false");
        time_to_drag = false;
        invalid_drag = false;
        vibrated = false;
        popped = false;
        has_popped_once = false;
        moved = false;

        set_drag_time_async_task();
        //System.out.println("true position: " + icon.position_x + ", " + icon.position_y);
        //System.out.println("view position: " + view.getX() + ", " + view.getY());
        //dragGhost.getLayoutParams().width = x;
        //dragGhost.getLayoutParams().height = y;
        //dragGhost.setVisibility(View.VISIBLE);
        //some code....
    }
    public void end_drag(View view){
        finished = true;
        //System.out.println("action up event tag");

        //System.out.println("average iterate tick time: " + average_time());
        //System.out.println("compute time: " + average_compute_time());
        groups.scroll.setScrollingEnabled(true);
        if(popped){
            //System.out.println("adding icon to previous position");
            groups.add_icon(starting_position, icon);
        }
        icon.animate_view(view.getX(), view.getY(), icon.layout_position_x, icon.layout_position_y);
        if(outside == false)
            icon.view.performClick();
        else{
            //System.out.println("action is up and is outside performing icon transfer");
        }

        //System.out.println("\n~~~~~~~~~~~~~~~~~~~~~");

    }
    public void move_view_into_box(Adding_Box adding_box){
        //System.out.println("moving icon into an adding_box");
        ArrayList<Adding_Box> adding_boxes = groups.adding_boxes;
        //System.out.println("all boxes: ");
        for(int i = 0; i < adding_boxes.size(); i++){
            //System.out.println("\t"+adding_boxes.get(i));
        }
        //System.out.println("moving view into box: the box moving into: " + adding_box);
        //System.out.println("moving view into box");
        popped = false;
        moved = true;
        groups.add_icon(adding_box.location, icon);
    }
}
