package com.example.journal.Custom_Layout;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.journal.MyAsyncTask;


import java.util.ArrayList;

public class BackEndClock {
    static BackEndClock clock = null;
    boolean keep_running = true;
    long tick_count = 0;
    public ArrayList<BooleanFunction> methods = new ArrayList();
    public static Activity activity;
    Handler handler;
    public BackEndClock(){
        handler = new Handler(Looper.getMainLooper());
        setup_next_tick();
    }

    public static void init(Activity _activity){
        activity = _activity;
        clock = new BackEndClock();
    }

    private void setup_next_tick(){
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(25); // Wait for 1 second
                System.out.println("Finished waiting");
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        tick();
//                    }
//                });

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        tick();
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.start();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                tick();
//            }
//        }, 100);
    }


    private void tick(){
        if(tick_count % 20 == 0){
            System.out.println("20 ticks");
        }
        tick_count++;
        for (int i = methods.size() - 1; i >= 0; i--) {
            boolean keep_going = methods.get(i).run();
            if (!keep_going) {
                methods.remove(i);
            }
        }
        setup_next_tick();
    }

    public static BackEndClock get_clock(){
        if(clock == null)
            clock = new BackEndClock();
        return clock;
    }
}
