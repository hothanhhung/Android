package com.hth.data;

public class ObjectFavorite {
	String channelId;
	String id;
	String title;
	String path;
	String time;
	
	public ObjectFavorite()
	{
		channelId = "";
		id = "";
		title = "";
		path = "";
		time = "";
	}
	
	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String titile) {
		this.title = titile;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
