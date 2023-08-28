package com.example.journal.Custom_Layout.Icon_Layout;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.journal.Custom_Layout.Describer.Journal_Describer;
import com.example.journal.Custom_Layout.Describer.User_Journals;
import com.example.journal.Custom_Layout.Entry.Journal_Entry;
import com.example.journal.Custom_Layout.Entry.User_Entries;
import com.example.journal.Custom_Layout.Listener.Page_Change_Listener;
import com.example.journal.MainActivity;
import com.example.journal.R;

import java.util.Calendar;

public class Entry_Page extends Page {
    ConstraintLayout layout;
    public Entry_Page(Calendar date){
        super();
        this.date = date;

    }
    Calendar date;
    Group_Manager group_manager;
    Journal_Describer journal_describer = null;
    @Override
    public void start(Context context, Page_Change_Listener listener) {
        this.context = context;
        this.page_change_listener = listener;
        this.journal_describer = User_Journals.get_journal(context);
        create_journal_entry_page();
    }

    @Override
    public boolean on_close() {
        close_entry();
        return true;
    }

    @Override
    public View get_view() {
       return layout;
    }

    private void close_entry(){
        Journal_Entry journal_entry = group_manager.get_entry();
        User_Entries.write_entry_grouped(journal_entry, date, context, journal_describer);
        journal_describer = null;
        Journal_Entry parsed_entry = User_Entries.read_group_file(User_Journals.get_journal(context), date, context);
    }

    private void create_journal_entry_page(){
        Icon.journal_mode();
        layout = MainActivity.inflate_page(context, R.layout.journal_non_edit_entry);
        group_manager = new Group_Manager(context, journal_describer, layout);
        group_manager.journal_mode();
        group_manager.set_date(date);

        ImageView edit_page_button = layout.findViewById(R.id.edit_page_button);
        edit_page_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("\t<entry page> changing page to edit page");
                page_change_listener.change_page(new Edit_Journal_Page(date, journal_describer));
            }
        });
        ImageView calendar_page_button = layout.findViewById(R.id.close_entry_open_calendar_button);
        calendar_page_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page_change_listener.change_page(new Calendar_Page());
            }
        });
        Journal_Entry journal_entry = User_Entries.read_group_file(group_manager.get_describer(), date, context);
        if(journal_entry != null)
            group_manager.set_entries(journal_entry);
    }
}
