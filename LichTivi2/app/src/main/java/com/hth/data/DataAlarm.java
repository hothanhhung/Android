package com.hth.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hth.utils.AlarmItem;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Lenovo on 9/7/2016.
 */
public class DataAlarm {
    public  static final String STATE_YES_KEY = "STATE_YES_KEY";
    public  static final String STATE_NO_KEY = "STATE_NO_KEY";
    public  static final String STATE_KEY = "STATE_KEY";
    public  static final String TITLE_SCHEDULE_KEY = "TITLE_SCHEDULE_KEY";
    public  static final String CONTENT_SCHEDULE_KEY = "CONTENT_SCHEDULE_KEY";

    final private static String ALARM_KEY_NAME = "LichTiviAlarmsRecord";

    public static  ArrayList<AlarmItem> alarmItems;
    static public ArrayList<AlarmItem> getAlarms(Activity ctx)
    {
        if(alarmItems == null){
            alarmItems = getAlarmsFromRecord(ctx);
        }
        return alarmItems;
    }

    static public ArrayList<AlarmItem> getAlarmsFromRecord(Activity ctx)
    {
        ArrayList<AlarmItem> listAlarmItem = new ArrayList<AlarmItem>();
        {
            try {
                SharedPreferences sharedPref =ctx.getPreferences(Context.MODE_PRIVATE);
                String favoritesStr = sharedPref.getString(ALARM_KEY_NAME, "");
                if(!favoritesStr.isEmpty())
                {
                    Gson gSon = new Gson();
                    Type collectionType = new TypeToken<ArrayList<AlarmItem>>(){}.getType();
                    listAlarmItem = gSon.fromJson(favoritesStr, collectionType);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return listAlarmItem;
    }


    static public void setAlarmItemsToRecord(Activity ctx, ArrayList<AlarmItem> lstObject)
    {
        try {

            Gson gSon = new Gson();
            String dataInString = gSon.toJson(lstObject);
            SharedPreferences sharedPref = ctx.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(ALARM_KEY_NAME, dataInString);
            editor.commit();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}
