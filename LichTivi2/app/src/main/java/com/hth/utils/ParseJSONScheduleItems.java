package com.hth.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class ParseJSONScheduleItems {
	private static final String APP_SHARED_PREFS = "com.hth.lichtivi";
	static public final String SERVER_DEFAULT = "http://hunght.com";
	static public final String CONTROLLER_DEFAULT = "/api/lichtivi/GetSchedules/?channel=%s&date=%s&device=%s&open=%s&version=%s";
	static public final String RECODE_SERVER = "RECODE_SERVER";

	private Context context;

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


	 public ArrayList<ScheduleItem> getSchedules(String channel, Date date, String open)
    {
		String device ="111", version="222", dateInString;

		dateInString = MethodsHelper.getStringFromDate(date);
		String link = String.format(getServer() + CONTROLLER_DEFAULT, channel, dateInString, device, open, version);
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

			HttpClient httpclient = new DefaultHttpClient();

			// make GET request to the given URL
			HttpResponse httpResponse = httpclient.execute(new HttpGet(link));

			// receive response as inputStream
			InputStream inputStream = httpResponse.getEntity().getContent();

    		BufferedReader input = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

    		String inputLine;
            while ((inputLine = input.readLine()) != null)
            {
            	jsonStringBuilder.append(inputLine);
            }
            input.close();
			inputStream.close();
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

}
