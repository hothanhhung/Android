package com.hth.utils;

import com.hth.lichtivi.Data;

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
    public String getStartOn() {
        if(scheduleItem!=null)
        {
            return scheduleItem.getStartOn().concat(" ngày ").concat(getDateOfSchedule());
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
            return getDateOfSchedule().concat(scheduleItem.getStartOn()).concat(scheduleItem.getChannelKey());
        }
        return "";
    }
}
