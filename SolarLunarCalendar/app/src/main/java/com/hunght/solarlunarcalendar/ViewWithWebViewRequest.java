package com.hunght.solarlunarcalendar;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hunght.data.MenuLookUpItemKind;

import java.util.Date;

/**
 * Created by Lenovo on 10/28/2016.
 */
public class ViewWithWebViewRequest extends LinearLayout {
    WebView webView;
    TextView textView;
    RelativeLayout llWebView;
    MenuLookUpItemKind kind;
    MainActivity mainActivity;

    public ViewWithWebViewRequest(Context context) {
        super(context);
        mainActivity = (MainActivity)context;
        initView();
    }

    public ViewWithWebViewRequest(Context context, MenuLookUpItemKind kind) {
        super(context);
        mainActivity = (MainActivity)context;
        this.kind = kind;
        initView();
    }

    public ViewWithWebViewRequest(Context context, AttributeSet attrs) {
        super(context, attrs);
        mainActivity = (MainActivity)context;
        initView();
    }

    public ViewWithWebViewRequest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mainActivity = (MainActivity)context;
        initView();
    }

    private static final String desktop_mode = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.109 Mobile Safari/537.36";

    private void initView() {
        View view = inflate(getContext(), R.layout.webview_request, this);
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
                super.onPageFinished(view, url);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textView.setVisibility(GONE);
                        llWebView.setVisibility(VISIBLE);
                    }
                }, 500);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if(mainActivity != null && request.getUrl().getHost().contains("hunght.com"))
                {
                   mainActivity.adViewShow(false);
                }
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                if(mainActivity != null && request.getUrl().getHost().contains("hunght.com"))
                {
                    mainActivity.adViewShow(false);
                }
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
            case TuViHangNgay:
                return "http://hunght.com/htmlpage/lichamduong/tuvi.html?ts="+ (new Date()).getTime();
            case BoiTinhCach:
                return "http://hunght.com/htmlpage/lichamduong/boitinhcach.html?ts="+ (new Date()).getTime();
            case BoiTinhCachVoiNhomMau:
                return "http://hunght.com/htmlpage/lichamduong/boinhommau.html?ts="+ (new Date()).getTime();
            case BoiTinhCachVoiNgayThangNamSinh:
                return "http://hunght.com/htmlpage/lichamduong/boingaythangnamsinh.html?ts="+ (new Date()).getTime();
            case BoiTenAiCap:
                return "http://hunght.com/htmlpage/lichamduong/boisoaicap.html?ts="+ (new Date()).getTime();
            case GiaiDiem:
                return "http://hunght.com/htmlpage/lichamduong/GiaiMaDiemBao.html?ts="+ (new Date()).getTime();
            case BoiBaiTarot:
                return "http://hunght.com/htmlpage/lichamduong/tarot.html?ts="+ (new Date()).getTime();
            case GieoQueQuanAm:
                return "http://hunght.com/htmlpage/lichamduong/GieoQueQuanAm.html?ts="+ (new Date()).getTime();
            case TietKhi:
                return "http://hunght.com/htmlpage/lichamduong/TietKhi.html?ts="+ (new Date()).getTime();
        }
        return "";
    }

}
