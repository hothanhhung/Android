package com.hth.adflex;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.os.StrictMode;
import android.util.Log;

public class ParseJSONAdFlex {

    static public ArrayList<AdFlexData> getAdFlexes(String country) 
    {
    	ArrayList<AdFlexData> adFlexList = new ArrayList<AdFlexData>();
    	Gson gSon = new Gson();
    	try {
    		if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
						.permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}
    		
    		StringBuilder jsonStringBuilder = new StringBuilder();
    		String link = "http://api.adflex.vn/api/?action=campaign&refcode=thanhhung1012&country="+country+"&os=android";
    		BufferedReader input = new BufferedReader(new InputStreamReader(new URL(link).openStream(), "UTF-8"));
    		
    		String inputLine;
            while ((inputLine = input.readLine()) != null) 
            {
            	jsonStringBuilder.append(inputLine);
            }
            input.close();
	    	String json = jsonStringBuilder.toString();
	    	
	    	Type collectionType = new TypeToken<ArrayList<AdFlexData>>(){}.getType();
	    	adFlexList = gSon.fromJson(json, collectionType);
    	}catch(Exception ex)
    	{
    		Log.w("getAdFlexes", ex.getMessage());
    		ex.printStackTrace();
    	}
    	return adFlexList;
    }

}
