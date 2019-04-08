package com.hth.haitonghop;

import java.util.Calendar;

/*import com.chartboost.sdk.CBLocation;
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.ChartboostDelegate;
import com.chartboost.sdk.Model.CBError;*/
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.hth.haitonghop.R;
import com.hth.utils.UIUtils;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.widget.LinearLayout;

public class MainActivity extends FragmentActivity implements		ActionBar.TabListener {

	private static long timeForRun = 0;
    private int countShow = 0;
    
	private AdView mAdView = null;
	private InterstitialAd interstitial = null;

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	private String[] tabs = { "Mới nhất", "Nâng cao",  "Yêu thích"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_main);

		/*Chartboost.startWithAppId(this, "5c91a84d4d8f000d2a0579aa", "8026e22e4ac0e2f84d57abf1aa884bcd8f7b0564");
		Chartboost.onCreate(this);
		Chartboost.setDelegate(yourDelegateObject);
		Chartboost.cacheInterstitial(CBLocation.LOCATION_HOME_SCREEN);
		Chartboost.cacheRewardedVideo(CBLocation.LOCATION_HOME_SCREEN);*/

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
		mAdView = (AdView) this.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

	}

	/*private ChartboostDelegate yourDelegateObject = new ChartboostDelegate() {
		// Declare delegate methods here, see CBSample project for examples
		public void didFailToLoadInterstitial(String location, CBError.CBImpressionError error) {
			Chartboost.cacheInterstitial(location);
		}

		public void didFailToLoadRewardedVideo(String location, CBError.CBImpressionError error) {
			Chartboost.cacheRewardedVideo(location);
		}
	};
	*/
	@Override
    protected void onPause() {
        if(mAdView!=null) mAdView.pause();
        super.onPause();
	//	Chartboost.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
	//	Chartboost.onResume(this);
        showInterstitial();

    }

    private void showInterstitial()
    {
		long timenow = Calendar.getInstance().getTime().getTime();
		/*long longtime = 350000;//(countShow*300000 );// + 100000;
		//if(longtime > 1000000) longtime = 1000000;
		if(timeForRun == 0){
			timeForRun = Calendar.getInstance().getTime().getTime();
		}
		if((timenow - timeForRun) > longtime)
		{
			//StartAppAd.showAd(this);
			if(Chartboost.hasRewardedVideo(CBLocation.LOCATION_HOME_SCREEN)){
				Chartboost.showRewardedVideo(CBLocation.LOCATION_HOME_SCREEN);
				Chartboost.cacheRewardedVideo(CBLocation.LOCATION_HOME_SCREEN);
				timeForRun = Calendar.getInstance().getTime().getTime();
			}
			else if (Chartboost.hasInterstitial(CBLocation.LOCATION_HOME_SCREEN)) {
				Chartboost.showInterstitial(CBLocation.LOCATION_HOME_SCREEN);
				Chartboost.cacheInterstitial(CBLocation.LOCATION_HOME_SCREEN);
				timeForRun = Calendar.getInstance().getTime().getTime();
			}
		}*/

		timenow = Calendar.getInstance().getTime().getTime();
		long maxTime = ((countShow*200000 ) + 200000);
		if(maxTime > 400000) maxTime = 400000;
		if(timeForRun > 0 && ((timenow - timeForRun) > maxTime))
		{
			if (interstitial == null) {
				interstitial = new InterstitialAd(this);
				interstitial.setAdUnitId(getResources().getString(R.string.ad_unit_id_interstitial));
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
	public void onBackPressed() {
		/*if (Chartboost.onBackPressed())
			return;
		else */
			{
				super.onBackPressed();
		}
	}

	@Override
	public void onStart() {
		super.onStart();
	//	Chartboost.onStart(this);
	}

    @Override
    protected void onDestroy() {
    	if(mAdView!=null) mAdView.destroy();
        super.onDestroy();
	//	Chartboost.onDestroy(this);
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
