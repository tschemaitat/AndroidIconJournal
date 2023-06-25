package com.example.journal.Custom_Layout;

public class Rectanglef {
    float top;
    float bottom;
    float left;
    float right;
    public Rectanglef(float top, float bottom ,float left, float right){
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
    }

    public String toString(){
        return "(t,b)(l,r):("+top+", "+bottom+")"+"-"+"("+left+", "+right+")";
    }

    public boolean inside(float x, float y){
        if(x > left && x < right && y > top && y < bottom)
            return true;
        return false;
    }

    public void reduce(float amount_x, float amount_y){
        top += amount_y;
        bottom -= amount_y;
        left += amount_x;
        right -= amount_x;
    }
}
