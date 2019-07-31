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
import com.hunght.inappbillingutils.IabHelper;
import com.hunght.inappbillingutils.IabResult;
import com.hunght.inappbillingutils.Inventory;
import com.hunght.inappbillingutils.Purchase;
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
        /* In-app purchase */
        mHelper = new IabHelper(this, StaticData.getLicenseKey());
        // enable debug logging (for a production application, you should set this to false).
        mHelper.enableDebugLogging(false);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.

        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    complain("Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d(TAG, "Setup successful. Querying inventory.");
                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });
        /* In-app purchase end*/

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
        if (mHelper != null) {
            mHelper.dispose();
            mHelper = null;
        }
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
            case R.id.btBuyMoreHint:
                String payload = "ssss";
                mHelper.launchPurchaseFlow(GameActivity.this, SKU_GAS, RC_REQUEST,
                        mPurchaseFinishedListener, payload);
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
    IabHelper mHelper;
    static final String SKU_GAS = "numberlink.buy10hints";//"android.test.purchased";

    // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

            // Check for gas delivery -- if we own gas, we should fill up the tank immediately
            Purchase gasPurchase = inventory.getPurchase(SKU_GAS);
            if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                Log.d(TAG, "We have gas. Consuming it.");
                mHelper.consumeAsync(inventory.getPurchase(SKU_GAS), mConsumeFinishedListener);
                return;
            }
            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (mHelper == null) return;

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        }
        else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    /** Verifies the developer payload of a purchase. */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        return true;
    }

    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                complain("Error purchasing: " + result);
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                complain("Error purchasing. Authenticity verification failed.");
                return;
            }

            Log.d(TAG, "Purchase successful.");

            if (purchase.getSku().equals(SKU_GAS)) {
                // bought 1/4 tank of gas. So consume it.
                Log.d(TAG, "Purchase is gas. Starting gas consumption.");
                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
            }
        }
    };

    // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            // We know this is the "gas" sku because it's the only one we consume,
            // so we don't check which sku was consumed. If you have more than one
            // sku, you probably should check...
            if (result.isSuccess()) {
                // successfully consumed, so we apply the effects of the item in our
                // game world's logic, which in our case means filling the gas tank a bit
                Log.d(TAG, "Consumption successful. Provisioning.");
                updateUIandSaveData();
            }
            else {
                complain("Error while consuming: " + result);
            }
            Log.d(TAG, "End consumption flow.");
        }
    };

    private void updateUIandSaveData(){
        StaticData.awardNumberOfHint(10);
        updateHintUI();
    }

    private void complain(String message) {
        Log.e(TAG, "**** TrivialDrive Error: " + message);
        Toast.makeText(this, "Error: " + message, Toast.LENGTH_LONG).show();
    }
}
