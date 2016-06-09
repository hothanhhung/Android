package com.hth.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Lenovo on 6/9/2016.
 */
public class MethodsHelper {

    static public String getCurrentDateToOrder(){
        return getCurrentDate("yyyyMMddhhmmss");
    }

    static public String getCurrentDate(){
        return getCurrentDate("hh:mm:ss - MMM dd, yyyy");
    }

    static public String getCurrentDate(String format){
        Date now = new Date();
        String formattedDate = new SimpleDateFormat(format).format(now);
        return formattedDate.replace('-','\n');
    }

}
