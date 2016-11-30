package com.hth.datmon;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.provider.Settings;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Lenovo on 6/9/2016.
 */
public class MethodsHelper {


    public static String stripAccentsAndD(String s)
    {
        s = s.toLowerCase();
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        s = s.replaceAll("[đ]", "d");
        return s;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getApplicationContext().getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }

    static public String getddMMyyyyFromString(String date){
        Date rs = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try
        {
            rs = simpleDateFormat.parse(date);
            return outDateFormat.format(rs);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return "";
    }

    static public String getCurrentDateToOrder(){
        return getCurrentDate("yyyyMMddHHmmss");
    }

    static public String getCurrentDate(String format){
        Date now = new Date();
        String formattedDate = new SimpleDateFormat(format).format(now);
        return formattedDate;
    }
}
