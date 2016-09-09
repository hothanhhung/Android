package com.hth.lichtivi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hth.data.DataAlarm;
import com.hth.utils.AlarmItem;
import com.hth.utils.AlarmItemsManager;
import com.hth.utils.ScheduleItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FlexibleAlarmManagerRowAdapter  extends BaseExpandableListAdapter {
    private Activity activity;
    private static LayoutInflater inflater=null;
    private ArrayList<AlarmItem> oldAlarmItems;
    private ArrayList<AlarmItem> nextlarmItems;
    private ArrayList<String> alarmItemGroups =  new ArrayList<String>() {{ add("Sắp đến"); add("Đã qua");}};

    public FlexibleAlarmManagerRowAdapter(Activity activity, ArrayList<AlarmItem> alarmItems) {
        this.activity = activity;
        inflater = LayoutInflater.from(activity) ;
        resolveData(alarmItems);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if(groupPosition == 0){
            return nextlarmItems.get(childPosition);
        }else {
            return oldAlarmItems.get(childPosition);
        }
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {

        ViewHolderAlarmManager viewHolder;
        TextView tvProgramName;
        TextView tvChannelName;
        TextView tvStartOn;
        TextView tvRemindBefore;
        ImageButton btDeleteAlarm;

        AlarmItem alarmItem = (AlarmItem) getChild(groupPosition, childPosition);
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
        if(childPosition % 2 == 0) {
            convertView.setBackgroundColor(activity.getResources().getColor(R.color.item_odd_color));
        }else {
            convertView.setBackgroundColor(activity.getResources().getColor(R.color.item_even_color));
        }

        btDeleteAlarm.setTag(alarmItem);
        tvProgramName.setText(alarmItem.getProgramName()); //
        tvChannelName.setText(alarmItem.getChannelName());
        tvStartOn .setText(alarmItem.getStartOn());
        tvRemindBefore.setText(alarmItem.getRemindBeforeInMinute() + " phút");

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        if(groupPosition == 0){
            return nextlarmItems.size();
        }else {
            return oldAlarmItems.size();
        }

    }

    @Override
    public Object getGroup(int groupPosition) {
        return alarmItemGroups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return alarmItemGroups.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view,
                             ViewGroup parent) {

        String groupName = (String) getGroup(groupPosition);
        if (view == null) {
            view = inflater.inflate(R.layout.group_channel_row, null);
        }

        TextView heading = (TextView) view.findViewById(R.id.heading);
        heading.setText(groupName);
        if(groupPosition % 2 == 0) {
            view.setBackgroundColor(activity.getResources().getColor(R.color.group_item_odd_color));
        }else {
            view.setBackgroundColor(activity.getResources().getColor(R.color.group_item_even_color));
        }
        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void updateData( ArrayList<AlarmItem> d )
    {
        resolveData(d);
        notifyDataSetChanged();
    }

    private void resolveData(ArrayList<AlarmItem> alarmItems){
        if(oldAlarmItems== null ) oldAlarmItems = new ArrayList<>();
        else oldAlarmItems.clear();

        if(nextlarmItems== null ) nextlarmItems = new ArrayList<>();
        else nextlarmItems.clear();

        if(alarmItems != null) {
            long currentTime = System.currentTimeMillis();

            for (AlarmItem alarmItem : alarmItems) {
                if (alarmItem.getTimeToRemindInMiliSecond() > currentTime) {
                    nextlarmItems.add(alarmItem);
                } else {
                    oldAlarmItems.add(alarmItem);
                }
            }
            Collections.sort(nextlarmItems, new Comparator<AlarmItem>() {
                public int compare(AlarmItem o1, AlarmItem o2) {
                    return (int) (o1.getTimeToRemindInMiliSecond() - o2.getTimeToRemindInMiliSecond()) / 1000;
                }
            });

            Collections.sort(oldAlarmItems, new Comparator<AlarmItem>() {
                public int compare(AlarmItem o1, AlarmItem o2) {
                    return (int) (o2.getTimeToRemindInMiliSecond() - o1.getTimeToRemindInMiliSecond());
                }
            });
        }
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