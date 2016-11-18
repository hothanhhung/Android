package com.hth.service;

public class OrderCustomer
{
    public String ID;
    public String CustomerId;
    public Customer Customer;
    public String OrderId;
    public Order Order;
    public boolean IsActive;
    public boolean IsUpdate;
    public String UserId;
    public String CreatedBy;
    public String CreatedDate;
    public String UpdatedDate;

    public String getID() {
        return ID;
    }

    public boolean isNew() {
        return ID==null || ID.isEmpty();
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public com.hth.service.Customer getCustomer() {
        return Customer;
    }

    public void setCustomer(com.hth.service.Customer customer) {
        Customer = customer;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }
}

