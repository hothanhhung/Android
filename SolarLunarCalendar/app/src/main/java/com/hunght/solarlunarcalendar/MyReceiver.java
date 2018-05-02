package com.hunght.solarlunarcalendar;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.hunght.data.DateItemForGridview;
import com.hunght.utils.ServiceProcessor;
import com.hunght.utils.SharedPreferencesUtils;
import com.hunght.utils.Utils;

import java.util.Date;

public class MyReceiver extends BroadcastReceiver {
    DateItemForGridview selectedDate;
    PerformServiceProcessBackgroundTask currentPerformServiceProcessBackgroundTaskGoodDate, currentPerformServiceProcessBackgroundTaskChamNgon;
    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Log.d("AmDuong", "onReceive 1: " + intent.getAction());
        boolean isOn = false;
        switch (intent.getAction()) {
            // case PowerManager.PARTIAL_WAKE_LOCK:
            case Intent.ACTION_SCREEN_ON:
                Log.d("AmDuong", "onReceive ACTION_SCREEN_ON: checking");
                isOn = isOnline(context);
                break;
            case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                Log.d("AmDuong", "onReceive NETWORK_STATE_CHANGED_ACTION: checking");
                isOn = isOnline(context);
                break;

        }
        Log.d("AmDuong", "onReceive 1: on off" + isOn);
        Date now = new Date();
        if (isOn && now.getHours() > 6 && (MyService.lastOn == null || (now.getTime() - MyService.lastOn.getTime()) > 60000)) {
            Log.d("AmDuong", "onReceive 1: on");
            MyService.lastOn = now;
            selectedDate = new DateItemForGridview("", new Date(), false);
            if (SharedPreferencesUtils.getShowDailyNotifyGoodDateBadDate(context) && ((now.getDate() != SharedPreferencesUtils.getShowDailyNotifyGoodDateBadDateTime(context)))) {

                if (currentPerformServiceProcessBackgroundTaskGoodDate != null) {
                    currentPerformServiceProcessBackgroundTaskGoodDate.cancel(true);
                    currentPerformServiceProcessBackgroundTaskGoodDate = null;
                }
                Log.d("AmDuong", "onReceive 1: good date");
                currentPerformServiceProcessBackgroundTaskGoodDate = new PerformServiceProcessBackgroundTask();
                currentPerformServiceProcessBackgroundTaskGoodDate.execute(ServiceProcessor.SERVICE_GET_INFO_OF_DATE_SHORT, selectedDate.getDisplaySolarDate(), selectedDate.getThapNhiBatTu());
            }

            if (SharedPreferencesUtils.getShowNotifyChamNgon(context) && ((now.getDate() != SharedPreferencesUtils.getShowNotifyChamNgonTime(context)))) {
                if (currentPerformServiceProcessBackgroundTaskChamNgon != null) {
                    currentPerformServiceProcessBackgroundTaskChamNgon.cancel(true);
                    currentPerformServiceProcessBackgroundTaskChamNgon = null;
                }
                currentPerformServiceProcessBackgroundTaskChamNgon = new PerformServiceProcessBackgroundTask();
                Log.d("AmDuong", "onReceive 1: cham ngon");
                currentPerformServiceProcessBackgroundTaskChamNgon.execute(ServiceProcessor.SERVICE_GET_CHAM_NGON, selectedDate.getDisplaySolarDate(), selectedDate.getDayOfMonth());
            }
        }
    }

    public Boolean isOnline(Context ctx) {
        ConnectivityManager cm =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnected();

    }

    class PerformServiceProcessBackgroundTask extends AsyncTask<Object, Object, Object> {
        private int type;

        protected void onPreExecute() {
        }

        protected Object doInBackground(Object... params) {
            type = Integer.parseInt(params[0].toString());
            Log.d("AmDuong", "doInBackground 1: " + type);
            switch (type) {
                case ServiceProcessor.SERVICE_GET_INFO_OF_DATE_SHORT:
                    return ServiceProcessor.getInfoDateShort(params[1].toString(), Integer.parseInt(params[2].toString()));
                case ServiceProcessor.SERVICE_GET_CHAM_NGON:
                    return ServiceProcessor.getChamNgon(params[1].toString(), Integer.parseInt(params[2].toString()), 1);

            }
            return null;
        }

        protected void onPostExecute(Object object) {
            Log.d("AmDuong", "onPostExecute 1: " + type);
            switch (type) {
                case ServiceProcessor.SERVICE_GET_INFO_OF_DATE_SHORT:
                    Log.d("AmDuong", "onPostExecute 2: " + ServiceProcessor.SERVICE_GET_INFO_OF_DATE_SHORT);
                    if (object != null) {
                        SharedPreferencesUtils.setShowDailyNotifyGoodDateBadDateTime(context, (new Date()).getDate());
                        String str =  "Là ngày " + String.valueOf(object);
                        Log.d("AmDuong", "onPostExecute" +  str);


                        Intent intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        MainActivity.setViewId(R.id.navGoodDayBadDay);
                        PendingIntent activity = PendingIntent.getActivity(context, 0, intent, 0);

                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(context)
                                        .setContentIntent(activity)
                                        .setSmallIcon(Utils.getIconConGiap(selectedDate.getDateInLunar()))
                                        .setContentTitle("Hôm nay " + selectedDate.getSolarInfo(false))
                                        .setAutoCancel(true)
                                        .setContentText(str);

                        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(ServiceProcessor.SERVICE_GET_INFO_OF_DATE_SHORT, mBuilder.build());

                    } else {
                        //Toast.makeText(getContext(),"Error to connect to server", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ServiceProcessor.SERVICE_GET_CHAM_NGON:
                    Log.d("AmDuong", "onPostExecute 2: " + ServiceProcessor.SERVICE_GET_CHAM_NGON);
                    if (object != null) {
                        SharedPreferencesUtils.setShowNotifyChamNgonTime(context, (new Date()).getDate());
                        String str = String.valueOf(object);
                        Log.d("AmDuong", "onPostExecute" +  str);

                        Intent intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        MainActivity.setViewId(R.id.navSolarLunarCalendar);
                        PendingIntent activity = PendingIntent.getActivity(context, 0, intent, 0);

                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(context)
                                        .setContentIntent(activity)
                                        .setSmallIcon(Utils.getIconConGiap(selectedDate.getDateInLunar()))
                                        .setContentTitle("Hôm nay " + selectedDate.getSolarInfo(false))
                                        .setAutoCancel(true)
                                        .setContentText(str);

                        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(ServiceProcessor.SERVICE_GET_CHAM_NGON, mBuilder.build());

                    } else {
                        //Toast.makeText(getContext(),"Error to connect to server", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

        }

    }
}
