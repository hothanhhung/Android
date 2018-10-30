package com.hth.utils;

import java.util.*;

public class ConfigAds
{
    public int Version;
    public List<String> DenyLinks;
    public List<CSSWeb> CSSWebs;

    public String getCSSWeb(String name){
        String rs = "";
        if(CSSWebs!=null){
            for (CSSWeb cssWeb: CSSWebs) {
                if(cssWeb.Name.equalsIgnoreCase(name))
                {
                    return cssWeb.CSS;
                }
            }
        }
        return rs;
    }

    public boolean isDenied(String name){
        if(DenyLinks!=null && name!=null){
            name = name.toLowerCase();
            for (String link: DenyLinks) {
                if(name.contains(link))
                {
                    return true;
                }
            }
        }
        return false;
    }
}

class CSSWeb
{
    public String Name;
    public String CSS;
}
