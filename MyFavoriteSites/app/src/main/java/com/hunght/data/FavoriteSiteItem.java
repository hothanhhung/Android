package com.hunght.data;

import java.net.URL;

public class FavoriteSiteItem {

    private String id;
    public String name;
    public String iconURL;
    public String siteURL;

    public FavoriteSiteItem(){
        this.id =  java.util.UUID.randomUUID().toString();
    }

    public FavoriteSiteItem(String name, String iconURL, String siteURL){
        this.id =  java.util.UUID.randomUUID().toString();
        this.name = name;
        this.iconURL = iconURL;
        this.siteURL = siteURL.toLowerCase();
        if(!this.siteURL.startsWith("http://") && !this.siteURL.startsWith("https://")){
            this.siteURL = "http://" + this.siteURL;
        }
    }

    public void updateFrom(FavoriteSiteItem item){
        if(item!=null){
            this.name = item.name;
            this.iconURL = item.iconURL;
            this.siteURL = item.siteURL;
        }
    }

    public boolean isEmpty() {
        return name==null || name.isEmpty() || siteURL==null || siteURL.isEmpty();
    }

    public boolean isExistIconUrl() {
        return iconURL!=null && !iconURL.isEmpty();
    }

    public String getName() {
        return name;
    }

    public String getIconURL() {
        return iconURL;
    }

    public String getSiteURL() {
        return siteURL;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSiteURL(String siteURL) {
        this.siteURL = siteURL;
    }

    @Override
    public String toString() {
        return this.id;
    }

    public String getFavicon(){
        try {
            URL url = new URL(siteURL);
            String host = url.getProtocol() + "://" + url.getHost();
            if(url.getPort() != -1 && url.getPort() != 80 ){
                host += host + ":" + url.getPort();
            }
            return host + "/favicon.ico";
        }catch (Exception ex){ex.printStackTrace();}
        return "";
    }

}
