package com.example.journal.Custom_Layout.Icon_Layout;



import android.content.Context;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.journal.Custom_Layout.Custom_View.LockableScrollView;
import com.example.journal.Custom_Layout.Data_Structure.Drawable_With_Data;
import com.example.journal.Custom_Layout.Listener.Drawable_Listener;
import com.example.journal.Custom_Layout.Listener.String_Listener;
import com.example.journal.Custom_Layout.Utility.ViewFactory;
import com.example.journal.MainActivity;

public class Bottom_Sroll_Sheet {
    public boolean sheet_is_open = false;
    ConstraintLayout root_view;
    LinearLayout inner_view;
    Context context;
    LockableScrollView scroll;
    Icon icon;
    Group_Manager manager;
    ViewGroup parent;

    int min_height = 250;
    int height = min_height;
    public Bottom_Sroll_Sheet(Context context, Icon icon, Group_Manager manager){
        this.manager = manager;
        this.icon = icon;
        this.context = context;
        root_view = new ConstraintLayout(context);
        root_view.setLayoutParams(ViewFactory.createLayoutParams(-1, 0, 0, 0, ConstraintLayout.LayoutParams.MATCH_PARENT, height));
        root_view.setBackgroundColor(Color.BLUE);
        scroll = new LockableScrollView(context);
        scroll.setLayoutParams(ViewFactory.createLayoutParams(0, -1, 0, 0, ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));
        root_view.addView(scroll);
        inner_view = new LinearLayout(context);
        inner_view.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(inner_view);
//        inner_view = new ConstraintLayout(context);
//        vertical.addView(inner_view);
        add_handle();

        TextView text = new TextView(context);
        text.setText("hello");
        text.setTextSize(100f);
        text.setHeight(500);
        //inner_view.addView(text);
        //inject_title_text();

        System.out.println("end of sheet constructor");
    }
    int title_text_size = 30;

    public static Bottom_Sroll_Sheet make_sheet(ViewGroup parent, Context context, Icon icon, Group_Manager manager) {

        Bottom_Sroll_Sheet sheet = new Bottom_Sroll_Sheet(context, icon, manager);
        parent.addView(sheet.root_view);
        sheet.parent = parent;
        return sheet;
    }

    public void inject_icon_content(Icon icon){
        inject_spacer(50);
        System.out.println("<sheet>: injecting icon content");
        clear_content();
        String title = icon.get_title();
        String text = icon.entry_text;
        inject_title_text(title);
        MainActivity.print_view(inner_view, 0);
    }

    public void inject_spacer(int space){

        View view = new View(context);
        view.setLayoutParams(new LinearLayout.LayoutParams(0, space));
        inner_view.addView(view);
    }

    public void clear_content(){
        inner_view.removeAllViews();
    }



    public void inject_icon_settings(Icon icon){
        System.out.println("<sheet> injecting settings content");
        this.icon = icon;
        inject_spacer(50);
        String title = icon.get_title();
        inject_title_editor(title);
        Drawable_Listener drawable_listener = new Drawable_Listener() {
            @Override
            public void retrieve_drawable(Drawable_With_Data drawable_with_data) {
                icon.set_drawable(drawable_with_data);
            }
        };
        Image_Change_View image_change_view = new Image_Change_View(icon.drawable_with_data, context, inner_view, (ConstraintLayout) parent, drawable_listener);



        System.out.println("<sheet> injecting settings content finished");
    }

    public void inject_title_text(String text){
        TextView tvEditable = new TextView(context);
        tvEditable.setTextSize(title_text_size);
        tvEditable.setText(text);
        inner_view.addView(tvEditable);

    }

    public void inject_title_editor(String text){
        String_Listener listener = new String_Listener() {
            @Override
            public void set_string(String string) {
                System.out.println("(icon: "+icon.journal_describer_id+")setting string: " + string);
                icon.set_title(string);
            }
        };
        Edit_Text edit_text = new Edit_Text(listener, text, 20, context);
        edit_text.get_root().setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 250));
        inner_view.addView(edit_text.get_root());

//        EditText etEditable = new EditText(context);
//        etEditable.setTextSize(title_text_size);
//        etEditable.setText(text);
//        inner_view.addView(etEditable);
//        etEditable.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                // If EditText loses focus, hide EditText and show TextView with new text
//                if (!hasFocus) {
//                    icon.set_title(etEditable.getText().toString());
//
//                    // Hide keyboard
//                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                }
//            }
//        });
    }





    public void setup_title_text_with_edit(){

        View view = new View(context);
        view.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
        //view.setLayoutParams(ViewFactory.createLayoutParams(0, 0, 0, 0, 100, 100));
        inner_view.addView(view);
        ConstraintLayout title_layout = new ConstraintLayout(context);
        inner_view.addView(title_layout);
        //title_layout.setLayoutParams(ViewFactory.createLayoutParams(0, 0, 0, 0, ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
        TextView tvEditable = new TextView(context);
        tvEditable.setTextSize(title_text_size);
        tvEditable.setText("my title!");
        EditText etEditable = new EditText(context);
        etEditable.setVisibility(View.INVISIBLE);
        etEditable.setTextSize(title_text_size);
        title_layout.addView(tvEditable);
        title_layout.addView(etEditable);

        tvEditable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide TextView, show EditText with current TextView's text
                etEditable.setText(tvEditable.getText());
                tvEditable.setVisibility(View.INVISIBLE);
                etEditable.setVisibility(View.VISIBLE);
                etEditable.requestFocus();
                // Show keyboard
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etEditable, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        etEditable.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // If EditText loses focus, hide EditText and show TextView with new text
                if (!hasFocus) {
                    tvEditable.setText(etEditable.getText());
                    etEditable.setVisibility(View.INVISIBLE);
                    tvEditable.setVisibility(View.VISIBLE);
                    // Hide keyboard
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });

        // Allow saving EditText content by pressing 'Enter'
        etEditable.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_ENTER:
                            etEditable.clearFocus();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
    }

    public int get_max_height(){

        int max_height = inner_view().getHeight();
        if(max_height > 400)
            return max_height;
        else
            return 400;
    }
    public void set_height(int new_height){
        if(new_height > get_max_height()){
            int difference = new_height - get_max_height();
            height = get_max_height() + (200 * difference) / (200 + difference);
        }else if(new_height < min_height){
            int difference = min_height - new_height;
            height = min_height - (60 * difference) / (60 + difference);
        }
        else
            height = new_height;
        root_view.setLayoutParams(ViewFactory.createLayoutParams(-1, 0, 0, 0, ConstraintLayout.LayoutParams.MATCH_PARENT, height));
        System.out.println("height set to: " + height);
    }

    private void rubber_set_height(int new_height, int max_height){
        if(new_height < max_height)
            height = max_height;
        else if(new_height < min_height)
            height = min_height;
        else
            height = new_height;
        root_view.setLayoutParams(ViewFactory.createLayoutParams(-1, 0, 0, 0, ConstraintLayout.LayoutParams.MATCH_PARENT, height));
    }

    public void configure_layout_height(){
        root_view.setLayoutParams(ViewFactory.createLayoutParams(-1, 0, 0, 0, ConstraintLayout.LayoutParams.MATCH_PARENT, height));
    }

    public void click_while_open(){
        System.out.println("background was clicked while sheet was open");
        close();
        sheet_is_open = false;
    }

    public void setup_rubber_banding(int rubber_max_height){
        if(height > rubber_max_height){
            root_view.postOnAnimation(new Runnable() {
                @Override
                public void run() {
                    int new_height = (int)(rubber_max_height + (height - rubber_max_height)/1.2 - 1);
                    rubber_set_height(new_height, rubber_max_height);
                    setup_rubber_banding(rubber_max_height);
                }
            });
        }
    }

    public void setup_rubber_band_up(int rubber_min_height){
        if(height < rubber_min_height){
            root_view.postOnAnimation(new Runnable() {
                @Override
                public void run() {
                    int new_height = (int)(rubber_min_height - ((rubber_min_height - height)/1.2 - 1));
                    if(new_height > min_height)
                        height = min_height;
                    else
                        height = new_height;
                    configure_layout_height();
                    setup_rubber_band_up(rubber_min_height);
                }
            });
        }
    }

    public void close(){
//        ViewGroup parent = (ViewGroup)root_view.getParent();
//        parent.removeView(root_view);
        sheet_is_open = false;
        setup_rubber_banding(min_height);
    }
    Handle_Drag handle_drag = null;
    public void add_handle(){
        View bar = new View(context);
        bar.setBackgroundColor(Color.BLACK);
        bar.setLayoutParams(ViewFactory.createLayoutParams(20, 0, 0, 0, 50, 5));
        View shadow = new View(context);
        shadow.setAlpha(0.5f);
        shadow.setBackgroundColor(Color.BLACK);
        shadow.setLayoutParams(ViewFactory.createLayoutParams(20, 0, 0, 0, ConstraintLayout.LayoutParams.MATCH_PARENT, 50));
        ConstraintLayout handle_layout = new ConstraintLayout(context);
        handle_layout.setLayoutParams(ViewFactory.createLayoutParams(-1, -1, -1, -1, ConstraintLayout.LayoutParams.MATCH_PARENT, 90));
        handle_layout.addView(bar);
        handle_layout.addView(shadow);
        root_view.addView(handle_layout);
        Bottom_Sroll_Sheet sheet = this;
        handle_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    handle_drag = new Handle_Drag(sheet);
                    handle_drag.handle_touch_event(event);
                    scroll.setScrollingEnabled(false);
                    sheet_is_open = true;
                }
                handle_drag.handle_touch_event(event);
                if(event.getAction() == MotionEvent.ACTION_UP){
                    handle_drag = null;
                    scroll.setScrollingEnabled(true);
                    if(height > get_max_height())
                        setup_rubber_banding(get_max_height());
                    else if(height < 350) {
                        setup_rubber_band_up(min_height);
                    }
                }
                return true;
            }
        });
    }

    public View get_root(){
        return root_view;
    }

    public ViewGroup inner_view(){
        return inner_view;
    }
}
