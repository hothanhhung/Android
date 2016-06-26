package com.hth.lines;

/**
 * Created by Lenovo on 6/5/2016.
 */
public class TrackChange {
    private int x;
    private int y;
    private int value;

    public TrackChange(int x, int y, int value){
        this.x = x;
        this.y = y;
        this.value = value;
    }

    public int getX(){
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getValue() {
        return this.value;
    }
}
