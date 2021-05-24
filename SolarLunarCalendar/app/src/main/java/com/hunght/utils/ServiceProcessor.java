package com.hunght.utils;

import android.os.StrictMode;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Lenovo on 4/6/2018.
 */

public class ServiceProcessor {
    static public final int SERVICE_GET_DEBUG = 20120;
    static public final int SERVICE_GET_CHAM_NGON = SERVICE_GET_DEBUG + 1;
    static public final int SERVICE_GET_INFO_OF_DATE = SERVICE_GET_CHAM_NGON + 1;
    static public final int SERVICE_GET_INFO_OF_DATE_SHORT = SERVICE_GET_INFO_OF_DATE + 1;
    static public final int SERVICE_GET_NGAY_RAM = SERVICE_GET_INFO_OF_DATE_SHORT + 1;
    static public final int SERVICE_GET_SUGGEST_TO_TUVI = SERVICE_GET_NGAY_RAM + 1;
    static public final int SERVICE_GET_TO_NOTE = SERVICE_GET_SUGGEST_TO_TUVI + 1;

    public static String getContent(String urlPage){
        return getContent(urlPage, true);
    }
    public static String getContent(String urlPage, boolean needHtml) {

        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            Document doc = Jsoup.connect(urlPage).timeout(10000)
                    // .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64; rv:30.0) Gecko/20100101 Firefox/30.0")
                    .header("Connection", "close")
                    .get();

            if(needHtml) {
                return doc.html();
            }else {
                return doc.text();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    static public String getChamNgon(String date, int index)
    {
        return getChamNgon(date, index, 0, true);
    }
    static public String getChamNgon(String date, int index, int type, boolean needHtml){
        String link = "http://hunght.com/api/solarlunar/GetInfoChamNgon/?date="+date+"&index="+index+"&type="+type;
        String jsonStr = null;

        try {
            jsonStr = getContent(link, needHtml);
        } catch (Exception e) {
            Log.e("getChamNgon", "Error " +  e.toString());
        }
        return jsonStr;
    }

    static public String getInfoDate(String date, int index) {
        String link = "http://hunght.com/api/solarlunar/GetGoodDateBadDate/?date="+date+"&index="+index;
        String jsonStr = null;

        try {
            jsonStr = getContent(link);
        } catch (Exception e) {
            Log.e("getChamNgon", "Error ", e);
        }
        return jsonStr;
    }

    static public String getInfoDateShort(String date, int index, boolean needHtml) {
        String link = "http://hunght.com/api/solarlunar/GetGoodDateBadDateShort/?date="+date+"&index="+index;
        String jsonStr = null;

        try {
            jsonStr = getContent(link, needHtml);
        } catch (Exception e) {
            Log.e("getChamNgon", "Error ", e);
        }
        return jsonStr;
    }

}
