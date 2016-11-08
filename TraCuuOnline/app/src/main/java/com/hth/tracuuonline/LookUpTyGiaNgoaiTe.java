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
public class LookUpTyGiaNgoaiTe extends LinearLayout {
    WebView webView;
    TextView textView;
    RelativeLayout llWebView;

    public LookUpTyGiaNgoaiTe(Context context) {
        super(context);
        initView();
    }

    public LookUpTyGiaNgoaiTe(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LookUpTyGiaNgoaiTe(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.look_up_with_webview, this);
        textView = (TextView) view.findViewById(R.id.textView);
        webView = (WebView) view.findViewById(R.id.webView);
        llWebView = (RelativeLayout) view.findViewById(R.id.llWebView);

        WebSettings webViewSettings = webView.getSettings();
        webViewSettings.setBuiltInZoomControls(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                textView.setVisibility(GONE);
                llWebView.setVisibility(VISIBLE);
            }
        });
        firstLoadWeb();
    }


    private void firstLoadWeb()
    {
        textView.setVisibility(VISIBLE);
        llWebView.setVisibility(GONE);
        String data = ParserData.getExchageRateFromVietcombank();
        webView.loadDataWithBaseURL(null, data, "text/html; charset=utf-8", "UTF-8", null);
    }

}
