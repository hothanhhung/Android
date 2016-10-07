package com.hth.AnimalMemory;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;

import java.util.ArrayList;

public class SavedValues {

	private static final String APP_SHARED_PREFS = "com.hth.AnimalMemory";
	private static final String RECORD_SCORE = "RECORD_SCORE";
	private static final String RECORD_TIME = "RECORD_TIME";
	private static final String RECORD_PLAYBACKGROUND = "RECORD_PLAYBACKGROUND";
	private SharedPreferences appSharedPrefs;
	private Editor prefsEditor;

	public SavedValues(Context var1) {
		appSharedPrefs = var1.getSharedPreferences(
				APP_SHARED_PREFS, 0);
		prefsEditor = appSharedPrefs.edit();
	}

	public int getRecordScore() {
		return appSharedPrefs.getInt(RECORD_SCORE, 0);
	}

	public long getRecordTime() {
		return appSharedPrefs.getLong(RECORD_TIME, 0l);
	}


	public boolean getRecordPlaybackground() {
		return appSharedPrefs.getBoolean(RECORD_PLAYBACKGROUND, true);
	}


	public void setRecordTime(long var1) {
		prefsEditor.putLong(RECORD_TIME, var1);
		prefsEditor.commit();
	}

	public void setRecordPlaybackground(boolean var1) {
		prefsEditor.putBoolean(RECORD_PLAYBACKGROUND, var1);
		prefsEditor.commit();
	}

}
