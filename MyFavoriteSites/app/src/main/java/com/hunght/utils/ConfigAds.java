package com.hunght.utils;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import com.hunght.data.DataAccessor;

import java.util.*;

public class ConfigAds
{
    private static ConfigAds configAds;
    public int Version;
    public List<String> DenyLinks;
    public List<CSSWeb> CSSWebs;

    private String getCSS(String name){
        String rs = "";
        if(CSSWebs!=null){
            for (CSSWeb cssWeb: CSSWebs) {
                if(cssWeb.Name.equalsIgnoreCase(name))
                {
                    return cssWeb.CSS;
                }
            }
        }
        return rs;
    }

    private boolean isDeniedURL(String name){
        if(DenyLinks!=null && name!=null){
            name = name.toLowerCase();
            for (String link: DenyLinks) {
                if(name.contains(link))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public static void reloadConfigAdsAsync(final Context context){
        new Thread(new Runnable(){
            public void run() {
                reloadConfigAds(context);
            }
        }).start();
    }

    public static void reloadConfigAds(Context context){
        configAds = getConfigAds();
        if(configAds!=null && configAds.Version > 0){
            DataAccessor.setConfigAds(context, configAds);
        }else{
            configAds = DataAccessor.getConfigAds(context);
        }
    }

    public static boolean isDenied(String url){
        if (configAds != null && configAds.Version != 0) {
            return configAds.isDeniedURL(url);
        }
        return false;
    }

    public static String getCSSWeb(String name){
        if (configAds != null && configAds.Version != 0) {
            return configAds.getCSS(name);
        }
        return "";
    }

    private static ConfigAds getConfigAds() {
        String link = "https://hunghtpage.github.io/DocBaoTongHop.json";
        StringBuilder contentResult = new StringBuilder();

        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            java.io.BufferedReader input = new java.io.BufferedReader(new java.io.InputStreamReader(new java.net.URL(link).openStream(), "UTF-8"));

            String inputLine;
            while ((inputLine = input.readLine()) != null)
            {
                contentResult.append(inputLine);
            }
            input.close();
            String json = contentResult.toString();
            Log.w("getConfigAds", json);
            com.google.gson.Gson gSon = new com.google.gson.Gson();
            return gSon.fromJson(json, ConfigAds.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ConfigAds();
    }
}

class CSSWeb
{
    public String Name;
    public String CSS;
}
