package com.hth.utils;

import java.io.InputStream;

import android.content.res.AssetManager;
import android.util.Log;

public class FileProcess {

	public static String readTextFileFromAssets(String fileName, AssetManager assetManager)
	{
		Log.w("readTextFileFromAssets", fileName);
		String content = "";
        try {
            InputStream input;
            input = assetManager.open(fileName);
 
             int size = input.available();
             byte[] buffer = new byte[size];
             input.read(buffer);
             input.close();
 
             // byte buffer into a string
             content = new String(buffer);
 
        } catch (Exception e) {
            // TODO Auto-generated catch block
    		Log.w("readTextFileFromAssets", e.getMessage());
            e.printStackTrace();
        }
        return content;
	}
}
