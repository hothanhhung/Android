package com.hunght.tinchungkhoan;

import android.app.ActionBar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.hunght.data.MenuLookUpItem;
import com.hunght.data.StaticData;
import com.hunght.data.DoanhNghiepItem;
import com.hunght.utils.ParserData;
import com.hunght.utils.SavedValues;
import com.hunght.utils.UIUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
	TextView tvSelectedMenuLookUpItem;
    ListView lvMenuLookUpItems;
    MenuLookUpItemAdapter menuLookUpItemAdapter;
    LinearLayout llMainContent;

    private DrawerLayout mDrawerLayout;
    private LinearLayout mLeftDrawerList;

    private AdView mAdView = null;
    private InterstitialAd interstitial = null;

    private static long timeForRun = 0;
    private static long numberOfSelectItem = 0;
    private static boolean isFirstRun = false;
    private static SavedValues savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        if(decorView!=null) {
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
        ActionBar actionBar = getActionBar();
        if(actionBar!=null)
        {
            actionBar.hide();
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLeftDrawerList = (LinearLayout) findViewById(R.id.leftNavdrawer);

        llMainContent = (LinearLayout) findViewById(R.id.llMainContent);
        tvSelectedMenuLookUpItem = (TextView) findViewById(R.id.tvSelectedMenuLookUpItem);
        lvMenuLookUpItems = (ListView) findViewById(R.id.lvMenuLookUpItems);
        ArrayList<MenuLookUpItem> menuLookUpItems = StaticData.GetMenuLookUpItems();
        menuLookUpItemAdapter = new MenuLookUpItemAdapter(MainActivity.this, menuLookUpItems);

        lvMenuLookUpItems.setAdapter(menuLookUpItemAdapter);
        lvMenuLookUpItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                numberOfSelectItem++;
                //checkForShowInterstital();
                MenuLookUpItem menuLookUpItem = (MenuLookUpItem) view.getTag();
                changeLayout(menuLookUpItem);
            }
        });
        numberOfSelectItem = 1;
        timeForRun = 0;
        savedValues = new SavedValues(this);
        if(savedValues.getRecordFirstRun() == 0)
        {
            isFirstRun = true;
            savedValues.setRecordFirstRun(Calendar.getInstance().getTime().getTime());
        }else {
            isFirstRun = false;
        }

        mAdView = (AdView) this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mDrawerLayout.openDrawer(mLeftDrawerList);
        (new DownloadContentTask()).execute();
    }

    public void changeLayout(MenuLookUpItem menuLookUpItem)
    {
        if(menuLookUpItem.hasAction())
        {
            tvSelectedMenuLookUpItem.setText(menuLookUpItem.getName());
            llMainContent.removeAllViews();
            mDrawerLayout.closeDrawer(mLeftDrawerList);
            llMainContent.addView(menuLookUpItem.getView(MainActivity.this), 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        }else{
            Toast.makeText(MainActivity.this, "Not Implemented Yet", Toast.LENGTH_LONG).show();
        }
    }
	public void menuClick(View view) {
        switch (view.getId()) {
            case R.id.btMenuLookUpItems:
                if (mDrawerLayout.isDrawerOpen(mLeftDrawerList)) {
                    mDrawerLayout.closeDrawer(mLeftDrawerList);
                } else {
                    mDrawerLayout.openDrawer(mLeftDrawerList);
                }
                break;
            case R.id.btMoreApp:
                UIUtils.showAlertGetMoreAppsServer(this);
                break;

        }
    }

	boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
	            super.onBackPressed();
	            return;
	        }

	        this.doubleBackToExitPressedOnce = true;
	        Toast.makeText(this, "press BACK again to exit", Toast.LENGTH_SHORT).show();

	        new Handler().postDelayed(new Runnable() {

	            @Override
	            public void run() {
	                doubleBackToExitPressedOnce=false;
	            }
	        }, 2000);
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
	
	@Override
    protected  void onResume()
    {
        super.onResume();
      //  checkForShowInterstital();
    }

    private void checkForShowInterstital()
    {
        long timenow = Calendar.getInstance().getTime().getTime();
        long longtime = 300000 ;

        if(!isFirstRun )
        {
            if(timeForRun <= 0)
            {
                timeForRun = Calendar.getInstance().getTime().getTime();
                if ((new Random()).nextInt(3) == 1) {
                    showInterstitial();
                }
            }else if((timenow - timeForRun) > longtime)
            {
                showInterstitial();
            }
            else if(numberOfSelectItem%4 == 0) {
                showInterstitial();
            }
        }
    }

    private void showInterstitial() {
        if (interstitial == null) {
            interstitial = new InterstitialAd(this);
            interstitial.setAdUnitId(getResources().getString(R.string.interstitial_unit_id));
            interstitial.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    if (interstitial.isLoaded()) {
                        interstitial.show();
                        timeForRun = Calendar.getInstance().getTime().getTime();
                        numberOfSelectItem = 1;
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

    private void setThongTinDoanhNghieps(ArrayList<DoanhNghiepItem> data)
    {
        if(data != null && data.size() > 10)
        {
            if(savedValues != null) {
                savedValues.setThongTinDoanhNghieps(data);
            }
            StaticData.setThongTinDoanhNghieps(data);
        }
    }

    static ArrayList<DoanhNghiepItem> gethongTinDoanhNghieps()
    {
        if(StaticData.getThongTinDoanhNghieps() == null && savedValues != null)
        {
            StaticData.setThongTinDoanhNghieps(savedValues.getThongTinDoanhNghieps());
        }
        return StaticData.getThongTinDoanhNghieps();
    }

    private class DownloadContentTask extends AsyncTask<String, Integer, ArrayList<DoanhNghiepItem>> {
        protected ArrayList<DoanhNghiepItem> doInBackground(String... maCK) {
            return ParserData.getThongTinDoanhNghieps();
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(ArrayList<DoanhNghiepItem> data) {
            setThongTinDoanhNghieps(data);
        }
    }
}
