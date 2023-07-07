package com.example.journal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import android.Manifest;

import com.example.journal.Custom_Layout.*;
import com.example.journal.Custom_Layout.Custom_View.LockableScrollView;
import com.example.journal.Custom_Layout.Describer.User_Journals;
import com.example.journal.Custom_Layout.Entry.Journal_Entry;
import com.example.journal.Custom_Layout.Entry.User_Entries;
import com.example.journal.Custom_Layout.Utility.File_Utilities;
import com.example.journal.Custom_Layout.Describer.Journal_Describer;
import com.example.journal.Custom_Layout.Icon_Layout.Group_Manager;
import com.example.journal.Custom_Layout.Icon_Layout.Icon;
import com.example.journal.Custom_Layout.Utility.ViewFactory;

public class MainActivity extends AppCompatActivity {

    //add view to layout
    //add input text to layout
    Context context;
    ConstraintLayout main_layout;
    Group_Manager displayed_icons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Drawable_Manager.drawables(this);
        verifyStoragePermissions(this);
        //User_Journals.set_journal_normal();
        context = this;

        setContentView(R.layout.activity_main);
        main_layout = findViewById(R.id.main_layout);

        //create_edit_page();
        Journal_Describer journal = User_Journals.get_journal(this);
        create_journal_entry_page(journal);
        print_view(main_layout, 0);
    }
    private static final int REQUEST_WRITE_STORAGE = 112;
    private static final int REQUEST_READ_STORAGE = 113;
    public void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE
            );
        }

//        permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
//        if (permission != PackageManager.PERMISSION_GRANTED) {
//            // We don't have permission so prompt the user
//            ActivityCompat.requestPermissions(
//                    activity,
//                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                    REQUEST_READ_STORAGE
//            );
//        }
    }

    public ConstraintLayout inflate_page(int layout_id){
        ConstraintLayout edit_layout = (ConstraintLayout) LayoutInflater.from(context).inflate(layout_id, null);
        ConstraintLayout main_layout = findViewById(R.id.main_layout);
        main_layout.addView(edit_layout);
        edit_layout.setLayoutParams(ViewFactory.createLayoutParams(0, 0, 0, 0, -1, -1));
        return edit_layout;
    }

    public void create_edit_page(Journal_Describer journal){
        Icon.edit_mode();

        ConstraintLayout edit_layout = inflate_page(R.layout.group_row);

        ScrollView scrollView =  edit_layout.findViewById(R.id.icon_scrollView);
        edit_layout.removeView(scrollView);

        displayed_icons = using_custom_layout(edit_layout, journal);
        displayed_icons.edit_mode();
        create_button("print cord", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayed_icons.print_cord();
                displayed_icons.print_tree();
            }
        });

        create_button("move icon", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayed_icons.move_icon(0, 0, 0, 0, 1, 1);
            }
        });

        create_button("add group", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayed_icons.add_group("default");
            }
        });

        create_button("add icon", new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                displayed_icons.new_icon();
            }
        });
        create_button("exit edit", new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                close_edit_open_journal();
            }
        });
    }

    public void close_edit_open_journal(){
        //this is the object that describes the structure of the journal that was last seen in the edit page
        //it can be used to create a new Group_Manager object
        Journal_Describer new_journal = displayed_icons.get_describer();
        //String json_data = new_journal.get_json().toString();
        //System.out.println(json_data);
        System.out.println("writing data");
        User_Journals.write_journal(new_journal, context);
        //this object is create from the data outputed from the new_journal
        //this will be used in the new page to make sure that the saved data is not corrupt
        Journal_Describer from_data = User_Journals.check_data_parse(new_journal);
        //System.out.println("parse journal from data: \n" + from_data.data_string());
        User_Journals.set_journal(from_data);
        main_layout.removeAllViews();
        create_journal_entry_page(User_Journals.get_journal(context));


    }

    public void create_journal_entry_page(Journal_Describer journal){
        Icon.journal_mode();
        ConstraintLayout journal_entry_layout = inflate_page(R.layout.journal_non_edit_entry);

        displayed_icons = using_custom_layout(journal_entry_layout, journal);
        displayed_icons.journal_mode();
        ImageView edit_page_button = findViewById(R.id.edit_page_button);
        edit_page_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close_journal_open_edit();
            }
        });
        Journal_Entry journal_entry = User_Entries.get_entry(displayed_icons.get_describer(), context);
        displayed_icons.set_entries(journal_entry);
    }

    public void close_journal_open_edit(){
        Journal_Entry journal_entry = displayed_icons.get_entry();
        System.out.println("entry: \n" + journal_entry.get_json());
        System.out.println("writing entry");
        User_Entries.write_entry(journal_entry, context);
        Journal_Entry parsed_entry = User_Entries.get_entry(User_Journals.get_journal(context), context);
        System.out.println("parsed entry from file: \n" + parsed_entry);

        main_layout.removeAllViews();
        create_edit_page(User_Journals.get_journal(context));

        button_count = 0;

    }

    int button_count = 0;
    public void create_button(String name, View.OnClickListener action){
        LinearLayout button_vertical_layout = (LinearLayout) findViewById(R.id.button_linear_layout);
        LinearLayout button_horizontal_layout = (LinearLayout)button_vertical_layout.getChildAt(0);
        button_count ++;
        if(button_count > 3){
            System.out.println("using second button layout");
            button_horizontal_layout = (LinearLayout)button_vertical_layout.getChildAt(1);
        }
        Button button = (Button) LayoutInflater.from(context).inflate(R.layout.button_template, null);
        button.setText(name);
        button.setOnClickListener(action);
        button_horizontal_layout.addView(button);



    }

    public LockableScrollView make_custom_scroll(int top_distance, int bottom_distance){
        LockableScrollView scroll = new LockableScrollView(this);
        scroll.setLayoutParams(ViewFactory.createLayoutParams(top_distance, bottom_distance, 0, 0, -1, -1));
        ViewGroup scroll_linearLayout = new LinearLayout(this);
        scroll.addView(scroll_linearLayout);
        return scroll;
    }

    public Group_Manager using_custom_layout(ConstraintLayout edit_layout, Journal_Describer journal){
        LockableScrollView scroll = make_custom_scroll(300, 0);
        edit_layout.addView(scroll);
        LinearLayout scroll_linear_layout = (LinearLayout) scroll.getChildAt(0);
        ConstraintLayout icon_layout = new ConstraintLayout(context);
        scroll_linear_layout.addView(icon_layout);
        //ConstraintLayout icon_layout = edit_layout.findViewById(R.id.icon_layout);
        icon_layout.setLayoutParams(new LinearLayout.LayoutParams(-1, 0));


        Group_Manager groups = new Group_Manager(this, icon_layout, scroll, journal);
        return groups;
    }


    public void print_view(View view, int num_tabs){
        for(int tab = 0; tab < num_tabs; tab++)
            System.out.print("\t");
        //view.setVisibility(View.VISIBLE);
        System.out.println(view + ", ");
        if(!(view instanceof ViewGroup))
            return;
        ViewGroup group = (ViewGroup) view;
        for(int i = 0; i < group.getChildCount(); i++){
            View child = group.getChildAt(i);
            print_view(child, num_tabs + 1);
        }
    }





    public void create_button_animation(){
        Button myButton = findViewById(R.id.button);
        //RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) myButton.getLayoutParams();

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Determine the end position for the animation. The Y value might be
                // the distance in pixels you want to move the view by. Adjust as necessary.
                float endPosition = myButton.getY() + 100f;

                // Create an ObjectAnimator instance
                ObjectAnimator anim = ObjectAnimator.ofFloat(myButton, "translationY", endPosition);
                anim.setDuration(1000); // Set duration to 1 second (1000 milliseconds)
                anim.start();
            }
        });
    }
}