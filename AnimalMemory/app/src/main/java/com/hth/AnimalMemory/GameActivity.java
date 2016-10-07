package com.hth.AnimalMemory;

import android.app.Activity;
import android.app.Dialog;
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
import java.util.Random;
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

    private int btHeight=0, btWidth = 0, screenH = 0, screenW = 0;

    private static Object lock = new Object();

    int turns;
    long timeInGame = 0;
    private TableLayout mainTable;
    private UpdateCardsHandler handler;
    private TextView tvTimeGame;
    private TextView tvNumberOfTriesGame;
    private MediaPlayer backgroundMusic = null;
    private boolean isPlayMusic = true;
    private boolean isGameover = false;
    SavedValues savedValues;
    Dialog ingameMenu;

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
        screenH = displaymetrics.heightPixels - 120;
        screenW = displaymetrics.widthPixels;

        handler = new UpdateCardsHandler();
        loadImages();
        backImage = R.drawable.item;

        buttonListener = new ButtonListener();

        mainTable = (TableLayout) findViewById(R.id.TableLayout03);
        savedValues = new SavedValues(activity);
        tvNumberOfTriesGame = (TextView)findViewById(R.id.tvNumberOfTriesGame);
        tvTimeGame = (TextView)findViewById(R.id.tvTimeGame);
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

        int x, y;
        int pos = 7;

        switch (pos) {
            case 1:
                x = 4;
                y = 4;
                break;
            case 2:
                x = 4;
                y = 5;
                break;
            case 3:
                x = 4;
                y = 6;
                break;
            case 4:
                x = 5;
                y = 6;
                break;
            case 5:
                x = 6;
                y = 6;
                break;
            case 6:
                x = 6;
                y = 7;
                break;
            case 7:
                x = 6;
                y = 8;
                break;
            case 8:
                x = 7;
                y = 8;
                break;
            case 9:
                x = 8;
                y = 8;
                break;
            case 10:
                x = 8;
                y = 9;
                break;
            default:
                return;
        }
        btWidth = screenW / x;
        btHeight = (int) (btWidth * 45/38);
        if(btHeight * y >= screenH){
            btHeight = screenH / y;
            btWidth = (int) (btHeight * 38/45);
        }
        if(btWidth > 57)
        {
            btWidth = 57;
            btHeight = 67;
        }
        //btHeight = screenH / y;
        newGame(x, y);

    }

    private void newGame(int c, int r) {
        ROW_COUNT = r;
        COL_COUNT = c;

        cards = new int [COL_COUNT] [ROW_COUNT];

        TableRow tr = ((TableRow)findViewById(R.id.TableRow03));
        tr.removeAllViews();

        mainTable = new TableLayout(activity);
        tr.addView(mainTable);

        for (int y = 0; y < ROW_COUNT; y++) {
            mainTable.addView(createRow(y));
        }

        firstCard=null;
        loadCards();

        turns=0;
        tvNumberOfTriesGame.setText(""+turns);
        timeInGame = 0;
        this.timeStart();

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

    private void loadCards(){
        try{
            int size = ROW_COUNT*COL_COUNT;
            ArrayList<Integer> list = new ArrayList<Integer>();
            for(int i=0;i<size;i++){
                list.add(new Integer(i));
            }

            Random r = new Random();
            for(int i=size-1;i>=0;i--){
                int t=0;
                if(i>0){
                    t = r.nextInt(i);
                }

                t=list.remove(t).intValue();
                cards[i%COL_COUNT][i/COL_COUNT]=t%(size/2);

            }
        }
        catch (Exception e) {
            Log.e("loadCards()", e+"");
        }

    }

    private TableRow createRow(int y){
        TableRow row = new TableRow(activity);
        row.setHorizontalGravity(Gravity.CENTER);

        for (int x = 0; x < COL_COUNT; x++) {
            row.addView(createImageButton(x,y));
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
        button.setId(100*x+y);
        button.setOnClickListener(buttonListener);
        return button;
    }

    class ButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

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
            button.setImageResource(images.get(cards[x][y]));

            if(firstCard==null){
                firstCard = new Card(button,x,y);
            }
            else{

                if(firstCard.x == x && firstCard.y == y){
                    return; //the user pressed the same card
                }

                secondCard = new Card(button,x,y);

                turns++;
                tvNumberOfTriesGame.setText(""+ turns);

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
                    tCheckCard.schedule(tt, 1000);
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
                //ini;
            }
        });

        btIngameExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        // dialog.setGravity(Gravity.CENTER, 0, 0);
        return dialog;
    }

    public void btInGame_Click(View view) {
        if (view.getId() == R.id.btPauseInGame) {
            timeStop();
            ingameMenu.show();
        } else if (view.getId() == R.id.btSoundInGame) {
            isPlayMusic = !isPlayMusic;
            savedValues.setRecordPlaybackground(isPlayMusic);
            if (isPlayMusic) {
                backgroundMusic.start();
                btSoundInGame.setImageResource(R.drawable.speaker);
            } else {
                backgroundMusic.pause();
                btSoundInGame.setImageResource(R.drawable.speaker_mute);
            }
        }
    }
    Thread timestart;

    private boolean fTimeStart = false;
    private boolean fTimeStop = true;
    private void timeStart() {
        if (fTimeStart == false) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showInterstitial();
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
    private void showInterstitial(){
        if(timePlay!=0 && System.currentTimeMillis() - timePlay > 15 * 60000) {
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
}
