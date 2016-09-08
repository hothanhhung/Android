package com.hth.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.hth.data.DataAlarm;

import java.util.ArrayList;

/**
 * Created by Lenovo on 9/7/2016.
 */
public class AlarmItemsManager {

    // Set alarm and save to record
    static public void setAlarm(Activity active, AlarmItem alarmItem)
    {
        ArrayList<AlarmItem> alarmItems = DataAlarm.getAlarms(active);
        if(alarmItems == null) alarmItems = new ArrayList<>();
        int index = getIndex(alarmItems, alarmItem);
        if(index == -1) {
            //set alarm
            AlarmManager alarmManager = (AlarmManager) active.getSystemService(active.ALARM_SERVICE);
            Intent myIntent = new Intent(active, AlarmReceiver.class);
            myIntent.putExtra(DataAlarm.STATE_KEY, DataAlarm.STATE_YES_KEY);
            myIntent.putExtra(DataAlarm.PROGRAM_NAME_SCHEDULE_KEY, alarmItem.getProgramName());
            myIntent.putExtra(DataAlarm.START_ON_SCHEDULE_KEY, alarmItem.getStartOnTime());
            myIntent.putExtra(DataAlarm.CHANNEL_NAME_SCHEDULE_KEY, alarmItem.getChannelName());
            myIntent.putExtra(DataAlarm.ALARM_ID_SCHEDULE_KEY, alarmItem.getId());
            myIntent.putExtra(DataAlarm.VIBRATE_SCHEDULE_KEY, alarmItem.isVibrate());
            PendingIntent pending_intent = PendingIntent.getBroadcast(active, alarmItem.getId(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmItem.getTimeToRemindInMiliSecond(), pending_intent);

            //save alarm
            alarmItems.add(alarmItem);
            DataAlarm.setAlarmItemsToRecord(active, alarmItems);
        }
    }

    // cancel alarm and remove it from record
    static public void deleteAlarm(Activity active, AlarmItem alarmItem)
    {
        ArrayList<AlarmItem> alarmItems = DataAlarm.getAlarms(active);
        if(alarmItems != null)
        {
            int index = getIndex(alarmItems, alarmItem);
            if(index!=-1){

                // cancel alarm
                AlarmManager alarmManager = (AlarmManager) active.getSystemService(active.ALARM_SERVICE);
                Intent myIntent = new Intent(active, AlarmReceiver.class);
                myIntent.putExtra(DataAlarm.STATE_KEY, DataAlarm.STATE_NO_KEY);
                myIntent.putExtra(DataAlarm.PROGRAM_NAME_SCHEDULE_KEY, alarmItem.getProgramName());
                myIntent.putExtra(DataAlarm.START_ON_SCHEDULE_KEY, alarmItem.getStartOnTime());
                myIntent.putExtra(DataAlarm.CHANNEL_NAME_SCHEDULE_KEY, alarmItem.getChannelName());
                myIntent.putExtra(DataAlarm.ALARM_ID_SCHEDULE_KEY, alarmItem.getId());
                myIntent.putExtra(DataAlarm.VIBRATE_SCHEDULE_KEY, alarmItem.isVibrate());
                PendingIntent pending_intent = PendingIntent.getBroadcast(active, alarmItem.getId(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                active.sendBroadcast(myIntent);
                alarmManager.cancel(pending_intent);

                // remove alarm
                alarmItems.remove(index);
                DataAlarm.setAlarmItemsToRecord(active, alarmItems);
            }
        }
    }

    // get index of schedule in alarms
    static public int getIndex(Activity active,ScheduleItem scheduleItem)
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
