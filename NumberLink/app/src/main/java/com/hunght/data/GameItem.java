package com.hunght.data;

import java.io.Serializable;

/**
 * Created by Lenovo on 5/15/2017.
 */

public class GameItem implements Serializable {
    final String splitValue = "|";
    String name;
    String id;
    boolean isCompleted;
    int[][] gameTarget;
    int[][] gameCurrent;
    int[][] gameStart;
    int gameColumn;
    int gameRow;

    public GameItem(){

    }

    public GameItem(String name, String id, boolean isCompleted, int gameColumn, int gameRow, String gameStart, String gameCurrent, String gameTarget){
        this.name = name;
        this.id = id;
        this.isCompleted = isCompleted;
        this.gameColumn = gameColumn;
        this.gameRow = gameRow;
        this.gameTarget = convertGameMapFromString(gameTarget);
        this.gameCurrent = convertGameMapFromString(gameCurrent);
        this.gameStart = convertGameMapFromString(gameStart);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public int[][] getGameTarget() {
        return gameTarget;
    }

    public int[][] getGameCurrent() {
        return gameCurrent;
    }

    public void setGameCurrent(int x, int y, int value) {

        if (x >= 0 && x < gameRow && y >= 0 && y < gameColumn) {
            gameCurrent[x][y] = value;
        }
    }

    public int getGameCurrent(int x, int y) {

        if (x >= 0 && x < gameRow && y >= 0 && y < gameColumn) {
            return gameCurrent[x][y];
        }
        return -2;
    }

    public String getGameCurrentInString(int x, int y) {

        int value = getGameCurrent(x, y);
        if(value > 0) {
            return String.valueOf(value);
        }else{
            return "";
        }
    }

    public int[][] getGameStart() {
        return gameStart;
    }

    public int getGameColumn() {
        return gameColumn;
    }

    public void setGameColumn(int gameColumn) {
        this.gameColumn = gameColumn;
    }

    public int getGameRow() {
        return gameRow;
    }

    public void setGameRow(int gameRow) {
        this.gameRow = gameRow;
    }

    public boolean canChange(int x, int y) {

        if (x >= 0 && x < gameRow && y >= 0 && y < gameColumn) {
            return gameStart[x][y] == 0;
        }
        return false;
    }

    public boolean canSee(int x, int y) {

        if (x >= 0 && x < gameRow && y >= 0 && y < gameColumn) {
            return gameStart[x][y] != -1;
        }
        return false;
    }

    private int[][] convertGameMapFromString(String strGame){
        int[][] twoDimensionalGameMap = new int[gameRow][gameColumn];
        if(strGame == null) {
            String[] oneDimensionalGameMap = strGame.split(splitValue);
            if (oneDimensionalGameMap.length == gameColumn * gameRow) {
                for(int i=0; i<gameColumn; i++)
                    {
                    twoDimensionalGameMap[i/gameColumn][i%gameColumn] = convertStringToInt(oneDimensionalGameMap[i]);
                }
            }
        }
        return twoDimensionalGameMap;
    }

    private String convertGameMapToString(int[][] gameMap){
        StringBuilder oneDimensionalGameMap = new StringBuilder();
        if(gameMap == null) {
            {
                for(int i=0; i<gameMap.length; i++)
                {
                    for(int j=0; i<gameMap[i].length; j++)
                    {
                        if(oneDimensionalGameMap.length() == 0){
                            oneDimensionalGameMap.append(gameMap[i][j]);
                        }else{
                            oneDimensionalGameMap.append(splitValue + gameMap[i][j]);
                        }
                    }
                }
            }
        }
        return oneDimensionalGameMap.toString();
    }

    private static int convertStringToInt(String value){
        try {
            return Integer.parseInt(value);
        }catch (Exception ex){
            return 0;
        }
    }
}
