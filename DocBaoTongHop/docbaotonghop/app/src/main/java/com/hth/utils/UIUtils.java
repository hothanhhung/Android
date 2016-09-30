package com.hth.utils;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hth.adflex.AdFlexData;
import com.hth.adflex.ParseJSONAdFlex;
import com.hth.docbaotonghop.R;

public class UIUtils {
	public static Dialog showProgressDialog(Dialog loadingDialog, Activity activity)
	{
		if ((loadingDialog == null) || (!loadingDialog.isShowing())) {
			loadingDialog= new Dialog(activity);
			loadingDialog.getWindow().getCurrentFocus();
			Drawable d = new ColorDrawable(Color.BLACK);
			d.setAlpha(210);
			loadingDialog.getWindow().setBackgroundDrawable(d);
			loadingDialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
			loadingDialog.setContentView(R.layout.loading_dialog);
			loadingDialog.setCancelable(false);
			loadingDialog.setOwnerActivity(activity);

			loadingDialog.show();
	    } else {
	    	loadingDialog.setOwnerActivity(activity);
	    }
		return loadingDialog;
	}
	
	public static AlertDialog showAlertError(final Activity activity, final Boolean isCloseThis)
	{
		AlertDialog alertDialog = new android.app.AlertDialog.Builder(activity)
        .setTitle("Error")
        .setMessage(R.string.error_internet_or_parser)
        .setNeutralButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                if(isCloseThis) activity.finish();
            }
        })
        .setIcon(android.R.drawable.ic_dialog_info)
        .show();
		return alertDialog;
	}
	
	public static void showAlertAbout(final Activity activity)
	{
		new android.app.AlertDialog.Builder(activity)
	    .setTitle("About")
	    .setMessage(R.string.content_about)
	    .setNeutralButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        })
	    .setIcon(android.R.drawable.ic_dialog_info)
	    .show();
	}

    public static void showAlertInform(final Activity activity, String message)
    {
        new android.app.AlertDialog.Builder(activity)
                .setTitle("Thông báo")
                .setMessage(message)
                .setNeutralButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    public static ProgressDialog showPopUpLoading(final Activity activity)
    {
        return ProgressDialog.show(activity, "Loading", "Please wait for a moment...");
    }

    public static Boolean isOnline(Context ctx)
    {
        ConnectivityManager cm =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnected();

    }

    public static AlertDialog showAlertErrorNoInternet(final Activity activity, final Boolean isCloseThis)
    {
        AlertDialog alertDialog = new android.app.AlertDialog.Builder(activity)
                .setTitle(R.string.error_title)
                .setMessage(R.string.error_internet)
                .setNeutralButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                if(isCloseThis) activity.finish();
                            }
                        })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
        return alertDialog;
    }
    
    private static ProgressBar progressBar1;
    public static AlertDialog showAlertGetMoreApps(final Activity activity, String urlPage)
	{		
    	AlertDialog.Builder alert = new AlertDialog.Builder(activity);

		WebView wv = new WebView(activity);
        WebSettings settings = wv.getSettings();
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
		settings.setJavaScriptEnabled(true);
        settings.setAppCacheMaxSize(50000 * 1024);
        
		progressBar1 = new ProgressBar(activity);//,null, android.R.attr.progressBarStyleHorizontal);
		progressBar1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		wv.setWebViewClient(new WebViewClient() {
		    @Override
		    public boolean shouldOverrideUrlLoading(WebView view, String url) {
		    	Intent i = new Intent(Intent.ACTION_VIEW);
            	i.setData(Uri.parse(url));
            	activity.startActivity(i);
		        return true;
		    }
		});
		wv.setWebChromeClient(new WebChromeClient() {
		    public void onProgressChanged(WebView view, int progress) {
            	if(progress < 95)
            	{
            		progressBar1.setVisibility(View.VISIBLE);
            		if(progressBar1.getProgress() < progress){
            			progressBar1.setProgress(progress);
            		}
            	}else
            	{
            		view.setVisibility(View.VISIBLE);
            		progressBar1.setVisibility(View.GONE);
            	}
            }
		});
		wv.loadUrl(urlPage);
		LinearLayout linearLayout = new LinearLayout(activity);
		if (android.os.Build.VERSION.SDK_INT>=17) {
			// call something for API Level 11+
			linearLayout.setLayoutDirection(LinearLayout.LAYOUT_DIRECTION_LTR);
		}
		linearLayout.addView(progressBar1);
		linearLayout.addView(wv);
		alert.setView(linearLayout);
		alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int id) {
		        dialog.dismiss();
		    }
		});
		//alert.setIcon(android.R.drawable.box_new)
		return alert.show();
		
	}

	public static LinearLayout BuildGetMoreApps(final Activity activity)
	{
		ArrayList<AdFlexData> adFlexDataList = ParseJSONAdFlex.getAdFlexes("vn");

		ArrayList<AdFlexData> adFlexes = new ArrayList<AdFlexData>();
		AdFlexData firstAdFlex = new AdFlexData();
		firstAdFlex.setName("Ủng hộ tác giả bằng cách down và cài đặt Game và ứng dụng");
		firstAdFlex.setDesc("Đối với file APK vui lòng bật chế độ cho phép cài đặt nguồn ngoài Google play \n Settings > Security > ☑ Unknown Sources.");
		adFlexes.add(firstAdFlex);

		AdFlexData firstAdFlex2 = new AdFlexData();
		firstAdFlex2.setType("Google play");
		firstAdFlex2.setName("Đọc Truyện Cổ");
		firstAdFlex2.setDesc("Với ứng dụng như một sách truyện giúp bạn có thể đọc offline các truyện");
		firstAdFlex2.setLink("https://play.google.com/store/apps/details?id=com.hth.doctruyenco");
		firstAdFlex2.setUrlImage("https://lh3.ggpht.com/wk8buIq8ybv9vjZ1GPQ1q6NiClaZDJJ1KzQ3EBCAhxretCryfJRsXaLxrXjLSdLjTw=w300");
		adFlexes.add(firstAdFlex2);

		AdFlexData firstAdFlex3 = new AdFlexData();
		firstAdFlex3.setType("Google play");
		firstAdFlex3.setName("Hài Tổng Hợp");
		firstAdFlex3.setDesc("Ứng dụng hài tổng hợp có các kênh hài, video hài, và clip hài đang được yêu thích trên youtube: Hài tuyển chọn, 5S Online, YEAH1TV, DAM tv, BB&BG Entertainment,...");
		firstAdFlex3.setLink("https://play.google.com/store/apps/details?id=com.hth.haitonghop");
		firstAdFlex3.setUrlImage("https://lh3.googleusercontent.com/IJA3lu0hYrH4ZkSqe3b92dHs5NZfiribIWBguyh-HB5ISFFoo4aOEMVogmbhe7UEcw=w300");
		adFlexes.add(firstAdFlex3);

		AdFlexData firstAdFlex1 = new AdFlexData();
		firstAdFlex1.setType("Google play");
		firstAdFlex1.setName("Animal Connection");
		firstAdFlex1.setDesc("Game Pikachu mới, càng chơi càng thú vị");
		firstAdFlex1.setLink("https://play.google.com/store/apps/details?id=com.hth.animalconnection");
		firstAdFlex1.setUrlImage("https://lh3.googleusercontent.com/UifZjCgWDSnWQ6c-9ukm8e8osK9VthxPvgAqEaLBpZJOWYad8ncIl-AL0Sokn5yRLOU=w300");
		adFlexes.add(firstAdFlex1);


		AdFlexData firstAdFlex4 = new AdFlexData();
		firstAdFlex4.setType("Google play");
		firstAdFlex4.setName("Quà Tặng Cuộc Sống");
		firstAdFlex4.setDesc("Ứng dụng xem lại video Quà Tặng Cuộc sống phát sóng trên VTV3 lúc 22:15 hàng ngày");
		firstAdFlex4.setLink("https://play.google.com/store/apps/details?id=com.hth.qtcs");
		firstAdFlex4.setUrlImage("https://lh3.googleusercontent.com/kJ4kA4PS2GSNYKkLjW9UC3s2FTTvi6nacJOKSkXlL1Nm-VWgxtnb2geGWfqjh94dCg=w300");
		adFlexes.add(firstAdFlex4);

		AdFlexData firstAdFlex5 = new AdFlexData();
		firstAdFlex5.setType("Google play");
		firstAdFlex5.setName("Fruit Link");
		firstAdFlex5.setDesc("Game Pikachu mới, càng chơi càng thú vị");
		firstAdFlex5.setLink("https://play.google.com/store/apps/details?id=com.hth.fruitlink");
		firstAdFlex5.setUrlImage("https://lh3.googleusercontent.com/xBiot__H7a2OBnjijQXxS5xAEhLrbM6hQ2FdUhrP4ukS69n7_FlBR3RIu_L_FtHEZg=w300");
		adFlexes.add(firstAdFlex5);

		AdFlexData firstAdFlex6 = new AdFlexData();
		firstAdFlex6.setType("Google play");
		firstAdFlex6.setName("Photo Puzzle");
		firstAdFlex6.setDesc("Game xếp hình, càng chơi càng thú vị");
		firstAdFlex6.setLink("https://play.google.com/store/apps/details?id=com.hth.photopuzzle");
		firstAdFlex6.setUrlImage("https://lh3.googleusercontent.com/5MBrxlD_Ct1D9QEQbqRVw0dxajJXGfsnk2JhkejsfchVKK6CcFDSOuewfixqV9oycw=w300");
		adFlexes.add(firstAdFlex6);

		AdFlexData firstAdFlex7 = new AdFlexData();
		firstAdFlex7.setType("Google play");
		firstAdFlex7.setName("Files Transfer");
		firstAdFlex7.setDesc("Chuyển và quản lý các tập tin trên Android của bạn từ các thiết bị khác thông qua WiFi.");
		firstAdFlex7.setLink("https://play.google.com/store/apps/details?id=com.hth.filestransfer");
		firstAdFlex7.setUrlImage("https://lh3.googleusercontent.com/EJvOjPkghTWeg_a-eH3fuimTpjKMmL5kp7ummS_FBPbhAoddB_Ur9tmZfzJTl_Zl32A=w300");
		adFlexes.add(firstAdFlex7);


		AdFlexData firstAdFlex8 = new AdFlexData();
		firstAdFlex8.setType("Google play");
		firstAdFlex8.setName("Learn English By Videos");
		firstAdFlex8.setDesc("Tổng hợp các kênh học tiếng anh hay trên youtube");
		firstAdFlex8.setLink("https://play.google.com/store/apps/details?id=com.hth.learnenglishbyvideos");
		firstAdFlex8.setUrlImage("https://lh3.ggpht.com/jPHpzWNuoFd9M0fcKb3gkIUAuXllrJ58vabIPQiWpqRGA-toG3YEl-BczsTTbAjbK58=w300");
		adFlexes.add(firstAdFlex8);

		for(int i = adFlexDataList.size(); i>0 ; i--)
		{
			AdFlexData adFlex = adFlexDataList.get(i-1);
			if(!adFlex.getAppId().contains("gameloft"))
			{
				adFlexes.add(adFlex);
			}
		}
		ListView listView = new ListView(activity);
		com.hth.adflex.FlexibleRowAdapter adapter = new com.hth.adflex.FlexibleRowAdapter(activity, adFlexes, activity.getResources());
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(position > 0){
					//check internet
					if(!UIUtils.isOnline(activity)){
						UIUtils.showAlertErrorNoInternet(activity, false);
						return;
					}
					String url = view.findViewById(R.id.title).getTag().toString();
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(url));
					activity.startActivity(i);
				}
			}
		});
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		layoutParams.setMargins(0, 0, 0, 50);
		listView.setLayoutParams(layoutParams);
		LinearLayout linearLayout = new LinearLayout(activity);
		if (android.os.Build.VERSION.SDK_INT>=17) {
			// call something for API Level 11+
			linearLayout.setLayoutDirection(LinearLayout.LAYOUT_DIRECTION_LTR);
		}

		//linearLayout.addView(progressBar1);
		linearLayout.addView(listView);
		return linearLayout;
	}

    public static LinearLayout OldBuildGetMoreApps(final Activity activity)
    {
    	TelephonyManager tm = (TelephonyManager)activity.getSystemService(Activity.TELEPHONY_SERVICE);
    	String countryCodeValue = tm.getNetworkCountryIso();
    	ArrayList<AdFlexData> adFlexDataList = ParseJSONAdFlex.getAdFlexes(countryCodeValue);
    	
    	ArrayList<AdFlexData> adFlexes = new ArrayList<AdFlexData>();
    	AdFlexData firstAdFlex = new AdFlexData();
    	firstAdFlex.setName("Ủng hộ tác giả bằng cách down và cài đặt Game và ứng dụng");
    	firstAdFlex.setDesc("Đối với file APK vui lòng bật chế độ cho phép cài đặt nguồn ngoài Google play \n Settings > Security > ☑ Unknown Sources.");
    	adFlexes.add(firstAdFlex);
    	
    	for(int i = adFlexDataList.size() - 1; i > -1 ; i--)
    	{
    		AdFlexData adFlex = adFlexDataList.get(i);
    		if(!adFlex.getAppId().contains("gameloft"))
    		{
    			adFlexes.add(adFlex);
    		}
    	}
    	ListView listView = new ListView(activity);
		com.hth.adflex.FlexibleRowAdapter adapter = new com.hth.adflex.FlexibleRowAdapter(activity, adFlexes, activity.getResources());
    	listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	if(position > 0){
            		//check internet
	                if(!UIUtils.isOnline(activity)){
	                	UIUtils.showAlertErrorNoInternet(activity, false);
	                	return;
	                }     
	                String url = view.findViewById(R.id.title).getTag().toString();
	                Intent i = new Intent(Intent.ACTION_VIEW);
	            	i.setData(Uri.parse(url));
	            	activity.startActivity(i);
	            }
            }
        });
	    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
	    layoutParams.setMargins(0, 0, 0, 50);
	    listView.setLayoutParams(layoutParams);
		LinearLayout linearLayout = new LinearLayout(activity);
		//linearLayout.setLayoutDirection(LinearLayout.VERTICAL);
		//linearLayout.addView(progressBar1);
		linearLayout.addView(listView);
		return linearLayout;
    }
    
    public static AlertDialog showAlertGetMoreApps(final Activity activity)
	{		
    	AlertDialog.Builder alert = new AlertDialog.Builder(activity);
    	
		alert.setView(BuildGetMoreApps(activity));
		alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int id) {
		        dialog.dismiss();
		    }
		});
		//alert.setIcon(android.R.drawable.box_new)
		return alert.show();
		
	}

	public static LinearLayout BuildGetMoreAppsServer(final Activity activity)
	{
		final ParseJSONAds parseJSONAds = new ParseJSONAds(activity, "VN", "android");
		ArrayList<AdItem> adItems = parseJSONAds.getAds();

		ListView listView = new ListView(activity);
		FlexibleRowAdapter adapter = new FlexibleRowAdapter(activity, adItems, activity.getResources());
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(position > 0){
					//check internet
					if(!UIUtils.isOnline(activity)){
						UIUtils.showAlertErrorNoInternet(activity, false);
						return;
					}
					final AdItem adItem = (AdItem) view.findViewById(R.id.title).getTag();
					if(adItem.getLink() != null && adItem.getLink()!="") {
						Thread background = new Thread(new Runnable() {
							public void run() {
								try {
									parseJSONAds.userClickAd(adItem.getLink());
								} catch (Throwable t) {}
							}
						});
						background.start();

						Intent i = new Intent(Intent.ACTION_VIEW);
						i.setData(Uri.parse(adItem.getLink()));
						activity.startActivity(i);
					}
				}
			}
		});
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		layoutParams.setMargins(0, 0, 0, 50);
		listView.setLayoutParams(layoutParams);
		LinearLayout linearLayout = new LinearLayout(activity);
		if (android.os.Build.VERSION.SDK_INT>=17) {
			// call something for API Level 11+
			linearLayout.setLayoutDirection(LinearLayout.LAYOUT_DIRECTION_LTR);
		}

		//linearLayout.addView(progressBar1);
		linearLayout.addView(listView);
		return linearLayout;
	}

	static LinearLayout linearLayout;
	static AlertDialog alertDialog;
	static AlertDialog.Builder alert;
	public static void showAlertGetMoreAppsServer(final Activity activity) {
		alert = new AlertDialog.Builder(activity);
		TextView textView = new TextView(activity);
		textView.setText("Loading...");
		textView.setPadding(10, 10, 10, 10);
		textView.setTextSize(15);
		textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
		alert.setView(textView);
		alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
				alertDialog = null;
			}
		});
		//alert.setIcon(android.R.drawable.box_new)
		alertDialog = alert.show();

		(new Thread(new Runnable() {
			@Override
			public void run() {
				linearLayout = BuildGetMoreAppsServer(activity);
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if(alertDialog!=null && alertDialog.isShowing()) {
							alert.setView(linearLayout);
							alertDialog.dismiss();
							alertDialog = alert.show();
						}
					}
				});
			}
		}
		)).start();
	}
}
