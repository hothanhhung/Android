package com.hth.datmon;

import android.app.Application;
import android.content.Context;

/**
 * Created by Lenovo on 12/5/2016.
 */

public class DatMonApp extends Application {
    private static DatMonApp mInstance = new DatMonApp();
    private static Context mAppContext;

    public static DatMonApp getInstance() {
        return mInstance;
    }

    public static Context getAppContext() {
        return mAppContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;
        mAppContext=this.getApplicationContext();
    }
}
