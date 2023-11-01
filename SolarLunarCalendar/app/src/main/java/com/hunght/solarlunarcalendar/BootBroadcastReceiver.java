package com.hunght.solarlunarcalendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.Calendar;

public class BootBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "AmDuong";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            // Do your work related to alarm manager
            Intent liveIntent = new Intent(context, AReceiver.class);
			int flagPendingIntent = PendingIntent.FLAG_CANCEL_CURRENT;
			if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
				flagPendingIntent = flagPendingIntent|PendingIntent.FLAG_IMMUTABLE;
			}
            PendingIntent recurring = PendingIntent.getBroadcast(context, 0, liveIntent, flagPendingIntent);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Calendar updateTime = Calendar.getInstance();
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, updateTime.getTimeInMillis(), 31 * 60 * 1000, recurring);
            //wakeup and starts service in every 16 minutes.
            Log.d(TAG, "AlarmManager scheduled after reboot");
        }
    }
}