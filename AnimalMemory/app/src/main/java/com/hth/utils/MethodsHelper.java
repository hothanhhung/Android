package com.hth.utils;

import com.hth.AnimalMemory.R;

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

    static public String getScoreInFormat(int score){
        return String.format("%1$05d", score);

    }

    static public String getTimeFormat(Long var1) {
        String var2 = String.valueOf((int) (var1.longValue() / 3600L));
        String var3 = String.valueOf((int) (var1.longValue() % 3600L / 60L));
        String var4 = String.valueOf((int) (var1.longValue() % 60L));
        if (var2.length() == 1) {
            var2 = "0".concat(var2);
        }

        if (var3.length() == 1) {
            var3 = "0".concat(var3);
        }

        if (var4.length() == 1) {
            var4 = "0".concat(var4);
        }

        return var2.concat(":").concat(var3).concat(":").concat(var4);
    }
}
