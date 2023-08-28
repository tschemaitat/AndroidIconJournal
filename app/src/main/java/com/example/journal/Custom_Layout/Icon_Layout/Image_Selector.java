package com.example.journal.Custom_Layout.Icon_Layout;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.journal.Custom_Layout.Data_Structure.Drawable_With_Data;
import com.example.journal.Custom_Layout.Listener.Close_Listener;
import com.example.journal.Custom_Layout.Utility.Drawable_Manager;
import com.example.journal.Custom_Layout.Listener.Drawable_Listener;
import com.example.journal.Custom_Layout.Utility.Image_Processing;
import com.example.journal.Custom_Layout.Utility.ViewFactory;
import com.example.journal.MainActivity;
import com.example.journal.R;

import java.util.ArrayList;

public class Image_Selector implements Close_Listener {
    ConstraintLayout selector_parent;
    Context context;
    Drawable_Listener drawable_listener;
    ConstraintLayout layout;
    public Image_Selector(ConstraintLayout selector_parent, Context context, Drawable_Listener drawable_listener){
        this.selector_parent = selector_parent;
        this.context = context;
        this.drawable_listener = drawable_listener;
        add_gray_background();
        construct_selector_view();

    }

    public View get_view(){
        return layout;
    }

    public void add_gray_background(){
        Gray_Over gray_over = new Gray_Over(context, selector_parent, this);
    }
    @Override
    public void close(){
        selector_parent.removeView(layout);
    }

    public void construct_selector_view(){
        int top_panel_size = 200;
        ArrayList<Drawable_With_Data> drawables = Drawable_Manager.get_drawables();
        int icon_size = Image_Processing.icon_picture_width;
        int spacing = 20;
        int width = (icon_size + spacing) * 5;
        int lines = 6;//(drawables.size() - 1)/4 + 1;
        int height = calculate_height(icon_size, spacing, lines) + (icon_size + spacing)/2;
        System.out.println("height: " + height + ", lines: " + lines + ", width: "+(icon_size+spacing) );
        System.out.println("<image selector>setting width and height: " + width + ", " + height);

        layout = construct_main_constraint_layout(width, height);

        LinearLayout vertical_layout = construct_vertical_layout(drawables, icon_size, spacing);
        //layout.addView(vertical_layout);
        ScrollView scrollView = new ScrollView(context);
        scrollView.setLayoutParams(ViewFactory.createLayoutParams(top_panel_size, 0, 0, 0, ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));
        scrollView.addView(vertical_layout);
        layout.addView(scrollView);

        ConstraintLayout top_panel = construct_top_panel(top_panel_size);
        layout.addView(top_panel);
//        ConstraintLayout button_layout = new ConstraintLayout(context);
//        layout.addView(button_layout);
//        button_layout.setLayoutParams(ViewFactory.createLayoutParams(-1, 0, 0, 0, ConstraintLayout.LayoutParams.MATCH_PARENT, 200));
//

    }

    private ConstraintLayout construct_top_panel(int top_panel_size){
        ConstraintLayout constraintLayout = new ConstraintLayout(context);
        constraintLayout.setLayoutParams(ViewFactory.createLayoutParams(0, -1, 0, 0, -1, top_panel_size));
        ImageButton exit_button = new ImageButton(context);
        constraintLayout.addView(exit_button);
        exit_button.setLayoutParams(ViewFactory.createLayoutParams(0, 0, 10, -1, 150, 150));
        exit_button.setImageResource(R.drawable.exit_drawable_foreground);
        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawable_listener.retrieve_drawable(null);
                selector_parent.removeView(layout);
            }
        });
        return constraintLayout;
    }

    private int calculate_height(int icon_size, int spacing, int lines){
        return lines * (icon_size + spacing);
    }

    private ScrollView construct_scroll_view(){
        ScrollView scrollView = MainActivity.make_scroll(context);
        scrollView.setLayoutParams(ViewFactory.createLayoutParams(0, 0, 0, 0, ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));
        return scrollView;
    }

    private ConstraintLayout construct_main_constraint_layout(int width, int height){
        ConstraintLayout constraint_layout = new ConstraintLayout(context);
        constraint_layout.setBackgroundColor(Color.DKGRAY);
        //layout.setLayoutParams(ViewFactory.createLayoutParams(0, 0, 0, 0, ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));
        constraint_layout.setLayoutParams(ViewFactory.createLayoutParams(0, 0, 0, 0, width, height));
        selector_parent.addView(constraint_layout);
        selector_parent.bringChildToFront(constraint_layout);
        constraint_layout.setElevation(100f);
        return constraint_layout;
    }

    private LinearLayout construct_vertical_layout(ArrayList<Drawable_With_Data> drawables, int icon_size, int spacing){
        LinearLayout vertical_layout = new LinearLayout(context);
        vertical_layout.setOrientation(LinearLayout.VERTICAL);
        vertical_layout.setLayoutParams(ViewFactory.createLayoutParams(0, 0, 0, 0, -1, -1));
        ArrayList<ConstraintLayout> drawable_layouts = new ArrayList<>();

        for(int i = 0; i < drawables.size(); i++){
            ConstraintLayout drawable_layout = new ConstraintLayout(context);
            drawable_layouts.add(drawable_layout);
            drawable_layout.setLayoutParams(new LinearLayout.LayoutParams(icon_size + spacing, icon_size + spacing));
            ImageView image = new ImageView(context);
            Drawable_With_Data my_drawable = drawables.get(i);
            System.out.println("<image selector> drawable size: " + my_drawable.drawable.getIntrinsicWidth() + ", " + my_drawable.drawable.getIntrinsicHeight());//prints 140, 140
            image.setImageDrawable(my_drawable.drawable);
            image.setLayoutParams(ViewFactory.createLayoutParams(0, 0, 0, 0, icon_size, icon_size));
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawable_listener.retrieve_drawable(my_drawable);
                    selector_parent.removeView(layout);
                }
            });
            drawable_layout.addView(image);
        }
        LinearLayout current_horizontal_layout = null;
        for(int i = 0; i < drawable_layouts.size(); i++){
            if(i % 4 == 0){
                current_horizontal_layout = new LinearLayout(context);
                current_horizontal_layout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
                vertical_layout.addView(current_horizontal_layout);
            }
            current_horizontal_layout.addView(drawable_layouts.get(i));
        }
        return vertical_layout;
    }


}
