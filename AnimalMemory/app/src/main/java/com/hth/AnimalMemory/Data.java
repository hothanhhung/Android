package com.hth.AnimalMemory;

import java.util.ArrayList;

/**
 * Created by Lenovo on 6/3/2016.
 */
public class Data {
    public static final String DIFFICULTY_KEY="DIFFICULTY_KEY";
    public static final int DIFFICULTY_CONTINUES = -1;
    public static final int DIFFICULTY_NEWGAME = 0;

    public static final GameLevel []GameLevels = new GameLevel [] {
            new GameLevel(6, 6, 5), new GameLevel(6, 6, 7), new GameLevel(6, 6, 9), new GameLevel(6, 6, 11), new GameLevel(6, 6, 13), new GameLevel(6, 6, 15),
            new GameLevel(6, 7, 7), new GameLevel(6, 7, 9), new GameLevel(6, 7, 11), new GameLevel(6, 7, 13), new GameLevel(6, 7, 15), new GameLevel(6, 7, 17), new GameLevel(6, 7, 19),
            new GameLevel(6, 8, 9), new GameLevel(6, 8, 11), new GameLevel(6, 8, 13), new GameLevel(6, 8, 15), new GameLevel(6, 8, 17), new GameLevel(6, 8, 19), new GameLevel(6, 8, 21), new GameLevel(6, 8, 23),
            new GameLevel(7, 8, 11), new GameLevel(7, 8, 13), new GameLevel(7, 8, 15), new GameLevel(7, 8, 17), new GameLevel(7, 8, 19), new GameLevel(7, 8, 21), new GameLevel(7, 8, 23), new GameLevel(7, 8, 25), new GameLevel(7, 8, 27),
            new GameLevel(8, 8, 13), new GameLevel(8, 8, 15), new GameLevel(8, 8, 17), new GameLevel(8, 8, 19), new GameLevel(8, 8, 21), new GameLevel(8, 8, 23), new GameLevel(8, 8, 25), new GameLevel(8, 8, 27), new GameLevel(8, 8, 29), new GameLevel(7, 8, 31),
            new GameLevel(8, 9, 15), new GameLevel(8, 9, 17), new GameLevel(8, 9, 19), new GameLevel(8, 9, 21), new GameLevel(8, 9, 23), new GameLevel(8, 9, 25), new GameLevel(8, 9, 27), new GameLevel(8, 9, 29), new GameLevel(8, 9, 31), new GameLevel(8, 9, 33), new GameLevel(8, 9, 35),
    };
}
