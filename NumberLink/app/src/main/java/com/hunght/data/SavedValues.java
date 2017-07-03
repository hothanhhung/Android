package com.hunght.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;

import java.util.ArrayList;

public class SavedValues {

	private static final String APP_SHARED_PREFS = "com.hth.numberlink";
	private static final String RECORD_PLAYBACKGROUND = "RECORD_PLAYBACKGROUND";
	private static final String RECORD_HIGHSCORE = "RECORD_HIGHSCORE";
	private SharedPreferences appSharedPrefs;
	private Editor prefsEditor;

	public SavedValues(Context var1) {
		appSharedPrefs = var1.getSharedPreferences(
				APP_SHARED_PREFS, 0);
		prefsEditor = appSharedPrefs.edit();
	}


	public GameItem getGameItem(String key) {
		try {
			Gson gson = new Gson();
			String json = appSharedPrefs.getString(key, "");
			GameItem item = gson.fromJson(json, GameItem.class);
			return item;
		}catch (Exception ex){
			return null;
		}
	}

	public boolean getRecordPlaybackground() {
		return appSharedPrefs.getBoolean(RECORD_PLAYBACKGROUND, true);
	}


	public void setGameItem(GameItem item) {
		Gson gson = new Gson();
		String json = gson.toJson(item);
		prefsEditor.putString(item.getId(), json);
		prefsEditor.commit();
	}


	public void setRecordPlaybackground(boolean var1) {
		prefsEditor.putBoolean(RECORD_PLAYBACKGROUND, var1);
		prefsEditor.commit();
	}

}
