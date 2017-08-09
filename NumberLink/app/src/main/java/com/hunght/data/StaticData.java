package com.hunght.data;

import java.util.ArrayList;

/**
 * Created by Lenovo on 5/15/2017.
 */

public class StaticData {
    private static String base64EncodedPublicKey =
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzkQKTsT81Dv2Awahyq7nhEYn/O9iw0xmvSLNbpTOt927lmE8oDp/ida1IofatTFh59k5a9xgDgDinOT7ZQoyn6+ylhfCRfTj2QKuYZpbOlCLW1KDsG7ya9EswPb614ui+9oVvX186bHtVn8m6cAIulh6gK5m6S78JoXs2mznYfcBfKcsC1ryaoCnmP+MrS8dGQxHyKdmcLu54YdUo/OvAp46tCxyFMV7H9U+qYax51mvXuf/FTYP6EPR7CtLfHsuLxzVutNsjRstt5l2pnv2xNwEk5F0H5YBe/nfAvR0AoHPo70/KhvnsUudmXYhI2bCna0qVQTpp9LuGP8cSU6cTwIDAQAB";
    public static final String DIFFICULTY_KEY = "DIFFICULTY_KEY";
    public static final String GAME_ITEM_KEY = "GAME_ITEM_KEY";
    public static final int GAME_COLUMN = 0;
    public static final int GAME_ROW = 0;
    public static int currentNumberOfHints = 10;

    private static boolean isStart = true;
    private static GameItem currentGameItem;
    private static LevelItem currentLevelItem;
    private static boolean isShowLine;

    public  static String getLicenseKey()
    {
        return base64EncodedPublicKey;
    }

    public static boolean isStart(){
        if(isStart){
            isStart = false;
            return true;
        }
        return false;
    }
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

    public static LevelItem getCurrentLevel()
    {
        return currentLevelItem;
    }

    public static void setCurrentLevel(LevelItem levelItem)
    {
        currentLevelItem = levelItem;
    }

    public static ArrayList<ArrayList<Integer>> getLines() {
        return StaticData.getCurrentGame().getLines();
    }
    public static void resetGame()
    {
        StaticData.getCurrentGame().resetGame();
    }
    public static boolean isCompleted()
    {
        return StaticData.getCurrentGame().isCompleted();
    }
    public static int execHint(int x, int y)
    {
        currentNumberOfHints --;
        return StaticData.getCurrentGame().getHint(x, y);
    }

    public static int getCurrentHint()
    {
        return currentNumberOfHints;
    }

    public static int getMaxValue()
    {
        return StaticData.getCurrentGame().getMaxValue();
    }

    public static void setCurrentHint(int val)
    {
        currentNumberOfHints = val;
    }
    public static void awardNumberOfHint()
    {
        currentNumberOfHints = currentNumberOfHints + 1;
    }
}
