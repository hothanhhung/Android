package com.hth.tracuuonline;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hth.utils.ParserData;

/**
 * Created by Lenovo on 10/28/2016.
 */
public class LookUpLichChieuPhimRap extends LinearLayout {
    WebView webView;
    TextView textView;
    RelativeLayout llWebView;
    //th:nth-child(4), th:nth-child(6),th:nth-child(7),td:nth-child(4), td:nth-child(6),td:nth-child(7),
    String cssJoin ="header, #dropdown-location-header, #menu-hide, #event, #welcome, footer, iframe, .cinema-price, .groupCinema  {display:none !important}"+
            " html, section, .main, .showtime, #wrapCinemaBook {width: 100% !important;margin:0px !important; padding:0px !important;  border: none !important;float: none !important; background: white !important;}"+
            " .select {width: 99% !important;float: none; margin: 10px 2% 10px 0px !important;} "+
            " body{margin:5px 0px 10px 0px !important; min-width: 50% !important; padding: 0px !important;width: 99% !important;}" +
            " a:not(.dd-option) {pointer-events: none;cursor: default;opacity: 0.6;}"+
            " .titItemCinema {font-size: 20px !important;}"+
            " .main:after {content: \"Nguồn: 123phim.vn\";}";
    public LookUpLichChieuPhimRap(Context context) {
        super(context);
        initView();
    }

    public LookUpLichChieuPhimRap(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LookUpLichChieuPhimRap(Context context, AttributeSet attrs, int defStyleAttr) {
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
        webView.loadUrl("http://www.123phim.vn/");
    }

}
