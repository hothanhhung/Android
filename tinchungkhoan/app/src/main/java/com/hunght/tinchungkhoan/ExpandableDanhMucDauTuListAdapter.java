package com.hunght.tinchungkhoan;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.hunght.data.DanhMucDauTuItem;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Lenovo on 6/10/2016.
 */
public class ExpandableDanhMucDauTuListAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> _listDataGroup; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<DanhMucDauTuItem>> _profileItems;

    public ExpandableDanhMucDauTuListAdapter(Context context, List<String> listDataGroup,
                                             HashMap<String, List<DanhMucDauTuItem>> items) {
        this._context = context;
        this._listDataGroup = listDataGroup;
        this._profileItems = items;
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

        DanhMucDauTuItem danhMucDauTuItem = ((DanhMucDauTuItem) getChild(groupPosition, childPosition));

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.danh_muc_dau_tu_item, null);
        }

        TextView tvNgay = convertView.findViewById(R.id.tvNgay);
        TextView tvTen = convertView.findViewById(R.id.tvTen);
        TextView tvSoLuong = convertView.findViewById(R.id.tvSoLuong);
        TextView tvGiaMua = convertView.findViewById(R.id.tvGiaMua);
        TextView tvGiaThiTruong = convertView.findViewById(R.id.tvGiaThiTruong);
        TextView tvLoiNhuan = convertView.findViewById(R.id.tvLoiNhuan);

        float loiNhuan = danhMucDauTuItem.getLoiNhan();
        tvNgay.setText(danhMucDauTuItem.getNgayMua());
        tvTen.setText(danhMucDauTuItem.getMaCK());
        tvSoLuong.setText(getStringFromInt(danhMucDauTuItem.getSoLuong()));
        tvGiaMua.setText(getStringFromFloat(danhMucDauTuItem.getGiaMua()));
        tvGiaThiTruong.setText(getStringFromFloat(danhMucDauTuItem.getGiaThiTruong()));
        tvLoiNhuan.setText(getStringFromFloat(loiNhuan));

        if(loiNhuan == 0)
        {
            tvLoiNhuan.setTextColor(Color.rgb(204, 204, 0));
        }else if(loiNhuan > 0)
        {
            tvLoiNhuan.setTextColor(Color.GREEN);
        }else{
            tvLoiNhuan.setTextColor(Color.RED);
        }
        if(childPosition % 2 == 0) {
            convertView.setBackgroundColor(_context.getResources().getColor(R.color.item_odd_color));
        }else {
            convertView.setBackgroundColor(_context.getResources().getColor(R.color.item_even_color));
        }
        convertView.setTag(danhMucDauTuItem);
        return convertView;
    }

    public String getStringFromFloat(float number) {
        return String.format("%.2f", number);
    }

    public String getStringFromInt(int number) {
        return String.format("%d", number);
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

    public Object getGroupItem(int groupPosition) {
        return this._profileItems.get(this._listDataGroup.get(groupPosition));
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
        List<DanhMucDauTuItem> groupItems = (List<DanhMucDauTuItem>) getGroupItem(groupPosition);
        String headerTitle = groupItems.get(0).getMaCKAndTenCongTy();
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.danh_muc_dau_tu_group, null);
        }

        TextView lblListHeader = convertView
                .findViewById(R.id.tvProfileGroup);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        TextView tvTongLoiNhuan = convertView.findViewById(R.id.tvTongLoiNhuan);

        float loiNhuan = 0;
        for(DanhMucDauTuItem danhMucDauTuItem: groupItems){
            loiNhuan += danhMucDauTuItem.getLoiNhan();
        }
        tvTongLoiNhuan.setText(getStringFromFloat(loiNhuan));

        if(loiNhuan == 0)
        {
            tvTongLoiNhuan.setTextColor(Color.rgb(204, 204, 0));
        }else if(loiNhuan > 0)
        {
            tvTongLoiNhuan.setTextColor(Color.GREEN);
        }else{
            tvTongLoiNhuan.setTextColor(Color.RED);
        }

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
