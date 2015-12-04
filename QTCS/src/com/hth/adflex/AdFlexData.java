package com.hth.adflex;

public class AdFlexData {
	
	public String title;
	public String desc;
	public String type;
	public String link;
	public String appid;
	public IconObject icon;
	
	public String getAppId()
	{
		if(appid == null) return "";
		return appid;
	}
	
	public void setName(String name)
	{
		title = name;
	}
	
	
	public String getName()
	{
		if(title == null) return "";
		return title;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}
	
	public String getDesc()
	{
		if(desc == null) return "";
		return desc;
	}
	
	public String GetFullType()
	{
		if(type == null) return "";
		else if(type.contains("gplay")) return "Google play";
		else if(type.contains("apk")) return "File APK";
		return type;
	}

	public String getLink()
	{
		if(link == null) return "";
		return link;
	}
	
	public String getUrlImage()
	{
		if(icon != null) return icon.getfullLink();
		return "";
	}
	
	public class IconObject
	{
		public String full;
		public String getfullLink()
		{
			return full;
		}
	}
}
