package com.hunght.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.hunght.utils.ConfigAds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DataAccessor {
    private static final String APP_SHARED_PREFS = "com.hunght.myfavoritesites";
    private static final String RECORD_FAVORITE_SITE_ITEM = "RECORD_FAVORITE_SITE_ITEM";
    private static final String SWIPE_TO_BACK="SWIPE_TO_BACK";
    private static final String BACK_TO_HOME="BACK_TO_HOME";
    private static final String USE_INSIDE_BROWSER="USE_INSIDE_BROWSER";
    private static final String USE_IMPROVE_BROWSER="USE_IMPROVE_BROWSER";
    private static final String CONFIG_ADS_KEY_NAME="CONFIG_ADS_KEY_NAME";
    private static final String SHARE_PAGE="SHARE_PAGE";

    static ArrayList<FavoriteSiteItem> favoriteSiteItems =  null;
    static HashMap<String, List<FavoriteSiteItem>> suggestionSiteItems =  null;
    static List<String> headerSuggestionSiteItems =  null;
    public static List<String> getHeaderSuggestionSiteItems(){
        if(headerSuggestionSiteItems == null){
            headerSuggestionSiteItems = new ArrayList<>();
            headerSuggestionSiteItems.add("English");
            headerSuggestionSiteItems.add("Vietnamese");
        }
        return headerSuggestionSiteItems;
    }
    public static HashMap<String, List<FavoriteSiteItem>> getSuggestionSiteItems(){
        if(suggestionSiteItems ==  null){
            suggestionSiteItems = new HashMap<>();

            //ENGLISH
            ArrayList<FavoriteSiteItem> suggestionEnglishItems = new ArrayList<FavoriteSiteItem>();
            suggestionEnglishItems.add(new FavoriteSiteItem("BBC", "", "https://www.bbc.com/"));
            suggestionEnglishItems.add(new FavoriteSiteItem("CNN", "", "https://edition.cnn.com/"));
            suggestionEnglishItems.add(new FavoriteSiteItem("Fox News", "", "https://www.foxnews.com/"));
            suggestionEnglishItems.add(new FavoriteSiteItem("Reuters", "", "https://www.reuters.com/"));
            suggestionEnglishItems.add(new FavoriteSiteItem("New York Times", "", "https://www.nytimes.com/"));
            suggestionEnglishItems.add(new FavoriteSiteItem("NBC News", "", "https://www.nbcnews.com/"));
            suggestionEnglishItems.add(new FavoriteSiteItem("USA Today", "", "https://www.usatoday.com/"));
            suggestionSiteItems.put(getHeaderSuggestionSiteItems().get(0), suggestionEnglishItems);

            //VIETNAMESE
            ArrayList<FavoriteSiteItem> suggestionVietnameseItems = new ArrayList<FavoriteSiteItem>();
            suggestionVietnameseItems.add(new FavoriteSiteItem("VNExpress", "https://vnexpress.net/favicon.ico", "https://vnexpress.net/"));
            suggestionVietnameseItems.add(new FavoriteSiteItem("Dân Trí", "https://dulich.dantri.com.vn/favicon.ico", "https://dantri.com.vn/"));
            suggestionVietnameseItems.add(new FavoriteSiteItem("Tuổi Trẻ", "", "http://tuoitre.vn/"));
            suggestionVietnameseItems.add(new FavoriteSiteItem("Thanh Niên", "", "https://thanhnien.vn"));
            suggestionVietnameseItems.add(new FavoriteSiteItem("Báo Mới", "https://baomoi-static.zadn.vn/favicons/favicon.ico", "http://m.baomoi.com/"));
            suggestionVietnameseItems.add(new FavoriteSiteItem("Zing", "https://static-znews.zadn.vn/favicon/v003/favicon_48x48.ico", "http://news.zing.vn"));
            suggestionVietnameseItems.add(new FavoriteSiteItem("Vietnamnet", "https://vnn-res.vgcloud.vn/ResV9/images/faviconvnn2018.ico", "http://vietnamnet.vn"));
            suggestionVietnameseItems.add(new FavoriteSiteItem("Người Lao Động", "", "https://nld.com.vn"));
            suggestionVietnameseItems.add(new FavoriteSiteItem("Báo Đất Việt", "http://baodatviet.vn/images/Icon_DVO.ico", "http://baodatviet.vn/"));
            suggestionVietnameseItems.add(new FavoriteSiteItem("VTC", "", "http://vtc.vn"));
            suggestionVietnameseItems.add(new FavoriteSiteItem("Người Đưa Tin", "https://www.nguoiduatin.vn/favicon.ico", "https://www.nguoiduatin.vn"));
            suggestionVietnameseItems.add(new FavoriteSiteItem("Đời Sống Pháp Luật", "", "http://doisongphapluat.com/"));
            suggestionVietnameseItems.add(new FavoriteSiteItem("An Ninh Thủ Đô", "", "https://m.anninhthudo.vn/"));
            suggestionVietnameseItems.add(new FavoriteSiteItem("24h", "", "https://24h.com.vn/"));
            suggestionVietnameseItems.add(new FavoriteSiteItem("Viettimes", "", "https://viettimes.vn/"));
            suggestionVietnameseItems.add(new FavoriteSiteItem("So Ha", "", "https://soha.vn/"));
            suggestionVietnameseItems.add(new FavoriteSiteItem("VOV", "", "https://vov.vn/"));
            suggestionVietnameseItems.add(new FavoriteSiteItem("2 Sao", "", "https://2sao.vn"));
            suggestionVietnameseItems.add(new FavoriteSiteItem("Tin Mới", "https://www.tinmoi.vn/fav.ico", "https://tinmoi.vn"));
            suggestionVietnameseItems.add(new FavoriteSiteItem("Kênh 14", "", "http://kenh14.vn"));
            suggestionVietnameseItems.add(new FavoriteSiteItem("Ngôi Sao", "", "https://ngoisao.vn/"));
            suggestionVietnameseItems.add(new FavoriteSiteItem("Tin Tức Online", "https://tintuconline.com.vn/Images/favicon.gif", "https://tintuconline.com.vn/"));
            suggestionVietnameseItems.add(new FavoriteSiteItem("ICT News", "https://cdn.ictnews.vn/a/Assets/img/favicon.ico", "https://ictnews.vn/"));
            suggestionVietnameseItems.add(new FavoriteSiteItem("VNReview", "", "https://vnreview.vn/"));
            suggestionVietnameseItems.add(new FavoriteSiteItem("Genk", "https://genknews.genkcdn.vn/web_images/genk192.png", "http://m.genk.vn/"));
            suggestionVietnameseItems.add(new FavoriteSiteItem("Tinh Tế", "", "https://www.tinhte.vn/"));
            suggestionVietnameseItems.add(new FavoriteSiteItem("Bóng Đá Plus", "http://bongdaplus.vn/img/appico.png", "http://bongdaplus.vn/"));
            suggestionVietnameseItems.add(new FavoriteSiteItem("Thể Thao 247", "", "https://thethao247.vn/"));
            suggestionVietnameseItems.add(new FavoriteSiteItem("Bóng Đá", "", "http://www.bongda.com.vn/"));
            suggestionVietnameseItems.add(new FavoriteSiteItem("Thể Thao Văn Hóa", "", "https://thethaovanhoa.vn/"));
            suggestionVietnameseItems.add(new FavoriteSiteItem("CafeF", "", "http://cafef.vn/"));
            suggestionVietnameseItems.add(new FavoriteSiteItem("Tin Nhanh Chứng Khoán", "", "https://tinnhanhchungkhoan.vn/"));
            suggestionSiteItems.put(getHeaderSuggestionSiteItems().get(1), suggestionVietnameseItems);
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

    public static void setUsingInsideBrowser(Context context, boolean value)
    {
        try {
            android.content.SharedPreferences sharedPref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(USE_INSIDE_BROWSER, value);
            editor.commit();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    public static boolean getUsingImproveBrowser(Context context)
    {
        {
            try {
                android.content.SharedPreferences sharedPref =context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
                return sharedPref.getBoolean(USE_IMPROVE_BROWSER, true);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return true;
    }

    public static void setUsingImproveBrowser(Context context, boolean value)
    {
        try {
            android.content.SharedPreferences sharedPref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(USE_IMPROVE_BROWSER, value);
            editor.commit();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    public static ConfigAds getConfigAds(Context context)
    {
        ConfigAds configAds = null;
        try {
            Gson gSon = new Gson();
            android.content.SharedPreferences sharedPref =context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
            String json = sharedPref.getString(CONFIG_ADS_KEY_NAME, "");
            configAds = gSon.fromJson(json, ConfigAds.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if(configAds == null) configAds = new ConfigAds();
        return configAds;
    }

    public static void setConfigAds(Context context, ConfigAds configAds)
    {
        try {
            android.content.SharedPreferences sharedPref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            Gson gSon = new Gson();
            String json = gSon.toJson(configAds);
            editor.putString(CONFIG_ADS_KEY_NAME, json);
            editor.commit();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    public static boolean getSharingPage(Context context)
    {
        {
            try {
                android.content.SharedPreferences sharedPref =context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
                return sharedPref.getBoolean(SHARE_PAGE, true);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return true;
    }

    public static void setSharingPage(Context context, boolean value)
    {
        try {
            android.content.SharedPreferences sharedPref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(SHARE_PAGE, value);
            editor.commit();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}
