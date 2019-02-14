package com.hunght.tinchungkhoan;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.hunght.data.MenuLookUpItem;
import com.hunght.data.MenuLookUpItemKind;
import com.hunght.data.StaticData;
import com.hunght.data.DoanhNghiepItem;
import com.hunght.utils.MethodsHelper;
import com.hunght.utils.ParserData;
import com.hunght.utils.SavedValues;
import com.hunght.utils.UIUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
	TextView tvSelectedMenuLookUpItem;
    //ListView lvMenuLookUpItems;
    MenuLookUpItemAdapter menuLookUpItemAdapter;
    LinearLayout llMainContent;

    private DrawerLayout mDrawerLayout;
    private NavigationView mLeftDrawerList;

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
        mLeftDrawerList = findViewById(R.id.leftNavdrawer);

        llMainContent = findViewById(R.id.llMainContent);
        tvSelectedMenuLookUpItem = (TextView) findViewById(R.id.tvSelectedMenuLookUpItem);
        /*lvMenuLookUpItems = findViewById(R.id.lvMenuLookUpItems);
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
        });*/
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
        mLeftDrawerList.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                return navigationItemSelected(menuItem);
            }
        });

        if(savedValues.getRecordPasswordInApp()){
            showPasswordPopup(0);
        }
    }

    private void showPasswordPopup(final int index) {
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.password_popup, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Bảo Mật")
                .setView(dialogView).setCancelable(false)
                .setPositiveButton("Đi Vào", null)
                .setNegativeButton("Bỏ Qua", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(index == 0)
                        {
                            finish();
                        }else{
                            dialog.dismiss();
                        }
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);
        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = ((EditText) dialogView.findViewById(R.id.etPassword)).getText().toString();
                if(password.equals(savedValues.getRecordPassword())){
                    dialog.dismiss();
                    if(index != 0)
                    {
                        updateUI(index);
                    }
                }else{
                    ((TextView)dialogView.findViewById(R.id.tvMessage)).setText("Mật Khẩu không đúng. Vui lòng nhập lại");
                }

            }
        });
    }

    public void updateWhenScrollChange(Boolean isUp)
    {
        LinearLayout llHeaderMenu = findViewById(R.id.llHeaderMenu);
        if(llHeaderMenu!=null) {
            if (isUp) {
                if (llHeaderMenu.getVisibility() != View.VISIBLE) llHeaderMenu.setVisibility(View.VISIBLE);
            } else {
                if (llHeaderMenu.getVisibility() != View.GONE) llHeaderMenu.setVisibility(View.GONE);
            }
        }
    }

    public void changeLayout(final MenuLookUpItem menuLookUpItem) {
        if (menuLookUpItem.hasAction()) {
            if (menuLookUpItem.isTrangBao() && !savedValues.getRecordInappBrowser()) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(menuLookUpItem.getUrl()));
                startActivity(browserIntent);
            } else {
                checkForShowInterstital();
                tvSelectedMenuLookUpItem.setText(menuLookUpItem.getName());
                mDrawerLayout.closeDrawer(mLeftDrawerList);
                loadingPopup = UIUtils.showPopUpLoading(MainActivity.this);
                /*runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        llMainContent.removeAllViews();
                        llMainContent.addView(menuLookUpItem.getView(MainActivity.this), 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        if(loadingPopup!=null && loadingPopup.isShowing()){
                            loadingPopup.dismiss();
                        }
                    }
                });*/
                //llMainContent.removeAllViews();
                //llMainContent.addView(menuLookUpItem.getView(MainActivity.this), 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

                (new CreateViewTask()).execute(menuLookUpItem);
            }
        } else {
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (LookUpForViewWithWebViewRequest.canGoBack()) {
                LookUpForViewWithWebViewRequest.goBack();
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
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
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


    public boolean navigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if((id == R.id.nav_DanhMucDauTu || id == R.id.nav_Setting) && savedValues.getRecordPasswordMucDauTu()){
            showPasswordPopup(id);
        }else {
            updateUI(id);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updateUI(int id){

        if(savedValues.getRecordShowHeader()){
            updateWhenScrollChange(true);
        }

        switch (id)
        {
            case R.id.nav_DanhMucDauTu:
                changeLayout(StaticData.geMenuItemBasedOnKind(MenuLookUpItemKind.DanhMucDauTu));
                break;

            case R.id.nav_DanhMucYeuThich:
                changeLayout(StaticData.geMenuItemBasedOnKind(MenuLookUpItemKind.DanhMucYeuThich));
                break;

            case R.id.nav_ToanCanhThiTruong:
                changeLayout(StaticData.geMenuItemBasedOnKind(MenuLookUpItemKind.ToanCanhThiTruong));
                break;

            case R.id.nav_DuLieuMuBan:
                changeLayout(StaticData.geMenuItemBasedOnKind(MenuLookUpItemKind.DuLieuMuaBan));
                break;

            case R.id.nav_ThucHienQuyen:
                changeLayout(StaticData.geMenuItemBasedOnKind(MenuLookUpItemKind.ThucHienQuyen));
                break;

            case R.id.nav_ThongTinDoanhNghiep:
                changeLayout(StaticData.geMenuItemBasedOnKind(MenuLookUpItemKind.ThongTinDoanhNghiep));
                break;

            case R.id.nav_Cafef:
                changeLayout(StaticData.geMenuItemBasedOnKind(MenuLookUpItemKind.Cafef));
                break;
            case R.id.nav_Vietstock:
                changeLayout(StaticData.geMenuItemBasedOnKind(MenuLookUpItemKind.Vietstock));
                break;
            case R.id.nav_BaoDauTu:
                changeLayout(StaticData.geMenuItemBasedOnKind(MenuLookUpItemKind.DauTuOnline));
                break;
            case R.id.nav_TinNhanhChungKhoan:
                changeLayout(StaticData.geMenuItemBasedOnKind(MenuLookUpItemKind.TinNhanhChungKhoan));
                break;
            case R.id.nav_Setting:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_Rate:
                try {
                    Uri marketUri = Uri.parse("market://details?id=" + getPackageName());
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                    startActivity(marketIntent);
                }catch(Exception e) {
                    Uri marketUri = Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName());
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                    startActivity(marketIntent);
                }
                break;
        }
    }

	@Override
    protected  void onResume()
    {
        super.onResume();
        checkForShowInterstital();
        if(savedValues!=null)
        {
            updateWhenScrollChange(savedValues.getRecordShowHeader());
        }
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
                //showInterstitial();
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

    static ArrayList<DoanhNghiepItem> gethongTinDoanhNghiepsClone()
    {
        if(StaticData.getThongTinDoanhNghieps() == null && savedValues != null)
        {
            StaticData.setThongTinDoanhNghieps(savedValues.getThongTinDoanhNghieps());
        }
        if(StaticData.getThongTinDoanhNghieps()!=null) {
            return (ArrayList<DoanhNghiepItem>) StaticData.getThongTinDoanhNghieps().clone();
        }
        return null;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        if (decorView != null) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE
                            // Set the content to appear under the system bars so that the
                            // content doesn't resize when the system bars hide and show.
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            // Hide the nav bar and status bar
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }
    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }


    final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 10000;
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    MethodsHelper.exportToExcel(this, savedValues.getDanhMucDauTus());
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Không có quyền để tạo file", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
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

    ProgressDialog loadingPopup;
    private class CreateViewTask extends AsyncTask<MenuLookUpItem, Integer, MenuLookUpItem> {
        protected MenuLookUpItem doInBackground(MenuLookUpItem... menuLookUpItems) {
            try {
                Thread.sleep(1000);
            }catch (Exception ex){
                ex.printStackTrace();
            }
            return menuLookUpItems[0];
            //return menuLookUpItems[0].getView(MainActivity.this);
            //return new DanhMucDauTuView(MainActivity.this);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(final MenuLookUpItem menuLookUpItem) {
            if(loadingPopup!=null){
                loadingPopup.dismiss();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    llMainContent.removeAllViews();
                    llMainContent.addView(menuLookUpItem.getView(MainActivity.this), 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    if(loadingPopup!=null && loadingPopup.isShowing()){
                        loadingPopup.dismiss();
                    }
                }
            });
            /*llMainContent.removeAllViews();
            llMainContent.addView(menuLookUpItem.getView(MainActivity.this), 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            if(loadingPopup!=null){
                loadingPopup.dismiss();
            }*/
        }
    }
}
