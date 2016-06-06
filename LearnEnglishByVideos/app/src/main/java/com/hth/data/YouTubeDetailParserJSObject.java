package com.hth.data;

import java.util.ArrayList;

public class YouTubeDetailParserJSObject {

	YouTubeVideoDetail [] items = null;
	
	public ArrayList<YouTubeVideoDetail> getYouTubeVideoDetail()
	{
		ArrayList<YouTubeVideoDetail> lstResults = new ArrayList<YouTubeVideoDetail>();
		if(items!=null)
	        for(int i =0; i< items.length; i++)
	        {
	        	lstResults.add(items[i]);
	        }
        
        return lstResults;
	}
	
	public ArrayList<YouTubeVideo> getYouTubeVideo()
	{
		ArrayList<YouTubeVideo> lstResults = new ArrayList<YouTubeVideo>();
		if(items!=null)
	        for(int i =0; i< items.length; i++)
	        {
	        	YouTubeVideo obj = new YouTubeVideo();
	        	obj.etag = items[i].etag;
	        	obj.setvideoId(items[i].id);
	        	obj.kind = items[i].kind;
	        	obj.snippet = items[i].snippet;
	        	obj.statistics = items[i].statistics;
	        	
	        	lstResults.add(obj);
	        }
		lstResults.trimToSize();
        return lstResults;
	}
}
