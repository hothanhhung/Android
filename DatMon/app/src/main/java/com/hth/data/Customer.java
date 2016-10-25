package com.hth.data;

import com.hth.datmon.MethodsHelper;

/**
 * Created by Lenovo on 10/25/2016.
 */
public class Customer {
    int Id;
    String Name;
    String Address;
    String PhoneNumber;
    String Email;
    String Sex;
    String EnglishName;

    public Customer(){

    }

    public Customer(String name, String address, String phoneNumber, String email, String sex){
        Id = 0;
        Name = name;
        Address = address;
        PhoneNumber = phoneNumber;
        Email = email;
        Sex = sex;
    }

    public String getEnglishName(){
        if(EnglishName==null || EnglishName.isEmpty())
        {
            EnglishName = MethodsHelper.stripAccentsAndD(Name);
        }
        return EnglishName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }
}
