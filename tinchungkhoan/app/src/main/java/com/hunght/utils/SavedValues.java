package com.hunght.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class SavedValues {

	private static final String APP_SHARED_PREFS = "com.hth.tracuuonline";
	private static final String RECORD_FIRST_RUN = "RECORD_FIRST_RUN";
	private SharedPreferences appSharedPrefs;
	private Editor prefsEditor;

	public SavedValues(Context var1) {
		appSharedPrefs = var1.getSharedPreferences(
				APP_SHARED_PREFS, 0);
		prefsEditor = appSharedPrefs.edit();
	}

	public long getRecordFirstRun() {
		return appSharedPrefs.getLong(RECORD_FIRST_RUN, 0);
	}



	public void setRecordFirstRun(long var1) {
		prefsEditor.putLong(RECORD_FIRST_RUN, var1);
		prefsEditor.commit();
	}
}
