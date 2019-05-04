package com.hunght.myfavoritesites;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.hunght.data.DataAccessor;
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
        Switch swLongClickToShare = findViewById(R.id.swLongClickToShare);

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

        /*swLongClickToShare.setChecked(SaveData.getLongClickToShare(this));
        swLongClickToShare.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SaveData.setLongClickToShare(SettingsActivity.this, isChecked);
            }
        });*/
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
