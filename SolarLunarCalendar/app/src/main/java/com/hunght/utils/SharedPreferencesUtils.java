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
    public static final String ShowGoodDayBadDate = "ShowGoodDayBadDate";
    public static final String ShowNotifyChamNgon = "ShowNotifyChamNgon";
    public static final String ShowDailyNotifyGoodDateBadDate = "ShowDailyNotifyGoodDateBadDate";
    public static final String ShowChamNgon = "ShowChamNgon";
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


    static public boolean getShowDailyNotifyGoodDateBadDate(Context var1)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            return appSharedPrefs.getBoolean(ShowDailyNotifyGoodDateBadDate, true);
        }catch (Exception ex){
            return true;
        }
    }

    static public void setShowDailyNotifyGoodDateBadDate(Context var1, boolean value)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            prefsEditor.putBoolean(ShowDailyNotifyGoodDateBadDate, value);
            prefsEditor.commit();
        }catch (Exception ex){
        }
    }

    static public boolean getShowNotifyChamNgon(Context var1)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            return appSharedPrefs.getBoolean(ShowNotifyChamNgon, true);
        }catch (Exception ex){
            return true;
        }
    }

    static public void setShowNotifyChamNgon(Context var1, boolean value)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            prefsEditor.putBoolean(ShowNotifyChamNgon, value);
            prefsEditor.commit();
        }catch (Exception ex){
        }
    }


    static public boolean getShowGoodDayBadDate(Context var1)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            return appSharedPrefs.getBoolean(ShowGoodDayBadDate, true);
        }catch (Exception ex){
            return true;
        }
    }

    static public void setShowGoodDayBadDate(Context var1, boolean value)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            prefsEditor.putBoolean(ShowGoodDayBadDate, value);
            prefsEditor.commit();
        }catch (Exception ex){
        }
    }

    static public boolean getShowChamNgon(Context var1)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            return appSharedPrefs.getBoolean(ShowChamNgon, true);
        }catch (Exception ex){
            return true;
        }
    }

    static public void setShowChamNgon(Context var1, boolean value)
    {
        try {
            SharedPreferences appSharedPrefs = var1.getSharedPreferences(
                    APP_SHARED_PREFS, 0);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            prefsEditor.putBoolean(ShowChamNgon, value);
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
