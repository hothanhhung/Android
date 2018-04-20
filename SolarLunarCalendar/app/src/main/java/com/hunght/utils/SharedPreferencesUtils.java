package com.hunght.utils;

import android.content.Context;
import android.content.SharedPreferences;

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

    public ArrayList<NoteItem> getNoteItems(Context var1)
    {
        NoteItem[] items =  getItem(var1, NotesKey, NoteItem[].class);
        return new ArrayList<>(Arrays.asList(items));
    }

    public void saveNoteItems(Context var1, ArrayList<NoteItem> obj)
    {
        setItem(var1, NotesKey, obj.toArray());
    }

    private <T> T getItem(Context var1, String key, Class<T> tClass) {
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

    private <T> void setItem(Context var1, String key, T[] obj) {
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
