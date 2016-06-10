package com.hth.sudoku;

import android.content.Context;

import com.hth.utils.DataBaseHelper;

import java.util.List;
import java.util.Random;

/**
 * Created by Lenovo on 6/3/2016.
 */
public class Data {
    public static final String DIFFICULTY_KEY="DIFFICULTY_KEY";
    public static final int DIFFICULTY_CONTINUES = 0;
    public static final int DIFFICULTY_EASY = 1;
    public static final int DIFFICULTY_MEDIUM = 2;
    public static final int DIFFICULTY_HARD = 3;
    public static final int DIFFICULTY_EXPERT = 4;
    public static final int DIFFICULTY_CREATE = 5;
    public static final int DIFFICULTY_SPECIAL = 6;
    public static final int DIFFICULTY_PLAY_AGAIN = 8;

    public static final String EasyPuzzle =  "854219763397865421261473985"
                                            +"785126394649538172132947856"
                                            +"926384517513792648478600000";
    public static final String MediumPuzzle =
             "360000000004230800000004200"
            +"070460003820000014500013020"
            +"001900000007048300000000045";
    public static final String HarPuzzle =
             "360000000004230800000004200"
            +"070460003820000014500013020"
            +"001900000007048300000000045";

    public static SudokuItem getSudokuToPlay(Context context, int diff){
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        List<SudokuItem> sudokuItems = dataBaseHelper.getUnusedSudokus(diff);
        if(sudokuItems.size() > 0){
            return sudokuItems.get((new Random()).nextInt(sudokuItems.size()));
        }
        return null;
    }

    public static String ReplayOriginalMap = "";
    public static int ReplayLevel=0;
}
