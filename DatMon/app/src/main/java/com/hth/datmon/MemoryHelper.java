package com.hth.datmon;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by Lenovo on 4/28/2016.
 */
public class MemoryHelper {
    public static File getCacheFolder(Context context)
    {
        if(isExternalStorageWritable()){
            return context.getExternalCacheDir();
        }else{
            return context.getCacheDir();
        }
    }

    public static void freeCacheFolder(Context context)
    {
        if(isExternalStorageWritable()) {
            clearFolder(context.getCacheDir());
        }
        clearFolder(context.getCacheDir());

    }

    public static void clearFolder(File folder) {
        try {
            File[] directory = folder.listFiles();
            if (directory != null) {
                for (File file : directory) {
                    file.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

}
