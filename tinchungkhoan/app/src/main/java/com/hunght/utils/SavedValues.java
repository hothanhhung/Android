package com.hunght.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class SavedValues {

	private static final String APP_SHARED_PREFS = "com.hth.tracuuonline";
	private static final String RECORD_FIRST_RUN = "RECORD_FIRST_RUN";
	private static final String RECORD_FAVORITES = "RECORD_FAVORITES";
	private SharedPreferences appSharedPrefs;
	private Editor prefsEditor;

	public SavedValues(Context var1) {
		appSharedPrefs = var1.getSharedPreferences(
				APP_SHARED_PREFS, 0);
		prefsEditor = appSharedPrefs.edit();
	}

	public ArrayList<String> getFavorites() {
		Set<String> data = appSharedPrefs.getStringSet(RECORD_FAVORITES, null);
		ArrayList<String> returnData = new ArrayList<String>();
		if(data != null){
			returnData.addAll(data) ;
		}
		return returnData;
	}



	public void setFavorites(ArrayList<String> data) {
		Set<String> set = new HashSet<String>();
		set.addAll(data);
		prefsEditor.putStringSet(RECORD_FAVORITES, set);
		prefsEditor.commit();
	}

	public long getRecordFirstRun() {
		return appSharedPrefs.getLong(RECORD_FIRST_RUN, 0);
	}



	public void setRecordFirstRun(long var1) {
		prefsEditor.putLong(RECORD_FIRST_RUN, var1);
		prefsEditor.commit();
	}
}
