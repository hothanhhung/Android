package com.hunght.numberlink;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hunght.data.GameItem;
import com.hunght.data.StaticData;

public class GameActivity extends AppCompatActivity {

    private GameItem gameItem;
    private PuzzleView puzzleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameItem = (GameItem) getIntent().getSerializableExtra(StaticData.GAME_ITEM_KEY);
        puzzleView = (PuzzleView)findViewById(R.id.puzzleView);
    }

    public String getGameItemString(int x, int y) {
        return gameItem.getGameCurrentInString(x, y);
    }

    public boolean canChangeValue(int x, int y) {
        return gameItem.canChange(x, y);
    }

    public boolean canSeeValue(int x, int y) {
        return gameItem.canSee(x, y);
    }

    public void resetNumberButton(){}

    public boolean setTileIfValid(int x, int y, int val)
    {
        return true;
    }
}
