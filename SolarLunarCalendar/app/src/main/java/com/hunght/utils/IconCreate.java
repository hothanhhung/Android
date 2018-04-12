package com.hunght.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.ImageView;

import java.util.Date;

/**
 * Created by Lenovo on 4/9/2018.
 */

public class IconCreate {
 /*void shortcutAdd(Context context, String name) {
        // Intent to be send, when shortcut is pressed by user ("launched")
        Intent shortcutIntent = new Intent(context, Play.class);
        shortcutIntent.setAction(Constants.ACTION_PLAY);

        Date today = new Date();
        // Create bitmap with number in it -> very default. You probably want to give it a more stylish look
        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        paint.setColor(0xFF808080); // gray
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(50);
        new Canvas(bitmap).drawText(""+today.getDate(), 50, 50, paint);
     //   ((ImageView) findViewById(R.id.icon)).setImageBitmap(bitmap);

        // Decorate the shortcut
        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, bitmap);

        // Inform launcher to create shortcut
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        context.sendBroadcast(addIntent);
    }

    private void shortcutDel(Context context, String name) {
        // Intent to be send, when shortcut is pressed by user ("launched")
        Intent shortcutIntent = new Intent(context, Play.class);
        shortcutIntent.setAction(Constants.ACTION_PLAY);

        // Decorate the shortcut
        Intent delIntent = new Intent();
        delIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        delIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);

        // Inform launcher to remove shortcut
        delIntent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
        context.sendBroadcast(delIntent);
    }*/
}
