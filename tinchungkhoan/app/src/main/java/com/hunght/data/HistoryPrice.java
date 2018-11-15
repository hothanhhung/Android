package com.hunght.data;

import java.util.ArrayList;

public class HistoryPrice {
    String maCK;
    ArrayList<PriceItem> prices;

    public HistoryPrice(){

    }

    public HistoryPrice(String maCK){
        this.maCK = maCK;
    }

    public HistoryPrice(String maCK, ArrayList<PriceItem> prices){
        this.maCK = maCK;
        this.prices = prices;
    }

    public String getMaCK() {
        if(maCK == null) maCK = "";
        return maCK;
    }

    public String getFullName(ArrayList<DoanhNghiepItem> thongTinDoanhNghieps) {
        if(maCK == null) maCK = "";
        if(thongTinDoanhNghieps!=null){
            for (DoanhNghiepItem thongtindoanhnghiep: thongTinDoanhNghieps) {
                if(thongtindoanhnghiep.c.equalsIgnoreCase(maCK)){
                    return thongtindoanhnghiep.c + " - " + thongtindoanhnghiep.m;
                }
            }
        }

        return maCK;
    }

    public ArrayList<PriceItem> getPrices() {
        return prices;
    }

    public void setPrices(ArrayList<PriceItem> prices) {
        this.prices = prices;
    }

    public PriceItem getPriceBasedOnDate(String date)
    {
        if(prices!=null)
        {
            for (PriceItem priceItem:prices) {
                if(priceItem.getDate().equalsIgnoreCase(date))
                {
                    return priceItem;
                }
            }
            return new PriceItem(date, "-");
        }
        return new PriceItem(date, "...");
    }
}
