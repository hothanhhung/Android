package com.hunght.data;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Lenovo on 6/28/2017.
 */

public class LevelItem {
    int levelId;
    int numberRows;
    int numberColumns;
    ArrayList<GameItem> gameItems;
    LevelItem referedLevelItem;
    Set<String> unlockLevels;

    public LevelItem(){}

    public LevelItem(int levelId, int numberRows, int numberColumns, ArrayList<GameItem> gameItems, LevelItem referedLevelItem, Set<String> unlockLevels){
        this.levelId = levelId;
        this.numberRows = numberRows;
        this.numberColumns = numberColumns;
        this.gameItems = gameItems;
        this.referedLevelItem = referedLevelItem;
        this.unlockLevels = unlockLevels;
    }

    public int getLevelId() {
        return numberRows*10 + numberColumns;
    }

    public String getLevelIdInString() {
        return String.valueOf(getLevelId());
    }
    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public int getNumberRows() {
        return numberRows;
    }

    public void setNumberRows(int numberRows) {
        this.numberRows = numberRows;
    }

    public int getNumberColumns() {
        return numberColumns;
    }

    public void setNumberColumns(int numberColumns) {
        this.numberColumns = numberColumns;
    }

    public int getNumberWinGame() {
        int numberWinGame = 0;
        for (GameItem gameItem:getGameItems()
             ) {
            if(gameItem.isWin()){
                numberWinGame++;
            }
        }
        return numberWinGame;
    }

    public int getTotalGames() {
        return getGameItems().size();
    }

    public int getTotalPlayingGames() {
        int number = 0;
        for (GameItem gameItem:getGameItems()
                ) {
            if(gameItem.isPlaying() && !gameItem.isWin()){
                number++;
            }
        }
        return number;
    }

    public ArrayList<GameItem> getGameItems()
    {
        if(gameItems == null) gameItems = new ArrayList<>();
        return gameItems;
    }

    public boolean isAllComplete()
    {
        return getNumberWinGame() == getTotalGames();
    }

    public String getSize()
    {
        return getNumberRows()+"-"+getNumberColumns();
    }

    public String getStatus()
    {
        return getNumberWinGame()+"/"+getTotalGames();
    }

    public boolean isAllWin() {
        for (GameItem gameItem: getGameItems()) {
            if(!gameItem.isWin()) return false;
        }
        return true;
    }

    public boolean isPlaying() {
        for (GameItem gameItem: getGameItems()) {
            if(gameItem.isPlaying()) return true;
        }
        return false;
    }

    public boolean isLock()
    {
        if(unlockLevels!=null)
        {
            if(unlockLevels.contains(getLevelIdInString()))
            {
                return false;
            }
        }
        if(referedLevelItem != null)
        {
            return !referedLevelItem.isAllWin();
        }
        return false;
    }

    public String getLockMessage()
    {
        if(referedLevelItem != null)
        {
            return "Complete "+referedLevelItem.getSize()+ " session to Unlock";
        }
        return "Complete all previous sessions to Unlock";
    }
}
