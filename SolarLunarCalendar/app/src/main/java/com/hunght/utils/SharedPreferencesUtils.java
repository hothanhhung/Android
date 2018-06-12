package com.hunght.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.ContactsContract;

import com.google.gson.Gson;
import com.hunght.data.NoteItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public static final String ShowDailyNotifyGoodDateBadDateTime = "ShowDailyNotifyGoodDateBadDateTime";

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
            savedObj.DateOfMonth = obj.DateOfMonth;
            savedObj.Month = obj.Month;
            savedObj.Year = obj.Year;
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
        }
    }
}
