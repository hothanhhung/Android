package com.hth.tracuuonline;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hth.data.MenuLookUpItemKind;

/**
 * Created by Lenovo on 10/28/2016.
 */
public class LookUpForViewWithWebViewRequest extends LinearLayout {
    WebView webView;
    TextView textView;
    RelativeLayout llWebView;
    MenuLookUpItemKind kind;

    public LookUpForViewWithWebViewRequest(Context context) {
        super(context);
        initView();
    }

    public LookUpForViewWithWebViewRequest(Context context, MenuLookUpItemKind kind) {
        super(context);
        this.kind = kind;
        initView();
    }

    public LookUpForViewWithWebViewRequest(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LookUpForViewWithWebViewRequest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.look_up_with_webview_request, this);
        textView = (TextView) view.findViewById(R.id.textView);
        webView = (WebView) view.findViewById(R.id.webView);
        llWebView = (RelativeLayout) view.findViewById(R.id.llWebView);

        WebSettings webViewSettings = webView.getSettings();
        webViewSettings.setSupportZoom(true);
        webViewSettings.setBuiltInZoomControls(true);
        webViewSettings.setDisplayZoomControls(false);
        webViewSettings.setJavaScriptEnabled(true);
        webViewSettings.setDomStorageEnabled(true);

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
        webView.loadUrl(getUrl(kind));
    }

    public String getUrl(MenuLookUpItemKind kind) {

        switch (kind)
        {
            case MaBienSo:
                return "http://hunght.com/htmlpage/lookuponline/mabienso.html";
            case DauSoDienThoai:
                return "http://hunght.com/htmlpage/lookuponline/dausodienthoai.html";
            case BongDa:
                return "http://hunght.com/htmlpage/lookuponline/bongda.html";
        }
        return "";
    }

}
