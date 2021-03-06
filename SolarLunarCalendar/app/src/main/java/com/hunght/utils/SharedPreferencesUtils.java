package com.hunght.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

import com.google.gson.Gson;
import com.hunght.data.NoteItem;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Lenovo on 4/20/2018.
 */

public class SharedPreferencesUtils {
    private static final String APP_SHARED_PREFS = "com.hunght.solarlunarcalendar";
    public static final String NotesKey = "NotesKey";
    public static final String ShowGoodDayBadDateSetting = "ShowGoodDayBadDateSetting";
    public static final String ShowNotifyChamNgonSetting = "ShowNotifyChamNgonSetting";
    public static final String ShowDailyNotifyGoodDateBadDateSetting = "ShowDailyNotifyGoodDateBadDateSetting";
    public static final String ShowDailyNotifyEventSetting = "ShowDailyNotifyEventSetting";
    public static final String ShowDailyNotifyEvent = "ShowDailyNotifyEvent";
    public static final String ShowChamNgonSetting = "ShowChamNgonSetting";
    public static final String ShowDailyGoodDateBadDate = "ShowDailyGoodDateBadDate";
    public static final String ShowNotifyChamNgonTime = "ShowNotifyChamNgonTime";
    public static final String ShowNotifyNgayRam = "ShowNotifyNgayRam";
    public static final String ShowDailyNotifyGoodDateBadDateTime = "ShowDailyNotifyGoodDateBadDateTime";
    public static final String ShowDailyNotifyReminding = "ShowDailyNotifyReminding";
    public static final String ShowNgayRamNotifyReminding = "ShowNgayRamNotifyReminding";
    public static final String AlarmVersion = "AlarmVersionInAppOfHungHt";
    public static final String ShowSuggestViewTuVi = "ShowSuggestViewTuVi";
    public static final String ShowWidgetConGiap = "ShowWidgetConGiap";
    public static final String SettingWidgetTextColor = "SettingWidgetTextColor";
    public static final String SettingWidgetTextFontSize = "SettingWidgetTextFontSize";
    public static final String SettingWidgetLastUpdateDate = "SettingWidgetLastUpdateDate";

    static public int getSettingWidgetLastUpdateDate(Context var1)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            return appSharedPrefs.getInt(SettingWidgetLastUpdateDate, 0);
        }catch (Exception ex){
            return 0;
        }
    }

    static public void setSettingWidgetLastUpdateDate(Context var1, int value)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            prefsEditor.putInt(SettingWidgetLastUpdateDate, value);
            prefsEditor.commit();
        }catch (Exception ex){
        }
    }

    static public int getWidgetTextFontSize(Context var1)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            return appSharedPrefs.getInt(SettingWidgetTextFontSize, 14);
        }catch (Exception ex){
            return 0;
        }
    }


    static public void setWidgetTextFontSize(Context var1, int value)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            prefsEditor.putInt(SettingWidgetTextFontSize, value);
            prefsEditor.commit();
        }catch (Exception ex){
        }
    }

    static public int getWidgetTextColor(Context var1)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            return appSharedPrefs.getInt(SettingWidgetTextColor, Color.WHITE);
        }catch (Exception ex){
            return 0;
        }
    }

    static public void setWidgetTextColor(Context var1, int value)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            prefsEditor.putInt(SettingWidgetTextColor, value);
            prefsEditor.commit();
        }catch (Exception ex){
        }
    }

    static public int getAlarmVersion(Context var1)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            return appSharedPrefs.getInt(AlarmVersion, 0);
        }catch (Exception ex){
            return 0;
        }
    }

    static public void setAlarmVersion(Context var1, int value)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            prefsEditor.putInt(AlarmVersion, value);
            prefsEditor.commit();
        }catch (Exception ex){
        }
    }

    static public int getShowNgayRamNotifyReminding(Context var1)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            return appSharedPrefs.getInt(ShowNgayRamNotifyReminding, 0);
        }catch (Exception ex){
            return 0;
        }
    }

    static public void setShowSuggestTuVi(Context var1, int value)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            prefsEditor.putInt(ShowSuggestViewTuVi, value);
            prefsEditor.commit();
        }catch (Exception ex){
        }
    }

    static public int getShowSuggestTuVi(Context var1)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            return appSharedPrefs.getInt(ShowSuggestViewTuVi, 0);
        }catch (Exception ex){
            return 0;
        }
    }

    static public void setShowNgayRamNotifyReminding(Context var1, int value)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            prefsEditor.putInt(ShowNgayRamNotifyReminding, value);
            prefsEditor.commit();
        }catch (Exception ex){
        }
    }

    static public int getShowDailyNotifyReminding(Context var1)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            return appSharedPrefs.getInt(ShowDailyNotifyReminding, 0);
        }catch (Exception ex){
            return 0;
        }
    }

    static public void setShowDailyNotifyReminding(Context var1, int value)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            prefsEditor.putInt(ShowDailyNotifyReminding, value);
            prefsEditor.commit();
        }catch (Exception ex){
        }
    }

    static public int getShowDailyNotifyGoodDateBadDateTime(Context var1)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            return appSharedPrefs.getInt(ShowDailyNotifyGoodDateBadDateTime, 0);
        }catch (Exception ex){
            return 0;
        }
    }

    static public void setShowDailyNotifyGoodDateBadDateTime(Context var1, int value)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            prefsEditor.putInt(ShowDailyNotifyGoodDateBadDateTime, value);
            prefsEditor.commit();
        }catch (Exception ex){
        }
    }

    static public int getShowNotifyChamNgonTime(Context var1)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            return appSharedPrefs.getInt(ShowNotifyChamNgonTime, 0);
        }catch (Exception ex){
            return 0;
        }
    }

    static public void setShowNotifyChamNgonTime(Context var1, int value)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            prefsEditor.putInt(ShowNotifyChamNgonTime, value);
            prefsEditor.commit();
        }catch (Exception ex){
        }
    }
    static public boolean getShowDailyNotifyEvent(Context var1)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            return appSharedPrefs.getBoolean(ShowDailyNotifyEvent, true);
        }catch (Exception ex){
            return true;
        }
    }

    static public void setShowDailyNotifyEvent(Context var1, boolean value)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            prefsEditor.putBoolean(ShowDailyNotifyEvent, value);
            prefsEditor.commit();
        }catch (Exception ex){
        }
    }

    static public boolean getShowDailyNotifyEventSetting(Context var1)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            return appSharedPrefs.getBoolean(ShowDailyNotifyEventSetting, true);
        }catch (Exception ex){
            return true;
        }
    }

    static public void setShowDailyNotifyEventSetting(Context var1, boolean value)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            prefsEditor.putBoolean(ShowDailyNotifyEventSetting, value);
            prefsEditor.commit();
        }catch (Exception ex){
        }
    }

    static public boolean getShowDailyNotifyGoodDateBadDateSetting(Context var1)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            return appSharedPrefs.getBoolean(ShowDailyNotifyGoodDateBadDateSetting, true);
        }catch (Exception ex){
            return true;
        }
    }

    static public void setShowDailyNotifyGoodDateBadDateSetting(Context var1, boolean value)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            prefsEditor.putBoolean(ShowDailyNotifyGoodDateBadDateSetting, value);
            prefsEditor.commit();
        }catch (Exception ex){
        }
    }

    static public boolean getShowNotifyChamNgonSetting(Context var1)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            return appSharedPrefs.getBoolean(ShowNotifyChamNgonSetting, true);
        }catch (Exception ex){
            return true;
        }
    }

    static public void setShowNotifyChamNgonSetting(Context var1, boolean value)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            prefsEditor.putBoolean(ShowNotifyChamNgonSetting, value);
            prefsEditor.commit();
        }catch (Exception ex){
        }
    }

    static public boolean getShowNotifyNgayRam(Context var1)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            return appSharedPrefs.getBoolean(ShowNotifyNgayRam, true);
        }catch (Exception ex){
            return true;
        }
    }

    static public void setShowNotifyNgayRam(Context var1, boolean value)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            prefsEditor.putBoolean(ShowNotifyNgayRam, value);
            prefsEditor.commit();
        }catch (Exception ex){
        }
    }

    static public boolean getShowWidgetConGiap(Context var1)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            return appSharedPrefs.getBoolean(ShowWidgetConGiap, true);
        }catch (Exception ex){
            return true;
        }
    }

    static public void setShowWidgetConGiap(Context var1, boolean value)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            prefsEditor.putBoolean(ShowWidgetConGiap, value);
            prefsEditor.commit();
        }catch (Exception ex){
        }
    }

    static public boolean getShowGoodDayBadDateSetting(Context var1)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            return appSharedPrefs.getBoolean(ShowGoodDayBadDateSetting, true);
        }catch (Exception ex){
            return true;
        }
    }

    static public void setShowGoodDayBadDateSetting(Context var1, boolean value)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            prefsEditor.putBoolean(ShowGoodDayBadDateSetting, value);
            prefsEditor.commit();
        }catch (Exception ex){
        }
    }

    static public boolean getShowChamNgonSetting(Context var1)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            return appSharedPrefs.getBoolean(ShowChamNgonSetting, true);
        }catch (Exception ex){
            return true;
        }
    }

    static public void setShowChamNgonSetting(Context var1, boolean value)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            prefsEditor.putBoolean(ShowChamNgonSetting, value);
            prefsEditor.commit();
        }catch (Exception ex){
        }
    }


    static public ArrayList<NoteItem> getNoteItems(Context var1)
    {
        NoteItem[] items =  getItem(var1, NotesKey, NoteItem[].class);
        if(items == null) {
            return  new ArrayList<NoteItem>();
        }
        return new ArrayList<>(Arrays.asList(items));
    }

    static public ArrayList<NoteItem> deleteNoteItem(Context var1, String id)
    {
        ArrayList<NoteItem> lstObjs = getNoteItems(var1);
        NoteItem savedObj = null;
        for (NoteItem item:lstObjs) {
            if(item.getId().equals(id)){
                savedObj = item;
                break;
            }
        }
        if(savedObj!=null) {
            lstObjs.remove(savedObj);
            saveNoteItems(var1, lstObjs);
        }
        return lstObjs;
    }

    static public void updateNoteItems(Context var1, NoteItem obj)
    {
        if(obj.Id == null || obj.Id.isEmpty())
        {
            obj.Id = java.util.UUID.randomUUID().toString();
        }
        ArrayList<NoteItem> lstObjs = getNoteItems(var1);
        NoteItem savedObj = null;
        for (NoteItem item:lstObjs) {
            if(item.getId().equals(obj.getId())){
                savedObj = item;
                break;
            }
        }

        if(savedObj == null)
        {
            lstObjs.add(obj);
        }else{
            savedObj.updateDate(obj.DateOfMonth, obj.Month, obj.Year);
            savedObj.Subject = obj.Subject;
            savedObj.Content = obj.Content;
            savedObj.RemindType = obj.RemindType;
            savedObj.haveDate = obj.haveDate;
        }

        saveNoteItems(var1, lstObjs);
    }

    static public void saveNoteItems(Context var1, ArrayList<NoteItem> obj)
    {
        setItems(var1, NotesKey, obj.toArray());
    }

    static private <T> T getItem(Context var1, String key, Class<T> tClass) {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            Gson gson = new Gson();
            String json = appSharedPrefs.getString(key, "");
            T items = gson.fromJson(json, tClass);
            return items;
        }catch (Exception ex){
            return null;
        }
    }

    static private <T> void setItems(Context var1, String key, T[] obj) {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(obj);
            prefsEditor.putString(key, json);
            prefsEditor.commit();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
