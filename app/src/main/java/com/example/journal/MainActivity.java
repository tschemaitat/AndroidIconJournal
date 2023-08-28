package com.example.journal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.Manifest;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.journal.Custom_Layout.Custom_View.LockableScrollView;
import com.example.journal.Custom_Layout.Icon_Layout.Calendar_Page;
import com.example.journal.Custom_Layout.Icon_Layout.Page;
import com.example.journal.Custom_Layout.Listener.Page_Change_Listener;
import com.example.journal.Custom_Layout.Utility.Drawable_Manager;
import com.example.journal.Custom_Layout.Utility.File_Utilities;
import com.example.journal.Custom_Layout.Icon_Layout.Group_Manager;
import com.example.journal.Custom_Layout.Utility.ViewFactory;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements Page_Change_Listener {

    //add view to layout
    //add input text to layout
    Context context;
    ConstraintLayout main_layout;
    Group_Manager displayed_icons;
    Calendar date_for_entry = null;
    private static final int REQUEST_WRITE_STORAGE = 112;
    private static final int REQUEST_READ_STORAGE = 113;
    //Calendar entry_page_date;
    int button_count = 0;

    Page current_page;
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

        File_Utilities.print_files(context);

        change_page(new Calendar_Page());
    }

    public static LockableScrollView make_custom_scroll(Context context){
        LockableScrollView scroll = new LockableScrollView(context);
        ViewGroup scroll_linearLayout = new LinearLayout(context);
        scroll.addView(scroll_linearLayout);
        return scroll;
    }

    public static ScrollView make_scroll(Context context){
        ScrollView scroll = new ScrollView(context);
        ViewGroup scroll_linearLayout = new LinearLayout(context);
        scroll.addView(scroll_linearLayout);
        return scroll;
    }

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
    }

    public static ConstraintLayout inflate_page(Context context, int layout_id){
        ConstraintLayout edit_layout = (ConstraintLayout) LayoutInflater.from(context).inflate(layout_id, null);
        edit_layout.setLayoutParams(ViewFactory.createLayoutParams(0, 0, 0, 0, -1, -1));
        return edit_layout;
    }


    //prints hierarchy of views
    //usually called with 0 initial tabs
    public static void print_view(View view, int num_tabs){
        //prints the number of tabs
        for(int tab = 0; tab < num_tabs; tab++)
            System.out.print("\t");
        System.out.println(view + ", ");
        //if the view cannot have children, return
        if(!(view instanceof ViewGroup))
            return;
        ViewGroup group = (ViewGroup) view;
        //print the children with a tab
        for(int i = 0; i < group.getChildCount(); i++){
            View child = group.getChildAt(i);
            print_view(child, num_tabs + 1);
        }
    }

    @Override
    public void change_page(Page page) {
        System.out.println("\t<main activity>changing page");
        if(current_page != null)
            current_page.on_close();
        main_layout.removeAllViews();
        current_page = page;
        current_page.start(this, this);
        main_layout.addView(current_page.get_view());
    }
}