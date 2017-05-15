package com.hunght.data;

/**
 * Created by Lenovo on 5/15/2017.
 */

public class GameItem {
    String name;
    int id;
    boolean isCompleted;
    String gameTarget;
    String gameCurrent;
    String gameStart;

    public GameItem(){

    }

    public GameItem(String name, int id, boolean isCompleted, String gameStart, String gameCurrent, String gameTarget){
        this.name = name;
        this.id = id;
        this.isCompleted = isCompleted;
        this.gameTarget = gameTarget;
        this.gameCurrent = gameCurrent;
        this.gameStart = gameStart;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getGameTarget() {
        return gameTarget;
    }

    public void setGameTarget(String gameTarget) {
        this.gameTarget = gameTarget;
    }

    public String getGameCurrent() {
        return gameCurrent;
    }

    public void setGameCurrent(String gameCurrent) {
        this.gameCurrent = gameCurrent;
    }

    public String getGameStart() {
        return gameStart;
    }

    public void setGameStart(String gameStart) {
        this.gameStart = gameStart;
    }
}
