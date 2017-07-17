package com.hunght.numberlink;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hunght.data.GameItem;
import com.hunght.data.SavedValues;
import com.hunght.data.StaticData;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    private PuzzleView puzzleView;
    private TextView tvTime, tvNumberOfHints;
    private boolean fTimeStart = false;
    private boolean fTimeStop = true;
    Thread timestart;
    SavedValues savedValues;

    private MediaPlayer backgroundMusic = null;
    private boolean isPlayMusic = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvNumberOfHints = (TextView) findViewById(R.id.tvNumberOfHints);
        puzzleView = (PuzzleView)findViewById(R.id.puzzleView);
        savedValues= new SavedValues(this);
        isPlayMusic = savedValues.getRecordPlaybackground();
        backgroundMusic = MediaPlayer.create(this, R.raw.backgroundmusic);
        backgroundMusic.setLooping(true);
        updateHintUI();
        savedValues.setCurrentGameId(StaticData.getCurrentGame().getId());
        ImageButton btSpeaker = (ImageButton) findViewById(R.id.btSpeaker);
        if(isPlayMusic)
        {
            backgroundMusic.start();
            btSpeaker.setImageResource(R.drawable.speaker);
        }else{
            btSpeaker.setImageResource(R.drawable.speaker_mute);
        }
       // timeStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timeStop();
        if (backgroundMusic.isPlaying()) {
            backgroundMusic.pause();
        }
    }
    @Override
    protected void onDestroy() {
        backgroundMusic.release();
        backgroundMusic = null;
        super.onDestroy();
    }
    protected void onResume() {
        super.onResume();
        if (isPlayMusic) {
            backgroundMusic.start();
        }
        timeStart();
        textViewTimer();
        System.gc();
    }
    @Override
    public void onBackPressed()
    {
        finish();
        Intent intent = new Intent(this, LevelActivity.class);
        this.startActivity(intent);
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
        if(StaticData.getCurrentGame().setGameCurrent(x, y, val))
        {
            savedValues.setGameItem(StaticData.getCurrentGame());
            return true;
        }
        return false;
    }

    private void textViewTimer() {
        tvTime.setText(getString(R.string.time).concat(
                timeFormat(Long.valueOf(StaticData.getCurrentGame().getGamePlaySeconds()))));
    }

    private String timeFormat(Long var1) {
        String var2 = String.valueOf((int) (var1.longValue() / 3600L));
        String var3 = String.valueOf((int) (var1.longValue() % 3600L / 60L));
        String var4 = String.valueOf((int) (var1.longValue() % 60L));
        if (var2.length() == 1) {
            var2 = "0".concat(var2);
        }

        if (var3.length() == 1) {
            var3 = "0".concat(var3);
        }

        if (var4.length() == 1) {
            var4 = "0".concat(var4);
        }

        return var2.concat(":").concat(var3).concat(":").concat(var4);
    }
    public void timeStart() {
        if (fTimeStart == false && !StaticData.getCurrentGame().isWin()) {
            fTimeStart = true;
            fTimeStop = false;
            timestart = new Thread() {
                public void run() {
                    try {
                        while (!fTimeStop && !StaticData.getCurrentGame().isWin()) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    textViewTimer();
                                }
                            });
                            sleep(1000L);
                            GameActivity game = GameActivity.this;
                            StaticData.getCurrentGame().setGamePlaySeconds(StaticData.getCurrentGame().getGamePlaySeconds() + 1);
                        }

                        if(StaticData.getCurrentGame().isWin())
                        {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    puzzleView.invalidate();
                                }
                            });
                            savedValues.setGameItem(StaticData.getCurrentGame());
                        }

                    } catch (InterruptedException exception) {
                        exception.printStackTrace();
                    }
                }
            };
            timestart.start();
        }

    }

    public void timeStop() {
        if (fTimeStart) {
            timestart = null;
            fTimeStop = true;
            fTimeStart = false;
        }
    }

    public void btClick(View view) {
        switch (view.getId()){
            case R.id.btUndo:
                StaticData.resetGame();
                puzzleView.invalidate();
                timeStop();
                StaticData.getCurrentGame().setGamePlaySeconds(0);
                timeStart();
                break;
            case R.id.btHint:
                puzzleView.changeIsShowLineValue();
                break;
            case R.id.btSpeaker:
                isPlayMusic = !isPlayMusic;
                savedValues.setRecordPlaybackground(isPlayMusic);
                ImageButton btSpeaker = (ImageButton) findViewById(R.id.btSpeaker);
                if(isPlayMusic)
                {
                    backgroundMusic.start();
                    btSpeaker.setImageResource(R.drawable.speaker);
                }else{
                    backgroundMusic.pause();
                    btSpeaker.setImageResource(R.drawable.speaker_mute);
                }
                break;
            case R.id.btRewarded:
                break;
        }
    }

    public void btNumberClick(View view) {
        if(StaticData.isCompleted()) return;
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
                if(StaticData.getCurrentHint() > 0) {
                    puzzleView.setSelectedTile(-2);
                    updateHintUI();
                }
                break;
        }
    }

    private void updateHintUI()
    {
        tvNumberOfHints.setText("" + StaticData.getCurrentHint());
    }
}
