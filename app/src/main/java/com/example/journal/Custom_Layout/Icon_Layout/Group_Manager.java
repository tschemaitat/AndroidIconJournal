package com.example.journal.Custom_Layout.Icon_Layout;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.journal.Custom_Layout.Custom_View.LockableScrollView;
import com.example.journal.Custom_Layout.Describer.Group_Describer;
import com.example.journal.Custom_Layout.Describer.Icon_Describer;
import com.example.journal.Custom_Layout.Describer.Journal_Describer;
import com.example.journal.Custom_Layout.Utility.Drawable_Manager;
import com.example.journal.Custom_Layout.Data_Structure.Drawable_With_Data;
import com.example.journal.Custom_Layout.Data_Structure.IconLocationStruct;
import com.example.journal.Custom_Layout.Entry.Icon_Entry;
import com.example.journal.Custom_Layout.Entry.Journal_Entry;
import com.example.journal.Custom_Layout.Utility.ViewFactory;
import com.example.journal.MainActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;

public class Group_Manager {
    public static int count = 1;
    public int my_count;
    ArrayList<Group_Layout> groups = new ArrayList<>();
    Context context;
    ViewGroup parent;
    ArrayList<Drawable> drawables;
    LockableScrollView scroll;
    Icon_Debugger icon_debugger = new Icon_Debugger();
    boolean ready_to_open_sheets = false;
    ViewGroup page_layout;
    Bottom_Sroll_Sheet sheet = null;

    public Calendar date;

    boolean edit_mode = true;

    public Group_Manager(Context context, Journal_Describer journal, ConstraintLayout page_layout){

        my_count = count;
        count++;
        System.out.println("<group manager "+count+"> new manager");
        this.context = context;
        LockableScrollView scroll = construct_scroll(300, 0, context);
        page_layout.addView(scroll);
        LinearLayout scroll_linear_layout = (LinearLayout) scroll.getChildAt(0);
        ConstraintLayout icon_layout = new ConstraintLayout(context);
        scroll_linear_layout.addView(icon_layout);
        //ConstraintLayout icon_layout = edit_layout.findViewById(R.id.icon_layout);
        icon_layout.setLayoutParams(new LinearLayout.LayoutParams(-1, 0));

        this.page_layout = page_layout;
        this.scroll = scroll;
        this.parent = icon_layout;
        this.context = context;
        setup(journal);
    }

    public LockableScrollView construct_scroll(int top_distance, int bottom_distance, Context context){
        LockableScrollView lockableScrollView = MainActivity.make_custom_scroll(context);
        lockableScrollView.setLayoutParams(ViewFactory.createLayoutParams(top_distance, bottom_distance, 0, 0, -1, -1));
        return lockableScrollView;
    }

//    public Group_Manager(Context context, ViewGroup parent, LockableScrollView scroll, Journal_Describer journal, ViewGroup sheet_parent){
//        this.page_layout = sheet_parent;
//        this.scroll = scroll;
//        this.parent = parent;
//        this.context = context;
//        setup(journal);
//
//    }



//    public Group_Manager using_custom_layout(ConstraintLayout edit_layout, Journal_Describer journal){
//        LockableScrollView scroll = make_custom_scroll(300, 0);
//        edit_layout.addView(scroll);
//        LinearLayout scroll_linear_layout = (LinearLayout) scroll.getChildAt(0);
//        ConstraintLayout icon_layout = new ConstraintLayout(context);
//        scroll_linear_layout.addView(icon_layout);
//        //ConstraintLayout icon_layout = edit_layout.findViewById(R.id.icon_layout);
//        icon_layout.setLayoutParams(new LinearLayout.LayoutParams(-1, 0));
//
//
//        Group_Manager manager = new Group_Manager(context, icon_layout, scroll, journal, edit_layout);
//        return manager;
//    }

    public void set_date(Calendar date){
        this.date = date;
    }

    public void journal_mode(){
        edit_mode = false;
    }

    public void edit_mode(){
        ready_to_open_sheets = false;
        System.out.println("<group manager "+count+"> edit mode" );
        edit_mode = true;
        for(int i = 0; i < groups.size(); i++){
            Group_Layout group = groups.get(i);
            for(int j = 0; j < group.icons.size(); j++){
                group.icons.get(j).make_white();
            }
        }
        ready_to_open_sheets = true;
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
        System.out.println("<group manager "+count+"> setup" );
        //System.out.println("\n\n\n setting up group");
        for(int i = 0; i < journal.groups.size(); i++){
            Group_Describer group_describer = journal.groups.get(i);
            add_group(group_describer.name);
            for(int j = 0; j < group_describer.size(); j++){
                //System.out.println("adding icon from journal");
                Icon_Describer icon_describer = group_describer.get(j);
                Drawable_With_Data drawable = Drawable_Manager.get_drawable(icon_describer.drawable_describer.name);
                Icon icon = new Icon(icon_describer, this, context, drawable);
                add_icon_plus_layout(icon);
            }
        }
        ArrayList<Icon> icons = get_icons();
        for(int i = 0; i < icons.size(); i++){
            Icon icon = icons.get(i);
            icon.journal_describer_id = i;

        }
//        add_group();
//        add_icon_plus_layout(new Icon(context, drawables.get(0)));
//        add_icon_plus_layout(new Icon(context, drawables.get(1)));
//        add_icon_plus_layout(new Icon(context, drawables.get(0)));
//        add_icon_plus_layout(new Icon(context, drawables.get(1)));
//        add_icon_plus_layout(new Icon(context, drawables.get(0)));
//        add_icon_plus_layout(new Icon(context, drawables.get(1)));
        set_coordinates();
        //print_cord();
        ready_to_open_sheets = true;
    }

    private ArrayList<Icon> get_icons(){
        ArrayList<Icon> icons = new ArrayList<>();
        for(int i = 0; i < groups.size(); i++){
            Group_Layout group = groups.get(i);
            icons.addAll(group.icons);
        }
        return icons;
    }

    public Journal_Describer get_describer(){
        System.out.println("<group manager> exporting journal describer");
        Journal_Describer journal_describer = new Journal_Describer();
        for(int g = 0; g < groups.size(); g++){
            Group_Layout group = groups.get(g);
            journal_describer.add_group(group.title);
            for(int i = 0; i < group.icons.size(); i++){
                Icon icon = group.icons.get(i);
                System.out.println("\t<group manager> saving icon, title: " + icon.get_title());
                journal_describer.add(new Icon_Describer(icon.get_title(), icon.drawable_with_data.get_describer()));
            }
        }
        return journal_describer;
    }

    public HashMap<Integer, Integer> get_old_to_new_map(){
        System.out.println("creating icon map:");
        HashMap<Integer, Integer> map = new HashMap<>();
        ArrayList<Icon> icons = get_icons();
        for(int i = 0; i < icons.size(); i++){
            int old_id = icons.get(i).journal_describer_id;
            if(old_id == -1){
                continue;
            }
            //the new id, i, will map to the old id, <old_id>
            //we will use this to converts the entries, listed with characteristics of the old id, and convert it to the new id
            map.put(i, old_id);
            System.out.println("old id: " + old_id + ", maps to: " + i);
        }

        return map;
    }

    public Journal_Entry get_entry(){
        Journal_Entry entry = new Journal_Entry(get_describer(), date);
        int icon_count = 0;
        for(int g = 0; g < groups.size(); g++){
            Group_Layout group = groups.get(g);
            Group_Describer group_describer = new Group_Describer(group.title);
            for(int i = 0; i < group.icons.size(); i++, icon_count++){
                Icon icon = group.icons.get(i);
                Icon_Describer icon_describer = new Icon_Describer(icon.get_title(), icon.drawable_with_data.get_describer());
                int icon_id = icon_count;
                boolean on = icon.is_white;
                String text = "null";
                entry.add_icon_entry(icon_id, on, text);
            }
        }
        return entry;
    }

    public void new_icon(){
        int size = get_icons().size();
        Icon icon = new Icon(new Icon_Describer(""+size, Drawable_Manager.get_drawable(0).get_describer()), this, context, Drawable_Manager.get_drawable(0));
        icon.make_white();
        add_icon_plus_layout(icon);
    }

    public void add_icon(Icon icon){
        groups.get(groups.size() - 1).add_icon(icon);
        adjust_rows();
        set_coordinates();
    }

    public void add_icon(IconLocationStruct position, Icon icon){
        //System.out.println("adding icon: " + icon.id + ", to: " + position);
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
        icon.manager = this;
        add_icon(icon);
        parent.addView(icon.view);
        set_coordinates();
    }

    public void add_group(String name){
        //System.out.println("adding group");
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
        //System.out.println("num possible boxes: " + adding_boxes.size());
    }

    public void print_cord() {
        for(int i = 0; i < groups.size(); i++)
            groups.get(i).print_cord();

    }

    public void set_entries(Journal_Entry journal_entry){
        ready_to_open_sheets = false;
        System.out.println("<group manager "+count+"> set entries" );
        //print_tree();
        int group_index = 0;
        int group_icon_index = 0;
        ArrayList<Icon> icons = get_icons();
        ArrayList<Icon_Entry> entries = journal_entry.icon_entries;
        for(int i = 0; i < entries.size(); i++, group_icon_index++){
            Icon_Entry entry = entries.get(i);
            Icon icon = icons.get(entry.icon_id);
            if(entry.on){
                icon.make_white();
            }
            icon.entry_text = entry.text;
        }
        ready_to_open_sheets = true;
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

    public void open_sheet(Icon icon) {

        if(ready_to_open_sheets){
            if(sheet != null){
                page_layout.removeView(sheet.root_view);
            }

            sheet = Bottom_Sroll_Sheet.make_sheet(page_layout, context, icon, this);
            System.out.println("<group manager "+count+"> opening sheet" );
        }

        else
            return;
        if(edit_mode){
            sheet.inject_icon_settings(icon);
        }

        if(!edit_mode){
            sheet.inject_icon_content(icon);
        }
    }
}
