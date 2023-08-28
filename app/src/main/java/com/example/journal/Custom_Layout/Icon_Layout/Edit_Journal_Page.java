package com.example.journal.Custom_Layout.Icon_Layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.journal.Custom_Layout.Describer.Journal_Describer;
import com.example.journal.Custom_Layout.Describer.User_Journals;
import com.example.journal.Custom_Layout.Listener.Page_Change_Listener;
import com.example.journal.MainActivity;
import com.example.journal.R;

import java.util.Calendar;
import java.util.HashMap;

public class Edit_Journal_Page extends Page{
    ConstraintLayout layout;
    Group_Manager group_manager;
    Journal_Describer journal_describer;
    Calendar calendar;
    int button_count = 0;
    public Edit_Journal_Page(Calendar entry_page_date, Journal_Describer journal_describer){
        calendar = entry_page_date;
        this.journal_describer = journal_describer;
    }

    @Override
    public void start(Context context, Page_Change_Listener listener) {
        System.out.println("\t<edit page> starting page");
        this.context = context;
        this.page_change_listener = listener;
        create_edit_page();
    }

    @Override
    public boolean on_close() {
        close_edit();
        return true;
    }

    @Override
    public View get_view() {
        return layout;
    }

    public void close_edit(){
        System.out.println("getting new journal: ");
        Journal_Describer new_journal = group_manager.get_describer();
        HashMap<Integer, Integer> map = group_manager.get_old_to_new_map();
        //User_Entries.rewrite_entries_new_journal(User_Journals.get_journal(context), new_journal, context, map);
        System.out.println("new journal: " + new_journal);
        System.out.println("writing new journal data");
        User_Journals.write_journal(new_journal, context);
        Journal_Describer from_data = User_Journals.check_data_parse(new_journal);
        System.out.println("journal read from file: " + from_data);
        User_Journals.set_journal(from_data);

        //create_journal_entry_page(User_Journals.get_journal(context), entry_page_date);
    }

    public void create_edit_page(){
        Icon.edit_mode();

        layout = MainActivity.inflate_page(context, R.layout.group_row);

        ScrollView scrollView =  layout.findViewById(R.id.icon_scrollView);
        layout.removeView(scrollView);

        group_manager = new Group_Manager(context, journal_describer, layout);
        group_manager.edit_mode();
        create_button(layout, "print cord", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                group_manager.print_cord();
                group_manager.print_tree();
            }
        });

        create_button(layout, "move icon", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                group_manager.move_icon(0, 0, 0, 0, 1, 1);
            }
        });

        create_button(layout, "add group", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                group_manager.add_group("default");
            }
        });

        create_button(layout, "add icon", new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                group_manager.new_icon();
            }
        });
        create_button(layout, "exit edit", new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                page_change_listener.change_page(new Entry_Page(calendar));
            }
        });
    }

    public void create_button(ViewGroup parent, String name, View.OnClickListener action){
        LinearLayout button_vertical_layout = (LinearLayout) parent.findViewById(R.id.button_linear_layout);
        LinearLayout button_horizontal_layout = (LinearLayout)button_vertical_layout.getChildAt(0);
        button_count ++;
        if(button_count > 3){
            //System.out.println("using second button layout");
            button_horizontal_layout = (LinearLayout)button_vertical_layout.getChildAt(1);
        }
        Button button = (Button) LayoutInflater.from(context).inflate(R.layout.button_template, null);
        button.setText(name);
        button.setOnClickListener(action);
        button_horizontal_layout.addView(button);



    }
}
