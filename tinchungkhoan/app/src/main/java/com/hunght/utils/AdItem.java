package com.hunght.utils;

public class AdItem {

	public String Name;
	public String Desc;
	public String Type;
	public String Link;
	public String AppId;
	public String UrlImage;

        /*---------- For owner ads --------- */

	public String InCountries; //VN, EN,

	public String IgnoreCountries; //VN, EN,

	public String getName() {
		if(Name == null){
			return "";
		}
		else{
			return Name;
		}
	}

	public void setName(String name) {
		Name = name;
	}

	public String getDesc() {
		if(Desc == null){
			return "";
		}
		else{
			return Desc;
		}
	}

	public void setDesc(String desc) {
		Desc = desc;
	}

	public String getType() {
		if(Type == null){
			return "";
		}
		else{
			return Type;
		}
	}

	public void setType(String type) {
		Type = type;
	}

	public String getLink() {
		if(Link == null){
			return "";
		}
		else{
			return Link;
		}
	}

	public void setLink(String link) {
		Link = link;
	}

	public String getAppId() {
		if(AppId == null){
			return "";
		}
		else{
			return AppId;
		}
	}

	public void setAppId(String appId) {
		AppId = appId;
	}

	public String getUrlImage() {
		if(UrlImage == null){
			return "";
		}
		else{
			return UrlImage;
		}
	}

	public void setUrlImage(String urlImage) {
		UrlImage = urlImage;
	}
}
