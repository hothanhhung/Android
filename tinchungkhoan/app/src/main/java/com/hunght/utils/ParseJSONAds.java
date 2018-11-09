package com.hunght.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.StrictMode;

public class ParseJSONAds {
	private static final String APP_SHARED_PREFS = "com.hungho.tracuuonline";
	static public final String SERVER_DEFAULT = "http://hunght.com/api/ads/getAds/";
	static public final String SERVER_DEFAULT_1 = "http://hunght.com/api/ads/userclickad/";
	static public final String RECODE_SERVER = "RECODE_SERVER";

	private Context context;
	String country;
	String os;

	static private String openKey;
	static private String androidId;
	static private String appVersion;
	static private String packageName;

	private String getAndroidId(){
		if(androidId == null || androidId.isEmpty())
		{
			androidId = MethodsHelper.getAndroidId(context);
		}
		return androidId;
	}

	private String getAppVersion(){
		if(appVersion == null || appVersion.isEmpty())
		{
			appVersion = MethodsHelper.getAppVersion(context);
		}
		return appVersion;
	}

	private String getPackageName()
	{
		if(packageName == null || packageName.isEmpty())
		{
			packageName = MethodsHelper.getPackageName(context);
		}
		return packageName;
	}

	public ParseJSONAds(Context context, String os){
		this.context = context;
		this.country =  "VN";
		this.os = os;
		if( openKey == null || openKey.isEmpty())
		{
			openKey = MethodsHelper.getCurrentDateToOrder();
		}
	}

	private void setServer(String newDomain) {
		SharedPreferences appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, 0);
		SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
		prefsEditor.putString(RECODE_SERVER, newDomain);
		prefsEditor.commit();
	}

	private String getServer()
	{
		SharedPreferences appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, 0);
		return appSharedPrefs.getString(RECODE_SERVER, SERVER_DEFAULT);
	}

	private String getLinkServer() {
		return getServer() + "?country=" + country + "&os=" + os + "&device=" + getAndroidId() + "&open=" + openKey + "&version=" + getAppVersion() + "&package=" + getPackageName();
	}

	private String getLinkServerUserClick(String link) {
		return SERVER_DEFAULT_1 + "?country=" + country + "&os=" + os + "&info=" + link + "&device=" + getAndroidId() + "&open=" + openKey + "&version=" + getAppVersion() + "&package=" + getPackageName();
	}
	public ArrayList<AdItem> getAds()
	{
		String link = getLinkServer();
		//Log.d("getAdFlexes", link);

		ResponseJson<AdItem> responseJson = new ResponseJson<AdItem>();
		Gson gSon = new Gson();
		try {
			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
						.permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}

			StringBuilder jsonStringBuilder = new StringBuilder();
			BufferedReader input = new BufferedReader(new InputStreamReader(new URL(link).openStream(), "UTF-8"));

			String inputLine;
			while ((inputLine = input.readLine()) != null)
			{
				jsonStringBuilder.append(inputLine);
			}
			input.close();
			String json = jsonStringBuilder.toString();

			Type collectionType = new TypeToken<ResponseJson<AdItem>>(){}.getType();
			responseJson = gSon.fromJson(json, collectionType);
			if(responseJson.NeedChangeDomain){
				setServer(responseJson.NewDomain);
			}
		}catch(Exception ex)
		{
			ex.printStackTrace();
			return getLocalAds();
		}
		return responseJson.Data;
	}

	public void userClickAd(String toLink)
	{
		String link = getLinkServerUserClick(Uri.encode(toLink));
		//Log.d("getAdFlexes", link);

		ResponseJson<String> responseJson = new ResponseJson<String>();
		Gson gSon = new Gson();
		try {
			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
						.permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}

			StringBuilder jsonStringBuilder = new StringBuilder();
			BufferedReader input = new BufferedReader(new InputStreamReader(new URL(link).openStream(), "UTF-8"));

			String inputLine;
			while ((inputLine = input.readLine()) != null)
			{
				jsonStringBuilder.append(inputLine);
			}
			input.close();
			String json = jsonStringBuilder.toString();

			Type collectionType = new TypeToken<ResponseJson<String>>(){}.getType();
			responseJson = gSon.fromJson(json, collectionType);
			if(responseJson.NeedChangeDomain){
				//	setServer(responseJson.NewDomain);
			}
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private ArrayList<AdItem> getLocalAds()
	{
		ArrayList<AdItem> adFlexes = new ArrayList<AdItem>();
		AdItem firstAdFlex = new AdItem();
		firstAdFlex.setName("Ủng hộ tác giả bằng cách down và cài đặt Game và ứng dụng");
		firstAdFlex.setDesc("Đối với file APK vui lòng bật chế độ cho phép cài đặt nguồn ngoài Google play \n Settings > Security > ☑ Unknown Sources.");
		adFlexes.add(firstAdFlex);

		AdItem firstAdFlex0 = new AdItem();
		firstAdFlex0.setType("Google play");
		firstAdFlex0.setName("Sudoku");
		firstAdFlex0.setDesc("The Sudoku game is famous in the world. The rule is simple. However, it is not easy to find solution.");
		firstAdFlex0.setLink("https://play.google.com/store/apps/details?id=com.hth.sudoku");
		firstAdFlex0.setUrlImage("https://lh3.googleusercontent.com/7BFgI4j1PX7f-J6B47BqzuW2C5DGqQ-WpbcRcZQjSI6pQO7CXNQSprjOXHEDilXaY9Q=w300");
		adFlexes.add(firstAdFlex0);

		AdItem firstAdFlex2 = new AdItem();
		firstAdFlex2.setType("Google play");
		firstAdFlex2.setName("Đọc Truyện Cổ");
		firstAdFlex2.setDesc("Với ứng dụng như một sách truyện giúp bạn có thể đọc offline các truyện");
		firstAdFlex2.setLink("https://play.google.com/store/apps/details?id=com.hth.doctruyenco");
		firstAdFlex2.setUrlImage("https://lh3.ggpht.com/wk8buIq8ybv9vjZ1GPQ1q6NiClaZDJJ1KzQ3EBCAhxretCryfJRsXaLxrXjLSdLjTw=w300");
		adFlexes.add(firstAdFlex2);

		AdItem firstAdFlex3 = new AdItem();
		firstAdFlex3.setType("Google play");
		firstAdFlex3.setName("Hài Tổng Hợp");
		firstAdFlex3.setDesc("Ứng dụng hài tổng hợp có các kênh hài, video hài, và clip hài đang được yêu thích trên youtube: Hài tuyển chọn, 5S Online, YEAH1TV, DAM tv, BB&BG Entertainment,...");
		firstAdFlex3.setLink("https://play.google.com/store/apps/details?id=com.hth.haitonghop");
		firstAdFlex3.setUrlImage("https://lh3.googleusercontent.com/IJA3lu0hYrH4ZkSqe3b92dHs5NZfiribIWBguyh-HB5ISFFoo4aOEMVogmbhe7UEcw=w300");
		adFlexes.add(firstAdFlex3);

		AdItem firstAdFlex1 = new AdItem();
		firstAdFlex1.setType("Google play");
		firstAdFlex1.setName("Animal Connection");
		firstAdFlex1.setDesc("Game Pikachu mới, càng chơi càng thú vị");
		firstAdFlex1.setLink("https://play.google.com/store/apps/details?id=com.hth.animalconnection");
		firstAdFlex1.setUrlImage("https://lh3.googleusercontent.com/UifZjCgWDSnWQ6c-9ukm8e8osK9VthxPvgAqEaLBpZJOWYad8ncIl-AL0Sokn5yRLOU=w300");
		adFlexes.add(firstAdFlex1);


		AdItem firstAdFlex4 = new AdItem();
		firstAdFlex4.setType("Google play");
		firstAdFlex4.setName("Quà Tặng Cuộc Sống");
		firstAdFlex4.setDesc("Ứng dụng xem lại video Quà Tặng Cuộc sống phát sóng trên VTV3 lúc 22:15 hàng ngày");
		firstAdFlex4.setLink("https://play.google.com/store/apps/details?id=com.hth.qtcs");
		firstAdFlex4.setUrlImage("https://lh3.googleusercontent.com/kJ4kA4PS2GSNYKkLjW9UC3s2FTTvi6nacJOKSkXlL1Nm-VWgxtnb2geGWfqjh94dCg=w300");
		adFlexes.add(firstAdFlex4);

		AdItem firstAdFlex5 = new AdItem();
		firstAdFlex5.setType("Google play");
		firstAdFlex5.setName("Fruit Link");
		firstAdFlex5.setDesc("Game Pikachu mới, càng chơi càng thú vị");
		firstAdFlex5.setLink("https://play.google.com/store/apps/details?id=com.hth.fruitlink");
		firstAdFlex5.setUrlImage("https://lh3.googleusercontent.com/xBiot__H7a2OBnjijQXxS5xAEhLrbM6hQ2FdUhrP4ukS69n7_FlBR3RIu_L_FtHEZg=w300");
		adFlexes.add(firstAdFlex5);

		AdItem firstAdFlex6 = new AdItem();
		firstAdFlex6.setType("Google play");
		firstAdFlex6.setName("Photo Puzzle");
		firstAdFlex6.setDesc("Game xếp hình, càng chơi càng thú vị");
		firstAdFlex6.setLink("https://play.google.com/store/apps/details?id=com.hth.photopuzzle");
		firstAdFlex6.setUrlImage("https://lh3.googleusercontent.com/5MBrxlD_Ct1D9QEQbqRVw0dxajJXGfsnk2JhkejsfchVKK6CcFDSOuewfixqV9oycw=w300");
		adFlexes.add(firstAdFlex6);

		AdItem firstAdFlex7 = new AdItem();
		firstAdFlex7.setType("Google play");
		firstAdFlex7.setName("Files Transfer");
		firstAdFlex7.setDesc("Chuyển và quản lý các tập tin trên Android của bạn từ các thiết bị khác thông qua WiFi.");
		firstAdFlex7.setLink("https://play.google.com/store/apps/details?id=com.hth.filestransfer");
		firstAdFlex7.setUrlImage("https://lh3.googleusercontent.com/EJvOjPkghTWeg_a-eH3fuimTpjKMmL5kp7ummS_FBPbhAoddB_Ur9tmZfzJTl_Zl32A=w300");
		adFlexes.add(firstAdFlex7);


		AdItem firstAdFlex8 = new AdItem();
		firstAdFlex8.setType("Google play");
		firstAdFlex8.setName("Learn English By Videos");
		firstAdFlex8.setDesc("Tổng hợp các kênh học tiếng anh hay trên youtube");
		firstAdFlex8.setLink("https://play.google.com/store/apps/details?id=com.hth.learnenglishbyvideos");
		firstAdFlex8.setUrlImage("https://lh3.ggpht.com/jPHpzWNuoFd9M0fcKb3gkIUAuXllrJ58vabIPQiWpqRGA-toG3YEl-BczsTTbAjbK58=w300");
		adFlexes.add(firstAdFlex8);

		return adFlexes;
	}
}
