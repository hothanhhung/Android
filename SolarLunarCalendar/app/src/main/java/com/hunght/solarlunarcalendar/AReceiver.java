package com.hunght.solarlunarcalendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AReceiver extends BroadcastReceiver {
    private static final String TAG = "AmDuong";
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Log.d(TAG, "onReceive");
            MyReceiver myReceiver = new MyReceiver();
            myReceiver.start(context);
        } catch (Exception e) {
            Log.d(TAG, "AReceiver"+e.getMessage());
            e.printStackTrace();
        }
    }
}