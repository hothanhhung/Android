package com.hunght.data;

import com.hunght.tinchungkhoan.R;

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
            menuLookUpItems.add(new MenuLookUpItem("Thông Tin Phương Tiện Từ Biển Số Ôtô", R.drawable.ic_menu_gallery,"com.hth.tracuuonline.LookUpBienSoOto",""));
            menuLookUpItems.add(new MenuLookUpItem("Cafef",R.drawable.ic_menu_camera,"com.hunght.tinchungkhoan.LookUpForViewWithWebViewRequest","", MenuLookUpItemKind.Cafef));

        }
        return menuLookUpItems;
    }


}
