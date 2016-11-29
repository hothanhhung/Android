package com.hth.eyetest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.hth.data.Data;
import com.hth.data.Item;
import com.hth.data.QuestionItem;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private AdView mAdView = null;
    private InterstitialAd interstitial = null;

    private TextView tvQuestionContent;
    private GridView grvGameContent;
    private GameContentGridViewAdapter gameContentGridViewAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        grvGameContent = (GridView) findViewById(R.id.grvGameContent);
        tvQuestionContent = (TextView) findViewById(R.id.tvQuestionContent);
        mAdView = (AdView) this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        //mAdView.loadAd(adRequest);
        timePlay= System.currentTimeMillis();
        rand = new Random();
        loadQuestionContent();
    }
    Random rand;
    private void loadQuestionContent()
    {
       // int index = rand.nextInt(Data.gameData.size());
        QuestionItem questionItem = Data.getRandomGameData();
        tvQuestionContent.setText(questionItem.getContentTitle());
        if(gameContentGridViewAdapter == null){
            gameContentGridViewAdapter = new GameContentGridViewAdapter(GameActivity.this, questionItem.createItem(18 * 15), getResources());
        }else{
            gameContentGridViewAdapter.updateData(questionItem.createItem(18 * 15));
        }
        grvGameContent.setAdapter(gameContentGridViewAdapter);
        grvGameContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = (Item) view.getTag();
                if(item.isGoal())
                {
                    foundOut();
                }
            }
        });
    }

    private void foundOut()
    {
        loadQuestionContent();
    }

    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btMenuInGame:
                break;
            case R.id.btSpeaker:
                break;
            case R.id.btHint:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
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

    }

    @Override
    protected void onDestroy() {
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
