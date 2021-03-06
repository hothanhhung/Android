package com.hunght.tinchungkhoan;

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

import com.hunght.data.MenuLookUpItemKind;
import com.hunght.data.StaticData;
import com.hunght.utils.MyWebview;

/**
 * Created by Lenovo on 10/28/2016.
 */
public class LookUpForViewWithWebViewRequest extends LinearLayout {
    static MyWebview webView;
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
        webView = view.findViewById(R.id.webView);
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

    public static boolean canGoBack()
    {
        if(webView!=null)
        {
            return webView.canGoBack();
        }
        return false;
    }

    public static void goBack()
    {
        if(webView!=null)
        {
            webView.goBack();
        }
    }

    private void firstLoadWeb()
    {
        textView.setVisibility(VISIBLE);
        llWebView.setVisibility(GONE);
        webView.loadUrl(getUrl(kind));
    }

    public String getUrl(MenuLookUpItemKind kind) {

        return StaticData.geMenuItemBasedOnKind(kind).getUrl();

    }

    @Override
    protected void finalize() throws Throwable {
        webView = null;
        super.finalize();
    }
}
