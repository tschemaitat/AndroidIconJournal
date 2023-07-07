package com.example.journal.Custom_Layout.Utility;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class ViewFactory {
    public static View fill_rectangle(AppCompatActivity activity, int color){
        return new View(activity){
            @Override
            protected void onDraw(Canvas canvas) {
                int padding = 0;

                System.out.println("bounds: " + canvas.getClipBounds());
                float radius = 20.0f;


                int left = padding;//getLeft() + 50;
                int top = padding;//getTop() + 50;
                int right = getRight() - getLeft() - 2*padding;
                int bottom = getBottom() - getTop() - 2*padding;

                Paint paint_fill_blue = paint_fill(color);

                RectF rect_blue = new RectF(left, top, right, bottom);//(int)(getRight()-getLeft()-2*thickness), (int)(getBottom()-getTop()-2*thickness));
                canvas.drawRect(rect_blue, paint_fill_blue);}

        };
    }

    public static Paint paint_fill(int color){
        Paint paint = new Paint();
        paint.setColor(color);
        //paint.setAlpha(128);
        return paint;
    }

    public static Paint paint_stroke(int color, int thickness){
        Paint paint_stroke = new Paint();
        paint_stroke.setStyle(Paint.Style.STROKE);
        paint_stroke.setStrokeWidth(thickness);
        paint_stroke.setColor(color);
        //paint_stroke.setAlpha(128);
        return paint_stroke;
    }

    public static View rectangle(AppCompatActivity activity, int color, int thickness){
        return new View(activity){
            @Override
            protected void onDraw(Canvas canvas) {
                int padding = 0;

                System.out.println("bounds: " + canvas.getClipBounds());
                float radius = 20.0f;


                int left = padding + thickness/2;//getLeft() + 50;
                int top = padding + thickness/2;//getTop() + 50;
                int right = getRight() - getLeft() - 2*padding - thickness/2;
                int bottom = getBottom() - getTop() - 2*padding - thickness/2;

                Paint paint_fill_blue = paint_stroke(color, thickness);

                RectF rect_blue = new RectF(left, top, right, bottom);//(int)(getRight()-getLeft()-2*thickness), (int)(getBottom()-getTop()-2*thickness));
                canvas.drawRect(rect_blue, paint_fill_blue);
            }

        };
    }

    public static int get_width_of_screen(AppCompatActivity activity){
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        return screenWidth;
    }

    public static ConstraintLayout construct_camera_layout(ConstraintLayout main_layout, AppCompatActivity activity){
        int background_color2 = 0;
        int background_color1 = 0;
        int width = get_width_of_screen(activity);

        int border_color = Color.BLACK;

        ConstraintLayout camera_layout = new ConstraintLayout(activity);
        camera_layout.setLayoutParams(createLayoutParams(0, 0, 0, 0, -1, -1));
        main_layout.addView(camera_layout);
        //camera_layout.setVisibility(View.INVISIBLE);
        int camera_vertical_start = 200;
        int camera_width = (int)(width*0.8);
        int camera_height = (int)(camera_width * 0.62);
        int camera_padding = 100;
        int number_spinner = 4;


        View background = fill_rectangle(activity, background_color2);
        background.setLayoutParams(createLayoutParams(0, 0, 0, 0, -1, -1));
        camera_layout.addView(background);

        ScrollView scroll = new ScrollView(activity);
        scroll.setLayoutParams(createLayoutParams(200, 0, 100, 100, -1, -1));
        camera_layout.addView(scroll);

        ConstraintLayout scroll_container = new ConstraintLayout(activity);
        scroll.addView(scroll_container);

        //array of 4 layouts
        //each layout
        //Video View
        //button

        //video view = findview


        //array of 4 layouts
        //each layout
        //video view.copy
        //button



        ConstraintLayout[] layouts = new ConstraintLayout[number_spinner];
        for(int i = 0; i < layouts.length; i++){
            ConstraintLayout constraintLayout_camera1 = new ConstraintLayout(activity);
            constraintLayout_camera1.setLayoutParams(createLayoutParams(100 + (camera_height + camera_padding) * i, -1, 0, 0, camera_width, camera_height));
            scroll_container.addView(constraintLayout_camera1);
            layouts[i] = constraintLayout_camera1;
        }

        for(int i = 0; i < layouts.length; i++){
            View border = rectangle(activity, border_color, 15);
            border.setLayoutParams(createLayoutParams(0, 0, 0, 0, -1, -1));
            View fill = fill_rectangle(activity, background_color1);
            fill.setLayoutParams(createLayoutParams(0, 0, 0, 0, -1, -1));
            layouts[i].addView(fill);
            layouts[i].addView(border);

            Button settings = new Button(activity);
            settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            settings.setLayoutParams(createLayoutParams(50, -1, 50, -1, 100, 100));
            layouts[i].addView(settings);
        }
        return camera_layout;
    }

    public static View round_rectangle(AppCompatActivity activity, int color){
        return new View(activity){
            @Override
            protected void onDraw(Canvas canvas) {
                float radius = 20.0f;
                Paint paint = new Paint();
                paint.setColor(color);
                paint.setAlpha(128);
                System.out.println("round: " );
                System.out.println(getBottom() - 1 * radius);
                //public void drawRoundRect(float left, float top, float right, float bottom, float rx, float ry, @NonNull Paint paint)
                canvas.drawRoundRect(0, 0, getRight() - getLeft(), getBottom() - getTop(), radius, radius, paint);
            }

        };
    }

    public static void set_entries(ArrayList<String> entries, Spinner spinner, Context context){
        String[] final_entries = new String[entries.size()];
        for(int j = 0; j < entries.size(); j++)
            final_entries[j] = entries.get(j);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                context, android.R.layout.simple_spinner_item, final_entries );
        spinner.setAdapter(spinnerArrayAdapter);
    }







    public static ConstraintLayout.LayoutParams createLayoutParams(
            int top, int  bottom, int right, int left,
            int width, int height
    ){
        // Create a new ConstraintLayout.LayoutParams object
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );

        // Set the view's layout parameters

        // Set the constraints for the view
        if(left != -1)
            layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        if(top != -1)
            layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        if(right != -1)
            layoutParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
        if(bottom != -1)
            layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;

        // Set the view's margin
        int margin = 16; // in pixels
        layoutParams.setMargins(left, top, right, bottom);
        layoutParams.width = width;
        layoutParams.height = height;
        return layoutParams;
    }
}

