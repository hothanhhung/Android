package com.hth.haitonghop;

import java.util.ArrayList;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.hth.data.Data;
import com.hth.data.ObjectChannel;
import com.hth.data.YouTubeService;
import com.hth.haitonghop.R;
import com.hth.utils.UIUtils;

import android.graphics.Color;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class HomeActivity extends Activity {

	private AdView mAdView = null;
	private static ArrayList<ObjectChannel> _lstChannels = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		actionBar.setDisplayShowHomeEnabled(false);
	    actionBar.setDisplayShowTitleEnabled(false);
	    
		LinearLayout layoutEnglishChannels =(LinearLayout)findViewById(R.id.layoutEnglishChannels);
		_lstChannels = Data.getChannels();
		for(int i =0 ; i<_lstChannels.size(); i++)
		{
			ObjectChannel channel = _lstChannels.get(i); 
			Button button = new Button(this);
			button.setTextColor(Color.DKGRAY);
			button.setText(channel.getTitle());
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

					ObjectChannel channel = (ObjectChannel) v.getTag();
					YouTubeService.setCurrentChannel(channel);
					Intent channelIntent = new Intent(HomeActivity.this, MainActivity.class);
	                startActivity(channelIntent);
				}
			});
			layoutEnglishChannels.addView(button);
		}

		
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
        if(mAdView!=null) mAdView.resume();
    }

    @Override
    protected void onDestroy() {
    	if(mAdView!=null) mAdView.destroy();
        super.onDestroy();
    }
}