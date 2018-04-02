package com.hunght.data;

/**
 * Created by Lenovo on 3/29/2018.
 */

public class LunarDate {
    public static final int TY = 0, SUU = 1, DAN =2, MAO = 3, THIN = 4, TI = 5, NGO=6, MUI=7, THAN=8, DAU = 9, TUAT = 10, HOI = 11;
    public static final String[] GIO = {"23-1","1-3", "3-5", "5-7", "7-9", "9-11", "11-13", "13-15", "15-17", "17-19", "19-21", "21-23"};
    public static final String[] CHI = {"Tý","Sữu", "Dần", "Mão", "Thìn", "Tỵ", "Ngọ", "Mùi", "Thân", "Dậu", "Tuất", "Hợi"};
    public static final String[] CAN = {"Canh", "Tân", "Nhâm", "Quý", "Giáp", "Ất", "Bính", "Đinh", "Mậu", "Kỷ"};
    int ngay, thang, nam;
    boolean nhuan;

    public LunarDate(int ngay, int thang, int nam, boolean nhuan)
    {
        this.ngay = ngay;
        this.thang = thang;
        this.nam =nam;
        this.nhuan = nhuan;
    }

    public int getDate()
    {
        return ngay;
    }

    public String getDateInString()
    {
        return "Chưa Tính";
    }

    public int getMonth()
    {
        return thang;
    }
    public String getMonthInString()
    {
        return CAN[((nam*12+thang+3)%10 + 4)%10]+" "+ CHI[(thang+1)%12];
    }
    public int getYear()
    {
        return nam;
    }

    public String getYearInString()
    {
        return CAN[nam%10]+" "+ CHI[(nam+8)%12];
    }

    public boolean isLeap()
    {
        return nhuan;
    }

    public static String getHourInfo(int chi)
    {
        return LunarDate.CHI[chi] + " (" + LunarDate.GIO[chi]+")";
    }
}
