package com.hth.data;

public class YouTubeVideo extends YouTubeVideoBase {
	
	class ID {
		String kind = "";
		String videoId = "";
	}
	

	void setvideoId(String strid) 
	{
		if(id==null) id = new ID();
		id.videoId = strid;
	};
	
	
	ID id = null;
	
	public String getID(){
		if(id!=null){
			return id.videoId;
		}
		return ""; 
	}
}
