package com.hth.data;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Lenovo on 11/28/2016.
 */

public class QuestionItem {
    String content;
    String contentGoal;
    boolean isColor;
    int color;
    int colorGoal;
    String contentTitle;

    public  QuestionItem()
    {

    }

    public  QuestionItem(String content, String contentGoal, boolean isColor, int color, int colorGoal, String contentTitle)
    {
        this.content = content;
        this.contentGoal = contentGoal;
        this.isColor = isColor;
        this.color = color;
        this.colorGoal = colorGoal;
        this.contentTitle = contentTitle;
    }

    public ArrayList<Item> createItem(int size)
    {
        Random rand = new Random();
        int showColorGoal = colorGoal, showColor = color;
        if(isColor && showColor == 0 && showColorGoal == 0)
        {
            showColorGoal = rand.nextInt(0x00FFFF00) + 0xAA000000;
            showColor = showColorGoal + 0x10000000;// - 0x100000;// + rand.nextInt(0xF);

            Log.d("createItem", Integer.toString(showColorGoal, 16) + "" + Integer.toString(showColor, 16));
        }
        ArrayList<Item> items = new ArrayList<>();
        for(int i =0; i<size; i++)
        {
            items.add(new Item (content, showColor, false, isColor));
        }
        int index = rand.nextInt(size - 2) + 1;

        items.add(index, new Item (contentGoal, showColorGoal, true, isColor));
        items.remove(index + 1);
        return items;
    }

    public String getContent() {
        return content;
    }

    public String getContentGoal() {
        return contentGoal;
    }

    public boolean isColor() {
        return isColor;
    }

    public int getColor() {
        return color;
    }

    public int getColorGoal() {
        return colorGoal;
    }

    public String getContentTitle() {
        return contentTitle;
    }

}
