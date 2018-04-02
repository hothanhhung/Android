package com.hunght.solarlunarcalendar;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hunght.data.DateItemForGridview;
import com.hunght.utils.DateTools;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Lenovo on 4/2/2018.
 */

public class ExchangeToolView extends LinearLayout {
    int date, month, year;
    NumberPicker npExchangeToolDate, npExchangeToolMonth, npExchangeToolYear;
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
            }
        });

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
