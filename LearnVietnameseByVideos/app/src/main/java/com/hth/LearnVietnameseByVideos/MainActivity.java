package com.hth.LearnVietnameseByVideos;

import java.util.Calendar;
import java.util.Random;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.hth.data.YouTubeService;
import com.hth.LearnVietnameseByVideos.R;
import com.hth.utils.UIUtils;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Window;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {
	
	private static long timeForRun = 0;
    private int countShow = 0;
	private AdView mAdView = null;
	private InterstitialAd interstitial = null;

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	private String[] tabs = { "NEWEST","PlayList", "Search", "Favorite" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_main);

		if(!UIUtils.isOnline(this)){
        	UIUtils.showAlertErrorNoInternet(this, false);
        	return;
        } 
		assetManager = getAssets();
		// Initilization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
	    actionBar.setDisplayShowTitleEnabled(false);
	   // actionBar.setTitle(YouTubeService.getCurrentChannelTitle());
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		//actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);		

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}

		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		timeForRun = Calendar.getInstance().getTime().getTime();
		mAdView = (AdView) this.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

	}

	

	@Override
    protected void onPause() {
        if(mAdView!=null) mAdView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showInterstitial();

    }

    private void showInterstitial()
    {
        long timenow = Calendar.getInstance().getTime().getTime();
		long maxTime = ((countShow*200000 ) + 200000);
		if(maxTime > 300000) maxTime = 300000;
        if(timeForRun > 0 && ((timenow - timeForRun) > maxTime))
        {
			if (interstitial == null) {
				interstitial = new InterstitialAd(this);
				interstitial.setAdUnitId(getResources().getString(R.string.interstitial_unit_id));
				interstitial.setAdListener(new AdListener() {
					@Override
					public void onAdLoaded() {
						if(interstitial.isLoaded()){
							interstitial.show();
							timeForRun = Calendar.getInstance().getTime().getTime();
							countShow++;
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
    @Override
    protected void onDestroy() {
    	if(mAdView!=null) mAdView.destroy();
        super.onDestroy();
    }
    
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	private static AssetManager assetManager = null;
	
	public static AssetManager getAssetManager()
	{
		return assetManager;
	}
	
	
}
