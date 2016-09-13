package com.hth.lichtivi;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hth.utils.AlarmItem;
import com.hth.utils.ScheduleItem;

import java.util.ArrayList;

/**
 * Created by Lenovo on 9/7/2016.
 */
public class AlarmItemsManager {

    // Set alarm and save to record
    static private Intent getIntent(Context active, AlarmItem alarmItem, String state) {
        Intent myIntent = new Intent(active, AlarmReceiver.class);
        myIntent.putExtra(DataAlarm.STATE_KEY, state);
        myIntent.putExtra(DataAlarm.PROGRAM_NAME_SCHEDULE_KEY, alarmItem.getProgramName());
        myIntent.putExtra(DataAlarm.START_ON_SCHEDULE_KEY, alarmItem.getStartOnTime());
        myIntent.putExtra(DataAlarm.CHANNEL_NAME_SCHEDULE_KEY, alarmItem.getChannelName());
        myIntent.putExtra(DataAlarm.ALARM_ID_SCHEDULE_KEY, alarmItem.getId());
        myIntent.putExtra(DataAlarm.VIBRATE_SCHEDULE_KEY, alarmItem.isVibrate());
        return myIntent;
    }


    static public void setAlarm(Context active, AlarmItem alarmItem)
    {
        ArrayList<AlarmItem> alarmItems = DataAlarm.getAlarms(active);
        if(alarmItems == null) alarmItems = new ArrayList<>();
        int index = getIndex(alarmItems, alarmItem);
        if(index == -1) {
            Log.d("setAlarm", alarmItem.getKey());
            //set alarm
            AlarmManager alarmManager = (AlarmManager) active.getSystemService(active.ALARM_SERVICE);
            Intent myIntent = getIntent(active, alarmItem, DataAlarm.STATE_YES_KEY);
            PendingIntent pending_intent = PendingIntent.getBroadcast(active, alarmItem.getId(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            if(android.os.Build.VERSION.SDK_INT >= 19)
            {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmItem.getTimeToRemindInMiliSecond(), pending_intent);
            }else{
                alarmManager.set(AlarmManager.RTC_WAKEUP, alarmItem.getTimeToRemindInMiliSecond(), pending_intent);
            }

            //save alarm
            alarmItems.add(alarmItem);
            DataAlarm.setAlarmItemsToRecord(active, alarmItems);
        }
    }

    // cancel alarm and remove it from record
    static public void deleteAlarm(Context active, AlarmItem alarmItem)
    {
        ArrayList<AlarmItem> alarmItems = DataAlarm.getAlarms(active);
        if(alarmItems != null)
        {
            int index = getIndex(alarmItems, alarmItem);
            if(index!=-1){
                // cancel alarm
                AlarmManager alarmManager = (AlarmManager) active.getSystemService(active.ALARM_SERVICE);
                Intent myIntent = getIntent(active, alarmItem, DataAlarm.STATE_NO_KEY);
                PendingIntent pending_intent = PendingIntent.getBroadcast(active, alarmItem.getId(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                active.sendBroadcast(myIntent);
                alarmManager.cancel(pending_intent);

                // remove alarm
                alarmItems.remove(index);
                DataAlarm.setAlarmItemsToRecord(active, alarmItems);
            }
        }
    }

    static public void resetupAlarmIfNeed(Context context){
        ArrayList<AlarmItem> alarmItems= DataAlarm.getAlarmsFromRecord(context);
        Log.d("resetAlarmIfNeed", "comming");
        if(alarmItems!=null){
            long currentTime = System.currentTimeMillis();
            for (AlarmItem alarmItem:alarmItems) {
                if (alarmItem.getTimeToRemindInMiliSecond() > currentTime)
                {
                    Intent myIntent = getIntent(context, alarmItem, DataAlarm.STATE_YES_KEY);
                    PendingIntent pending_intent = PendingIntent.getBroadcast(context, alarmItem.getId(), myIntent, PendingIntent.FLAG_NO_CREATE);
                    if (pending_intent == null)
                    {
                        Log.d("resetAlarmIfNeed", alarmItem.getKey());
                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
                        pending_intent = PendingIntent.getBroadcast(context, alarmItem.getId(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                        if(android.os.Build.VERSION.SDK_INT >= 19)
                        {
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmItem.getTimeToRemindInMiliSecond(), pending_intent);
                        }else{
                            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmItem.getTimeToRemindInMiliSecond(), pending_intent);
                        }
                    }
                }
            }
        }
    }

    static public AlarmItem getAlarmItem(Context active, ScheduleItem scheduleItem)
    {
        ArrayList<AlarmItem> alarmItems = DataAlarm.getAlarms(active);
        if(alarmItems != null && alarmItems.size() > 0) {
            for (int i = 0; i < alarmItems.size(); i++) {
                if (scheduleItem.getKey().equalsIgnoreCase(alarmItems.get(i).getKey())) {
                    return alarmItems.get(i);
                }
            }
        }
        return new AlarmItem(0, scheduleItem);
    }

    // get index of schedule in alarms
    static public int getIndex(Context active,ScheduleItem scheduleItem)
    {
        ArrayList<AlarmItem> alarmItems = DataAlarm.getAlarms(active);
        if(alarmItems != null && alarmItems.size() > 0) {
            for (int i = 0; i < alarmItems.size(); i++) {
                if (scheduleItem.getKey().equalsIgnoreCase(alarmItems.get(i).getKey())) {
                    return i;
                }
            }
        }
        return -1;
    }

    // get index of alarm in alarms
    static private int getIndex(ArrayList<AlarmItem> alarmItems, AlarmItem alarmItem)
    {
        for (int i = 0; i< alarmItems.size(); i++)
        {
            if(alarmItem.getKey().equalsIgnoreCase(alarmItems.get(i).getKey())){
                return i;
            }
        }
        return -1;
    }
}
