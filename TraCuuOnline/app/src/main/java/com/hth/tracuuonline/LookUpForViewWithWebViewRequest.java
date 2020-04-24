package com.hth.tracuuonline;

import android.content.Context;
import android.os.Handler;
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

    private static final String desktop_mode = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.109 Mobile Safari/537.36";

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
        webViewSettings.setUserAgentString(desktop_mode );
        webView.clearCache(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                // Inject CSS when page is done loading
                injectCSS();
                super.onPageFinished(view, url);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textView.setVisibility(GONE);
                        llWebView.setVisibility(VISIBLE);
                    }
                }, 500);
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
            case MaBuuDien:
                return "http://hunght.com/htmlpage/lookuponline/MaBuuDien.html";
            case SwiftCode:
                return "http://hunght.com/htmlpage/lookuponline/SwiftCode.html";
            case OtherLookUp:
                return "http://hunght.com/htmlpage/lookuponline/TraCuuKhac.html";
            case BangGiaVang:
                return "http://banggia.giavang.net";
            case GiaOto:
                return "http://hunght.com/tracuuonline/giaOto";
            case GiaXang:
                return "https://thanhnien.vn/tien-ich/xang-dau.html";
        }
        return "";
    }

    public String getCSSOverride(MenuLookUpItemKind kind) {

        switch (kind)
        {
            case GiaXang:
                return ".l-content{width: 100% !important;} div[id*=\"banner\"], iframe, .bottom-bar, .fb_reset, .banner, .m-header, .m-footer, a, #fb-root{display:none !important}";
        }
        return "";
    }

    private void injectCSS() {
        try {
            String encoded = getCSSOverride(kind);
            if(!encoded.isEmpty()) {
                webView.loadUrl("javascript:(function() {" +
                        "var parent = document.getElementsByTagName('head').item(0);" +
                        "var style = document.createElement('style');" +
                        "style.type = 'text/css';" +
                        "style.innerHTML = '" + encoded + "';" +
                        "parent.appendChild(style)" +
                        "})()");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
