package com.hth.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.hth.data.FolderInfo;
import com.hth.webserver.AndroidWebServer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Lenovo on 12/4/2015.
 */
public class DataSevices {
    public static final String KEY_LAST_PORT = "KEY_LAST_PORT";
    private static final int DEFAULT_PORT = 12345;

    public static void saveLastPort(Activity activity, int value)
    {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(KEY_LAST_PORT, value);
        editor.commit();
    }
    public static int getLastPort(Activity activity)
    {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        int lastPort = sharedPref.getInt(KEY_LAST_PORT, DEFAULT_PORT);
        return lastPort;
    }

    public static ArrayList<FolderInfo> getDirInfo(String dirPath)
    {
        ArrayList<FolderInfo> folderInfos = new ArrayList<FolderInfo>();
        ArrayList<FolderInfo> fileInfos = new ArrayList<FolderInfo>();
        try {
            File f = new File(dirPath);
            File[] files = f.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    FolderInfo folderInfo = new FolderInfo();
                    folderInfo.IsCommonPreview = true;
                    folderInfo.Name = file.getName();
                    folderInfo.FullPath = file.getPath();
                    folderInfo.IsFile = file.isFile();
                    folderInfo.LastModified = new Date(file.lastModified());
                    if(folderInfo.IsFile){
                        folderInfo.Size = file.length();
                        folderInfo.DisplaySize = FolderInfo.GetDisplaySize(file.length());
                        folderInfo.DisplayLastModified = FolderInfo.GetDisplaySize(folderInfo.LastModified);
                        String mimeType = AndroidWebServer.getMimeTypeForFile(folderInfo.FullPath);
                        if(mimeType!=null && mimeType.indexOf("image") == 0) {
                            folderInfo.Preview = "/?api=preview&path="+folderInfo.FullPath;
                            folderInfo.IsCommonPreview = false;
                        }else{
                            folderInfo.Preview = "images/file.png";
                        }
                        fileInfos.add(folderInfo);
                    }
                    else{
                        folderInfo.Preview = "images/folder.png";
                        folderInfos.add(folderInfo);
                    }

                }
            }
            Collections.sort(fileInfos, new Comparator<FolderInfo>() {
                public int compare(FolderInfo fileInfo1, FolderInfo fileInfo2) {
                    return fileInfo1.Name.compareToIgnoreCase(fileInfo2.Name);
                }
            });
            Collections.sort(folderInfos, new Comparator<FolderInfo>() {
                public int compare(FolderInfo fileInfo1, FolderInfo fileInfo2) {
                    return fileInfo1.Name.compareToIgnoreCase(fileInfo2.Name);
                }
            });
            folderInfos.addAll(fileInfos);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return folderInfos;

    }

    public static boolean isExist(String dirPath)
    {
        try {
            File f = new File(dirPath);
            if(f.exists()) return true;
            else return false;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean hasAllowWithKey(Activity activity, int key)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        boolean result = sharedPref.getBoolean(activity.getResources().getString(key), true);
        return result;
    }

    public static String getStringValueOfKey(Activity activity, int key)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        String result = sharedPref.getString(activity.getResources().getString(key), "");
        return result;
    }
}
