package com.hth.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hth.lichtivi.Data;
import com.hth.lichtivi.FlexibleScheduleRowAdapter;
import com.hth.lichtivi.R;

public class UIUtils {
	public static Dialog showSetAlarmPopup(final AlarmItem alarmItem, final Activity activity, final FlexibleScheduleRowAdapter flexibleScheduleRowAdapter) {
		final Dialog loadingDialog = new Dialog(activity);
		loadingDialog.getWindow().getCurrentFocus();
		loadingDialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
		loadingDialog.setContentView(R.layout.alarm_popup_layout);
		loadingDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		TextView tvProgramName = (TextView) loadingDialog.findViewById(R.id.tvProgramName);
		TextView tvChannelName = (TextView) loadingDialog.findViewById(R.id.tvChannelName);
		TextView tvStartOn = (TextView) loadingDialog.findViewById(R.id.tvStartOn);
		Spinner spTimeRemindBefore = (Spinner) loadingDialog.findViewById(R.id.spTimeRemindBefore);
		Button btCancel = (Button) loadingDialog.findViewById(R.id.btCancel);
		Button btSaveAlarm = (Button) loadingDialog.findViewById(R.id.btSaveAlarm);

		tvProgramName.setText(alarmItem.getProgramName());
		tvChannelName.setText(alarmItem.getChannelName());
		tvStartOn.setText(alarmItem.getStartOn());
		String[] arraySpinner = new String[] {
				"  5 phút",
				" 10 phút",
				" 15 phút",
				" 20 phút",
				" 30 phút",
				" 45 phút",
				" 60 phút",
				" 90 phút",
				"120 phút"
		};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, arraySpinner);
		spTimeRemindBefore.setAdapter(adapter);
		btCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				loadingDialog.dismiss();
			}
		});

		btSaveAlarm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Spinner spTimeRemindBefore = (Spinner) loadingDialog.findViewById(R.id.spTimeRemindBefore);
				int minute = 5;
				switch (spTimeRemindBefore.getSelectedItemPosition())
				{
					case 0: minute = 5; break;
					case 1: minute = 10; break;
					case 2: minute = 15; break;
					case 3: minute = 20; break;
					case 4: minute = 30; break;
					case 5: minute = 45; break;
					case 6: minute = 60; break;
					case 7: minute = 90; break;
					case 8: minute = 120; break;
				}
				alarmItem.setRemindBeforeInMinute(minute);
				if(alarmItem.getTimeToRemindInMiliSecond() > System.currentTimeMillis()) {
					AlarmItemsManager.setAlarm(activity, alarmItem);
					flexibleScheduleRowAdapter.updateUI();
					loadingDialog.dismiss();
				}else{
					showAlertError(activity, false, "Thời gian chạy được cài đặt đã ở quá khứ.");
				}
			}
		});

		loadingDialog.setCancelable(false);
		loadingDialog.setOwnerActivity(activity);

		loadingDialog.show();

		return loadingDialog;
	}

	public static AlertDialog showAlertError(final Activity activity, final Boolean isCloseThis, String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(activity)
				.setTitle("Error")
				.setMessage(message)
				.setNeutralButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								if (isCloseThis) activity.finish();
							}
						})
				.setIcon(android.R.drawable.ic_dialog_info)
				.show();
		return alertDialog;
	}

	public static AlertDialog showAlertError(final Activity activity, final Boolean isCloseThis) {
		AlertDialog alertDialog = new AlertDialog.Builder(activity)
				.setTitle("Error")
				.setMessage("No internet access")
				.setNeutralButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								if (isCloseThis) activity.finish();
							}
						})
				.setIcon(android.R.drawable.ic_dialog_info)
				.show();
		return alertDialog;
	}

	public static void showAlertInform(final Activity activity, String message) {
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


	public static Boolean isOnline(Context ctx) {
		ConnectivityManager cm =
				(ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

		return cm.getActiveNetworkInfo() != null &&
				cm.getActiveNetworkInfo().isConnected();

	}

	/*
        public static AlertDialog showAlertErrorNoInternet(final Activity activity, final Boolean isCloseThis)
        {
            AlertDialog alertDialog = new AlertDialog.Builder(activity)
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
        */
	private static ProgressBar progressBar1;

	public static AlertDialog showAlertGetMoreApps(final Activity activity, String urlPage) {
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
				if (progress < 95) {
					progressBar1.setVisibility(View.VISIBLE);
					if (progressBar1.getProgress() < progress) {
						progressBar1.setProgress(progress);
					}
				} else {
					view.setVisibility(View.VISIBLE);
					progressBar1.setVisibility(View.GONE);
				}
			}
		});
		wv.loadUrl(urlPage);
		LinearLayout linearLayout = new LinearLayout(activity);
		if (android.os.Build.VERSION.SDK_INT >= 17) {
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
}
