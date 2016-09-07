package com.hth.lichtivi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hth.data.DataAlarm;
import com.hth.utils.AlarmItem;
import com.hth.utils.AlarmItemsManager;
import com.hth.utils.ScheduleItem;

import java.util.ArrayList;

public class FlexibleAlarmManagerRowAdapter extends ArrayAdapter<AlarmItem> {
	private Activity activity;
    private ArrayList<AlarmItem> data;
    private static LayoutInflater inflater=null;

    public FlexibleAlarmManagerRowAdapter(Activity a, ArrayList<AlarmItem> d ) {
        super( a, R.layout.schedule_row);
        activity = a;
        if(d == null) data = new ArrayList<AlarmItem>();
        else data=d;
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
        ViewHolderAlarmManager viewHolder;
        TextView tvProgramName;
        TextView tvChannelName;
        TextView tvStartOn;
        TextView tvRemindBefore;
        ImageButton btDeleteAlarm;

        AlarmItem alarmItem = data.get(position);
    	if(convertView==null)
        {
    		convertView = inflater.inflate(R.layout.alarm_manager_row, null);
        	viewHolder = new ViewHolderAlarmManager();

            viewHolder.tvProgramName = tvProgramName = (TextView)convertView.findViewById(R.id.tvProgramName); // title
            viewHolder.tvChannelName = tvChannelName = (TextView)convertView.findViewById(R.id.tvChannelName); // title
        	viewHolder.tvStartOn = tvStartOn = (TextView)convertView.findViewById(R.id.tvStartOn); // title
        	viewHolder.tvRemindBefore = tvRemindBefore = (TextView)convertView.findViewById(R.id.tvRemindBefore);
            viewHolder.btDeleteAlarm = btDeleteAlarm = (ImageButton)convertView.findViewById(R.id.btDeleteAlarm);

            if(!alarmItem.getStartOn().isEmpty()) {
                btDeleteAlarm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlarmItem alarmItem = (AlarmItem)v.getTag();
                        if(alarmItem!=null)
                        {
                            new AlertDialog.Builder(activity)
                                    .setTitle("Delete")
                                    .setMessage("Do you really want to remove " +alarmItem.getProgramName()+ " ?")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int whichButton) {

                                            AlarmItemsManager.deleteAlarm(activity, alarmItem);
                                            updateData(DataAlarm.getAlarms(activity));
                                        }})
                                    .setNegativeButton(android.R.string.no, null).show();
                        }

                    }
                });
            }
        	convertView.setTag(viewHolder);
        }
    	else{
            viewHolder = (ViewHolderAlarmManager)convertView.getTag();
            tvChannelName = viewHolder.tvChannelName;
            tvStartOn = viewHolder.tvStartOn; // title
            tvRemindBefore = viewHolder.tvRemindBefore;
            btDeleteAlarm = viewHolder.btDeleteAlarm;
            tvProgramName = viewHolder.tvProgramName;
        }

        // Setting all values in listview
        if(position % 2 == 0) {
            convertView.setBackgroundColor(Color.parseColor("#e7e7e7"));
        }else {
            convertView.setBackgroundColor(Color.parseColor("#f4f4f4"));
        }

        btDeleteAlarm.setTag(alarmItem);
        tvProgramName.setText(alarmItem.getProgramName()); //
        tvChannelName.setText(alarmItem.getChannelName());
        tvStartOn .setText(alarmItem.getStartOn());
        tvRemindBefore.setText(alarmItem.getRemindBeforeInMinute() + " phút");

        return convertView;
    }

    public void updateData( ArrayList<AlarmItem> d )
    {
        if(d == null) data = new ArrayList<AlarmItem>();
        else data=d;
        notifyDataSetChanged();
    }
}

class ViewHolderAlarmManager
{
    TextView tvProgramName;
    TextView tvChannelName;
    TextView tvStartOn;
    TextView tvRemindBefore;
    ImageButton btDeleteAlarm;
}