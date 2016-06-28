package com.hth.lines;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.hth.utils.MethodsHelper;

import java.util.ArrayList;

public class SavedValues {

	private static final String APP_SHARED_PREFS = "com.hth.lines";
	private static final String RECORD_SCORE = "RECORD_SCORE";
	private static final String RECORD_TIME = "RECORD_TIME";
	private static final String RECORD_PUZZLE = "RECORD_PUZZLE";
	private static final String RECORD_TRACKCHANGE = "RECORD_TRACKCHANGE";
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

	public int[][]  getRecordPuzzle() {
		try {
			Gson gson = new Gson();
			String json = appSharedPrefs.getString(RECORD_PUZZLE, "");
			int[][] items = gson.fromJson(json, int[][].class);
			return items;
		}catch (Exception ex){
			return null;
		}
	}

	public void setRecordScore(int var1) {
		prefsEditor.putInt(RECORD_SCORE, var1);
		prefsEditor.commit();
	}


	public void setRecordTime(long var1) {
		prefsEditor.putLong(RECORD_TIME, var1);
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


	public void setRecordPlaybackground(boolean var1) {
		prefsEditor.putBoolean(RECORD_PLAYBACKGROUND, var1);
		prefsEditor.commit();
	}

	public void setRecordPuzzle(int[][] matric) {
		Gson gson = new Gson();
		String json = gson.toJson(matric);
		prefsEditor.putString(RECORD_PUZZLE, json);
		prefsEditor.commit();
	}
}
