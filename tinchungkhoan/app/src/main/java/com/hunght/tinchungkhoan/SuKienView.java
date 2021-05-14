package com.hunght.tinchungkhoan;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.hunght.data.KeyValueItem;
import com.hunght.data.MenuLookUpItemKind;
import com.hunght.data.StaticData;
import com.hunght.data.SuKienItem;
import com.hunght.utils.ParserData;
import com.hunght.utils.SavedValues;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * TODO: document your custom view class.
 */
public class SuKienView extends LinearLayout {
    ListView lvSuKien;
    TextView tvProcessInfo;
    private static SavedValues savedValues;

    public SuKienView(Context context) {
        super(context);
        init(null, 0);
    }

    public SuKienView(Context context, MenuLookUpItemKind kind) {
        this(context);
    }

    public SuKienView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SuKienView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final View view = inflate(getContext(), R.layout.su_kien_layout, this);
        final Spinner spLoaiSuKien = (Spinner) view.findViewById(R.id.spLoaiSuKien);
        final EditText etFromDate = (EditText) view.findViewById(R.id.etFromDate);
        final EditText etToDate = (EditText) view.findViewById(R.id.etToDate);
        final EditText etMaCK = (EditText) view.findViewById(R.id.etMaCK);
        final Button btTraCuu = (Button) view.findViewById(R.id.btTraCuu);
        lvSuKien = view.findViewById(R.id.lvSuKien);
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
        //myCalendarFromDate.add(Calendar.DAY_OF_MONTH, -1);
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

        etFromDate.setInputType(InputType.TYPE_NULL);
        etFromDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), fromDate, myCalendarFromDate
                        .get(Calendar.YEAR), myCalendarFromDate.get(Calendar.MONTH),
                        myCalendarFromDate.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        etToDate.setInputType(InputType.TYPE_NULL);
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


        ArrayAdapter<KeyValueItem> adapterLoai = new ArrayAdapter<KeyValueItem>(
                getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                StaticData.GetLoaiSuKien()
        );

        spLoaiSuKien.setAdapter(adapterLoai);

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

                updateListSuKien(ParserData.getSuKienItems(etMaCK.getText().toString(), ((KeyValueItem)spLoaiSuKien.getSelectedItem()).getKey(), getDateInStringYYYYMMdd(myCalendarFromDate), getDateInStringYYYYMMdd(myCalendarToDate)));
            }
        });
    }


    private void updateDateForEditText(EditText editText, Calendar calendar) {
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = (calendar.get(Calendar.MONTH) + 1);
        editText.setText((day > 9 ? day : "0" + day) + " - " + (month > 9 ? month : "0" + month) + " - " + calendar.get(Calendar.YEAR));
    }

    private String getDateInStringYYYYMMdd(Calendar calendar)
    {
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = (calendar.get(Calendar.MONTH) + 1);
        return calendar.get(Calendar.YEAR)  + "-" + (month > 9 ? month : "0" + month) + "-" + (day > 9 ? day : "0" + day);
    }

    ArrayList<String> listDataHeader;
    HashMap<String, List<SuKienItem>> listDataChild;
    private void updateListSuKien(ArrayList<SuKienItem> data)
    {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap();
        if(data.isEmpty())
        {
            if(tvProcessInfo!=null)
            {
                tvProcessInfo.setText("Không có dữ liệu");
            }
            if(lvSuKien !=null){
                lvSuKien.setVisibility(GONE);
            }
        }else{
            if(tvProcessInfo!=null) {
                tvProcessInfo.setText("Vui lòng thực hiện tra cứu");
            }
            SuKienItemAdapter listAdapter = new SuKienItemAdapter(getContext(), data);
            // setting list adapter
            if(lvSuKien !=null)
            {
                lvSuKien.setVisibility(VISIBLE);
                lvSuKien.setAdapter(listAdapter);
            }
        }

    }

}
