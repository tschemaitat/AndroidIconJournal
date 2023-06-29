package com.example.journal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.journal.Custom_Layout.*;

public class MainActivity extends AppCompatActivity {

    //add view to layout
    //add input text to layout
    Context context;
    ConstraintLayout main_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        context = this;

        setContentView(R.layout.activity_main);
        main_layout = findViewById(R.id.main_layout);

        //create_edit_page();
        Journal_Describer journal = User_Information.get_journal(this);
        create_journal_entry_page(journal);
        print_view(main_layout, 0);
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

        Group_Manager groups = using_custom_layout(edit_layout, journal);

        create_button("print cord", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groups.print_cord();
                groups.print_tree();
            }
        });

        create_button("move icon", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groups.move_icon(0, 0, 0, 0, 1, 1);
            }
        });

        create_button("add group", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groups.add_group("default");
            }
        });

        create_button("add icon", new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                groups.new_icon();
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
        main_layout.removeAllViews();
        create_journal_entry_page(User_Information.get_journal(context));

    }

    public void create_journal_entry_page(Journal_Describer journal){
        Icon.journal_mode();
        ConstraintLayout journal_entry_layout = inflate_page(R.layout.journal_non_edit_entry);

        using_custom_layout(journal_entry_layout, journal);

        ImageView edit_page_button = findViewById(R.id.edit_page_button);
        edit_page_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close_journal_open_edit();
            }
        });
    }

    public void close_journal_open_edit(){
        main_layout.removeAllViews();
        create_edit_page(User_Information.get_journal(context));
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
        //ScrollView old_scroll = scrollView;
        //scroll.setLayoutParams(old_scroll.getLayoutParams());
        scroll.setLayoutParams(ViewFactory.createLayoutParams(top_distance, bottom_distance, 0, 0, -1, -1));
        //ViewGroup old_scroll_linearLayout = ((ViewGroup)old_scroll.getChildAt(0));
        ViewGroup scroll_linearLayout = new LinearLayout(this);

        //scroll_linearLayout.setLayoutParams(old_scroll_linearLayout.getLayoutParams());
        scroll.addView(scroll_linearLayout);
//        int num_children = old_scroll_linearLayout.getChildCount();
//        //System.out.println("num children");
//        for(int i = num_children - 1; i >= 0; i--){
//            View child = old_scroll_linearLayout.getChildAt(0);
//            old_scroll_linearLayout.removeView(child);
//            scroll_linearLayout.addView(child);
//        }
//        ((ViewGroup)old_scroll.getParent()).addView(scroll);
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