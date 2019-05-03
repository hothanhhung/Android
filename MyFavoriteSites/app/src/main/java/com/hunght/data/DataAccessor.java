package com.hunght.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

public class DataAccessor {
    private static final String APP_SHARED_PREFS = "com.hunght.myfavoritesites";
    private static final String RECORD_FAVORITE_SITE_ITEM = "RECORD_FAVORITE_SITE_ITEM";
    private static final String SWIPE_TO_BACK="SWIPE_TO_BACK";
    private static final String BACK_TO_HOME="BACK_TO_HOME";

    static ArrayList<FavoriteSiteItem> favoriteSiteItems =  null;

    public static ArrayList<FavoriteSiteItem> getFavoriteSiteItems(Context context){
        if(favoriteSiteItems ==  null){
            favoriteSiteItems = getFavoriteSiteItemsFromSharedPreferences(context);
            /*favoriteSiteItems = new ArrayList<>();
            favoriteSiteItems.add(new FavoriteSiteItem("VNExpress", "https://vnexpress.net/favicon.ico", "https://vnexpress.net/"));
            favoriteSiteItems.add(new FavoriteSiteItem("Dân Trí", "", "https://dulich.dantri.com.vn/du-lich/hang-ngan-nguoi-do-ve-bien-da-nang-xa-hoi-ngay-le-20190428103143171.htm"));
            favoriteSiteItems.add(new FavoriteSiteItem("Tuổi Trẻ", "", "http://tuoitre.vn/"));
            favoriteSiteItems.add(new FavoriteSiteItem("Thanh Niên", "", "https://thanhnien.vn"));*/
        }
        return favoriteSiteItems;
    }

    public static void updateFavoriteSiteItems(Context context, FavoriteSiteItem favoriteSiteItem){
        if(favoriteSiteItem != null) {
            if(favoriteSiteItems == null){
                favoriteSiteItems = new ArrayList<>();
            }
            for (FavoriteSiteItem item : favoriteSiteItems) {
                if (item.toString().equalsIgnoreCase(favoriteSiteItem.toString())) {
                    item.updateFrom(favoriteSiteItem);
                    return;
                }
            }
            favoriteSiteItems.add(favoriteSiteItem);
            setFavoriteSiteItemsFromSharedPreferences(context, favoriteSiteItems);
        }
    }

    public static void removeFavoriteSiteItems(Context context, FavoriteSiteItem favoriteSiteItem){
        if(favoriteSiteItem != null && favoriteSiteItems != null) {
            FavoriteSiteItem deletedItem = null;
            for (FavoriteSiteItem item : favoriteSiteItems) {
                if (item.toString().equalsIgnoreCase(favoriteSiteItem.toString())) {
                    deletedItem = item;
                    break;
                }
            }
            favoriteSiteItems.remove(deletedItem);
            setFavoriteSiteItemsFromSharedPreferences(context, favoriteSiteItems);
        }
    }

    public static void setFavoriteSiteItems(Context context, ArrayList<FavoriteSiteItem> items){
        favoriteSiteItems = items;
        setFavoriteSiteItemsFromSharedPreferences(context, favoriteSiteItems);
    }

    private static ArrayList<FavoriteSiteItem> getFavoriteSiteItemsFromSharedPreferences(Context context) {
        try {
            SharedPreferences appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, 0);
            Gson gson = new Gson();
            String json = appSharedPrefs.getString(RECORD_FAVORITE_SITE_ITEM, "");
            FavoriteSiteItem[] items = gson.fromJson(json, FavoriteSiteItem[].class);
            if(items!=null){
                return  new ArrayList<>(Arrays.asList(items));
            }
            return new ArrayList<>();
        }catch (Exception ex){
            return new ArrayList<>();
        }
    }

    private static void setFavoriteSiteItemsFromSharedPreferences(Context context, ArrayList<FavoriteSiteItem> favoriteSiteItems) {
        if(favoriteSiteItems == null) return;
        FavoriteSiteItem[] items = new FavoriteSiteItem[favoriteSiteItems.size()];
        items = favoriteSiteItems.toArray(items);
        Gson gson = new Gson();
        String json = gson.toJson(items);
        SharedPreferences appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, 0);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(RECORD_FAVORITE_SITE_ITEM, json);
        prefsEditor.commit();
    }

    public static boolean getSwipeToBack(Context context)
    {
        {
            try {
                android.content.SharedPreferences sharedPref =context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
                return sharedPref.getBoolean(SWIPE_TO_BACK, true);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return true;
    }

    public static void setSwipeToBack(Context context, boolean isBackToHome)
    {
        try {
            android.content.SharedPreferences sharedPref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(SWIPE_TO_BACK, isBackToHome);
            editor.commit();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    public static boolean getBackToHome(Context context)
    {
        {
            try {
                android.content.SharedPreferences sharedPref =context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
                return sharedPref.getBoolean(BACK_TO_HOME, false);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return true;
    }

    public static void setBackToHome(Context context, boolean isBackToHome)
    {
        try {
            android.content.SharedPreferences sharedPref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(BACK_TO_HOME, isBackToHome);
            editor.commit();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}
