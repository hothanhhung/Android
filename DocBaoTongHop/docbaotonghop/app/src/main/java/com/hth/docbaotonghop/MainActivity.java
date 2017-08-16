package com.hth.docbaotonghop;


import com.hth.docbaotonghop.R;
import com.hth.utils.*;
import com.startapp.android.publish.adsCommon.StartAppSDK;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {
    static int width_Screen = 0;
    static int height_Screen = 0;
    static WebsitePage current_Website_Page = WebsitePage.VNExpressDotNet;
    private MyWebview viewArticleDetail;
    
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

        if(android.os.Build.VERSION.SDK_INT > 10) hideActionBar();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        height_Screen = metrics.heightPixels;
        width_Screen = metrics.widthPixels;

        context = getApplicationContext();

        setContentView(R.layout.activity_main_website);
        StartAppSDK.init(this, "207910015", true);

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


            progressBar1 = (ProgressBar)this.findViewById(R.id.progressBar1);

            //Uri uri = Uri.parse(current_Website_Page.getHomePageMobile());
            myHomeWebViewClient = new com.hth.utils.MyHomeWebViewClient(this);
            viewArticleDetail.setWebViewClient(myHomeWebViewClient);
            viewArticleDetail.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                	if(progress < 90)
                	{
                		progressBar1.setVisibility(View.VISIBLE);
                		if(progressBar1.getProgress() < progress){
                			progressBar1.setProgress(progress);
                		}
                	}else
                	{
                		progressBar1.setVisibility(View.GONE);
                	}
                	
            		if(progress > 70 && myHomeWebViewClient.getCurrentIndex() < 1)
            		{
            			changeAdProvider();
            		}
                }
                
            });
            needChanged = true;
            myHomeWebViewClient.addListViewedContents(current_Website_Page.getHomePageMobile());
            //viewArticleDetail.loadUrl("http://stackoverflow.com");
            viewArticleDetail.loadUrl(current_Website_Page.getHomePageMobile());
            //viewArticleDetail.loadDataWithBaseURL("file:///android_asset/",ParserData.getArticleDetail(current_Website_Page.getHomePageMobile()), "text/html", "utf-8", null);
        } else
            viewArticleDetail.loadDataWithBaseURL(null, "No Website Mobile", "text/html", "utf-8", null);
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
    	if(viewArticleDetail != null)
    		viewArticleDetail.onResume();

        try {
            if ((loadingDialog != null) && (loadingDialog.isShowing())) {
                loadingDialog.dismiss();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    	if(viewArticleDetail != null)
    		viewArticleDetail.onPause();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    	if(viewArticleDetail != null)
    		viewArticleDetail.destroy();
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
            	String url = getResources().getString(R.string.menu_moreApps_url);
            	Intent i = new Intent(Intent.ACTION_VIEW);
            	i.setData(Uri.parse(url));
            	startActivity(i);
                break;
            case R.id.button_home_app:
                finish();
                break;
            case R.id.button_hot_apps:
                UIUtils.showAlertGetMoreAppsServer(this);

                break;

        }
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
