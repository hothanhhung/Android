package com.hth.sudoku;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SavedValues {

	private static final String APP_SHARED_PREFS = "com.hth.sudoku";
	private static final String RECORD_CHANGES = "RECORD_CHANGES";
	private static final String RECORD_TIME = "RECORD_TIME";
	private static final String RECORD_PUZZLE = "RECORD_PUZZLE";
	private SharedPreferences appSharedPrefs;
	private Editor prefsEditor;

	public SavedValues(Context var1) {
		appSharedPrefs = var1.getSharedPreferences(
				APP_SHARED_PREFS, 0);
		prefsEditor = appSharedPrefs.edit();
	}

	public String getRecordChanges() {
		return appSharedPrefs.getString(RECORD_CHANGES, "0");
	}

	public String getRecordTime() {
		return appSharedPrefs.getString(RECORD_TIME, "0");
	}
	public String getRecordPuzzle() {
		return appSharedPrefs.getString(RECORD_PUZZLE, "");
	}

	public void setRecord(String var1) {
		prefsEditor.putString(RECORD_CHANGES, var1);
		prefsEditor.commit();
	}

	public void setRecordTime(String var1) {
		prefsEditor.putString(RECORD_TIME, var1);
		prefsEditor.commit();
	}

	public void setRecordPuzzle(String var1) {
		prefsEditor.putString(RECORD_PUZZLE, var1);
		prefsEditor.commit();
	}
}
