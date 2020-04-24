package com.hunght.solarlunarcalendar;

import android.content.Context;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.hunght.data.DateItemForGridview;
import com.hunght.utils.DateTools;

import java.util.Date;

/**
 * Created by Lenovo on 4/2/2018.
 */

public class ExchangeToolView extends LinearLayout {
    int date, month, year;
    NumberPicker npExchangeToolDate, npExchangeToolMonth, npExchangeToolYear;
    RadioButton rbLunarToSolar, rbSolarToLunar;
    TextView tvExchangeToolInfoDate;



    public ExchangeToolView(Context context) {
        super(context);
        initView();
    }

    public ExchangeToolView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ExchangeToolView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ExchangeToolView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {

        Date today = new Date();
        View view = inflate(getContext(), R.layout.exchange_tool_view, this);
        date = today.getDate();
        month = today.getMonth();
        year = today.getYear() + 1900;
        rbLunarToSolar = (RadioButton) view.findViewById(R.id.rbLunarToSolar);
        rbSolarToLunar = (RadioButton) view.findViewById(R.id.rbSolarToLunar);

        tvExchangeToolInfoDate = (TextView) view.findViewById(R.id.tvExchangeToolInfoDate);

        npExchangeToolDate = (NumberPicker) view.findViewById(R.id.npExchangeToolDate);
        npExchangeToolMonth = (NumberPicker) view.findViewById(R.id.npExchangeToolMonth);
        npExchangeToolYear = (NumberPicker) view.findViewById(R.id.npExchangeToolYear);
        Button btExchangeToolOk = (Button) view.findViewById(R.id.btExchangeToolOk);

        npExchangeToolMonth.setDisplayedValues( new String[] { "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4",
                "Tháng 5", "Tháng 6", "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12",} );

        npExchangeToolDate.setMinValue(1);
        npExchangeToolDate.setMaxValue(31);
        npExchangeToolMonth.setMinValue(0);
        npExchangeToolMonth.setMaxValue(11);
        npExchangeToolYear.setMinValue(1);
        npExchangeToolYear.setMaxValue(Integer.MAX_VALUE);
        npExchangeToolDate.setValue(date);
        npExchangeToolMonth.setValue(month);
        npExchangeToolYear.setValue(year);
        npExchangeToolYear.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                callPopupWindowGetYear();
            }
        });
        btExchangeToolOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                date = npExchangeToolDate.getValue();
                month = npExchangeToolMonth.getValue();
                year = npExchangeToolYear.getValue();

                if(rbLunarToSolar.isChecked())
                {
                    showDetailDate(DateItemForGridview.createDateItemForGridview(date, month + 1, year, false, true));
                }else if(rbSolarToLunar.isChecked())
                {
                    showDetailDate(DateItemForGridview.createDateItemForGridview(date, month + 1, year, false, false));
                }
            }
        });

        npExchangeToolMonth.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                validateDate();
            }
        });
        npExchangeToolYear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                validateDate();
            }
        });

        rbSolarToLunar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                validateDate();
            }
        });

        rbLunarToSolar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                validateDate();
            }
        });
        showDetailDate(new DateItemForGridview(null, today, false));
    }

    private void validateDate()
    {
        int m = npExchangeToolMonth.getValue() + 1;
        int y = npExchangeToolYear.getValue();
        if(rbLunarToSolar.isChecked())
        {
            int d = npExchangeToolDate.getValue();
            int dayInMonth = DateTools.numberOfDayInLunarMonth(m, y);
            npExchangeToolDate.setMaxValue(dayInMonth);
            if(d>dayInMonth) npExchangeToolDate.setValue(dayInMonth);
        }else if(rbSolarToLunar.isChecked())
        {

            if(m == 2){
                if((y%400 == 0) || (y % 4==0 && y%100 == 0)) {
                    int d = npExchangeToolDate.getValue();
                    npExchangeToolDate.setMaxValue(29);
                    if(d>29) npExchangeToolDate.setValue(29);
                }else{
                    int d = npExchangeToolDate.getValue();
                    npExchangeToolDate.setMaxValue(28);
                    if(d>28) npExchangeToolDate.setValue(28);
                }
            }
            else if(m == 1 || m == 3 || m == 5 || m == 7 || m == 8 || m  == 10 || m == 12)
            {
                npExchangeToolDate.setMaxValue(31);
            }else{
                int d = npExchangeToolDate.getValue();
                npExchangeToolDate.setMaxValue(30);
                if(d>30) npExchangeToolDate.setValue(30);
            }
        }
    }

    private void showDetailDate(DateItemForGridview exchangedDate)
    {
        if(exchangedDate!=null && !exchangedDate.isTitle())
        {
            String str = "";
            str += exchangedDate.getDayOfWeekInString()+", "+exchangedDate.getSolarInfo(false) + " dương lịch";
            str += "\nNhằm "+exchangedDate.getLunarInfo(false) + "\n" + exchangedDate.getLunarInfo1(false);
            str += exchangedDate.isGoodDay()?"\nLà ngày hoàng đạo":( exchangedDate.isBadDay()?"\nLà ngày hắc đạo":"");
            str += "\n"+exchangedDate.getLunarGoodTime();
            tvExchangeToolInfoDate.setText(str);
        }else{
            tvExchangeToolInfoDate.setText("Ngày không hợp lệ");
        }
    }

    PopupWindow popupWindowGetYear;
    private void callPopupWindowGetYear() {

        LayoutInflater layoutInflater = (LayoutInflater) getContext()
                .getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);

        View popupView = layoutInflater.inflate(R.layout.get_year_layout, null);

        final NumberPicker npGetYear1 = (NumberPicker) popupView.findViewById(R.id.npGetYear1);
        final NumberPicker npGetYear2 = (NumberPicker) popupView.findViewById(R.id.npGetYear2);
        final NumberPicker npGetYear3 = (NumberPicker) popupView.findViewById(R.id.npGetYear3);
        final NumberPicker npGetYear4 = (NumberPicker) popupView.findViewById(R.id.npGetYear4);
        npGetYear1.setMinValue(0);
        npGetYear2.setMinValue(0);
        npGetYear3.setMinValue(0);
        npGetYear4.setMinValue(0);
        npGetYear1.setMaxValue(9);
        npGetYear2.setMaxValue(9);
        npGetYear3.setMaxValue(9);
        npGetYear4.setMaxValue(9);

        int num = year;
        npGetYear4.setValue(num%10);
        num = (num/10);
        npGetYear3.setValue(num%10);
        num = (num/10);
        npGetYear2.setValue(num%10);
        num = num/10;
        npGetYear1.setValue(num);

        popupWindowGetYear = new PopupWindow(popupView,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                true);
        popupWindowGetYear.setTouchable(true);
        popupWindowGetYear.setFocusable(true);

        popupWindowGetYear.showAtLocation(popupView, Gravity.BOTTOM, 0, 0);
        ((Button) popupView.findViewById(R.id.btGetYearUpdate)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year = npGetYear1.getValue() * 1000 + npGetYear2.getValue() * 100 + npGetYear3.getValue() * 10 + npGetYear4.getValue();
                npExchangeToolYear.setValue(year);
                popupWindowGetYear.dismiss();
            }
        });

        ((Button) popupView.findViewById(R.id.btGetYearCancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindowGetYear.dismiss();
            }
        });

    }
}
