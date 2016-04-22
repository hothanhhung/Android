package com.hth.data;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by HoHung on 12/6/2015.
 */
public class FolderInfo {
    public String Name;
    public String FullPath;
    public boolean IsFile;
    public Date LastModified;
    public long Size = 0;
    public String DisplaySize = "";
    public String DisplayLastModified = "";
    public String Preview = "";
    public boolean IsCommonPreview = true;
    public boolean IsVideo = false;

    public static double roundToDecimals(double d, int c)
    {
        int temp = (int)(d * Math.pow(10 , c));
        return ((double)temp)/Math.pow(10 , c);
    }

    public static String GetDisplaySize(Date LastModified)
    {
        SimpleDateFormat df = new SimpleDateFormat(); //called without pattern
        return df.format(LastModified.getTime());
    }

    public static String GetDisplaySize(long size){
        if(size<1024){
            return size+"B";
        }
        else if(size<1024*1024){
            long rs = size/1024;
            return rs + "KB";
        }
        else if(size<1024*1024*1024){
            long rs = size/(1024*1024);
            return rs + "MB";
        }else {
            long rs = size/(1024*1024*1024);
            return rs + "GB";
        }
    }
}
