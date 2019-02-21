package com.hth.tracuuonline;

import android.content.Context;
import android.os.Message;
import android.os.StrictMode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hth.utils.MethodsHelper;
import com.hth.utils.ParserData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;

/**
 * Created by Lenovo on 10/28/2016.
 */
public class LookUpThongTinNguoiNopThue extends LinearLayout {
    WebView webView;
    TextView textView;
    RelativeLayout llWebView;
    //th:nth-child(4), th:nth-child(6),th:nth-child(7),td:nth-child(4), td:nth-child(6),td:nth-child(7),
    String cssJoin =".search_form >tbody> tr:nth-child(1),th:nth-child(7), td:nth-child(7), #loadFrm, #bgLeft, #bgTop, #bgRight, #top, #bgBottom, #footer, .module3Top{display:none !important}"+
            " html, .module3Body2, #body, #content, #wrapper, #left, .css-tabs, .search_form {width: 100% !important;margin:0px !important; padding:0px !important;  border: none !important;float: none !important; background: white !important;}"+
            " li> a, .ta_border td:nth-child(3) a{background: white !important; background-color: white !important;border: none !important; text-decoration:underline !important}"+
            " .ta_border {width: 99% !important;} .ta_border th, .ta_border td{width: auto !important; padding: 2px !important; margin:0px !important}"+
            " . .simple_overlay {min-width: 100px !important;width: 96% !important;} #left:after {content: \"Nguồn: Tổng cục Thuế\";}";
    public LookUpThongTinNguoiNopThue(Context context) {
        super(context);
        initView();
    }

    public LookUpThongTinNguoiNopThue(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LookUpThongTinNguoiNopThue(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.look_up_thong_tin_nguoi_nop_thue, this);
        textView = (TextView) view.findViewById(R.id.textView);
        webView = (WebView) view.findViewById(R.id.webView);
        llWebView = (RelativeLayout) view.findViewById(R.id.llWebView);

        WebSettings webViewSettings = webView.getSettings();
        webViewSettings.setSupportZoom(true);
        webViewSettings.setBuiltInZoomControls(true);
        webViewSettings.setDisplayZoomControls(false);
        webViewSettings.setJavaScriptEnabled(true);
        webViewSettings.setDomStorageEnabled(true);
        webView.clearCache(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                textView.setVisibility(GONE);
                llWebView.setVisibility(VISIBLE);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (url!=null && url.contains(".css")) {
                    return ParserData.getCSSDetail(url, cssJoin);
                } else
                {
                    return super.shouldInterceptRequest(view, url);
                }
            }
        });
        firstLoadWeb();
    }


    private void firstLoadWeb()
    {
        textView.setVisibility(VISIBLE);
        llWebView.setVisibility(GONE);
        webView.loadUrl("http://tracuunnt.gdt.gov.vn/tcnnt/mstcn.jsp");
    }

}
