package com.hth.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hth.lichtivi.ChannelItem;
import com.hth.utils.ResponseJson;
import com.hth.utils.ScheduleItem;

public class DataFavorite {
	
	final private static String FAVORITES_KEY_NAME = "LichTiviFavoritesRecord";

	static public ArrayList<ChannelItem> getFavorite(Activity ctx)
	{
		ArrayList<ChannelItem> listFavorite = new ArrayList<ChannelItem>();
		{
			try {
				SharedPreferences sharedPref =ctx.getPreferences(Context.MODE_PRIVATE);
				String favoritesStr = sharedPref.getString(FAVORITES_KEY_NAME, "");
				if(!favoritesStr.isEmpty())
				{
					Gson gSon = new Gson();
					Type collectionType = new TypeToken<ArrayList<ChannelItem>>(){}.getType();
					listFavorite = gSon.fromJson(favoritesStr, collectionType);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return listFavorite;
	}

	
	static public void setFavorites(Activity ctx, ArrayList<ChannelItem> lstObject)
	{
		try {
			
			Gson gSon = new Gson();
			String dataInString = gSon.toJson(lstObject);
			SharedPreferences sharedPref = ctx.getPreferences(Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putString(FAVORITES_KEY_NAME, dataInString);
			editor.commit();

		} catch (Exception e) {

		    e.printStackTrace();

		}
	}
}
