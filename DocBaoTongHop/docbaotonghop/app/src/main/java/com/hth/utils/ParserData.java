package com.hth.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import com.hth.docbaotonghop.MainActivity;

import android.app.Activity;
import android.os.StrictMode;
import android.util.Log;
import android.webkit.WebResourceResponse;

public class ParserData {

    public static ConfigAds getConfigAds() {
        String link = "https://hunghtpage.github.io/DocBaoTongHop.json";
		StringBuilder contentResult = new StringBuilder();

        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            java.io.BufferedReader input = new java.io.BufferedReader(new java.io.InputStreamReader(new java.net.URL(link).openStream(), "UTF-8"));

    		String inputLine;
            while ((inputLine = input.readLine()) != null)
            {
            	contentResult.append(inputLine);
            }
            input.close();
	    	String json = contentResult.toString();
            Log.w("getConfigAds", json);
            com.google.gson.Gson gSon = new com.google.gson.Gson();
	    	return gSon.fromJson(json, ConfigAds.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ConfigAds();
    }

	public static String getArticleDetail(String urlpage) {
		StringBuilder contentResult = new StringBuilder();

        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            Document doc = Jsoup.connect(urlpage).timeout(10000)
                   // .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64; rv:30.0) Gecko/20100101 Firefox/30.0")
                    .get();

            contentResult.append(doc.html());
            String joinContent =  MainActivity.getCurrent_Website_Page().GetReformatCss();
            contentResult.insert(contentResult.indexOf("</head>") - 1, joinContent);
        } catch (Exception ex) {
            contentResult =  new StringBuilder(ex.getMessage());
            ex.printStackTrace();
        }
        return contentResult.toString();
    }
	
	public static WebResourceResponse getCSSDetail(Activity context, String urlpage) {
		StringBuilder contentResult = new StringBuilder();

        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            Document doc = Jsoup.connect(urlpage).timeout(10000)
                   // .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64; rv:30.0) Gecko/20100101 Firefox/30.0")
                    .get();

            contentResult.append(doc.text());
            String joinContent =  MainActivity.getCurrent_Website_Page().GetReformatCssContent(context);
            Log.w("getCSSDetail", joinContent);
            contentResult.append(" ").append(joinContent);
            
            InputStream data = new ByteArrayInputStream(contentResult.toString().getBytes());
            return new WebResourceResponse("text/css", "UTF-8", data);
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
