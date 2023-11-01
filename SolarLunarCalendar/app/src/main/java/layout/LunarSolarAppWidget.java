package layout;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.widget.RemoteViews;

import com.hunght.data.DateItemForGridview;
import com.hunght.data.LunarDate;
import com.hunght.solarlunarcalendar.MainActivity;
import com.hunght.solarlunarcalendar.R;
import com.hunght.utils.SharedPreferencesUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Implementation of App Widget functionality.
 */
public class LunarSolarAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        DateItemForGridview today = new DateItemForGridview(null, new Date(), false);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.lunar_solar_app_widget);
        views.setTextViewText(R.id.appwidget_text_solar, today.getDayOfWeekInString() +"\n" + today.getSolarInfo(true));
        views.setTextViewText(R.id.appwidget_text_lunar, today.getLunarInfoWidget(true));

        views.setTextColor(R.id.appwidget_text_solar, SharedPreferencesUtils.getWidgetTextColor(context));
        views.setTextViewTextSize(R.id.appwidget_text_solar, TypedValue.COMPLEX_UNIT_SP, SharedPreferencesUtils.getWidgetTextFontSize(context));
        views.setTextColor(R.id.appwidget_text_lunar, SharedPreferencesUtils.getWidgetTextColor(context));
        views.setTextViewTextSize(R.id.appwidget_text_lunar, TypedValue.COMPLEX_UNIT_SP, SharedPreferencesUtils.getWidgetTextFontSize(context));

        if(SharedPreferencesUtils.getShowWidgetConGiap(context)) {
            views.setViewVisibility(R.id.appwidget_image_congiap, View.VISIBLE);
            int lunarDate = today.getDateInLunar() % 12;
            int resId = R.drawable.ty;
            switch (lunarDate) {
                case LunarDate.TY:
                    resId = R.drawable.ty;
                    break;
                case LunarDate.SUU:
                    resId = R.drawable.suu;
                    break;
                case LunarDate.DAN:
                    resId = R.drawable.dan;
                    break;
                case LunarDate.MAO:
                    resId = R.drawable.meo;
                    break;
                case LunarDate.THIN:
                    resId = R.drawable.thin;
                    break;
                case LunarDate.TI:
                    resId = R.drawable.ti;
                    break;
                case LunarDate.NGO:
                    resId = R.drawable.ngo;
                    break;
                case LunarDate.MUI:
                    resId = R.drawable.mui;
                    break;
                case LunarDate.THAN:
                    resId = R.drawable.than;
                    break;
                case LunarDate.DAU:
                    resId = R.drawable.dau;
                    break;
                case LunarDate.TUAT:
                    resId = R.drawable.tuat;
                    break;
                case LunarDate.HOI:
                    resId = R.drawable.hoi;
                    break;
            }
            views.setImageViewResource(R.id.appwidget_image_congiap, resId);
        }else{
            views.setViewVisibility(R.id.appwidget_image_congiap, View.GONE);
        }

        int flagPendingIntent = PendingIntent.FLAG_CANCEL_CURRENT;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
            flagPendingIntent = flagPendingIntent|PendingIntent.FLAG_IMMUTABLE;
        }

        Intent configIntent = new Intent(context, MainActivity.class);
        configIntent.setAction("LunarSolarAppWidget");
        configIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        configIntent.putExtra(MainActivity.EXTRA_FOR_NAVIGATION_MENU_ID, R.id.navSolarLunarCalendar);
        PendingIntent configPendingIntent = PendingIntent.getActivity(context, 20201010, configIntent, flagPendingIntent);
        views.setOnClickPendingIntent(R.id.appwidget_image_congiap, configPendingIntent);

        Calendar now = Calendar.getInstance();
        int dayOfYear = now.get(Calendar.DAY_OF_YEAR);
        SharedPreferencesUtils.setSettingWidgetLastUpdateDate(context, dayOfYear);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

