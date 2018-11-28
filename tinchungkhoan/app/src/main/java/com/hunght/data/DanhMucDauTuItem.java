package com.hunght.data;

import com.hunght.utils.MethodsHelper;

import java.util.Calendar;

public class DanhMucDauTuItem {
    private long id;
    private String dateMua;
    private String maCK;
    private String tenCongTy;
    private float giaMua;
    private int soLuong;
    private float giaThiTruong;
    private float giaBan;

    public DanhMucDauTuItem(){
        this.id = Calendar.getInstance().getTimeInMillis();
    }

    public DanhMucDauTuItem(String dateMua, String maCK, String tenCongTy, float giaMua, int soLuong) {
        this.id = Calendar.getInstance().getTimeInMillis();
        this.dateMua = dateMua;
        this.maCK = maCK;
        this.tenCongTy = tenCongTy;
        this.giaMua = giaMua;
        this.soLuong = soLuong;
    }

    public DanhMucDauTuItem(String dateMua, String maCK, String tenCongTy, float giaMua, int soLuong, float giaThiTruong, float giaBan) {
        this.id = Calendar.getInstance().getTimeInMillis();
        this.dateMua = dateMua;
        this.maCK = maCK;
        this.tenCongTy = tenCongTy;
        this.giaMua = giaMua;
        this.soLuong = soLuong;
        this.giaThiTruong = giaThiTruong;
        this.giaBan = giaBan;
    }

    public boolean isTheSame(DanhMucDauTuItem item)
    {
        return (item!= null && maCK.equalsIgnoreCase(item.getMaCK()) && giaBan == item.getGiaBan() &&
                giaMua == item.getGiaMua() && dateMua.equalsIgnoreCase(item.getNgayMua()));
    }

    public int compareDate(DanhMucDauTuItem item)
    {
        int index = getDateMuaInYYYYmmDD().compareTo(item.getDateMuaInYYYYmmDD());
        if(index == 0) return Long.compare(id, item.id);
        return index;
    }

    private String getDateMuaInYYYYmmDD(){
        String[] items = dateMua.split("-");
        if(items.length == 3)
        {
            return items[2] + items[1] + items[0];
        }
        return "";
    }
    public String getNgayMua() {
        return dateMua;
    }

    public void setNgayMua(String date) {
        this.dateMua = date;
    }

    public String getMaCK() {
        return maCK;
    }

    public String getAllInfo() {
        return "Mã " + getMaCK() + " - Số Lượng " + MethodsHelper.getStringFromInt(getSoLuong()) + " - Giá Mua " + MethodsHelper.getStringFromFloat(getGiaMua());
    }

    public boolean daBan() {
        return giaBan > 0;
    }

    public void setMaCK(String maCK) {
        this.maCK = maCK;
    }

    public String getTenCongTy() {
        return tenCongTy;
    }

    public String getMaCKAndTenCongTy() {
        return maCK + " - " + tenCongTy;
    }

    public void setTenCongTy(String tenCongTy) {
        this.tenCongTy = tenCongTy;
    }

    public float getGiaMua() {
        return giaMua;
    }

    public void setGiaMua(float giaMua) {
        this.giaMua = giaMua;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public String getSoLuongInString() {
        return String.format("%.2f", soLuong);
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public float getGiaThiTruong() {
        return giaThiTruong;
    }

    public float getGiaBanHoacThiTruong() {
        return giaBan > 0 ? giaBan : giaThiTruong;
    }

    public String getGiaThiTruongInString() {
        return String.format("%f", giaThiTruong);
    }

    public void setGiaThiTruong(float giaThiTruong) {
        this.giaThiTruong = giaThiTruong;
    }

    public float getGiaBan() {
        return giaBan;
    }

    public String getGiaBanInString() {
        return String.format("%f", giaBan);
    }

    public void setGiaBan(float giaBan) {
        this.giaBan = giaBan;
    }

    public float getLoiNhan() {
        return ((giaBan > 0? giaBan : giaThiTruong) - giaMua)*soLuong;
    }

    public float getTongThiTruongHoacBan() {
        return (giaBan > 0? giaBan : giaThiTruong)*soLuong;
    }

    public float getTongDauTu() {
        return giaMua*soLuong;
    }

    public String getLoiNhanInString() {
        return String.format("%f", getLoiNhan());
    }
}
