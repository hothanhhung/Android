package com.hth.photopuzzle;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SavedValues {

	private static final String APP_SHARED_PREFS = "com.hth.photopuzzle";
	private SharedPreferences appSharedPrefs;
	private Editor prefsEditor;

	public SavedValues(Context var1) {
		appSharedPrefs = var1.getSharedPreferences(
				"com.hth.photopuzzle", 0);
		prefsEditor = appSharedPrefs.edit();
	}

	public String getRecord() {
		return appSharedPrefs.getString("record", "0");
	}

	public String getRecordTime() {
		return appSharedPrefs.getString("recordtime", "0");
	}

	public void setRecord(String var1) {
		prefsEditor.putString("record", var1);
		prefsEditor.commit();
	}

	public void setRecordTime(String var1) {
		prefsEditor.putString("recordtime", var1);
		prefsEditor.commit();
	}
}
