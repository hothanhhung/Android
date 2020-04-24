package com.hunght.solarlunarcalendar;


import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.hunght.data.NoteItem;
import com.hunght.utils.SharedPreferencesUtils;
import com.rarepebble.colorpicker.ColorPickerView;

import java.util.ArrayList;

import layout.LunarSolarAppWidget;

/**
 * Created by Lenovo on 4/24/2018.
 */

public class SettingsView  extends LinearLayout {

    FloatingActionButton fbtNotesViewAdd;
    TextView tvNotesViewNoItem, tvWidgetTextColor;
    ListView lvNotesViewItems;
    ArrayList<NoteItem> noteItems;

    public SettingsView(Context context) {
        super(context);
        initView();
    }

    public SettingsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SettingsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SettingsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.settings_view, this);


        Switch[] btSwitchs = {(Switch) view.findViewById(R.id.swSettingsNgayHoangDao),
                (Switch) view.findViewById(R.id.swSettingsChamNgon),
                (Switch) view.findViewById(R.id.swSettingsDailyNotifyChamNgon),
                (Switch) view.findViewById(R.id.swSettingsDailyNotifyGoodDateBadDate),
                (Switch) view.findViewById(R.id.swSettingsDailyNotifyNgayRam),
                (Switch) view.findViewById(R.id.swSettingsWidgetShowConGiap),
                (Switch) view.findViewById(R.id.swSettingsNotifyEvent)};

        for (Switch switchItem : btSwitchs) {
            swithInitialData(switchItem);

            switchItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    btSwitchsChange(buttonView, isChecked);
                }
            });
        }

        tvWidgetTextColor = view.findViewById(R.id.tvWidgetTextColor);
        tvWidgetTextColor.setBackgroundColor(SharedPreferencesUtils.getWidgetTextColor(getContext()));
        tvWidgetTextColor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final ColorPickerView picker = new ColorPickerView(getContext());
                picker.setColor(SharedPreferencesUtils.getWidgetTextColor(getContext()));
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(picker);

                builder.setPositiveButton("Bỏ Qua", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }});
                builder.setPositiveButton("Chọn", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        int color = picker.getColor();
                        tvWidgetTextColor.setBackgroundColor(color);
                        SharedPreferencesUtils.setWidgetTextColor(getContext(), color);
                        updateWidgetTriger();
                        dialog.dismiss();
                    }});
                builder.show();
            }
        });
        //cpWidgetTextColor.setli
    }

    private void updateWidgetTriger(){
        Intent intent = new Intent(getContext(), LunarSolarAppWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getContext())
                .getAppWidgetIds(new ComponentName(getContext(), LunarSolarAppWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        getContext().sendBroadcast(intent);
    }

    private void btSwitchsChange(CompoundButton buttonView, boolean isChecked)
    {
        switch (buttonView.getId())
        {
            case R.id.swSettingsNgayHoangDao:
                SharedPreferencesUtils.setShowGoodDayBadDateSetting(getContext(), isChecked);
                break;
            case R.id.swSettingsChamNgon:
                SharedPreferencesUtils.setShowChamNgonSetting(getContext(), isChecked);
                break;
            case R.id.swSettingsDailyNotifyChamNgon:
                SharedPreferencesUtils.setShowNotifyChamNgonSetting(getContext(), isChecked);
                break;
            case R.id.swSettingsDailyNotifyGoodDateBadDate:
                SharedPreferencesUtils.setShowDailyNotifyGoodDateBadDateSetting(getContext(), isChecked);
                break;
            case R.id.swSettingsNotifyEvent:
                SharedPreferencesUtils.setShowDailyNotifyEventSetting(getContext(), isChecked);
                break;
            case R.id.swSettingsDailyNotifyNgayRam:
                SharedPreferencesUtils.setShowNotifyNgayRam(getContext(), isChecked);
                break;
            case R.id.swSettingsWidgetShowConGiap:
                SharedPreferencesUtils.setShowWidgetConGiap(getContext(), isChecked);
                updateWidgetTriger();
                break;
        }
    }

    private void swithInitialData(Switch buttonView)
    {
        switch (buttonView.getId())
        {
            case R.id.swSettingsNgayHoangDao:
                buttonView.setChecked(SharedPreferencesUtils.getShowGoodDayBadDateSetting(getContext()));
                break;
            case R.id.swSettingsChamNgon:
                buttonView.setChecked(SharedPreferencesUtils.getShowChamNgonSetting(getContext()));
                break;
            case R.id.swSettingsDailyNotifyChamNgon:
                buttonView.setChecked(SharedPreferencesUtils.getShowNotifyChamNgonSetting(getContext()));
                break;
            case R.id.swSettingsDailyNotifyGoodDateBadDate:
                buttonView.setChecked(SharedPreferencesUtils.getShowDailyNotifyGoodDateBadDateSetting(getContext()));
                break;
            case R.id.swSettingsNotifyEvent:
                buttonView.setChecked(SharedPreferencesUtils.getShowDailyNotifyEventSetting(getContext()));
                break;
            case R.id.swSettingsDailyNotifyNgayRam:
                buttonView.setChecked(SharedPreferencesUtils.getShowNotifyNgayRam(getContext()));
                break;
            case R.id.swSettingsWidgetShowConGiap:
                buttonView.setChecked(SharedPreferencesUtils.getShowWidgetConGiap(getContext()));
                break;

        }
    }
}