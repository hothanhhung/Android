package com.hth.datmon;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import com.hth.service.OrderDetail;

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
		AlertDialog alertDialog = new AlertDialog.Builder(activity)
        .setTitle("Error")
        .setMessage("Không thể kết nối Internet")
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
                .setTitle("Lỗi")
                .setMessage("Không thể kết nối Internet")
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

	public static void showNumberPickerDialog(Activity activity, final OrderDetail orderedItem, final ICallBack callBack)
	{
		String numberInString = String.valueOf(orderedItem.getQuantity());
		String [] parts = numberInString.indexOf('.')==-1? numberInString.split("[,]"):numberInString.split("[.]");
		int  integetNumber= 0, decimalNumber = 0;

		if(parts.length > 0){
			integetNumber = Integer.parseInt(parts[0]);
		}
		if(parts.length > 1){
			decimalNumber=Integer.parseInt(parts[1]);
		}

		final Dialog d = new Dialog(activity);
		d.setTitle("Chọn Số Lượng");
		d.setContentView(R.layout.number_picker_dialog);
		Button b1 = (Button) d.findViewById(R.id.button1);
		Button b2 = (Button) d.findViewById(R.id.button2);
		final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
		final NumberPicker np2 = (NumberPicker) d.findViewById(R.id.numberPicker2);
		final NumberPicker np3 = (NumberPicker) d.findViewById(R.id.numberPicker3);
		np.setMaxValue(100);
		np.setMinValue(0);
		np.setValue(integetNumber);
		np.setWrapSelectorWheel(false);

		np2.setDisplayedValues(new String[]{"."});

		np3.setMaxValue(100);
		np3.setMinValue(0);
		np3.setValue(decimalNumber);
		np3.setWrapSelectorWheel(false);

		b1.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				orderedItem.setQuantity(Float.valueOf(np.getValue()+"."+np3.getValue()));
				callBack.onNumberPikerDialogSave();
				d.dismiss();
			}
		});
		b2.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				d.dismiss();
			}
		});
		d.show();


	}
}
