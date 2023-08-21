package com.hunght.solarlunarcalendar;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.util.Log;

import com.hunght.data.DateItemForGridview;
import com.hunght.data.NoteItem;
import com.hunght.utils.ServiceProcessor;
import com.hunght.utils.SharedPreferencesUtils;
import com.hunght.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import layout.LunarSolarAppWidget;

public class MyReceiver// extends BroadcastReceiver
{
    private int backgroundCount = 0;
    private DateItemForGridview selectedDate;
    private PerformServiceProcessBackgroundTask currentPerformServiceProcessBackgroundTaskGoodDate, currentPerformServiceProcessBackgroundTaskChamNgon;
    private Context context;

    public boolean isBackgroundRunning(){
        return backgroundCount > 0;
    }
    public void start(Context context) {
        this.context = context;
        backgroundCount = 0;
        Log.d("AmDuong", "start ");
        //showNotificationDebug();

        Calendar now = Calendar.getInstance();
        int dayOfYear = now.get(Calendar.DAY_OF_YEAR);
        int lastWidgetLastUpdate = SharedPreferencesUtils.getSettingWidgetLastUpdateDate(context);
        if(dayOfYear != lastWidgetLastUpdate){

            Intent intent = new Intent(context, LunarSolarAppWidget.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            int[] ids = AppWidgetManager.getInstance(context)
                    .getAppWidgetIds(new ComponentName(context, LunarSolarAppWidget.class));
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            context.sendBroadcast(intent);

            SharedPreferencesUtils.setSettingWidgetLastUpdateDate(context, dayOfYear);
        }

        if (now.get(Calendar.HOUR_OF_DAY) > 5 ) {
            Log.d("AmDuong", "start: > 5h");
            boolean keepCheck = true;
            selectedDate = new DateItemForGridview("", now.getTime(), false);
            //if(isOn)
            {
                if (SharedPreferencesUtils.getShowDailyNotifyGoodDateBadDateSetting(context) && ((now.get(Calendar.DAY_OF_YEAR) != SharedPreferencesUtils.getShowDailyNotifyGoodDateBadDateTime(context))))
                {
                    Log.d("AmDuong", "Have Good Date");
                    keepCheck = false;
                    if (currentPerformServiceProcessBackgroundTaskGoodDate != null) {
                        currentPerformServiceProcessBackgroundTaskGoodDate.cancel(true);
                        currentPerformServiceProcessBackgroundTaskGoodDate = null;
                    }
                    Log.d("AmDuong", "onReceive 1: good date");
                    currentPerformServiceProcessBackgroundTaskGoodDate = new PerformServiceProcessBackgroundTask();
                    currentPerformServiceProcessBackgroundTaskGoodDate.execute(ServiceProcessor.SERVICE_GET_INFO_OF_DATE_SHORT, selectedDate.getDisplaySolarDate(), selectedDate.getThapNhiBatTu());
                }

                if (SharedPreferencesUtils.getShowNotifyChamNgonSetting(context) && now.get(Calendar.DAY_OF_YEAR) != SharedPreferencesUtils.getShowNotifyChamNgonTime(context))
                {
                    Log.d("AmDuong", "Have Cham ngon");
                    keepCheck = false;
                    if (currentPerformServiceProcessBackgroundTaskChamNgon != null) {
                        currentPerformServiceProcessBackgroundTaskChamNgon.cancel(true);
                        currentPerformServiceProcessBackgroundTaskChamNgon = null;
                    }
                    currentPerformServiceProcessBackgroundTaskChamNgon = new PerformServiceProcessBackgroundTask();
                    Log.d("AmDuong", "onReceive 1: cham ngon");
                    currentPerformServiceProcessBackgroundTaskChamNgon.execute(ServiceProcessor.SERVICE_GET_CHAM_NGON, selectedDate.getDisplaySolarDate(), selectedDate.getDayOfMonth());
                }
            }
            if (keepCheck && SharedPreferencesUtils.getShowDailyNotifyEventSetting(context) && now.get(Calendar.DAY_OF_YEAR) != SharedPreferencesUtils.getShowDailyNotifyReminding(context))
             {
                 Log.d("AmDuong", "Checking NoteItems");
                ArrayList<NoteItem> noteItems = SharedPreferencesUtils.getNoteItems(context);

                int index = 0;
                for (NoteItem noteItem: noteItems) {
                    if(noteItem != null && noteItem.haveDate && (noteItem.isToday() || noteItem.isTodayBefore()))
                    {
                        Log.d("AmDuong", "Have NoteItem");
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.setAction("SERVICE_GET_TO_NOTE");
                        intent.putExtra(MainActivity.EXTRA_FOR_NAVIGATION_MENU_ID, R.layout.notes_view_item);
                        SaveNoteItemView.setNoteItem(noteItem);
                        PendingIntent activity = PendingIntent.getActivity(context, getRandom(), intent, PendingIntent.FLAG_CANCEL_CURRENT);

                        /*NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(context)
                                        .setContentIntent(activity)
                                        .setSmallIcon(Utils.getIconConGiap(selectedDate.getDateInLunar()))
                                        .setContentTitle(noteItem.Subject)
                                        .setAutoCancel(true)
                                        .setContentText(noteItem.Content);

                        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(ServiceProcessor.SERVICE_GET_INFO_OF_DATE_SHORT, mBuilder.build());*/

                        index++;
                        if(noteItem.isToday())
                        {
                            showNotification(context, "SERVICE_GET_TO_NOTE", ServiceProcessor.SERVICE_GET_TO_NOTE * 100 + index, noteItem.Subject, noteItem.Content, activity, R.drawable.bell);
                        }else{
                            showNotification(context, "SERVICE_GET_TO_NOTE", ServiceProcessor.SERVICE_GET_TO_NOTE * 100 + index, "Sắp đến " + noteItem.Subject, noteItem.getTitleRemindBefore(), activity, R.drawable.bell);
                        }
                        keepCheck = false;
                    }

                }

                SharedPreferencesUtils.setShowDailyNotifyReminding(context, now.get(Calendar.DAY_OF_YEAR));
            }
            if (true || SharedPreferencesUtils.getShowNotifyNgayRam(context) && now.get(Calendar.DAY_OF_YEAR) != SharedPreferencesUtils.getShowNgayRamNotifyReminding(context))
            {
                DateItemForGridview date = DateItemForGridview.createDateItemForGridviewFromLunar(now.get(Calendar.DAY_OF_MONTH), now.get(Calendar.MONTH) + 1, now.get(Calendar.YEAR));

                int day = date.getLunarDate().getDate();
                Log.d("AmDuong", "Checking Ngay Ram: " + day);
                if(true || day == 1 || day == 15) {
                    String subject = (day == 1)? "Hôm nay là ngày đầu tháng âm lịch" : "Hôm nay là rằm", content = date.getLunarInfoWidget(false);
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setAction("SERVICE_GET_INFO_OF_NGAY_RAM");
                    intent.putExtra(MainActivity.EXTRA_FOR_NAVIGATION_MENU_ID, R.id.navSolarLunarCalendar);
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, getRandom(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    showNotification(context, "SERVICE_GET_INFO_OF_NGAY_RAM", ServiceProcessor.SERVICE_GET_NGAY_RAM, subject, content, pendingIntent, (day == 1)? R.drawable.moon:R.drawable.fullmoon);
                    keepCheck = false;
                }
                SharedPreferencesUtils.setShowNgayRamNotifyReminding(context, now.get(Calendar.DAY_OF_YEAR));
            }

            if (keepCheck && now.get(Calendar.DAY_OF_YEAR) != SharedPreferencesUtils.getShowSuggestTuVi(context))
            {
                int longTime = now.get(Calendar.DAY_OF_YEAR) - SharedPreferencesUtils.getShowSuggestTuVi(context);
                if(longTime < 0) longTime += 365;
                Log.d("AmDuong", "Checking suggest Tuvi");
                Random rand = new Random();
                if(rand.nextInt(4) == 1 || SharedPreferencesUtils.getShowSuggestTuVi(context) == 0 || longTime > 5){
                    String subject = "Xem tử vi hàng ngày", content = "Thử xem tử vi hôm nay của bạn nào";
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.setAction("SERVICE_GET_SUGGEST_TO_TUVI");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    switch (rand.nextInt(3)) {
                        case 0:
                            subject = "Thử gieo quẻ xem";
                            content = "Thử gieo quẻ xem thế nào";
                            intent.putExtra(MainActivity.EXTRA_FOR_NAVIGATION_MENU_ID, R.id.navGieoQueQuanAm);
                            break;
                        case 1:
                            subject = "Bói Bài Tarot";
                            content = "Thử bói bài Tarot nào";
                            intent.putExtra(MainActivity.EXTRA_FOR_NAVIGATION_MENU_ID, R.id.navBoiBaiTarot);
                            break;
                        default:
                            break;
                    }
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, getRandom(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    showNotification(context, "SERVICE_GET_SUGGEST_TO_TUVI", ServiceProcessor.SERVICE_GET_SUGGEST_TO_TUVI, subject, content, pendingIntent, R.drawable.icon);
                    keepCheck = false;
                }
                SharedPreferencesUtils.setShowSuggestTuVi(context, now.get(Calendar.DAY_OF_YEAR));
            }
        }
    }

    private Boolean isOnline(Context ctx) {
        ConnectivityManager cm =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnected();

    }

    private void showNotificationDebug(){
        showNotification(context, "SERVICE_GET_INFO_OF_DATE_SHORT_DEBUG", ServiceProcessor.SERVICE_GET_DEBUG, "Job alarm run", "Job alarm is running", null, R.drawable.icon);
    }

    private void showNotification(Context context, String channelId, int id, String subject, String content, PendingIntent intent, int icon)
    {
        showNotification(context, channelId, id, subject, content, intent, icon, null);
    }
    private void showNotification(Context context, String channelId, int id, String subject, String content, PendingIntent intent, int icon, NotificationCompat.Style style)
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setContentTitle(subject)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        if(style != null){
            builder.setStyle(style);
        }

        if(icon > 0){
            builder.setSmallIcon(icon);
        }

        if(intent!=null){
            builder.setContentIntent(intent);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "LichAmDuongChannel";
            String description = "Lich Am Duong Channel to Notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(id, builder.build());


    }

    Random random = new Random();
    private int getRandom(){
        return 0;//random.nextInt();
    }

    class PerformServiceProcessBackgroundTask extends AsyncTask<Object, Object, Object> {

        private int type;

        protected void onPreExecute() {
        }

        protected Object doInBackground(Object... params) {
            backgroundCount++;
            type = Integer.parseInt(params[0].toString());
            Log.d("AmDuong", "doInBackground 1: " + type);
            switch (type) {
                case ServiceProcessor.SERVICE_GET_INFO_OF_DATE_SHORT:
                    return ServiceProcessor.getInfoDateShort(params[1].toString(), Integer.parseInt(params[2].toString()), false);
                case ServiceProcessor.SERVICE_GET_CHAM_NGON:
                    return ServiceProcessor.getChamNgon(params[1].toString(), Integer.parseInt(params[2].toString()), 1, false);

            }
            return null;
        }

        protected void onPostExecute(Object object) {
            Log.d("AmDuong", "onPostExecute 1: " + type);
            Calendar now = Calendar.getInstance();
            switch (type) {
                case ServiceProcessor.SERVICE_GET_INFO_OF_DATE_SHORT:
                    Log.d("AmDuong", "onPostExecute 2: " + ServiceProcessor.SERVICE_GET_INFO_OF_DATE_SHORT);
                    if (object != null) {
                        SharedPreferencesUtils.setShowDailyNotifyGoodDateBadDateTime(context, now.get(Calendar.DAY_OF_YEAR));
                        String str =  "Là ngày " + String.valueOf(object);
                        Log.d("AmDuong", "onPostExecute" +  str);


                        Intent intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.setAction("SERVICE_GET_INFO_OF_DATE_SHORT");
                        intent.putExtra(MainActivity.EXTRA_FOR_NAVIGATION_MENU_ID, R.id.navGoodDayBadDay);
                        PendingIntent activity = PendingIntent.getActivity(context, getRandom(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
/*
                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(context)
                                        .setContentIntent(activity)
                                        .setSmallIcon(Utils.getIconConGiap(selectedDate.getDateInLunar()))
                                        .setContentTitle("Hôm nay " + selectedDate.getSolarInfo(false))
                                        .setAutoCancel(true)
                                        .setContentText(str);

                        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(ServiceProcessor.SERVICE_GET_INFO_OF_DATE_SHORT, mBuilder.build());*/

                        showNotification(context, "SERVICE_GET_INFO_OF_DATE_SHORT", ServiceProcessor.SERVICE_GET_INFO_OF_DATE_SHORT, "Hôm nay " + selectedDate.getSolarInfo(false), str, activity, Utils.getIconConGiap(selectedDate.getDateInLunar()));
                    } else {
                        //Toast.makeText(getContext(),"Error to connect to server", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ServiceProcessor.SERVICE_GET_CHAM_NGON:
                    Log.d("AmDuong", "onPostExecute 2: " + ServiceProcessor.SERVICE_GET_CHAM_NGON);
                    if (object != null) {
                        SharedPreferencesUtils.setShowNotifyChamNgonTime(context, now.get(Calendar.DAY_OF_YEAR));
                        String str = String.valueOf(object);
                        Log.d("AmDuong", "onPostExecute" +  str);

                        Intent intent = new Intent(context, MainActivity.class);
                        intent.setAction("SERVICE_GET_CHAM_NGON");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra(MainActivity.EXTRA_FOR_NAVIGATION_MENU_ID, R.id.navSolarLunarCalendar);
                        PendingIntent activity = PendingIntent.getActivity(context, getRandom(), intent, PendingIntent.FLAG_CANCEL_CURRENT);

                        /*NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(context)
                                        .setContentIntent(activity)
                                        .setSmallIcon(Utils.getIconConGiap(selectedDate.getDateInLunar()))
                                        .setContentTitle("Hôm nay " + selectedDate.getSolarInfo(false))
                                        .setAutoCancel(true)
                                        .setContentText(str);

                        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(ServiceProcessor.SERVICE_GET_CHAM_NGON, mBuilder.build());*/

                        showNotification(context, "SERVICE_GET_CHAM_NGON", ServiceProcessor.SERVICE_GET_CHAM_NGON, "Hôm nay " + selectedDate.getSolarInfo(false), str, activity, Utils.getIconConGiap(selectedDate.getDateInLunar()));

                    } else {
                        //Toast.makeText(getContext(),"Error to connect to server", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            backgroundCount--;
        }

    }
}
