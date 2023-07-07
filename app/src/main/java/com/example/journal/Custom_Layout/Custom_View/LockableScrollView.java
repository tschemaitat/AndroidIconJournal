package com.example.journal.Custom_Layout.Custom_View;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class LockableScrollView extends ScrollView {



    // true if we can scroll (not locked)
    // false if we cannot scroll (locked)
    private boolean mScrollable = true;

    public LockableScrollView(Context context) {
        super(context);
    }

    public void setScrollingEnabled(boolean enabled) {
        System.out.println("setting scrollable: " + enabled);
        mScrollable = enabled;
    }

    public boolean is_child_larger(){
        View child = this.getChildAt(0);
        if(child.getHeight() > this.getHeight())
            return true;
        return false;
    }

    public void scroll_down(float amount){
        if(!is_child_larger())
            return;
        this.scrollBy(0, (int)amount);
    }

    public void scroll_up(float amount){
        if(!is_child_larger())
            return;
        this.scrollBy(0, (int)(amount * -1));
    }

    public boolean isScrollable() {
        return mScrollable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // if we can scroll pass the event to the superclass
                return mScrollable && super.onTouchEvent(ev);
            default:
                return super.onTouchEvent(ev);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // Don't do anything with intercepted touch events if
        // we are not scrollable
        return mScrollable && super.onInterceptTouchEvent(ev);
    }

}