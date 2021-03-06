package com.hth.lichtivi;

import java.util.ArrayList;

/**
 * Created by Lenovo on 8/26/2016.
 */
public class Data {
    static private ArrayList<ChannelGroup> channelGroups;
    public static String getChannelName(String channelKey)
    {
        for (ChannelGroup channelGroup:getChannelGroup()) {
            for (ChannelItem channelItem:channelGroup.getChannelList()) {
                if(channelKey.equalsIgnoreCase(channelItem.getId()))
                {
                    return channelItem.getName();
                }
            }
        }
        return "";
    }
    static ArrayList<ChannelGroup> getChannelGroup()
    {
        if(channelGroups == null || channelGroups.size() == 0) {
            channelGroups = new ArrayList<ChannelGroup>();

            //Đài Truyền hình Việt Nam - VTV
            ArrayList<ChannelItem> vtvGroups = new ArrayList<ChannelItem>();
            vtvGroups.add(new ChannelItem("VTV1", "VTV1"));
            vtvGroups.add(new ChannelItem("VTV2", "VTV2"));
            vtvGroups.add(new ChannelItem("VTV3", "VTV3"));
            vtvGroups.add(new ChannelItem("VTV4", "VTV4"));
            vtvGroups.add(new ChannelItem("VTV5", "VTV5"));
            vtvGroups.add(new ChannelItem("VTV6", "VTV6"));
            vtvGroups.add(new ChannelItem("VTV7", "VTV7"));
            vtvGroups.add(new ChannelItem("VTV8", "VTV8"));
            vtvGroups.add(new ChannelItem("VTV9", "VTV9"));
            channelGroups.add(new ChannelGroup("Đài Truyền hình Việt Nam - VTV", vtvGroups));

            //Đài Tiếng nói Việt Nam
            ArrayList<ChannelItem> vovGroups = new ArrayList<ChannelItem>();
            vovGroups.add(new ChannelItem("VOVTV", "VOVTV"));
            channelGroups.add(new ChannelGroup("Đài Tiếng nói Việt Nam", vovGroups));


            //Đài Tiếng nói Việt Nam
            ArrayList<ChannelItem> hanoiGroups = new ArrayList<ChannelItem>();
            hanoiGroups.add(new ChannelItem("HaNoiTV1", "HaNoiTV 1"));
            hanoiGroups.add(new ChannelItem("HaNoiTV2", "HaNoiTV 2"));
            channelGroups.add(new ChannelGroup("Đài Phát thanh và Truyền hình Hà Nội", hanoiGroups));

            //Đài Truyền hình TP Hồ Chí Minh - HTV
            ArrayList<ChannelItem> htvGroups = new ArrayList<ChannelItem>();
            htvGroups.add(new ChannelItem("HTV1", "HTV1"));
            htvGroups.add(new ChannelItem("HTV2", "HTV2"));
            htvGroups.add(new ChannelItem("HTV3", "HTV3"));
            htvGroups.add(new ChannelItem("HTV4", "HTV4"));
            htvGroups.add(new ChannelItem("HTV7", "HTV7"));
            htvGroups.add(new ChannelItem("HTV9", "HTV9"));
            channelGroups.add(new ChannelGroup("Đài Truyền hình TP Hồ Chí Minh - HTV", htvGroups));


            //Đài truyền hình An Viên AVG
            ArrayList<ChannelItem> avGroups = new ArrayList<ChannelItem>();
            avGroups.add(new ChannelItem("AmnhacNamBo", "Âm nhạc Nam Bộ"));
            avGroups.add(new ChannelItem("AnNinhTheGioi", "An Ninh Thế Giới"));
            avGroups.add(new ChannelItem("AnVien-BTV9", "An Viên - BTV9"));
            avGroups.add(new ChannelItem("Canhacthieunhi", "Ca nhạc thiếu nhi"));
            avGroups.add(new ChannelItem("DocSach", "Đọc Sách", "Doc Sach"));
            avGroups.add(new ChannelItem("MienTay-THDT2", "Miền Tây - THĐT2"));
            avGroups.add(new ChannelItem("NCM-BTV10", "NCM - BTV10"));
            avGroups.add(new ChannelItem("NhacCachMang", "Nhạc Cách Mạng"));
            avGroups.add(new ChannelItem("NhacCodien", "Nhạc Cổ điển", "nhac co dien"));
            avGroups.add(new ChannelItem("NhacDantoc", "Nhạc Dân tộc"));
            avGroups.add(new ChannelItem("NhacNhe", "Nhạc Nhẹ"));
            avGroups.add(new ChannelItem("Nhactre", "Nhạc trẻ"));
            avGroups.add(new ChannelItem("NhacTrutinh", "Nhạc Trữ tình"));
            avGroups.add(new ChannelItem("Phimhay", "Phim hay"));
            avGroups.add(new ChannelItem("SAM-BTV11", "SAM - BTV11"));
            avGroups.add(new ChannelItem("VietTeen-BTV62idol", "ViệtTeen - BTV6 2idol"));
            channelGroups.add(new ChannelGroup("Đài truyền hình An Viên AVG", avGroups));

            //Đài Truyền hình Cáp TP Hồ Chí Minh - HTVC
            ArrayList<ChannelItem> htvcGroups = new ArrayList<ChannelItem>();
            htvcGroups.add(new ChannelItem("HTVCo.op", "HTV Co.op"));
            htvcGroups.add(new ChannelItem("HTVThethao", "HTV Thể thao"));
            htvcGroups.add(new ChannelItem("HTVCCANHAC", "HTVC CA NHẠC"));
            htvcGroups.add(new ChannelItem("HTVCDuLichCuocSong", "HTVC Du Lịch Cuộc Sống"));
            htvcGroups.add(new ChannelItem("HTVCFBNC", "HTVC FBNC"));
            htvcGroups.add(new ChannelItem("HTVCGIADINH", "HTVC GIA ĐÌNH", "HTVC GIA DINH"));
            htvcGroups.add(new ChannelItem("HTVCPHIM", "HTVC PHIM"));
            htvcGroups.add(new ChannelItem("HTVCPHUNU", "HTVC PHỤ NỮ"));
            htvcGroups.add(new ChannelItem("HTVCShopping", "HTVC Shopping"));
            htvcGroups.add(new ChannelItem("HTVC+", "HTVC+"));
            htvcGroups.add(new ChannelItem("ThuanViet", "Thuần Việt"));
            channelGroups.add(new ChannelGroup("Đài Truyền hình Cáp TP Hồ Chí Minh - HTVC", htvcGroups));

            //Đài Truyền hình Cáp Việt Nam
            ArrayList<ChannelItem> vtvcabGroups = new ArrayList<ChannelItem>();
            vtvcabGroups.add(new ChannelItem("VTVcab1-GiaitriTV", "VTVcab 1 - Giải trí TV"));
            vtvcabGroups.add(new ChannelItem("VTVcab2-PhimViet", "VTVcab 2 - Phim Việt"));
            vtvcabGroups.add(new ChannelItem("VTVcab3-TheThaoTV", "VTVcab 3 - Thể Thao TV"));
            vtvcabGroups.add(new ChannelItem("VTVcab4-Vanhoa", "VTVcab 4 - Văn hóa"));
            vtvcabGroups.add(new ChannelItem("VTVcab5-Echannel", "VTVcab 5 - E channel"));
            vtvcabGroups.add(new ChannelItem("VTVcab6-HayTV", "VTVcab 6 - Hay TV"));
            vtvcabGroups.add(new ChannelItem("VTVcab7-DDramas", "VTVcab 7 - D Dramas"));
            vtvcabGroups.add(new ChannelItem("VTVcab8-BiBi", "VTVcab 8 - BiBi"));
            vtvcabGroups.add(new ChannelItem("VTVcab9-INFOTV", "VTVcab 9 - INFO TV"));
            vtvcabGroups.add(new ChannelItem("VTVcab10-O2TV", "VTVcab 10 - O2TV"));
            vtvcabGroups.add(new ChannelItem("VTVcab11-TVShopping", "VTVcab 11 - TV Shopping"));
            vtvcabGroups.add(new ChannelItem("VTVcab12-StyleTV", "VTVcab 12 - Style TV"));
            vtvcabGroups.add(new ChannelItem("VTVcab14-LotteShopping", "VTVcab 14 - Lotte Shopping"));
            vtvcabGroups.add(new ChannelItem("VTVcab15-InvestTV", "VTVcab 15 - Invest TV"));
            vtvcabGroups.add(new ChannelItem("VTVcab16-BongdaTV", "VTVcab 16 - Bóng đá TV", "VTVcab 16 - Bong da TV"));
            vtvcabGroups.add(new ChannelItem("VTVcab17-Yeah1TV", "VTVcab 17 - Yeah1 TV "));
            vtvcabGroups.add(new ChannelItem("VTVcab19-KenhPhim", "VTVcab 19 - Kênh Phim "));
            vtvcabGroups.add(new ChannelItem("VTVcab20-VFamily", "VTVcab 20 - V Family"));
            vtvcabGroups.add(new ChannelItem("VTVcab21", "VTVcab 21"));
            vtvcabGroups.add(new ChannelItem("MTVVietNam", "MTV Việt Nam"));
            channelGroups.add(new ChannelGroup("Đài Truyền hình Cáp Việt Nam", vtvcabGroups));

            //Đài truyền hình cáp Hà Nội
            ArrayList<ChannelItem> hanoicabGroups = new ArrayList<ChannelItem>();
            hanoicabGroups.add(new ChannelItem("HCaTV", "HCaTV"));
            hanoicabGroups.add(new ChannelItem("HiTV", "HiTV"));
            hanoicabGroups.add(new ChannelItem("MOV", "MOV"));
            hanoicabGroups.add(new ChannelItem("YOUTV", "YOUTV"));
            channelGroups.add(new ChannelGroup("Đài truyền hình cáp Hà Nội", hanoicabGroups));

            //Đài truyền hình cáp Sài Gòn
            ArrayList<ChannelItem> sctvcabGroups = new ArrayList<ChannelItem>();
            sctvcabGroups.add(new ChannelItem("SCTV1", "SCTV1"));
            sctvcabGroups.add(new ChannelItem("SCTV2-YanTV", "SCTV2 - Yan TV"));
            sctvcabGroups.add(new ChannelItem("SCTV3-SaoTV", "SCTV3 - Sao TV"));
            sctvcabGroups.add(new ChannelItem("SCTV4", "SCTV4"));
            sctvcabGroups.add(new ChannelItem("SCTV5-SCJTVShopping", "SCTV5 - SCJ TV Shopping"));
            sctvcabGroups.add(new ChannelItem("SCTV6-SNTV", "SCTV6 - SNTV"));
            sctvcabGroups.add(new ChannelItem("SCTV7", "SCTV7"));
            sctvcabGroups.add(new ChannelItem("SCTV8-VITV", "SCTV8 - VITV"));
            sctvcabGroups.add(new ChannelItem("SCTV9", "SCTV9"));
            sctvcabGroups.add(new ChannelItem("SCTV10-HomeshoppingNetwork", "SCTV10 - Home shopping Network"));
            sctvcabGroups.add(new ChannelItem("SCTV11", "SCTV11"));
            sctvcabGroups.add(new ChannelItem("SCTV12", "SCTV12"));
            sctvcabGroups.add(new ChannelItem("SCTV13-TVF", "SCTV13 - TVF"));
            sctvcabGroups.add(new ChannelItem("SCTV14", "SCTV14"));
            sctvcabGroups.add(new ChannelItem("SCTV15", "SCTV15"));
            sctvcabGroups.add(new ChannelItem("SCTV16", "SCTV16"));
            sctvcabGroups.add(new ChannelItem("SCTVHDThethao", "SCTV HD Thể thao"));
            sctvcabGroups.add(new ChannelItem("SCTVPhimtonghop", "SCTV Phim tổng hợp"));
            channelGroups.add(new ChannelGroup("Đài truyền hình cáp Sài Gòn", sctvcabGroups));

            //Đài truyền hình số VTC
            ArrayList<ChannelItem> vtcGroups = new ArrayList<ChannelItem>();
            vtcGroups.add(new ChannelItem("VTC1", "VTC1"));
            vtcGroups.add(new ChannelItem("VTCHD1", "VTC HD1"));
            vtcGroups.add(new ChannelItem("VTC2", "VTC2"));
            vtcGroups.add(new ChannelItem("VTCHD2", "VTC HD2"));
            vtcGroups.add(new ChannelItem("VTC3", "VTC3"));
            vtcGroups.add(new ChannelItem("VTCHD3", "VTC HD3"));
            vtcGroups.add(new ChannelItem("VTC4-Yeah1Family", "VTC4 - Yeah1 Family"));
            vtcGroups.add(new ChannelItem("VTC5", "VTC5"));
            vtcGroups.add(new ChannelItem("VTC6", "VTC6"));
            vtcGroups.add(new ChannelItem("VTC7-TodayTV", "VTC7 - Today TV"));
            vtcGroups.add(new ChannelItem("VTC8-VIEWTV", "VTC8 - VIEW TV"));
            vtcGroups.add(new ChannelItem("VTC9-LETSVIET", "VTC9 - LETS VIỆT"));
            vtcGroups.add(new ChannelItem("VTC10-NetViet", "VTC10 - NetViet"));
            vtcGroups.add(new ChannelItem("VTC11-Kids\u0026FamilyTV", "VTC11 - Kids \u0026 Family TV"));
            vtcGroups.add(new ChannelItem("VTC12", "VTC12"));
            vtcGroups.add(new ChannelItem("VTC13-iTV", "VTC13 - iTV"));
            vtcGroups.add(new ChannelItem("VTC14", "VTC14"));
            vtcGroups.add(new ChannelItem("VTC16-3NTV", "VTC16-3NTV"));
            channelGroups.add(new ChannelGroup("Đài truyền hình số VTC", vtcGroups));

            //Truyền hình số vệ tinh k+
            ArrayList<ChannelItem> kplugGroups = new ArrayList<ChannelItem>();
            kplugGroups.add(new ChannelItem("K+PM", "K+PM"));
            kplugGroups.add(new ChannelItem("K+1", "K+1"));
            kplugGroups.add(new ChannelItem("K+NS", "K+NS"));
            kplugGroups.add(new ChannelItem("K+PC", "K+ PC"));
            channelGroups.add(new ChannelGroup("Truyền hình số vệ tinh k+", kplugGroups));

            //Đài TH địa phương
            ArrayList<ChannelItem> dpGroups = new ArrayList<ChannelItem>();
            dpGroups.add(new ChannelItem("TruyenHinhAnGiangATV", "Truyền Hình An Giang ATV"));
            dpGroups.add(new ChannelItem("TruyenhinhBaRia-VungTauBRT", "Truyền hình Bà Rịa - Vũng Tàu BRT"));
            dpGroups.add(new ChannelItem("TruyenhinhBacGiangBGTV", "Truyền hình Bắc Giang BGTV"));
            dpGroups.add(new ChannelItem("TruyenhinhBacLieuBTV", "Truyền hình Bạc Liêu BTV"));
            dpGroups.add(new ChannelItem("TruyenhinhBacNinhBNTV", "Truyền hình Bắc Ninh BNTV"));
            dpGroups.add(new ChannelItem("TruyenHinhBenTreTHBT", "Truyền Hình Bến Tre THBT"));
            dpGroups.add(new ChannelItem("TruyenhinhBinhDinhBTV", "Truyền hình Bình Định BTV", "Truyen hinh Binh Dinh BTV"));
            dpGroups.add(new ChannelItem("TruyenhinhBinhDuongBTV1", "Truyền hình Bình Dương BTV1"));
            dpGroups.add(new ChannelItem("TruyenhinhBinhDuongBTV2", "Truyền hình Bình Dương BTV2"));
            dpGroups.add(new ChannelItem("TruyenhinhBinhDuongBTV3", "Truyền hình Bình Dương BTV3"));
            dpGroups.add(new ChannelItem("TruyenhinhBinhDuongBTV4", "Truyền hình Bình Dương BTV4"));
            dpGroups.add(new ChannelItem("TruyenHinhBinhPhuocBPTV1", "Truyền Hình Bình Phước BPTV1"));
            dpGroups.add(new ChannelItem("TruyenhinhBinhPhuocBPTV2", "Truyền hình Bình Phước BPTV2"));
            dpGroups.add(new ChannelItem("TruyenHinhBinhThuanBTV", "Truyền Hình Bình Thuận BTV"));
            dpGroups.add(new ChannelItem("TruyenhinhCaMauCTV", "Truyền hình Cà Mau CTV"));
            dpGroups.add(new ChannelItem("TruyenhinhCanThoTHTPCT", "Truyền hình Cần Thơ THTPCT"));
            dpGroups.add(new ChannelItem("TruyenhinhCaoBangCRTV", "Truyền hình Cao Bằng CRTV"));
            dpGroups.add(new ChannelItem("TruyenhinhCongAnNhanDanANTV", "Truyền hình Công An Nhân Dân ANTV"));
            dpGroups.add(new ChannelItem("TruyenhinhDaNangDRT1", "Truyền hình Đà Nẵng DRT1", "Truyen hinh Da Nang DRT1"));
            dpGroups.add(new ChannelItem("TruyenhinhDaNangDRT2", "Truyền hình Đà Nẵng DRT2", "Truyen hinh Da Nang DRT2"));
            dpGroups.add(new ChannelItem("TruyenhinhDakLakDRT", "Truyền hình Đăk Lăk DRT", "Truyen hinh Dak Lak DRT"));
            dpGroups.add(new ChannelItem("TruyenhinhDakNongPTD", "Truyền hình Đắk Nông PTD", "Truyen hinh Dak Nong PTD"));
            dpGroups.add(new ChannelItem("TruyenhinhDienBienDBTV", "Truyền hình Điện Biên ĐBTV", "Truyen hinh Dien Bien DBTV"));
            dpGroups.add(new ChannelItem("TruyenhinhDongNaiDN1", "Truyền hình Đồng Nai DN1", "Truyen hinh Dong Nai DN1"));
            dpGroups.add(new ChannelItem("TruyenhinhDongNaiDN2", "Truyền hình Đồng Nai DN2", "Truyen hinh Dong Nai DN2"));
            dpGroups.add(new ChannelItem("TruyenhinhDongThapTHDT", "Truyền hình Đồng Tháp THĐT", "Truyen hinh Dong Thap THDT"));
            dpGroups.add(new ChannelItem("TruyenhinhGiaLaiTHGL", "Truyền hình Gia Lai THGL"));
            dpGroups.add(new ChannelItem("TruyenhinhHaGiangHTV", "Truyền hình Hà Giang HTV"));
            dpGroups.add(new ChannelItem("TruyenhinhHaNam", "Truyền hình Hà Nam "));
            dpGroups.add(new ChannelItem("TruyenhinhHaTinhHTTV", "Truyền hình Hà Tĩnh HTTV"));
            dpGroups.add(new ChannelItem("TruyenhinhHaiDuong", "Truyền hình Hải Dương"));
            dpGroups.add(new ChannelItem("TruyenhinhHaiPhongTHP", "Truyền hình Hải Phòng THP"));
            dpGroups.add(new ChannelItem("TruyenHinhHauGiangHGTV", "Truyền Hình Hậu Giang HGTV"));
            dpGroups.add(new ChannelItem("TruyenhinhHoaBinhHBTV", "Truyền hình Hòa Bình HBTV"));
            dpGroups.add(new ChannelItem("TruyenhinhHungYenHY", "Truyền hình Hưng Yên HY"));
            dpGroups.add(new ChannelItem("TruyenhinhKhanhHoaKTV", "Truyền hình Khánh Hòa KTV"));
            dpGroups.add(new ChannelItem("TruyenhinhKienGiang", "Truyền hình Kiên Giang"));
            dpGroups.add(new ChannelItem("TruyenhinhKonTumKRT", "Truyền hình Kon Tum KRT"));
            dpGroups.add(new ChannelItem("TruyenhinhLaiChauLTV", "Truyền hình Lai Châu LTV"));
            dpGroups.add(new ChannelItem("TruyenhinhLamDongLTV", "Truyền hình Lâm Đồng LTV", "Truyen hinh Lam Dong LTV"));
            dpGroups.add(new ChannelItem("TruyenhinhLangSonLSTV", "Truyền hình Lạng Sơn LSTV"));
            dpGroups.add(new ChannelItem("TruyenhinhLaoCaiTHLC", "Truyền hình Lào Cai THLC"));
            dpGroups.add(new ChannelItem("TruyenHinhLongAnLA34", "Truyền Hình Long An LA34"));
            dpGroups.add(new ChannelItem("TruyenhinhNamDinhNTV", "Truyền hình Nam Định NTV", "Truyen hinh Nam Dinh NTV"));
            dpGroups.add(new ChannelItem("TruyenhinhNgheAnNTV", "Truyền hình Nghệ An NTV"));
            dpGroups.add(new ChannelItem("TruyenhinhNhanDan", "Truyền hình Nhân Dân"));
            dpGroups.add(new ChannelItem("TruyenhinhNinhBinhNTV", "Truyền hình Ninh Bình NTV"));
            dpGroups.add(new ChannelItem("TruyenhinhNinhThuanNTV", "Truyền hình Ninh Thuận NTV"));
            dpGroups.add(new ChannelItem("TruyenhinhPhuThoPTV", "Truyền hình Phú Thọ PTV"));
            dpGroups.add(new ChannelItem("TruyenhinhQuangBinhQBTV", "Truyền hình Quảng Bình QBTV"));
            dpGroups.add(new ChannelItem("TruyenhinhQuangNamQRT", "Truyền hình Quảng Nam QRT"));
            dpGroups.add(new ChannelItem("TruyenhinhQuangNgaiPTQ", "Truyền hình Quảng Ngãi PTQ"));
            dpGroups.add(new ChannelItem("TruyenhinhQuangNinhQTV1", "Truyền hình Quảng Ninh QTV1"));
            dpGroups.add(new ChannelItem("TruyenhinhQuangNinhQTV3", "Truyền hình Quảng Ninh QTV3"));
            dpGroups.add(new ChannelItem("TruyenhinhQuangTriQTV", "Truyền hình Quảng Trị QTV"));
            dpGroups.add(new ChannelItem("TruyenhinhQuocHoi", "Truyền hình Quốc Hội"));
            dpGroups.add(new ChannelItem("TruyenhinhQuocPhongQPVN", "Truyền hình Quốc Phòng QPVN"));
            dpGroups.add(new ChannelItem("TruyenhinhSocTrangSTV", "Truyền hình Sóc Trăng STV"));
            dpGroups.add(new ChannelItem("TruyenhinhSonLaSTV", "Truyền hình Sơn La STV"));
            dpGroups.add(new ChannelItem("TruyenhinhTayNinhTTV", "Truyền hình Tây Ninh TTV"));
            dpGroups.add(new ChannelItem("TruyenhinhThaiBinhTBTV", "Truyền hình Thái Bình TBTV"));
            dpGroups.add(new ChannelItem("TruyenhinhThaiNguyenTV1", "Truyền hình Thái Nguyên TV1"));
            dpGroups.add(new ChannelItem("TruyenhinhThanhHoaTTV", "Truyền hình Thanh Hóa TTV"));
            dpGroups.add(new ChannelItem("TruyenhinhThongtanxaVietNamTTXVN", "Truyền hình Thông tấn xã Việt Nam TTXVN"));
            dpGroups.add(new ChannelItem("TruyenHinhThuaThienHueTRT", "Truyền Hình Thừa Thiên Huế TRT"));
            dpGroups.add(new ChannelItem("TruyenHinhTienGiangTHTG", "Truyền Hình Tiền Giang THTG"));
            dpGroups.add(new ChannelItem("TruyenhinhTraVinhTHTV", "Truyền hình Trà Vinh THTV"));
            dpGroups.add(new ChannelItem("TruyenhinhTuyenQuangTTV", "Truyền hình Tuyên Quang TTV"));
            dpGroups.add(new ChannelItem("TruyenHinhVinhLongTHVL1", "Truyền Hình Vĩnh Long THVL1"));
            dpGroups.add(new ChannelItem("TruyenhinhVinhLongTHVL2", "Truyền hình Vĩnh Long THVL2"));
            dpGroups.add(new ChannelItem("TruyenhinhVinhPhucVP", "Truyền hình Vĩnh Phúc VP"));
            dpGroups.add(new ChannelItem("TruyenhinhYenBaiYTV", "Truyền hình Yên Bái YTV"));
            dpGroups.add(new ChannelItem("VietNamNet", "VietNamNet"));
            channelGroups.add(new ChannelGroup("Đài TH địa phương", dpGroups));

            //Kênh TH quốc tế
            ArrayList<ChannelItem> qtGroups = new ArrayList<ChannelItem>();
            qtGroups.add(new ChannelItem("(HD)mioStadium102", "(HD) mioStadium 102"));
            qtGroups.add(new ChannelItem("(HD)mioStadium103", "(HD) mioStadium 103"));
            qtGroups.add(new ChannelItem("(HD)mioStadium104", "(HD) mioStadium 104"));
            qtGroups.add(new ChannelItem("(HD)mioStadium105", "(HD) mioStadium 105"));
            qtGroups.add(new ChannelItem("(HD)mioStadium106", "(HD) mioStadium 106"));
            qtGroups.add(new ChannelItem("[V]China", "[V] China"));
            qtGroups.add(new ChannelItem("[V]Taiwan", "[V] Taiwan"));
            qtGroups.add(new ChannelItem("AllSportsNetwork", "All Sports Network"));
            qtGroups.add(new ChannelItem("AnHuy-TrungQuoc", "An Huy - Trung Quốc"));
            qtGroups.add(new ChannelItem("ANC", "ANC"));
            qtGroups.add(new ChannelItem("AnimalPlanet", "Animal Planet"));
            qtGroups.add(new ChannelItem("Animax", "Animax"));
            qtGroups.add(new ChannelItem("ANIPLUSHD", "ANIPLUS HD"));
            qtGroups.add(new ChannelItem("ArirangTV", "Arirang TV"));
            qtGroups.add(new ChannelItem("ASIANEWS", "ASIA NEWS"));
            qtGroups.add(new ChannelItem("AsiaTravel", "Asia Travel"));
            qtGroups.add(new ChannelItem("AsianFoodChannel", "Asian Food Channel"));
            qtGroups.add(new ChannelItem("Asianet", "Asianet"));
            qtGroups.add(new ChannelItem("AustraliaNetwork", "Australia Network"));
            qtGroups.add(new ChannelItem("AXN", "AXN"));
            qtGroups.add(new ChannelItem("BabyTV", "BabyTV"));
            qtGroups.add(new ChannelItem("BBC", "BBC"));
            qtGroups.add(new ChannelItem("BBCEntertainment", "BBC Entertainment"));
            qtGroups.add(new ChannelItem("BBCKnowledge", "BBC Knowledge"));
            qtGroups.add(new ChannelItem("BBCLifestyle", "BBC Lifestyle"));
            qtGroups.add(new ChannelItem("BBCWorldNews", "BBC World News"));
            qtGroups.add(new ChannelItem("beTV", "beTV"));
            qtGroups.add(new ChannelItem("BLOOMBERG", "BLOOMBERG"));
            qtGroups.add(new ChannelItem("CartoonNetworks", "Cartoon Networks"));
            qtGroups.add(new ChannelItem("CBeebies", "CBeebies"));
            qtGroups.add(new ChannelItem("CBN", "CBN"));
            qtGroups.add(new ChannelItem("CCTVHD", "CCTV HD"));
            qtGroups.add(new ChannelItem("CCTVNews", "CCTV News"));
            qtGroups.add(new ChannelItem("CCTV-4", "CCTV-4"));
            qtGroups.add(new ChannelItem("CelestialClassicMovies", "Celestial Classic Movies"));
            qtGroups.add(new ChannelItem("CelestialMovies", "Celestial Movies"));
            qtGroups.add(new ChannelItem("Channel[V]", "Channel [V]"));
            qtGroups.add(new ChannelItem("Channel[V]India", "Channel [V] India"));
            qtGroups.add(new ChannelItem("Channeli", "Channel i"));
            qtGroups.add(new ChannelItem("channelM", "channel M"));
            qtGroups.add(new ChannelItem("ChietGiangTV", "Chiết Giang TV"));
            qtGroups.add(new ChannelItem("CinemaOneGlobal", "Cinema One Global"));
            qtGroups.add(new ChannelItem("CinemaWorldHD", "CinemaWorld HD"));
            qtGroups.add(new ChannelItem("CINEMAX", "CINEMAX"));
            qtGroups.add(new ChannelItem("CNBC", "CNBC"));
            qtGroups.add(new ChannelItem("CNNHEADLINES", "CNN HEADLINES"));
            qtGroups.add(new ChannelItem("COLORS", "COLORS"));
            qtGroups.add(new ChannelItem("ComedyCentralAsiaHD", "Comedy Central Asia HD"));
            qtGroups.add(new ChannelItem("Crime\u0026InvestigationNetworkHD", "Crime \u0026 Investigation Network HD"));
            qtGroups.add(new ChannelItem("CTITV", "CTI TV"));
            qtGroups.add(new ChannelItem("DaVinci", "Da Vinci"));
            qtGroups.add(new ChannelItem("DDNATIONAL", "DD NATIONAL"));
            qtGroups.add(new ChannelItem("DISCOVERY", "DISCOVERY"));
            qtGroups.add(new ChannelItem("DiscoveryChannel", "Discovery Channel"));
            qtGroups.add(new ChannelItem("DiscoveryKids", "Discovery Kids"));
            qtGroups.add(new ChannelItem("DiscoveryScience", "Discovery Science"));
            qtGroups.add(new ChannelItem("DiscoveryWorldHD", "Discovery World HD"));
            qtGroups.add(new ChannelItem("DisneyChannel", "Disney Channel"));
            qtGroups.add(new ChannelItem("DisneyJunior", "Disney Junior"));
            qtGroups.add(new ChannelItem("DIVAUniversal", "DIVA Universal"));
            qtGroups.add(new ChannelItem("DMAX", "DMAX"));
            qtGroups.add(new ChannelItem("DragonTV", "Dragon TV"));
            qtGroups.add(new ChannelItem("DW", "DW"));
            qtGroups.add(new ChannelItem("DW-TVAsia+", "DW-TV Asia+"));
            qtGroups.add(new ChannelItem("ECity", "E City"));
            qtGroups.add(new ChannelItem("ECity(+2)", "E City (+2)"));
            qtGroups.add(new ChannelItem("E!Entertainment", "E! Entertainment"));
            qtGroups.add(new ChannelItem("ESPN", "ESPN"));
            qtGroups.add(new ChannelItem("Eurosport", "Eurosport"));
            qtGroups.add(new ChannelItem("Eurosportnews", "Eurosportnews"));
            qtGroups.add(new ChannelItem("EVE", "EVE"));
            qtGroups.add(new ChannelItem("FashionTV", "Fashion TV"));
            qtGroups.add(new ChannelItem("FIFAWorldCup(TM)1", "FIFA World Cup (TM) 1"));
            qtGroups.add(new ChannelItem("FIFAWorldCup(TM)2", "FIFA World Cup (TM) 2"));
            qtGroups.add(new ChannelItem("FoodNetworkAsia", "Food Network Asia"));
            qtGroups.add(new ChannelItem("FootballChannel", "Football Channel"));
            qtGroups.add(new ChannelItem("FOX", "FOX"));
            qtGroups.add(new ChannelItem("FOXActionMoviesHD", "FOX Action Movies HD"));
            qtGroups.add(new ChannelItem("FOXFamilyMoviesHD", "FOX Family Movies HD"));
            qtGroups.add(new ChannelItem("FOXMoviesPremiumHD", "FOX Movies Premium HD"));
            qtGroups.add(new ChannelItem("FOXNewsChannel", "FOX News Channel"));
            qtGroups.add(new ChannelItem("FoxSport", "Fox Sport"));
            qtGroups.add(new ChannelItem("FoxSport2", "Fox Sport 2"));
            qtGroups.add(new ChannelItem("FOXSPORTSNEWS", "FOX SPORTS NEWS"));
            qtGroups.add(new ChannelItem("FOXSPORTSPLUS(HD)", "FOX SPORTS PLUS (HD)"));
            qtGroups.add(new ChannelItem("FOXCRIME", "FOXCRIME"));
            qtGroups.add(new ChannelItem("FX", "FX"));
            qtGroups.add(new ChannelItem("GiangToTV", "Giang Tô TV"));
            qtGroups.add(new ChannelItem("GINX(HD)", "GINX (HD)"));
            qtGroups.add(new ChannelItem("GMMChannel", "GMM Channel"));
            qtGroups.add(new ChannelItem("GolfChannel", "Golf Channel"));
            qtGroups.add(new ChannelItem("H2HD", "H2 HD"));
            qtGroups.add(new ChannelItem("HBO", "HBO"));
            qtGroups.add(new ChannelItem("HBOFamily", "HBO Family"));
            qtGroups.add(new ChannelItem("HBOHits", "HBO Hits"));
            qtGroups.add(new ChannelItem("HBOSignature", "HBO Signature"));
            qtGroups.add(new ChannelItem("Hello!Japan", "Hello! Japan"));
            qtGroups.add(new ChannelItem("HISTORYHD", "HISTORY HD"));
            qtGroups.add(new ChannelItem("HITSHD", "HITS HD"));
            qtGroups.add(new ChannelItem("HoNamTV", "Hồ Nam TV"));
            qtGroups.add(new ChannelItem("HorseRacingCh88", "Horse Racing Ch88"));
            qtGroups.add(new ChannelItem("HorseRacingCh89", "Horse Racing Ch89"));
            qtGroups.add(new ChannelItem("iConcerts", "iConcerts"));
            qtGroups.add(new ChannelItem("JAJA TV", "JAJA TV"));
            qtGroups.add(new ChannelItem("JiangsuSatelliteChannel", "Jiangsu Satellite Channel"));
            qtGroups.add(new ChannelItem("KBSWorld", "KBS World"));
            qtGroups.add(new ChannelItem("KBS2", "KBS2"));
            qtGroups.add(new ChannelItem("KisCo", "KisCo"));
            qtGroups.add(new ChannelItem("KIX", "KIX"));
            qtGroups.add(new ChannelItem("KMTV", "KMTV"));
            qtGroups.add(new ChannelItem("LifeOK", "Life OK"));
            qtGroups.add(new ChannelItem("LifetimeHD", "Lifetime HD"));
            qtGroups.add(new ChannelItem("LUXEHD", "LUXE HD"));
            qtGroups.add(new ChannelItem("MBC", "MBC"));
            qtGroups.add(new ChannelItem("mioStadiumMultiview101", "mioStadium Multiview 101"));
            qtGroups.add(new ChannelItem("MNCBusiness", "MNC Business"));
            qtGroups.add(new ChannelItem("MNCInternational", "MNC International"));
            qtGroups.add(new ChannelItem("momokidsAsia", "momokids Asia"));
            qtGroups.add(new ChannelItem("MTV", "MTV"));
            qtGroups.add(new ChannelItem("MTVChina", "MTV China"));
            qtGroups.add(new ChannelItem("MTVLIVEHD", "MTV LIVE HD"));
            qtGroups.add(new ChannelItem("MTVS.E.A", "MTV S.E.A"));
            qtGroups.add(new ChannelItem("NatGeoMusic", "Nat Geo Music"));
            qtGroups.add(new ChannelItem("NatGeoPeople", "Nat Geo People"));
            qtGroups.add(new ChannelItem("NatGeoWild", "Nat Geo Wild"));
            qtGroups.add(new ChannelItem("NATIONALGEOGRAPHICCHANNEL", "NATIONAL GEOGRAPHIC CHANNEL"));
            qtGroups.add(new ChannelItem("NaverTVcast", "Naver TVcast"));
            qtGroups.add(new ChannelItem("NBATV", "NBA TV"));
            qtGroups.add(new ChannelItem("NDTV24x7", "NDTV 24x7"));
            qtGroups.add(new ChannelItem("NDTVGoodTimes", "NDTV Good Times"));
            qtGroups.add(new ChannelItem("NHKWORLD", "NHK WORLD"));
            qtGroups.add(new ChannelItem("NHKWorldPremium", "NHK World Premium"));
            qtGroups.add(new ChannelItem("NickJr", "Nick Jr"));
            qtGroups.add(new ChannelItem("Nickelodeon", "Nickelodeon"));
            qtGroups.add(new ChannelItem("nowBaoguMovies", "now Baogu Movies"));
            qtGroups.add(new ChannelItem("nowHairunomneon", "now Hairunomneon"));
            qtGroups.add(new ChannelItem("nowMango", "now Mango"));
            qtGroups.add(new ChannelItem("ONE", "ONE"));
            qtGroups.add(new ChannelItem("ONEHD", "ONE HD"));
            qtGroups.add(new ChannelItem("OPT1", "OPT1"));
            qtGroups.add(new ChannelItem("OutdoorChannel(HD)", "Outdoor Channel (HD)"));
            qtGroups.add(new ChannelItem("PhoenixChineseChannel", "Phoenix Chinese Channel"));
            qtGroups.add(new ChannelItem("PhoenixInfoNewsChannel", "Phoenix InfoNews Channel"));
            qtGroups.add(new ChannelItem("PLAYHOUSE", "PLAY HOUSE"));
            qtGroups.add(new ChannelItem("RacquetChannel", "Racquet Channel"));
            qtGroups.add(new ChannelItem("RAI", "RAI"));
            qtGroups.add(new ChannelItem("RedCard", "Red Card"));
            qtGroups.add(new ChannelItem("RTLCBSExtremeHD", "RTL CBS Extreme HD"));
            qtGroups.add(new ChannelItem("RTM1", "RTM1"));
            qtGroups.add(new ChannelItem("RussiaToday", "Russia Today"));
            qtGroups.add(new ChannelItem("SABTV", "SAB TV"));
            qtGroups.add(new ChannelItem("SBS", "SBS"));
            qtGroups.add(new ChannelItem("Sensasi", "Sensasi"));
            qtGroups.add(new ChannelItem("SetantaSports", "Setanta Sports"));
            qtGroups.add(new ChannelItem("ShenZhouNewsChannel", "Shen Zhou News Channel"));
            qtGroups.add(new ChannelItem("SKYNEWS", "SKY NEWS"));
            qtGroups.add(new ChannelItem("SonyEntertainmentTelevision", "Sony Entertainment Television"));
            qtGroups.add(new ChannelItem("SONYMax", "SONY Max"));
            qtGroups.add(new ChannelItem("SportsHD", "Sports HD"));
            qtGroups.add(new ChannelItem("STARChineseChannel", "STAR Chinese Channel"));
            qtGroups.add(new ChannelItem("STARChineseMovies", "STAR Chinese Movies"));
            qtGroups.add(new ChannelItem("STARChineseMoviesLegend", "STAR Chinese Movies Legend"));
            qtGroups.add(new ChannelItem("STARCricket", "STAR Cricket"));
            qtGroups.add(new ChannelItem("STARGold", "STAR Gold"));
            qtGroups.add(new ChannelItem("STARPlus", "STAR Plus"));
            qtGroups.add(new ChannelItem("STARSports", "STAR Sports"));
            qtGroups.add(new ChannelItem("StarMovies", "StarMovies"));
            qtGroups.add(new ChannelItem("StarWorld", "StarWorld"));
            qtGroups.add(new ChannelItem("SunMusic", "Sun Music"));
            qtGroups.add(new ChannelItem("SUNTV", "SUN TV"));
            qtGroups.add(new ChannelItem("SuperSports", "SuperSports"));
            qtGroups.add(new ChannelItem("SuperSportsArena", "SuperSports Arena"));
            qtGroups.add(new ChannelItem("SuperSportsPlus", "SuperSports Plus"));
            qtGroups.add(new ChannelItem("Syfy", "Syfy"));
            qtGroups.add(new ChannelItem("TCM", "TCM"));
            qtGroups.add(new ChannelItem("TenCricket", "Ten Cricket"));
            qtGroups.add(new ChannelItem("TheBiographyChannelHD", "The Biography Channel HD"));
            qtGroups.add(new ChannelItem("TheFilipinoChannel", "The Filipino Channel"));
            qtGroups.add(new ChannelItem("Thrill", "Thrill"));
            qtGroups.add(new ChannelItem("Toonami", "Toonami"));
            qtGroups.add(new ChannelItem("TRAVEL\u0026LIVING", "TRAVEL \u0026 LIVING"));
            qtGroups.add(new ChannelItem("TravelChannelHD", "Travel Channel HD"));
            qtGroups.add(new ChannelItem("TrueVisionSport1", "True Vision Sport 1"));
            qtGroups.add(new ChannelItem("truTV", "truTV"));
            qtGroups.add(new ChannelItem("TurnerClassicMovies", "Turner Classic Movies"));
            qtGroups.add(new ChannelItem("TV4Me", "TV4Me"));
            qtGroups.add(new ChannelItem("TV5Monde", "TV5 Monde"));
            qtGroups.add(new ChannelItem("TVB8", "TVB 8"));
            qtGroups.add(new ChannelItem("TVBClassic", "TVB Classic"));
            qtGroups.add(new ChannelItem("TVBXingHe", "TVB Xing He"));
            qtGroups.add(new ChannelItem("TVBJ", "TVBJ"));
            qtGroups.add(new ChannelItem("TVBSAsia", "TVBS Asia"));
            qtGroups.add(new ChannelItem("TVBS-NEWS", "TVBS-NEWS"));
            qtGroups.add(new ChannelItem("TVE", "TVE"));
            qtGroups.add(new ChannelItem("TVN", "TVN"));
            qtGroups.add(new ChannelItem("UniversalChannel", "Universal Channel"));
            qtGroups.add(new ChannelItem("VThamizh", "V Thamizh"));
            qtGroups.add(new ChannelItem("Vannathirai", "Vannathirai"));
            qtGroups.add(new ChannelItem("Verna", "Verna"));
            qtGroups.add(new ChannelItem("VIJAY", "VIJAY"));
            qtGroups.add(new ChannelItem("VVDrama", "VV Drama"));
            qtGroups.add(new ChannelItem("VVDrama(+3)", "VV Drama (+3)"));
            qtGroups.add(new ChannelItem("WarnerTV", "WarnerTV"));
            qtGroups.add(new ChannelItem("XINGKONG", "XING KONG"));
            qtGroups.add(new ChannelItem("YTN", "YTN"));
            qtGroups.add(new ChannelItem("ZeeCinema", "Zee Cinema"));
            qtGroups.add(new ChannelItem("ZeeKhanaKhazana", "Zee Khana Khazana"));
            qtGroups.add(new ChannelItem("ZeeNews", "Zee News"));
            qtGroups.add(new ChannelItem("ZeeTamizh", "Zee Tamizh"));
            qtGroups.add(new ChannelItem("ZeeTV", "Zee TV"));
            qtGroups.add(new ChannelItem("ZooMooHD", "ZooMoo HD"));
            channelGroups.add(new ChannelGroup("Kênh TH quốc tế", qtGroups));
        }
        return channelGroups;
    }
}
