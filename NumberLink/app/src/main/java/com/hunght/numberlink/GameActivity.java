package com.hunght.numberlink;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.hunght.data.GameItem;
import com.hunght.data.StaticData;

public class GameActivity extends AppCompatActivity {

    private PuzzleView puzzleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        puzzleView = (PuzzleView)findViewById(R.id.puzzleView);
    }

    public String getGameItemString(int x, int y) {
        return StaticData.getCurrentGame().getGameCurrentInString(x, y);
    }

    public int getGameItem(int x, int y) {
        return StaticData.getCurrentGame().getGameCurrent(x, y);
    }


    public boolean canChangeValue(int x, int y) {
        return StaticData.getCurrentGame().canChange(x, y);
    }

    public boolean canSeeValue(int x, int y) {
        return StaticData.getCurrentGame().canSee(x, y);
    }

    public void resetNumberButton(){}

    public boolean setTileIfValid(int x, int y, int val)
    {
        StaticData.getCurrentGame().setGameCurrent(x, y, val);
        return true;
    }


    public void btNumberClick(View view) {

        resetNumberButton();
        switch (view.getId()){
            case R.id.keypad_0:
                puzzleView.setSelectedTile(0);
                break;
            case R.id.keypad_1:
                puzzleView.setSelectedTile(1);
                break;
            case R.id.keypad_2:
                puzzleView.setSelectedTile(2);
                break;
            case R.id.keypad_3:
                puzzleView.setSelectedTile(3);
                break;
            case R.id.keypad_4:
                puzzleView.setSelectedTile(4);
                break;
            case R.id.keypad_5:
                puzzleView.setSelectedTile(5);
                break;
            case R.id.keypad_6:
                puzzleView.setSelectedTile(6);
                break;
            case R.id.keypad_7:
                puzzleView.setSelectedTile(7);
                break;
            case R.id.keypad_8:
                puzzleView.setSelectedTile(8);
                break;
            case R.id.keypad_9:
                puzzleView.setSelectedTile(9);
                break;
            case R.id.keypad_del:
                puzzleView.setSelectedTile(-1);
                break;
            case R.id.keypad_help:
                puzzleView.setSelectedTile(-2);
                break;
        }
    }
}
