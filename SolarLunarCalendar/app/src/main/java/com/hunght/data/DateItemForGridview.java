package com.hunght.data;

import android.util.Log;

import com.hunght.solarlunarcalendar.R;
import com.hunght.utils.DateTools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Lenovo on 3/26/2018.
 */

public class DateItemForGridview {
    private boolean isOutOfMonth;
    private Date date;
    LunarDate lunarDate;
    private String title;

    public DateItemForGridview()
    {

    }

    static public DateItemForGridview createDateItemForGridview(int d, int m, int y, boolean isLeap, boolean isLunar)
    {
        if(isLunar){
            return DateTools.convertLunar2DateItemForGridview(d, m, y, isLeap);
        }else{
            return DateTools.convertSolar2DateItemForGridview(d, m, y);
        }
    }

    public DateItemForGridview(String title, Date date, boolean isOutOfMonth)
    {
        this.title = title;
        this.isOutOfMonth = isOutOfMonth;
        if(date!=null){
            this.date = new Date(date.getYear(), date.getMonth(), date.getDate());
            this.lunarDate = DateTools.convertSolar2Lunar(getDayOfMonth(), getMonth(), getYear());
        }
    }

    public void addMonth(int number)
    {
        if(date!=null) {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.MONTH, number);
            date = c.getTime();
            lunarDate = DateTools.convertSolar2Lunar(getDayOfMonth(), getMonth(), getYear());
        }
    }

    public void setMonthYear(int month, int year)
    {
        if(date!=null) {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.set(Calendar.DAY_OF_MONTH, 1);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.YEAR, year);
            date = c.getTime();
            lunarDate = DateTools.convertSolar2Lunar(getDayOfMonth(), getMonth(), getYear());
        }
    }


    public boolean isOutOfMonth()
    {
        return isOutOfMonth;
    }

    public boolean isTheSame(Date toDate)
    {
        if(date != null && toDate != null)
        {
            return toDate.getDate() == date.getDate() && toDate.getMonth() == date.getMonth() && toDate.getYear() == date.getYear();
        }
        return toDate == date;
    }

    public boolean isToday()
    {
        if(date != null)
        {
            Date today = new Date();
            return today.getDate() == date.getDate() && today.getMonth() == date.getMonth() && today.getYear() == date.getYear();

        }
        return false;
    }

    public boolean isTitle() {
        return title!=null && title!="";
    }

    public String getTitle() {
        return title;
    }

    public String getSolarDate()
    {
        if(date!=null)
        {
            return "" + date.getDate();
        }
        return "";
    }

    public String getLunarDateToDisplay()
    {
        String str = "";
        if(date!=null)
        {
            str += lunarDate.getDate();
            if((!isOutOfMonth && date.getDate() == 1) || lunarDate.getDate() == 1)
            {
                str += "/" + lunarDate.getMonth();
            }
        }
        return str;
    }
    public String getSolarInfo(boolean isNewLine)
    {
        String space = isNewLine?"\n":" ";
        String str = "";
        if(date!=null)
        {
            str += "Ngày "+getSolarDate()+ space +"Tháng "+getMonth()+space+"Năm " + getYear();
        }
        return str;
    }
    public String getLunarInfo(boolean isNewLine)
    {
        String space = isNewLine?"\n":" ";
        String str = "";
        if(date!=null)
        {
            str += "Ngày "+lunarDate.getDate()+ space +"Tháng "+lunarDate.getMonth() + (lunarDate.isLeap()?" (N)":"") + space +"Năm Âm Lịch";
        }
        return str;
    }

    public String getLunarInfo1(boolean isNewLine)
    {
        String space = isNewLine?"\n":" ";
        String str = "";
        if(date!=null)
        {
            str += "Ngày "+getLunarDateInString()+ space +"Tháng "+lunarDate.getMonthInString()+ space +"Năm "+lunarDate.getYearInString();
        }
        return str;
    }

    private int getDateInLunar()
    {
        Date orginalDate = new Date(2013 - 1900, 8 - 1, 2); //Canh Tý
        int day = new Long(((date.getTime() -orginalDate.getTime())/(1000*24*3600)) % 60).intValue();
        if(day<0) day+=60;
        return day;
    }

    public String getLunarGoodTime() {
        String str = "Giờ hoàng đạo: ";
        if (date != null) {

            int day = getDateInLunar() % 12;
            switch (day) {
                case LunarDate.DAN:
                case LunarDate.THAN:
                    str += LunarDate.getHourInfo(LunarDate.TY);
                    str += ", " + LunarDate.getHourInfo(LunarDate.SUU);
                    str += ", " + LunarDate.getHourInfo(LunarDate.THIN);
                    str += ", " + LunarDate.getHourInfo(LunarDate.TI);
                    str += ", " + LunarDate.getHourInfo(LunarDate.MUI);
                    str += ", " + LunarDate.getHourInfo(LunarDate.TUAT);
                    break;
                case LunarDate.MAO:
                case LunarDate.DAU:
                    str += LunarDate.getHourInfo(LunarDate.TY);
                    str += ", " + LunarDate.getHourInfo(LunarDate.DAN);
                    str += ", " + LunarDate.getHourInfo(LunarDate.MAO);
                    str += ", " + LunarDate.getHourInfo(LunarDate.NGO);
                    str += ", " + LunarDate.getHourInfo(LunarDate.MUI);
                    str += ", " + LunarDate.getHourInfo(LunarDate.DAU);
                    break;
                case LunarDate.THIN:
                case LunarDate.TUAT:
                    str += LunarDate.getHourInfo(LunarDate.DAN);
                    str += ", " + LunarDate.getHourInfo(LunarDate.THIN);
                    str += ", " + LunarDate.getHourInfo(LunarDate.TI);
                    str += ", " + LunarDate.getHourInfo(LunarDate.THAN);
                    str += ", " + LunarDate.getHourInfo(LunarDate.DAU);
                    str += ", " + LunarDate.getHourInfo(LunarDate.HOI);
                    break;
                case LunarDate.TI:
                case LunarDate.HOI:
                    //     str = "Sửu, Thìn, Ngọ, Mùi, Tuất, Hợi";
                    str += LunarDate.getHourInfo(LunarDate.SUU);
                    str += ", " + LunarDate.getHourInfo(LunarDate.THIN);
                    str += ", " + LunarDate.getHourInfo(LunarDate.NGO);
                    str += ", " + LunarDate.getHourInfo(LunarDate.MUI);
                    str += ", " + LunarDate.getHourInfo(LunarDate.TUAT);
                    str += ", " + LunarDate.getHourInfo(LunarDate.HOI);
                    break;
                case LunarDate.TY:
                case LunarDate.NGO:
                    //     str = "Tý, Sửu, Mão, Ngọ, Thân, Dậu";
                    str += LunarDate.getHourInfo(LunarDate.TY);
                    str += ", " + LunarDate.getHourInfo(LunarDate.SUU);
                    str += ", " + LunarDate.getHourInfo(LunarDate.MAO);
                    str += ", " + LunarDate.getHourInfo(LunarDate.NGO);
                    str += ", " + LunarDate.getHourInfo(LunarDate.THAN);
                    str += ", " + LunarDate.getHourInfo(LunarDate.DAU);
                    break;
                case LunarDate.SUU:
                case LunarDate.MUI:
                    // str = "Dần, Mão, Tỵ, Thân, Tuất, Hợi";
                    str += LunarDate.getHourInfo(LunarDate.DAN);
                    str += ", " + LunarDate.getHourInfo(LunarDate.MAO);
                    str += ", " + LunarDate.getHourInfo(LunarDate.TI);
                    str += ", " + LunarDate.getHourInfo(LunarDate.THAN);
                    str += ", " + LunarDate.getHourInfo(LunarDate.TUAT);
                    str += ", " + LunarDate.getHourInfo(LunarDate.HOI);
                    break;
            }
        }
        return str;
    }

    public boolean isGoodDay() {
        if (date != null) {
            int day = getDateInLunar() % 12;
            int month = lunarDate.getMonth();
            switch (month) {
                case 1:
                case 7:
                    if (day == LunarDate.TY || day == LunarDate.SUU || day == LunarDate.TI || day == LunarDate.MUI) {
                        return true;
                    }
                    break;
                case 2:
                case 8:
                    if (day == LunarDate.DAN || day == LunarDate.MAO || day == LunarDate.MUI || day == LunarDate.DAU) {
                        return true;
                    }
                    break;
                case 3:
                case 9:
                    if (day == LunarDate.THIN || day == LunarDate.TI || day == LunarDate.DAU || day == LunarDate.HOI) {
                        return true;
                    }
                    break;
                case 4:
                case 10:
                    if (day == LunarDate.NGO || day == LunarDate.MUI || day == LunarDate.SUU || day == LunarDate.DAU) {
                        return true;
                    }
                    break;
                case 5:
                case 11:
                    if (day == LunarDate.THAN || day == LunarDate.DAU || day == LunarDate.SUU || day == LunarDate.MAO) {
                        return true;
                    }
                    break;
                case 6:
                case 12:
                    if (day == LunarDate.TUAT || day == LunarDate.HOI || day == LunarDate.MAO || day == LunarDate.TI) {
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    public boolean isBadDay() {
        if (date != null) {
            int day = getDateInLunar() % 12;
            int month = lunarDate.getMonth();
            switch (month) {
                case 1:
                case 7:
                    if (day == LunarDate.NGO || day == LunarDate.MAO || day == LunarDate.HOI || day == LunarDate.DAU) {
                        return true;
                    }
                    break;
                case 2:
                case 8:
                    if (day == LunarDate.THAN || day == LunarDate.TI || day == LunarDate.SUU || day == LunarDate.HOI) {
                        return true;
                    }
                    break;
                case 3:
                case 9:
                    if (day == LunarDate.TUAT || day == LunarDate.MUI || day == LunarDate.SUU || day == LunarDate.HOI) {
                        return true;
                    }
                    break;
                case 4:
                case 10:
                    if (day == LunarDate.TY || day == LunarDate.DAU || day == LunarDate.TI || day == LunarDate.MAO) {
                        return true;
                    }
                    break;
                case 5:
                case 11:
                    if (day == LunarDate.DAN || day == LunarDate.HOI || day == LunarDate.MUI || day == LunarDate.TI) {
                        return true;
                    }
                    break;
                case 6:
                case 12:
                    if (day == LunarDate.THIN || day == LunarDate.SUU || day == LunarDate.DAU || day == LunarDate.MUI) {
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    public String getLunarDateInString()
    {
        if(date!=null)
        {
            Date orginalDate = new Date(2013 - 1900, 8 - 1, 2); //Canh Tý
            int d = getDateInLunar();
            return LunarDate.CAN[d%10]+" "+ LunarDate.CHI[d%12];
        }
        return "";
    }

    public String getLunarDate()
    {
        return "13";
    }

    public boolean isHoliday(){
        if(date!=null)
        {
            return date.getDay() == 0;
        }
        return title == "CN";
    }

    public Date getDate(){
        return date;
    }

    public String getDayOfWeekInString()
    {
        if(date!=null)
        {
            if(date.getDay() == 0) return "Chủ Nhật";
            else
                return "Thứ " + (date.getDay() + 1);
        }
        return "";
    }

    public int getDayOfMonth()
    {
        if(date!=null)
        {
            return date.getDate();
        }
        return 0;
    }

    public int getMonth()
    {
        if(date!=null)
        {
            return date.getMonth() + 1;
        }
        return 0;
    }

    public int getYear()
    {
        if(date!=null)
        {
            return date.getYear() + 1900;
        }
        return 0;
    }
}
