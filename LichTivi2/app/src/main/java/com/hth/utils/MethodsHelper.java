package com.hth.utils;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Lenovo on 6/9/2016.
 */
public class MethodsHelper {

    static public String getCurrentDateToOrder(){
        return getCurrentDate("yyyyMMddHHmmss");
    }

    static public String getCurrentDate(){
        return getCurrentDate("HH:mm:ss - MMM dd, yyyy");
    }

    static public String getCurrentDate(String format){
        Date now = new Date();
        String formattedDate = new SimpleDateFormat(format).format(now);
        return formattedDate;
    }

    static public String removeTone(String str){
        str= str.toLowerCase();
        str= str.replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]", "a");
        str= str.replaceAll("[èéẹẻẽêềếệểễ]", "e");
        str= str.replaceAll("[ìíịỉĩ]", "i");
        str= str.replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o");
        str= str.replaceAll("[ùúụủũưừứựửữ]", "u");
        str= str.replaceAll("[ỳýỵỷỹ]", "y");
        str= str.replaceAll("[đ]", "d");
        return str;
    }

    public static String stripAccents(String s)
    {
        s = s.toLowerCase();
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
       // s = s.replaceAll("[^\\p{ASCII}]", "");
       // s.replaceAll('đ', 'd');
        return s;
    }
}
