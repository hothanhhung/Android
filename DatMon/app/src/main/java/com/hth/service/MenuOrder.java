package com.hth.service;

import com.hth.datmon.MethodsHelper;

import java.util.Date;

public class MenuOrder
    {
        public MenuOrder()
        {

        }

        public String ID;
        public String PathImage;
        public String Name;
        public float Cost;
        public float Price;
        public float GrossProfit;
        public float PercentageCostToPrice;
        public float FixedCostPercent;
        public float GrossProfitPercent;
        public float Amount;
        public boolean IsActive;
        public boolean IsUpdate;
        public String UserId;
        public String CreatedDate;
        public String UpdatedDate;
        public int Type; //1: đồ uống, 2 đồ ăn, 3 khác

        public float getCost() {
            return Cost;
        }

        public String getID() {
            return ID;
        }

        public boolean isActive() {
            return IsActive;
        }

        public String getName() {
            return Name;
        }

        public String getPathImage() {
            return PathImage;
        }

        public float getPrice() {
            return Price;
        }

        public boolean hasImage()
        {
            return PathImage!=null && !PathImage.isEmpty();
        }

        String EnglishDetail;
        public String getEnglishDetail()
        {
            if(EnglishDetail == null || EnglishDetail.isEmpty())
            {
                EnglishDetail = MethodsHelper.stripAccentsAndD(Name);
            }
            return EnglishDetail;
        }

        public boolean isOutOfStock() {
            return false;
        }

        public int getType() {
            return Type;
        }
    }
