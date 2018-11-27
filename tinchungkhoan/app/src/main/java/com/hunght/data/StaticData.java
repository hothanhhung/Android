package com.hunght.data;

import com.hunght.tinchungkhoan.R;

import java.util.ArrayList;

/**
 * Created by Lenovo on 10/28/2016.
 */
public class StaticData {
    static private ArrayList<DoanhNghiepItem> thongTinDoanhNghieps;

    public static ArrayList<DoanhNghiepItem> getThongTinDoanhNghieps() {
        return thongTinDoanhNghieps;
    }

    public static void setThongTinDoanhNghieps(ArrayList<DoanhNghiepItem> thongTinDoanhNghieps) {
        StaticData.thongTinDoanhNghieps = thongTinDoanhNghieps;
    }

    public static String getNameCongTy(String maCK) {
        if(maCK == null) maCK = "";
        if(thongTinDoanhNghieps!=null){
            for (DoanhNghiepItem thongtindoanhnghiep: thongTinDoanhNghieps) {
                if(thongtindoanhnghiep.c.equalsIgnoreCase(maCK)){
                    return thongtindoanhnghiep.m;
                }
            }
        }

        return "";
    }

    private static ArrayList<String> loaiCK;
    private static ArrayList<String> thiTruong;
    private static ArrayList<MenuLookUpItem> menuLookUpItems;
    public static ArrayList<MenuLookUpItem> GetMenuLookUpItems(){
        if(menuLookUpItems == null)
        {
            menuLookUpItems = new ArrayList<>();
            menuLookUpItems.add(new MenuLookUpItem("Danh Mục Đầu Tư", R.drawable.ic_menu_gallery,"com.hunght.tinchungkhoan.DanhMucDauTuView","", MenuLookUpItemKind.DanhMucDauTu));
            menuLookUpItems.add(new MenuLookUpItem("Danh Mục Yêu Thích", R.drawable.ic_menu_gallery,"com.hunght.tinchungkhoan.FavoritesView","", MenuLookUpItemKind.DanhMucYeuThich));
            menuLookUpItems.add(new MenuLookUpItem("Dữ Liêu Mua Bán", R.drawable.ic_menu_gallery,"com.hunght.tinchungkhoan.LookUpForViewWithWebViewRequest","", MenuLookUpItemKind.DuLieuMuaBan));
            menuLookUpItems.add(new MenuLookUpItem("Thực Hiện Quyền", R.drawable.ic_menu_gallery,"com.hunght.tinchungkhoan.ThucHienQuyenView","", MenuLookUpItemKind.ThucHienQuyen));
            menuLookUpItems.add(new MenuLookUpItem("Thông Tin Doanh Nghiệp", R.drawable.ic_menu_gallery,"com.hunght.tinchungkhoan.ThongTinDoanhNghiepView","", MenuLookUpItemKind.ThongTinDoanhNghiep));
            menuLookUpItems.add(new MenuLookUpItem("Cafef",R.drawable.ic_menu_camera,"com.hunght.tinchungkhoan.LookUpForViewWithWebViewRequest","", MenuLookUpItemKind.Cafef));
            menuLookUpItems.add(new MenuLookUpItem("Vietstock",R.drawable.ic_menu_camera,"com.hunght.tinchungkhoan.LookUpForViewWithWebViewRequest","", MenuLookUpItemKind.Vietstock));
            menuLookUpItems.add(new MenuLookUpItem("Tin Nhanh Chứng Khoán",R.drawable.ic_menu_camera,"com.hunght.tinchungkhoan.LookUpForViewWithWebViewRequest","", MenuLookUpItemKind.TinNhanhChungKhoan));
            menuLookUpItems.add(new MenuLookUpItem("Báo Đầu Tư",R.drawable.ic_menu_camera,"com.hunght.tinchungkhoan.LookUpForViewWithWebViewRequest","", MenuLookUpItemKind.DauTuOnline));

        }
        return menuLookUpItems;
    }

    public static MenuLookUpItem geMenuItemBasedOnKind(MenuLookUpItemKind kind){
        for(MenuLookUpItem menuLookUpItem : GetMenuLookUpItems())
        {
            if(menuLookUpItem.getKind() == kind){
                return menuLookUpItem;
            }
        }
        return null;
    }

    public static MenuLookUpItem geMenuItemBasedOnViewClassName(String className){
        for(MenuLookUpItem menuLookUpItem : GetMenuLookUpItems())
        {
            if(menuLookUpItem.getViewClassName().endsWith(className)){
                return menuLookUpItem;
            }
        }
        return null;
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
