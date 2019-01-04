package com.hth.docbaotonghop;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.hth.utils.SaveData;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

public class SettingsActivity extends Activity {
    private SlidrInterface slidr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        slidr = Slidr.attach(this);

        if(android.os.Build.VERSION.SDK_INT > 10) hideActionBar();

        Switch swRemoveAds = findViewById(R.id.swRemoveAds);
        Switch swBackToHome = findViewById(R.id.swBackToHome);
        Switch swSwipeToClose = findViewById(R.id.swSwipeToClose);
        Switch swLongClickToShare = findViewById(R.id.swLongClickToShare);

        swRemoveAds.setChecked(WebsitePage.isHideAds);
        swRemoveAds.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                WebsitePage.isHideAds = isChecked;
                if(WebsitePage.isHideAds)
                {
                    synchronized(new Object()){WebsitePage.reloadConfigAds(SettingsActivity.this);}
                }
                SaveData.setHideAds(SettingsActivity.this, WebsitePage.isHideAds);
            }
        });

        swBackToHome.setChecked(SaveData.getBackToHome(SettingsActivity.this));
        swBackToHome.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SaveData.setBackToHome(SettingsActivity.this, isChecked);
            }
        });

        swSwipeToClose.setChecked(SaveData.getSwipeToBack(this));
        swSwipeToClose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SaveData.setSwipeToBack(SettingsActivity.this, isChecked);
                if(isChecked){
                    slidr.unlock();
                }else{
                    slidr.lock();
                }
            }
        });

        swLongClickToShare.setChecked(SaveData.getLongClickToShare(this));
        swLongClickToShare.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SaveData.setLongClickToShare(SettingsActivity.this, isChecked);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void hideActionBar()
    {
        if(getActionBar()!=null) getActionBar().hide();
    }

    protected void onClickBack(View v) {
        finish();
    }

	@Override
    protected  void onResume()
    {
        super.onResume();
        if(slidr!=null){
            if(SaveData.getSwipeToBack(this)){
                slidr.unlock();
            }else{
                slidr.lock();
            }
        }
      //  checkForShowInterstital();
    }

}
