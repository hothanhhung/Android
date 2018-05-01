package com.hunght.solarlunarcalendar;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.hunght.data.DateItemForGridview;
import com.hunght.utils.ServiceProcessor;

import java.util.Date;

public class MyService extends Service {
    BroadcastReceiver mReceiver;

    public static Date lastOn = null, lastGoodBadOn = null, lastChamNgonOn = null;
    public MyService() {
    }

    @Override
    public void onCreate() {
        Log.d("AmDuong MyService", "onCreate: ");
        super.onCreate();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        mReceiver = new MyReceiver();
        registerReceiver(mReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("AmDuong onStartCommand", "onStartCommand: ");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
}