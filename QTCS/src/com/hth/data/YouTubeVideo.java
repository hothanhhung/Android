package com.hth.data;

public class YouTubeVideo {
	class Statistic{
		String viewCount = "";
		String likeCount = "";
		String dislikeCount = "";
		String favoriteCount = "";
		String commnetCount = "";
	}
	class ID {
		String kind = "";
		String videoId = "";
	}
	
	class Thumbnails{
		class URL {
			String url = "";
		}
		
		//URL [default] = null;
		URL medium = null;
		URL high = null;
	}
	
	class Snippet{
		String publishedAt = "";
		String channelId = "";
		String title = "";
		String description = "";
		Thumbnails thumbnails = null;
		String channelTitle = "";
		String liveBroadcastContent = "";
	}
	
	String kind = "";
	String etag = "";
	ID id = null;
	Snippet snippet = null;	
	Statistic statistic = null;
	
	public String getViewCount()
	{
		if(statistic!=null){
			return statistic.viewCount;
		}
		return ""; 
	}
	
	public String getTitle(){
		if(snippet!=null){
			return snippet.title;
		}
		return ""; 
	}
	
	public String getID(){
		if(id!=null){
			return id.videoId;
		}
		return ""; 
	}
	
	public String getOrginalPublishedTime(){
		if(snippet!=null){
			return snippet.publishedAt;
		}
		return ""; 
	}
	
	//2014-09-14T17:05:22.000Z
	public String getPublishedTime(){
		if(snippet!=null){
			String time = snippet.publishedAt.substring(snippet.publishedAt.indexOf("T") + 1, snippet.publishedAt.indexOf("."));
			String date = snippet.publishedAt.substring(0, snippet.publishedAt.indexOf("T"));
			String [] dt = date.split("-");
			return time + " " + dt[2] + "-" + dt[1] + "-" + dt[0];
		}
		return ""; 
	}
	
	public String getThumbnailDefaut(){
		if(snippet!=null && snippet.thumbnails!=null && snippet.thumbnails.medium != null ){			
			return snippet.thumbnails.medium.url.replace("/mqdefault.jpg", "/default.jpg");
		}
		return ""; 
	}
}
