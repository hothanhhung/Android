package com.hth.sudoku;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.hth.utils.MethodsHelper;

import java.util.ArrayList;

public class SavedValues {

	private static final String APP_SHARED_PREFS = "com.hth.sudoku";
	private static final String RECORD_CHANGES = "RECORD_CHANGES";
	private static final String RECORD_TIME = "RECORD_TIME";
	private static final String RECORD_PUZZLE = "RECORD_PUZZLE";
	private static final String RECORD_TRACKCHANGE = "RECORD_TRACKCHANGE";
	private static final String RECORD_LEVEL = "RECORD_LEVEL";
	private static final String RECORD_PLAYBACKGROUND = "RECORD_PLAYBACKGROUND";
	private static final String RECORD_STARTAT = "RECORD_STARTAT";
	private static final String RECORD_NUMBER_OF_HINT = "RECORD_NUMBER_OF_HINT";
	private SharedPreferences appSharedPrefs;
	private Editor prefsEditor;

	public SavedValues(Context var1) {
		appSharedPrefs = var1.getSharedPreferences(
				APP_SHARED_PREFS, 0);
		prefsEditor = appSharedPrefs.edit();
	}

	public int getRecordNumberOfHints() {
		return appSharedPrefs.getInt(RECORD_NUMBER_OF_HINT, 10);
	}
	public int getRecordChanges() {
		return appSharedPrefs.getInt(RECORD_CHANGES, 0);
	}

	public long getRecordTime() {
		return appSharedPrefs.getLong(RECORD_TIME, 0l);
	}
	public Item[] getRecordPuzzle() {
		try {
			Gson gson = new Gson();
			String json = appSharedPrefs.getString(RECORD_PUZZLE, "");
			Item[] items = gson.fromJson(json, Item[].class);
			return items;
		}catch (Exception ex){
			return null;
		}
	}
	public int getRecordLevel() {
		return appSharedPrefs.getInt(RECORD_LEVEL, 1);
	}
	public TrackChange[] getRecordTrackChange() {
		try {
			Gson gson = new Gson();
			String json = appSharedPrefs.getString(RECORD_TRACKCHANGE, "");
			TrackChange[] items = gson.fromJson(json, TrackChange[].class);
			return items;
		}catch (Exception ex){
			return null;
		}
	}
	public boolean getRecordPlaybackground() {
		return appSharedPrefs.getBoolean(RECORD_PLAYBACKGROUND, true);
	}

	public String getRecordStartAt() {
		return appSharedPrefs.getString(RECORD_STARTAT, MethodsHelper.getCurrentDate());
	}

	public void setRecordNumberOfHints(int var1) {
		prefsEditor.putInt(RECORD_NUMBER_OF_HINT, var1);
		prefsEditor.commit();
	}

	public void setRecordChanges(int var1) {
		prefsEditor.putInt(RECORD_CHANGES, var1);
		prefsEditor.commit();
	}


	public void setRecordTime(long var1) {
		prefsEditor.putLong(RECORD_TIME, var1);
		prefsEditor.commit();
	}

	public void setRecordPuzzle(Item[] items) {
		Gson gson = new Gson();
		String json = gson.toJson(items);
		prefsEditor.putString(RECORD_PUZZLE, json);
		prefsEditor.commit();
	}
	public void setRecordTrackchange(ArrayList<TrackChange> trackChanges) {
		Gson gson = new Gson();
		TrackChange[] items = new TrackChange[trackChanges.size()];
		for (int i=0; i<items.length; i++){
			items[i] = trackChanges.get(i);
		}
		String json = gson.toJson(items);
		prefsEditor.putString(RECORD_TRACKCHANGE, json);
		prefsEditor.commit();
	}

	public void setRecordLevel(int var1) {
		prefsEditor.putInt(RECORD_LEVEL, var1);
		prefsEditor.commit();
	}

	public void setRecordPlaybackground(boolean var1) {
		prefsEditor.putBoolean(RECORD_PLAYBACKGROUND, var1);
		prefsEditor.commit();
	}
	public void setRecordStartAt(String var1) {
		prefsEditor.putString(RECORD_STARTAT, var1);
		prefsEditor.commit();
	}
}
