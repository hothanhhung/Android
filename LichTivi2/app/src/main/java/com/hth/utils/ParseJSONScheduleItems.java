package com.hth.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.StrictMode;
import android.util.Log;

public class ParseJSONScheduleItems {
	private static final String APP_SHARED_PREFS = "com.hth.lichtivi";
	static public final String SERVER_DEFAULT = "http://hunght.com";
	static public final String CONTROLLER_DEFAULT = "/api/lichtivi/GetSchedules/?channel=%s&date=%s&device=%s&open=%s&version=%s";
	static public final String CONTROLLER_SEARCH_DEFAULT = "/api/lichtivi/SearchProgram/?query=%s&group=%s&date=%s&device=%s&open=%s&version=%s";
	static public final String RECODE_SERVER = "RECODE_SERVER";

	private Context context;
	static private String openKey;
	static private String androidId;
	static private String appVersion;


	private String getOpenKey(){
		if(openKey == null || openKey.isEmpty())
		{
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			Date selectedDate = new Date(year, month, day);
			openKey = "" + selectedDate.getTime();
		}
		return openKey;
	}

	private String getAndroidId(){
		if(androidId == null || androidId.isEmpty())
		{
			androidId = MethodsHelper.getAndroidId(context);
		}
		return androidId;
	}

	private String getAppVersion(){
		if(appVersion == null || appVersion.isEmpty())
		{
			appVersion = MethodsHelper.getAppVersion(context);
		}
		return appVersion;
	}

	public ParseJSONScheduleItems(Context context){
		this.context = context;
	}

	private void setServer(String newDomain) {
		SharedPreferences appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, 0);
		SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
		prefsEditor.putString(RECODE_SERVER, newDomain);
		prefsEditor.commit();
	}

	private String getServer()
	{
		SharedPreferences appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, 0);
		return appSharedPrefs.getString(RECODE_SERVER, SERVER_DEFAULT);
	}


	 public ArrayList<ScheduleItem> getSchedules(String channel, Date date)
    {
		String device = getAndroidId(), version=getAppVersion(), dateInString;

		dateInString = MethodsHelper.getStringFromDate(date);
		String link = String.format(getServer() + CONTROLLER_DEFAULT, channel, dateInString, device, getOpenKey(), version);
		Log.d("getSchedules",link);
		ResponseJson<ScheduleItem> responseJson = new ResponseJson<ScheduleItem>();
    	Gson gSon = new Gson();
    	try {
    		if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
						.permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}
    		
    		StringBuilder jsonStringBuilder = new StringBuilder();

			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
						.permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}

			BufferedReader input = new BufferedReader(new InputStreamReader(new URL(link).openStream(), "UTF-8"));

    		String inputLine;
            while ((inputLine = input.readLine()) != null)
            {
            	jsonStringBuilder.append(inputLine);
            }
            input.close();
	    	String json = jsonStringBuilder.toString();
	    	
	    	Type collectionType = new TypeToken<ResponseJson<ScheduleItem>>(){}.getType();
			responseJson = gSon.fromJson(json, collectionType);
			if(responseJson.NeedChangeDomain){
				setServer(responseJson.NewDomain);
			}
    	}catch(Exception ex)
    	{
    		ex.printStackTrace();
			return null;
    	}
    	return responseJson.Data;
    }

	public ArrayList<SearchProgramItem> searchProgram(String query, String group, Date date)
	{
		String device = getAndroidId(), version=getAppVersion(), dateInString;

		dateInString = MethodsHelper.getStringFromDate(date);
		String link = String.format(getServer() + CONTROLLER_SEARCH_DEFAULT, Uri.encode(query), group, dateInString, device, getOpenKey(), version);
		Log.d("searchProgram",link);
		ResponseJson<SearchProgramItem> responseJson = new ResponseJson<SearchProgramItem>();
		Gson gSon = new Gson();
		try {
			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
						.permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}

			StringBuilder jsonStringBuilder = new StringBuilder();

			BufferedReader input = new BufferedReader(new InputStreamReader(new URL(link).openStream(), "UTF-8"));

			String inputLine;
			while ((inputLine = input.readLine()) != null)
			{
				jsonStringBuilder.append(inputLine);
			}
			input.close();


			String json = jsonStringBuilder.toString();

			Type collectionType = new TypeToken<ResponseJson<SearchProgramItem>>(){}.getType();
			responseJson = gSon.fromJson(json, collectionType);
			if(responseJson.NeedChangeDomain){
				setServer(responseJson.NewDomain);
			}
		}catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
		return responseJson.Data;
	}

}
