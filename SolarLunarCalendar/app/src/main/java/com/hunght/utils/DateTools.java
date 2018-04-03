package com.hunght.utils;

import com.hunght.data.DateItemForGridview;
import com.hunght.data.LunarDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Lenovo on 3/26/2018.
 */

public class DateTools {

    static public ArrayList<DateItemForGridview> GetDateItemsForGridviewFromDate()
    {
        Date currentDate = new Date();
        return GetDateItemsForGridviewFromDate(currentDate);
    }
    static public ArrayList<DateItemForGridview> GetDateItemsForGridviewFromDate(Date currentDate)
    {
        ArrayList<DateItemForGridview> result = new ArrayList<DateItemForGridview>();
        result.add(new DateItemForGridview("CN", null, false));
        result.add(new DateItemForGridview("T2", null, false));
        result.add(new DateItemForGridview("T3", null, false));
        result.add(new DateItemForGridview("T4", null, false));
        result.add(new DateItemForGridview("T5", null, false));
        result.add(new DateItemForGridview("T6", null, false));
        result.add(new DateItemForGridview("T7", null, false));
        if(currentDate!=null)
        {
            Calendar firstCalendarOfMonth = Calendar.getInstance();
            firstCalendarOfMonth.setTime(currentDate);
            firstCalendarOfMonth.set(Calendar.DAY_OF_MONTH, firstCalendarOfMonth.getActualMinimum(Calendar.DAY_OF_MONTH));
            firstCalendarOfMonth.add(Calendar.DATE, -1 * firstCalendarOfMonth.get(Calendar.DAY_OF_WEEK) + 1);
            for(int i = 0; i < 42; i++)
            {
                Date date = firstCalendarOfMonth.getTime();
                result.add(new DateItemForGridview(null, date, date.getMonth() != currentDate.getMonth()));
                firstCalendarOfMonth.add(Calendar.DATE, 1);
            }
        }
        return result;
    }

    //Doi ra ngay am-duong
    public static LunarDate convertSolar2Lunar(int intNgay, int intThang, int intNam) {

        int[] date = VietCalendar.convertSolar2Lunar(intNgay, intThang, intNam, 7.0);
        return new LunarDate(date[0], date[1], date[2], date[3] != 0);
    }

    public static DateItemForGridview convertLunar2DateItemForGridview(int intNgay, int intThang, int intNam, boolean isLeap) {
        int[] solarDate = VietCalendar.convertLunar2Solar(intNgay, intThang, intNam, isLeap?1:0, 7.0);
        int[] l = VietCalendar.convertSolar2Lunar(solarDate[0], solarDate[1], solarDate[2], 7.0);
        int[] solarDate2 = VietCalendar.convertLunar2Solar(l[0], l[1], l[2], l[3], 7.0);
        if (solarDate[0] == solarDate2[0] && solarDate[1] == solarDate2[1] && solarDate[2] == solarDate2[2]) {
            return new DateItemForGridview(null, new Date(solarDate2[2] - 1900, solarDate2[1] - 1, solarDate2[0]), false);
        } else {
            return null;
        }
    }

    public static DateItemForGridview convertSolar2DateItemForGridview(int intNgay, int intThang, int intNam) {
        int[] l = VietCalendar.convertSolar2Lunar(intNgay, intThang, intNam, 7.0);
        int[] solarDate2 = VietCalendar.convertLunar2Solar(l[0], l[1], l[2], l[3], 7.0);
        if (intNgay == solarDate2[0] && intThang == solarDate2[1] && intNam == solarDate2[2]) {
            return new DateItemForGridview(null, new Date(solarDate2[2] - 1900, solarDate2[1] - 1, solarDate2[0]), false);
        } else {
            return null;
        }
    }

    public static int numberOfDayInLunarMonth(int thang, int nam)
    {
        int[] solarDate = VietCalendar.convertLunar2Solar(30, thang, nam, 0, 7.0);
        int[] l = VietCalendar.convertSolar2Lunar(solarDate[0], solarDate[1], solarDate[2], 7.0);
        if(l[0]==30 && l[1] == thang && l[2] == nam)
        {
            return 30;
        }else
        {
            return 29;
        }
    }
}
