package com.hth.lichtivi;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hth.data.DataAlarm;

/**
 * Created by Lenovo on 8/31/2016.
 */
public class AlarmManagerView extends LinearLayout {
    Context context;
    ListView lvAlarms;
    TextView tvMessage;
    FlexibleAlarmManagerRowAdapter flexibleAlarmManagerRowAdapter;

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
        flexibleAlarmManagerRowAdapter = new FlexibleAlarmManagerRowAdapter((Activity)context, DataAlarm.getAlarms((Activity)context));
        lvAlarms.setAdapter(flexibleAlarmManagerRowAdapter);
    }

    public void showAndUpdate()
    {
        flexibleAlarmManagerRowAdapter.updateData(DataAlarm.getAlarms((Activity)context));
        this.setVisibility(VISIBLE);
    }

    public void hideView()
    {
        this.setVisibility(INVISIBLE);
    }
}
