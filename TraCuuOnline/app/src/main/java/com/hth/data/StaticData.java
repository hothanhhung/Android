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
            menuLookUpItems.add(new MenuLookUpItem("Thông Tin Phương Tiện Từ Biển Số Ôtô", R.drawable.autocar,"com.hth.tracuuonline.LookUpBienSoOto",""));
            menuLookUpItems.add(new MenuLookUpItem("Mã Biển Số Xe",R.drawable.biensoxe,"com.hth.tracuuonline.LookUpForViewWithWebViewRequest","", MenuLookUpItemKind.MaBienSo));
            menuLookUpItems.add(new MenuLookUpItem("Giá Xe Ôto",R.drawable.pricecar,"com.hth.tracuuonline.LookUpForViewWithWebViewRequest","", MenuLookUpItemKind.GiaOto));
            menuLookUpItems.add(new MenuLookUpItem("Mã Bưu Điện",R.drawable.mail,"com.hth.tracuuonline.LookUpForViewWithWebViewRequest","", MenuLookUpItemKind.MaBuuDien));
            menuLookUpItems.add(new MenuLookUpItem("Đầu Số Điện Thoại",R.drawable.phonenumber,"com.hth.tracuuonline.LookUpForViewWithWebViewRequest","", MenuLookUpItemKind.DauSoDienThoai));
            menuLookUpItems.add(new MenuLookUpItem("Thông Tin Người Nộp Thuế",R.drawable.tax,"com.hth.tracuuonline.LookUpThongTinNguoiNopThue",""));
            menuLookUpItems.add(new MenuLookUpItem("Mã Ngân Hàng (Swift Code)",R.drawable.swiftcode,"com.hth.tracuuonline.LookUpForViewWithWebViewRequest","", MenuLookUpItemKind.SwiftCode));
            menuLookUpItems.add(new MenuLookUpItem("Lãi Suất Ngân Hàng",R.drawable.rate_bank,"com.hth.tracuuonline.LookUpForViewWithWebView","", MenuLookUpItemKind.LaiSuatNganHang));
            menuLookUpItems.add(new MenuLookUpItem("Tỷ Giá Ngoại Tệ",R.drawable.money_exchange,"com.hth.tracuuonline.LookUpForViewWithWebView","", MenuLookUpItemKind.TyGiaNgoaiTe));
            menuLookUpItems.add(new MenuLookUpItem("Giá Vàng",R.drawable.gold,"com.hth.tracuuonline.LookUpForViewWithWebViewRequest","", MenuLookUpItemKind.BangGiaVang));
            menuLookUpItems.add(new MenuLookUpItem("Giá Xăng",R.drawable.oil,"com.hth.tracuuonline.LookUpForViewWithWebViewRequest","", MenuLookUpItemKind.GiaXang));
            menuLookUpItems.add(new MenuLookUpItem("Kết Quả Xổ Số",R.drawable.lottery,"com.hth.tracuuonline.LookUpKetQuaXoSo",""));
            menuLookUpItems.add(new MenuLookUpItem("Thông Tin Thời Tiết",R.drawable.weather,"com.hth.tracuuonline.LookUpForViewWithWebView","", MenuLookUpItemKind.DuBaoThoiTiet));
            menuLookUpItems.add(new MenuLookUpItem("Thông Tin Bóng Đá",R.drawable.football,"com.hth.tracuuonline.LookUpForViewWithWebViewRequest","", MenuLookUpItemKind.BongDa));
           //menuLookUpItems.add(new MenuLookUpItem("Lịch Chiếu Phim Rạp",R.drawable.film,"com.hth.tracuuonline.LookUpLichChieuPhimRap",""));
            menuLookUpItems.add(new MenuLookUpItem("Những Tra Cứu Khác",R.drawable.otherlookup,"com.hth.tracuuonline.LookUpForViewWithWebViewRequest","", MenuLookUpItemKind.OtherLookUp));

        }
        return menuLookUpItems;
    }


}
