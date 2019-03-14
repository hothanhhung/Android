package com.hth.docbaotonghop;

import com.chartboost.sdk.Model.CBError;
import com.hth.docbaotonghop.R;
import com.hth.utils.ConfigAds;
import com.hth.utils.ParserData;
import com.hth.utils.SaveData;
import com.hth.utils.UIUtils;
/*
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;
*/

import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.CBLocation;
import com.chartboost.sdk.ChartboostDelegate;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Calendar;
import java.util.Random;


public class HomePageActivity extends Activity {

	final int REQUEST_WRITE_STORAGE_REQUEST_CODE = 101;
    Dialog loadingDialog = null;
    private static long timeForRun = 0;

    private int countShow = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_page);
        context = getApplicationContext();

        /*StartAppSDK.init(this, "207910015", false);
        Random rand = new Random();
        if(rand.nextInt(4) == 3) PageApp.IsDisableSplash = false;
        else PageApp.IsDisableSplash = true;
        if(PageApp.IsDisableSplash) StartAppAd.disableSplash();*/

        Chartboost.startWithAppId(this, "5c6127198dd6d97dec5b0a49", "b409bc2a1610f1edde98ab2314b70b1bf14c5541");
        Chartboost.onCreate(this);
        Chartboost.setDelegate(yourDelegateObject);
        Chartboost.cacheInterstitial(CBLocation.LOCATION_MAIN_MENU);
        Chartboost.cacheRewardedVideo(CBLocation.LOCATION_MAIN_MENU);
        //timeForRun = Calendar.getInstance().getTime().getTime();
        WebsitePage.isHideAds = SaveData.getHideAds(this);
        (new DownloadContentTask()).execute(this);
       // requestAppPermissions();

	}

    private static Context context;

    public static Context getAppContext() {
        return context;
    }

    @Override
    public void onBackPressed() {
        // Create the interstitial.
        if (Chartboost.onBackPressed())
            return;
        else
            {
            showInterstitial();
            super.onBackPressed();
        }
    }

    public void goToMainPage(View view)
	{
        if(!UIUtils.isOnline(HomePageActivity.this)){
            UIUtils.showAlertErrorNoInternet(HomePageActivity.this, false);
            return;
        }

		if(view.getTag()!=null)
		{
			String tag = view.getTag().toString();
			int webpage = Integer.parseInt(tag);
			WebsitePage gotoPage = WebsitePage.valueOf(webpage);
			if(gotoPage!=null)
			{
                //loadingDialog = com.hth.utils.UIUtils.showProgressDialog(loadingDialog, HomePageActivity.this);
				MainActivity.current_Website_Page = gotoPage;
				new Thread(loadingToMainScreen).start();
				
				/*HomePageActivity.this.runOnUiThread(new Runnable() {
					public void run() {
						loadingDialog = com.hth.utils.UIUtils.showProgressDialog(loadingDialog, HomePageActivity.this);
					}});*/
			}
			//Toast.makeText(this, gotoPage.toString(), Toast.LENGTH_LONG).show();
		}
	}
	private Runnable loadingToMainScreen = new Runnable() {
	    public void run() {
            /*HomePageActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    if ((loadingDialog != null) && (loadingDialog.isShowing())) {
                        loadingDialog.dismiss();
                    }
                }});*/

	    	android.content.Intent articleDetailIntent = new android.content.Intent(HomePageActivity.this, MainActivity.class);
			startActivity(articleDetailIntent);
	    }
	};

    private void requestAppPermissions() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        if (hasReadPermissions() && hasWritePermissions()) {
            return;
        }

        ActivityCompat.requestPermissions(this,
                new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, REQUEST_WRITE_STORAGE_REQUEST_CODE); // your request code
    }

    private boolean hasReadPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasWritePermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private void showInterstitial()
    {
        long timenow = Calendar.getInstance().getTime().getTime();
        long longtime = 360000;//(countShow*300000 );// + 100000;
        //if(longtime > 1000000) longtime = 1000000;
        if(timeForRun == 0){
            timeForRun = timenow;
        }
        if((timenow - timeForRun) > longtime)
        {
            //StartAppAd.showAd(this);
            if(Chartboost.hasRewardedVideo(CBLocation.LOCATION_MAIN_MENU)){
                Chartboost.showRewardedVideo(CBLocation.LOCATION_MAIN_MENU);
                Chartboost.cacheRewardedVideo(CBLocation.LOCATION_MAIN_MENU);
                timeForRun = Calendar.getInstance().getTime().getTime();
            }
            else if (Chartboost.hasInterstitial(CBLocation.LOCATION_MAIN_MENU)) {
                Chartboost.showInterstitial(CBLocation.LOCATION_MAIN_MENU);
                Chartboost.cacheInterstitial(CBLocation.LOCATION_MAIN_MENU);
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
    public void onStart() {
        super.onStart();
        Chartboost.onStart(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Chartboost.onPause(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        Chartboost.onStop(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Chartboost.onDestroy(this);
    }

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		
       showInterstitial();
        
		super.onResume();
		if ((loadingDialog != null) && (loadingDialog.isShowing())) {
				loadingDialog.dismiss();
		}

        Chartboost.onResume(this);
	}

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.menu_moreApps).setVisible(true);
        menu.findItem(R.id.menu_hotApps).setVisible(true);
        menu.findItem(R.id.menu_about).setVisible(true);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        android.view.MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_setting:

                return true;

            case R.id.menu_about:
                UIUtils.showAlertAbout(this);
                return true;
            case R.id.menu_hotApps:
                UIUtils.showAlertGetMoreAppsServer(this);
                return true;
            case R.id.menu_moreApps:
            	String url = getResources().getString(R.string.menu_moreApps_url);
            	Intent i = new Intent(Intent.ACTION_VIEW);
            	i.setData(Uri.parse(url));
            	startActivity(i);
                return true;
                
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class DownloadContentTask extends AsyncTask<Activity, Integer, String> {
        protected String doInBackground(Activity... activities) {
            WebsitePage.reloadConfigAds(activities[0]);
            return "OK";
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(String data) {
        }
    }

}
