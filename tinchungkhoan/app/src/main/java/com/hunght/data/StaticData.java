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

    private static ArrayList<KeyValueItem> loaiSuKien;
	private static ArrayList<String> loaiCK;
    private static ArrayList<String> thiTruong;
    private static ArrayList<MenuLookUpItem> menuLookUpItems;
    public static ArrayList<MenuLookUpItem> GetMenuLookUpItems(){
        if(menuLookUpItems == null)
        {
            menuLookUpItems = new ArrayList<>();
            menuLookUpItems.add(new MenuLookUpItem("Danh Mục Đầu Tư", R.drawable.ic_menu_gallery,"com.hunght.tinchungkhoan.DanhMucDauTuView","", MenuLookUpItemKind.DanhMucDauTu));
            menuLookUpItems.add(new MenuLookUpItem("Danh Mục Yêu Thích", R.drawable.ic_menu_gallery,"com.hunght.tinchungkhoan.FavoritesView","", MenuLookUpItemKind.DanhMucYeuThich));
            menuLookUpItems.add(new MenuLookUpItem("Dữ Liêu Mua Bán", R.drawable.ic_menu_gallery,"com.hunght.tinchungkhoan.LookUpForViewWithWebViewRequest","", MenuLookUpItemKind.DuLieuMuaBan,"http://liveboard.cafef.vn/"));
            menuLookUpItems.add(new MenuLookUpItem("Toàn Cảnh Thị Trường", R.drawable.ic_menu_gallery,"com.hunght.tinchungkhoan.ToanCanhThiTruongView","", MenuLookUpItemKind.ToanCanhThiTruong));
            menuLookUpItems.add(new MenuLookUpItem("Thực Hiện Quyền", R.drawable.ic_menu_gallery,"com.hunght.tinchungkhoan.ThucHienQuyenView","", MenuLookUpItemKind.ThucHienQuyen));
            menuLookUpItems.add(new MenuLookUpItem("Sự Kiện", R.drawable.ic_menu_gallery,"com.hunght.tinchungkhoan.SuKienView","", MenuLookUpItemKind.SuKien));
            menuLookUpItems.add(new MenuLookUpItem("Thông Tin Doanh Nghiệp", R.drawable.ic_menu_gallery,"com.hunght.tinchungkhoan.ThongTinDoanhNghiepView","", MenuLookUpItemKind.ThongTinDoanhNghiep));
            menuLookUpItems.add(new MenuLookUpItem("Cafef",R.drawable.ic_menu_camera,"com.hunght.tinchungkhoan.LookUpForViewWithWebViewRequest","", MenuLookUpItemKind.Cafef, "http://cafef.vn/", true));
            menuLookUpItems.add(new MenuLookUpItem("Vietstock",R.drawable.ic_menu_camera,"com.hunght.tinchungkhoan.LookUpForViewWithWebViewRequest","", MenuLookUpItemKind.Vietstock, "https://vietstock.vn/", true));
            menuLookUpItems.add(new MenuLookUpItem("Tin Nhanh Chứng Khoán",R.drawable.ic_menu_camera,"com.hunght.tinchungkhoan.LookUpForViewWithWebViewRequest","", MenuLookUpItemKind.TinNhanhChungKhoan,"https://tinnhanhchungkhoan.vn/", true));
            menuLookUpItems.add(new MenuLookUpItem("Báo Đầu Tư",R.drawable.ic_menu_camera,"com.hunght.tinchungkhoan.LookUpForViewWithWebViewRequest","", MenuLookUpItemKind.DauTuOnline, "https://baodautu.vn/", true));

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

    public static ArrayList<KeyValueItem> GetLoaiSuKien(){
        if(loaiSuKien == null)
        {
            loaiSuKien = new ArrayList<>();
            loaiSuKien.add(new KeyValueItem("", "Chọn tất cả"));
            loaiSuKien.add(new KeyValueItem("dividend", "Cổ tức bằng tiền"));
            loaiSuKien.add(new KeyValueItem("adpaydate", "Thay đổi ngày chi trả cổ tức"));
            loaiSuKien.add(new KeyValueItem("kinddiv", "Cổ phiếu thưởng"));
            loaiSuKien.add(new KeyValueItem("issue", "Phát hành thêm"));
            loaiSuKien.add(new KeyValueItem("stockdiv", "Cổ tức bằng cổ phiếu"));
            loaiSuKien.add(new KeyValueItem("meeting", "Họp cổ đông"));
            loaiSuKien.add(new KeyValueItem("poll", "Lấy ý kiến cổ đông"));
            loaiSuKien.add(new KeyValueItem("convert", "Chuyển đổi trái phiếu thành cổ phiếu"));
            loaiSuKien.add(new KeyValueItem("merger", "Sáp nhập"));
            loaiSuKien.add(new KeyValueItem("schedIssue", "Dự kiến phát hành"));
            loaiSuKien.add(new KeyValueItem("schedDiv", "Dự kiến cổ tức"));
            loaiSuKien.add(new KeyValueItem("schedUpdate", "Dự kiến giao dịch bổ sung"));
            loaiSuKien.add(new KeyValueItem("schedExChange", "Dự kiến chuyển sàn"));
            loaiSuKien.add(new KeyValueItem("schedDelisted", "Dự kiến hủy niêm yết"));
            loaiSuKien.add(new KeyValueItem("listedHNX", "Ngày giao dịch đầu tiên trên HNX"));
            loaiSuKien.add(new KeyValueItem("listedHOSE", "Ngày giao dịch đầu tiên trên HOSE"));
            loaiSuKien.add(new KeyValueItem("listedUPCOM", "Ngày giao dịch đầu tiên trên UPCOM"));
            loaiSuKien.add(new KeyValueItem("nomargin", "Cổ phiếu không đủ điều kiện giao dịch ký quỹ"));
            loaiSuKien.add(new KeyValueItem("noticed", "Nhắc nhở"));
            loaiSuKien.add(new KeyValueItem("alert", "Cảnh báo"));
            loaiSuKien.add(new KeyValueItem("warning", "Cảnh cáo toàn thị trường"));
            loaiSuKien.add(new KeyValueItem("control", "Kiểm soát (không hạn chế giao dịch)"));
            loaiSuKien.add(new KeyValueItem("halt", "Kiểm soát đặc biệt (hạn chế giao dịch)"));
            loaiSuKien.add(new KeyValueItem("suspend", "Tạm ngừng giao dịch"));
            loaiSuKien.add(new KeyValueItem("delistedHOSE", "Hủy niêm yết trên HOSE"));
            loaiSuKien.add(new KeyValueItem("delistedHNX", "Hủy niêm yết trên HNX"));
            loaiSuKien.add(new KeyValueItem("delistedUPCOM", "Hủy niêm yết trên UPCOM"));

        }
        return loaiSuKien;
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
