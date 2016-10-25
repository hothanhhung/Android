package com.hth.data;

import com.hth.datmon.MethodsHelper;

/**
 * Created by Lenovo on 10/24/2016.
 */
public class OrderItem {
    String Detail;
    String Price;
    String UrlImage;
    boolean IsOutOfStock;
    String EnglishDetail;

    public OrderItem()
    {}

    public OrderItem(String detail, String price, String urlImage, boolean isOutOfStock)
    {
        Detail = detail;
        Price = price;
        UrlImage = urlImage;
        IsOutOfStock = isOutOfStock;
    }

    public String getEnglishDetail()
    {
        if(EnglishDetail == null || EnglishDetail.isEmpty())
        {
            EnglishDetail = MethodsHelper.stripAccentsAndD(Detail);
        }
        return EnglishDetail;
    }

    public String getDetail() {
        return Detail;
    }

    public boolean isOutOfStock() {
        return IsOutOfStock;
    }

    public String getPrice() {
        return Price;
    }

    public String getUrlImage() {
        return UrlImage;
    }


}
