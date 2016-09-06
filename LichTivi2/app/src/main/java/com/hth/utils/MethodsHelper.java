package com.hth.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.provider.Settings;
import android.view.inputmethod.InputMethodManager;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Lenovo on 6/9/2016.
 */
public class MethodsHelper {

    static public String getCurrentDateToOrder(){
        return getCurrentDate("yyyyMMddHHmmss");
    }

    static public String getCurrentDate(){
        return getCurrentDate("HH:mm:ss - MMM dd, yyyy");
    }

    static public String getCurrentDate(String format){
        Date now = new Date();
        String formattedDate = new SimpleDateFormat(format).format(now);
        return formattedDate;
    }

    static public String removeTone(String str){
        str= str.toLowerCase();
        str= str.replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]", "a");
        str= str.replaceAll("[èéẹẻẽêềếệểễ]", "e");
        str= str.replaceAll("[ìíịỉĩ]", "i");
        str= str.replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o");
        str= str.replaceAll("[ùúụủũưừứựửữ]", "u");
        str= str.replaceAll("[ỳýỵỷỹ]", "y");
        str= str.replaceAll("[đ]", "d");
        return str;
    }

    public static String stripAccentsAndD(String s)
    {
        s = s.toLowerCase();
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        s = s.replaceAll("[đ]", "d");
        return s;
    }

    public static String stripAccents(String s)
    {
        s = s.toLowerCase();
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
       // s = s.replaceAll("[^\\p{ASCII}]", "");
       // s.replaceAll('đ', 'd');
        return s;
    }

    public static String getStringFromDate(Date date) {
        StringBuilder str = new StringBuilder().append(date.getDate() > 9 ? date.getDate() : "0" + date.getDate()).append("-")
                .append(date.getMonth() > 8 ? (date.getMonth() + 1) : "0" + (date.getMonth() + 1)).append("-").append(date.getYear());
        return str.toString();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static String getAppVersion(Context context)
    {
        String version = "";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionName;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return version;

    }

    public static String getAndroidId(Context context)
    {
        String android_id = "";
        try {
            android_id = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return android_id;

    }
}
