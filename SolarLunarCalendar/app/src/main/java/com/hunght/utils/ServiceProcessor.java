package com.hunght.utils;

import android.os.StrictMode;
import android.util.Log;

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
    static public final int SERVICE_GET_CHAM_NGON = 0;
    static public final int SERVICE_GET_INFO_OF_DATE = SERVICE_GET_CHAM_NGON + 1;
    static public final int SERVICE_GET_INFO_OF_DATE_SHORT = SERVICE_GET_INFO_OF_DATE + 1;
    static public String getChamNgon(String date, int index)
    {
        return getChamNgon(date, index, 0);
    }
    static public String getChamNgon(String date, int index, int type){
        String link = "http://hunght.com/api/solarlunar/GetInfoChamNgon/?date="+date+"&index="+index+"&type="+type;
        String jsonStr = null;

        try {
            StringBuilder jsonStringBuilder = new StringBuilder();
            BufferedReader input = new BufferedReader(new InputStreamReader(new URL(link).openStream(), "UTF-8"));

            String inputLine;
            while ((inputLine = input.readLine()) != null)
            {
                jsonStringBuilder.append(inputLine + "\n");
            }
            input.close();
            jsonStr = jsonStringBuilder.toString();
        } catch (Exception e) {
            Log.e("getChamNgon", "Error " +  e.toString());
        }
        return jsonStr;
    }

    static public String getInfoDate(String date, int index) {
        String link = "http://hunght.com/api/solarlunar/GetGoodDateBadDate/?date="+date+"&index="+index;
        String jsonStr = null;

        try {
            StringBuilder jsonStringBuilder = new StringBuilder();
            BufferedReader input = new BufferedReader(new InputStreamReader(new URL(link).openStream(), "UTF-8"));

            String inputLine;
            while ((inputLine = input.readLine()) != null)
            {
                jsonStringBuilder.append(inputLine + "\n");
            }
            input.close();
            jsonStr = jsonStringBuilder.toString();
        } catch (Exception e) {
            Log.e("getChamNgon", "Error ", e);
        }
        return jsonStr;
    }

    static public String getInfoDateShort(String date, int index) {
        String link = "http://hunght.com/api/solarlunar/GetGoodDateBadDateShort/?date="+date+"&index="+index;
        String jsonStr = null;

        try {
            StringBuilder jsonStringBuilder = new StringBuilder();
            BufferedReader input = new BufferedReader(new InputStreamReader(new URL(link).openStream(), "UTF-8"));

            String inputLine;
            while ((inputLine = input.readLine()) != null)
            {
                jsonStringBuilder.append(inputLine + "\n");
            }
            input.close();
            jsonStr = jsonStringBuilder.toString();
        } catch (Exception e) {
            Log.e("getChamNgon", "Error ", e);
        }
        return jsonStr;
    }

}
