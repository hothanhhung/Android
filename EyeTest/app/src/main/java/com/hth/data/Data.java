package com.hth.data;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Lenovo on 6/3/2016.
 */
public class Data {
    public static final String DIFFICULTY_KEY="DIFFICULTY_KEY";
    public static final int DIFFICULTY_CONTINUES = -1;
    public static final int DIFFICULTY_NEWGAME = 0;


    public static final int COL_NUM = 9;
    public static final int ROW_NUM = 9;

    public static ArrayList<QuestionItem> gameData;

    public static QuestionItem getGameData(int index){
        if(gameData == null)
        {
            gameData = new ArrayList<>();
            gameData.add(new QuestionItem("b", "p",false, 0, 0, "Find p"));
            gameData.add(new QuestionItem("Q", "O",false, 0, 0, "Find O"));
            gameData.add(new QuestionItem("X", "V",false, 0, 0, "Find V"));
            gameData.add(new QuestionItem("B", "R",false, 0, 0, "Find R"));
            gameData.add(new QuestionItem("O", "C",false, 0, 0, "Find C"));
            gameData.add(new QuestionItem("C", "G",false, 0, 0, "Find C"));
            gameData.add(new QuestionItem("D", "Đ",false, 0, 0, "Find Đ"));
            gameData.add(new QuestionItem("2", "5",false, 0, 0, "Find 5"));
            gameData.add(new QuestionItem("8", "6",false, 0, 0, "Find 6"));
            gameData.add(new QuestionItem("6", "9",false, 0, 0, "Find 9"));
            gameData.add(new QuestionItem("9", "0",false, 0, 0, "Find 0"));
            gameData.add(new QuestionItem("<", ">",false, 0, 0, "Find >"));
            gameData.add(new QuestionItem("\"", "'",false, 0, 0, "Find '"));
            gameData.add(new QuestionItem("|", "!",false, 0, 0, "Find !"));
            /* gameData.add(new QuestionItem("", "",true, 0xFF97CCDE, 0xFFA1D2E3, "Find different color"));
            gameData.add(new QuestionItem("", "",true, 0xFFCD90D4, 0xFFCA8AD1, "Find different color"));
            gameData.add(new QuestionItem("", "",true, 0xFFD18ABF, 0xFFD48EC2, "Find different color"));
            gameData.add(new QuestionItem("", "",true, 0xFF8ED2D4, 0xFF8AD0D1, "Find different color"));
            gameData.add(new QuestionItem("", "",true, 0xFF8AD19D, 0xFF8ED4A0, "Find different color"));
            gameData.add(new QuestionItem("", "",true, 0xFFE6E65E, 0xFFE8E864, "Find different color"));
           gameData.add(new QuestionItem("", "",true, 0, 0, "Find different color"));
            gameData.add(new QuestionItem("", "",true, 0, 0, "Find different color"));
            gameData.add(new QuestionItem("", "",true, 0, 0, "Find different color"));
            gameData.add(new QuestionItem("", "",true, 0, 0, "Find different color"));
            gameData.add(new QuestionItem("", "",true, 0, 0, "Find different color"));
            gameData.add(new QuestionItem("", "",true, 0, 0, "Find different color"));
            gameData.add(new QuestionItem("", "",true, 0, 0, "Find different color"));*/
            gameData.add(new QuestionItem("", "",true, 0, 0, "Find different color"));
            gameData.add(new QuestionItem("", "",true, 0, 0, "Find different color"));
            gameData.add(new QuestionItem("", "",true, 0, 0, "Find different color"));
            gameData.add(new QuestionItem("", "",true, 0, 0, "Find different color"));
        }
        return gameData.get(index%gameData.size());
    }

    static Random rand;
    public static QuestionItem getRandomGameData()
    {
        if(rand == null)
        {
            rand = new Random();
        }
        return getGameData(rand.nextInt(1000));
    }
}
