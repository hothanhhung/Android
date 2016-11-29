package com.hth.data;

/**
 * Created by Lenovo on 7/5/2016.
 */
public class HighScoreItem {
    int score;
    String name;

    public HighScoreItem(int score, String name)
    {
        this.score = score;
        this.name = name;
    }

    public int getScore(){return score;}

    public String getName(){
        if(name != null)
            return  name;
        else return "";
    }
}
