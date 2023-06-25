package com.example.journal;

import android.os.AsyncTask;

public class MyAsyncTask extends AsyncTask<Void, Void, Void> {
    private Runnable task;
    private String name;
    private int count;
    public MyAsyncTask(String name, Runnable task) {
        count = 0;
        this.name = name;
        this.task = task;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        count++;
        System.out.println("doing in background: " + name + "("+count+")");
        task.run();
        return null;
    }
}