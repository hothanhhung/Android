package com.hth.data;

public class ObjectChannel {
	String id;
	String title;
	String imageUrl;
	
	public ObjectChannel()
	{
		id = "";
		title = "";
		imageUrl = "";
	}
	
	public ObjectChannel(String id, String title, String imageUrl)
	{
		this.id = id;
		this.title = title;
		this.imageUrl = imageUrl;
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
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}
	
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
