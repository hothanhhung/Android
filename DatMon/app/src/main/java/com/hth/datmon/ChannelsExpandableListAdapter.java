package com.hth.datmon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hth.service.Areas;
import com.hth.service.Desk;

import java.util.ArrayList;

/**
 * Created by Lenovo on 8/26/2016.
 */
public class ChannelsExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<Areas> channelGroups;

    public ChannelsExpandableListAdapter(Context context, ArrayList<Areas> channelGroups) {
        this.context = context;
        this.channelGroups = new ArrayList<Areas>();
        this.channelGroups.addAll(channelGroups);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<Desk> channelList = channelGroups.get(groupPosition).getDesks();
        return channelList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View view, ViewGroup parent) {

        Desk desk = (Desk) getChild(groupPosition, childPosition);
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.channel_row, null);
        }
        view.setTag(desk);
        TextView name = (TextView) view.findViewById(R.id.name);
        ImageView ivTable = (ImageView) view.findViewById(R.id.ivTable);
        if(desk.IsUsing()){
            ivTable.setImageResource(R.drawable.table_icon_red);
        }else{
            ivTable.setImageResource(R.drawable.table_icon_green);
        }
        name.setText(desk.getName().trim());
        if(childPosition % 2 == 0) {
            view.setBackgroundColor(context.getResources().getColor(R.color.item_odd_color));
        }else {
            view.setBackgroundColor(context.getResources().getColor(R.color.item_even_color));
        }
        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        ArrayList<Desk> countryList = channelGroups.get(groupPosition).getDesks();
        return countryList.size();

    }

    @Override
    public Object getGroup(int groupPosition) {
        return channelGroups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return channelGroups.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view,
                             ViewGroup parent) {

        Areas area = (Areas) getGroup(groupPosition);
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.group_channel_row, null);
        }

        TextView heading = (TextView) view.findViewById(R.id.heading);
        heading.setText(area.getName());
        if(groupPosition % 2 == 0) {
            view.setBackgroundColor(context.getResources().getColor(R.color.group_item_odd_color));
        }else {
            view.setBackgroundColor(context.getResources().getColor(R.color.group_item_even_color));
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

    public void updateData(ArrayList<Areas> channelGroups)
    {
        this.channelGroups = channelGroups;
        notifyDataSetChanged();
    }
}
