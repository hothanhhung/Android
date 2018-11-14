package com.hunght.data;

public class PriceItem {
    String Date;
    String Price;
    String change;
    String khoiLuong;
    String priceAtOpen;
    String lowestPrice;
    String highestPrice;

    public PriceItem(){

    }

    public PriceItem(String date, String price){
        this.Date = date;
        this.Price = price;
    }

    public PriceItem(String Date, String Price, String change, String khoiLuong, String priceAtOpen, String lowestPrice, String highestPrice){
        this.Date = Date;
        this.Price = Price;
        this.change = change;
        this.khoiLuong = khoiLuong;
        this.priceAtOpen = priceAtOpen;
        this.lowestPrice = lowestPrice;
        this.highestPrice = highestPrice;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDate() {
        return Date;
    }

    public String getPrice() {
        return Price;
    }

    public int getOffSet()
    {
        if(change == null || change.contains("...") || change.isEmpty()) return 2;
        else if(change!=null)
        {
            if(change.contains("-")) return -1;
            if(change.contains("(0.00%)")) return 0;
        }
        return 1;
    }

    public String getChange() {
        return change;
    }

    public String getKhoiLuong() {
        return khoiLuong;
    }

    public String getPriceAtOpen() {
        return priceAtOpen;
    }

    public String getLowestPrice() {
        return lowestPrice;
    }

    public String getHighestPrice() {
        return highestPrice;
    }
}
