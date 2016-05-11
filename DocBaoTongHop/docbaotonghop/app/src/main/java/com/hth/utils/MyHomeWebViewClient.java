package com.hth.utils;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hth.docbaotonghop.MainActivity;

public class MyHomeWebViewClient extends WebViewClient {
    private Activity context;

    private long lastTime = -1;
    ArrayList<String> listViewedContents = new ArrayList<String>();
    int index = -1;

    public ArrayList<String>  getListViewedContents(){return this.listViewedContents;}

    public int getIndexListViewedContents(){return this.index;}

    public Boolean canGobackHistory(){return index>0;}
    public Boolean canPreviousHistory(){return index<this.listViewedContents.size() - 1;}
    
    public int getCurrentIndex()
    {
    	return index;
    }
    
    public String decreaseIndexListViewedContents(){
        if(this.index>0)
        {
            this.index--;
            return listViewedContents.get(index);
        }
        return null;
    }

    public String currentIndexListViewedContents(){
        if(this.listViewedContents.size()> 0)
        {
            return listViewedContents.get(index);
        }
        return null;
    }

    public String increaseIndexListViewedContents(){
        if(this.index < this.listViewedContents.size() - 1)
        {
            this.index++;
            return listViewedContents.get(index);
        }
        return null;
    }

    public void addListViewedContents(String content){
    	long currrentTime = Calendar.getInstance().getTime().getTime();
    	if(lastTime == -1 || currrentTime - lastTime > 1500)
    	{
	        while(this.listViewedContents.size() - 1 > this.index)
	        {
	            this.listViewedContents.remove(this.listViewedContents.size() - 1);
	        }
	        this.listViewedContents.add(content);
	        this.index++;
	        ((MainActivity)context).updateButtonBackAndPrevious();
    	}else
    	{
    		if(this.listViewedContents.size() - 1 > 0){
	    		this.listViewedContents.remove(this.listViewedContents.size() - 1);
		        this.listViewedContents.add(content);
    		}
    	}
    	this.lastTime = currrentTime;
    }

    @Override
	public void onLoadResource(WebView view, String url) {
		// TODO Auto-generated method stub
    	/*if(url.contains("admicro1.vcmedia.vn") || url.contains("googlesyndication.com"))
    	{
    		return;
    	}*/
		super.onLoadResource(view, url);
	}


    public MyHomeWebViewClient(Activity context)
    {
        this.context = context;
    }
    
	@Override
    public WebResourceResponse shouldInterceptRequest (final WebView view, String url) {
    	if (url!=null && url.contains(".css")) {
            return ParserData.getCSSDetail(url);
        } else 
        {
            return super.shouldInterceptRequest(view, url);
        }
        
    }
	
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
    	String currenturl = currentIndexListViewedContents();
    	if(url!=null && url.trim()!="" && currenturl != null && currenturl.toLowerCase().trim() != url.toLowerCase().trim())
    	{
	        view.stopLoading();
	        view.clearView();
	        view.stopLoading();
	        this.addListViewedContents(url);
	        ((MainActivity)context).changeAdProvider();
	        //view.loadDataWithBaseURL(null,ParserData.getArticleDetail(url), "text/html", "utf-8", null);
	        view.loadUrl(url);
	        ((MainActivity)context).updateButtonBackAndPrevious();
    	}
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
       // view.scrollTo(0,0);
    	if(context!=null)
    		((MainActivity)context).lastChangeAdProvider();
      }
}

