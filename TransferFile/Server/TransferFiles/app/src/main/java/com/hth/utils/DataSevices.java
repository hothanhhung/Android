package com.hth.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.hth.data.FolderInfo;

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
                    folderInfo.Name = file.getName();
                    folderInfo.FullPath = file.getPath();
                    folderInfo.IsFile = file.isFile();
                    folderInfo.LastModified = new Date(file.lastModified());
                    if(folderInfo.IsFile){
                        folderInfo.Size = file.length();
                        folderInfo.DisplaySize = FolderInfo.GetDisplaySize(file.length());
                        fileInfos.add(folderInfo);
                    }
                    else folderInfos.add(folderInfo);

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

}