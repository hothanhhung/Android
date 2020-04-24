package com.hunght.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.view.inputmethod.InputMethodManager;

import com.hunght.solarlunarcalendar.MainActivity;

import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Lenovo on 6/9/2016.
 */
public class MethodsHelper {

    public static String stripAccentsAndD(String s)
    {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        s = s.replaceAll("[đ]", "d");
        s = s.replaceAll("[Đ]", "D");
        return s;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getApplicationContext().getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }

    static public String getCurrentDateToOrder(){
        return getCurrentDate("yyyyMMdd-HHmmss");
    }

    static public String getCurrentDate(){
        return getCurrentDate("HH:mm:ss - MMM dd, yyyy");
    }

    static public String getCurrentDate(String format){
        Date now = new Date();
        String formattedDate = new SimpleDateFormat(format).format(now);
        return formattedDate;
    }

    static public String getScoreInFormat(int score){
        return String.format("%1$05d", score);

    }

    static public String getTimeFormat(Long var1) {
        String var2 = String.valueOf((int) (var1.longValue() / 3600L));
        String var3 = String.valueOf((int) (var1.longValue() % 3600L / 60L));
        String var4 = String.valueOf((int) (var1.longValue() % 60L));
        if (var2.length() == 1) {
            var2 = "0".concat(var2);
        }

        if (var3.length() == 1) {
            var3 = "0".concat(var3);
        }

        if (var4.length() == 1) {
            var4 = "0".concat(var4);
        }

        return var2.concat(":").concat(var3).concat(":").concat(var4);
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

    public static String getStringFromFloat(float number) {
        int num = Math.round(number * 1000);
        return getStringFromInt(num);
    }

    public static String getStringFromInt(int number) {
//        return String.format("%.d%n", number);
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMANY);
        DecimalFormat myFormatter = (DecimalFormat)nf;
        String output = myFormatter.format(number);
        return output;
    }

    public static String getPackageName(Context context)
    {
        String packageName = "";
        try {
            packageName = context.getPackageName();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return packageName;

    }

    public static String getDDMMYYY(Calendar calendar) {
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        return (day < 10 ? "0" : "") + day + "-" + (month < 10 ? "0" : "") + month + "-" + year;
    }

    public static String getYYYYMMDD(Calendar calendar) {
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        return year + "" + (month < 10 ? "0" : "") + month + "" + (day < 10 ? "0" : "") + day;
    }

    public static String getYYYYMMDDhhmmss(Calendar calendar) {
        int second = calendar.get(Calendar.SECOND);
        int minute = calendar.get(Calendar.MINUTE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        return (year + "" + (month < 10 ? "0" : "") + month + "" + (day < 10 ? "0" : "") + day
                + (hour < 10 ? "0" : "") + hour + "" + (minute < 10 ? "0" : "") + minute+ "" + (second < 10 ? "0" : "") + second);
    }

    public static boolean checkPermission(Context context, int permissionKey)
    {
        switch (permissionKey) {
            case MainActivity.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions((Activity) context,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MainActivity.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                    return false;
                } else {
                    // Permission has already been granted
                    return true;
                }
            case MainActivity.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions((Activity) context,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MainActivity.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    return false;
                } else {
                    // Permission has already been granted
                    return true;
                }
        }
        return false;
    }
}
