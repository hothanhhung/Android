package com.hth.data;

import com.hth.datmon.MethodsHelper;

import java.util.ArrayList;

/**
 * Created by Lenovo on 10/24/2016.
 */
public class OrderedItem {
    String Detail;
    int Price;
    int Quantity;

    public OrderedItem()
    {
    }

    public OrderedItem(String detail, int price, int quantity)
    {
        Detail = detail;
        Price = price;
        Quantity = quantity;
    }

    public String getDetail() {
        return Detail;
    }

    public int getQuantity() {
        return Quantity;
    }

    public int getPrice() {
        return Price;
    }

    public int getTotal() {
        return Quantity * Price;
    }


}
