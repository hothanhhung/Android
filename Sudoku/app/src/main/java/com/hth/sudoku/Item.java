package com.hth.sudoku;

/**
 * Created by Lenovo on 6/4/2016.
 */
public class Item {
    public int value;
    public boolean canChange;
    public boolean canSee;

    public Item(int value)
    {
        this.value = value;
        if(value == 0)
        {
            this.canChange = true;
        }else{
            this.canChange = false;
        }
        this.canSee =true;
    }

    public Item(int value, boolean canChange)
    {
        this.value = value;
        this.canChange = canChange;
        this.canSee =true;
    }

    public void setValue(int value)
    {
        if(canChange){
            this.value = value;
        }
    }

    public int getValue()
    {
        return this.value;
    }

    public String getValueInString()
    {
        if(this.value == 0){
            return "";
        }

        return this.value+"";
    }

    public boolean canChange()
    {
        return this.canChange;
    }

    public boolean canSee()
    {
        return this.canSee;
    }
}
