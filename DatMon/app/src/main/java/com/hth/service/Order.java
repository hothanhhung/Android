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
    public String InputDate;
    public String CustomerID;
    public Customer Customer;
    public boolean IsRequestingPayment;
    public boolean IsUpdate;
    public boolean IsActive;
    public String UserId;
    public String CreatedDate;
    public String UpdatedDate;
    public int Status;
    public String UpdatedBy;
    public String CreatedBy;
    public String CompletedDate;
    public float PromotionPercent;
    public float TotalPromotion;
    public float Total;
    public float TotalPrice;
    public ArrayList<OrderDetail> OrderDetails;
       /* public List<HistoryOrderDetail> HistoryOrderDetails;*/

    public Desk getDesk() {
        return Desk;
    }
    public boolean isNew() {
        return ID== null || ID.isEmpty();
    }
    public void setDesk(Desk desk) {
        Desk = desk;
        if(Desk!=null) {
            DeskID = desk.getID();
        }
        else{
            DeskID = null;
        }
    }

    public com.hth.service.Customer getCustomer() {
        return Customer;
    }

    public void setCustomer(Customer customer) {
        Customer = customer;
        if (Customer != null) {
            CustomerID = Customer.getID();
        } else {
            CustomerID = null;
        }
    }

    public ArrayList<OrderDetail> getOrderDetails() {
        if(OrderDetails == null){
            OrderDetails = new ArrayList<>();
        }
        return OrderDetails;
    }
    public void clearOrderDetail()
    {
        OrderDetails = null;
    }
    public void setOrderDetails(ArrayList<OrderDetail> orderDetails) {
        OrderDetails = orderDetails;
    }

    public ArrayList<Customer> getCustomers() {
        return new ArrayList<>();
    }

    public String getID() {
        return ID;
    }

    public boolean isRequestingPayment() {
        return IsRequestingPayment;
    }

    public void setIsRequestingPayment(boolean isRequestingPayment) {
        IsRequestingPayment = isRequestingPayment;
    }

    public float getTotal() {
        return Total;
    }

    public float getTotalPrice() {
        return TotalPrice;
    }

    public float getTotalPromotion() {
        return TotalPromotion;
    }

    public float getPercentPromotion() {
        return PromotionPercent;
    }
}