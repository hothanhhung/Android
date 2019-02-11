package com.hth.utils;

import android.content.SharedPreferences;
import android.app.Activity;
import android.content.Context;
import com.google.gson.Gson;

public class SaveData {

    private static final String HIDE_ADS_KEY_NAME="HIDE_ADS_KEY_NAME";
    private static final String CONFIG_ADS_KEY_NAME="CONFIG_ADS_KEY_NAME";
    private static final String BACK_TO_HOME="BACK_TO_HOME";
    private static final String SWIPE_TO_BACK="SWIPE_TO_BACK";
    private static final String LONG_CLICK_TO_SHARE="LONG_CLICK_TO_SHARE";

    public static boolean getLongClickToShare(Activity activity)
    {
        {
            try {
                android.content.SharedPreferences sharedPref =activity.getSharedPreferences(activity.getPackageName(), Context.MODE_PRIVATE);
                return sharedPref.getBoolean(LONG_CLICK_TO_SHARE, false);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return true;
    }

    public static void setLongClickToShare(Activity activity, boolean value)
    {
        try {
            android.content.SharedPreferences sharedPref = activity.getSharedPreferences(activity.getPackageName(), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(LONG_CLICK_TO_SHARE, value);
            editor.commit();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    public static boolean getSwipeToBack(Activity activity)
    {
        {
            try {
                android.content.SharedPreferences sharedPref =activity.getSharedPreferences(activity.getPackageName(), Context.MODE_PRIVATE);
                return sharedPref.getBoolean(SWIPE_TO_BACK, true);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return true;
    }

    public static void setSwipeToBack(Activity activity, boolean isBackToHome)
    {
        try {
            android.content.SharedPreferences sharedPref = activity.getSharedPreferences(activity.getPackageName(), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(SWIPE_TO_BACK, isBackToHome);
            editor.commit();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    public static boolean getBackToHome(Activity activity)
    {
        {
            try {
                android.content.SharedPreferences sharedPref =activity.getSharedPreferences(activity.getPackageName(), Context.MODE_PRIVATE);
                return sharedPref.getBoolean(BACK_TO_HOME, false);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return true;
    }

    public static void setBackToHome(Activity activity, boolean isBackToHome)
    {
        try {
            android.content.SharedPreferences sharedPref = activity.getSharedPreferences(activity.getPackageName(), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(BACK_TO_HOME, isBackToHome);
            editor.commit();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    public static boolean getHideAds(Activity activity)
    {
        {
            try {
                android.content.SharedPreferences sharedPref =activity.getSharedPreferences(activity.getPackageName(), Context.MODE_PRIVATE);
                return sharedPref.getBoolean(HIDE_ADS_KEY_NAME, true);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return true;
    }

    public static void setHideAds(Activity activity, boolean isHide)
    {
        try {
            android.content.SharedPreferences sharedPref = activity.getSharedPreferences(activity.getPackageName(), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(HIDE_ADS_KEY_NAME, isHide);
            editor.commit();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    public static ConfigAds getConfigAds(Activity activity)
    {
        ConfigAds configAds = null;
            try {
                Gson gSon = new Gson();
                android.content.SharedPreferences sharedPref =activity.getSharedPreferences(activity.getPackageName(), Context.MODE_PRIVATE);
                String json = sharedPref.getString(CONFIG_ADS_KEY_NAME, "");
	    	    configAds = gSon.fromJson(json, ConfigAds.class);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        if(configAds == null) configAds = new ConfigAds();
        return configAds;
    }

    public static void setConfigAds(Activity activity, ConfigAds configAds)
    {
        try {
            android.content.SharedPreferences sharedPref = activity.getSharedPreferences(activity.getPackageName(), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            Gson gSon = new Gson();
            String json = gSon.toJson(configAds);
            editor.putString(CONFIG_ADS_KEY_NAME, json);
            editor.commit();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

}
