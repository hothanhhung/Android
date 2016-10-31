package com.hth.service;

import java.util.Date;
import java.util.List;

public class Order {
    public Order() {

    }

    public String ID;
    public String DeskID;
    public String DeskName;
    public Desk Desk;
    public Date InputDate;
    public String CustomerID;
    public Customer Customer;
    public boolean IsActive;
    public boolean IsUpdate;
    public String UserId;
    public Date CreatedDate;
    public Date UpdatedDate;
    public int Status;
    public String UpdatedBy;
    public String CreatedBy;
    public Date CompletedDate;
    public List<OrderDetail> OrderDetails;
       /* public List<HistoryOrderDetail> HistoryOrderDetails;*/
}