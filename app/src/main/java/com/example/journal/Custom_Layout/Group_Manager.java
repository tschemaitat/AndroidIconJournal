package com.example.journal.Custom_Layout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.journal.Image_Processing;
import com.example.journal.R;
import com.example.journal.ViewFactory;

import java.util.ArrayList;
import java.util.List;

public class Group_Manager {
    ArrayList<Group_Layout> groups = new ArrayList<>();
    Context context;
    ViewGroup parent;
    ArrayList<Drawable> drawables;
    LockableScrollView scroll;
    Icon_Debugger icon_debugger = new Icon_Debugger();

    boolean edit_mode = true;
    public Group_Manager(Context context, ViewGroup parent, LockableScrollView scroll, Journal_Describer journal){
        this.scroll = scroll;
        this.parent = parent;
        this.context = context;
        setup(journal);

    }

    public void journal_mode(){
        edit_mode = false;
    }

    public void edit_mode(){
        edit_mode = true;
        for(int i = 0; i < groups.size(); i++){
            Group_Layout group = groups.get(i);
            for(int j = 0; j < group.icons.size(); j++){
                group.icons.get(j).make_white();
            }
        }
    }



    public int[] get_pos_from_cord(int x, int y) {
        for (int i = 0; i < groups.size(); i++) {
            Group_Layout group = groups.get(i);
            for (int j = 0; j < group.rows.size(); j++) {
                Row_Layout row = group.rows.get(j);
                for (int k = 0; k < row.size(); k++) {
                    Icon icon = row.get(k);
                    int[] icon_cords = null;
                    icon.view.getLocationOnScreen(icon_cords);
                    int rel_x = x - icon_cords[0];
                    int rel_y = y - icon_cords[1];
                    if (rel_x > 0 && rel_x < icon.width()) {
                        if (rel_y > 0 && rel_y < icon.width()) {
                            if (rel_y < icon.width() / 2) {
                                return new int[]{i, j, k};
                            } else {
                                return new int[]{i, j, k + 1};
                            }
                        }
                    }
                }
            }
        }
        return new int[]{-1, -1, -1};
    }

    public void setup(Journal_Describer journal){
        System.out.println("\n\n\n setting up group");
        for(int i = 0; i < journal.groups.size(); i++){
            Group_Describer group_describer = journal.groups.get(i);
            add_group(group_describer.name);
            for(int j = 0; j < group_describer.size(); j++){
                System.out.println("adding icon from journal");
                Icon_Describer icon_describer = group_describer.get(j);
                Drawable_With_Data drawable = Drawable_Manager.get_drawable_from_id(icon_describer.drawable_describer.id);
                add_icon_plus_layout(new Icon(context, drawable));
            }
        }
//        add_group();
//        add_icon_plus_layout(new Icon(context, drawables.get(0)));
//        add_icon_plus_layout(new Icon(context, drawables.get(1)));
//        add_icon_plus_layout(new Icon(context, drawables.get(0)));
//        add_icon_plus_layout(new Icon(context, drawables.get(1)));
//        add_icon_plus_layout(new Icon(context, drawables.get(0)));
//        add_icon_plus_layout(new Icon(context, drawables.get(1)));
        set_coordinates();
        print_cord();
    }

    public Journal_Describer get_describer(){
        Journal_Describer journal_describer = new Journal_Describer();
        for(int g = 0; g < groups.size(); g++){
            Group_Layout group = groups.get(g);
            journal_describer.add_group(group.title);
            for(int i = 0; i < group.icons.size(); i++){
                Icon icon = group.icons.get(i);
                journal_describer.add(new Icon_Describer(icon.drawable_with_data.get_describer()));
            }
        }
        return journal_describer;
    }

    public void new_icon(){
        Icon icon = new Icon(context, Drawable_Manager.get_drawable(0));
        icon.make_white();
        add_icon_plus_layout(icon);
    }

    public void add_icon(Icon icon){
        groups.get(groups.size() - 1).add_icon(icon);
        adjust_rows();
        set_coordinates();
    }

    public void add_icon(IconLocationStruct position, Icon icon){
        System.out.println("adding icon: " + icon.id + ", to: " + position);
        position.group.add_icon(position.icon_position, icon);
        adjust_rows();
        set_coordinates();
        try{
            icon.get_position();
        }catch (RuntimeException exception){
            System.out.println("RUNTIME EXCEPTION: " + position + ", icon: " + icon);
            System.out.println("group: ");
            position.group.print_tree();
            throw new RuntimeException();
        }
    }

    public void add_icon_plus_layout(Icon icon){
        add_icon(icon);
        parent.addView(icon.view);
        set_coordinates();
    }

    public void add_group(String name){
        System.out.println("adding group");
        groups.add(new Group_Layout(context, parent, this, name));
//        parent.postOnAnimation(new Runnable() {
//            @Override
//            public void run() {
//                set_coordinates();
//            }
//        });

        parent.postOnAnimationDelayed(new Runnable() {
            @Override
            public void run() {
                set_coordinates();
            }
        }, 20);

    }
    ArrayList<Adding_Box> adding_boxes;
    public void set_coordinates(){

        float current_y = 0;
        float group_margin = 50;
        for(int i = 0; i < groups.size(); i++){
            current_y = groups.get(i).set_coordinates(current_y) + group_margin;
        }
        adding_boxes = new ArrayList<>();
        for(int i = 0; i < groups.size(); i++){
            ArrayList<Adding_Box> group_boxes = groups.get(i).adding_boxes;
            adding_boxes.addAll(group_boxes);
        }
        int min_height = scroll.getHeight();
        if(current_y < min_height)
            parent.getLayoutParams().height = min_height;
        else
            parent.getLayoutParams().height = (int)current_y;
        System.out.println("num possible boxes: " + adding_boxes.size());
    }

    public void print_cord() {
        for(int i = 0; i < groups.size(); i++)
            groups.get(i).print_cord();

    }

    public void print_tree() {
        for(int i = 0; i < groups.size(); i++)
            groups.get(i).print_tree();
    }




    public void move_icon(int start_group, int start_row, int start_pos, int end_group, int end_row, int end_position){

        Icon icon = groups.get(start_group).remove_icon(start_row, start_pos);
        groups.get(end_group).add_icon(icon, end_row, end_position);
        adjust_rows();
        set_coordinates();
    }

    Drag current_drag = null;
    Drag_Manager drag_manager;
    public void start_drag(Icon icon, MotionEvent event){
        if(drag_manager == null){
            drag_manager = new Drag_Manager();
        }
        if(current_drag != null){
            if(current_drag.finished != true){
                System.out.println("previous drag not finished after calling start drag");
                System.out.println("finishing previous drag");
                //current_drag.finished = true;
                //throw new RuntimeException();
                drag_manager.print_drag_statement();
            }
        }


        Drag drag = new Drag(icon, this, drag_manager, parent, event);
        current_drag = drag;
        icon.drag = drag;
        drag.consume_event(event);
    }

    public void remove_icon(Icon icon){
        searching:
        for (int i = 0; i < groups.size(); i++) {
            Group_Layout group = groups.get(i);
            for (int j = 0; j < group.rows.size(); j++) {
                Row_Layout row = group.rows.get(j);
                boolean found = row.remove(icon);
                if(found){
                    group.icons.remove(icon);
                    break searching;
                }

            }
        }
        adjust_rows();
        set_coordinates();

    }

    public void adjust_rows(){
        for(int i = 0; i < groups.size(); i++)
            groups.get(i).adjust_rows();
    }
}
