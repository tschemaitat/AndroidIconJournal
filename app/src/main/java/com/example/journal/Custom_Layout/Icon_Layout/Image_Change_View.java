package com.example.journal.Custom_Layout.Icon_Layout;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.journal.Custom_Layout.Data_Structure.Drawable_With_Data;
import com.example.journal.Custom_Layout.Listener.Drawable_Listener;
import com.example.journal.Custom_Layout.Utility.ViewFactory;
import com.example.journal.MainActivity;

public class Image_Change_View {
    Drawable_With_Data drawable;
    Context context;
    LinearLayout parent;
    ConstraintLayout selector_parent;
    Drawable_Listener drawable_listener;
    ConstraintLayout layout;
    ImageView image;
    public Image_Change_View(Drawable_With_Data drawable, Context context, LinearLayout parent, ConstraintLayout selector_parent, Drawable_Listener drawable_listener){
        this.drawable = drawable;
        this.context = context;
        this.parent = parent;
        this.selector_parent = selector_parent;
        this.drawable_listener = drawable_listener;
        construct_view();
    }

    private void construct_view(){
        layout = new ConstraintLayout(context);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200));
        parent.addView(layout);
        parent.bringChildToFront(layout);
        image = new ImageView(context);
        image.setImageDrawable(drawable.drawable);
        layout.addView(image);
        image.setLayoutParams(ViewFactory.createLayoutParams(0, 0, -1, 20, ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
        Button button = new Button(context);
        button.setLayoutParams(ViewFactory.createLayoutParams(0, 0, 200, -1, ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
        button.setText("change image");
        layout.addView(button);
        Drawable_Listener change_view_drawable_listener = new Drawable_Listener() {
            @Override
            public void retrieve_drawable(Drawable_With_Data drawable_with_data) {
                if(drawable_with_data == null){
                    return;
                }
                set_image(drawable_with_data);
                drawable_listener.retrieve_drawable(drawable_with_data);
            }
        };
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Image_Selector selector = new Image_Selector(selector_parent, context, change_view_drawable_listener);
                MainActivity.print_view(selector_parent, 0);
                selector_parent.requestLayout();
            }
        });
    }

    private void set_image(Drawable_With_Data drawable){
        this.image.setImageDrawable(drawable.drawable);
    }
}
