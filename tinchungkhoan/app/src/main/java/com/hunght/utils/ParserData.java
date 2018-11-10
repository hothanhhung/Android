package com.hunght.utils;

import android.os.StrictMode;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hunght.data.MenuLookUpItemKind;
import com.hunght.data.ThucHienQuyenItem;
import com.hunght.tinchungkhoan.ThucHienQuyenView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ParserData {
    
	public static String getContent(MenuLookUpItemKind kind) {

        switch (kind)
        {
            case Cafef:
                return "Cafef";
        }
        return "Không xác định được dữ liêu";
    }

    public static ArrayList<ThucHienQuyenItem> getThucHienQuyenItems(String search, String market, String stockType, String start, String end)
    {
   //     String link = "http://vsd.vn/ModuleLichHoatDong/ThucHienQuyen/ThucHienQuyenSearch/?p_Search="+search+"&p_StockType=1&p_Market=Trái+phiếu+chuyên+biệt&p_StartDate=02/11/2018&p_EndDate=23/11/2018&_=1541843815679";
        Calendar calendar = Calendar.getInstance();
        market = market.replace(' ', '+');
        String link = "http://vsd.vn/ModuleLichHoatDong/ThucHienQuyen/ThucHienQuyenSearch/?p_Search="+search+"&p_StockType=1&p_Market="+market+"&p_StartDate="+start+"&p_EndDate="+end+"&_="+calendar.getTimeInMillis();

        ArrayList<ThucHienQuyenItem> thucHienQuyenItems = new ArrayList<ThucHienQuyenItem>();
        /*thucHienQuyenItems.add(new ThucHienQuyenItem("20/11/2018", "AAA", "AAA: Thực hiện lấy ý kiến cổ đông bằng văn bản", "VN000000SID3", "Cổ phiếu", "UpCOM", "Chi nhánh"));
        thucHienQuyenItems.add(new ThucHienQuyenItem("20/11/2018", "SID", "SID: Thực hiện lấy ý kiến cổ đông bằng văn bản", "VN000000SID3", "Cổ phiếu", "UpCOM", "Chi nhánh"));
        thucHienQuyenItems.add(new ThucHienQuyenItem("20/11/2018", "VGI", "VGI: Thực hiện lấy ý kiến cổ đông bằng văn bản", "VN000000SID3", "Cổ phiếu", "UpCOM", "Chi nhánh"));
        thucHienQuyenItems.add(new ThucHienQuyenItem("22/11/2018", "VNM", "VNM: Thực hiện lấy ý kiến cổ đông bằng văn bản", "VN000000SID3", "Cổ phiếu", "UpCOM", "Chi nhánh"));
        thucHienQuyenItems.add(new ThucHienQuyenItem("22/11/2018", "THS", "THS: Thực hiện lấy ý kiến cổ đông bằng văn bản", "VN000000SID3", "Cổ phiếu", "UpCOM", "Chi nhánh"));
        thucHienQuyenItems.add(new ThucHienQuyenItem("22/11/2018", "SID", "SID: Thực hiện lấy ý kiến cổ đông bằng văn bản", "VN000000SID3", "Cổ phiếu", "UpCOM", "Chi nhánh"));
        if(true) return thucHienQuyenItems;*/
        try {
            Log.d("getThucHienQuyenItems", link);

            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            Document doc = Jsoup.connect(link).get();
            Elements trTags = doc.select("tr");

            String date = "", maCK = "", tieuDe = "", maISIN = "", loaiCK = "", thiTruong = "", chiNhanh = "";
            for (Element trTag : trTags) {
                Elements tdTags = trTag.select("td");
                if(tdTags.size() == 8) {
                    date = tdTags.get(1).text();
                    maCK = tdTags.get(2).text();
                    maISIN = tdTags.get(3).text();
                    tieuDe = tdTags.get(4).text();
                    loaiCK = tdTags.get(5).text();
                    thiTruong = tdTags.get(6).text();
                    chiNhanh = tdTags.get(7).text();

                    ThucHienQuyenItem thucHienQuyenItem = new ThucHienQuyenItem(date, maCK, tieuDe, maISIN, loaiCK, thiTruong, chiNhanh);
                    thucHienQuyenItems.add(thucHienQuyenItem);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return thucHienQuyenItems;
    }
}
