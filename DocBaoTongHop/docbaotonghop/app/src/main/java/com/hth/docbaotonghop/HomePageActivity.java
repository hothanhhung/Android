package com.hth.docbaotonghop;

import com.hth.docbaotonghop.R;
import com.hth.utils.ConfigAds;
import com.hth.utils.ParserData;
import com.hth.utils.SaveData;
import com.hth.utils.UIUtils;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Calendar;
import java.util.Random;


public class HomePageActivity extends Activity {

	
    Dialog loadingDialog = null;
    private static long timeForRun = 0;

    private int countShow = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_page);
        context = getApplicationContext();

        StartAppSDK.init(this, "207910015", false);
        Random rand = new Random();
        if(rand.nextInt(4) == 3) PageApp.IsDisableSplash = false;
        else PageApp.IsDisableSplash = true;
        if(PageApp.IsDisableSplash) StartAppAd.disableSplash();

        timeForRun = Calendar.getInstance().getTime().getTime();
        WebsitePage.isHideAds = SaveData.getHideAds(this);
        (new DownloadContentTask()).execute(this);

	}

    private static Context context;

    public static Context getAppContext() {
        return context;
    }

    @Override
    public void onBackPressed() {
        // Create the interstitial.
        showInterstitial();
        super.onBackPressed();
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
				MainActivity.current_Website_Page = gotoPage;
				new Thread(loadingToMainScreen).start();
				
				HomePageActivity.this.runOnUiThread(new Runnable() { 
					public void run() {
						loadingDialog = com.hth.utils.UIUtils.showProgressDialog(loadingDialog, HomePageActivity.this);
					}});
			}
			//Toast.makeText(this, gotoPage.toString(), Toast.LENGTH_LONG).show();
		}
	}
	private Runnable loadingToMainScreen = new Runnable() {
	    public void run() {
	    	android.content.Intent articleDetailIntent = new android.content.Intent(HomePageActivity.this, MainActivity.class);
			startActivity(articleDetailIntent);
	    }
	};

    private void showInterstitial()
    {
        long timenow = Calendar.getInstance().getTime().getTime();
        long longtime = (countShow*2000000 ) + 400000;
        if(longtime > 1000000) longtime = 1000000;
        
        if(timeForRun > 0 && ((timenow - timeForRun) > longtime))
        {
            StartAppAd.showAd(this);
            timeForRun = Calendar.getInstance().getTime().getTime();
        }

    }

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		
       showInterstitial();
        
		super.onResume();
		if ((loadingDialog != null) && (loadingDialog.isShowing())) {
				loadingDialog.dismiss();
		}
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
