package com.hth.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.StrictMode;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebResourceResponse;

import com.hth.data.MenuLookUpItemKind;

public class ParserData {
    
	public static String getContent(MenuLookUpItemKind kind) {

        switch (kind)
        {
            case TyGiaNgoaiTe:
                return getExchageRateFromVietcombank();
            case BangGiaVang:
                return getBangGiaVang();
            case GiaXang:
                return getBangGiaXang();
            case LaiSuatNganHang:
                return getBangLaiSuat();
            case DuBaoThoiTiet:
                return getThoiTiet();
        }
        return "Không xác định được dữ liêu";
    }
	
	public static WebResourceResponse getCSSDetail(String urlpage, String joinContent) {
		StringBuilder contentResult = new StringBuilder();

        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            Document doc = Jsoup.connect(urlpage).timeout(10000)
                   // .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64; rv:30.0) Gecko/20100101 Firefox/30.0")
                    .get();

            contentResult.append(doc.text());
            Log.w("getCSSDetail", joinContent);
            contentResult.append(joinContent);
            InputStream data = new ByteArrayInputStream(contentResult.toString().getBytes());
            return new WebResourceResponse("text/css", "UTF-8", data);
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String getExchageRateFromVietcombank() {
        String urlpage = "https://www.vietcombank.com.vn/exchangerates/";
        StringBuilder contentResult = new StringBuilder();
        String joinContent="<style> h2, a, #exch-rates table {display:none} .rateTable {display:block !important} \n" +
                "body, .rateTable {font: 81%/1.4 Tahoma, Arial, Helvetica, sans-serif;color: #000} " +
                ".tbl-01 th {background-color: #88EE99; color: #333333;}"+
                ".tbl-01 td {text-align: right;} body {margin-bottom: 40px;}</style>";
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            Document doc = Jsoup.connect(urlpage).timeout(10000)
                   // .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64; rv:30.0) Gecko/20100101 Firefox/30.0")
                    .get();
            Element content = doc.getElementById("exch-rates");
            contentResult.append(joinContent);
            contentResult.append("<body>").append(content.outerHtml()).append("</body>");


        } catch (Exception ex) {
            ex.printStackTrace();
            return "Lỗi khi kết nối server";
        }
        return contentResult.toString();
    }
    public static String getBangGiaVang() {
        String urlpage = "http://banggia.giavang.net/";
        StringBuilder contentResult = new StringBuilder();
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            Document doc = Jsoup.connect(urlpage).timeout(10000)
                    // .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64; rv:30.0) Gecko/20100101 Firefox/30.0")
                    .get();
            contentResult.append(doc.outerHtml());//.replaceAll("size=\"5\"","size=\"4\""));


        } catch (Exception ex) {
            ex.printStackTrace();
            return "Lỗi khi kết nối server";
        }
        return contentResult.toString();
    }


    public static String getBangGiaXang() {
        String urlpage = "http://giaxang.vietbao.vn/";
        StringBuilder contentResult = new StringBuilder();
        String joinContent="<style> .head{background: #fafafa; font-weight: bold; height: 25px; line-height: 25px; text-transform: capitalize;overflow: hidden;}" +
                ".col{float: left; padding: 0 10px;box-sizing: border-box;font-size: 12px;} .col-1 {width: 60%;font-weight: bold;}.col-2, .col-3 {width: 20%;}" +
                ".box-std .inner { border: 1px solid #ccc; box-shadow: -2px 5px 5px #ccc; min-height: 40px; overflow: hidden; border-radius: 0 0 3px 3px; padding-bottom: 20px;}"+
                ".block-2,.block-3{display:none}</style>";
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            Document doc = Jsoup.connect(urlpage).timeout(10000)
                    // .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64; rv:30.0) Gecko/20100101 Firefox/30.0")
                    .get();

            Elements contents = doc.getElementsByClass("box-std");


            contentResult.append(joinContent);
            contentResult.append("<body>").append(contents.first().outerHtml()).append(contents.get(1).outerHtml()).append("</body>");
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Lỗi khi kết nối server";
        }
        return contentResult.toString();
    }
    public static String getBangLaiSuat() {
        String urlpage = "http://www.nganhangdientu.com.vn/lai-suat.html";
        StringBuilder contentResult = new StringBuilder();
        String joinContent="<style>  .tabs-level-2{list-style: none;margin: 0;padding: 5px 0 0 20px;background: #3498db;} .tabs-level-2>li {display: inline-block;}"+
                ".table { border: 1px solid #3498db; border-top: 0; width: 100%; max-width: 100%; margin-bottom: 20px;}" +
                ".table thead tr td { font-size: 11px; font-weight: 500; border-right: 1px solid #dee1e3; text-align: center; color: #3498db;}" +
                ".table tbody tr td { border-right: 1px solid #dee1e3; text-align: center; font-size: 11px;" +
                "tr { display: table-row; vertical-align: inherit; border-color: inherit;}" +
                "tbody { display: table-row-group; vertical-align: middle; border-color: inherit;}" +
                "thead { display: table-header-group; vertical-align: middle; border-color: inherit;}" +
                "img {width: 50px;}</style>";
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            Document doc = Jsoup.connect(urlpage).timeout(10000)
                    // .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64; rv:30.0) Gecko/20100101 Firefox/30.0")
                    .get();

            Element content = doc.getElementById("vnd");


            contentResult.append(joinContent);
            String data = content.outerHtml().replaceAll("src=\"/uploads","src=\"http://www.nganhangdientu.com.vn/uploads");
            data = data.replaceAll("<a ","<b ").replaceAll("</a>", "</b>");
            contentResult.append("<body>").append(data).append("</body>");
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Lỗi khi kết nối server";
        }
        return contentResult.toString();
    }

    public static String getThoiTiet() {
        String urlpage = "http://laban.vn/p/weather";
        StringBuilder contentResult = new StringBuilder();
        String joinContent="<style>.table{width:100% !important; font-size: 10px;text-align: center;}</style>";
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            URL url = new URL(urlpage);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                int start = result.indexOf("<table"), end = result.indexOf("/table>");
                String strResult = result.substring(start, end + 7).replace("\\n", "").replace("\\", "");
                //Log.d("getThoiTiet", strResult);
                contentResult.append(joinContent);
                contentResult.append("<body>").append(strResult).append("<div style=\"font-size:9px\">Nguồn: laban.vn</div>").append("</body>");
            } finally {
                urlConnection.disconnect();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return "Lỗi khi kết nối server";
        }
        return contentResult.toString();
    }
}
