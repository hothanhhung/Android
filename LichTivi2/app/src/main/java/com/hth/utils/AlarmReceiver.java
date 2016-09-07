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
        String title = intent.getExtras().getString(DataAlarm.TITLE_SCHEDULE_KEY);
        String content = intent.getExtras().getString(DataAlarm.CONTENT_SCHEDULE_KEY);

        Log.d("AlarmReceiver/onReceive", title);

        Intent serviceIntent = new Intent(context,RingtonePlayingService.class);
        serviceIntent.putExtra(DataAlarm.STATE_KEY, state);
        serviceIntent.putExtra(DataAlarm.TITLE_SCHEDULE_KEY, title);
        serviceIntent.putExtra(DataAlarm.CONTENT_SCHEDULE_KEY, content);

        context.startService(serviceIntent);
    }
}
