package com.hunght.data;

import com.hunght.tinchungkhoan.R;

import java.util.ArrayList;

/**
 * Created by Lenovo on 10/28/2016.
 */
public class StaticData {
    private static ArrayList<String> loaiCK;
    private static ArrayList<String> thiTruong;
    private static ArrayList<MenuLookUpItem> menuLookUpItems;
    public static ArrayList<MenuLookUpItem> GetMenuLookUpItems(){
        if(menuLookUpItems == null)
        {
            menuLookUpItems = new ArrayList<>();
            menuLookUpItems.add(new MenuLookUpItem("Danh Mục Yêu Thích", R.drawable.ic_menu_gallery,"com.hunght.tinchungkhoan.FavoritesView",""));
            menuLookUpItems.add(new MenuLookUpItem("Dữ Liêu Mua Bán", R.drawable.ic_menu_gallery,"com.hunght.tinchungkhoan.LookUpForViewWithWebViewRequest","", MenuLookUpItemKind.DuLieuMuaBan));
            menuLookUpItems.add(new MenuLookUpItem("Thực Hiện Quyền", R.drawable.ic_menu_gallery,"com.hunght.tinchungkhoan.ThucHienQuyenView",""));
            menuLookUpItems.add(new MenuLookUpItem("Thông Tin Doanh Nghiệp", R.drawable.ic_menu_gallery,"com.hunght.tinchungkhoan.ThongTinDoanhNghiepView",""));
            menuLookUpItems.add(new MenuLookUpItem("Cafef",R.drawable.ic_menu_camera,"com.hunght.tinchungkhoan.LookUpForViewWithWebViewRequest","", MenuLookUpItemKind.Cafef));

        }
        return menuLookUpItems;
    }

    public static ArrayList<String> GetLoaiChungKhoan(){
        if(loaiCK == null)
        {
            loaiCK = new ArrayList<>();
            loaiCK.add("---Tất cả---");
            loaiCK.add("Cổ phiếu");
            loaiCK.add("Trái phiếu");
            loaiCK.add("Chứng chỉ quỹ");
            loaiCK.add("Tín Phiếu");

        }
        return loaiCK;
    }

    public static String GetValueFromLoaiChungKhoan(String loai)
    {
        int index = GetLoaiChungKhoan().indexOf(loai);
        if(index == 0) return "";
        else return "" + index;
    }

    public static ArrayList<String> GetThiTruong(){
        if(thiTruong == null)
        {
            thiTruong = new ArrayList<>();
            thiTruong.add("---Tất cả---");
            thiTruong.add("Tín Phiếu");
            thiTruong.add("Đại chúng chưa niêm yết");
            thiTruong.add("Trái phiếu ngoại tệ");
            thiTruong.add("Trái phiếu chuyên biệt");
            thiTruong.add("HOSE");
            thiTruong.add("HNX");
            thiTruong.add("UpCOM");

        }
        return thiTruong;
    }

    public static String GetValueFromThiTruong(String thiTruong)
    {
        if(thiTruong == null || GetThiTruong().indexOf(thiTruong) == 0) return "";
        return thiTruong.replace(' ', '+');
    }
}
