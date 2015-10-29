package com.hth.data;

import java.util.ArrayList;

public class YouTubeParserJSObject {

	class PageInfo {
	    int totalResults = 0;
	    int resultsPerPage = 0;
	  }
	String nextPageToken = "";
	String prevPageToken = "";
	
	public String getNextPageToken() {
		return nextPageToken;
	}

	public String getPrevPageToken() {
		return prevPageToken;
	}

	PageInfo pageInfo = null;
	YouTubeVideo [] items = null;
	
	public ArrayList<YouTubeVideo> getYouTubeVideos()
	{
		ArrayList<YouTubeVideo> lstResults = new ArrayList<YouTubeVideo>();
		if(items!=null)
	        for(int i =0; i< items.length; i++)
	        {
	        	lstResults.add(items[i]);
	        }
        
        return lstResults;
	}
	
	public int getResultsPerPage()
	{
		if(pageInfo!=null) return pageInfo.resultsPerPage;
		return 0;
	}
	
	public int getTotalResults()
	{
		if(pageInfo!=null) return pageInfo.totalResults;
		return 0;
	}
}
