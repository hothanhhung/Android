package com.hunght.utils;

import android.os.StrictMode;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hunght.data.DanhMucDauTuItem;
import com.hunght.data.HistoryPrice;
import com.hunght.data.MenuLookUpItemKind;
import com.hunght.data.PriceItem;
import com.hunght.data.DoanhNghiepItem;
import com.hunght.data.ThongTinDoanhNghiep;
import com.hunght.data.ThucHienQuyenItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class ParserData {
    
	public static String getContent(MenuLookUpItemKind kind) {

        switch (kind)
        {
            case Cafef:
                return "Cafef";
        }
        return "Không xác định được dữ liêu";
    }

    public static ArrayList<ThucHienQuyenItem> getThucHienQuyenItems(String search, String market, String stockType, String start, String end, boolean moreTime){
        Log.d("getThucHienQuyenItems", moreTime?"true":"false");
	    ArrayList<ThucHienQuyenItem> thucHienQuyenItems = new ArrayList<ThucHienQuyenItem>();
        ArrayList<String> listSearch;
        if(search != null && !search.trim().isEmpty()) {
            search = search.replaceAll(" ", "").toUpperCase();
            listSearch = new ArrayList<>(Arrays.asList(search.split(",")));
        }else{
            listSearch = new ArrayList<>();
        }
        if(moreTime && listSearch.size() > 1)
        {
            for (int i = 0;i<listSearch.size() - 1; i++) {
                getThucHienQuyenItems(thucHienQuyenItems, new ArrayList<String>(listSearch.subList(i, i+1)), market, stockType, start, end, "");
            }
            getThucHienQuyenItems(thucHienQuyenItems, listSearch, market, stockType, start, end, "");
        }else{
            getThucHienQuyenItems(thucHienQuyenItems, listSearch, market, stockType, start, end, "");
        }
        return thucHienQuyenItems;
    }
    public static void getThucHienQuyenItems(ArrayList<ThucHienQuyenItem> thucHienQuyenItems, ArrayList<String> listSearch, String market, String stockType, String start, String end, String page) {
        //     String link = "http://vsd.vn/ModuleLichHoatDong/ThucHienQuyen/ThucHienQuyenSearch/?p_Search="+search+"&p_StockType=1&p_Market=Trái+phiếu+chuyên+biệt&p_StartDate=02/11/2018&p_EndDate=23/11/2018&_=1541843815679";
        Calendar calendar = Calendar.getInstance();
        market = market.replace(' ', '+');

        String link = "http://vsd.vn/ModuleLichHoatDong/ThucHienQuyen/ArticleSort/?p_OrderBy=NGAYDKCC&p_OrderType=0&p_Search="+(listSearch.size()==1?listSearch.get(0):"")+"&p_StockType=" + stockType + "&p_Market=" + market + "&p_StartDate=" + start + "&p_EndDate=" + end + "&_=" + calendar.getTimeInMillis();
        if(page!=null && page !="") {
            link += "&p_CurrPage=" + page;
        }else{
            link += "&p_CurrPage=" + 1;
        }

        if(thucHienQuyenItems == null){
            thucHienQuyenItems = new ArrayList<ThucHienQuyenItem>();
        }

        try {
            Log.d("getThucHienQuyenItems", link);

            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            Document doc = Jsoup.connect(link).get();
            Elements trTags = doc.select("tr");

            String date = "", maCK = "", tieuDe = "", maISIN = "", loaiCK = "", thiTruong = "", chiNhanh = "", url = "";
            for (Element trTag : trTags) {
                Elements tdTags = trTag.select("td");
                if (tdTags.size() == 8) {
                    date = tdTags.get(1).text().trim();
                    maCK = tdTags.get(2).text().trim();
                    if (listSearch.isEmpty() || listSearch.contains(maCK.toUpperCase())) {

                        maISIN = tdTags.get(3).text().trim();
                        tieuDe = tdTags.get(4).text().trim();
                        Elements aTags = trTag.select("a");
                        if(aTags!=null && aTags.size() > 0)
                        {
                            url = "http://vsd.vn" + aTags.get(0).attr("href");
                        }
                        loaiCK = tdTags.get(5).text().trim();
                        thiTruong = tdTags.get(6).text().trim();
                        chiNhanh = tdTags.get(7).text().trim();

                        ThucHienQuyenItem thucHienQuyenItem = new ThucHienQuyenItem(date, maCK, tieuDe, maISIN, loaiCK, thiTruong, chiNhanh, url);
                        thucHienQuyenItems.add(thucHienQuyenItem);
                    }
                }
            }

            if(page == null || page =="") {
                Elements liTags = doc.select("li");
                for (Element liTag : liTags) {
                    String index = liTag.text().trim();
                    if (index.matches("\\d+(?:\\.\\d+)?") && !index.equals("1")) {
                        getThucHienQuyenItems(thucHienQuyenItems, listSearch, market, stockType, start, end, index);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static HistoryPrice getHistoryPrice(String maCK) {
        if(maCK == null || maCK.isEmpty()) return null;
        maCK = maCK.toUpperCase();
        String link = "http://s.cafef.vn/Lich-su-giao-dich-"+maCK+"-1.chn";

        try {
            Log.d("getThucHienQuyenItems", link);

            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            Document doc = Jsoup.connect(link).get();
            Elements tables = doc.select("table");
            for (Element table: tables ) {
                Elements trTags = table.select("tr");
                if(trTags.size() < 3) continue;;
                String date = "", price = "", change = "", khoiLuong = "", priceAtOpen = "", high = "", low = "";
                ArrayList<PriceItem> priceItems = new ArrayList<>();
                for (int i = 2; i < trTags.size(); i++) {
                    Element trTag = trTags.get(i);
                    Elements tdTags = trTag.select("td");
                    if (tdTags.size() > 12) {
                        date = tdTags.get(0).text().trim().replace('/', '-');
                        price = tdTags.get(2).text().trim();
                        change = tdTags.get(3).text().trim().replace(" %","%");
                        khoiLuong = tdTags.get(5).text().trim();
                        priceAtOpen = tdTags.get(9).text().trim();
                        high = tdTags.get(10).text().trim();
                        low = tdTags.get(11).text().trim();
                        priceItems.add(new PriceItem(date, price, change, khoiLuong, priceAtOpen, low, high));
                    }
                }
                if(priceItems.size() > 0)
                {
                    return new HistoryPrice(maCK, priceItems);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<DoanhNghiepItem> getThongTinDoanhNghieps()
    {
        String link = "http://cafefcdn.com/frontend/scripts/kby_v1.js";
        Log.d("getThongTinDoanhNghieps", link);

        Gson gSon = new Gson();
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            StringBuilder jsonStringBuilder = new StringBuilder();
            BufferedReader input = new BufferedReader(new InputStreamReader(new URL(link).openStream(), "UTF-8"));

            String inputLine;
            while ((inputLine = input.readLine()) != null)
            {
                jsonStringBuilder.append(inputLine);
            }
            input.close();
            String json = jsonStringBuilder.substring(jsonStringBuilder.indexOf("[")).replace(';',' ');
            Log.d("getThongTinDoanhNghieps",json);
            Type collectionType = new TypeToken<ArrayList<DoanhNghiepItem>>(){}.getType();
            return gSon.fromJson(json, collectionType);
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    public static ThongTinDoanhNghiep getThongTinDoanhNghiep(String link) {
        if(link == null || link.isEmpty()) return null;

        try {
            Log.d("getThongTinDoanhNghiep", link);

            String name = "", code = "", logoURL = "", information = "", currentPrice = "", thongSoKT = "", doThi = "", traCoTuc = "";
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            Document doc = Jsoup.connect(link).get();
            Element divContent = doc.select("div[id='content']").first();
            if(divContent!=null)
            {
                Element symbolbox = divContent.select("div[id='symbolbox']").first();
                if(symbolbox!=null){
                    code = symbolbox.text().trim();
                }
                Element namebox = divContent.select("div[id='namebox']").first();
                if(namebox!=null){
                    name = namebox.text().trim();
                }
                Element avartar = divContent.select("div[class='avartar']").first();
                if(avartar!=null){
                    Element img = avartar.select("img").first();
                    if(img!=null)
                    {
                        logoURL = img.attr("src").trim();
                    }
                }
                Element companyIntro = divContent.select("div[class='companyIntro']").first();
                if(companyIntro!=null){
                    information = companyIntro.text().trim();
                }

                Element dl_thongtin = divContent.select("div[class='dl-thongtin clearfix']").first();
                if(dl_thongtin!=null)
                {
                    Elements ultags = dl_thongtin.select("ul");
                    for (Element ul:ultags) {
                        for(Element item: ul.select("li"))
                        {
                            String text = item.text().replaceAll("(^\\h*)|(\\h*$)","");
                            text = text.replace("    ", "").replace("(*)","").replace("(**)","").trim();
                            /*String [] str = text.split(" ");
                            text = "";
                            for (String s:str) {
                                if(s!=null && !s.trim().isEmpty()){
                                    text +=" " + s.trim();
                                }
                            }*/
                            if(!text.isEmpty())
                            {
                                thongSoKT+=text+"\n";
                            }
                        }
                        thongSoKT+="\n";
                    }
                    //thongSoKT = "<b>"+thongSoKT+"</b>";
                    Log.d("",thongSoKT);
                }
                /*Element dlt_left = divContent.select("div[class='dlt-left']").first();
                if(dlt_left!=null){
                    Element dltl_other = dlt_left.select("div[class='dltl-other']").first();
                    if(dltl_other!=null){
                        Element ul = dlt_left.select("ul").first();
                        if(ul!=null)
                        {
                            Elements litags = ul.select("li");
                            for (Element li:litags) {
                                Elements divtags = li.select("div");
                                if(divtags.size() > 1){
                                    thongSoKT+=divtags.first().text().trim()+ " &nbsp;" + divtags.last().text().trim() + "<br/>";
                                }
                            }
                            thongSoKT = "<b>"+thongSoKT+"</b>";
                        }
                    }
                }*/

                Element dlt_right = divContent.select("a[class='dangky']").first();
                if(dlt_right!=null){
                    Element tooltip = dlt_right.parent().select("div[class='tooltip']").first();
                    if(tooltip!=null){
                        traCoTuc = tooltip.html();
                    }
                }

                if(!code.isEmpty())
                {
                    return new ThongTinDoanhNghiep(name, code, logoURL, information, "0", thongSoKT, traCoTuc);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getBaoCaoTaiChinh(String link)
    {
        Log.d("getThongTinDoanhNghieps", link);

        Gson gSon = new Gson();
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            Document doc = Jsoup.connect(link).get();
            Element divContent = doc.select("div[class='treeview']").first();
            if(divContent!=null)
            {
                return divContent.outerHtml();
            }
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return "";
    }

    public static DanhMucDauTuItem getCurrentPrice(String maCK) {
        if(maCK == null || maCK.isEmpty()) return null;
        maCK = maCK.toUpperCase();
        String link = "http://vcbs.com.vn/DataGen/StockInfo/"+maCK+".xml?_="+ Calendar.getInstance().getTimeInMillis();

        try {
            Log.d("getCurrentPrice", link);

            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            Document doc = Jsoup.connect(link).get();
            Element stock  = doc.select("Stock").first();
            if(stock != null){
                String current_price = stock.attr("current_price");
                if(current_price!=null && !current_price.isEmpty()){
                    current_price = current_price.replace(',', '.');
                    DanhMucDauTuItem danhMucDauTuItem = new DanhMucDauTuItem();
                    danhMucDauTuItem.setMaCK(maCK);
                    danhMucDauTuItem.setGiaThiTruong(Float.valueOf(current_price));
                    return danhMucDauTuItem;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
