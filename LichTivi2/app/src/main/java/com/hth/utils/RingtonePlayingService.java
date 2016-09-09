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
import android.os.Vibrator;
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
    Vibrator vibrator;
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
        String programName = intent.getExtras().getString(DataAlarm.PROGRAM_NAME_SCHEDULE_KEY);
        String startOn = intent.getExtras().getString(DataAlarm.START_ON_SCHEDULE_KEY);
        String channelName = intent.getExtras().getString(DataAlarm.CHANNEL_NAME_SCHEDULE_KEY);
        int idOfAlarm = intent.getExtras().getInt(DataAlarm.ALARM_ID_SCHEDULE_KEY);
        boolean vibrate = intent.getExtras().getBoolean(DataAlarm.VIBRATE_SCHEDULE_KEY);

        Intent serviceIntent = new Intent(this.getApplicationContext(),AlarmReceiver.class);
        serviceIntent.putExtra(DataAlarm.STATE_KEY, DataAlarm.STATE_NO_KEY);
        serviceIntent.putExtra(DataAlarm.PROGRAM_NAME_SCHEDULE_KEY, programName);
        serviceIntent.putExtra(DataAlarm.START_ON_SCHEDULE_KEY, startOn);
        serviceIntent.putExtra(DataAlarm.CHANNEL_NAME_SCHEDULE_KEY, channelName);
        serviceIntent.putExtra(DataAlarm.ALARM_ID_SCHEDULE_KEY, idOfAlarm);
        serviceIntent.putExtra(DataAlarm.VIBRATE_SCHEDULE_KEY, vibrate);
        PendingIntent pserviceIntent = PendingIntent.getBroadcast(this, idOfAlarm, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intent1 = new Intent(this.getApplicationContext(), MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent1, 0);

        //Notification.Action repeatButton = (new Notification.Action.Builder()).build();
        Notification mNotify  = new Notification.Builder(this)
                .setContentTitle(programName)
                .setContentText("Phát lúc ".concat(startOn).concat(" trên kênh ").concat(channelName))
                .setSmallIcon(R.drawable.icon)
                .addAction(R.drawable.circle_close, "Đóng", pserviceIntent) // #0
               // .addAction(R.drawable.ic_pause, "Pause", pausePendingIntent)
                //.setContentIntent(pIntent)
                .setAutoCancel(true)
                .setDeleteIntent(pserviceIntent)
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
            if(vibrate){
                // Vibrate for 5000 milliseconds
                vibrator = (Vibrator) this.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(5000);
            }
            mMediaPlayer = MediaPlayer.create(this, R.raw.alarm2);
            //mMediaPlayer.setLooping(true);
            mMediaPlayer.start();
            mNM.notify(idOfAlarm, mNotify);
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

            if(vibrator!=null){
                vibrator.cancel();
            }
            mNM.cancel(idOfAlarm);
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