package com.hunght.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hunght.data.DanhMucDauTuItem;
import com.hunght.data.DoanhNghiepItem;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class SavedValues {

	private static final String APP_SHARED_PREFS = "com.hth.tracuuonline";
	private static final String RECORD_FIRST_RUN = "RECORD_FIRST_RUN";
	private static final String RECORD_FAVORITES = "RECORD_FAVORITES";
	private static final String RECORD_THONG_TIN_DOANH_NGHIEP = "RECORD_THONG_TIN_DOANH_NGHIEP";
	private static final String RECORD_DANH_MUC_DAU_TU = "RECORD_DANH_MUC_DAU_TU";
	private static final String RECORD_INAPP_BROWSER = "RECORD_INAPP_BROWSER";
	private static final String RECORD_PASSWORD_MUC_DAU_TU = "RECORD_PASSWORD_MUC_DAU_TU";
	private static final String RECORD_PASSWORD_APP = "RECORD_PASSWORD_APP";
	private static final String RECORD_PASSWORD = "RECORD_PASSWORD";
	private static final String RECORD_SHOW_HEADER = "RECORD_SHOW_HEADER";
	private static final String RECORD_HIDE_CHI_MUC_DAU_TU = "RECORD_HIDE_CHI_MUC_DAU_TU";
	private static final String RECORD_HIDE_CHUNG_KHOAN_DAU_TU = "RECORD_HIDE_CHUNG_KHOAN_DAU_TU";
	private static final String RECORD_LAST_UPDATE_PRICE_TIME = "RECORD_LAST_UPDATE_PRICE_TIME";
	private static final String RECORD_CAC_MA_CK_CHUNG_KHOAN_DAU_TU = "RECORD_CAC_MA_CK_CHUNG_KHOAN_DAU_TU";

	private SharedPreferences appSharedPrefs;
	private Editor prefsEditor;

	public SavedValues(Context var1) {
		appSharedPrefs = var1.getSharedPreferences(
				APP_SHARED_PREFS, 0);
		prefsEditor = appSharedPrefs.edit();
	}

	public void setRecordPassword(String var1) {
		prefsEditor.putString(RECORD_PASSWORD, var1);
		prefsEditor.commit();
	}

	public String getRecordPassword( ) {
		return appSharedPrefs.getString(RECORD_PASSWORD, "");
	}
	public void setLastUpdatePriceTime(String var1) {
		prefsEditor.putString(RECORD_LAST_UPDATE_PRICE_TIME, var1);
		prefsEditor.commit();
	}

	public String getLastUpdatePriceTime( ) {
		return appSharedPrefs.getString(RECORD_LAST_UPDATE_PRICE_TIME, "");
	}


	public void setRecordHideChiMucDauTu(boolean var1) {
		prefsEditor.putBoolean(RECORD_HIDE_CHI_MUC_DAU_TU, var1);
		prefsEditor.commit();
	}

	public boolean getRecordHideChiMucDauTu( ) {
		return appSharedPrefs.getBoolean(RECORD_HIDE_CHI_MUC_DAU_TU, false);
	}
	public void setRecordHideChungKhoanDauTu(boolean var1) {
		prefsEditor.putBoolean(RECORD_HIDE_CHUNG_KHOAN_DAU_TU, var1);
		prefsEditor.commit();
	}

	public boolean getRecordHideChungKhoanDauTu( ) {
		return appSharedPrefs.getBoolean(RECORD_HIDE_CHUNG_KHOAN_DAU_TU, false);
	}


	public void setRecordCacMaChungKhoanDauTu(String var1) {
		prefsEditor.putString(RECORD_CAC_MA_CK_CHUNG_KHOAN_DAU_TU, var1);
		prefsEditor.commit();
	}

	public String getRecordCacMaChungKhoanDauTu( ) {
		return appSharedPrefs.getString(RECORD_CAC_MA_CK_CHUNG_KHOAN_DAU_TU, "");
	}

	public void setRecordShowHeader(boolean var1) {
		prefsEditor.putBoolean(RECORD_SHOW_HEADER, var1);
		prefsEditor.commit();
	}

	public boolean getRecordShowHeader( ) {
		return appSharedPrefs.getBoolean(RECORD_SHOW_HEADER, true);
	}

	public void setRecordPasswordInApp(boolean var1) {
		prefsEditor.putBoolean(RECORD_PASSWORD_APP, var1);
		prefsEditor.commit();
	}

	public boolean getRecordPasswordInApp( ) {
		return appSharedPrefs.getBoolean(RECORD_PASSWORD_APP, false);
	}

	public void setRecordPasswordMucDauTu(boolean var1) {
		prefsEditor.putBoolean(RECORD_PASSWORD_MUC_DAU_TU, var1);
		prefsEditor.commit();
	}

	public boolean getRecordPasswordMucDauTu( ) {
		return appSharedPrefs.getBoolean(RECORD_PASSWORD_MUC_DAU_TU, false);
	}

	public void setRecordInappBrowser(boolean var1) {
		prefsEditor.putBoolean(RECORD_INAPP_BROWSER, var1);
		prefsEditor.commit();
	}

	public boolean getRecordInappBrowser( ) {
		return appSharedPrefs.getBoolean(RECORD_INAPP_BROWSER, false);
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

	public ArrayList<DoanhNghiepItem> getThongTinDoanhNghieps()
	{
		ArrayList<DoanhNghiepItem> list = new ArrayList<>();
		{
			try {
				String favoritesStr = appSharedPrefs.getString(RECORD_THONG_TIN_DOANH_NGHIEP, "");
				if(!favoritesStr.isEmpty())
				{
					Gson gSon = new Gson();
					Type collectionType = new TypeToken<ArrayList<DoanhNghiepItem>>(){}.getType();
					list = gSon.fromJson(favoritesStr, collectionType);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return list;
	}


	public void setThongTinDoanhNghieps(ArrayList<DoanhNghiepItem> lstObject)
	{
		try {

			Gson gSon = new Gson();
			String dataInString = gSon.toJson(lstObject);
			prefsEditor.putString(RECORD_THONG_TIN_DOANH_NGHIEP, dataInString);
			prefsEditor.commit();

		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	public ArrayList<DanhMucDauTuItem> getDanhMucDauTus()
	{
		ArrayList<DanhMucDauTuItem> list = new ArrayList<>();
		{
			try {
				String favoritesStr = appSharedPrefs.getString(RECORD_DANH_MUC_DAU_TU, "");
				if(!favoritesStr.isEmpty())
				{
					Gson gSon = new Gson();
					Type collectionType = new TypeToken<ArrayList<DanhMucDauTuItem>>(){}.getType();
					list = gSon.fromJson(favoritesStr, collectionType);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return list;
	}


	public void setDanhMucDauTus(ArrayList<DanhMucDauTuItem> lstObject)
	{
		try {
			Gson gSon = new Gson();
			String dataInString = gSon.toJson(lstObject);
			prefsEditor.putString(RECORD_DANH_MUC_DAU_TU, dataInString);
			prefsEditor.commit();

		} catch (Exception e) {

			e.printStackTrace();

		}
	}
}
