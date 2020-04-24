package com.hth.tracuuonline;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
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
import com.hth.data.MenuLookUpItemKind;
import com.hth.utils.MethodsHelper;
import com.hth.utils.ParserData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;

/**
 * Created by Lenovo on 10/28/2016.
 */
public class LookUpForViewWithWebView extends LinearLayout {
    WebView webView;
    TextView textView;
    RelativeLayout llWebView;
    MenuLookUpItemKind kind;

    public LookUpForViewWithWebView(Context context) {
        super(context);
        initView();
    }

    public LookUpForViewWithWebView(Context context, MenuLookUpItemKind kind) {
        super(context);
        this.kind = kind;
        initView();
    }

    public LookUpForViewWithWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LookUpForViewWithWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.look_up_with_webview, this);
        textView = (TextView) view.findViewById(R.id.textView);
        webView = (WebView) view.findViewById(R.id.webView);
        llWebView = (RelativeLayout) view.findViewById(R.id.llWebView);

        WebSettings webViewSettings = webView.getSettings();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webViewSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        webViewSettings.setSupportZoom(true);
        webViewSettings.setBuiltInZoomControls(true);
        webViewSettings.setDisplayZoomControls(false);
        webView.clearCache(true);

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
        new DownloadContentTask().execute(kind);
    }

    private class DownloadContentTask extends AsyncTask<MenuLookUpItemKind, Integer, String> {
        protected String doInBackground(MenuLookUpItemKind... kinds) {
            return ParserData.getContent(kind);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(String result) {
            webView.loadDataWithBaseURL(null, result, "text/html; charset=utf-8", "UTF-8", null);
        }
    }

}
