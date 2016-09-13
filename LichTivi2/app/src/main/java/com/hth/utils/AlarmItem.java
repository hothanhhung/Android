package com.hth.utils;

import com.hth.lichtivi.Data;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Lenovo on 9/6/2016.
 */
public class AlarmItem {
    int id;
    int remindBeforeInMinute;
    ScheduleItem scheduleItem;

    public AlarmItem(int remindBeforeInMinute, ScheduleItem scheduleItem)
    {
        this.id = (int) System.currentTimeMillis()/1000;
        this.remindBeforeInMinute = remindBeforeInMinute;
        this.scheduleItem = scheduleItem;
    }

    public String getDateOfSchedule() {
        if(scheduleItem!=null)
        {
            return scheduleItem.getDateOn();
        }
        return "";
    }

    public int getId() {
        return id;
    }

    public int getRemindBeforeInMinute() {
        return remindBeforeInMinute;
    }

    public ScheduleItem getScheduleItem() {
        return scheduleItem;
    }

    public String getProgramName() {
        if(scheduleItem!=null)
        {
            return scheduleItem.getTextProgramName();
        }
        return "";
    }

    public String getStartOnTime() {
        if(scheduleItem!=null)
        {
            return scheduleItem.getStartOn();
        }
        return "";
    }

    public String getStartOn() {
        if(scheduleItem!=null)
        {
            return scheduleItem.getStartOn().concat(" ngày ").concat( MethodsHelper.getddMMyyyyFromString(getDateOfSchedule()));
        }
        return "";
    }

    public String getChannelName() {
        if(scheduleItem!=null)
        {
            return Data.getChannelName(scheduleItem.getChannelKey());
        }
        return "";
    }

    public String getKey()
    {
        if(scheduleItem!=null){
            return scheduleItem.getKey();
        }
        return "";
    }

    public void setRemindBeforeInMinute(int remindBeforeInMinute)
    {
        this.remindBeforeInMinute = remindBeforeInMinute;
    }

    public long getTimeToRemindInMiliSecond()
    {
        /*Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 3);
        if(true) return calendar.getTimeInMillis();*/
        Date date = MethodsHelper.getDateFromString(getDateOfSchedule(), getStartOnTime());
        return (date.getTime() - remindBeforeInMinute * 60 * 1000) ;
    }

    public boolean isVibrate()
    {
        return true;
    }
}
