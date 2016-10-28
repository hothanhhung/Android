package com.hth.tracuuonline;

import android.content.Context;
import android.os.Message;
import android.util.AttributeSet;
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

import com.hth.data.StaticData;
import com.hth.utils.MethodsHelper;

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
                firstLoadWeb(lotteryPlaces[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        WebSettings webViewSettings = webView.getSettings();
        webViewSettings.setBuiltInZoomControls(true);
        webViewSettings.setJavaScriptEnabled(true);
        webViewSettings.setLoadWithOverviewMode(true);
        webViewSettings.setUseWideViewPort(false);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {

                // Inject CSS when page is done loading
                injectCSS();
                super.onPageFinished(view, url);
                llWebView.setVisibility(VISIBLE);
            }
        });
        //firstLoadWeb();
    }

    private void firstLoadWeb(String id)
    {
        webView.loadUrl(urlLookUpKetQuaXoSo+id);
        llWebView.setVisibility(GONE);
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
       // this.setMeasuredDimension(width, height);
    }
}
