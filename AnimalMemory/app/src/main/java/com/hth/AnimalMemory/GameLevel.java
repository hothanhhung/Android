package com.hth.AnimalMemory;

/**
 * Created by Lenovo on 10/7/2016.
 */
public class GameLevel {
    public int level;
    public int numberOfRows;
    public int numberOfCols;
    public int numberOfItems;

    public GameLevel(int numberOfCols, int numberOfRows, int numberOfItems)
    {
        this.numberOfRows = numberOfRows;
        this.numberOfCols = numberOfCols;
        this.numberOfItems = numberOfItems;
    }
}
