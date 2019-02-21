package com.hth.tracuuonline;

import android.content.Context;
import android.os.Message;
import android.os.StrictMode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hth.data.StaticData;
import com.hth.utils.AdItem;
import com.hth.utils.MethodsHelper;
import com.hth.utils.ResponseJson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Lenovo on 10/28/2016.
 */
public class LookUpKetQuaXoSo extends LinearLayout {
    String urlLookUpKetQuaXoSo = "http://laban.vn/ajax/getLottery?id=";
    WebView webView;
    Spinner spinner;
    RelativeLayout llWebView;
    private  String[] lotteryPlaces = new String[]{"TP.HỒ CHÍ MINH","AN GIANG", "BÌNH DƯƠNG", "BÌNH THUẬN", "BÌNH ĐỊNH", "BẠC LIÊU", "BẾN TRE", "CÀ MAU", "CẦN THƠ","DAKLAK", "ĐÀ LẠT", "ĐÀ NẴNG", "ĐẮC NÔNG", "ĐỒNG NAI", "ĐỒNG THÁP", "GIA LAI", "HẬU GIANG", "KHÁNH HÒA", "KIÊN GIANG", "KON TUM","LONG AN","MIỀN BẮC","NINH THUẬN", "PHÚ YÊN","QUẢNG BÌNH","QUẢNG NAM","QUẢNG NGÃI","QUẢNG TRỊ","SÓC TRĂNG","THỪA THIÊN HUẾ","TIỀN GIANG", "TRÀ VINH", "TÂY NINH", "VĨNH LONG", "VŨNG TÀU"};

    public LookUpKetQuaXoSo(Context context) {
        super(context);
        initView();
    }

    public LookUpKetQuaXoSo(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LookUpKetQuaXoSo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.look_up_ket_qua_xo_so_layout, this);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        webView = (WebView) view.findViewById(R.id.webView);
        llWebView = (RelativeLayout) view.findViewById(R.id.llWebView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (
                        getContext(),
                        android.R.layout.simple_spinner_item,
                        lotteryPlaces
                );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                firstLoadWeb(getIdFromName(lotteryPlaces[position]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        WebSettings webViewSettings = webView.getSettings();
        webViewSettings.setSupportZoom(true);
        webViewSettings.setBuiltInZoomControls(true);
        webViewSettings.setDisplayZoomControls(false);
        webView.clearCache(true);
        //webViewSettings.setJavaScriptEnabled(true);
        //webViewSettings.setLoadWithOverviewMode(true);
       // webViewSettings.setUserAgentString("Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.23 Mobile Safari/537.36");
        /*webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                llWebView.setVisibility(VISIBLE);
            }
        });*/
        //firstLoadWeb();
    }

    private void firstLoadWeb(String id)
    {
        Log.d("firstLoadWeb", id);
        String data = getDatafromServer(urlLookUpKetQuaXoSo+id);
        data="<style>\n" +
                ".headrow {\n" +
                "    background-color: #EEE;\n" +
                "    border-bottom: 1px solid #CCC;\n" +
                "    font-weight: 700;\n" +
                "    height: 40px;\n" +
                "    line-height: 40px;\n" +
                "}\n" +
                ".row { \n" +
                "\tborder-bottom: 1px solid #eee;\n" +
                "    overflow: auto;\n" +
                "    line-height: 40px;\n" +
                "}\n" +
                "\n" +
                "span.cell {\n" +
                "    color: #333;\n" +
                "    display: block;\n" +
                "    float: left;\n" +
                "    margin-left: 10px;\n" +
                "    overflow: hidden;\n" +
                "}" +
                ".c01 {width: 35%; } .col2 {width: 55%; text-align: center; }" +
                "</style>" + data + "";
        webView.loadDataWithBaseURL(null, data, "text/html; charset=utf-8", "UTF-8", null);
        //llWebView.setVisibility(GONE);
        //btRefresh.setVisibility(GONE);
    }

    private String getIdFromName(String name)
    {
        String id = MethodsHelper.stripAccentsAndD(name);
        if("TP.HO CHI MINH".equalsIgnoreCase(id))
        {
            return "HO_CHI_MINH";
        }else if("DAKLAK".equalsIgnoreCase(id))
        {
            return "DAK_LAK";
        }
        id = id.replace(' ', '_');
        return id;
    }

    private void injectCSS() {
        try {
            String encoded = " img {display:none !important} #Image1 {display:block !important} #Label2{font-size: 14px !important; width: 95% !important;color: green !important;} span {width:100% !important} input {display:block !important; width: 95% !important; height:30px !important} #Button1{margin-top: 15px;margin-bottom: 15px;}";
            webView.loadUrl("javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var style = document.createElement('style');" +
                    "style.type = 'text/css';" +
                    "style.innerHTML = '" + encoded + "';" +
                    "parent.appendChild(style)" +
                    "})()");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    class DatafromServer{
        String id;
        String html;
        String date;

        String getHtml(){return html;}
    }
    public String getDatafromServer(String link)
    {
        DatafromServer responseJson = new DatafromServer();
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
            String json = jsonStringBuilder.toString();

            Type collectionType = new TypeToken<DatafromServer>(){}.getType();
            responseJson = gSon.fromJson(json, collectionType);
            return responseJson.getHtml();
        }catch(Exception ex)
        {
            ex.printStackTrace();
            return "Lỗi khi kết nối server";
        }
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
       // this.setMeasuredDimension(width, height);
    }
}
