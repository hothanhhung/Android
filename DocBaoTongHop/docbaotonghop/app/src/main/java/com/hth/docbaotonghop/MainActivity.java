package com.hth.docbaotonghop;


import com.chartboost.sdk.CBLocation;
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.ChartboostDelegate;
import com.chartboost.sdk.Model.CBError;
import com.hth.docbaotonghop.R;
import com.hth.utils.*;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.r0adkll.slidr.model.SlidrListener;
import com.r0adkll.slidr.model.SlidrPosition;
/*
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;
*/

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.Calendar;

public class MainActivity extends Activity {
    static int width_Screen = 0;
    static int height_Screen = 0;
    static WebsitePage current_Website_Page = WebsitePage.VNExpressDotNet;
    private MyWebview viewArticleDetail;
    private SlidrInterface slidr;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static WebsitePage getCurrent_Website_Page() {
        return current_Website_Page;
    }

    public static int getWidth_Screen() {
        return width_Screen;
    }

    public static int getHeight_Screen() {
        return height_Screen;
    }

    Dialog loadingDialog = null;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void hideActionBar()
    {
        if(getActionBar()!=null) getActionBar().hide();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Chartboost.startWithAppId(this, "5c6127198dd6d97dec5b0a49", "b409bc2a1610f1edde98ab2314b70b1bf14c5541");
        Chartboost.onCreate(this);
        Chartboost.setDelegate(yourDelegateObject);
        Chartboost.cacheInterstitial(CBLocation.LOCATION_HOME_SCREEN);
        Chartboost.cacheRewardedVideo(CBLocation.LOCATION_HOME_SCREEN);
        //timeForRun = Calendar.getInstance().getTime().getTime();

        SlidrConfig config = new SlidrConfig.Builder()
              //  .primaryColor(getResources().getColor(R.color.primary)
                     //   .secondaryColor(getResources().getColor(R.color.secondary)
                             //   .position(SlidrPosition.LEFT|SlidrPosition.RIGHT|SlidrPosition.TOP|SlidrPosition.BOTTOM|SlidrPosition.VERTICAL|SlidrPosition.HORIZONTAL)
                                .sensitivity(1f)
                                .scrimColor(Color.BLACK)
                                .scrimStartAlpha(0.8f)
                                .scrimEndAlpha(0f)
                                .velocityThreshold(2400)
                                .distanceThreshold(0.25f)
                                .edge(true|false)
                                .edgeSize(0.18f) // The % of the screen that counts as the edge, default 18%
                              //  .listener(new SlidrListener(){})
                                .build();
        slidr = Slidr.attach(this, config);

        if(android.os.Build.VERSION.SDK_INT > 10) hideActionBar();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        height_Screen = metrics.heightPixels;
        width_Screen = metrics.widthPixels;

        context = getApplicationContext();

        setContentView(R.layout.activity_main_website);
        WebsitePage.isHideAds = SaveData.getHideAds(this);
        /*StartAppSDK.init(this, "207910015", true);
        if(PageApp.IsDisableSplash) StartAppAd.disableSplash();
*/
        ImageButton bthome = (ImageButton) findViewById(R.id.button_home);
        bthome.setImageResource(MainActivity.getCurrent_Website_Page().getIcon());
        onCreateWebsiteMobileStyle();
/*
        adview = (AdView) this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adview.loadAd(adRequest);

         */
        //MobileAd.showGift(this,	MobileAd.GIFT_TOP_CENTER);
/*
        adsView= new AdsView(this);
        LinearLayout layout = (LinearLayout)findViewById(R.id.adFlexView);
        layout.addView(adsView);
        adsView.loadAds(new AdsRequest());
        //changeAddProvider();
*/

        swipeRefreshLayout = ((SwipeRefreshLayout)this.findViewById(R.id.swipeContainer));
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if(viewArticleDetail!=null)
                        {
                            viewArticleDetail.reload();
                            if(swipeRefreshLayout!=null)
                                swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }
        );
    }

    com.hth.utils.MyHomeWebViewClient myHomeWebViewClient;
    public static ProgressBar progressBar1;
    
    @SuppressLint("SetJavaScriptEnabled")
	private void onCreateWebsiteMobileStyle() {
        viewArticleDetail = (MyWebview) findViewById(R.id.viewArticleDetail);
        viewArticleDetail.clearHistory();
        if (current_Website_Page.getHomePageMobile() != "") {
            WebSettings settings = viewArticleDetail.getSettings();
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
            settings.setJavaScriptEnabled(true);
            settings.setAppCacheMaxSize(50000 * 1024);
            settings.setJavaScriptCanOpenWindowsAutomatically(false);
            settings.setBuiltInZoomControls(true);
            settings.setSupportZoom(true);
            settings.setDisplayZoomControls(false);
            settings.setDomStorageEnabled(true);


            progressBar1 = (ProgressBar) this.findViewById(R.id.progressBar1);

            //Uri uri = Uri.parse(current_Website_Page.getHomePageMobile());
            myHomeWebViewClient = new com.hth.utils.MyHomeWebViewClient(this);
            viewArticleDetail.setWebViewClient(myHomeWebViewClient);
            viewArticleDetail.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    if (progress < 90) {
                        progressBar1.setVisibility(View.VISIBLE);
                        if (progressBar1.getProgress() < progress) {
                            progressBar1.setProgress(progress);
                        }
                    } else {
                        progressBar1.setVisibility(View.GONE);
                        try {
                            String encoded = MainActivity.getCurrent_Website_Page().GetReformatCssContent(MainActivity.this);
                            view.loadUrl("javascript:(function() {" +
                                    "var parent = document.getElementsByTagName('head').item(0);" +
                                    "var style = document.createElement('style');" +
                                    "style.type = 'text/css';" +
                                    "style.innerHTML = `" + encoded + "`;" +
                                    "parent.appendChild(style)" +
                                    "})()");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (progress > 70 && myHomeWebViewClient.getCurrentIndex() < 1) {
                        changeAdProvider();
                    }
                }

            });
            needChanged = true;
            myHomeWebViewClient.addListViewedContents(current_Website_Page.getHomePageMobile());
            //viewArticleDetail.loadUrl("http://stackoverflow.com");
            viewArticleDetail.loadUrl(current_Website_Page.getHomePageMobile());
            //viewArticleDetail.loadDataWithBaseURL("file:///android_asset/",ParserData.getArticleDetail(current_Website_Page.getHomePageMobile()), "text/html", "utf-8", null);
        } else {
            viewArticleDetail.loadDataWithBaseURL(null, "No Website Mobile", "text/html", "utf-8", null);
        }
        viewArticleDetail.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(SaveData.getSwipeToBack(MainActivity.this)) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    String shareSubText = viewArticleDetail.getTitle();
                    String shareBodyText = viewArticleDetail.getUrl();
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubText);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
                    startActivity(Intent.createChooser(shareIntent, "Share With"));
                }
                return false;
            }
        });
    }

    private static long timeForRun = 0;
    private void showInterstitial()
    {
        long timenow = Calendar.getInstance().getTime().getTime();
        long longtime = 400000;//(countShow*300000 );// + 100000;
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
        }

    }

    private ChartboostDelegate yourDelegateObject = new ChartboostDelegate() {
        // Declare delegate methods here, see CBSample project for examples
        public void didFailToLoadInterstitial(String location, CBError.CBImpressionError error) {
            Chartboost.cacheInterstitial(location);
        }

        public void didFailToLoadRewardedVideo(String location, CBError.CBImpressionError error) {
            Chartboost.cacheRewardedVideo(location);
        }
    };

    @Override
    public void onBackPressed() {
        if (Chartboost.onBackPressed())
            return;
        else {
            if (myHomeWebViewClient != null && myHomeWebViewClient.canGobackHistory() && !SaveData.getBackToHome(this)) {
                backToHistory(null);

            } else {
                super.onBackPressed();
            }
        }
    }

    //  AdsView adsView;
    
    static boolean isAdmod = true;
    static boolean needChanged = false;
    public void lastChangeAdProvider()
    {
    	/*
    	if(adview.getVisibility() == adsView.getVisibility())
    		changeAdProvider();
    		*/
    }
    public void changeAdProvider()
    {
    	/*
    	if(isAdmod)
    	{
    		adview.setVisibility(View.VISIBLE);
    		adsView.setVisibility(View.GONE);
    	}else{
    		adview.setVisibility(View.GONE);
    		adsView.setVisibility(View.VISIBLE);
    	}
    	needChanged = false;
		isAdmod = !isAdmod;
		*/
    }
    
    public void needChangeAds()
    {
    	needChanged = true;
    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if(slidr!=null){
            if(SaveData.getSwipeToBack(this)){
                slidr.unlock();
            }else{
                slidr.lock();
            }
        }

    	if(viewArticleDetail != null)
    		viewArticleDetail.onResume();

        try {
            if ((loadingDialog != null) && (loadingDialog.isShowing())) {
                loadingDialog.dismiss();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Chartboost.onResume(this);
        showInterstitial();
    }

    @Override
    public void onStart() {
        super.onStart();
        Chartboost.onStart(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    	if(viewArticleDetail != null)
    		viewArticleDetail.onPause();
        Chartboost.onPause(this);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    	if(viewArticleDetail != null)
    		viewArticleDetail.destroy();
        Chartboost.onDestroy(this);
    }
    
    private static Context context;

    public static Context getAppContext() {
        return context;
    }

    public void navigationClick(View view) {

        switch (view.getId()) {
            case R.id.button_home:
                WebView viewArticleDetail = (WebView) findViewById(R.id.viewArticleDetail);
                    viewArticleDetail.loadUrl(current_Website_Page.getHomePageMobile());
                    viewArticleDetail.clearHistory();
                break;
            case R.id.button_more_apps:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);

                //showAlertAdsInform();
                /*
            	String url = getResources().getString(R.string.menu_moreApps_url);
            	Intent i = new Intent(Intent.ACTION_VIEW);
            	i.setData(Uri.parse(url));
            	startActivity(i);*/
                break;
            case R.id.button_home_app:
                finish();
                break;
            case R.id.button_hot_apps:
                UIUtils.showAlertGetMoreAppsServer(this);
                //
                break;

        }
    }

    public void showAlertAdsInform()
    {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Lọc Quảng Cáo Trong Báo")
                .setMessage("Ứng dụng sẽ cố lọc quảng cáo từ trang báo. Hiện tại tính năng đang "+(WebsitePage.isHideAds?"BẬT":"TẮT"))
                .setNegativeButton((!WebsitePage.isHideAds?"BẬT":"TẮT"),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                WebsitePage.isHideAds = !WebsitePage.isHideAds;
                                if(WebsitePage.isHideAds)
                                {
                                    synchronized(new Object()){WebsitePage.reloadConfigAds(MainActivity.this);}
                                }
                                SaveData.setHideAds(MainActivity.this, WebsitePage.isHideAds);
                                WebView viewArticleDetail = (WebView) MainActivity.this.findViewById(R.id.viewArticleDetail);
                                viewArticleDetail.reload();
                                dialog.cancel();
                            }
                        })
                .setPositiveButton("Bỏ Qua",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    public void backToHistory(View view) {
    	if(myHomeWebViewClient == null) return;
        WebView viewArticleDetail = (WebView) findViewById(R.id.viewArticleDetail);
        String url = myHomeWebViewClient.decreaseIndexListViewedContents();
        if(url != null){
        	viewArticleDetail.stopLoading();
        	viewArticleDetail.loadUrl(url);
        }
        updateButtonBackAndPrevious();
    }

    public void previousHistory(View view) {
    	if(myHomeWebViewClient == null) return;
        WebView viewArticleDetail = (WebView) findViewById(R.id.viewArticleDetail);
        String url = myHomeWebViewClient.increaseIndexListViewedContents();
        if(url != null){
        	viewArticleDetail.stopLoading();
        	viewArticleDetail.loadUrl(url);
        }
        updateButtonBackAndPrevious();
    }

    public void updateButtonBackAndPrevious() {
        showInterstitial();
    	if(myHomeWebViewClient == null) return;
        if (myHomeWebViewClient.canGobackHistory())
            ((ImageButton) findViewById(R.id.btGobackHistory)).setVisibility(View.VISIBLE);
        else ((ImageButton) findViewById(R.id.btGobackHistory)).setVisibility(View.GONE);

        if (myHomeWebViewClient.canPreviousHistory())
            ((ImageButton) findViewById(R.id.btPreviousHistroy)).setVisibility(View.VISIBLE);
        else ((ImageButton) findViewById(R.id.btPreviousHistroy)).setVisibility(View.GONE);
    }

    public void UpdateWhenScrollChange(Boolean isUp)
    {
    	RelativeLayout nagTop = (RelativeLayout) findViewById(R.id.nagTop);
    	if(isUp)
    	{
    		if(nagTop.getVisibility() != View.VISIBLE) nagTop.setVisibility(View.VISIBLE);
    	}
    	else{
    		if(nagTop.getVisibility() != View.GONE) nagTop.setVisibility(View.GONE);
    	}
    	
    }
}
