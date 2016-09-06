package com.hth.lichtivi;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.hth.utils.MethodsHelper;
import com.hth.utils.ParseJSONScheduleItems;
import com.hth.utils.SearchProgramItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Lenovo on 8/31/2016.
 */
public class AlarmManagerView extends LinearLayout {
    Context context;
    ListView lvAlarms;
    TextView tvMessage;

    public AlarmManagerView(Context context) {
        super(context);
        initView();
    }

    public AlarmManagerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public AlarmManagerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initView();
    }

    private void initView()
    {
        inflate(getContext(), R.layout.alarm_manager_layout, this);
        lvAlarms = (ListView) this.findViewById(R.id.lvAlarms);
        tvMessage = (TextView) this.findViewById(R.id.tvMessage);
    }

}
