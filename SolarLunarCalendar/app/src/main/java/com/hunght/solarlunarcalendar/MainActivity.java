package com.hunght.solarlunarcalendar;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.view.menu.ExpandedMenuView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.hunght.data.DateItemForGridview;
import com.hunght.utils.DateTools;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static int ViewId = 0;

    DateItemForGridview selectedDate;
    DateItemAdapter adapter;
    Button btMonth, btYear;
    TextView tvSolarMonthInfo, tvSolarInfoDate, tvSolarInfoDayInWeek, tvLunarInfoDayInWeek, tvLunarInfoDayInWeek1, tvSolarInfoToday;

    LinearLayout llMainContent;

    private AdView mAdView = null;
    private InterstitialAd interstitial = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);/*
        selectedDate = new DateItemForGridview("", new Date(), false);*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        mAdView = (AdView) this.findViewById(R.id.adView);
        llMainContent = (LinearLayout)findViewById(R.id.llMainContent);
        llMainContent.removeAllViews();
        updateUI(ViewId);
        ViewId = 0;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        MyService mSensorService = new MyService();
        Intent mServiceIntent = new Intent(this, mSensorService.getClass());
        if (!isMyServiceRunning(mSensorService.getClass())) {
            startService(mServiceIntent);
        }
        /*IntentFilter filter = new     IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        BroadcastReceiver mReceiver = new MyReceiver();
        registerReceiver(mReceiver, filter);*/

        Random random = new Random();
        if(random.nextInt(4) == 2)
        {
            showInterstitial();
        }
        lastShowAds = (new Date()).getTime();
    }

    public static void setViewId(int id){
        ViewId = id;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        updateUI(id);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResume(){
        super.onResume();
        showAdsAfterLongTime();

    }

    public void updateUI(int id) {
        showAdsAfterLongTime();
        boolean shownAds = true;
        llMainContent.removeAllViews();
        switch (id) {
            case R.id.navSolarLunarCalendar:
                shownAds = false;
                llMainContent.addView(new SolarLunarCalendarView(this), 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                break;
            case R.id.navExchangeTool:
                llMainContent.addView(new ExchangeToolView(this), 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

                break;
            case R.id.navGoodDayBadDay:
                llMainContent.addView(new GoodDateBabDateView(this), 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

                break;
            case R.id.navPositionOfTheSun:
                break;
            case R.id.navNotes:
                llMainContent.addView(new NotesView(this), 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                break;
            case R.id.navShare:
                break;
            case R.id.navAbout:
                llMainContent.addView(new AboutView(this), 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                break;
            case R.id.navSettings:
                llMainContent.addView(new SettingsView(this), 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                break;
            case R.layout.notes_view_item:
                llMainContent.addView(new SaveNoteItemView(this), 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                break;
            default:
                shownAds = false;
                llMainContent.addView(new SolarLunarCalendarView(this), 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                break;
        }
        if (shownAds) {
            if (mAdView.getVisibility() != View.VISIBLE) {
                mAdView.setVisibility(View.VISIBLE);
                mAdView.loadAd(new AdRequest.Builder().build());
            }
        } else {
            if (mAdView.getVisibility() != View.GONE) {
                mAdView.setVisibility(View.GONE);
            }
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void showAdsAfterLongTime()
    {
        if(lastShowAds != 0 && ((new Date()).getTime() - lastShowAds == 300000))
        {
            showInterstitial();
        }
    }

    long lastShowAds = 0;
    private void showInterstitial() {
        if (interstitial == null) {
            interstitial = new InterstitialAd(this);
            interstitial.setAdUnitId(getResources().getString(R.string.interstitial_unit_id));
            interstitial.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    if (interstitial.isLoaded()) {
                        interstitial.show();
                        lastShowAds = (new Date()).getTime();
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
