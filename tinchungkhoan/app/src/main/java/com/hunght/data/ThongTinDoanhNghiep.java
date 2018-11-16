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
        return "http://cafef4.vcmedia.vn/"+getCurrentDate()+"/"+code+"/all.png";
    }

    public String getDoThiOneMonth() {
        return "http://cafef4.vcmedia.vn/"+getCurrentDate()+"/"+code+"/1month.png";
    }

    public String getDoThiOneYear() {
        return "http://cafef4.vcmedia.vn/"+getCurrentDate()+"/"+code+"/1year.png";
    }

    public String getDoThiSevenDays() {
        return "http://cafef4.vcmedia.vn/"+getCurrentDate()+"/"+code+"/7days.png";
    }
    public String getTraCoTuc() {
        return traCoTuc;
    }

    public String getBaoCaoTaiChinhURL() {
        return "http://s.cafef.vn/Ajax/CongTy/BaoCaoTaiChinh.aspx?sym="+code+"&type=0&year=0";
    }

    private Calendar calendar;
    private String getCurrentDate(){
        if(calendar == null){
            calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        return ""+year + (month<10?"0":"") + month+ (day<10?"0":"") + day;
    }
}
