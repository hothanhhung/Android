package com.hth.utils;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.hth.adflex.AdFlexData;
import com.hth.adflex.FlexibleRowAdapter;
import com.hth.adflex.ParseJSONAdFlex;
import com.hth.qtcs.R;

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
                .setTitle(R.string.notify_title)
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
        return ProgressDialog.show(activity, "Loading", "Please wait for a moment...", true);
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
    
    public static LinearLayout BuildGetMoreApps(final Activity activity)
    {
    	ArrayList<AdFlexData> adFlexDataList = ParseJSONAdFlex.getAdFlexes("vn");
    	
    	ArrayList<AdFlexData> adFlexes = new ArrayList<AdFlexData>();
    	AdFlexData firstAdFlex = new AdFlexData();
    	firstAdFlex.setName("Ủng hộ tác giả bằng cách down và cài đặt Game và ứng dụng");
    	firstAdFlex.setDesc("Đối với file APK vui lòng bật chế độ cho phép cài đặt nguồn ngoài Google play \n Settings > Security > ☑ Unknown Sources.");
    	adFlexes.add(firstAdFlex);
    	
    	for(int i = 0; i < adFlexDataList.size(); i++)
    	{
    		AdFlexData adFlex = adFlexDataList.get(i);
    		if(!adFlex.getAppId().contains("gameloft"))
    		{
    			adFlexes.add(adFlex);
    		}
    	}
    	ListView listView = new ListView(activity);
    	FlexibleRowAdapter adapter = new FlexibleRowAdapter(activity, adFlexes, activity.getResources());
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
		linearLayout.setLayoutDirection(LinearLayout.VERTICAL);
		//linearLayout.addView(progressBar1);
		linearLayout.addView(listView);
		return linearLayout;
    }
    
    public static AlertDialog showAlertGetMoreApps(final Activity activity, String urlPage)
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
}
