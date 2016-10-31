package com.hth.data;

import android.os.StrictMode;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hth.service.Areas;
import com.hth.service.Desk;
import com.hth.service.MenuOrder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.os.StrictMode.*;

/**
 * Created by Lenovo on 10/31/2016.
 */
public class ServiceProcess {
    static LoginResponse loginInfo;
    static String serverLink = "http://restapi.somee.com";
    static class LoginResponse{
        String access_token;
        String token_type;

        public String getAccessToken() {
            return access_token;
        }

        public String getTokenType() {
            return token_type;
        }

        public String getToken()
        {
            return token_type + " " + access_token;
        }

        public boolean hasAccessToken()
        {
            if(access_token == null || access_token.isEmpty()){
                return false;
            }else{
                return true;
            }
        }
    }

    static public ArrayList<Areas> getAreas() {
        ArrayList<Areas> areas = new ArrayList<Areas>();
        ArrayList<Desk> Desks = getDesks();
        if(Desks != null && Desks.size() != 0){
            HashMap<String, ArrayList<Desk>> hashMap = new HashMap<String, ArrayList<Desk>>();
            for (Desk desk:Desks) {
                if (!hashMap.containsKey(desk.getAreaId())) {
                    ArrayList<Desk> list = new ArrayList<Desk>();
                    list.add(desk);

                    hashMap.put(desk.getAreaId(), list);
                } else {
                    hashMap.get(desk.getAreaId()).add(desk);
                }
            }
            for (Map.Entry<String, ArrayList<Desk>> entry : hashMap.entrySet()) {
                ArrayList<Desk> desks = entry.getValue();
                Areas area = new Areas(desks.get(0).getAreas());
                area.setDesks(entry.getValue());
                areas.add(area);
            }
        }

        return areas;
    }

    static public ArrayList<Desk> getDesks() {
        String link = serverLink + "/api/Manage/GetListDesk";
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(link);

        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            //execute http post
            httpget.addHeader("Authorization",loginInfo.getToken());
            httpget.addHeader("Accept","application/json");
            HttpResponse response = httpclient.execute(httpget);

            HttpEntity httpEntity = response.getEntity();
            StringBuilder jsonStringBuilder = new StringBuilder();
            InputStream in = httpEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                jsonStringBuilder.append(inputLine);
            }
            reader.close();
            String json = jsonStringBuilder.toString();
            Gson gSon = new Gson();
            Type collectionType = new TypeToken<ArrayList<Desk>>() {
            }.getType();
            return gSon.fromJson(json, collectionType);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static public ArrayList<MenuOrder> getMenuOrder() {
        String link = serverLink + "/api/Manage/GetListMenuOrder";
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(link);

        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            //execute http post
            httpget.addHeader("Authorization",loginInfo.getToken());
            httpget.addHeader("Accept","application/json");
            HttpResponse response = httpclient.execute(httpget);

            HttpEntity httpEntity = response.getEntity();
            StringBuilder jsonStringBuilder = new StringBuilder();
            InputStream in = httpEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                jsonStringBuilder.append(inputLine);
            }
            reader.close();
            String json = jsonStringBuilder.toString();
            Gson gSon = new Gson();
            Type collectionType = new TypeToken<ArrayList<MenuOrder>>() {
            }.getType();
            return gSon.fromJson(json, collectionType);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static public String login(String user, String pass) {
        String link = serverLink + "/Token";
        String urlParameters = "grant_type=password&username=" + user + "&password=" + pass;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(link);

        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            //add data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("grant_type", "password"));
            nameValuePairs.add(new BasicNameValuePair("username", user));
            nameValuePairs.add(new BasicNameValuePair("password", pass));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            //execute http post
            HttpResponse response = httpclient.execute(httppost);

            HttpEntity httpEntity = response.getEntity();
            StringBuilder jsonStringBuilder = new StringBuilder();
            InputStream in = httpEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                jsonStringBuilder.append(inputLine);
            }
            reader.close();
            String json = jsonStringBuilder.toString();
            Gson gSon = new Gson();
            Type collectionType = new TypeToken<LoginResponse>() {
            }.getType();
            loginInfo = gSon.fromJson(json, collectionType);
            if (loginInfo == null || !loginInfo.hasAccessToken()) {
                return "Tên đăng nhập và mật khẩu không đúng";
            } else {
                return "";

            }

        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }


    static public boolean login1(String user, String pass) {
        String link = serverLink + "/Token";
        String urlParameters = "grant_type=password&username=" + user + "&password=" + pass;
        Gson gSon = new Gson();
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            /*connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(urlParameters.getBytes().length));

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();*/

            //Get Response
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            StringBuilder jsonStringBuilder = new StringBuilder();
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                jsonStringBuilder.append(inputLine);
            }
            reader.close();
            String json = jsonStringBuilder.toString();

            Type collectionType = new TypeToken<LoginResponse>() {
            }.getType();
            loginInfo = gSon.fromJson(json, collectionType);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
