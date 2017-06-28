package com.hunght.data;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Lenovo on 5/15/2017.
 */

public class DataProcess {
    private static GameItem[] originalGameItems = new GameItem[]{
            new GameItem("","0303GAME01", true, false, 3, 3,"0|0|6|0|2|0|0|4|1","0|0|6|0|2|0|0|4|1","9|7|6|8|2|5|3|4|1"),
            new GameItem("","0404GAME01", true, false, 4, 4,"0|14|0|4|-1|0|3|0|0|2|0|7|1|0|9|0","0|14|0|4|-1|0|3|0|0|2|0|7|1|0|9|0","15|14|13|4|-1|12|3|5|11|2|6|7|1|10|9|8")
    };
    private static ArrayList<GameItem> getGameItems()
    {
        return new ArrayList<>(Arrays.asList(originalGameItems));
    }

    public static GameItem getGameItem(String Id)
    {
        ArrayList<GameItem> gameItems = getGameItems();
        for(int i = 0; i<gameItems.size(); i++)
        {
            if(gameItems.get(i).getId().equals(Id)) return gameItems.get(i);
        }

        return null;
    }
    public static ArrayList<GameItem> getGameItem(String[] ids)
    {
        ArrayList<GameItem> gameAllItems = getGameItems(), gameItems = new ArrayList<>();
        List<String> listGame = Arrays.asList(ids);
        for(int i = 0; i<gameAllItems.size(); i++) {
            if (listGame.contains(gameAllItems.get(i).getId())) {
                gameItems.add(gameAllItems.get(i));
            }
        }

        return gameItems;
    }

    public static ArrayList<GameItem> getGameItems(int level)
    {
        ArrayList<GameItem> gameItems = new ArrayList<GameItem>();
        for(int i = 0; i<11; i++)
        {
            gameItems.add(new GameItem("","0303GAME01", i<5, false, 3, 3,"0|0|6|0|2|0|0|4|1","0|0|6|0|2|0|0|4|1","9|7|6|8|2|5|3|4|1"));
            gameItems.add(new GameItem("","0404GAME01", i<5, false, 4, 4,"0|14|0|4|-1|0|3|0|0|2|0|7|1|0|9|0","0|14|0|4|-1|0|3|0|0|2|0|7|1|0|9|0","15|14|13|4|-1|12|3|5|11|2|6|7|1|10|9|8"));
        }

        return gameItems;
    }

    public static ArrayList<LevelItem> getLevelItems()
    {
        ArrayList<LevelItem> levelItems = new ArrayList<LevelItem>();
        levelItems.add(new LevelItem(1, 3, 3, 3, new String[]{"0303GAME01"}));
        levelItems.add(new LevelItem(2, 4, 3, 3, new String[]{}));
        levelItems.add(new LevelItem(3, 4, 4, 3, new String[]{"0303GAME01"}));
        levelItems.add(new LevelItem(4, 5, 4, 3, new String[]{}));
        levelItems.add(new LevelItem(5, 5, 5, 3, new String[]{}));
        levelItems.add(new LevelItem(6, 6, 6, 3, new String[]{}));
        levelItems.add(new LevelItem(7, 7, 6, 3, new String[]{}));
        levelItems.add(new LevelItem(8, 7, 7, 3, new String[]{}));

        return levelItems;
    }
}
