package com.hunght.data;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Pattern;

/**
 * Created by Lenovo on 5/15/2017.
 */

public class GameItem implements Serializable {
    final static String splitValue = "|";
    final static int errorNumber = - 99999;
    String name;
    String id;
    boolean isCompleted;
    int[][] gameTarget;
    int[][] gameCurrent;
    int[][] gameStart;
    int gameColumn;
    int gameRow;

    boolean isEnable;
    long lSeconds;

    ArrayList<ArrayList<Integer>> lines;

    public GameItem(){

    }

    public GameItem(String name, String id, boolean isEnable, boolean isCompleted, int gameRow, int gameColumn, String gameStart, String gameCurrent, String gameTarget){
        this.name = name;
        this.id = id;
        this.isEnable = isEnable;
        this.isCompleted = isCompleted;
        this.gameColumn = gameColumn;
        this.gameRow = gameRow;
        this.gameStart = convertGameMapFromString(gameStart);
        this.gameTarget = convertGameMapFromString(gameTarget);
        if(gameCurrent == null || gameCurrent == ""){
            this.gameCurrent = convertGameMapFromString(gameStart);
        }else{
            this.gameCurrent = convertGameMapFromString(gameCurrent);
        }
    }

    public void resetGame()
    {
        isCompleted = false;
        for(int i = 0; i<gameRow; i++)
            for(int j =0; j<gameColumn; j++) gameCurrent[i][j] = gameStart[i][j];
    }

    public long getGamePlaySeconds() {
        return lSeconds;
    }

    public void setGamePlaySeconds(long lSeconds) {
        this.lSeconds = lSeconds;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public String getName() {
        return name;
    }
    public int getGroupId() {
        return gameRow * 10 + gameColumn;
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

    public boolean isWin() {
        return isCompleted;
    }
    public boolean isPlaying() {
        return lSeconds > 0;
    }
    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
    public int getMaxValue()
    {
        return gameColumn * gameRow;
    }
    public int[][] getGameTarget() {
        return gameTarget;
    }

    public int[][] getGameCurrent() {
        return gameCurrent;
    }

    public boolean setGameCurrent(int x, int y, int value) {

        if (x >= 0 && x < gameRow && y >= 0 && y < gameColumn) {
            gameCurrent[x][y] = value;
            return true;
        }
        return false;
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
    public int getHint(int x, int y){
        if (x >= 0 && x < gameRow && y >= 0 && y < gameColumn) {
            return gameTarget[x][y];
        }
        return 0;
    }
    public ArrayList<ArrayList<Integer>> getLines()
    {
        ArrayList<ArrayList<Integer>> lines = findLines(gameCurrent);
        if(lines!=null && lines.size() == 1){
            ArrayList<Integer> line = lines.get(0);
            if(line!=null){
                int ignore = 0;
                for(int i =0;i<gameCurrent.length;i++)
                    for(int j=0; j<gameCurrent[i].length; j++)
                    {
                        if(gameCurrent[i][j] == -1 ) {
                            ignore++;
                        }
                    }
                if(line.size() == gameRow * gameColumn - ignore) {
                    boolean win = true;
                    for (int i = 0; i < line.size(); i++) {
                        if (!(line.get(i) /100 == i + 1)) win = false;
                    }
                    isCompleted = win;
                }
            }
        }
        return lines;
    }

    private int[][] convertGameMapFromString(String strGame){
        int[][] twoDimensionalGameMap = new int[gameRow][gameColumn];
        if(strGame != null) {
            String[] oneDimensionalGameMap = strGame.split(Pattern.quote(splitValue));
            if (oneDimensionalGameMap.length >= gameColumn * gameRow) {
                int count = 0;
                for(int i=0; i<oneDimensionalGameMap.length; i++)
                    {
                        int value = convertStringToInt(oneDimensionalGameMap[i]);
                        if(value != errorNumber) {
                            twoDimensionalGameMap[count / gameColumn][count % gameColumn] = value;
                            count++;
                        }
                        if(count >= gameColumn * gameRow) break;
                }
            }
        }
        return twoDimensionalGameMap;
    }

    private String convertGameMapToString(int[][] gameMap){
        StringBuilder oneDimensionalGameMap = new StringBuilder();
        if(gameMap != null) {
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
            return errorNumber;
        }
    }

    private boolean hasLine(int val1, int val2){
        return (val1/100 - val2/100) == -1 || (val1/100 - val2/100) == 1;
    }
    private ArrayList<ArrayList<Integer>> findLines(int[][] gameMap)
    {
        ArrayList<ArrayList<Integer>> lines = new ArrayList<>();
        if(gameMap!=null){
            for(int i=0; i<gameMap.length; i++)
            {
                if(gameMap[i]!= null) {

                    for (int j = 0; j < gameMap[i].length; j++) {
                        if(gameMap[i][j]>0) {
                            int val1 = gameMap[i][j] * 100 + i*gameMap[i].length + j, val2 = 0;
                            // left = 0,
                            if(j - 1 >= 0 && gameMap[i][j - 1]>0){
                                val2 = gameMap[i][j - 1] * 100 + i*gameMap[i].length + j - 1;
                                if(hasLine(val1, val2)) calculateLine(lines, val1, val2);
                            }
                            //, leftTop = 0
                            if(j - 1 >= 0 && i - 1 >=0 && gameMap[i - 1][j - 1]>0){
                                val2 = gameMap[i - 1][j - 1] * 100  + (i-1)*gameMap[i].length + j - 1;
                                if(hasLine(val1, val2)) calculateLine(lines, val1, val2);
                            }
                            //leftBottom = 0
                            if(j - 1 >= 0 &&  i + 1 < gameMap.length && gameMap[i + 1][j - 1]>0){
                                val2 = gameMap[i + 1][j - 1] * 100  + (i+1)*gameMap[i].length + j - 1;
                                if(hasLine(val1, val2)) calculateLine(lines, val1, val2);
                            }
                            // top = 0,
                            if(i - 1 >= 0 && gameMap[i - 1][j]>0){
                                val2 = gameMap[i - 1][j] * 100  + (i-1)*gameMap[i].length + j;
                                if(hasLine(val1, val2)) calculateLine(lines, val1, val2);
                            }
                            //rightTop =0,
                            if(j + 1 < gameMap[i].length && i - 1 >= 0 && gameMap[i - 1][j + 1]>0){
                                val2 = gameMap[i - 1][j + 1] * 100  + (i-1)*gameMap[i].length + j + 1;
                                if(hasLine(val1, val2)) calculateLine(lines, val1, val2);
                            }
                            //right = 0,
                            if(j + 1 < gameMap[i].length && gameMap[i][j + 1]>0){
                                val2 = gameMap[i][j + 1] * 100  + (i)*gameMap[i].length + j + 1;
                                if(hasLine(val1, val2)) calculateLine(lines, val1, val2);
                            }
                            //rightBottom = 0;
                            if(j + 1 < gameMap[i].length && i + 1 < gameMap.length && gameMap[i + 1][j + 1]>0){
                                val2 = gameMap[i + 1][j + 1] * 100  + (i+1)*gameMap[i].length + j + 1;
                                if(hasLine(val1, val2)) calculateLine(lines, val1, val2);
                            }
                            //bottom = 0;
                            if(i + 1 < gameMap.length && gameMap[i + 1][j]>0){
                                val2 = gameMap[i + 1][j] * 100  + (i+1)*gameMap[i].length + j;
                                if(hasLine(val1, val2)) calculateLine(lines, val1, val2);
                            }

                        }
                    }
                }
            }
        }
        for (ArrayList<Integer> line:lines) {
            Collections.sort(line);
        }
        return lines;
    }

    private void calculateLine(ArrayList<ArrayList<Integer>> lines, int val1, int val2)
    {
        ArrayList<Integer> line1 = null, line2 = null;
        for (ArrayList<Integer> line : lines) {
            if (line.contains(val1))
            {
                line1 = line;
            }
            if (line.contains(val2))
            {
                line2 = line;
            }
        }

        if(line1 == null && line2 == null)
        {
            line1 = new ArrayList<>();
            line1.add(val1);
            line1.add(val2);
            lines.add(line1);
        }
        else if(line1 != null && line2 == null)
        {
            line1.add(val2);
        }
        else if(line1 == null && line2 != null)
        {
            line2.add(val1);
        }else if(!line1.equals(line2)){
            line1.addAll(line2);
            lines.remove(line2);
        }
    }
}
