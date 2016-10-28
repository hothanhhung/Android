package com.hth.data;

import java.util.ArrayList;

/**
 * Created by Lenovo on 10/28/2016.
 */
public class StaticData {
    private static ArrayList<MenuLookUpItem> menuLookUpItems;
    public static ArrayList<MenuLookUpItem> GetMenuLookUpItems(){
        if(menuLookUpItems == null)
        {
            menuLookUpItems = new ArrayList<>();
            menuLookUpItems.add(new MenuLookUpItem("Tra Cứu Biển Số OtO",0,"com.hth.tracuuonline.LookUpBienSoOto",""));
            menuLookUpItems.add(new MenuLookUpItem("Tra Cứu Mã Biển Số",0,"",""));
            menuLookUpItems.add(new MenuLookUpItem("Tra Cứu Đầu Số Di Động",0,"",""));
            menuLookUpItems.add(new MenuLookUpItem("Tra Cứu Lãi Suất Ngân Hàng",0,"",""));
            menuLookUpItems.add(new MenuLookUpItem("Tra Cứu Kết Quả Sổ Số",0,"com.hth.tracuuonline.LookUpKetQuaXoSo",""));
        }
        return menuLookUpItems;
    }


}
