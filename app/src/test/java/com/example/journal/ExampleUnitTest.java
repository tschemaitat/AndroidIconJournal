package com.example.journal;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void image_scaling(){
        int targetW = 100;
        int targetH = 100;
        print_scaling(50, 75, 100, 100);
        print_scaling(50, 120, 100, 100);
        print_scaling(200, 120, 100, 100);
    }

    public void print_scaling(int photoW, int photoH, int targetW, int targetH){
        float target_ratio_w = (targetW * 1.0f) / photoW;
        float target_ratio_h = (targetH * 1.0f) / photoH;

        float scaleFactor = Math.max(target_ratio_w, target_ratio_h);
        System.out.println("scale: " + scaleFactor);
    }
}

