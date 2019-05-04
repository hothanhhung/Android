package com.hunght.myfavoritesites;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.hunght.data.DataAccessor;
import com.hunght.data.FavoriteSiteItem;
import com.hunght.dynamicgrid.DynamicGridView;
import com.hunght.utils.UIUtils;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    DynamicGridView grvFavoriteSite;
    SiteItemAdapter siteItemAdapter;
    AdView adview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        grvFavoriteSite = findViewById(R.id.grvFavoriteSite);

        siteItemAdapter = new SiteItemAdapter(this, DataAccessor.getFavoriteSiteItems(this), getResources(), getResources().getInteger(R.integer.number_of_gridview_columns));
        grvFavoriteSite.setAdapter(siteItemAdapter);
        grvFavoriteSite.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // check internet
                if (!UIUtils.isOnline(getApplicationContext())) {
                    UIUtils.showAlertErrorNoInternet(
                            MainActivity.this, false);
                }else{
                    FavoriteSiteItem item = (FavoriteSiteItem) view.getTag();
                    if(DataAccessor.getUsingInsideBrowser(MainActivity.this)) {
                        SiteActivity.current_Website_Page = item;
                        new Thread(loadingToSiteActivity).start();
                    }else{
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getSiteURL()));
                        startActivity(browserIntent);
                    }
                }
            }
        });

        grvFavoriteSite.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                FavoriteSiteItem item = (FavoriteSiteItem) view.getTag();
                showEditAndAddSiteDialog(item);
                return true;
            }
        });

        adview = this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adview.loadAd(adRequest);
    }

    private Runnable loadingToSiteActivity = new Runnable() {
        public void run() {
            /*HomePageActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    if ((loadingDialog != null) && (loadingDialog.isShowing())) {
                        loadingDialog.dismiss();
                    }
                }});*/

            android.content.Intent articleDetailIntent = new android.content.Intent(MainActivity.this, SiteActivity.class);
            startActivity(articleDetailIntent);
        }
    };

    private static long timeForRun = 0, countShow = 1;
    private InterstitialAd interstitial;
    private void showInterstitial()
    {
        long timenow = Calendar.getInstance().getTime().getTime();
        long longtime = (countShow*300000 );// + 100000;
        if(longtime > 1000000) longtime = 1000000;
        if(timeForRun == 0){
            timeForRun = Calendar.getInstance().getTime().getTime();
        }
        if((timenow - timeForRun) > longtime)
        {
            if (interstitial == null) {
                interstitial = new InterstitialAd(this);
                interstitial.setAdUnitId(getResources().getString(R.string.interstitial_ads));
                interstitial.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        if (interstitial.isLoaded()) {
                            interstitial.show();
                            timeForRun = Calendar.getInstance().getTime().getTime();
                            countShow = 1;
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
        switch (id) {
            case R.id.action_settings:
                Intent settingIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingIntent);
                return true;
            case R.id.action_add:
                showEditAndAddSiteDialog(new FavoriteSiteItem());
                return true;
            case R.id.action_reorder_gridview:
                grvFavoriteSite.startEditMode();
                return true;
            case R.id.action_suggestion_sites:
                showSuggestionSitesDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSuggestionSitesDialog(){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView= inflater.inflate(R.layout.suggestion_sites, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        //dialogBuilder.setTitle("Suggestion Websites");

        final ListView lvSuggestionSites = dialogView.findViewById(R.id.lvSuggestionSites);
        SuggestionSiteItemAdapter suggestionSiteItemAdapter = new SuggestionSiteItemAdapter(this, DataAccessor.getSuggestionSiteItems());
        lvSuggestionSites.setAdapter(suggestionSiteItemAdapter);
        dialogBuilder.setPositiveButton("OK",null);

        final AlertDialog mAlertDialog = dialogBuilder.create();
        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button btPositive = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                btPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        siteItemAdapter.set(DataAccessor.getFavoriteSiteItems(MainActivity.this));
                        mAlertDialog.dismiss();
                    }
                });
            }
        });
        mAlertDialog.show();
    }


    private void showEditAndAddSiteDialog(final FavoriteSiteItem favoriteSiteItem){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView= inflater.inflate(R.layout.favorite_site_item_update_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        //dialogBuilder.setTitle("");

        final TextView inputName = dialogView.findViewById(R.id.inputName);
        final TextView inputAddress = dialogView.findViewById(R.id.inputAddress);

        if(favoriteSiteItem != null)
        {
            inputName.setText(favoriteSiteItem.getName());
            inputAddress.setText(favoriteSiteItem.getSiteURL());
        }
        else{
            inputName.setText("");
            inputAddress.setText("");
        }

        dialogBuilder.setPositiveButton("SAVE",null);

        dialogBuilder.setNegativeButton("CANCEL",null);

        if(!favoriteSiteItem.isEmpty()){
            dialogBuilder.setNeutralButton("DELETE",null);
        }

        final AlertDialog mAlertDialog = dialogBuilder.create();
        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button btNegative = mAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                btNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAlertDialog.dismiss();
                    }});

                Button btNeutral = mAlertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                if(btNeutral!=null) {
                    btNeutral.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            askForDelete(mAlertDialog, favoriteSiteItem);
                        }
                    });
                }

                Button btPositive = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                btPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = inputName.getText().toString().trim();
                        String url = inputAddress.getText().toString().trim();
                        boolean isValidate = true;

                        if (name.isEmpty()) {
                            inputName.setError("Name is required!");
                            isValidate = false;
                        }
                        if (url.isEmpty()) {
                            inputAddress.setError("Address is required!");
                            isValidate = false;
                        }
                        if(!url.startsWith("http://") && !url.startsWith("https://")){
                            url = "http://" + url;
                        }
                        if(!URLUtil.isValidUrl(url)){
                            inputAddress.setError("Address is invalid!");
                            isValidate = false;
                        }
                        if (isValidate) {
                            favoriteSiteItem.setName(name);
                            favoriteSiteItem.setSiteURL(url);
                            DataAccessor.updateFavoriteSiteItems(MainActivity.this, favoriteSiteItem);
                            siteItemAdapter.set(DataAccessor.getFavoriteSiteItems(MainActivity.this));
                            mAlertDialog.dismiss();
                        }
                    }
                });
            }
        });
        mAlertDialog.show();
    }

    private void askForDelete(final AlertDialog farentDialog, final FavoriteSiteItem favoriteSiteItem)
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete "+favoriteSiteItem.getName() +"?")
             //   .setIcon(R.drawable.delete)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        DataAccessor.removeFavoriteSiteItems(MainActivity.this, favoriteSiteItem);
                        siteItemAdapter.set(DataAccessor.getFavoriteSiteItems(MainActivity.this));
                        farentDialog.dismiss();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        myQuittingDialogBox.show();

    }

    @Override
    public void onBackPressed() {
        if (grvFavoriteSite != null && grvFavoriteSite.isEditMode()) {
            grvFavoriteSite.stopEditMode();
            DataAccessor.setFavoriteSiteItems(this, siteItemAdapter.getDataItems());
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showInterstitial();
    }
}
