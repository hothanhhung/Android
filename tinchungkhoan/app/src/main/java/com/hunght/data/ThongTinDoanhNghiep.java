package com.hunght.data;

import java.util.Calendar;

public class ThongTinDoanhNghiep {
    private String name;
    private String code;
    private String logoURL;
    private String information;
    private String currentPrice;
    private String thongSoKT;
    private String traCoTuc;

    public ThongTinDoanhNghiep(){}

    public ThongTinDoanhNghiep(String name, String code, String logoURL, String information, String currentPrice, String thongSoKT, String traCoTuc){
        this.name = name;
        this.code = code;
        this.logoURL = logoURL;
        this.information = information;
        this.currentPrice = currentPrice;
        this.thongSoKT = thongSoKT;
        this.traCoTuc = traCoTuc;
    }

    public String getFullName() {
        return code + " - " + name;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public String getInformation() {
        return information;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public String getThongSoKT() {
        return thongSoKT;
    }

    public String getDoThiAll() {
        return "http://cafef4.vcmedia.vn/"+ getLastWorkingDate()+"/"+code+"/all.png";
    }

    public String getDoThiOneMonth() {
        return "http://cafef4.vcmedia.vn/"+ getLastWorkingDate()+"/"+code+"/1month.png";
    }
    public String getDoThiThreeMonth() {
        return "http://cafef4.vcmedia.vn/"+ getLastWorkingDate()+"/"+code+"/3months.png";
    }

    public String getDoThiSixMonth() {
        return "http://cafef4.vcmedia.vn/"+ getLastWorkingDate()+"/"+code+"/6months.png";
    }

    public String getDoThiOneYear() {
        return "http://cafef4.vcmedia.vn/"+ getLastWorkingDate()+"/"+code+"/1year.png";
    }

    public String getDoThiSevenToDay() {
        return "http://s.cafef.vn/chartindex/pricechart.ashx?symbol="+code+"&type=price&date="+getCurrentDate1()+"&width=380&height=250&rd=102830";
    }

    public String getDoThiSevenDays() {
        return "http://cafef4.vcmedia.vn/"+ getLastWorkingDate()+"/"+code+"/7days.png";
    }
    public String getTraCoTuc() {
        return traCoTuc;
    }

    public String getBaoCaoTaiChinhURL() {
        return "http://s.cafef.vn/Ajax/CongTy/BaoCaoTaiChinh.aspx?sym="+code+"&type=0&year=0";
    }

    private Calendar calendar;
    private String getLastWorkingDate(){
        if(calendar == null){
            calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
            {
                calendar.add(Calendar.DAY_OF_MONTH, -1);
            }
            else if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
            {
                calendar.add(Calendar.DAY_OF_MONTH, -2);
            }
        }

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        return ""+year + (month<10?"0":"") + month+ (day<10?"0":"") + day;
    }

    private String getCurrentDate1(){
        Calendar calendar = Calendar.getInstance();
        if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
        {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        else if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
        {
            calendar.add(Calendar.DAY_OF_MONTH, -2);
        }

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        return (day<10?"0":"") + day + (month<10?"/0":"/") + month+ "/"+year ;
    }
}
