package com.hth.owner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hth.learnenglishbyvideos.R;
import com.hth.utils.MethodsHelper;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class OwnerAds {
    private static final String OWNER_ADS_DATA="OWNER_ADS_DATA";

    private static AdsItem getAdsItem(Activity activity)
    {
        AdsItem adsItem = null;
        try {
            Gson gSon = new Gson();
            SharedPreferences sharedPref =activity.getSharedPreferences(activity.getPackageName(), Context.MODE_PRIVATE);
            String json = sharedPref.getString(OWNER_ADS_DATA, "");
            adsItem = gSon.fromJson(json, AdsItem.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if(adsItem == null) adsItem = new AdsItem();
        return adsItem;
    }

    private static void setAdsItem(Activity activity, AdsItem adsItem)
    {
        try {
            SharedPreferences sharedPref = activity.getSharedPreferences(activity.getPackageName(), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            Gson gSon = new Gson();
            String json = gSon.toJson(adsItem);
            editor.putString(OWNER_ADS_DATA, json);
            editor.commit();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    public static void loadPopupDataAds(Activity activity)
    {
        //setAdsItem(activity, new AdsItem());
        AdsItem currentAds = getAdsItem(activity);
        String link = "http://hunght.com/api/Ads/GetPopupAds/?country=EN&package="+MethodsHelper.getPackageName(activity)+"&adsId="+ currentAds.getId();

        //AdsItem adsItem = new AdsItem("My Favorite Sites", "https://lh3.googleusercontent.com/BoUomKA8E1lF3pCv0e9n5qIn63SHBJJW26XCjbmbOcqb6jWYkEtnTPZk3QUB1yxbivE=s360", "xin chao", "https://play.google.com/store/apps/details?id=com.hunght.myfavoritesites");
        /*setAdsItem(activity, adsItem);
        if(true) return;*/
        Gson gSon = new Gson();
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            StringBuilder jsonStringBuilder = new StringBuilder();
            BufferedReader input = new BufferedReader(new InputStreamReader(new URL(link).openStream(), "UTF-8"));

            String inputLine;
            while ((inputLine = input.readLine()) != null)
            {
                jsonStringBuilder.append(inputLine);
            }
            input.close();
            String json = jsonStringBuilder.toString();
            Log.d("loadPopupDataAds", json);
            AdsItem adsItem = gSon.fromJson(json, AdsItem.class);
            if(adsItem!=null && adsItem.haveId()){
                if(!adsItem.isTheSame(currentAds)){
                    setAdsItem(activity, adsItem);
                }
            }
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    static AlertDialog mAlertDialog;
    public static boolean showPopupAds(final Activity activity){
        if(mAlertDialog != null && mAlertDialog.isShowing()) return true;

        final AdsItem adsItem = getAdsItem(activity);
        if(adsItem!=null && adsItem.haveId() && !adsItem.isShown()){
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
            LayoutInflater inflater = activity.getLayoutInflater();

            View dialogView= inflater.inflate(R.layout.owner_ads_layout, null);
            dialogBuilder.setView(dialogView);
            dialogBuilder.setCancelable(false);
            //dialogBuilder.setTitle("Quảng Cáo");

            TextView tvName = dialogView.findViewById(R.id.tvName);
            ImageView imageView = dialogView.findViewById(R.id.imageView);
            TextView tvDescription = dialogView.findViewById(R.id.tvDescription);

            tvName.setText(adsItem.getName());
            tvDescription.setText(adsItem.getDesc());

            if(adsItem.haveImage())
            {
                try {
                    Picasso.with(activity).load(adsItem.getImage()).into(imageView);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            dialogBuilder.setNegativeButton("OK",null);
            if(adsItem.haveLink())
            {
                dialogBuilder.setPositiveButton("VIEW",null);
                tvName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(adsItem.getLink()));
                        activity.startActivity(i);
                    }
                });

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(adsItem.getLink()));
                        activity.startActivity(i);
                    }
                });
            }


            mAlertDialog = dialogBuilder.create();
            mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                @Override
                public void onShow(DialogInterface dialog) {

                    if(adsItem.haveLink()) {
                        Button btPositive = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        if(btPositive!=null) {
                            btPositive.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(adsItem.getLink()));
                                    activity.startActivity(i);
                                }
                            });
                        }
                    }
                    Button btNegative = mAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                    if(btNegative!=null) {
                        btNegative.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mAlertDialog.dismiss();
                                adsItem.setShown(true);
                                setAdsItem(activity, adsItem);
                            }
                        });
                    }
                }
            });
            mAlertDialog.show();
            return true;
        }
        return false;
    }
}
