package com.hth.data;

import android.graphics.Color;
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

    private static int getColorBands(int color, int index, int bands) {
        return darken(color, ((double) index) / ((double) bands));
    }

    private static int darken(int color, double fraction) {
        if (fraction < 0.05d) {
            fraction = 0.05000000074505806d;
        }
        return Color.argb(Color.alpha(color), (int) Math.round(Math.min(255.0d, ((double) Color.red(color)) + (255.0d * fraction))), (int) Math.round(Math.min(255.0d, ((double) Color.green(color)) + (255.0d * fraction))), (int) Math.round(Math.min(255.0d, ((double) Color.blue(color)) + (255.0d * fraction))));
    }

    public ArrayList<Item> createItem(int size)
    {
        Random rand = new Random();
        int showColorGoal = colorGoal, showColor = color;
        int shadeDenum = 11 + rand.nextInt(10);
        if(isColor && showColor == 0 && showColorGoal == 0)
        {
            showColor = rand.nextInt(0x00FFFF00) + 0xAA000000;
            showColorGoal = getColorBands(showColor, 1, shadeDenum);//showColorGoal - 0x10000000;// - 0x100000;// + rand.nextInt(0xF);

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
