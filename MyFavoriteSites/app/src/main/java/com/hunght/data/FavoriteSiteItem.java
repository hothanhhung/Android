package com.hunght.data;

import com.hunght.utils.MethodHelpers;

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
        if(siteURL==null || siteURL.isEmpty())
        {
            this.id =  java.util.UUID.randomUUID().toString();
        }else{
            this.id =  siteURL.trim().toLowerCase();
        }
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

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }
    @Override
    public String toString() {
        return this.siteURL;
    }

    public String getFavicon(){
        if(iconURL == null || iconURL.isEmpty()){
            return findIconURL();
        }else{
            return iconURL;
        }

    }

    public String findIconURL(){
        return MethodHelpers.findIconURL(siteURL);
    }

    public String getHost(){
        return MethodHelpers.findHost(siteURL);
    }

    public String getAvatarName(){
        if(name != null && !name.isEmpty()){
            name = name.trim();
            if(name.length()<4) return name;
            String initials = "";
            for (String s : name.split(" ")) {
                if(s!=null) {
                    s = s.trim();
                    if (!s.isEmpty())
                        initials += s.charAt(0);
                    if(initials.length() == 3) break;
                }
            }
            return initials;
        }
        return "";
    }
}
