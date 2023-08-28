package com.example.journal.Custom_Layout.Icon_Layout;

import android.content.Context;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.journal.Custom_Layout.Describer.Journal_Describer;
import com.example.journal.Custom_Layout.Describer.User_Journals;
import com.example.journal.Custom_Layout.Listener.Page_Change_Listener;
import com.example.journal.MainActivity;
import com.example.journal.R;

import java.util.ArrayList;
import java.util.Calendar;

public class Calendar_Page extends Page{
    Journal_Describer journal_describer;
    ConstraintLayout layout;
    public Calendar_Page(){

    }
    Context context;
    @Override
    public void start(Context context, Page_Change_Listener listener) {
        this.context = context;
        this.page_change_listener = listener;
        journal_describer = User_Journals.get_journal(context);
        create_calendar();
    }

    @Override
    public boolean on_close() {
        close_calendar_screen();
        return true;
    }

    @Override
    public View get_view() {
        return layout;
    }

    public void close_calendar_screen(){
        CalendarView calendarView = layout.findViewById(R.id.calendarView);
        calendarView.clearDisappearingChildren();
    }

    public void create_calendar(){
        layout = MainActivity.inflate_page(context, R.layout.calenar_layout);
        ArrayList<EventDay> events = new ArrayList<>();


        CalendarView calendarView = layout.findViewById(R.id.calendarView);
        calendarView.setEvents(events);

        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar clickedDayCalendar = eventDay.getCalendar();

                int year = clickedDayCalendar.get(Calendar.YEAR);
                int month = clickedDayCalendar.get(Calendar.MONTH);
                int day = clickedDayCalendar.get(Calendar.DAY_OF_MONTH);

                on_close();
                page_change_listener.change_page(new Entry_Page(clickedDayCalendar));
                // do something here
            }
        });
    }
}
