package com.hth.data;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import android.content.Context;

public class DataFavorite {
	
	final private static String FILE_NAME = "DayTwentyTwoFile123"; 
	
	static public ArrayList<ObjectFavorite> getFavorite(Context ctx)
	{
		ArrayList<ObjectFavorite> listFavorite = new ArrayList<ObjectFavorite>();
		try{
			FileInputStream fis = ctx.openFileInput(FILE_NAME);
		    BufferedReader inputReader = new BufferedReader(new InputStreamReader(fis));
	
		    String inputString;
	
		    while ((inputString = inputReader.readLine()) != null) {
		    	if(inputString.trim().isEmpty()) continue;

		    	ObjectFavorite objectFavorite = new ObjectFavorite();
		    	String [] data = inputString.split("\\|");
				
		    	
		    	if(data.length > 0) objectFavorite.setChannelId(data[0]);
		    	if(data.length > 1) objectFavorite.setId(data[1]);
		    	if(data.length > 2){
		    		byte[] titleData = android.util.Base64.decode(data[2], android.util.Base64.NO_WRAP);
		    		objectFavorite.setTitle(new String(titleData, "UTF-8"));
		    		//objectFavorite.setTitle(data[1]);
		    	}
		    	if(data.length > 3) objectFavorite.setPath(data[3]);
		    	if(data.length > 4) objectFavorite.setTime(data[4]);
		    	
		    	listFavorite.add(objectFavorite);
		    }
		    inputReader.close();
		    fis.close();
		    
		}catch(Exception ex){ex.printStackTrace();}
	    
		return listFavorite;
	}

	static public void setFavorite(Context ctx, ObjectFavorite object)
	{
		if(object.getChannelId().isEmpty()) return;
		try {
			String titleEncode =  android.util.Base64.encodeToString(object.getTitle().getBytes("UTF-8"),android.util.Base64.NO_WRAP);
			String data = ""+object.getChannelId()+"|"+""+object.getId()+"|"+titleEncode+"|"+object.getPath()+"|"+object.getTime()+"\n";
		    FileOutputStream fos = ctx.openFileOutput(FILE_NAME, Context.MODE_APPEND);
		    
		    fos.write(data.getBytes());

		    fos.close();

		} catch (Exception e) {

		    e.printStackTrace();

		}
	}
	
	static public void setFavorites(Context ctx, ArrayList<ObjectFavorite> lstObject)
	{
		try {
			
		    FileOutputStream fos = ctx.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
		    for(ObjectFavorite object: lstObject)
		    {
				if(object.getChannelId().isEmpty()) continue;
		    	String titleEncode = android.util.Base64.encodeToString(object.getTitle().replace("|", "/").getBytes("UTF-8"),android.util.Base64.NO_WRAP);

				String data = ""+object.getChannelId()+"|"+""+object.getId()+"|"+titleEncode+"|"+object.getPath()+"|"+object.getTime()+"\n";
			    
			    fos.write(data.getBytes());
		    }
		    fos.flush();
		    fos.close();

		} catch (Exception e) {

		    e.printStackTrace();

		}
	}
}
