package com.hunght.data;

import java.util.ArrayList;

/**
 * Created by Lenovo on 5/15/2017.
 */

public class DataProcess {
    public static ArrayList<GameItem> getGameItems(int level)
    {
        ArrayList<GameItem> gameItems = new ArrayList<GameItem>();
        for(int i = 0; i<11; i++)
        {
            gameItems.add(new GameItem("","20052017GAME01", false, 3, 3,"0|0|6|0|2|0|0|4|1","0|0|6|0|2|0|0|4|1","9|7|6|8|2|5|3|4|1"));
            gameItems.add(new GameItem("","20052017GAME02", false, 4, 4,"0|14|0|4|-1|0|3|0|0|2|0|7|1|0|9|0","0|14|0|4|-1|0|3|0|0|2|0|7|1|0|9|0","15|14|13|4|-1|12|3|5|11|2|6|7|1|10|9|8"));
        }

        return gameItems;
    }
}
