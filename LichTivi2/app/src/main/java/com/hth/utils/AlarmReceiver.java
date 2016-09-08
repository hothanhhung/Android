package com.hth.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hth.data.DataAlarm;

/**
 * Created by Lenovo on 9/7/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        String state = intent.getExtras().getString(DataAlarm.STATE_KEY);
        String programName = intent.getExtras().getString(DataAlarm.PROGRAM_NAME_SCHEDULE_KEY);
        String startOn = intent.getExtras().getString(DataAlarm.START_ON_SCHEDULE_KEY);
        String channelName = intent.getExtras().getString(DataAlarm.CHANNEL_NAME_SCHEDULE_KEY);
        int id = intent.getExtras().getInt(DataAlarm.ALARM_ID_SCHEDULE_KEY);
        boolean vibrate = intent.getExtras().getBoolean(DataAlarm.VIBRATE_SCHEDULE_KEY);

        Log.d("AlarmReceiver/onReceive", programName);

        Intent serviceIntent = new Intent(context,RingtonePlayingService.class);
        serviceIntent.putExtra(DataAlarm.STATE_KEY, state);
        serviceIntent.putExtra(DataAlarm.PROGRAM_NAME_SCHEDULE_KEY, programName);
        serviceIntent.putExtra(DataAlarm.START_ON_SCHEDULE_KEY, startOn);
        serviceIntent.putExtra(DataAlarm.CHANNEL_NAME_SCHEDULE_KEY, channelName);
        serviceIntent.putExtra(DataAlarm.ALARM_ID_SCHEDULE_KEY, id);
        serviceIntent.putExtra(DataAlarm.VIBRATE_SCHEDULE_KEY, vibrate);

        context.startService(serviceIntent);
    }
}
