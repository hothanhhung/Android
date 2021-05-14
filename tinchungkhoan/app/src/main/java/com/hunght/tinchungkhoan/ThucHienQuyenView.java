package com.hunght.tinchungkhoan;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;

import androidx.annotation.UiThread;

import android.text.TextUtils;
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
import android.widget.Toast;

import com.hunght.data.MenuLookUpItemKind;
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
public class ThucHienQuyenView extends LinearLayout {
    ExpandableListView lvThucHienQuyen;
    TextView tvProcessInfo;
    private static SavedValues savedValues;

    public ThucHienQuyenView(Context context) {
        super(context);
        init(null, 0);
    }

    public ThucHienQuyenView(Context context, MenuLookUpItemKind kind) {
        this(context);
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
        TextView tvGetFromFavorite = view.findViewById(R.id.tvGetFromFavorite);

        savedValues = new SavedValues(getContext());

        tvGetFromFavorite.setPaintFlags(tvGetFromFavorite.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvGetFromFavorite.setTextColor(Color.BLUE);
        tvGetFromFavorite.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> items = savedValues.getFavorites();
                etMaCK.setText(TextUtils.join(", ", items));
            }
        });

        final Calendar myCalendarFromDate = Calendar.getInstance();
        myCalendarFromDate.set(myCalendarFromDate.get(Calendar.YEAR), myCalendarFromDate.get(Calendar.MONTH), myCalendarFromDate.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
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

        final Calendar myCalendarToDate = (Calendar)myCalendarFromDate.clone();
        myCalendarToDate.add(Calendar.DAY_OF_MONTH, 7);
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
                ((Activity)getContext()).runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                tvProcessInfo.setText("Đang nhận dữ liệu....");
                            }
                        });

                Calendar calendar = (Calendar) myCalendarFromDate.clone();
                calendar.add(Calendar.DAY_OF_MONTH, 7);
                boolean isMoreDay = myCalendarToDate.compareTo(calendar) > 0;
                updateListThucHienQuyen(ParserData.getThucHienQuyenItems(etMaCK.getText().toString(), StaticData.GetValueFromThiTruong(spThiTruong.getSelectedItem().toString()), StaticData.GetValueFromLoaiChungKhoan(spLoaiChungKhoan.getSelectedItem().toString()), getDateInString(myCalendarFromDate), getDateInString(myCalendarToDate), isMoreDay));
            }
        });

        lvThucHienQuyen.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                ThucHienQuyenItem thucHienQuyenItem = (ThucHienQuyenItem) v.getTag();
                Uri webpage = Uri.parse(thucHienQuyenItem.getFullUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                    getContext().startActivity(intent);
                }else{
                    Toast.makeText(getContext(), "Không tìm thấy trình duyệt", Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });
    }


    private void updateDateForEditText(EditText editText, Calendar calendar) {
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = (calendar.get(Calendar.MONTH) + 1);
        editText.setText((day > 9 ? day : "0" + day) + " - " + (month > 9 ? month : "0" + month) + " - " + calendar.get(Calendar.YEAR));
    }

    private String getDateInString(Calendar calendar)
    {
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = (calendar.get(Calendar.MONTH) + 1);
        return (day > 9 ? day : "0" + day)  + "/" + (month > 9 ? month : "0" + month) + "/" + calendar.get(Calendar.YEAR);
    }

    ArrayList<String> listDataHeader;
    HashMap<String, List<ThucHienQuyenItem>> listDataChild;
    private void updateListThucHienQuyen(ArrayList<ThucHienQuyenItem> data)
    {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap();
        if(data.isEmpty())
        {
            if(tvProcessInfo!=null)
            {
                tvProcessInfo.setText("Không có dữ liệu");
            }
            if(lvThucHienQuyen!=null){
                lvThucHienQuyen.setVisibility(GONE);
            }
        }else{
            if(tvProcessInfo!=null) {
                tvProcessInfo.setText("Vui lòng thực hiện tra cứu");
            }
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
                lvThucHienQuyen.setVisibility(VISIBLE);
                lvThucHienQuyen.setAdapter(listAdapter);
                if(listDataHeader.size() > 0)
                {
                    lvThucHienQuyen.expandGroup(0, true);
                }
            }
        }

    }

}
