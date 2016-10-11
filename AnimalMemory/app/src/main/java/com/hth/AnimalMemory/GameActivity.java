package com.hth.AnimalMemory;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.hth.utils.MethodsHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    private static int ROW_COUNT = -1;
    private static int COL_COUNT = -1;
    private Activity activity;
    private int backImage;
    private int [] [] cards;
    private List<Integer> images;
    private Card firstCard;
    private Card secondCard;
    private ButtonListener buttonListener;

    private int numberOfHints, level, btHeight=0, btWidth = 0, screenH = 0, screenW = 0;

    private static Object lock = new Object();

    int numberOfTries;
    long timeInGame = 0;
    private TableLayout mainTable;
    private UpdateCardsHandler handler;
    private TextView tvTimeGame;
    private TextView tvNumberOfTriesGame;
    private TextView tvLevel;
    private TextView tvTimeHintCountDown;
    private Button btHint;
    private MediaPlayer backgroundMusic = null;
    private boolean isPlayMusic = true;
    private boolean isGameover = false;
    SavedValues savedValues;
    Dialog ingameMenu, wonGameDialog;

    ImageButton btSoundInGame;
    private AdView mAdView = null;
    private InterstitialAd interstitial = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);
        activity = this;
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        screenH = displaymetrics.heightPixels - 240;
        screenW = displaymetrics.widthPixels - 70;

        handler = new UpdateCardsHandler();
        loadImages();
        backImage = R.drawable.item;

        buttonListener = new ButtonListener();

        mainTable = (TableLayout) findViewById(R.id.TableLayout03);

        savedValues = new SavedValues(activity);
        tvNumberOfTriesGame = (TextView)findViewById(R.id.tvNumberOfTriesGame);
        tvTimeGame = (TextView)findViewById(R.id.tvTimeGame);
        tvLevel = (TextView)findViewById(R.id.tvLevel);
        tvTimeHintCountDown = (TextView)findViewById(R.id.tvTimeHintCountDown);
        btHint = (Button)findViewById(R.id.btHint);
        btSoundInGame = (ImageButton)findViewById(R.id.btSoundInGame);
        initGame();
        ingameMenu = createIngameMenu();
        isPlayMusic = savedValues.getRecordPlaybackground();
        backgroundMusic = MediaPlayer.create(activity, R.raw.backgroundmusic);
        backgroundMusic.setLooping(true);
        if (isPlayMusic) {
            backgroundMusic.start();
            btSoundInGame.setImageResource(R.drawable.speaker);
        } else {
            btSoundInGame.setImageResource(R.drawable.speaker_mute);
        }

        mAdView = (AdView) this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        timePlay= System.currentTimeMillis();
    }

    public void initGame() {


        int diff = activity.getIntent().getIntExtra(Data.DIFFICULTY_KEY, Data.DIFFICULTY_NEWGAME);
        if(diff == Data.DIFFICULTY_CONTINUES)
        {
            level = savedValues.getRecordLevel();
            newGame(true);
        }else {
            savedValues.setRecordNumberOfHints(0);
            level = 1;
            newGame(false);
            savedValues.setRecordLevel(level);
        }

    }

    private void saveValues()
    {
        savedValues.setRecordCard(cards);
        savedValues.setRecordNumberOfTries(numberOfTries);
        savedValues.setRecordTime(timeInGame);
    }

    private void newGame(boolean isContinue) {
        cards = null;
        if(isContinue) {
            cards = savedValues.getRecordCard();
            numberOfTries = savedValues.getRecordNumberOfTries();
            timeInGame = savedValues.getRecordTime();
        }
        if(cards == null) {
            cards = Data.getCardsForLevel(level);
            numberOfTries =0;
            timeInGame = 0;
            saveValues();
        }
        numberOfHints = savedValues.getRecordNumberOfHints();

        ROW_COUNT = cards.length;
        COL_COUNT = cards[0].length;

        btWidth = screenW / COL_COUNT;
        btHeight = (int) (btWidth * 45/38);
        if(btHeight * ROW_COUNT >= screenH){
            btHeight = screenH / ROW_COUNT;
            btWidth = (int) (btHeight * 38/45);
        }
        /*if(btWidth > 57)
        {
            btWidth = 57;
            btHeight = 67;
        }*/

        if(btWidth > 76)
        {
            btWidth = 76;
            btHeight = 90;
        }

        TableRow tr = ((TableRow)findViewById(R.id.TableRow03));
        tr.removeAllViews();

        mainTable = new TableLayout(activity);
        tr.addView(mainTable);

        for (int x = 0; x < ROW_COUNT; x++) {
            mainTable.addView(createRow(x));
        }

        firstCard=null;
        tvNumberOfTriesGame.setText("" + numberOfTries);
        tvLevel.setText("" + level);
        tvTimeGame.setText(MethodsHelper.getTimeFormat(timeInGame));
        updateHint();
        isGameover = false;
        this.timeStart();

    }

    private void updateHint()
    {
        savedValues.setRecordNumberOfHints(numberOfHints);
        btHint.setText("" + numberOfHints);
        btHint.setEnabled(numberOfHints > 0);
        btHint.setAlpha(numberOfHints > 0 ? 1 : 0.5f);
    }

    private void loadImages() {
        images = new ArrayList<Integer>();

        images.add(R.drawable.item0);
        images.add(R.drawable.item1);
        images.add(R.drawable.item2);
        images.add(R.drawable.item3);
        images.add(R.drawable.item4);
        images.add(R.drawable.item5);
        images.add(R.drawable.item6);
        images.add(R.drawable.item7);
        images.add(R.drawable.item8);
        images.add(R.drawable.item9);
        images.add(R.drawable.item10);
        images.add(R.drawable.item11);
        images.add(R.drawable.item12);
        images.add(R.drawable.item13);
        images.add(R.drawable.item14);
        images.add(R.drawable.item15);
        images.add(R.drawable.item16);
        images.add(R.drawable.item17);
        images.add(R.drawable.item18);
        images.add(R.drawable.item19);
        images.add(R.drawable.item20);
        images.add(R.drawable.item21);
        images.add(R.drawable.item22);
        images.add(R.drawable.item23);
        images.add(R.drawable.item24);
        images.add(R.drawable.item25);
        images.add(R.drawable.item26);
        images.add(R.drawable.item27);
        images.add(R.drawable.item28);
        images.add(R.drawable.item29);
        images.add(R.drawable.item30);
        images.add(R.drawable.item31);
        images.add(R.drawable.item32);
        images.add(R.drawable.item33);
        images.add(R.drawable.item34);
        images.add(R.drawable.item35);

    }

    private TableRow createRow(int x){
        TableRow row = new TableRow(activity);
        row.setHorizontalGravity(Gravity.CENTER);

        for (int y = 0; y < COL_COUNT; y++) {
            View view = createImageButton(x, y);
            row.addView(view);
            if(cards[x][y]<0){
                view.setVisibility(View.INVISIBLE);
            }

        }
        return row;
    }

    private View createImageButton(int x, int y){
        ImageButton button = new ImageButton(activity);
        button.setAdjustViewBounds(true);
        button.setPadding(0, 0, 0, 0);
        button.setLayoutParams(new TableRow.LayoutParams(btWidth, btHeight));
        button.setScaleType(ImageView.ScaleType.FIT_XY);
        button.setImageResource(backImage);
        //button.setImageResource(getImageCard(cards[x][y]));
        button.setId(100*x+y);
        button.setOnClickListener(buttonListener);
        return button;
    }

    private int getImageCard(int value)
    {
        int index = value;
        if(index < 0) {
            index = (-1 * value);
        }
       // index = index -1;
        if(index < 0){
            index = 0;
        }
        if(index > images.size() - 1) {
            index = images.size() - 1;
        }
        return images.get(index);
    }

    private boolean checkWin()
    {
        for(int i=0; i<ROW_COUNT; i++)
            for(int j = 0; j<COL_COUNT; j++)
            {
                if(cards[i][j] > 0) return false;
            }
        return true;
    }

    private void showOrHideAll(boolean isShow)
    {
        for(int i =0; i< mainTable.getChildCount(); i++)
        {
            TableRow row = (TableRow) mainTable.getChildAt(i);
            for(int j =0; j< row.getChildCount(); j++)
            {
                if(cards[i][j] > 0){
                    ImageButton imageButton = (ImageButton) row.getChildAt(j);
                    if(isShow){
                        imageButton.setImageResource(getImageCard(cards[i][j]));
                    }else{
                        imageButton.setImageResource(backImage);
                    }
                }
            }

        }
        if(isShow){
            timeHintCountDown=5;
            tvTimeHintCountDown.setVisibility(View.VISIBLE);
            tvTimeHintCountDown.setText(""+timeHintCountDown);
            tvTimeGame.setVisibility(View.GONE);
            timeStop();
        }else{
            tvTimeHintCountDown.setVisibility(View.GONE);
            tvTimeGame.setVisibility(View.VISIBLE);
            if(!gameIsPause)timeStart();
        }
    }
    class ButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if(timeHintCountDown > 0) return;
            synchronized (lock) {
                if(firstCard!=null && secondCard != null)
                {
                    if(cards[secondCard.x][secondCard.y] == cards[firstCard.x][firstCard.y]) {
                        return;
                    }else{
                        secondCard.button.setImageResource(backImage);
                        firstCard.button.setImageResource(backImage);
                        firstCard = null;
                        secondCard = null;
                    }
                }

                int id = v.getId();
                int x = id/100;
                int y = id%100;
                turnCard((ImageButton)v,x,y);
            }

        }

        private Timer tCheckCard;
        private void turnCard(ImageButton button,int x, int y) {
            button.setImageResource(getImageCard(cards[x][y]));

            if(firstCard==null){
                firstCard = new Card(button,x,y);
            }
            else{

                if(firstCard.x == x && firstCard.y == y){
                    return; //the user pressed the same card
                }

                secondCard = new Card(button,x,y);

                numberOfTries++;
                tvNumberOfTriesGame.setText(""+ numberOfTries);

                if(cards[secondCard.x][secondCard.y] == cards[firstCard.x][firstCard.y]) {
                    TimerTask tt = new TimerTask() {

                        @Override
                        public void run() {
                            try {
                                synchronized (lock) {
                                    handler.sendEmptyMessage(0);
                                }
                            } catch (Exception e) {
                                Log.e("E1", e.getMessage());
                            }
                        }
                    };
                    if (tCheckCard != null) tCheckCard.cancel();
                    tCheckCard = new Timer(false);
                    tCheckCard.schedule(tt, 500);
                }
            }


        }

    }

    class UpdateCardsHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            synchronized (lock) {
                checkCards();
            }
        }
        public void checkCards() {
            if (firstCard != null && secondCard != null) {
                if (cards[secondCard.x][secondCard.y] == cards[firstCard.x][firstCard.y]) {
                    firstCard.button.setVisibility(View.INVISIBLE);
                    secondCard.button.setVisibility(View.INVISIBLE);
                    cards[firstCard.x][firstCard.y] = -1*cards[firstCard.x][firstCard.y];
                    cards[secondCard.x][secondCard.y] = -1*cards[secondCard.x][secondCard.y];
                    saveValues();
                    if(checkWin())
                    {
                        showInterstitial();
                        timeStop();
                        level = level + 1;
                        savedValues.setRecordCard(null);
                        savedValues.setRecordLevel(level);
                        numberOfHints ++;
                        updateHint();
                        if (isPlayMusic) {
                            MediaPlayer level_win = MediaPlayer.create(activity, R.raw.level_win);
                            level_win.start();
                        }
                        if(wonGameDialog == null)
                        {
                            wonGameDialog = createWonGameDialog();
                        }
                        isGameover = true;
                        wonGameDialog.show();
                    }
                } else {
                    secondCard.button.setImageResource(backImage);
                    firstCard.button.setImageResource(backImage);
                }

                firstCard = null;
                secondCard = null;
            }
        }
    }

    private Dialog createIngameMenu()
    {
        final Dialog dialog = new Dialog(activity);
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
                newGame(false);
            }
        });

        btIngameExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        // dialog.setGravity(Gravity.CENTER, 0, 0);
        dialog.setCancelable(false);
        return dialog;
    }

    private Dialog createWonGameDialog()
    {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.gameover);
        ColorDrawable dialogColor = new ColorDrawable(Color.GRAY);
        dialogColor.setAlpha(0);
        dialog.getWindow().setBackgroundDrawable(dialogColor);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);;

        Button btMainMenu = (Button) dialog.findViewById(R.id.btMainMenu);
        Button btNextLevel = (Button) dialog.findViewById(R.id.btNextLevel);

        btNextLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                newGame(false);
            }
        });

        btMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        dialog.setCancelable(false);
        return dialog;
    }

    public void btInGame_Click(View view) {
        switch (view.getId()) {
            case R.id.btPauseInGame:
                gameIsPause = true;
                timeStop();
                ingameMenu.show();
                break;
            case R.id.btSoundInGame:
                isPlayMusic = !isPlayMusic;
                savedValues.setRecordPlaybackground(isPlayMusic);
                if (isPlayMusic) {
                    backgroundMusic.start();
                    btSoundInGame.setImageResource(R.drawable.speaker);
                } else {
                    backgroundMusic.pause();
                    btSoundInGame.setImageResource(R.drawable.speaker_mute);
                }
                break;
            case R.id.btHint:
                showOrHideAll(true);
                startHintCountDown();
                numberOfHints --;
                updateHint();
                break;
            case R.id.btNextLevelInGame:
                level = level + 1;
                newGame(false);
                break;
        }
    }

    Thread hintCountDown;
    int timeHintCountDown;
    private void startHintCountDown() {
        hintCountDown = new Thread() {
            public void run() {
                try {
                    while (timeHintCountDown > 0) {
                        sleep(1000L);
                        timeHintCountDown--;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvTimeHintCountDown.setText(""+timeHintCountDown);
                            }
                        });
                    }

                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showOrHideAll(false);
                    }
                });
            }
        };
        hintCountDown.start();
    }


    Thread timestart;

    private boolean fTimeStart = false;
    private boolean fTimeStop = true;
    public void timeStart() {
        if (fTimeStart == false) {
            gameIsPause = false;
            fTimeStart = true;
            fTimeStop = false;
            timestart = new Thread() {
                public void run() {
                    try {
                        while (!fTimeStop) {
                            sleep(1000L);
                            timeInGame = 1 + timeInGame;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvTimeGame.setText(MethodsHelper.getTimeFormat(timeInGame));
                                }
                            });
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

    boolean gameIsPause = false;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void onPause() {
        gameIsPause = true;
        timeStop();
        if (backgroundMusic.isPlaying()) {
            backgroundMusic.pause();
        }
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (ingameMenu.isShowing()) {
            timeStart();
            ingameMenu.dismiss();
        } else if (wonGameDialog != null && wonGameDialog.isShowing()) {
        } else {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showInterstitialAfterResume();
        if (isPlayMusic) {
            backgroundMusic.start();
        }
        if(fTimeStop && !isGameover)
        {
            ingameMenu.show();
        }

    }

    @Override
    protected void onDestroy() {
        backgroundMusic.release();
        backgroundMusic = null;
        super.onDestroy();
    }

    private long timePlay = 0;
    private void showInterstitialAfterResume(){
        if(timePlay!=0 && System.currentTimeMillis() - timePlay > 15 * 60000) {
            showInterstitial();
        }
    }

    private void showInterstitial(){
            if (interstitial == null) {
                interstitial = new InterstitialAd(this);
                interstitial.setAdUnitId(getResources().getString(R.string.ad_unit_id_interstitial));
                interstitial.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        timePlay = System.currentTimeMillis();
                        if (interstitial.isLoaded()) {
                            interstitial.show();
                        }
                    }

                    @Override
                    public void onAdClosed() {
                    }

                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                    }
                });
            }
            AdRequest adRequest_interstitial = new AdRequest.Builder().build();
            interstitial.loadAd(adRequest_interstitial);
    }
}
