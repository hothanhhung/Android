package com.hth.lichtivi;

import java.util.ArrayList;

import com.hth.utils.AlarmItem;
import com.hth.utils.ScheduleItem;
import com.hth.utils.UIUtils;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FlexibleScheduleRowAdapter extends ArrayAdapter<ScheduleItem> {
	private Activity activity;
    private ArrayList<ScheduleItem> data;
    private static LayoutInflater inflater=null;
    public Resources res;

    public FlexibleScheduleRowAdapter(Activity a, ArrayList<ScheduleItem> d, Resources resLocal ) {
        super( a, R.layout.schedule_row);
        activity = a;
        if(d == null) data = new ArrayList<ScheduleItem>();
        else data=d;
        res = resLocal;
        inflater = LayoutInflater.from(a) ;
    }
 
    public int getCount() {
        return data.size();
    }

    public Object onRetainNonConfigurationInstance() {
        return data ;
    }
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
    	ViewHolder viewHolder;
        TextView tvStartOn;
        TextView tvProgramName;
        LinearLayout llSetAlarm;
        ImageView ivAlarmIcon;

        ScheduleItem scheduleItem = data.get(position);
    	if(convertView==null)
        {
    		convertView = inflater.inflate(R.layout.schedule_row, null);
        	viewHolder = new ViewHolder();

            viewHolder.llSetAlarm = llSetAlarm = (LinearLayout)convertView.findViewById(R.id.llSetAlarm); // title
            viewHolder.ivAlarmIcon = ivAlarmIcon = (ImageView)convertView.findViewById(R.id.ivAlarmIcon); // title
        	viewHolder.tvStartOn = tvStartOn = (TextView)convertView.findViewById(R.id.tvStartOn); // title
        	viewHolder.tvProgramName = tvProgramName = (TextView)convertView.findViewById(R.id.tvProgramName);

            if(!scheduleItem.getStartOn().isEmpty()) {
                llSetAlarm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ScheduleItem scheduleItem = (ScheduleItem)v.getTag();
                        if(scheduleItem!=null)
                        {
                            AlarmItem alarmItem = AlarmItemsManager.getAlarmItem(activity, scheduleItem);
                            UIUtils.showSetAlarmPopup(alarmItem, activity, FlexibleScheduleRowAdapter.this);
                        }

                    }
                });
            }
        	convertView.setTag(viewHolder);
        }
    	else{
            viewHolder = (ViewHolder)convertView.getTag();
            llSetAlarm = viewHolder.llSetAlarm;
            tvStartOn = viewHolder.tvStartOn; // title
            tvProgramName = viewHolder.tvProgramName;
            ivAlarmIcon = viewHolder.ivAlarmIcon;
        }

        // Setting all values in listview
        if(position % 2 == 0) {
            convertView.setBackgroundColor(activity.getResources().getColor(R.color.item_odd_color));
        }else {
            convertView.setBackgroundColor(activity.getResources().getColor(R.color.item_even_color));
        }

        if(AlarmItemsManager.getIndex(activity, scheduleItem)!=-1)
        {
            ivAlarmIcon.setImageResource(R.drawable.alarm_active);
        }else{
            ivAlarmIcon.setImageResource(R.drawable.alarm_inactive);
        }
        llSetAlarm.setTag(scheduleItem);
        tvStartOn.setText(scheduleItem.getStartOn());
        tvProgramName.setText(scheduleItem.getTextProgramName());

        return convertView;
    }

    public void updateUI()
    {
        notifyDataSetChanged();
    }

    public void updateData(ArrayList<ScheduleItem> d)
    {
        if(d == null) data = new ArrayList<ScheduleItem>();
        else data=d;
        notifyDataSetChanged();
    }
}

class ViewHolder
{
    ImageView ivAlarmIcon;
    TextView tvStartOn;
    TextView tvProgramName;
    LinearLayout llSetAlarm;
}