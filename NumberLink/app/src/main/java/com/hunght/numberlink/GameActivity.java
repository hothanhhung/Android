package com.hunght.numberlink;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.hunght.data.DataProcess;
import com.hunght.data.GameItem;
import com.hunght.data.SavedValues;
import com.hunght.data.StaticData;
import com.hunght.utils.UIUtils;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity implements RewardedVideoAdListener {

    private PuzzleView puzzleView;
    private TextView tvTime, tvNumberOfHints;
    private boolean fTimeStart = false;
    private boolean fTimeStop = true;
    private boolean isShowLines = true;
    Thread timestart;
    SavedValues savedValues;

    private MediaPlayer backgroundMusic = null;
    private boolean isPlayMusic = true;
    Dialog loadingDialog;

    private AdView mAdView = null;
    static private boolean isShowInterstitial = true;
    private InterstitialAd interstitial = null;
    private RewardedVideoAd mAd;

    private TableLayout llPlayLayout;
    private LinearLayout llWinLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.


        tvTime = (TextView) findViewById(R.id.tvTime);
        tvNumberOfHints = (TextView) findViewById(R.id.tvNumberOfHints);
        puzzleView = (PuzzleView)findViewById(R.id.puzzleView);
        llPlayLayout = findViewById(R.id.llPlayLayout);
        llWinLayout = findViewById(R.id.llWinLayout);

        savedValues= new SavedValues(this);
        isShowLines = savedValues.getNeedShowLines();
        isPlayMusic = savedValues.getRecordPlaybackground();
        StaticData.setCurrentHint(savedValues.getRecordNumberOfHints());
        backgroundMusic = MediaPlayer.create(this, R.raw.backgroundmusic);
        backgroundMusic.setLooping(true);
        updateHintUI();
        savedValues.setCurrentGameId(StaticData.getCurrentGame().getId());
        ImageButton btShowLines = (ImageButton) findViewById(R.id.btShowLines);
        if(isShowLines)
        {
            btShowLines.setImageResource(R.drawable.link);
        }else{
            btShowLines.setImageResource(R.drawable.unlink);
        }
        puzzleView.changeIsShowLineValue(isShowLines);
        ImageButton btSpeaker = (ImageButton) findViewById(R.id.btSpeaker);
        if(isPlayMusic)
        {
            backgroundMusic.start();
            btSpeaker.setImageResource(R.drawable.speaker);
        }else{
            btSpeaker.setImageResource(R.drawable.speaker_mute);
        }
       // timeStart();
        mAdView = (AdView) this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAd = MobileAds.getRewardedVideoAdInstance(this);
        mAd.setRewardedVideoAdListener(this);
        timePlay= System.currentTimeMillis();
        updateLayOut(StaticData.getCurrentGame().isWin());
    }

    private void updateLayOut(boolean isWin){
        if(isWin){
            llPlayLayout.setVisibility(View.GONE);
            llWinLayout.setVisibility(View.VISIBLE);
            ArrayList<GameItem> gameItems = DataProcess.getNextAndPreviousGameItem(StaticData.getCurrentGame().getId(), this);
            Button btPreviousGame = findViewById(R.id.btPreviousGame);
            Button btNextGame = findViewById(R.id.btNextGame);
            if(gameItems.get(0) == null){
                btPreviousGame.setVisibility(View.INVISIBLE);
            }else{
                btPreviousGame.setVisibility(View.VISIBLE);
                btPreviousGame.setTag(gameItems.get(0));
            }

            if(gameItems.get(1) == null){
                btNextGame.setVisibility(View.INVISIBLE);
            }else{
                btNextGame.setVisibility(View.VISIBLE);
                btNextGame.setTag(gameItems.get(1));
            }

        }else{
            llPlayLayout.setVisibility(View.VISIBLE);
            llWinLayout.setVisibility(View.GONE);
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
        timeStart();
        textViewTimer();
        System.gc();
        showInterstitial(false);
    }
    @Override
    public void onBackPressed()
    {
        finish();
        Intent intent = new Intent(this, LevelActivity.class);
        this.startActivity(intent);
    }

    private long timePlay = 0;
    private void showInterstitial(boolean needShow) {
        if(needShow || (timePlay!=0 && System.currentTimeMillis() - timePlay > 15 * 60000)) {
            if (interstitial == null) {
                interstitial = new InterstitialAd(this);
                interstitial.setAdUnitId(getResources().getString(R.string.ad_unit_id_interstitial));
                interstitial.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        if (interstitial.isLoaded()) {
                            timePlay = System.currentTimeMillis();
                            runOnUiThread(new Runnable() {
                                @Override public void run() {
                                        interstitial.show();
                                }
                            });
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
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AdRequest adRequest_interstitial = new AdRequest.Builder().build();
                    interstitial.loadAd(adRequest_interstitial);
                }
            });
        }
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
            updateLayOut(false);
            fTimeStart = true;
            fTimeStop = false;
            timestart = new Thread() {
                public void run() {
                    try {
                        long start = System.currentTimeMillis();
                        long startTime = StaticData.getCurrentGame().getGamePlaySeconds();

                        while (!fTimeStop && !StaticData.getCurrentGame().isWin()) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    textViewTimer();
                                }
                            });
                            sleep(1000L);
                            GameActivity game = GameActivity.this;
                            StaticData.getCurrentGame().setGamePlaySeconds(startTime + (System.currentTimeMillis() - start)/1000);
                        }

                        if(StaticData.getCurrentGame().isWin())
                        {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    updateLayOut(true);
                                }
                            });
                            showInterstitial(true);
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
            case R.id.btPreviousGame:
            case R.id.btNextGame:
                GameItem item = (GameItem)view.getTag();
                if(item != null){
                    StaticData.setCurrentGame(item);
                    updateLayOut(StaticData.getCurrentGame().isWin());
                    puzzleView.invalidate();
                    textViewTimer();
                    timeStop();
                    if(!StaticData.getCurrentGame().isWin())
                        timeStart();
                }
                break;
            case R.id.btReplayGame:
            case R.id.btUndo:
                AlertDialog diaBox = AskOption();
                diaBox.show();
                break;
            case R.id.btShowLines:
                isShowLines=!isShowLines;
                ImageButton btShowLines = (ImageButton) findViewById(R.id.btShowLines);
                if(isShowLines)
                {
                    btShowLines.setImageResource(R.drawable.link);
                }else{
                    btShowLines.setImageResource(R.drawable.unlink);
                }
                puzzleView.changeIsShowLineValue(isShowLines);
                savedValues.setNeedShowLines(isShowLines);
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
                timeStop();
                if(rewardedVideoAdsDialog == null){
                    rewardedVideoAdsDialog = createRewardedVideoAds();
                }
                if(!rewardedVideoAdsDialog.isShowing()){
                    rewardedVideoAdsDialog.show();
                }
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
                }else{
                    timeStop();
                    if(rewardedVideoAdsDialog == null){
                        rewardedVideoAdsDialog = createRewardedVideoAds();
                    }
                    if(!rewardedVideoAdsDialog.isShowing()){
                        rewardedVideoAdsDialog.show();
                    }
                }
                break;
        }
    }
    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Reset")
                .setMessage("Do you want to re-play this game?")
              //  .setIcon(R.drawable.delete)

                .setPositiveButton("Re-play", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        StaticData.resetGame();
                        puzzleView.invalidate();
                        timeStop();
                        StaticData.getCurrentGame().setGamePlaySeconds(0);
                        timeStart();
                        dialog.dismiss();
                    }

                })



                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

    }

    private void updateHintUI()
    {
        tvNumberOfHints.setText("" + StaticData.getCurrentHint());
        savedValues.setRecordNumberOfHints(StaticData.getCurrentHint());
    }
    Dialog rewardedVideoAdsDialog;
    private Dialog createRewardedVideoAds()
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.menu_rewarded);
        ColorDrawable dialogColor = new ColorDrawable(Color.GRAY);
        dialogColor.setAlpha(0);
        dialog.getWindow().setBackgroundDrawable(dialogColor);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);;

        LinearLayout btRewardGetHint = (LinearLayout)dialog.findViewById(R.id.btRewardGetHint);
        btRewardGetHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btRewardedAdsClick(view);
            }
        });
        dialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    timeStart();
                }
                return true;
            }
        });
        return dialog;
    }
    public void btRewardedAdsClick(View view) {

        switch (view.getId()) {
            case R.id.btRewardGetHint:
                requestRewardedVideoAdShow();
                break;
            case R.id.btDownloadMoreGame:
                UIUtils.showAlertGetMoreApps(this);
                break;
            /*case R.id.btRewardedTurnOffInterstitialAds:
                requestRewardedVideoAdShow();
                break;
            case R.id.btRewardedTurnOffBannerAds:
                requestRewardedVideoAdShow();
                break;*/
            case R.id.btRewardedCancel:
                if(rewardedVideoAdsDialog !=null) rewardedVideoAdsDialog.dismiss();
                timeStart();
                break;
        }
    }

    private void requestRewardedVideoAdShow()
    {
        loadingDialog = UIUtils.showProgressDialog(loadingDialog, this);
        mAd.loadAd(getResources().getString(R.string.ad_unit_id_rewarded_video), new AdRequest.Builder().build());
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        if(loadingDialog!=null && loadingDialog.isShowing())
        {
            loadingDialog.hide();
        }
        mAd.show();
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        //timeStart();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        StaticData.awardNumberOfHint(1);
        updateHintUI();
        //timeStart();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        if(loadingDialog!=null && loadingDialog.isShowing())
        {
            loadingDialog.hide();
        }
        Toast.makeText(this,"Failed to load video",Toast.LENGTH_LONG).show();
        //timeStart();
    }

    @Override
    public void onRewardedVideoCompleted(){}

    private static final String TAG = "numberlink.inappbilling";
    static final int RC_REQUEST = 10001;
    static final String SKU_GAS = "numberlink.buy10hints";//"android.test.purchased";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
    }

    private void updateUIandSaveData(){
        StaticData.awardNumberOfHint(10);
        updateHintUI();
    }

    private void complain(String message) {
        Log.e(TAG, "**** TrivialDrive Error: " + message);
        Toast.makeText(this, "Error: " + message, Toast.LENGTH_LONG).show();
    }
}
