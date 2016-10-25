package com.hth.datmon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.hth.data.ChannelGroup;
import com.hth.data.ChannelItem;
import java.util.ArrayList;

/**
 * Created by Lenovo on 8/26/2016.
 */
public class ChannelsExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<ChannelGroup> channelGroups;
    private ArrayList<ChannelGroup> originalChannelGroups;

    public ChannelsExpandableListAdapter(Context context, ArrayList<ChannelGroup> channelGroups) {
        this.context = context;
        this.channelGroups = new ArrayList<ChannelGroup>();
        this.channelGroups.addAll(channelGroups);
        this.originalChannelGroups = new ArrayList<ChannelGroup>();
        this.originalChannelGroups.addAll(channelGroups);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<ChannelItem> channelList = channelGroups.get(groupPosition).getChannelList();
        return channelList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View view, ViewGroup parent) {

        ChannelItem channel = (ChannelItem) getChild(groupPosition, childPosition);
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.channel_row, null);
        }
        view.setTag(channel);
        TextView name = (TextView) view.findViewById(R.id.name);
        name.setText(channel.getName().trim());
        if(childPosition % 2 == 0) {
            view.setBackgroundColor(context.getResources().getColor(R.color.item_odd_color));
        }else {
            view.setBackgroundColor(context.getResources().getColor(R.color.item_even_color));
        }
        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        ArrayList<ChannelItem> countryList = channelGroups.get(groupPosition).getChannelList();
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

        ChannelGroup channelGroup = (ChannelGroup) getGroup(groupPosition);
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.group_channel_row, null);
        }

        TextView heading = (TextView) view.findViewById(R.id.heading);
        heading.setText(channelGroup.getName().trim());
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


}
