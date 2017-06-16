package com.hunght.data;

import java.util.ArrayList;

/**
 * Created by Lenovo on 5/15/2017.
 */

public class StaticData {
    public static final String DIFFICULTY_KEY = "DIFFICULTY_KEY";
    public static final String GAME_ITEM_KEY = "GAME_ITEM_KEY";
    public static final int GAME_COLUMN = 0;
    public static final int GAME_ROW = 0;

    private static GameItem currentGameItem;

    public static int getNumberRows()
    {
        return getCurrentGame().getGameRow();
    }

    public static int getNumberColumns()
    {
        return getCurrentGame().getGameColumn();
    }

    public static GameItem getCurrentGame()
    {
        return currentGameItem;
    }

    public static void setCurrentGame(GameItem gameItem)
    {
        currentGameItem = gameItem;
    }

    public static ArrayList<ArrayList<Integer>> getLines() {
        return StaticData.getCurrentGame().getLines();
    }
    public static void resetGame()
    {
        StaticData.getCurrentGame().resetGame();
    }

}
