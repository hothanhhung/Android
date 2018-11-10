package com.hunght.tinchungkhoan;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.hunght.data.StaticData;
import com.hunght.data.ThucHienQuyenItem;
import com.hunght.utils.ParserData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * TODO: document your custom view class.
 */
public class ThucHienQuyenView extends LinearLayout {
    ExpandableListView lvThucHienQuyen;
    TextView tvProcessInfo;

    public ThucHienQuyenView(Context context) {
        super(context);
        init(null, 0);
    }

    public ThucHienQuyenView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ThucHienQuyenView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final View view = inflate(getContext(), R.layout.thuc_hien_quyen_layout, this);
        final Spinner spLoaiChungKhoan = (Spinner) view.findViewById(R.id.spLoaiChungKhoan);
        final Spinner spThiTruong = (Spinner) view.findViewById(R.id.spThiTruong);
        final EditText etFromDate = (EditText) view.findViewById(R.id.etFromDate);
        final EditText etToDate = (EditText) view.findViewById(R.id.etToDate);
        final EditText etMaCK = (EditText) view.findViewById(R.id.etMaCK);
        final Button btTraCuu = (Button) view.findViewById(R.id.btTraCuu);
        lvThucHienQuyen = (ExpandableListView) view.findViewById(R.id.lvThucHienQuyen);
        tvProcessInfo = (TextView) view.findViewById(R.id.tvProcessInfo);

        final Calendar myCalendarFromDate = Calendar.getInstance();
        myCalendarFromDate.add(Calendar.DAY_OF_MONTH, -1);
        final DatePickerDialog.OnDateSetListener fromDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendarFromDate.set(Calendar.YEAR, year);
                myCalendarFromDate.set(Calendar.MONTH, monthOfYear);
                myCalendarFromDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateForEditText(etFromDate, myCalendarFromDate);
            }

        };

        final Calendar myCalendarToDate = Calendar.getInstance();
        myCalendarToDate.add(Calendar.DAY_OF_MONTH, 6);
        final DatePickerDialog.OnDateSetListener toDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendarToDate.set(Calendar.YEAR, year);
                myCalendarToDate.set(Calendar.MONTH, monthOfYear);
                myCalendarToDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateForEditText(etToDate, myCalendarToDate);
            }

        };

        etFromDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), fromDate, myCalendarFromDate
                        .get(Calendar.YEAR), myCalendarFromDate.get(Calendar.MONTH),
                        myCalendarFromDate.get(Calendar.DAY_OF_MONTH)).show();

            }
        });


        etToDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), toDate, myCalendarToDate
                        .get(Calendar.YEAR), myCalendarToDate.get(Calendar.MONTH),
                        myCalendarToDate.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        updateDateForEditText(etFromDate, myCalendarFromDate);
        updateDateForEditText(etToDate, myCalendarToDate);


        ArrayAdapter<String> adapterLoaiChungKhoan = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_spinner_item,
                StaticData.GetLoaiChungKhoan()
        );


        ArrayAdapter<String> adapterThiTruong = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_spinner_item,
                StaticData.GetThiTruong()
        );

        spLoaiChungKhoan.setAdapter(adapterLoaiChungKhoan);
        spThiTruong.setAdapter(adapterThiTruong);

        btTraCuu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateListThucHienQuyen(ParserData.getThucHienQuyenItems("", "", "", "", ""));
            }
        });
    }


    private void updateDateForEditText(EditText editText, Calendar calendar)
    {
        editText.setText(calendar.get(Calendar.DAY_OF_MONTH) + " - " + (calendar.get(Calendar.MONTH) +1 ) + " - " + calendar.get(Calendar.YEAR));
    }

    ArrayList<String> listDataHeader = new ArrayList<String>();
    HashMap<String, List<ThucHienQuyenItem>> listDataChild = new HashMap<String, List<ThucHienQuyenItem>>();
    private void updateListThucHienQuyen(ArrayList<ThucHienQuyenItem> data)
    {
        if(data.isEmpty())
        {
            if(tvProcessInfo!=null)
            {
                tvProcessInfo.setVisibility(View.VISIBLE);
                tvProcessInfo.setText("Không có dữ liệu");
            }
        }else{
            tvProcessInfo.setVisibility(View.GONE);
            for (ThucHienQuyenItem thucHienQuyenItem:data) {
                if(listDataHeader.contains(thucHienQuyenItem.getDate()))
                {
                    listDataChild.get(thucHienQuyenItem.getDate()).add(thucHienQuyenItem);
                }else{
                    listDataHeader.add(thucHienQuyenItem.getDate());
                    ArrayList<ThucHienQuyenItem> newList = new ArrayList<ThucHienQuyenItem>();
                    newList.add(thucHienQuyenItem);
                    listDataChild.put(thucHienQuyenItem.getDate(), newList);
                }

            }
            ExpandableProfileListAdapter listAdapter = new ExpandableProfileListAdapter(getContext(), listDataHeader, listDataChild);
            // setting list adapter
            if(lvThucHienQuyen!=null)
            {
                lvThucHienQuyen.setAdapter(listAdapter);
            }
        }

    }

}
