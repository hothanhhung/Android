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
        setItem(var1, NotesKey, obj.toArray());
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

    static private <T> void setItem(Context var1, String key, T[] obj) {
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
