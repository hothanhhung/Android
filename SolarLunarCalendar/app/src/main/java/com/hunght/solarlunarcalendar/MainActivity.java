package com.hunght.solarlunarcalendar;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import android.util.Log;
import android.view.View;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.interstitial.InterstitialAd ;
import com.hunght.data.MenuLookUpItemKind;
import com.hunght.utils.SharedPreferencesUtils;
import com.hunght.utils.Utils;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "AmDuong";
    public static final String EXTRA_FOR_NAVIGATION_MENU_ID = "EXTRA_FOR_NAVIGATION_MENU_ID";
    private static final int ALARM_VERSION = 5;

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

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = (AdView) this.findViewById(R.id.adView);
        //mAdView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        llMainContent = (LinearLayout) findViewById(R.id.llMainContent);
        llMainContent.removeAllViews();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent liveIntent = new Intent(getApplicationContext(), AReceiver.class);
        if (SharedPreferencesUtils.getAlarmVersion(this) != ALARM_VERSION || PendingIntent.getBroadcast(getApplicationContext(), 0, liveIntent, PendingIntent.FLAG_NO_CREATE) == null) {
            PendingIntent recurring = PendingIntent.getBroadcast(getApplicationContext(), 0, liveIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Calendar updateTime = Calendar.getInstance();
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, updateTime.getTimeInMillis(), 31 * 60 * 1000, recurring);
            //wakeup and starts service in every 45 minutes.

            SharedPreferencesUtils.setAlarmVersion(this, ALARM_VERSION);
            SharedPreferencesUtils.setShowSuggestTuVi(this, 0);
            Log.d(TAG, "AlarmManager scheduled " + ALARM_VERSION);
        } else {
            Log.d(TAG, "AlarmManager existed " + ALARM_VERSION);
        }
        createInterstitialAds();
        int viewId = getIntent().getIntExtra(EXTRA_FOR_NAVIGATION_MENU_ID, 0);
        updateUI(viewId);
        Log.d(TAG, "viewId " + viewId);
    }

    @Override
    protected void onDestroy() {
        //  stopService(mServiceIntent);
        super.onDestroy();
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }


        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 3000);
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
    public void onResume() {
        super.onResume();
        showAdsAfterLongTime(false);

    }

    public final static int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 10000;
    public final static int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE + 1;

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Utils.exportToCsv(this, SharedPreferencesUtils.getNoteItems(this));
                } else {
                    Toast.makeText(this, "Không có quyền để tạo file", Toast.LENGTH_LONG).show();
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Quyền đã được cấp. Vui lòng thử lại", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Không có quyền đọc file", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void updateUI(int id) {
        showAdsAfterLongTime(id == R.id.navTuVi);
        boolean shownAds = true;
        if (R.id.navMoreApp != id && R.id.navShare != id) llMainContent.removeAllViews();
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
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Lịch Âm Dương");
                    String shareMessage = "\nChia sẻ ứng dụng với bạn bè\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "Chia sẻ với"));
                } catch (Exception e) {
                    //e.toString();
                }
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
            case R.id.navTuVi:
                llMainContent.addView(new ViewWithWebViewRequest(this, MenuLookUpItemKind.TuViHangNgay), 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                break;
            case R.id.navBoiTinhCach:
                llMainContent.addView(new ViewWithWebViewRequest(this, MenuLookUpItemKind.BoiTinhCach), 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                break;
            case R.id.navBoiTinhCachVoiNhomMau:
                llMainContent.addView(new ViewWithWebViewRequest(this, MenuLookUpItemKind.BoiTinhCachVoiNhomMau), 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                break;
            case R.id.navBoiTinhCachVoiNgaySinh:
                llMainContent.addView(new ViewWithWebViewRequest(this, MenuLookUpItemKind.BoiTinhCachVoiNgayThangNamSinh), 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                break;
            case R.id.navBoiTenAiCap:
                llMainContent.addView(new ViewWithWebViewRequest(this, MenuLookUpItemKind.BoiTenAiCap), 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                break;
            case R.id.navGiaiDiem:
                llMainContent.addView(new ViewWithWebViewRequest(this, MenuLookUpItemKind.GiaiDiem), 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                break;
            case R.id.navBoiBaiTarot:
                llMainContent.addView(new ViewWithWebViewRequest(this, MenuLookUpItemKind.BoiBaiTarot), 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                break;
            case R.id.navGieoQueQuanAm:
                llMainContent.addView(new ViewWithWebViewRequest(this, MenuLookUpItemKind.GieoQueQuanAm), 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                break;
            case R.id.navTietKhi:
                llMainContent.addView(new ViewWithWebViewRequest(this, MenuLookUpItemKind.TietKhi), 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                break;
            case R.id.navMoreApp:
                Utils.showAlertGetMoreAppsServer(this);
                break;
            default:
                shownAds = false;
                llMainContent.addView(new SolarLunarCalendarView(this), 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                break;
        }
        adViewShow(shownAds);
    }

    public void adViewShow(boolean shownAds){
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

    private void showAdsAfterLongTime(boolean shouldShow)
    {
        if(shouldShow || lastShowAds == 0 || ((new Date()).getTime() - lastShowAds == 300000))
        {
            showInterstitial();
        }
    }

    static long lastShowAds = 0;
    private void showInterstitial() {
        if (interstitial == null) {
            createInterstitialAds();
    }else{
            interstitial.show(this);
            lastShowAds = (new Date()).getTime();
        }
    }

    private void createInterstitialAds(){
        if (interstitial == null) {
            AdRequest adRequest = new AdRequest.Builder().build();
            InterstitialAd.load(this,getResources().getString(R.string.interstitial_unit_id), adRequest, new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            // The mInterstitialAd reference will be null until
                            // an ad is loaded.
                            interstitial = interstitialAd;
                            Log.i(TAG, "onAdLoaded");
                            interstitialAd.setFullScreenContentCallback(
                                    new FullScreenContentCallback() {
                                        @Override
                                        public void onAdDismissedFullScreenContent() {
                                            MainActivity.this.interstitial = null;
                                            createInterstitialAds();
                                            Log.d("TAG", "The ad was dismissed.");
                                        }

                                        @Override
                                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                                            MainActivity.this.interstitial = null;
                                            Log.d("TAG", "The ad failed to show.");
                                        }

                                        @Override
                                        public void onAdShowedFullScreenContent() {
                                            // Called when fullscreen content is shown.
                                            Log.d("TAG", "The ad was shown.");
                                        }
                                    });

                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error
                            Log.i(TAG, loadAdError.getMessage());
                            interstitial = null;
                        }
            });
        }
    }
}
