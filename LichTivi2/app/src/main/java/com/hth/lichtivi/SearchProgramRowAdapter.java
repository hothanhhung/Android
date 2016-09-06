package com.hth.lichtivi;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hth.utils.SearchProgramItem;

import java.util.ArrayList;

/**
 * Created by Lenovo on 9/5/2016.
 */
public class SearchProgramRowAdapter extends ArrayAdapter<ArrayList<SearchProgramItem>> {
    private Context activity;
    private ArrayList<SearchProgramItem> data;
    private static LayoutInflater inflater=null;

    public SearchProgramRowAdapter(Context a, ArrayList<SearchProgramItem> d) {
        super( a, R.layout.search_program_row);
        activity = a;
        if(d == null) data = new ArrayList<SearchProgramItem>();
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
        ProgramViewHolder viewHolder;
        TextView tvChannel;
        TextView tvProgramName;
        TextView tvTime;

        if(convertView==null)
        {
            convertView = inflater.inflate(R.layout.search_program_row, null);
            viewHolder = new ProgramViewHolder();

            viewHolder.tvChannel = tvChannel = (TextView)convertView.findViewById(R.id.tvChannel); // title
            viewHolder.tvProgramName = tvProgramName = (TextView)convertView.findViewById(R.id.tvProgramName); // title
            viewHolder.tvTime = tvTime = (TextView)convertView.findViewById(R.id.tvTime); // title
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ProgramViewHolder)convertView.getTag();
            tvChannel = viewHolder.tvChannel;
            tvProgramName = viewHolder.tvProgramName; // title
            tvTime = viewHolder.tvTime;
        }
        SearchProgramItem searchProgramItem = data.get(position);

        // Setting all values in listview
        /*if(position % 2 == 0) {
            llRow.setBackgroundColor(Color.parseColor("#e7e7e7"));
        }else {
            llRow.setBackgroundColor(Color.parseColor("#f4f4f4"));
        }*/

        if(position % 2 == 0) {
            convertView.setBackgroundColor(Color.parseColor("#e7e7e7"));
        }else {
            convertView.setBackgroundColor(Color.parseColor("#f4f4f4"));
        }

        tvChannel.setText(searchProgramItem.getChannelName());
        tvProgramName.setText(searchProgramItem.getProgramName());
        tvTime.setText(searchProgramItem.getTime());
        return convertView;
    }

}

class ProgramViewHolder
{
    TextView tvChannel;
    TextView tvProgramName;
    TextView tvTime;
}
