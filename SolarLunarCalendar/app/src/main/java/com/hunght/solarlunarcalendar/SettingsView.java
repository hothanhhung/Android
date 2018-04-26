package com.hunght.solarlunarcalendar;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.hunght.data.NoteItem;
import com.hunght.utils.SharedPreferencesUtils;

import java.util.ArrayList;

/**
 * Created by Lenovo on 4/24/2018.
 */

public class SettingsView  extends LinearLayout {

    FloatingActionButton fbtNotesViewAdd;
    TextView tvNotesViewNoItem;
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
                (Switch) view.findViewById(R.id.swSettingsChamNgon)};

        for (Switch switchItem:btSwitchs) {
            swithInitialData(switchItem);

            switchItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    btSwitchsChange(buttonView, isChecked);
                }
            });
        }
    }

    private void btSwitchsChange(CompoundButton buttonView, boolean isChecked)
    {
        switch (buttonView.getId())
        {
            case R.id.swSettingsNgayHoangDao:
                SharedPreferencesUtils.setShowGoodDayBadDate(getContext(), isChecked);
                break;
            case R.id.swSettingsChamNgon:
                SharedPreferencesUtils.setShowChamNgon(getContext(), isChecked);
                break;
        }
    }

    private void swithInitialData(Switch buttonView)
    {
        switch (buttonView.getId())
        {
            case R.id.swSettingsNgayHoangDao:
                buttonView.setChecked(SharedPreferencesUtils.getShowGoodDayBadDate(getContext()));
                break;
            case R.id.swSettingsChamNgon:
                buttonView.setChecked(SharedPreferencesUtils.getShowChamNgon(getContext()));
                break;
        }
    }
}