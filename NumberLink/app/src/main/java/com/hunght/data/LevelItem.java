package com.hunght.data;

/**
 * Created by Lenovo on 6/28/2017.
 */

public class LevelItem {
    int levelId;
    int numberRows;
    int numberColumns;
    int numberWinGame;
    int totalGames;
    String [] gameIds;
    public LevelItem(){}

    public LevelItem(int levelId, int numberRows, int numberColumns, int numberWinGame, String [] gameIds){
        this.levelId = levelId;
        this.numberRows = numberRows;
        this.numberColumns = numberColumns;
        this.numberWinGame = numberWinGame;
        this.gameIds = gameIds;
        if(this.gameIds == null){
            this.totalGames = 0;
        }else {
            this.totalGames = this.gameIds.length;
        }
    }

    public int getLevelId() {
        return numberRows;
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
        return numberWinGame;
    }

    public void setNumberWinGame(int numberWinGame) {
        this.numberWinGame = numberWinGame;
    }

    public int getTotalGames() {
        return totalGames;
    }

    public void setTotalGames(int totalGames) {
        this.totalGames = totalGames;
    }

    public String [] getGameIds()
    {
        return gameIds;
    }

    public boolean isAllComplete()
    {
        return totalGames == numberWinGame;
    }

    public String getSize()
    {
        return getNumberRows()+"-"+getNumberColumns();
    }

    public String getStatus()
    {
        return getNumberWinGame()+"/"+getTotalGames();
    }
}
