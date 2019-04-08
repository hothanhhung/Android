package com.hth.haitonghop;

import java.util.ArrayList;
import java.util.Calendar;

/*import com.chartboost.sdk.CBLocation;
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.ChartboostDelegate;
import com.chartboost.sdk.Model.CBError;*/
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.hth.data.Data;
import com.hth.data.ObjectChannel;
import com.hth.data.YouTubeService;
import com.hth.haitonghop.R;
import com.hth.utils.UIUtils;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class HomeActivity extends Activity {

    private static long timeForRun = 0;
    private int countShow = 0;
    private InterstitialAd interstitial = null;
	private AdView mAdView = null;
	private static ArrayList<ObjectChannel> _lstChannels = null;
	private ProgressDialog progressDialog = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		ActionBar actionBar = getActionBar();
		if(actionBar!=null) {
			actionBar.hide();
			actionBar.setDisplayShowHomeEnabled(false);
			actionBar.setDisplayShowTitleEnabled(false);
		}
		/*Chartboost.startWithAppId(this, "5c91a84d4d8f000d2a0579aa", "8026e22e4ac0e2f84d57abf1aa884bcd8f7b0564");
		Chartboost.onCreate(this);
		Chartboost.setDelegate(yourDelegateObject);
		Chartboost.cacheInterstitial(CBLocation.LOCATION_HOME_SCREEN);
		Chartboost.cacheRewardedVideo(CBLocation.LOCATION_HOME_SCREEN);*/

		LinearLayout layoutEnglishChannels =(LinearLayout)findViewById(R.id.layoutEnglishChannels);
		_lstChannels = Data.getChannels();
		for(int i =0 ; i<_lstChannels.size(); i++)
		{
			if( i == 4){
				addMoreGameButton(layoutEnglishChannels);
			}
			ObjectChannel channel = _lstChannels.get(i); 
			Button button = new Button(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);

			params.setMargins(2, 5, 2, 5);
			button.setLayoutParams(params);

			button.setTextColor(Color.DKGRAY);
			button.setText(channel.getTitle());
			button.setTypeface(button.getTypeface(), Typeface.BOLD);
			button.setTag(channel);

			button.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!UIUtils.isOnline(HomeActivity.this)) {
						UIUtils.showAlertErrorNoInternet(HomeActivity.this, false);
						return;
					}
					if(progressDialog == null)
					{
						progressDialog = UIUtils.showPopUpLoading(HomeActivity.this);
					}else{
						progressDialog.show();
					}
					ObjectChannel channel = (ObjectChannel) v.getTag();
					YouTubeService.setCurrentChannel(channel);
					Intent channelIntent = new Intent(HomeActivity.this, MainActivity.class);
	                startActivity(channelIntent);
				}
			});
			button.setBackgroundColor(Color.CYAN);
			button.setAlpha(0.9f);
			layoutEnglishChannels.addView(button);
		}

		
		mAdView = (AdView) this.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);
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
	private void addMoreGameButton(LinearLayout layoutEnglishChannels) {
		Button button = new Button(this);
		button.setTextColor(Color.BLUE);
		button.setText("More Games");
		button.setTypeface(button.getTypeface(), Typeface.BOLD);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);

		params.setMargins(2, 5, 2, 5);
		button.setLayoutParams(params);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!UIUtils.isOnline(HomeActivity.this)) {
					UIUtils.showAlertErrorNoInternet(HomeActivity.this, false);
					return;
				}
				UIUtils.showAlertGetMoreAppsServer(HomeActivity.this);
			}
		});
		button.setBackgroundColor(Color.CYAN);
		button.setAlpha(0.98f);
		layoutEnglishChannels.addView(button);
	}

	@Override
    protected void onPause() {
        if(mAdView!=null) mAdView.pause();
        super.onPause();
		//Chartboost.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
		if(progressDialog != null && progressDialog.isShowing())
		{
			progressDialog.dismiss();
		}
        if(mAdView!=null) mAdView.resume();
		//Chartboost.onResume(this);
		showInterstitial();
    }

    @Override
    protected void onDestroy() {
    	if(mAdView!=null) mAdView.destroy();
        super.onDestroy();
		//Chartboost.onDestroy(this);
    }

	@Override
	public void onStart() {
		super.onStart();
		//Chartboost.onStart(this);
	}

	@Override
	public void onBackPressed() {
		// If an interstitial is on screen, close it.
		/*if (Chartboost.onBackPressed())
			return;
		else*/
			super.onBackPressed();
	}
}
