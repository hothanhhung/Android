package com.hth.sudoku;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.camera2.params.TonemapCurve;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private TextView tvMoves;
    private TextView tvTime;
    private boolean fTimeStart = false;
    private boolean fTimeStop = true;
    Thread timestart;
    private long lSeconds = 0;
    private int changes = 0;
    private boolean gameFinished = false;
    private boolean resumeTimer = false;

    private ImageButton btUndo;
    private ArrayList<TrackChange> trackChanges = new ArrayList<TrackChange>();
    private Item puzzle[];
    private PuzzleView puzzleView;
    SavedValues savedValues;
    private int level = Data.DIFFICULTY_EASY;

    private MediaPlayer backgroundMusic = null;
    private boolean isPlayMusic = true;
    Dialog ingameMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else {
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            if(decorView!=null) {
                int uiOptions =  View.SYSTEM_UI_FLAG_HIDE_NAVIGATION ;//| View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
            }
            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
            ActionBar actionBar = getActionBar();
            if(actionBar!=null) actionBar.hide();
        }*/
        setContentView(R.layout.activity_game);
        savedValues = new SavedValues(this);
        tvMoves = (TextView) findViewById(R.id.tvMoves);
        tvTime = (TextView) findViewById(R.id.tvTime);
        btUndo = (ImageButton) findViewById(R.id.btUndo);
        isPlayMusic = savedValues.getRecordPlaybackground();
        backgroundMusic = MediaPlayer.create(this, R.raw.backgroundmusic);
        backgroundMusic.setLooping(true);
        int diff = getIntent().getIntExtra(Data.DIFFICULTY_KEY, Data.DIFFICULTY_EASY);

        puzzleView = (PuzzleView) findViewById(R.id.puzzleView);// new PuzzleView(this);
        //setContentView(puzzleView);
       // puzzleView.requestFocus();
        ImageButton btSpeaker = (ImageButton) findViewById(R.id.btSpeaker);
        ingameMenu = createIngameMenu();
        if(isPlayMusic)
        {
            backgroundMusic.start();
            btSpeaker.setImageResource(R.drawable.speaker);
        }else{
            btSpeaker.setImageResource(R.drawable.speaker_mute);
        }

        initGame(diff);
    }
    private void initGame(int diff)
    {
        changes = 0;
        lSeconds = 0;
        fTimeStart = false;
        fTimeStop = true;
        startGame(getPuzzle(diff));
    }
    private void startGame(Item[] puzzle) {
        this.puzzle = puzzle;
        calculateUsedTiles();

        for(int i = 0; i<9 * 9; i++) {
            int x = i % 9, y = i / 9;
            if (getTile(x, y).canChange()) {
                puzzleView.select(x, y);
                break;
            }
        }

        timeStop();
        changeSetText();
        textViewTimer();
        gameFinished = false;
        timeStart();
        String[] levelNames = getResources().getStringArray(R.array.difficulty);
        if(level > 0 && levelNames!= null && levelNames.length > level - 1) {
            ((TextView) findViewById(R.id.tvLevel)).setText(levelNames[level - 1]);
        }
    }
    private void changeSetText() {
        tvMoves.setText(getString(R.string.changes).concat(
                String.valueOf(changes)));
    }
    private void textViewTimer() {
        tvTime.setText(getString(R.string.time).concat(
                timeFormat(Long.valueOf(lSeconds))));
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
        if (fTimeStart == false && !gameFinished) {
            fTimeStart = true;
            fTimeStop = false;
            timestart = new Thread() {
                public void run() {
                    try {
                        while (!fTimeStop && !gameFinished) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    textViewTimer();
                                }
                            });
                            sleep(1000L);
                            GameActivity game = GameActivity.this;
                            game.lSeconds = 1 + game.lSeconds;
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
            resumeTimer = true;
        }
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
        if (resumeTimer == true && gameFinished == false) {
            if(!ingameMenu.isShowing()){
                ingameMenu.show();
            }
            /*AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(getResources().getString(R.string.dialogTitle))
                    .setPositiveButton(getResources().getString(R.string.yes),
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0,
                                                    int arg1) {
                                    timeStart();
                                    arg0.dismiss();

                                }
                            })
                    .setNegativeButton(getResources().getString(R.string.no),
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0,
                                                    int arg1) {
                                    timeStop();
                                    finish();

                                }
                            }).setCancelable(false).show();
*/
        }
        /*if (shareScreen == true) {
            GameScreen.this.finish();
        }*/
        System.gc();
    }

    boolean setTileIfValid(int x, int y, int value) {
        if(getTile(x,y).canChange) {
            int tiles[] = getUsedTiles(x, y);
            if (value != 0) {
                for (int tile : tiles) {
                    if (tile == value) {
                        return false;
                    }
                }
            }
            addToTrackChange(new TrackChange(x, y, getTile(x, y).getValue()));
            getTile(x, y).setValue(value);
            calculateUsedTiles();
            checkWin();
        }
        return true;
    }

    private final int used[][][] = new int[9][9][];

    protected int[] getUsedTiles(int x, int y) {
        return used[x][y];
    }

    private void calculateUsedTiles() {
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                used[x][y] = calculateUsedTiles(x, y);
            }
        }
    }

    private int[] calculateUsedTiles(int x, int y) {
        int c[] = new int[9];

        for (int i = 0; i < 9; i++) {
            if (i == y) {
                continue;
            }
            int t = getTile(x, i).getValue();
            if (t != 0) {
                c[t - 1] = t;
            }
        }

        for (int i = 0; i < 9; i++) {
            if (i == x) {
                continue;
            }
            int t = getTile(i, y).getValue();
            if (t != 0) {
                c[t - 1] = t;
            }
        }

        int startx = x / 3 * 3;
        int starty = y / 3 * 3;

        for (int i = startx; i < startx + 3; i++) {
            for (int j = starty; j < starty + 3; j++) {
                if (i == x && j == y) {
                    continue;
                }
                int t = getTile(i, j).getValue();
                if (t != 0) {
                    c[t - 1] = t;
                }
            }
        }

        int nused = 0;
        for (int t : c) {
            if (t != 0) {
                nused++;
            }
        }

        int c1[] = new int[nused];
        nused = 0;
        for (int t : c) {
            if (t != 0) {
                c1[nused++] = t;
            }
        }

        return c1;
    }

    private void checkWin()
    {
        if(isWin()){
            gameFinished = true;
            timeStop();
            wingameDialog = createWingameDialog("Easy", timeFormat(Long.valueOf(lSeconds)), ""+changes,
                    "10:12:30\nJun 06, 2016", "14:12:30\nJun 06, 2016","");
            wingameDialog.show();
            savedValues.setRecordTime(0);
        }else{
            saveRecord();
        }
    }
    private boolean isWin() {
        for (int i = 0; i < 9 * 9; i++) {
            if (puzzle[i].getValue() == 0) {
                return false;
            }
        }
        return true;
    }

    private void saveRecord()
    {
        savedValues.setRecordPuzzle(puzzle);
        savedValues.setRecordTime(lSeconds);
        savedValues.setRecordChanges(changes);
        savedValues.setRecordTrackchange(trackChanges);
        savedValues.setRecordLevel(level);
    }
    private Item[] getPuzzle(int diff) {
        String puz;
        level = diff;
        switch (diff) {
            case Data.DIFFICULTY_CONTINUES:
                Item[] items = savedValues.getRecordPuzzle();
                level = savedValues.getRecordLevel();
                if(items == null){
                    return getPuzzle(level);
                }else{
                    lSeconds = savedValues.getRecordTime();
                    changes = savedValues.getRecordChanges();
                    trackChanges.clear();
                    TrackChange[] trchs = savedValues.getRecordTrackChange();
                    if(trchs != null){
                        for(TrackChange trch:trchs){
                            trackChanges.add(trch);
                        }
                    }
                    if(trackChanges.size() > 0){
                        btUndo.setClickable(true);
                        if (Build.VERSION.SDK_INT > 10)
                        {
                            btUndo.setAlpha(1f);
                        }
                    }
                    return items;
                }
            case Data.DIFFICULTY_HARD:

                puz = Data.HarPuzzle;
                break;
            case Data.DIFFICULTY_MEDIUM:
                puz = Data.MediumPuzzle;
                break;
            case Data.DIFFICULTY_EASY:
            default:
                puz = Data.EasyPuzzle;
                break;
        }

        return fromPuzzleString(puz);
    }

    protected Item[] fromPuzzleString(String str) {
        Item[] puz = new Item[str.length()];
        for (int i = 0; i < puz.length; i++) {
            puz[i] = new Item(str.charAt(i) - '0');
        }
        return puz;
    }

    private Item getTile(int x, int y) {
        return puzzle[y * 9 + x];
    }

    public String getTileString(int x, int y) {
        return puzzle[y * 9 + x].getValueInString();
    }

    public boolean canChangeValue(int x, int y) {
        return puzzle[y * 9 + x].canChange();
    }

    public boolean canSeeValue(int x, int y) {
        return puzzle[y * 9 + x].canSee();
    }

    private void addToTrackChange(TrackChange trackChange)
    {
        trackChanges.add(trackChange);
        if(trackChanges.size() > 0){
            btUndo.setClickable(true);
            if (Build.VERSION.SDK_INT > 10)
            {
                btUndo.setAlpha(1f);
            }
        }
        changes++;
        changeSetText();
    }

    private TrackChange removeFromTrackChange()
    {
        if(trackChanges.size() < 2){
            btUndo.setClickable(false);
            if (Build.VERSION.SDK_INT > 10){
                btUndo.setAlpha(0.3f);
            }
        }
        if(trackChanges.isEmpty()) return null;
        changes--;
        changeSetText();
        return  trackChanges.remove(trackChanges.size() - 1);
    }

    TrackChange lastTrackChange;
    boolean isRunningUndo = false;
    public void btClick(View view) {
        switch (view.getId()){
            case R.id.btMenu:
                timeStop();
                if(!ingameMenu.isShowing()){
                    ingameMenu.show();
                }
                break;
            case R.id.btUndo:
                if(gameFinished) break;
                if(!trackChanges.isEmpty() && !isRunningUndo){
                    isRunningUndo = true;
                    lastTrackChange = removeFromTrackChange();
                    if(lastTrackChange!=null) {
                        puzzleView.select(lastTrackChange.getX(), lastTrackChange.getY());
                        getTile(lastTrackChange.getX(), lastTrackChange.getY()).setValue(lastTrackChange.getValue());
                        calculateUsedTiles();
                        saveRecord();
                        isRunningUndo = false;
                        lastTrackChange = null;
                        /*new Thread() {
                            public void run() {
                                try {
                                    if (lastTrackChange != null) {
                                        sleep(300);
                                        puzzleView.select(lastTrackChange.getX(), lastTrackChange.getY());

                                    }
                                } catch (Exception ex) {
                                }
                            }
                        }.start();*/
                    }
                }
                break;
            case R.id.btHint:
                if(gameFinished) break;
                int [] tiles = getUsedTiles(puzzleView.getSelX(), puzzleView.getSelY());
                if(tiles == null || tiles.length < 9) {
                    for (int i = 1; i < 10; i++) {
                        if (contains(tiles, i)) continue;
                        hintOnNumberButton(i);
                    }
                    hinted = true;
                }else{
                    Toast toast = Toast.makeText(this, R.string.no_move, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
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
        }
    }

    Dialog wingameDialog;
    private Dialog createWingameDialog(String levelName, String time, String moves, String startOn, String endOn, String comment)
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.win);
        ColorDrawable dialogColor = new ColorDrawable(Color.GRAY);
        dialogColor.setAlpha(0);
        dialog.getWindow().setBackgroundDrawable(dialogColor);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);;

        ((TextView) dialog.findViewById(R.id.tvLevel)).setText(levelName);
        ((TextView) dialog.findViewById(R.id.tvTime)).setText(time);
        ((TextView) dialog.findViewById(R.id.tvMoves)).setText(moves);
        ((TextView) dialog.findViewById(R.id.tvStartOn)).setText(startOn);
        ((TextView) dialog.findViewById(R.id.tvEndOn)).setText(endOn);
        ((EditText) dialog.findViewById(R.id.etComment)).setText(comment);

        Button btWingameSaveComment = (Button) dialog.findViewById(R.id.btWingameSaveComment);
        Button btWingamePlay = (Button) dialog.findViewById(R.id.btWingamePlay);
        Button btWingameProfile = (Button) dialog.findViewById(R.id.btWingameProfile);
        Button btWingameDimiss = (Button) dialog.findViewById(R.id.btWingameDimiss);

        // if button is clicked, close the custom dialog
        btWingameSaveComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GameActivity.this, "No implement", Toast.LENGTH_LONG);
            }
        });

        btWingamePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wingameDialog!=null && wingameDialog.isShowing()){
                    wingameDialog.dismiss();
                }
                initGame(level);
            }
        });

        btWingameProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GameActivity.this, "No implement", Toast.LENGTH_LONG);
            }
        });

        btWingameDimiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wingameDialog!=null && wingameDialog.isShowing()){
                    wingameDialog.dismiss();
                }
            }
        });
        return dialog;
    }
    private Dialog createIngameMenu()
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.menu_ingame);
        ColorDrawable dialogColor = new ColorDrawable(Color.GRAY);
        dialogColor.setAlpha(0);
        dialog.getWindow().setBackgroundDrawable(dialogColor);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);;

        Button btIngameResume = (Button) dialog.findViewById(R.id.btIngameResume);
        Button btIngameRestart = (Button) dialog.findViewById(R.id.btIngameRestart);
        Button btIngameExit = (Button) dialog.findViewById(R.id.btIngameExit);

        // if button is clicked, close the custom dialog
        btIngameResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                timeStart();
            }
        });

        btIngameRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                for(Item item:puzzle){
                    if(item.canChange()){
                        item.setValue(0);
                    }
                }
                changes = 0;
                lSeconds = 0;
                fTimeStart = false;
                trackChanges.clear();
                fTimeStop = true;
                startGame(puzzle);
            }
        });

        btIngameExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // dialog.setGravity(Gravity.CENTER, 0, 0);
        return dialog;
    }

    public static boolean contains(int[] arr, int item) {
        for (int n : arr) {
            if (item == n) {
                return true;
            }
        }
        return false;
    }
    boolean hinted = false;
    public void resetNumberButton()
    {
        if(hinted) {
            Button imgbt;
            imgbt = (Button) findViewById(R.id.keypad_1);
            imgbt.setBackgroundResource(R.drawable.number_default);
            imgbt = (Button) findViewById(R.id.keypad_2);
            imgbt.setBackgroundResource(R.drawable.number_default);
            imgbt = (Button) findViewById(R.id.keypad_3);
            imgbt.setBackgroundResource(R.drawable.number_default);
            imgbt = (Button) findViewById(R.id.keypad_4);
            imgbt.setBackgroundResource(R.drawable.number_default);
            imgbt = (Button) findViewById(R.id.keypad_5);
            imgbt.setBackgroundResource(R.drawable.number_default);
            imgbt = (Button) findViewById(R.id.keypad_6);
            imgbt.setBackgroundResource(R.drawable.number_default);
            imgbt = (Button) findViewById(R.id.keypad_7);
            imgbt.setBackgroundResource(R.drawable.number_default);
            imgbt = (Button) findViewById(R.id.keypad_8);
            imgbt.setBackgroundResource(R.drawable.number_default);
            imgbt = (Button) findViewById(R.id.keypad_9);
            imgbt.setBackgroundResource(R.drawable.number_default);
            hinted = false;
        }
    }
    public void hintOnNumberButton(int number)
    {
        Button imgbt;
        switch (number){
            case 1:
                imgbt = (Button) findViewById(R.id.keypad_1);
                imgbt.setBackgroundResource(R.drawable.number_hint);
                break;
            case 2:
                imgbt = (Button) findViewById(R.id.keypad_2);
                imgbt.setBackgroundResource(R.drawable.number_hint);
                break;
            case 3:
                imgbt = (Button) findViewById(R.id.keypad_3);
                imgbt.setBackgroundResource(R.drawable.number_hint);
                break;
            case 4:
                imgbt = (Button) findViewById(R.id.keypad_4);
                imgbt.setBackgroundResource(R.drawable.number_hint);
                break;
            case 5:
                imgbt = (Button) findViewById(R.id.keypad_5);
                imgbt.setBackgroundResource(R.drawable.number_hint);
                break;
            case 6:
                imgbt = (Button) findViewById(R.id.keypad_6);
                imgbt.setBackgroundResource(R.drawable.number_hint);
                break;
            case 7:
                imgbt = (Button) findViewById(R.id.keypad_7);
                imgbt.setBackgroundResource(R.drawable.number_hint);
                break;
            case 8:
                imgbt = (Button) findViewById(R.id.keypad_8);
                imgbt.setBackgroundResource(R.drawable.number_hint);
                break;
            case 9:
                imgbt = (Button) findViewById(R.id.keypad_9);
                imgbt.setBackgroundResource(R.drawable.number_hint);
                break;

        }
    }
    public void btNumberClick(View view) {
        if(isRunningUndo || gameFinished) return;

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
        }
    }
    void showKeypadOrError(int x, int y) {
        if (canChangeValue(x, y)) {
            int tiles[] = getUsedTiles(x, y);
            if (tiles.length == 9) {
                Toast toast = Toast.makeText(this, R.string.no_move, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else {
                Dialog v = new KeyPad(this, tiles, puzzleView);
                v.show();
            }
        }
    }
}
