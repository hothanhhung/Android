package com.hth.utils;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.hth.data.DataAlarm;
import com.hth.lichtivi.MainActivity;
import com.hth.lichtivi.R;

/**
 * Created by Lenovo on 9/7/2016.
 */
public class RingtonePlayingService extends Service {

    private boolean isRunning;
    private Context context;
    MediaPlayer mMediaPlayer;
    private int startId;

    @Override
    public IBinder onBind(Intent intent)
    {
        Log.e("RingtonePlayingService", "In the Richard service");
        return null;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {


        final NotificationManager mNM = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);



        String state = intent.getExtras().getString(DataAlarm.STATE_KEY);
        String title = intent.getExtras().getString(DataAlarm.TITLE_SCHEDULE_KEY);
        String content = intent.getExtras().getString(DataAlarm.CONTENT_SCHEDULE_KEY);

        Intent intent1 = new Intent(this.getApplicationContext(), MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent1, 0);

        Notification mNotify  = new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.icon)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .build();


        Log.e("what is going on here  ", state);

        assert state != null;
        switch (state) {
            case DataAlarm.STATE_NO_KEY:
                startId = 0;
                break;
            case DataAlarm.STATE_YES_KEY:
                startId = 1;
                break;
            default:
                startId = 0;
                break;
        }

        if(!this.isRunning && startId == 1) {
            Log.e("if there was not sound ", " and you want start");
            mMediaPlayer = MediaPlayer.create(this, R.raw.alarm1);
            mMediaPlayer.start();
            mNM.notify(0, mNotify);
            this.isRunning = true;
            this.startId = 0;

        }
        else if (!this.isRunning && startId == 0){
            Log.e("if there was not sound ", " and you want end");
            this.isRunning = false;
            this.startId = 0;

        }

        else if (this.isRunning && startId == 1){
            Log.e("if there is sound ", " and you want start");
            this.isRunning = true;
            this.startId = 0;

        }
        else {
            Log.e("if there is sound ", " and you want end");
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            this.isRunning = false;
            this.startId = 0;
        }

        Log.e("MyActivity", "In the service");

        return START_NOT_STICKY;
    }



    @Override
    public void onDestroy() {
        Log.e("JSLog", "on destroy called");
        super.onDestroy();

        this.isRunning = false;
    }


}