package com.hth.lichtivi;

import java.util.ArrayList;

import com.hth.utils.ScheduleItem;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FlexibleScheduleRowAdapter extends ArrayAdapter<ScheduleItem> {
	private Context activity;
    private ArrayList<ScheduleItem> data;
    private static LayoutInflater inflater=null;
    public Resources res;

    public FlexibleScheduleRowAdapter(Context a, ArrayList<ScheduleItem> d, Resources resLocal ) {
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
        LinearLayout llRow;

    	if(convertView==null)
        {
    		convertView = inflater.inflate(R.layout.schedule_row, null);
        	viewHolder = new ViewHolder();

            viewHolder.llRow = llRow = (LinearLayout)convertView.findViewById(R.id.llRow); // title
        	viewHolder.tvStartOn = tvStartOn = (TextView)convertView.findViewById(R.id.tvStartOn); // title
        	viewHolder.tvProgramName = tvProgramName = (TextView)convertView.findViewById(R.id.tvProgramName);
        	convertView.setTag(viewHolder);
        }
    	else{
            viewHolder = (ViewHolder)convertView.getTag();
            llRow = viewHolder.llRow;
            tvStartOn = viewHolder.tvStartOn; // title
            tvProgramName = viewHolder.tvProgramName;
        }
        ScheduleItem scheduleItem = data.get(position);

        // Setting all values in listview
        if(position % 2 == 0) {
            llRow.setBackgroundColor(Color.parseColor("#e7e7e7"));
        }else {
            llRow.setBackgroundColor(Color.parseColor("#f4f4f4"));
        }

        tvStartOn.setText(scheduleItem.getStartOn());
        tvProgramName.setText(scheduleItem.getTextProgramName());

        return convertView;
    }

}

class ViewHolder
{
    TextView tvStartOn;
    TextView tvProgramName;
    LinearLayout llRow;
}