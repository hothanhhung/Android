package com.hth.data;

public class YouTubeVideoBase{
	class Statistic{
		String viewCount = "";
		String likeCount = "";
		String dislikeCount = "";
		String favoriteCount = "";
		String commentCount = "";
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
	Snippet snippet = null;
	Statistic statistics = null;

	public String getTitle(){
		if(snippet!=null){
			return snippet.title;
		}
		return "";
	}

	//2014-09-14T17:05:22.000Z
		public String getPublishedTime(){
			if(snippet!=null){
				String time = snippet.publishedAt.substring(snippet.publishedAt.indexOf("T") + 1, snippet.publishedAt.indexOf("."));
				String date = snippet.publishedAt.substring(0, snippet.publishedAt.indexOf("T"));
				String [] dt = date.split("-");
				return time + " " + dt[1] + "-" + dt[2] + "-" + dt[0];
			}
			return "";
		}

		public String getOrginalPublishedTime(){
			if(snippet!=null){
				return snippet.publishedAt;
			}
			return "";
		}

		public String getThumbnailDefaut(){
			if(snippet!=null && snippet.thumbnails!=null && snippet.thumbnails.medium != null ){
				return snippet.thumbnails.medium.url.replace("/mqdefault.jpg", "/default.jpg");
			}
			return "";
		}

		public String getThumbnailMedium(){
			if(snippet!=null && snippet.thumbnails!=null && snippet.thumbnails.medium != null ){
				return snippet.thumbnails.medium.url;
			}
			return "";
		}

		public String getViewCount()
		{
			if(statistics!=null) return statistics.viewCount;
			return "0";
		}
		public String getLikeCount()
		{
			if(statistics!=null) return statistics.likeCount;
			return "0";
		}
		public String getDislikeCount()
		{
			if(statistics!=null) return statistics.dislikeCount;
			return "0";
		}
		public String getFavoriteCount()
		{
			if(statistics!=null) return statistics.favoriteCount;
			return "0";
		}
		public String getCommentCount()
		{
			if(statistics!=null) return statistics.commentCount;
			return "0";
		}
		public String getDescription()
		{
			if(snippet!=null) return snippet.description;//.replace("XEM TH�M: http://www.youtube.com/QuaTangCuocSong4U", "");
			return "";
		}
}
