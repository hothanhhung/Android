package com.hth.qtcs;

import java.util.Calendar;

import mobi.mclick.ad.Ads;
import mobi.mclick.ad.AdsListener;
import mobi.mclick.ad.AdsRequest;
import mobi.mclick.ad.AdsView;
import mobi.mclick.ad.ErrorCode;
import mobi.mclick.ad.InterstitialAds;
import mobi.mclick.ad.VideoAds;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.hth.qtcs.R;
import com.hth.utils.UIUtils;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;

public class MainActivity extends FragmentActivity implements AdsListener,
		ActionBar.TabListener {

	private static long timeForRun = 0;
    private int countShow = 0;
    
	private AdsView adsView;
	private AdView mAdView = null;
	private VideoAds videoAds;
	private InterstitialAds interstitialAds;
	
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	private String[] tabs = { "Mới nhất", "Nâng cao", "More Apps", "Yêu thích"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		/*tạo AdsView*/
        adsView= new AdsView(this);
        LinearLayout layout = (LinearLayout)findViewById(R.id.adFlexView);
        layout.addView(adsView);
        adsView.loadAds(new AdsRequest());
       // MobileAd.showGift(this,	MobileAd.GIFT_TOP_LEFT);
       // UIUtils.showAlertGetMoreApps(this, "");
        

        videoAds = new VideoAds(this);
        videoAds.setAdsListener(this);
        
        interstitialAds = new InterstitialAds(this);
        interstitialAds.setAdsListener(this);
	}
	
	@Override
    public void onAdsLoaded(Ads ads) {
		// TODO Auto-generated method stub
		if (ads == videoAds) {
			videoAds.show();
            timeForRun = Calendar.getInstance().getTime().getTime();
            countShow++;
        }else if(ads == interstitialAds)
        {
        	interstitialAds.show();
            timeForRun = Calendar.getInstance().getTime().getTime();
            countShow++;
        }
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
        adsView.loadAds(new AdsRequest());
    }

    private void showInterstitial()
    {
        long timenow = Calendar.getInstance().getTime().getTime();
        if(timeForRun > 0 && ((timenow - timeForRun) > ((countShow*200000 ) + 200000)))
        {
        	numberOfFail = 0;
        	switch(countShow%2)
        	{
        	case 0:
                videoAds.loadAds(new AdsRequest());
        		break;
        	case 1:
	            interstitialAds.loadAds(new AdsRequest());
        		break;
        	}
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

	@Override
	public void onAdsClosed(Ads arg0) {
		// TODO Auto-generated method stub
		
	}

	int numberOfFail = 0;
	@Override
	public void onAdsFailedToLoad(Ads ads, ErrorCode arg1) {
		numberOfFail++;
		if(numberOfFail > 2){
			return;
		}
		// TODO Auto-generated method stub
		if (ads == videoAds) {
			interstitialAds.loadAds(new AdsRequest());
        }else if (ads == interstitialAds) {
        	videoAds.loadAds(new AdsRequest());
        }
	}

	@Override
	public void onAdsOpened(Ads arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLeaveApplication(Ads arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}
