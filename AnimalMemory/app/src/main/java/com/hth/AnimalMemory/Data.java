package com.hth.AnimalMemory;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Lenovo on 6/3/2016.
 */
public class Data {
    public static final String DIFFICULTY_KEY="DIFFICULTY_KEY";
    public static final int DIFFICULTY_CONTINUES = -1;
    public static final int DIFFICULTY_NEWGAME = 0;

    public static final GameLevel []GameLevels = new GameLevel [] {
            new GameLevel(4, 4, 2),
            new GameLevel(6, 6, 3), new GameLevel(6, 6, 5), new GameLevel(6, 6, 7), new GameLevel(6, 6, 9), new GameLevel(6, 6, 11), new GameLevel(6, 6, 13), new GameLevel(6, 6, 15),
            new GameLevel(6, 7, 5), new GameLevel(6, 7, 7), new GameLevel(6, 7, 9), new GameLevel(6, 7, 11), new GameLevel(6, 7, 13), new GameLevel(6, 7, 15), new GameLevel(6, 7, 17), new GameLevel(6, 7, 19),
            new GameLevel(6, 8, 7), new GameLevel(6, 8, 9), new GameLevel(6, 8, 11), new GameLevel(6, 8, 13), new GameLevel(6, 8, 15), new GameLevel(6, 8, 17), new GameLevel(6, 8, 19), new GameLevel(6, 8, 21), new GameLevel(6, 8, 23),
            new GameLevel(7, 8, 9), new GameLevel(7, 8, 11), new GameLevel(7, 8, 13), new GameLevel(7, 8, 15), new GameLevel(7, 8, 17), new GameLevel(7, 8, 19), new GameLevel(7, 8, 21), new GameLevel(7, 8, 23), new GameLevel(7, 8, 25), new GameLevel(7, 8, 27),
            new GameLevel(8, 8, 11), new GameLevel(8, 8, 13), new GameLevel(8, 8, 15), new GameLevel(8, 8, 17), new GameLevel(8, 8, 19), new GameLevel(8, 8, 21), new GameLevel(8, 8, 23), new GameLevel(8, 8, 25), new GameLevel(8, 8, 27), new GameLevel(8, 8, 29), new GameLevel(8, 8, 31), new GameLevel(8, 8, 32),
            new GameLevel(8, 9, 13), new GameLevel(8, 9, 15), new GameLevel(8, 9, 17), new GameLevel(8, 9, 19), new GameLevel(8, 9, 21), new GameLevel(8, 9, 23), new GameLevel(8, 9, 25), new GameLevel(8, 9, 27), new GameLevel(8, 9, 29), new GameLevel(8, 9, 31), new GameLevel(8, 9, 33), new GameLevel(8, 9, 35), new GameLevel(8, 9, 36),
    };

    public static int[][] getCardsForLevel(int level)
    {
        if(level>GameLevels.length){
            return getCardsForLevel(GameLevels[GameLevels.length - 1]);
        }else{
            return getCardsForLevel(GameLevels[level - 1]);
        }
    }

    private static ArrayList<Integer> getListMaxValueItems(int max){
        ArrayList<Integer> list = new ArrayList<Integer>();
        for(int i=1;i<=max;i++){
            list.add(new Integer(i));
        }
        return list;
    }
    public static int[][] getCardsForLevel(GameLevel gameLevel)
    {
        int [][]cards = new int[gameLevel.numberOfRows][gameLevel.numberOfCols];
        int size = gameLevel.numberOfRows*gameLevel.numberOfCols;
        int maxItem = gameLevel.numberOfItems;

        Random r = new Random();

        ArrayList<Integer> list = new ArrayList<Integer>();
        ArrayList<Integer> listMaxValueItems = getListMaxValueItems(maxItem);
        int valueIndex, value;
        for(int i=0;i<size;i=i+2){
            if(listMaxValueItems.size() == 0){
                listMaxValueItems.addAll(getListMaxValueItems(maxItem));
            }
            valueIndex = r.nextInt(listMaxValueItems.size());
            value = listMaxValueItems.remove(valueIndex);
            list.add(value);
            list.add(value);
        }

        for(int i=size-1;i>=0;i--){
            int t=0;
            if(i>0){
                t = r.nextInt(i);
            }

            t=list.remove(t).intValue();
            cards[i/gameLevel.numberOfCols][i%gameLevel.numberOfCols]=t;

        }
        return cards;
    }
}
