package com.hunght.solarlunarcalendar;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hunght.data.NoteItem;

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
    }
}