package com.example.journal.Custom_Layout;

import java.util.ArrayList;

public class Compute_Timer {
    ArrayList<Long> start_times = new ArrayList<>();
    ArrayList<Long> compute_times = new ArrayList<>();
    long past_print_time;
    long print_interval = 5000000;
    String name;
    public Compute_Timer(String name){
        this.name = name;
        past_print_time = get_time_micro();
    }

    public void log_time(long start, long end){
        start_times.add(start);
        compute_times.add(end-start);
        check_to_print();
    }

    public long get_average_compute(){
        return average(compute_times);
    }

    public long get_average_compute(long recent_time_threshold_micro){
        return average(get_past_times(recent_time_threshold_micro));
    }

    public long get_average_compute_past_second(){
        return average(get_past_times(1000*1000));
    }

    public long get_total_mill_compute_past_second(){
        return total(get_past_times(1000*1000))/1000;
    }

    public void check_to_print(){
        if(start_times.size() == 0)
            return;
        if(start_times.get(start_times.size() - 1) - past_print_time < print_interval){
            //System.out.println("("+name+") time to print: " + (start_times.get(start_times.size() - 1) - past_print_time) + ">=" + print_interval);
            return;
        }

        past_print_time = start_times.get(start_times.size() - 1);
        System.out.println("("+name+") average compute time in the past second: "+get_average_compute_past_second());
        System.out.println("("+name+") total compute time in past second: " + get_total_mill_compute_past_second());
    }

    public ArrayList<Long> get_past_times(long time){
        long current_time = get_time_micro();
        ArrayList<Long> recent_compute_times = new ArrayList<>();

        for(int i = start_times.size() - 1; i >= 0; i--){
            if(start_times.get(i) - current_time < 1000*1000){
                recent_compute_times.add(compute_times.get(i));
            }
        }
        return recent_compute_times;
    }

    public long average(ArrayList<Long> times){
        long total = 0;
        for(int i = 0; i < times.size(); i++){
            total += times.get(i);
        }

        return total/times.size();
    }

    public long total(ArrayList<Long> times){
        long total = 0;
        for(int i = 0; i < times.size(); i++){
            total += times.get(i);
        }

        return total;
    }

    public static long get_time_micro(){
        return System.nanoTime()/1000;
    }
}
