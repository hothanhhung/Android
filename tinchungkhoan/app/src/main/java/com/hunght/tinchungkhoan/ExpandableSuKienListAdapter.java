package com.hunght.tinchungkhoan;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.hunght.data.SuKienItem;
import com.hunght.data.ThucHienQuyenItem;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Lenovo on 6/10/2016.
 */
public class ExpandableSuKienListAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> _listDataGroup; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<SuKienItem>> _profileItems;

    public ExpandableSuKienListAdapter(Context context, List<String> listDataGroup,
                                       HashMap<String, List<SuKienItem>> profileItems) {
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

        SuKienItem item = ((SuKienItem) getChild(groupPosition, childPosition));

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.thuc_hien_quyen_item, null);
        }

        TextView tvTitle = (TextView) convertView
                .findViewById(R.id.tvTitle);
        TextView tvMoreInfo = (TextView) convertView
                .findViewById(R.id.tvMoreInfo);

        tvTitle.setText(item.getTieuDe());
        tvMoreInfo.setText(item.getExtraInfo());
        if(childPosition % 2 == 0) {
            convertView.setBackgroundColor(_context.getResources().getColor(R.color.item_odd_color));
        }else {
            convertView.setBackgroundColor(_context.getResources().getColor(R.color.item_even_color));
        }
        convertView.setTag(item);
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
            convertView = infalInflater.inflate(R.layout.thuc_hien_quyen_group, null);
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
