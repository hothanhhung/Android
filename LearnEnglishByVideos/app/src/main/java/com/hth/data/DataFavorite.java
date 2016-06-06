package com.hth.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import android.content.Context;

public class DataFavorite {
	
	final private static String FILE_NAME = "EnglishRecord";

	static public boolean fileExists(Context context, String filename) {
		File file = context.getFileStreamPath(filename);
		if(file == null || !file.exists()) {
			return false;
		}
		return true;
	}

	static public ArrayList<ObjectFavorite> getFavorite(Context ctx)
	{
		ArrayList<ObjectFavorite> listFavorite = new ArrayList<ObjectFavorite>();
		if(fileExists(ctx, FILE_NAME)) {
			try {
				FileInputStream fis = ctx.openFileInput(FILE_NAME);
				BufferedReader inputReader = new BufferedReader(new InputStreamReader(fis));

				String inputString;

				while ((inputString = inputReader.readLine()) != null) {
					if (inputString.trim().isEmpty()) continue;

					String[] data = inputString.split("\\|");
					ObjectFavorite objectFavorite = new ObjectFavorite();

					if (data.length > 0) objectFavorite.setChannelId(data[0]);
					if (data.length > 1) objectFavorite.setId(data[1]);
					if (data.length > 2) {
						objectFavorite.setTitle(data[2]);
						//objectFavorite.setTitle(data[1]);
					}
					if (data.length > 3) objectFavorite.setPath(data[3]);
					if (data.length > 4) objectFavorite.setTime(data[4]);

					listFavorite.add(objectFavorite);
				}
				inputReader.close();
				fis.close();

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return listFavorite;
	}

	static public void setFavorite(Context ctx, ObjectFavorite object)
	{
		if(object.getChannelId().isEmpty()) return;
		
		try {
			String titleEncode =  object.getTitle().replace("|", "/");
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
		    	
				String titleEncode =  object.getTitle().replace("|", "/");
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
