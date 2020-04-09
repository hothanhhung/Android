package com.hunght.solarlunarcalendar;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    private static BroadcastReceiver mReceiver;
    private static Timer timer = new Timer();

    public static Calendar lastOn = null;
    public MyService() {
    }

    @Override
    public void onCreate() {
        Log.d("AmDuong MyService", "onCreate: ");
        super.onCreate();
        //IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        //mReceiver = new MyReceiver();
        // Set broadcast receiver priority.
        //filter.setPriority(100);
        //registerReceiver(mReceiver, filter);
    }

    private class mainTask extends TimerTask
    {
        public void run()
        {
           // toastHandler.sendEmptyMessage(0);
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("AmDuong onStartCommand", "onStartCommand: ");
        //super.onStartCommand(intent, flags, startId);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("AmDuong MyService", "onDestroy: ");
        //unregisterReceiver(mReceiver);
        /*Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("com.hunght.solarlunarcalendar.restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);*/
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
}