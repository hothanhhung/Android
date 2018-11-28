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
import com.hunght.data.StaticData;
import com.hunght.utils.MethodsHelper;

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
        java.util.Collections.sort(this._listDataGroup);
        for(String str:this._listDataGroup){
            List<DanhMucDauTuItem> list = items.get(str);
            if (list.size() > 0) {
                java.util.Collections.sort(list, new java.util.Comparator<DanhMucDauTuItem>() {
                    @Override
                    public int compare(final DanhMucDauTuItem object1, final DanhMucDauTuItem object2) {
                        return -1 * object1.compareDate(object2);
                    }
                });
            }
        }
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
        tvGiaThiTruong.setText(getStringFromFloat(danhMucDauTuItem.getGiaBanHoacThiTruong()));
        tvLoiNhuan.setText(getStringFromFloat(loiNhuan));

        if(loiNhuan == 0)
        {
            tvLoiNhuan.setTextColor(_context.getResources().getColor(R.color.giaZero));
            tvGiaThiTruong.setTextColor(_context.getResources().getColor(R.color.giaZero));
        }else if(loiNhuan > 0)
        {
            tvLoiNhuan.setTextColor(_context.getResources().getColor(R.color.giaDuong));
            tvGiaThiTruong.setTextColor(_context.getResources().getColor(R.color.giaDuong));
        }else{
            tvLoiNhuan.setTextColor(_context.getResources().getColor(R.color.giaAm));
            tvGiaThiTruong.setTextColor(_context.getResources().getColor(R.color.giaAm));
        }
        if(childPosition % 2 == 0) {
            convertView.setBackgroundColor(_context.getResources().getColor(R.color.item_odd_color));
        }else {
            convertView.setBackgroundColor(_context.getResources().getColor(R.color.item_even_color));
        }
        if(danhMucDauTuItem.daBan()){
            tvNgay.setTypeface(Typeface.DEFAULT_BOLD);
            tvTen.setTypeface(Typeface.DEFAULT_BOLD);
            tvSoLuong.setTypeface(Typeface.DEFAULT_BOLD);
            tvGiaThiTruong.setTypeface(Typeface.DEFAULT_BOLD);
            tvGiaMua.setTypeface(Typeface.DEFAULT_BOLD);
            tvLoiNhuan.setTypeface(Typeface.DEFAULT_BOLD);
        }else{
            tvNgay.setTypeface(Typeface.DEFAULT);
            tvTen.setTypeface(Typeface.DEFAULT);
            tvSoLuong.setTypeface(Typeface.DEFAULT);
            tvGiaThiTruong.setTypeface(Typeface.DEFAULT);
            tvGiaMua.setTypeface(Typeface.DEFAULT);
            tvLoiNhuan.setTypeface(Typeface.DEFAULT);
        }
        convertView.setTag(danhMucDauTuItem);
        return convertView;
    }

    public String getStringFromFloat(float number) {
        return MethodsHelper.getStringFromFloat(number);
    }

    public String getStringFromInt(int number) {
        return MethodsHelper.getStringFromInt(number);
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

        TextView tvTongDauTu = convertView.findViewById(R.id.tvTongDauTu);
        TextView tvTongThiTruong = convertView.findViewById(R.id.tvTongThiTruong);
        TextView tvTongLoiNhuan = convertView.findViewById(R.id.tvTongLoiNhuan);

        float loiNhuan = 0, dauTu = 0, thiTruong = 0;
        for(DanhMucDauTuItem danhMucDauTuItem: groupItems){
            loiNhuan += danhMucDauTuItem.getLoiNhan();
            dauTu += danhMucDauTuItem.getTongDauTu();
            thiTruong += danhMucDauTuItem.getTongThiTruongHoacBan();
        }
        tvTongDauTu.setText(getStringFromFloat(dauTu));
        tvTongThiTruong.setText(getStringFromFloat(thiTruong));
        tvTongLoiNhuan.setText(getStringFromFloat(loiNhuan));

        if(loiNhuan == 0)
        {
            tvTongLoiNhuan.setTextColor(_context.getResources().getColor(R.color.giaZero));
        }else if(loiNhuan > 0)
        {
            tvTongLoiNhuan.setTextColor(_context.getResources().getColor(R.color.giaDuong));
        }else{
            tvTongLoiNhuan.setTextColor(_context.getResources().getColor(R.color.giaAm));
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
