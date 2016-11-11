package com.hth.data;

import com.hth.tracuuonline.R;

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
            menuLookUpItems.add(new MenuLookUpItem("Tra Cứu Biển Số OtO", R.drawable.autocar,"com.hth.tracuuonline.LookUpBienSoOto",""));
            menuLookUpItems.add(new MenuLookUpItem("Tra Cứu Mã Biển Số",0,"com.hth.tracuuonline.LookUpForViewWithWebViewRequest","", MenuLookUpItemKind.MaBienSo));
            menuLookUpItems.add(new MenuLookUpItem("Tra Cứu Đầu Số Dien Thoai",R.drawable.phonenumber,"com.hth.tracuuonline.LookUpForViewWithWebViewRequest","", MenuLookUpItemKind.DauSoDienThoai));
            menuLookUpItems.add(new MenuLookUpItem("Tra Cứu Thông Tin Người Nộp Thuế",R.drawable.tax,"com.hth.tracuuonline.LookUpThongTinNguoiNopThue",""));
            menuLookUpItems.add(new MenuLookUpItem("Tra Cứu Lãi Suất Ngân Hàng",R.drawable.rate_bank,"com.hth.tracuuonline.LookUpForViewWithWebView","", MenuLookUpItemKind.LaiSuatNganHang));
            menuLookUpItems.add(new MenuLookUpItem("Tra Cứu Tỷ Giá Ngoại Tệ",R.drawable.money_exchange,"com.hth.tracuuonline.LookUpForViewWithWebView","", MenuLookUpItemKind.TyGiaNgoaiTe));
            menuLookUpItems.add(new MenuLookUpItem("Tra Cứu Giá Vàng",R.drawable.gold,"com.hth.tracuuonline.LookUpForViewWithWebView","", MenuLookUpItemKind.BangGiaVang));
            menuLookUpItems.add(new MenuLookUpItem("Tra Cứu Giá Xăng",R.drawable.oil,"com.hth.tracuuonline.LookUpForViewWithWebView","", MenuLookUpItemKind.GiaXang));
            menuLookUpItems.add(new MenuLookUpItem("Tra Cứu Kết Quả Sổ Số",R.drawable.lottery,"com.hth.tracuuonline.LookUpKetQuaXoSo",""));
            menuLookUpItems.add(new MenuLookUpItem("Tra Cứu Thông Tin Thời Tiết",R.drawable.weather,"com.hth.tracuuonline.LookUpForViewWithWebView","", MenuLookUpItemKind.DuBaoThoiTiet));
            menuLookUpItems.add(new MenuLookUpItem("Tra Cứu Thông Tin Bóng Đá",R.drawable.football,"com.hth.tracuuonline.LookUpForViewWithWebViewRequest","", MenuLookUpItemKind.BongDa));

        }
        return menuLookUpItems;
    }


}
