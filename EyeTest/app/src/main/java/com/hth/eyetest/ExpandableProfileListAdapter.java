package com.hth.eyetest;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.hth.data.HighScoreItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lenovo on 6/10/2016.
 */
public class ExpandableProfileListAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> _listDataGroup; // header titles
    // child data in format of header title, child title
    private HashMap<String, ArrayList<HighScoreItem>> _profileItems;

    public ExpandableProfileListAdapter(Context context, List<String> listDataGroup,
                                 HashMap<String, ArrayList<HighScoreItem>> profileItems) {
        this._context = context;
        this._listDataGroup = listDataGroup;
        this._profileItems = profileItems;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._profileItems.get(this._listDataGroup.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        HighScoreItem highScoreItem = ((HighScoreItem) getChild(groupPosition, childPosition));

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.profile_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.tvProfileItem);
        TextView txtListChild1 = (TextView) convertView
                .findViewById(R.id.tvProfileItem2);

        txtListChild.setText(highScoreItem.getName());
        txtListChild1.setText("" + highScoreItem.getScore());
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._profileItems.get(this._listDataGroup.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataGroup.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataGroup.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.profile_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.tvProfileGroup);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
