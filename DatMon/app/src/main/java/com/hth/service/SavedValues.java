package com.hth.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;

import java.util.ArrayList;

public class SavedValues {

	private static final String APP_SHARED_PREFS = "com.hth.AnimalMemory";
	private static final String RECORD_CARD = "RECORD_CARD";
	private static final String RECORD_LEVEL = "RECORD_LEVEL";
	private static final String RECORD_TIME = "RECORD_TIME";
	private static final String RECORD_NUMBER_OF_TRIES = "RECORD_NUMBER_OF_TRIES";
	private static final String RECORD_NUMBER_OF_HINTS = "RECORD_NUMBER_OF_HINTS";
	private static final String RECORD_PLAYBACKGROUND = "RECORD_PLAYBACKGROUND";
	private SharedPreferences appSharedPrefs;
	private Editor prefsEditor;

	public SavedValues(Context var1) {
		appSharedPrefs = var1.getSharedPreferences(
				APP_SHARED_PREFS, 0);
		prefsEditor = appSharedPrefs.edit();
	}

	public int getRecordNumberOfHints() {
		return appSharedPrefs.getInt(RECORD_NUMBER_OF_HINTS, 0);
	}

	public int getRecordNumberOfTries() {
		return appSharedPrefs.getInt(RECORD_NUMBER_OF_TRIES, 0);
	}

	public int getRecordLevel() {
		return appSharedPrefs.getInt(RECORD_LEVEL,0);
	}

	public int[][]  getRecordCard() {
		try {
			Gson gson = new Gson();
			String json = appSharedPrefs.getString(RECORD_CARD, "");
			int[][] items = gson.fromJson(json, int[][].class);
			return items;
		}catch (Exception ex){
			return null;
		}
	}

	public long getRecordTime() {
		return appSharedPrefs.getLong(RECORD_TIME, 0l);
	}


	public boolean getRecordPlaybackground() {
		return appSharedPrefs.getBoolean(RECORD_PLAYBACKGROUND, true);
	}

	public void setRecordLevel(int var1) {
		prefsEditor.putInt(RECORD_LEVEL, var1);
		prefsEditor.commit();
	}

	public void setRecordTime(long var1) {
		prefsEditor.putLong(RECORD_TIME, var1);
		prefsEditor.commit();
	}

	public void setRecordNumberOfHints(int var1) {
		prefsEditor.putInt(RECORD_NUMBER_OF_HINTS, var1);
		prefsEditor.commit();
	}

	public void setRecordNumberOfTries(int var1) {
		prefsEditor.putInt(RECORD_NUMBER_OF_TRIES, var1);
		prefsEditor.commit();
	}

	public void setRecordPlaybackground(boolean var1) {
		prefsEditor.putBoolean(RECORD_PLAYBACKGROUND, var1);
		prefsEditor.commit();
	}

	public void setRecordCard(int[][] matric) {
		Gson gson = new Gson();
		String json = gson.toJson(matric);
		prefsEditor.putString(RECORD_CARD, json);
		prefsEditor.commit();
	}
}
