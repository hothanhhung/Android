package com.hth.lines;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class GameActivity extends AppCompatActivity {
    private DrawBallPanel drawBallPanel;
    private AdView mAdView = null;
    private InterstitialAd interstitial = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawBallPanel = new DrawBallPanel(this);
        setContentView(R.layout.activity_game);
        LinearLayout pnlGame = (LinearLayout) findViewById(R.id.pnlGame);
        pnlGame.addView(drawBallPanel);
        mAdView = (AdView) this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        timePlay= System.currentTimeMillis();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(drawBallPanel!=null){
            drawBallPanel.onPause();
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
        if(drawBallPanel!=null){
            drawBallPanel.onResume();
        }

    }

    @Override
    protected void onDestroy() {
        if(drawBallPanel!=null){
            drawBallPanel.onDestroy();
        }
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
