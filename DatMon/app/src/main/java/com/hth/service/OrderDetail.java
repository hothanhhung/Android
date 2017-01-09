package com.hth.service;

import java.util.Date;

public class OrderDetail {
    public OrderDetail() {
    }

    public String ID;
    public String OrderID;
    public String MenuOrderID;
    public float Quantity;
    public float UnitPrice;
    public float TotalPrice;
        /*public MenuOrder MenuOrder;*/

    public Order Order;
    public boolean IsActive;
    public boolean IsUpdate;
    public boolean IsPromotion;
    public String UpdatedBy;
    public String CreatedBy;
    public String UserId;
    public String CreatedDate;
    public String UpdatedDate;
    public int Status;
    public String CompletedDate;


    //client object
    MenuOrder MenuOrder;

    public OrderDetail( MenuOrder menuOrder, float quantity)
    {
        MenuOrderID = menuOrder.getID();
        UnitPrice = menuOrder.getPrice();
        MenuOrder = menuOrder;
        Quantity = quantity;
    }

    public OrderDetail clone()
    {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.MenuOrderID = this.MenuOrderID;
        orderDetail.UnitPrice = this.UnitPrice;
        orderDetail.MenuOrder = this.MenuOrder;
        orderDetail.Quantity = this.Quantity;
        return orderDetail;
    }

    public String getMenuOrderID() {
        return MenuOrderID;
    }

    public String getName() {
        return MenuOrder.getName();
    }

    public void setQuantity(float quantity) {
        Quantity = quantity;
    }

    public float getQuantity() {
        return Quantity;
    }

    public float getPrice() {
        return MenuOrder.getPrice();
    }

    public float getTotal() {
        return Quantity * getPrice();
    }

    public int getStatus() {
        return Status;
    }
    public boolean isPromotion() {
        return IsPromotion;
    }

    public boolean isAllowedChangeQuatity()
    {
        return MenuOrder.getType() == 2 || MenuOrder.getType() == 3;
    }
}
