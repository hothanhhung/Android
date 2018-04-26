package layout;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.hunght.data.DateItemForGridview;
import com.hunght.data.LunarDate;
import com.hunght.solarlunarcalendar.MainActivity;
import com.hunght.solarlunarcalendar.R;

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
        int lunarDate = today.getDateInLunar() % 12;
        int resId = R.drawable.ty;
        switch (lunarDate)
        {
            case LunarDate.TY: resId = R.drawable.ty; break;
            case LunarDate.SUU: resId = R.drawable.suu; break;
            case LunarDate.DAN: resId = R.drawable.dan; break;
            case LunarDate.MAO: resId = R.drawable.meo; break;
            case LunarDate.THIN: resId = R.drawable.thin; break;
            case LunarDate.TI: resId = R.drawable.ti; break;
            case LunarDate.NGO: resId = R.drawable.ngo; break;
            case LunarDate.MUI: resId = R.drawable.mui; break;
            case LunarDate.THAN: resId = R.drawable.than; break;
            case LunarDate.DAU: resId = R.drawable.dau; break;
            case LunarDate.TUAT: resId = R.drawable.tuat; break;
            case LunarDate.HOI: resId = R.drawable.hoi; break;
        }
        views.setImageViewResource(R.id.appwidget_image_congiap, resId);

        Intent configIntent = new Intent(context, MainActivity.class);
        PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);
        views.setOnClickPendingIntent(R.id.appwidget_image_congiap, configPendingIntent);

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

