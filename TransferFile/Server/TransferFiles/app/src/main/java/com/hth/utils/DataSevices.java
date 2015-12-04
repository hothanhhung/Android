package com.hth.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Lenovo on 12/4/2015.
 */
public class DataSevices {
    public static final String KEY_LAST_PORT = "KEY_LAST_PORT";
    private static final int DEFAULT_PORT = 12345;

    public static void saveLastPort(Activity activity, int value)
    {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(KEY_LAST_PORT, value);
        editor.commit();
    }
    public static int getLastPort(Activity activity)
    {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        int lastPort = sharedPref.getInt(KEY_LAST_PORT, DEFAULT_PORT);
        return lastPort;
    }
}
