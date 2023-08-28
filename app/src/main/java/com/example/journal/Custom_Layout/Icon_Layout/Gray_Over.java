package com.example.journal.Custom_Layout.Icon_Layout;

import android.content.Context;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.journal.Custom_Layout.Listener.Close_Listener;
import com.example.journal.Custom_Layout.Utility.ViewFactory;

public class Gray_Over {
    ConstraintLayout parent;
    Close_Listener close_listener;
    Context context;
    ConstraintLayout layout;
    public Gray_Over(Context context, ConstraintLayout parent, Close_Listener close_listener){
        this.parent = parent;
        this.close_listener = close_listener;
        this.context = context;
        layout = construct_gray_over();
        parent.addView(layout);
    }

    public View view(){
        return layout;
    }

    private ConstraintLayout construct_gray_over(){
        ConstraintLayout constraintLayout = new ConstraintLayout(context);
        constraintLayout.setLayoutParams(ViewFactory.createLayoutParams(0, 0, 0, 0, -1, -1));
        constraintLayout.setBackgroundColor(Color.parseColor("#60000000"));

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close_listener.close();
                parent.removeView(layout);
            }
        });
        return constraintLayout;
    }


}
