package com.hth.service;


import com.hth.datmon.MethodsHelper;

import java.util.Date;

public class Customer
    {
        public Customer() { }

        public String ID;
        public String FirstName;
        public String LastName;

        public String BithDay;
        public String Image ;
        public String Job;
        public String Address;
        public String PhoneNumber;
        public String Email;
        public String Facebook;
        public String Zalo;
        public String CarNumber;
        public boolean IsActive;
        public boolean IsUpdate;
        public String UserId;
        public String CreatedDate;
        public String UpdatedDate;

        public String getAddress() {
            return Address;
        }

        public void setAddress(String address) {
            Address = address;
        }

        public String getBithDay() {
            return BithDay;
        }

        public void setBithDay(String bithDay) {
            BithDay = bithDay;
        }

        public String getCarNumber() {
            return CarNumber;
        }

        public void setCarNumber(String carNumber) {
            CarNumber = carNumber;
        }

        public String getEmail() {
            return Email;
        }

        public void setEmail(String email) {
            Email = email;
        }

        public String getFacebook() {
            return Facebook;
        }

        public void setFacebook(String facebook) {
            Facebook = facebook;
        }

        public String getFirstName() {
            return FirstName;
        }

        public void setFirstName(String firstName) {
            FirstName = firstName;
        }

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getJob() {
            return Job;
        }

        public void setJob(String job) {
            Job = job;
        }

        public String getLastName() {
            return LastName;
        }

        public void setLastName(String lastName) {
            LastName = lastName;
        }

        public String getPhoneNumber() {
            return PhoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            PhoneNumber = phoneNumber;
        }

        public String getZalo() {
            return Zalo;
        }

        public void setZalo(String zalo) {
            Zalo = zalo;
        }

        String EnglishName;
        public String getEnglishName(){
            if(EnglishName==null || EnglishName.isEmpty())
            {
                EnglishName = MethodsHelper.stripAccentsAndD(FirstName);
            }
            return EnglishName;
        }
    }
   
