package com.hunght.solarlunarcalendar;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.hunght.data.DateItemForGridview;
import com.hunght.utils.ServiceProcessor;

import java.util.Date;

/**
 * Created by Lenovo on 4/16/2018.
 */

public class GoodDateBabDateView extends LinearLayout {

    int date, month, year;
    NumberPicker npGoodDateBadDateDate, npGoodDateBadDateMonth, npGoodDateBadDateYear;
    WebView wvGoodDateBadDateInfoDate;
    TextView tvGoodDateBadDateInfoDate;
    PerformServiceProcessBackgroundTask currentPerformServiceProcessBackgroundTask;

    public GoodDateBabDateView(Context context) {
        super(context);
        initView();
    }

    public GoodDateBabDateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public GoodDateBabDateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GoodDateBabDateView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {

        Date today = new Date();
        View view = inflate(getContext(), R.layout.good_date_bad_date_view, this);
        date = today.getDate();
        month = today.getMonth();
        year = today.getYear() + 1900;

        wvGoodDateBadDateInfoDate = (WebView) view.findViewById(R.id.wvGoodDateBadDateInfoDate);
        tvGoodDateBadDateInfoDate = (TextView) view.findViewById(R.id.tvGoodDateBadDateInfoDate);
        npGoodDateBadDateDate = (NumberPicker) view.findViewById(R.id.npGoodDateBadDateDate);
        npGoodDateBadDateMonth = (NumberPicker) view.findViewById(R.id.npGoodDateBadDateMonth);
        npGoodDateBadDateYear = (NumberPicker) view.findViewById(R.id.npGoodDateBadDateYear);
        Button btGoodDateBadDateOk = (Button) view.findViewById(R.id.btGoodDateBadDateOk);

        npGoodDateBadDateMonth.setDisplayedValues( new String[] { "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4",
                "Tháng 5", "Tháng 6", "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12",} );

        npGoodDateBadDateDate.setMinValue(1);
        npGoodDateBadDateDate.setMaxValue(31);
        npGoodDateBadDateMonth.setMinValue(0);
        npGoodDateBadDateMonth.setMaxValue(11);
        npGoodDateBadDateYear.setMinValue(1);
        npGoodDateBadDateYear.setMaxValue(Integer.MAX_VALUE);
        npGoodDateBadDateDate.setValue(date);
        npGoodDateBadDateMonth.setValue(month);
        npGoodDateBadDateYear.setValue(year);
        npGoodDateBadDateYear.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                callPopupWindowGetYear();
            }
        });
        btGoodDateBadDateOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                date = npGoodDateBadDateDate.getValue();
                month = npGoodDateBadDateMonth.getValue();
                year = npGoodDateBadDateYear.getValue();

                showDetailDate(DateItemForGridview.createDateItemForGridview(date, month + 1, year, false, true));

            }
        });

        npGoodDateBadDateMonth.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                validateDate();
            }
        });
        npGoodDateBadDateYear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                validateDate();
            }
        });

        showDetailDate(new DateItemForGridview(null, today, false));
    }

    private void showDetailDate(DateItemForGridview exchangedDate)
    {
        if(exchangedDate!=null && !exchangedDate.isTitle())
        {
            if(currentPerformServiceProcessBackgroundTask!=null)
            {
                currentPerformServiceProcessBackgroundTask.cancel(true);
                currentPerformServiceProcessBackgroundTask = null;
            }
            currentPerformServiceProcessBackgroundTask = new PerformServiceProcessBackgroundTask();
            currentPerformServiceProcessBackgroundTask.execute(ServiceProcessor.SERVICE_GET_INFO_OF_DATE, exchangedDate.getDisplaySolarDate(), exchangedDate.getThapNhiBatTu());

            String str = "";
            str += exchangedDate.getDayOfWeekInString()+", "+exchangedDate.getSolarInfo(false) + " dương lịch";
            str += "\nNhằm "+exchangedDate.getLunarInfo(false) + "\n" + exchangedDate.getLunarInfo1(false);
            str += exchangedDate.isGoodDay()?"\nLà ngày hoàng đạo":( exchangedDate.isBadDay()?"\nLà ngày hắc đạo":"");
            str += "\n"+exchangedDate.getLunarGoodTime();
            tvGoodDateBadDateInfoDate.setText(str);
        }else{
            wvGoodDateBadDateInfoDate.loadDataWithBaseURL("", "<div style='text-align: center;'>Ngày không hợp lệ</div>", "text/html", "UTF-8", "");
        }
    }

    private void validateDate() {
        int m = npGoodDateBadDateMonth.getValue() + 1;
        int y = npGoodDateBadDateYear.getValue();

        if (m == 2) {
            if ((y % 400 == 0) || (y % 4 == 0 && y % 100 == 0)) {
                int d = npGoodDateBadDateDate.getValue();
                npGoodDateBadDateDate.setMaxValue(29);
                if (d > 29) npGoodDateBadDateDate.setValue(29);
            } else {
                int d = npGoodDateBadDateDate.getValue();
                npGoodDateBadDateDate.setMaxValue(28);
                if (d > 28) npGoodDateBadDateDate.setValue(28);
            }
        } else if (m == 1 || m == 3 || m == 5 || m == 7 || m == 8 || m == 10 || m == 12) {
            npGoodDateBadDateDate.setMaxValue(31);
        } else {
            int d = npGoodDateBadDateDate.getValue();
            npGoodDateBadDateDate.setMaxValue(30);
            if (d > 30) npGoodDateBadDateDate.setValue(30);
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
                npGoodDateBadDateYear.setValue(year);
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

    class PerformServiceProcessBackgroundTask extends AsyncTask< Object, Object, Object >
    {
        private int type;

        protected void onPreExecute()
        {
            wvGoodDateBadDateInfoDate.loadDataWithBaseURL("", "<div style='text-align: center;'>Loading...</div>", "text/html", "UTF-8", "");

        }

        protected Object doInBackground(Object... params)
        {
            type = Integer.parseInt(params[0].toString());
            switch (type){
                case ServiceProcessor.SERVICE_GET_INFO_OF_DATE:
                    return ServiceProcessor.getInfoDate(params[1].toString(),Integer.parseInt(params[2].toString()));

            }
            return null;
        }

        protected void onPostExecute(Object object)
        {
            switch (type){
                case ServiceProcessor.SERVICE_GET_INFO_OF_DATE:
                    if(object != null) {
                        String str = String.valueOf(object);
                        Log.d("onPostExecute",str);
                        if(str!=null) {
                            wvGoodDateBadDateInfoDate.loadDataWithBaseURL("", str, "text/html", "UTF-8", "");
                        }else{
                            Toast.makeText(getContext(),"Error to connect to server", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(getContext(),"Error to connect to server", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

        }

    }
}
