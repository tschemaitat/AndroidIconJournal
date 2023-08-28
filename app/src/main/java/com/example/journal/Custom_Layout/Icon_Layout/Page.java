package com.example.journal.Custom_Layout.Icon_Layout;

import android.content.Context;
import android.view.View;

import com.example.journal.Custom_Layout.Listener.Page_Change_Listener;

public abstract class Page {
    Context context;
    Page_Change_Listener page_change_listener;

    public abstract void start(Context context, Page_Change_Listener listener);
    public abstract boolean on_close();
    public abstract View get_view();
}
