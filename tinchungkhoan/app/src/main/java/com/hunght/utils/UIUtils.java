package com.hunght.utils;

import java.util.ArrayList;

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

import com.hunght.tinchungkhoan.R;

public class UIUtils {

	public static Dialog showProgressDialog(Dialog loadingDialog, Context context)
	{
		if ((loadingDialog == null) || (!loadingDialog.isShowing())) {
			loadingDialog= new Dialog(context);
			loadingDialog.getWindow().getCurrentFocus();
			Drawable d = new ColorDrawable(Color.BLACK);
			d.setAlpha(210);
			loadingDialog.getWindow().setBackgroundDrawable(d);
			loadingDialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
			loadingDialog.setContentView(R.layout.loading_dialog);
			loadingDialog.setCancelable(false);
			//loadingDialog.setOwnerActivity(context);

			loadingDialog.show();
		} else {
			//loadingDialog.setOwnerActivity(activity);
			loadingDialog.show();
		}
		return loadingDialog;
	}

    public static void showAlertInform(final Activity activity, String message)
    {
        new AlertDialog.Builder(activity)
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
        AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setTitle("Error")
                .setMessage("No internet access")
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


	public static LinearLayout BuildGetMoreAppsServer(final Activity activity)
	{
		final ParseJSONAds parseJSONAds = new ParseJSONAds(activity, "android");
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
