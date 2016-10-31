package com.hth.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;

import java.util.ArrayList;

public class SavedValues {

	private static final String APP_SHARED_PREFS = "com.hth.datmon";
	private static final String RECORD_USER_NAME = "RECORD_USER_NAME";
	private static final String RECORD_PASSWORD = "RECORD_PASSWORD";
	private static final String RECORD_REMEMBER = "RECORD_REMEMBER";
	private SharedPreferences appSharedPrefs;
	private Editor prefsEditor;

	public SavedValues(Context var1) {
		appSharedPrefs = var1.getSharedPreferences(
				APP_SHARED_PREFS, 0);
		prefsEditor = appSharedPrefs.edit();
	}

	public String getRecordUserName() {
		return appSharedPrefs.getString(RECORD_USER_NAME, "");
	}

	public String getRecordPassword() {
		return appSharedPrefs.getString(RECORD_PASSWORD, "");
	}

	public boolean getRecordRemember() {
		return appSharedPrefs.getBoolean(RECORD_REMEMBER, false);
	}

	public void setRecordUserName(String var1) {
		prefsEditor.putString(RECORD_USER_NAME, var1);
		prefsEditor.commit();
	}

	public void setRecordPassword(String var1) {
		prefsEditor.putString(RECORD_PASSWORD, var1);
		prefsEditor.commit();
	}

	public void setRecordRemember(boolean var1) {
		prefsEditor.putBoolean(RECORD_REMEMBER, var1);
		prefsEditor.commit();
	}
}
