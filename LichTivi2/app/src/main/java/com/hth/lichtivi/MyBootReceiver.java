package com.hth.lichtivi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hth.utils.AlarmItem;

import java.util.ArrayList;

/**
 * Created by Lenovo on 9/12/2016.
 */
public class MyBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("MyBootReceiver", intent.getAction());
        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                // Set the alarm here.
            Log.d("MyBootReceiver", "onReceive1");
            AlarmItemsManager.resetupAlarmIfNeed(context);
        }
    }

}