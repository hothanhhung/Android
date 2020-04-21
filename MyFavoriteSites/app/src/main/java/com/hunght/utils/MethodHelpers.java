package com.hunght.utils;

import java.net.URL;

public class MethodHelpers {
    public static String findIconURL(String siteURL) {
        try {
            URL url = new URL(siteURL);
            String host = url.getProtocol() + "://" + url.getHost();
            if (url.getPort() != -1 && url.getPort() != 80) {
                host += host + ":" + url.getPort();
            }
            return host + "/favicon.ico";
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String findHost(String siteURL) {
        try {
            URL url = new URL(siteURL);
            return url.getHost();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

}
