package com.hth.lines;

import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created by Lenovo on 6/5/2016.
 */
public class TrackChange {
    ArrayList<Pair<Integer, Integer>> changes = new ArrayList<Pair<Integer, Integer>>();
    int score;

    public TrackChange(int score, int[][] history, int[][] now){
        this.changes = genaratedChanges(history, now);
        this.score = score;
    }

    public  ArrayList<Pair<Integer, Integer>> getChanges(){
        return this.changes;
    }

    public int getScore() {
        return this.score;
    }

    private ArrayList<Pair<Integer, Integer>> genaratedChanges(int[][] history, int[][] now)
    {
        ArrayList<Pair<Integer, Integer>> rs = new ArrayList<>();
        for(int i = 0; i< history.length; i++ )
            for(int j=0; j<history[i].length; j++ ){
                if(history[i][j] != now[i][j]){
                    rs.add(new Pair<Integer, Integer>(i * history[i].length + j, history[i][j]));
                }
            }
        return rs;
    }
}
