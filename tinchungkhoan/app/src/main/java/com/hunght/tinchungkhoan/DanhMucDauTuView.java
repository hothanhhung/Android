package com.hunght.tinchungkhoan;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hunght.data.DanhMucDauTuItem;
import com.hunght.data.StaticData;
import com.hunght.data.ThucHienQuyenItem;
import com.hunght.utils.ParserData;
import com.hunght.utils.SavedValues;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * TODO: document your custom view class.
 */
public class DanhMucDauTuView extends LinearLayout {
    ExpandableListView lvDanhMucDauTu;
    TextView tvProcessInfo;
    private static SavedValues savedValues;
    ArrayList<DanhMucDauTuItem> danhMucDauTuItems;

    public DanhMucDauTuView(Context context) {
        super(context);
        init(null, 0);
    }

    public DanhMucDauTuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public DanhMucDauTuView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final View view = inflate(getContext(), R.layout.danh_muc_dau_tu_layout, this);
        final EditText etNgayMua = view.findViewById(R.id.etNgayMua);
        final EditText etSoLuong = view.findViewById(R.id.etSoLuong);
        final EditText etMaCK = view.findViewById(R.id.etMaCK);
        final EditText etGiaMua = view.findViewById(R.id.etGiaMua);
        final Button btLưu = view.findViewById(R.id.btLưu);
        lvDanhMucDauTu = view.findViewById(R.id.lvDanhMucDauTu);
        tvProcessInfo = view.findViewById(R.id.tvProcessInfo);

        savedValues = new SavedValues(getContext());
        danhMucDauTuItems = new ArrayList<>();

        final Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        myCalendar.add(Calendar.DAY_OF_MONTH, -1);
        final DatePickerDialog.OnDateSetListener fromDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateForEditText(etNgayMua, myCalendar);
            }

        };
        final DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), fromDate, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        etNgayMua.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                datePickerDialog.show();
                View view = ((Activity)getContext()).getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

            }
        });

        updateDateForEditText(etNgayMua, myCalendar);

        btLưu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = ((Activity)getContext()).getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                String ngayMua = etNgayMua.getText().toString().trim();
                String maCK = etMaCK.getText().toString().toUpperCase().trim();
                int soLuong = convertStringToInt(etSoLuong.getText().toString());
                float giaMua = convertStringToFloat(etGiaMua.getText().toString());
                if(ngayMua.isEmpty() || maCK.isEmpty() || soLuong == 0 || giaMua == 0){
                    Toast.makeText(getContext(), "Dữ Liệu Không Hợp Lệ", Toast.LENGTH_LONG).show();
                }else {
                    String name = StaticData.getNameCongTy(maCK);
                    if(name.isEmpty()){
                        Toast.makeText(getContext(), "Mã Chứng Khoán Không Tồn Tại", Toast.LENGTH_LONG).show();
                    }else {
                        DanhMucDauTuItem danhMucDauTuItem = new DanhMucDauTuItem(ngayMua, maCK, name, giaMua, soLuong);
                        updateDanhMucDauTu(danhMucDauTuItem);
                    }
                }
            }
        });

        lvDanhMucDauTu.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                return false;
            }
        });

        updateListDanhMucDauTu();
    }

    private int convertStringToInt(String number)
    {
        number= number.trim();
        if(number.isEmpty()) return 0;
        return Integer.valueOf(number);
    }

    private float convertStringToFloat(String number)
    {
        number= number.trim();
        if(number.isEmpty()) return 0;
        return Float.valueOf(number);
    }

    private void updateDanhMucDauTu(DanhMucDauTuItem item){
        for(DanhMucDauTuItem danhMucDauTuItem: danhMucDauTuItems){
            if(danhMucDauTuItem.isTheSame(item)){
                danhMucDauTuItem.setSoLuong(danhMucDauTuItem.getSoLuong() + item.getSoLuong());
                item = null;
                break;
            }
        }

        if(item!=null){
            if(danhMucDauTuItems == null){
                danhMucDauTuItems = new ArrayList<>();
            }
            danhMucDauTuItems.add(item);
        }
        updateListDanhMucDauTu();
    }

    private void updateDateForEditText(EditText editText, Calendar calendar) {
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        editText.setText((day < 10 ? "0" : "") + day + "-" + (month < 10 ? "0" : "") + month + "-" + year);
    }


    ArrayList<String> listDataHeader;
    HashMap<String, List<DanhMucDauTuItem>> listDataChild;
    private void updateListDanhMucDauTu()
    {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap();
        if(danhMucDauTuItems == null || danhMucDauTuItems.isEmpty())
        {
            if(tvProcessInfo!=null)
            {
                tvProcessInfo.setText("Không có dữ liệu");
            }
            if(lvDanhMucDauTu !=null){
                lvDanhMucDauTu.setVisibility(GONE);
            }
        }else{
            if(tvProcessInfo!=null) {
                tvProcessInfo.setText("");
            }
            for (DanhMucDauTuItem danhMucDauTuItem:danhMucDauTuItems) {
                if(listDataHeader.contains(danhMucDauTuItem.getMaCK()))
                {
                    listDataChild.get(danhMucDauTuItem.getMaCK()).add(danhMucDauTuItem);
                }else{
                    listDataHeader.add(danhMucDauTuItem.getMaCK());
                    ArrayList<DanhMucDauTuItem> newList = new ArrayList<>();
                    newList.add(danhMucDauTuItem);
                    listDataChild.put(danhMucDauTuItem.getMaCK(), newList);
                }

            }
            ExpandableDanhMucDauTuListAdapter listAdapter = new ExpandableDanhMucDauTuListAdapter(getContext(), listDataHeader, listDataChild);
            // setting list adapter
            if(lvDanhMucDauTu !=null)
            {
                lvDanhMucDauTu.setVisibility(VISIBLE);
                lvDanhMucDauTu.setAdapter(listAdapter);
                for(int i = 0; i < listDataHeader.size(); i++)
                {
                    lvDanhMucDauTu.expandGroup(i, true);
                }
            }
        }

    }

}
