package com.hunght.utils;

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
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hunght.myfavoritesites.R;

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


    public static void showAlertInform(final Activity activity, String message)
    {
        new AlertDialog.Builder(activity)
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
}
