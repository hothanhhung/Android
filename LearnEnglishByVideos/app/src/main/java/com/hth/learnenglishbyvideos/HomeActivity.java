package com.hth.learnenglishbyvideos;

import java.util.ArrayList;
import java.util.Calendar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.hth.data.Data;
import com.hth.data.ObjectChannel;
import com.hth.data.YouTubeService;
import com.hth.learnenglishbyvideos.R;
import com.hth.owner.OwnerAds;
import com.hth.utils.ParseJSONAds;
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
	private AdView mAdView = null;
	private InterstitialAd interstitial = null;

	private static ArrayList<ObjectChannel> _lstChannels = null;
	private ProgressDialog progressDialog = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		OwnerAds.loadPopupDataAds(this);
		ActionBar actionBar = getActionBar();
		//if(actionBar!=null)
		{
			actionBar.hide();
			actionBar.setDisplayShowHomeEnabled(false);
			actionBar.setDisplayShowTitleEnabled(false);
		}
		LinearLayout layoutEnglishChannels =(LinearLayout)findViewById(R.id.layoutEnglishChannels);
		_lstChannels = Data.getChannels();
		for(int i =0 ; i<_lstChannels.size(); i++)
		{
			if( i == 5){
				addMoreGameButton(layoutEnglishChannels);
				//addDonateButton(layoutEnglishChannels);
			}
			ObjectChannel channel = _lstChannels.get(i); 
			Button button = new Button(this);
			//button.setTextColor(0x0000A0);
			button.setText(channel.getTitle());
			button.setTypeface(button.getTypeface(), Typeface.BOLD);
			button.setTag(channel);			
			button.setLayoutParams(new LinearLayout.LayoutParams(
		        ViewGroup.LayoutParams.MATCH_PARENT,
		            ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
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
			layoutEnglishChannels.addView(button);
		}

		timeForRun = Calendar.getInstance().getTime().getTime();
		mAdView = (AdView) this.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);
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

	private void addMoreGameButton(LinearLayout layoutEnglishChannels) {
		Button button = new Button(this);
		button.setTextColor(Color.BLUE);
		button.setText("More Apps");
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
		//button.setBackgroundColor(Color.WHITE);
		button.setAlpha(0.98f);
		layoutEnglishChannels.addView(button);
	}

	private void addDonateButton(LinearLayout layoutEnglishChannels) {
		Button button = new Button(this);
		button.setTextColor(Color.RED);
		button.setText("Donate!");
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
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, ParseJSONAds.getDonateServer());
				startActivity(browserIntent);
			}
		});
		//button.setBackgroundColor(Color.WHITE);
		button.setAlpha(0.98f);
		layoutEnglishChannels.addView(button);
	}

	@Override
    protected void onPause() {
        if(mAdView!=null) mAdView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
		if(progressDialog != null && progressDialog.isShowing())
		{
			progressDialog.dismiss();
		}
        if(mAdView!=null) mAdView.resume();
		if(!OwnerAds.showPopupAds(this)){
			showInterstitial();
		}
    }

    @Override
    protected void onDestroy() {
    	if(mAdView!=null) mAdView.destroy();
        super.onDestroy();
    }
}
