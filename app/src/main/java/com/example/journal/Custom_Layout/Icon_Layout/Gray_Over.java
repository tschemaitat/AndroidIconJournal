package com.example.journal.Custom_Layout.Icon_Layout;

import android.content.Context;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.journal.Custom_Layout.Listener.Close_Listener;
import com.example.journal.Custom_Layout.Utility.ViewFactory;

import org.json.JSONObject;

import java.util.ArrayList;

public class Gray_Over {
    ConstraintLayout parent;
    Close_Listener close_listener;
    Context context;
    ConstraintLayout layout;
    boolean closed = false;
    public Gray_Over(Context context, ConstraintLayout parent, Close_Listener close_listener, boolean add_gray){
        this.parent = parent;
        this.close_listener = close_listener;
        this.context = context;
        layout = construct_gray_over(add_gray);
        parent.addView(layout);
    }

    public View view(){
        return layout;
    }

    private ConstraintLayout construct_gray_over(boolean add_gray){
        ConstraintLayout constraintLayout = new ConstraintLayout(context);
        constraintLayout.setLayoutParams(ViewFactory.createLayoutParams(0, 0, 0, 0, -1, -1));
        if(add_gray)
            constraintLayout.setBackgroundColor(Color.parseColor("#60000000"));

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
        return constraintLayout;
    }

    public void close(){
        if(closed)
            return;
        closed = true;
        close_listener.close();
        parent.removeView(layout);
    }

    public static void json(){

        JSONObject temp = new JSONObject();
        temp.put("value", 100);
        temp.put("name", "dollar");
        ArrayList<JSONObject> jsons = new ArrayList<>();
        jsons.add()

    }




}
