package com.example.journal.Custom_Layout.Icon_Layout;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.journal.Custom_Layout.Data_Structure.IconLocationStruct;
import com.example.journal.Custom_Layout.Data_Structure.Rectanglef;
import com.example.journal.Custom_Layout.Utility.Image_Processing;
import com.example.journal.R;
import com.example.journal.Custom_Layout.Utility.ViewFactory;

import java.util.ArrayList;

public class Group_Layout {
    static int count = 1;
    public int id;
    ArrayList<Row_Layout> rows = new ArrayList<>();
    ArrayList<Icon> icons = new ArrayList<>();
    ViewGroup layout;
    Context context;
    int icon_width = Image_Processing.icon_draw_width;
    int group_width = 800;
    Group_Manager manager;
    float y = 0;
    float x = 0;
    CardView card;
    String title;
    TextView titleView;

    public Group_Layout(Context context, ViewGroup parent, Group_Manager manager, String name){
        id = count;
        title = name;
        count++;
        this.manager = manager;
        this.context = context;
        layout = new ConstraintLayout(context);
        layout.setLayoutParams(ViewFactory.createLayoutParams(0, 0, 0, 0, -1, -1));
        parent.addView(layout);
        layout = parent;
        create_card();
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(manager.sheet.sheet_is_open){
                    manager.sheet.click_while_open();
                }
            }
        });
    }

    public void create_card(){
        card = (CardView) LayoutInflater.from(context).inflate(R.layout.card_template, null);
        layout.addView(card);
        card.setLayoutParams(ViewFactory.createLayoutParams(300, -1, 8, 8, -1, -1));
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(manager.sheet.sheet_is_open){
                    manager.sheet.click_while_open();
                }
            }
        });
        titleView = new TextView(context);
        titleView.setLayoutParams(ViewFactory.createLayoutParams(0, -1, -1, 0, ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
        titleView.setText(title);
        titleView.setElevation(100);
        titleView.setTypeface(Typeface.DEFAULT_BOLD);
        titleView.setTextSize(25);
        titleView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                titleView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                manager.set_coordinates();

                // Now you have the dimensions of the TextView
            }
        });
        layout.addView(titleView);
    }

    public void move_icon(int start_row, int start_pos, int end_row, int end_position){
        Icon icon = rows.get(start_row).remove(start_pos);
        rows.get(end_row).add(icon, end_position);
        check_for_icons_error();
    }

    public Group_Manager parent(){
        return manager;
    }

    public void check_for_icons_error(){
        for(int i = 0; i < rows.size(); i++){
            ArrayList<Icon> row_icons = rows.get(i).icons;
            for(int j = 0; j < row_icons.size(); j++){
                if(!icons.contains(row_icons.get(j))){
                    System.out.println("group: " + id + ", row: " + rows.get(i).id + "at pos: " + j + ", icon: " + row_icons.get(j).class_id + " is in the row but isn't in the group's list");
                    throw new RuntimeException();
                }
            }
        }
    }

    public Icon get_icon(int row, int position){
        return rows.get(row).get(position);
    }

    public Icon remove_icon(int row, int position){

        Icon icon = rows.get(row).remove(position);
        icons.remove(icon);
        check_for_icons_error();
        return icon;
    }

    public void add_icon(Icon icon, int row, int position){
        if(rows.size() == row)
            rows.add(new Row_Layout(this));
        rows.get(row).add(icon, position);
        icons.add(icon);
        check_for_icons_error();
    }

    public void adjust_rows(){
        check_for_icons_error();
        int count_remove = 0;
        for(int i = 0; i < rows.size(); i++){
            for(int j = rows.get(i).size() - 1; j >= 0; j--){
                rows.get(i).remove(j);
                count_remove++;
            }
        }
        for(int i = 0; i < icons.size(); i++){
            icons.get(i).remove_from_row();
        }

        rows = new ArrayList<>();
        int icon_index = 0;
        while(icon_index < icons.size()){
            //System.out.println("iterating while: (icons)size: " + icons.size() + " , index: " + icon_index);
            rows.add(new Row_Layout(this));
            while(rows.get(rows.size() - 1).can_fit(icon_width, group_width)){
                int row_index = (rows.size() - 1);
                //System.out.println("(rows)size: " + rows.size() + " , index: " + row_index);
                //System.out.println("(icons)size: " + icons.size() + " , index: " + icon_index);
                rows.get(row_index).add(icons.get(icon_index));
                icon_index++;
                if(icon_index >= icons.size()){
                    //System.out.println("breaking because icon index greater than size");
                    break;
                }

            }
        }


        check_for_icons_error();
    }

    public void adjust_rows_bad(){
        check_for_icons_error();
        //System.out.println("adjusting for overflow");
        for(int i = 0; i < rows.size(); i++){
            Row_Layout row = rows.get(i);
            while(rows.get(i).overflowing(group_width)){
                Icon icon = row.remove(row.size() - 1);
                if(rows.size() - 1 == i)
                    rows.add(new Row_Layout(this));
                rows.get(i + 1).add(icon, 0);
            }
        }
        boolean did_something = false;
        //System.out.println("filling up top rows");
        while(1==1){
            did_something = false;
            for(int i = 0; i < rows.size() - 1; i++){
                Row_Layout row = rows.get(i);
                if(rows.get(i + 1).size() > 0){
                    Icon icon = rows.get(i + 1).get(0);
                    if(row.can_fit(icon.width(), group_width)){
                        //System.out.println("row " + i +" can fit icon from " + (i + 1));
                        did_something = true;
                        rows.get(i + 1).remove(0);
                        row.add(icon);
                    }
                }

            }
            //System.out.println("did something? " + did_something);
            if(!did_something){
                //System.out.println("breaking while loop");
                break;
            }

        }
        for(int i = rows.size() - 1; i >= 0; i--){
            if(rows.get(i).icons.size() == 0)
                rows.remove(i);
        }
        //System.out.println("done filling up");
        check_for_icons_error();
    }


    ArrayList<Adding_Box> adding_boxes;
    public float set_coordinates(float y_start){
        //System.out.println("group icon id's: ");
        for(int i = 0; i < icons.size(); i++){
            //System.out.print(icons.get(i).id+", ");
        }
        //System.out.println();
        this.y = y_start;
        adding_boxes = new ArrayList<>();
        float icon_margin = 10;
        //System.out.println("("+id+")setting card y to: " + y_start);
        float icon_and_card_margin = 10;
        float title_height = 100;
        if(titleView.getHeight() > title_height)
            title_height = titleView.getHeight();

        float x = icon_and_card_margin + icon_margin;
        float y = icon_and_card_margin + y_start;

        titleView.setY(y);
        titleView.setX(x);
        y += titleView.getHeight() + icon_margin;


        boolean could_fit_ghost_in_last_row = false;
        for(int i = 0; i < rows.size(); i++){
            //System.out.println("row: " + i);
            //System.out.println("y+= "+icon_margin + "(icon margin)");
            Row_Layout row = rows.get(i);
            float icon_x = x;
            Icon icon = null;
            //System.out.println("starting x: " + icon_x);
            for(int j = 0; j < row.size(); j++){

                //System.out.print("icon: " + j + ", ");
                icon = row.get(j);
                //System.out.println("adding bounding box for icon: " + icon.id + ", index in group: " + icons.indexOf(icon));
                adding_boxes.add(new Adding_Box(new Rectanglef(y, y+icon_width, icon_x, icon_x + icon_width), new IconLocationStruct(this, icons.indexOf(icon))));
                //System.out.println("\t"+adding_boxes.get(adding_boxes.size() - 1));
                icon.set_layout_position(icon_x, y);
                icon_x += icon_width + icon_margin;
                //System.out.println("adding to x: " + icon_x);
            }
            //if the row can fit another icon, create a bounding box for adding to the end of the row
            if(row.can_fit(icon_width, group_width) && icon != null){
                could_fit_ghost_in_last_row = true;
                //System.out.println("(end of row)adding bounding box for icon: " + icon.id);
                adding_boxes.add(new Adding_Box(new Rectanglef(y, y+icon_width, icon_x, icon_x + icon_width), new IconLocationStruct(this, icons.indexOf(icon) + 1)));
                //System.out.println("\t"+adding_boxes.get(adding_boxes.size() - 1));
            }

            //System.out.println();
            //System.out.println("y+="+icon_width+"(icon width)");
            y += icon_width + icon_margin;
        }
        if(!could_fit_ghost_in_last_row){
            //System.out.println("(could not fit in last row)adding bounding box for icon: ");
            adding_boxes.add(new Adding_Box(new Rectanglef(y, y + icon_width,x , x + icon_width), new IconLocationStruct(this, icons.size())));
        }

        y+= icon_and_card_margin;
        //System.out.println("("+id+")setting card height to: " + (int)(y - y_start));
        card.setLayoutParams(ViewFactory.createLayoutParams((int)y_start, -1, 20, 20, -1, (int)(y - y_start)));
        //layout.setLayoutParams(ViewFactory.createLayoutParams(0, 0, 0, 0, -1, (int)y));
        //System.out.println("the adding boxes: ");
        for(int i = 0; i < adding_boxes.size(); i++){
            adding_boxes.get(i).bounding_box.reduce(50, 50);
        }
        for(int i = 0; i < adding_boxes.size(); i++){
            //System.out.println("\t"+adding_boxes.get(i));
        }
        //System.out.println("("+id+"finished setting coordinates");
        return y;
    }

    public void print_cord(){
        System.out.println("("+id+")");
        System.out.println("card: " + card.getX() + ", " + card.getY() + " - " + card.getWidth() + ", " + card.getHeight());
        for(int i = 0; i < rows.size(); i++){
            Row_Layout row = rows.get(i);
            System.out.print("row: ");
            for(int j = 0; j < row.size(); j++){
                Icon icon = row.get(j);
                View view = icon.view;
                System.out.print("("+view.getX() + ", " + view.getY() + ")-("+view.getWidth() + ", "+view.getHeight()+"), ");
            }
            System.out.println();

        }
        System.out.println("printing true positions");
        for(int i = 0; i < rows.size(); i++){
            Row_Layout row = rows.get(i);
            System.out.print("row: ");
            for(int j = 0; j < row.size(); j++){
                Icon icon = row.get(j);
                View view = icon.view;
                System.out.print("("+icon.position_x + ", " + icon.position_y + ")");
            }
            System.out.println();

        }
    }

    public void print_tree(){
        System.out.println("("+id+")printing tree: ");
        for(int i = 0; i < rows.size(); i++){

            Row_Layout row = rows.get(i);
            System.out.print("row ("+row.id+"): ");
            for(int j = 0; j < row.size(); j++){
                Icon icon = row.get(j);
                View view = icon.view;
                System.out.print(icon.class_id +", ");
            }
            System.out.println();

        }
    }

    public void add_icon(int index, Icon icon){
        icons.add(index, icon);
    }

    public void add_icon_bad(int index, Icon icon){
        if(icons.size() < index)
            throw new RuntimeException();
        if(icons.size() == index){
            add_icon(icon);
            check_for_icons_error();
            return;
        }
        if(icons.size() == 0){
            add_icon(icon);
            check_for_icons_error();
            return;
        }
        if(index == 0){
            add_icon(icon, 0, 0);
            check_for_icons_error();
            return;
        }
        Icon previous_icon = icons.get(index - 1);
        System.out.println("previous icon: " + previous_icon.class_id);
        for(int i = 0; i < rows.size(); i++){
            if(rows.get(i).icons.contains(previous_icon)){
                System.out.println("found previous icon, row: " + rows.get(i).id);
                int previous_index_of_row = rows.get(i).icons.indexOf(previous_icon);
                System.out.println("index in row: " + previous_index_of_row);
                rows.get(i).add(icon, previous_index_of_row + 1);
                icons.add(index, icon);
                try{
                    check_for_icons_error();
                }catch(RuntimeException exception){
                    System.out.println();
                }
                return;
            }
        }
        throw new RuntimeException();
    }

    public void add_icon(Icon icon){
        if(rows.size() == 0)
            rows.add(new Row_Layout(this));
        rows.get(rows.size() - 1).add(icon);
        icons.add(icon);
        check_for_icons_error();
    }
}
