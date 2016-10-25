package com.hth.data;

import java.util.ArrayList;

/**
 * Created by Lenovo on 10/25/2016.
 */
public class OrderData {

    ArrayList<OrderedItem> OrderedItem;
    ArrayList<Customer> Customers;

    public OrderData()
    {
        OrderedItem = new ArrayList<>();
        Customers = new ArrayList<>();
    }


    public ArrayList<Customer> getCustomers() {
        return Customers;
    }

    public void setCustomers(ArrayList<Customer> customers) {
        Customers = customers;
    }

    public ArrayList<com.hth.data.OrderedItem> getOrderedItem() {
        return OrderedItem;
    }

    public void setOrderedItem(ArrayList<com.hth.data.OrderedItem> orderedItem) {
        OrderedItem = orderedItem;
    }
}
