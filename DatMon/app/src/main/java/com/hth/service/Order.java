package com.hth.service;

import java.util.ArrayList;
import java.util.Date;

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
    public ArrayList<OrderDetail> OrderDetails;
       /* public List<HistoryOrderDetail> HistoryOrderDetails;*/

    public Desk getDesk() {
        return Desk;
    }

    public void setDesk(Desk desk) {
        Desk = desk;
    }

    public com.hth.service.Customer getCustomer() {
        return Customer;
    }

    public void setCustomer(Customer customer) {
        Customer = customer;
    }

    public ArrayList<OrderDetail> getOrderDetails() {
        if(OrderDetails == null){
            OrderDetails = new ArrayList<>();
        }
        return OrderDetails;
    }

    public void setOrderDetails(ArrayList<OrderDetail> orderDetails) {
        OrderDetails = orderDetails;
    }

    public ArrayList<Customer> getCustomers() {
        return new ArrayList<>();
    }
}