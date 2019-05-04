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
    private static final String USE_INSIDE_BROWSER="USE_INSIDE_BROWSER";

    static ArrayList<FavoriteSiteItem> favoriteSiteItems =  null;
    static ArrayList<FavoriteSiteItem> suggestionSiteItems =  null;

    public static ArrayList<FavoriteSiteItem> getSuggestionSiteItems(){
        if(suggestionSiteItems ==  null){
            suggestionSiteItems = new ArrayList<>();
            suggestionSiteItems.add(new FavoriteSiteItem("VNExpress", "https://vnexpress.net/favicon.ico", "https://vnexpress.net/"));
            suggestionSiteItems.add(new FavoriteSiteItem("Dân Trí", "", "https://dantri.com.vn/"));
            suggestionSiteItems.add(new FavoriteSiteItem("Tuổi Trẻ", "", "http://tuoitre.vn/"));
            suggestionSiteItems.add(new FavoriteSiteItem("Thanh Niên", "", "https://thanhnien.vn"));
            suggestionSiteItems.add(new FavoriteSiteItem("Vietnamnet", "", "http://vietnamnet.vn"));
            suggestionSiteItems.add(new FavoriteSiteItem("Người Lao Động", "", "https://nld.com.vn"));
            suggestionSiteItems.add(new FavoriteSiteItem("VTC", "", "http://vtc.vn"));
            suggestionSiteItems.add(new FavoriteSiteItem("Người Đưa Tin", "", "http://www.nguoiduatin.vn"));
            suggestionSiteItems.add(new FavoriteSiteItem("Zing", "", "http://news.zing.vn"));
            suggestionSiteItems.add(new FavoriteSiteItem("Tin Mới", "", "http://m.tinmoi.vn"));
            suggestionSiteItems.add(new FavoriteSiteItem("Đời Sống Pháp Luật", "", "http://m.doisongphapluat.com/"));
            suggestionSiteItems.add(new FavoriteSiteItem("24h", "", "http://m.24h.com.vn/"));
            suggestionSiteItems.add(new FavoriteSiteItem("Báo Đất Việt", "", "http://m.baodatviet.vn/"));
            suggestionSiteItems.add(new FavoriteSiteItem("Báo Mới", "", "http://m.baomoi.com/"));
            suggestionSiteItems.add(new FavoriteSiteItem("Viettimes", "", "http://m.viettimes.vn/"));
            suggestionSiteItems.add(new FavoriteSiteItem("2 Sao", "", "http://m.2sao.vn"));
            suggestionSiteItems.add(new FavoriteSiteItem("Tin Mới", "", "http://m.tinmoi.vn"));
            suggestionSiteItems.add(new FavoriteSiteItem("Kênh 14", "", "http://m.kenh14.vn"));
            suggestionSiteItems.add(new FavoriteSiteItem("Ngôi Sao", "", "http://m.ngoisao.vn/"));
            suggestionSiteItems.add(new FavoriteSiteItem("Tin Tức Online", "", "http://m.tintuconline.com.vn/"));
            suggestionSiteItems.add(new FavoriteSiteItem("Số Ha", "", "http://m.soha.vn/"));
            suggestionSiteItems.add(new FavoriteSiteItem("VOV", "", "http://m.vov.vn/"));
            suggestionSiteItems.add(new FavoriteSiteItem("ICT News", "", "http://ictnews.vn/"));
            suggestionSiteItems.add(new FavoriteSiteItem("VNReview", "", "http://m.vnreview.vn/"));
            suggestionSiteItems.add(new FavoriteSiteItem("Genk", "", "http://m.genk.vn/"));
            suggestionSiteItems.add(new FavoriteSiteItem("Tinh Tế", "", "https://www.tinhte.vn/"));
        }
        return suggestionSiteItems;
    }

    public static boolean isInFavoriteSiteItems(FavoriteSiteItem favoriteSiteItem){
        for (FavoriteSiteItem item : favoriteSiteItems) {
            if (item.toString().equalsIgnoreCase(favoriteSiteItem.toString())) {
                return true;
            }
        }
        return false;
    }

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
            SharedPreferences appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
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
        SharedPreferences appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
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


    public static boolean getUsingInsideBrowser(Context context)
    {
        {
            try {
                android.content.SharedPreferences sharedPref =context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
                return sharedPref.getBoolean(USE_INSIDE_BROWSER, true);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return true;
    }

    public static void setUsingInsideBrowser(Context context, boolean isBackToHome)
    {
        try {
            android.content.SharedPreferences sharedPref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(USE_INSIDE_BROWSER, isBackToHome);
            editor.commit();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}
