package com.example.journal.Custom_Layout.Icon_Layout;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Debug;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.journal.Custom_Layout.Compute_Timer;
import com.example.journal.Custom_Layout.Data_Structure.Drawable_With_Data;
import com.example.journal.Custom_Layout.Data_Structure.IconLocationStruct;
import com.example.journal.Custom_Layout.Describer.Icon_Describer;
import com.example.journal.Custom_Layout.Utility.Image_Processing;
import com.example.journal.R;
import com.example.journal.Custom_Layout.Utility.ViewFactory;
import com.google.android.material.internal.ClippableRoundedCornerLayout;

public class Icon {
    public static int trace_count = 1;
    public static boolean journal_mode = true;

    ViewGroup view;
    TextView textView;
    private Row_Layout row;
    static int count = 1;
    int class_id;
    public int journal_describer_id;
    private String title;
    Context context;
    int wiggle = 50;
    final Icon this_icon = this;

    float position_x = 0;
    float position_y = 0;

    float layout_position_x = 0;
    float layout_position_y = 0;

    Drawable white_rounded_square;
    Drawable black_rounded_square;
    Drawable_With_Data drawable_with_data;
    boolean is_white;
    ImageView background_rounded_square;

    Icon_Debugger icon_debugger;
    Group_Manager manager;
    String entry_text = null;
    ImageView image_view;


    @SuppressLint("RestrictedApi")
    public Icon(Icon_Describer icon_describer, Group_Manager manager, Context context, Drawable_With_Data drawable_with_data){
        System.out.println("\t<icon> creating icon: " + icon_describer.title);
        this.manager = manager;
        journal_describer_id = -1;
        this.drawable_with_data = drawable_with_data;
        this.context = context;
        class_id = count;
        title = icon_describer.title;
        count++;
        view = (ConstraintLayout) LayoutInflater.from(context).inflate(R.layout.image_template, null);
        image_view = ((ImageView)view.getChildAt(0));
        textView = (TextView) view.getChildAt(1);
        view.bringChildToFront(image_view);
        if(drawable_with_data.drawable == null){
            System.out.println("null drawable: " + drawable_with_data.id);
        }
        image_view.setImageDrawable(drawable_with_data.drawable);
        ConstraintLayout.LayoutParams image_params = ViewFactory.createLayoutParams(10, -1, 0, 0, Image_Processing.icon_picture_width, Image_Processing.icon_picture_width);
        image_view.setLayoutParams(image_params);
        //image_view.setVisibility(View.INVISIBLE);

        set_drag_listener();

        int shadow_size = 10;

        Bitmap circle = Image_Processing.circle(Image_Processing.icon_picture_width, shadow_size, Color.WHITE);
        white_rounded_square = new BitmapDrawable(context.getResources(), circle);
        circle = Image_Processing.circle(Image_Processing.icon_picture_width, shadow_size, Color.BLACK);
        black_rounded_square = new BitmapDrawable(context.getResources(), circle);
        background_rounded_square = new ImageView(context);
        background_rounded_square.setImageDrawable(black_rounded_square);
        is_white = false;
        //circle_view.setImageDrawable(context.getDrawable(R.drawable.cow));
        background_rounded_square.setLayoutParams(ViewFactory.createLayoutParams(0, -1, 0, 0, Image_Processing.icon_picture_width+shadow_size*2, Image_Processing.icon_picture_width+shadow_size*2));
        view.addView(background_rounded_square);

        //System.out.println("\n\n\nbackground fade dimensions: "+circle.getWidth() + ", " + circle.getHeight());

        //edit_mode();
        make_black();


        //roundedCornerLayout.setBackgroundColor(Color.BLACK);
        setup_text();
    }

    public static void edit_mode(){
        journal_mode = false;
    }

    public static void journal_mode(){
        journal_mode = true;
    }

    @SuppressLint("RestrictedApi")
    private void insert_image_into_rounded_layout(ImageView image_view){
        @SuppressLint("RestrictedApi") ClippableRoundedCornerLayout roundedCornerLayout = new ClippableRoundedCornerLayout(context);
        view.removeView(image_view);
        roundedCornerLayout.addView(image_view);
        view.addView(roundedCornerLayout);
        roundedCornerLayout.setLayoutParams(ViewFactory.createLayoutParams(0, 0, 0, 0, -1, -1));
        View black = new View(context);
        roundedCornerLayout.addView(black);
        black.setBackgroundColor(Color.BLACK);
        roundedCornerLayout.setElevation(100);
        roundedCornerLayout.updateClipBoundsAndCornerRadius(20, 10, 180, 160, 30);
    }

    public String get_title(){
        return title;
    }

    public void set_title(String title){
        System.out.println("<icon> setting title from: " + this.title + ", to: " + title);
        this.title = title;
        setup_text();
        System.out.println("<icon> my title is now: " + this.title);
    }

    private void setup_text(){
        textView.setText(title);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setTextSize(10);
        textView.setElevation(100);
    }

    public void add_to_row(Row_Layout row){
        if(this.row != null)
            throw new RuntimeException();
        this.row = row;
        icon_debugger = row.group.manager.icon_debugger;
    }

    public void remove_from_row(){
        row = null;
    }

    public Row_Layout get_row(){
        return row;
    }

    public void vibrate(Context context){
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        if (vibrator.hasVibrator()) { // check if the hardware has a vibrator
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                // For API level 26 and above
                // Create a vibration effect
                // You can pass VibrationEffect.DEFAULT_AMPLITUDE to play the default vibration strength
                VibrationEffect effect = VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE);
                // Vibrate with the given effect.
                vibrator.vibrate(effect);
            } else {
                // For API level below 26
                // The passed parameter is the number of milliseconds for which to vibrate
                vibrator.vibrate(50);
            }
        } else {
            // the device does not have a vibrator
        }
    }





    public IconLocationStruct get_position(){
        return new IconLocationStruct(row.group, row.group.icons.indexOf(this));
    }

    public void set_wiggle_position(int x, int y){
        long start = Compute_Timer.get_time_micro();
        //System.out.println("doing wiggle: " + x + ", " + y);
        float x_sign = (Math.abs(x)/(x*1.0f));
        float y_sign = (Math.abs(y)/(y*1.0f));
        if(x > -0.01f && x < 0.01f)
            x_sign = 1.0f;
        if(y > -0.01f && y < 0.01f)
            y_sign = 1.0f;
        //System.out.println("signs: " + x_sign + ", " + y_sign);
        float x_factor = Math.abs((x * 1.0f)/width()) - 0.0f;
        float y_factor = Math.abs((y * 1.0f)/width()) - 0.0f;
        if(x_factor < 0)
            x_factor = 0;
        else
            x_factor = x_factor * x_sign;
        if(y_factor < 0)
            y_factor = 0;
        else
            y_factor = y_factor * y_sign;

        //System.out.println("factors: "+ x_factor+", " + y_factor);
        float x_wiggle = wiggle*x_sign*(1.0f - 1.0f/(1.0f+Math.abs(x_factor)));
        float y_wiggle = wiggle*y_sign*(1.0f - 1.0f/(1.0f+Math.abs(y_factor)));
        //System.out.println("wiggle amount: " + x_wiggle + ", " + y_wiggle);
        float x_cord = position_x + x_wiggle;
        float y_cord = position_y + y_wiggle;
        //System.out.println("wiggle cord: " + x_cord + ", " + y_cord);

        //System.out.println("animate: (" + view.getX()+", "+view.getY()+"), ("+(view.getX()+x_cord)+", "+(view.getY()+y_cord));
        //animate_view(view.getX(), view.getY(), view.getX() + x_cord, view.getY() + y_cord);
        view.setX(x_cord);
        view.setY(y_cord);
        long end = Compute_Timer.get_time_micro();
        icon_debugger.log_wiggle(start, end);
    }

    public float get_cursor_distance_from_center(float cord){
        float x_factor = Math.abs((cord * 1.0f - width()/2)/width());
        return x_factor;
    }

    public float get_sign(float num){
        float x_sign = (Math.abs(num)/(num*1.0f));
        if(num > -0.01f && num < 0.01f)
            x_sign = 1.0f;
        return x_sign;
    }
    Drag drag = null;
    public void set_drag_listener(){
        view.setClickable(true);
        view.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")

            public boolean onTouch(View v, MotionEvent event) {
                System.out.println("<icon> on touch");
                //if we are not editing, do not do drag
                if(journal_mode){
                    System.out.println("<icon> finished on touch");
                    return false;
                }

                if(event.getActionMasked() == MotionEvent.ACTION_DOWN){
                    if(drag != null){
                        if(!drag.finished){
                            System.out.println("icon recieved action down and previous drag still running");
                            //throw new RuntimeException();
                        }
                    }
                    row.parent().start_drag(this_icon, event);
                }

                //return drag.consume_event(event);
                if(event.getActionMasked() != MotionEvent.ACTION_DOWN)
                    drag.consume_event(event);
                System.out.println("<icon> finished on touch");
                return true;
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Debug.stopMethodTracing();
                //Debug.startMethodTracing("trace " + trace_count);
                //trace_count++;
                System.out.println("<icon> view got clicked");
                if(manager.sheet != null)
                    if(manager.sheet.sheet_is_open){
                        manager.sheet.click_while_open();
                        System.out.println("<icon> finished on click");
                        return;
                    }
                if(manager.edit_mode)
                    manager.open_sheet(this_icon);
                //if we are journal, allow icon clicks
                if(journal_mode){
                    perform_icon_click();
                }
                System.out.println("<icon> finished on click");
            }
        });

    }

    public void perform_icon_click(){
        if(is_white){
            make_black();
            return;
        }
        make_white();


    }

    public void make_black(){
        is_white = false;
        background_rounded_square.setImageDrawable(black_rounded_square);

        //dimNonTransparentParts(view, 0.5f);
        addDimOverlay(view, 0.5f);
        return;
    }

    public void make_white(){
        System.out.println("<icon "+journal_describer_id+"> making white");
        is_white = true;
        //do before in case I want to use the black background for the edit screen
        addDimOverlay(view, 0.0f);
        background_rounded_square.setImageDrawable(white_rounded_square);

        System.out.println("icon: opening sheet");
        manager.open_sheet(this);

    }

    public void addDimOverlay(ViewGroup viewGroup, float dimAmount) {
        // Create and add the overlay
//        DimOverlay overlay = new DimOverlay(viewGroup.getContext(), null);
//        viewGroup.addView(overlay, new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT));

        // Dim the children
        for (int i = 0; i < viewGroup.getChildCount() - 1; i++) {
            View child = viewGroup.getChildAt(i);
            child.setAlpha(1 - dimAmount);
        }
    }

    public void dimNonTransparentParts(ViewGroup viewGroup, float dimAmount) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);

            if (child instanceof ViewGroup) {
                // Recursively dim the children
                dimNonTransparentParts((ViewGroup) child, dimAmount);
            } else {
                Drawable drawable = child.getBackground();
                if (drawable != null) {
                    // Only dim non-transparent parts
                    drawable.setColorFilter(new PorterDuffColorFilter(
                            Color.argb(Math.round(dimAmount * 255), 0, 0, 0),
                            PorterDuff.Mode.SRC_ATOP
                    ));
                }
            }
        }
    }

    public void set(float x, float y){
        position_x = x;
        position_y = y;
        animate_view(view.getX(), view.getY(), x, y);
    }

    public void set_layout_position(float x, float y){
        layout_position_x = x;
        layout_position_y = y;
        set(x, y);
    }

    public float raw_true_position_x(){
        ViewGroup layout = (ViewGroup)view.getParent();
        int[] layout_raw = new int[2];
        layout.getLocationOnScreen(layout_raw);
        return layout_raw[0] + position_x;
    }

    public float raw_true_position_y(){
        ViewGroup layout = (ViewGroup)view.getParent();
        int[] layout_raw = new int[2];
        layout.getLocationOnScreen(layout_raw);
        return layout_raw[0] + position_y;
    }



    int new_position;

    boolean moving = false;
    public int width(){
        return Image_Processing.icon_draw_width;
    }

    boolean x_moved;
    boolean y_moved;
    AnimatorSet waiting_animation = null;

    float starting_x, starting_y, destX, destY;
    int time;
    boolean set_animator = false;

    boolean do_custom_animation = true;

    public void animate_view(float starting_x, float starting_y, float destX, float destY){
        view.postOnAnimation(new Runnable() {
            @Override
            public void run() {
                synchronized (this_icon){

                    custom_animation();

                }

            }
        });


    }

    public float distance(float x1, float y1, float x2, float y2){
        return (float)Math.sqrt(Math.pow( Math.abs(x1 - x2), 2 ) + Math.pow( Math.abs(y1 - y2), 2 ));
    }

    boolean animate = true;
    boolean currently_animating = false;
    long last_time_animation = 0;
    float velocity_x = 0;
    float velocity_y = 0;

    public synchronized void acceleration_based_animation(){
        float acceleration = 50;
        float braking = 100;
        long current_time = System.currentTimeMillis();
        long elapsed = current_time - last_time_animation;
        last_time_animation = current_time;

        float distance_x = (position_x - view.getX());
        float distance_y = (position_y - view.getY());
        float distance = distance(view.getX(), view.getY(), position_x, position_y);

        float braking_needed_x = braking_needed(distance_x, velocity_x);
        float braking_needed_y = braking_needed(distance_y, velocity_y);
        float acceleration_possible = get_amount_can_accelerate(elapsed, acceleration, braking, braking_needed_x, braking_needed_y);
        if(acceleration_possible < 0.1){
            float total_braking_needed = braking_needed_x + braking_needed_y;
            float factor = (braking * elapsed) / total_braking_needed;
            float final_breaking_x = braking_needed_x * factor;
            float final_breaking_y = braking_needed_y * factor;
            velocity_x -= final_breaking_x;
            velocity_y -= final_breaking_y;
        }



    }

    public synchronized void custom_animation(){
        long start = Compute_Timer.get_time_micro();
        int animation_factor = 10;

        float distance_x = (position_x - view.getX());
        float distance_y = (position_y - view.getY());
        float distance = distance(view.getX(), view.getY(), position_x, position_y);

        float animation_travel_x = distance_x/animation_factor;
        float animation_travel_y = distance_y/animation_factor;

        synchronized (this){
            if(currently_animating)
                return;
            if(distance > 10){
                view.setX(view.getX() + animation_travel_x);
                view.setY(view.getY() + animation_travel_y);
                currently_animating = true;
                view.postOnAnimation(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (this_icon){
                            currently_animating = false;
                        }
                        custom_animation();
                    }
                });
            }else{
                last_time_animation = 0;
                velocity_x = 0;
                velocity_y = 0;
                view.setX(position_x);
                view.setY(position_y);
            }
        }
        if(currently_animating)
            return;



        long end = Compute_Timer.get_time_micro();
        icon_debugger.log_animation(start, end);
    }

    public float braking_needed(float distance, float velocity){
        float time_no_brake = distance/velocity;
        float time_with_perfect_brake = time_no_brake*2;
        float braking_needed = velocity/time_with_perfect_brake;
        return braking_needed;
    }

    public static float get_time(float distance, float a, float v){
        float distance_from_deaccelerating = 0;
        float time_from_deaccelerating = 0;
        if(distance * v < 0){
            distance_from_deaccelerating = (v*v) / (2*a);
            time_from_deaccelerating = Math.abs(v) / (2*a);
        }
        float total_distance = distance_from_deaccelerating + Math.abs(distance);
        float time_from_accelerating = 2.0f*((float)Math.sqrt((4.0f/3.0f)*(Math.abs(total_distance)/a)));
        float total_time = time_from_accelerating+time_from_deaccelerating;
        System.out.println("time from braking: " + time_from_deaccelerating);
        System.out.println("time from accel: " + time_from_accelerating);
        System.out.println("distance from braking: " + distance_from_deaccelerating);
        return total_time;
    }

    public float get_amount_can_accelerate(float elapsed, float acceleration, float braking, float braking_needed_x, float braking_needed_y){
        float amount_accelerating_available = elapsed * acceleration;
        float amount_braking_available = elapsed * braking;
        float acceleration_left = amount_braking_available - 10 - (braking_needed_x + braking_needed_y);
        if(braking_needed_x + braking_needed_y < amount_braking_available - 10){
            if(acceleration_left > amount_accelerating_available)
                acceleration_left = amount_accelerating_available;
        }else
            acceleration_left = 0;
        return acceleration_left;
    }

    public void set_animator(){
        view.setX(starting_x);
        view.setY(starting_y);
        //ObjectAnimator animationX = ObjectAnimator.ofFloat(view, "translationX", new float[]{starting_x, destX});
        //ObjectAnimator animationY = ObjectAnimator.ofFloat(view, "translationY", new float[]{starting_y, destY});
        ObjectAnimator animationX = ObjectAnimator.ofFloat(view, "translationX", destX);
        ObjectAnimator animationY = ObjectAnimator.ofFloat(view, "translationY", destY);
        animationX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                x_moved = true;
                if(y_moved){
                    if(waiting_animation != null){
                        view.clearAnimation();
                        waiting_animation.start();
                        x_moved = false;
                        y_moved = false;
                        waiting_animation = null;
                    }
                }
            }
        });
        animationY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                y_moved = true;
                if(x_moved){
                    if(waiting_animation != null){
                        view.clearAnimation();
                        waiting_animation.start();
                        x_moved = false;
                        y_moved = false;
                        waiting_animation = null;
                    }
                }
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animationX, animationY);
        animatorSet.setDuration(time);  // Set your duration. This is set for 1 second.
        if(view.getAnimation() != null){
            waiting_animation = animatorSet;
        }
        else
            animatorSet.start();
    }

    public void set_drawable(Drawable_With_Data drawable_with_data) {
        image_view.setImageDrawable(drawable_with_data.drawable);
        this.drawable_with_data = drawable_with_data;
    }
}
