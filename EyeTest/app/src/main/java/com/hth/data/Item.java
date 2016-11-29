package com.hth.data;

import android.graphics.Color;
import android.view.View;

/**
 * Created by Lenovo on 11/28/2016.
 */

public class Item {
    private String content;
    private boolean isGoal;
    private boolean isColor;
    private int color;
    private boolean isSelected;

    public Item()
    {

    }

    public Item(String content, int color, boolean isGoal, boolean isColor)
    {
        this.content = content;
        this.color = color;
        this.isGoal = isGoal;
        this.isColor = isColor;
        this.isSelected = false;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isGoal() {
        return isGoal;
    }

    public void setGoal(boolean goal) {
        isGoal = goal;
    }

    public boolean isColor() {
        return isColor;
    }

    public void setColor(boolean color) {
        isColor = color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
