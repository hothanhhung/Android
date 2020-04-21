package com.hunght.myfavoritesites;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.UiThread;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.hunght.data.DataAccessor;
import com.hunght.utils.ConfigAds;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

public class SettingsActivity extends Activity {
    private SlidrInterface slidr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        slidr = Slidr.attach(this);

        if(Build.VERSION.SDK_INT > 10) hideActionBar();

        Switch swUsingInSideBrowser = findViewById(R.id.swUsingInSideBrowser);
        Switch swBackToHome = findViewById(R.id.swBackToHome);
        Switch swSwipeToClose = findViewById(R.id.swSwipeToClose);
        final Switch swSwipeToRightToShare = findViewById(R.id.swSwipeToRightToShare);
        final Switch swLongClickToShare = findViewById(R.id.swLongClickToShare);
        Switch swOptimation = findViewById(R.id.swOptimation);

        swOptimation.setChecked(DataAccessor.getUsingImproveBrowser(SettingsActivity.this));
        swOptimation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataAccessor.setUsingImproveBrowser(SettingsActivity.this, isChecked);
                if(isChecked){
                    ConfigAds.reloadConfigAdsAsync(SettingsActivity.this);
                }
            }
        });

        swBackToHome.setChecked(DataAccessor.getBackToHome(SettingsActivity.this));
        swBackToHome.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataAccessor.setBackToHome(SettingsActivity.this, isChecked);
            }
        });

        swUsingInSideBrowser.setChecked(DataAccessor.getUsingInsideBrowser(SettingsActivity.this));
        swUsingInSideBrowser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataAccessor.setUsingInsideBrowser(SettingsActivity.this, isChecked);
            }
        });

        swBackToHome.setChecked(DataAccessor.getBackToHome(SettingsActivity.this));
        swBackToHome.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataAccessor.setBackToHome(SettingsActivity.this, isChecked);
            }
        });

        swSwipeToClose.setChecked(DataAccessor.getSwipeToBack(this));
        swSwipeToClose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataAccessor.setSwipeToBack(SettingsActivity.this, isChecked);
                if(isChecked){
                    slidr.unlock();
                }else{
                    slidr.lock();
                }
            }
        });

        swLongClickToShare.setChecked(DataAccessor.getSharingPage(this));
        swLongClickToShare.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    DataAccessor.setSwipeSharingPage(SettingsActivity.this, false);
                    swSwipeToRightToShare.setChecked(false);
                }
                DataAccessor.setSharingPage(SettingsActivity.this, isChecked);
            }
        });

        swSwipeToRightToShare.setChecked(DataAccessor.getSwipeSharingPage(this));
        swSwipeToRightToShare.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    DataAccessor.setSharingPage(SettingsActivity.this, false);
                    swLongClickToShare.setChecked(false);
                }
                DataAccessor.setSwipeSharingPage(SettingsActivity.this, isChecked);
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
            if(DataAccessor.getSwipeToBack(this)){
                slidr.unlock();
            }else{
                slidr.lock();
            }
        }
      //  checkForShowInterstital();
    }

}
