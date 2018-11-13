package com.hunght.data;

public class ThucHienQuyenItem {
    private String date;
    private String maCK;
    private String tieuDe;
    private String maISIN;
    private String loaiCK;
    private String thiTruong;
    private String chiNhanh;
    private String url;

    public ThucHienQuyenItem(){};

    public ThucHienQuyenItem(String date, String maCK, String tieuDe, String maISIN, String loaiCK, String thiTruong, String chiNhanh, String url) {
        this.date = date;
        this.maCK = maCK;
        this.tieuDe = tieuDe;
        this.maISIN = maISIN;
        this.loaiCK = loaiCK;
        this.thiTruong = thiTruong;
        this.chiNhanh = chiNhanh;
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public String getMaCK() {
        return maCK;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public String getMaISIN() {
        return maISIN;
    }

    public String getLoaiCK() {
        return loaiCK;
    }

    public String getThiTruong() {
        return thiTruong;
    }

    public String getChiNhanh() {
        return chiNhanh;
    }
    public String getFullUrl() {
        return url;
    }

    public String getExtraInfo() {
        return maCK + " - " + maISIN + " - " + loaiCK + " - " + thiTruong + " - " + chiNhanh;
    }
}
