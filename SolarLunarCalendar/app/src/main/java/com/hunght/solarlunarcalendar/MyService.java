package com.hunght.solarlunarcalendar;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.hunght.data.DateItemForGridview;
import com.hunght.utils.ServiceProcessor;

import java.util.Date;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public void onCreate() {
        Log.d("AmDuong MyService", "onCreate: ");
        super.onCreate();
        IntentFilter filter = new IntentFilter();
       // filter.addAction("android.provider.Telephony.SMS_RECEIVED");
       // filter.addAction(android.telephony.TelephonyManager.ACTION_PHONE_STATE_CHANGED);
       // filter.addAction("your_action_strings"); //further more
       // filter.addAction("your_action_strings"); //further more

        registerReceiver(receiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("AmDuong onStartCommand", "onStartCommand: ");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }


    private static final BroadcastReceiver receiver = new BroadcastReceiver() {
        DateItemForGridview selectedDate;
        PerformServiceProcessBackgroundTask currentPerformServiceProcessBackgroundTask;
        Context context;

        @Override
        public void onReceive(Context context, Intent intent) {
            this.context = context;
            Log.d("AmDuong", "onReceive: "+intent.getAction());
            switch (intent.getAction()) {
                case Intent.ACTION_SCREEN_ON:
                case WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION:
                    if (isOnline(context)) {
                        selectedDate = new DateItemForGridview("", new Date(), false);
                        if (currentPerformServiceProcessBackgroundTask == null) {
                            currentPerformServiceProcessBackgroundTask = new PerformServiceProcessBackgroundTask();
                        }
                        currentPerformServiceProcessBackgroundTask.execute(ServiceProcessor.SERVICE_GET_CHAM_NGON, selectedDate.getDisplaySolarDate(), selectedDate.getDayOfMonth());
                    }
                    break;

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
                switch (type) {
                    case ServiceProcessor.SERVICE_GET_CHAM_NGON:
                        return ServiceProcessor.getChamNgon(params[1].toString(), Integer.parseInt(params[2].toString()));

                }
                return null;
            }

            protected void onPostExecute(Object object) {
                switch (type) {
                    case ServiceProcessor.SERVICE_GET_CHAM_NGON:
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
    };
}