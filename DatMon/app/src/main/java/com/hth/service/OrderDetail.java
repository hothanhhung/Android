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
    public String UpdatedBy;
    public String CreatedBy;
    public String UserId;
    public Date CreatedDate;
    public Date UpdatedDate;
    public int Status;
    public Date CompletedDate;
}
