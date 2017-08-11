package com.hunght.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Set;

public class SavedValues {

	private static final String APP_SHARED_PREFS = "com.hth.numberlink";
	private static final String RECORD_UNLOCK_LEVELS = "RECORD_UNLOCK_LEVELS";
	private static final String RECORD_PLAYBACKGROUND = "RECORD_PLAYBACKGROUND";
	private static final String RECORD_HIGHSCORE = "RECORD_HIGHSCORE";
	private static final String RECORD_CURRENT_GAME_ID = "RECORD_CURRENT_GAME_ID";
    private static final String RECORD_NUMBER_OF_HINT = "RECORD_NUMBER_OF_HINT";
	private static final String RECORD_NEED_SHOW_LINES = "RECORD_NEED_SHOW_LINES";
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

	public String getCurrentGameId() {
		try {
			String item = appSharedPrefs.getString(RECORD_CURRENT_GAME_ID, "");
			return item;
		}catch (Exception ex){
			return null;
		}
	}
	public void setCurrentGameId(String currentGameId) {
		try {
			prefsEditor.putString(RECORD_CURRENT_GAME_ID, currentGameId);
			prefsEditor.commit();
		}catch (Exception ex){
		}
	}

	public Set<String> getUnlockLevels() {
		try {
			return appSharedPrefs.getStringSet(RECORD_UNLOCK_LEVELS, null);
		}catch (Exception ex){
			return null;
		}
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


	public boolean getNeedShowLines() {
		return appSharedPrefs.getBoolean(RECORD_NEED_SHOW_LINES, true);
	}

    public void setRecordNumberOfHints(int var1) {
        prefsEditor.putInt(RECORD_NUMBER_OF_HINT, var1);
        prefsEditor.commit();
    }

	public void setGameItem(GameItem item) {
		Gson gson = new Gson();
		String json = gson.toJson(item);
		prefsEditor.putString(item.getId(), json);
		prefsEditor.commit();
	}

	public void setNeedShowLines(boolean var1) {
		prefsEditor.putBoolean(RECORD_NEED_SHOW_LINES, var1);
		prefsEditor.commit();
	}

	public void setRecordPlaybackground(boolean var1) {
		prefsEditor.putBoolean(RECORD_PLAYBACKGROUND, var1);
		prefsEditor.commit();
	}

	public void setUnlockLevels(Set<String> var1) {
		prefsEditor.putStringSet(RECORD_UNLOCK_LEVELS, var1);
		prefsEditor.commit();
	}
}
