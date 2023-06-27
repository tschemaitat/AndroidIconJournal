package com.example.journal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.transition.ChangeBounds;
import androidx.transition.Scene;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.journal.Custom_Layout.Group_Layout;
import com.example.journal.Custom_Layout.*;

public class MainActivity extends AppCompatActivity {

    //add view to layout
    //add input text to layout
    Context context;
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

        create_edit_page();
    }

    public ConstraintLayout inflate_page(int layout_id){
        ConstraintLayout edit_layout = (ConstraintLayout) LayoutInflater.from(context).inflate(layout_id, null);
        ConstraintLayout main_layout = findViewById(R.id.main_layout);
        main_layout.addView(edit_layout);
        edit_layout.setLayoutParams(ViewFactory.createLayoutParams(0, 0, 0, 0, -1, -1));
        return edit_layout;
    }

    public void create_edit_page(){

        ConstraintLayout edit_layout = inflate_page(R.layout.group_row);

        using_custom_layout(edit_layout);
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

    public LockableScrollView replace_scrollView(ScrollView scrollView){
        LockableScrollView scroll = new LockableScrollView(this);
        ScrollView old_scroll = scrollView;
        scroll.setLayoutParams(old_scroll.getLayoutParams());
        ViewGroup old_scroll_linearLayout = ((ViewGroup)old_scroll.getChildAt(0));
        ViewGroup scroll_linearLayout = new LinearLayout(this);

        scroll_linearLayout.setLayoutParams(old_scroll_linearLayout.getLayoutParams());
        scroll.addView(scroll_linearLayout);
        int num_children = old_scroll_linearLayout.getChildCount();
        //System.out.println("num children");
        for(int i = num_children - 1; i >= 0; i--){
            View child = old_scroll_linearLayout.getChildAt(0);
            old_scroll_linearLayout.removeView(child);
            scroll_linearLayout.addView(child);
        }
        ((ViewGroup)old_scroll.getParent()).addView(scroll);
        return scroll;
    }

    public void using_custom_layout(ConstraintLayout edit_layout){

        ScrollView scrollView =  edit_layout.findViewById(R.id.icon_scrollView);
        ConstraintLayout icon_layout = edit_layout.findViewById(R.id.icon_layout);

        LockableScrollView scroll = replace_scrollView(scrollView);
        ConstraintLayout constraintLayout = icon_layout;
        Group_Manager groups = new Group_Manager(this, constraintLayout, scroll);
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
                groups.add_group();
            }
        });

        create_button("add icon", new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                groups.new_icon();
            }
        });
    }


    public void print_view(View view, int num_tabs){
        for(int tab = 0; tab < num_tabs; tab++)
            System.out.print("\t");
        view.setVisibility(View.VISIBLE);
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