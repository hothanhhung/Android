package com.hth.data;

public class YouTubeVideoDetail extends YouTubeVideoBase {
	
	class ContentDetail
	{
		int itemCount = 0;
	}
	
	ContentDetail contentDetails = null;
	
	String id = "";
	
	public String getID(){
		return id; 
	}
	
	public int getItemCount()
	{
		if(contentDetails == null) return 0;
		return contentDetails.itemCount;
	}
	
	
}
