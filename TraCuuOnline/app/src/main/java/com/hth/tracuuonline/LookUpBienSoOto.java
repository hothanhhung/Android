package com.hth.tracuuonline;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Lenovo on 10/28/2016.
 */
public class LookUpBienSoOto extends LinearLayout {
    String urlLookUpBienSoOto = "http://www.vr.org.vn/ptpublic/ThongTinPTPublic.aspx";
    WebView webView;
    TextView textView;
    RelativeLayout llWebView;
    Button btRefresh;

    public LookUpBienSoOto(Context context) {
        super(context);
        initView();
    }

    public LookUpBienSoOto(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LookUpBienSoOto(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private static final String desktop_mode = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.109 Mobile Safari/537.36";
    private void initView() {
        View view = inflate(getContext(), R.layout.look_up_bien_so_oto_layout, this);
        textView = (TextView) view.findViewById(R.id.textView);
        webView = (WebView) view.findViewById(R.id.webView);
        llWebView = (RelativeLayout) view.findViewById(R.id.llWebView);
        btRefresh = (Button) view.findViewById(R.id.btRefresh);

        btRefresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                firstLoadWeb();
            }
        });

        WebSettings webViewSettings = webView.getSettings();
        webViewSettings.setSupportZoom(true);
        webViewSettings.setBuiltInZoomControls(true);
        webViewSettings.setDisplayZoomControls(false);
        webViewSettings.setJavaScriptEnabled(true);
        webViewSettings.setLoadWithOverviewMode(true);
        webViewSettings.setUseWideViewPort(false);
        webViewSettings.setUserAgentString(desktop_mode );
        webView.clearCache(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onFormResubmission(WebView view, Message dontResend, Message resend) {
                textView.setVisibility(VISIBLE);
                llWebView.setVisibility(GONE);
                super.onFormResubmission(view, dontResend, resend);
                //btRefresh.setVisibility(VISIBLE);
            }

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
        webView.loadUrl(urlLookUpBienSoOto);
        llWebView.setVisibility(GONE);
        //btRefresh.setVisibility(GONE);
    }

    private void injectCSS() {
        try {
            String encoded = " table {display:none !important} font {display: flex;flex-direction: column;} #Image1 {display:block !important} #Label2{font-size: 14px !important; width: 95% !important;color: green !important;} span {width:100% !important} input {display:block !important; width: 95% !important; height:30px !important} #ImaRefresh{display:block !important; width: 30px !important; height:30px !important}  #Button1{margin-top: 15px;margin-bottom: 15px;}";
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
