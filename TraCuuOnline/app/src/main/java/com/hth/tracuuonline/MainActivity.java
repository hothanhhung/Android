package com.hth.tracuuonline;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd ;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.hth.data.MenuLookUpItem;
import com.hth.data.StaticData;
import com.hth.utils.SavedValues;
import com.hth.utils.UIUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    TextView tvSelectedMenuLookUpItem;
    ListView lvMenuLookUpItems;
    MenuLookUpItemAdapter menuLookUpItemAdapter;
    LinearLayout llMainContent;

    private DrawerLayout mDrawerLayout;
    private LinearLayout mLeftDrawerList;

    private AdView mAdView = null;
    private InterstitialAd interstitial = null;

    private static long timeForRun = 0;
    private static long numberOfSelectItem = 0;
    private static boolean isFirstRun = false;
    private static SavedValues savedValues;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLeftDrawerList = (LinearLayout) findViewById(R.id.leftNavdrawer);

        llMainContent = (LinearLayout) findViewById(R.id.llMainContent);
        tvSelectedMenuLookUpItem = (TextView) findViewById(R.id.tvSelectedMenuLookUpItem);
        lvMenuLookUpItems = (ListView) findViewById(R.id.lvMenuLookUpItems);
        ArrayList<MenuLookUpItem> menuLookUpItems = StaticData.GetMenuLookUpItems();
        menuLookUpItemAdapter = new MenuLookUpItemAdapter(MainActivity.this, menuLookUpItems);

        lvMenuLookUpItems.setAdapter(menuLookUpItemAdapter);
        lvMenuLookUpItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                numberOfSelectItem++;
                checkForShowInterstital();
                MenuLookUpItem menuLookUpItem = (MenuLookUpItem) view.getTag();
                updateUI(menuLookUpItem);
            }
        });
        numberOfSelectItem = 1;
        timeForRun = 0;
        savedValues = new SavedValues(this);
        if(savedValues.getRecordFirstRun() == 0)
        {
            isFirstRun = true;
            savedValues.setRecordFirstRun(Calendar.getInstance().getTime().getTime());
        }else {
            isFirstRun = false;
        }

        createInterstitialAds();

        mAdView = (AdView) this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mDrawerLayout.openDrawer(mLeftDrawerList);

        //updateUI(menuLookUpItems.get(10));
    }

    private void updateUI(MenuLookUpItem menuLookUpItem)
    {
        if(menuLookUpItem.hasAction())
        {
            tvSelectedMenuLookUpItem.setText(menuLookUpItem.getName());
            // vwMainContent.ad
            llMainContent.removeAllViews();
            mDrawerLayout.closeDrawer(mLeftDrawerList);
            llMainContent.addView(menuLookUpItem.getView(MainActivity.this), 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        }else{
            Toast.makeText(MainActivity.this, "Not Implemented Yet", Toast.LENGTH_LONG).show();
        }
    }

    public void menuClick(View view) {
        switch (view.getId()){
            case R.id.btMenuLookUpItems:
                if (mDrawerLayout.isDrawerOpen(mLeftDrawerList)) {
                    mDrawerLayout.closeDrawer(mLeftDrawerList);
                } else {
                    mDrawerLayout.openDrawer(mLeftDrawerList);
                }
                break;
            case R.id.btMoreApp:
                UIUtils.showAlertGetMoreAppsServer(this);
                break;

        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "press BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        if(mAdView!=null) mAdView.destroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if(mAdView!=null) mAdView.pause();
        super.onPause();
    }

    @Override
    protected  void onResume()
    {
        super.onResume();
        checkForShowInterstital();
    }

    private void checkForShowInterstital()
    {
        long timenow = Calendar.getInstance().getTime().getTime();
        long longtime = 300000 ;

        if(!isFirstRun )
        {
            if(timeForRun <= 0)
            {
                timeForRun = Calendar.getInstance().getTime().getTime();
                if ((new Random()).nextInt(3) == 1) {
                    showInterstitial();
                }
            }else if((timenow - timeForRun) > longtime)
            {
                showInterstitial();
            }
            else if(numberOfSelectItem%3 == 0) {
                showInterstitial();
            }
        }
    }

    static  String TAG = "Admob";
    static boolean loadAdsFailed = false;
    private void createInterstitialAds(){
        if (interstitial == null) {
            AdRequest adRequest = new AdRequest.Builder().build();
            InterstitialAd.load(this,getResources().getString(R.string.interstitial_unit_id), adRequest, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    interstitial = interstitialAd;
                    Log.i(TAG, "onAdLoaded");
                    interstitialAd.setFullScreenContentCallback(
                            new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    MainActivity.this.interstitial = null;
                                    createInterstitialAds();
                                    Log.d(TAG, "The ad was dismissed.");
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(AdError adError) {
                                    MainActivity.this.interstitial = null;
                                    Log.d(TAG, "The ad failed to show.");
                                }

                                @Override
                                public void onAdShowedFullScreenContent() {
                                    // Called when fullscreen content is shown.
                                    Log.d(TAG, "The ad was shown.");
                                }
                            });

                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    // Handle the error
                    Log.i(TAG, loadAdError.getMessage());
                    interstitial = null;
                }
            });
        }
    }

    private void showInterstitial() {
        if (interstitial == null) {
            createInterstitialAds();
        } else {
            interstitial.show(this);
            timeForRun = Calendar.getInstance().getTime().getTime();
        }
    }
}
