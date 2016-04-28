package com.hth.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

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

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

}
