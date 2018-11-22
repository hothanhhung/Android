package com.hunght.data;

public class DanhMucDauTuItem {
    private String dateMua;
    private String maCK;
    private String tenCongTy;
    private float giaMua;
    private int soLuong;
    private float giaThiTruong;
    private float giaBan;

    public DanhMucDauTuItem(){}

    public DanhMucDauTuItem(String dateMua, String maCK, String tenCongTy, float giaMua, int soLuong) {

        this.dateMua = dateMua;
        this.maCK = maCK;
        this.tenCongTy = tenCongTy;
        this.giaMua = giaMua;
        this.soLuong = soLuong;
        this.giaThiTruong = giaThiTruong;
        this.giaBan = giaBan;
    }

    public DanhMucDauTuItem(String dateMua, String maCK, String tenCongTy, float giaMua, int soLuong, float giaThiTruong, float giaBan) {

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

    public String getNgayMua() {
        return dateMua;
    }

    public void setNgayMua(String date) {
        this.dateMua = date;
    }

    public String getMaCK() {
        return maCK;
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
        return String.format("%f", soLuong);
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public float getGiaThiTruong() {
        return giaThiTruong;
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
        return (giaThiTruong - giaMua)*soLuong;
    }

    public String getLoiNhanInString() {
        return String.format("%f", getLoiNhan());
    }
}
