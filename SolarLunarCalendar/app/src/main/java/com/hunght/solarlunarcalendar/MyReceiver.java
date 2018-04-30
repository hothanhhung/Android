package com.hunght.solarlunarcalendar;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.hunght.data.DateItemForGridview;
import com.hunght.utils.ServiceProcessor;

import java.util.Date;

public class MyReceiver extends BroadcastReceiver {
    static Date lastOn = null;
    DateItemForGridview selectedDate;
    PerformServiceProcessBackgroundTask currentPerformServiceProcessBackgroundTask;
    Context context;
    boolean isThapNhiBatTu = true;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Log.d("AmDuong", "onReceive 1: "+intent.getAction());
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
           /* case WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION:
                Log.d("AmDuong", "onReceive SUPPLICANT_CONNECTION_CHANGE_ACTION: checking");
                isOn = intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false);
                break;*/

        }
        Log.d("AmDuong", "onReceive 1: on off" + isOn);
        Date now = new Date();
        if (isOn && (lastOn == null || (now.getTime() - lastOn.getTime()) > 60000 )) {
            lastOn = now;
            Log.d("AmDuong", "onReceive 1: on");
            selectedDate = new DateItemForGridview("", new Date(), false);
            if (currentPerformServiceProcessBackgroundTask == null) {
                currentPerformServiceProcessBackgroundTask = new PerformServiceProcessBackgroundTask();
            }
            if(isThapNhiBatTu){
                currentPerformServiceProcessBackgroundTask.execute(ServiceProcessor.SERVICE_GET_INFO_OF_DATE_SHORT, selectedDate.getDisplaySolarDate(), selectedDate.getThapNhiBatTu());
            }
            else
            {
                currentPerformServiceProcessBackgroundTask.execute(ServiceProcessor.SERVICE_GET_CHAM_NGON, selectedDate.getDisplaySolarDate(), selectedDate.getDayOfMonth());
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
                case ServiceProcessor.SERVICE_GET_CHAM_NGON:
                    Log.d("AmDuong", "onPostExecute 2: " + ServiceProcessor.SERVICE_GET_CHAM_NGON);
                    if (object != null) {
                        String str = String.valueOf(object);
                        Log.d("onPostExecute", str);
                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(context)
                                        .setSmallIcon(R.drawable.amduong)
                                        .setContentTitle("Lịch Âm Dương")
                                        .setContentText(str);

                        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(1, mBuilder.build());

                    } else {
                        //Toast.makeText(getContext(),"Error to connect to server", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

        }

    }
}
