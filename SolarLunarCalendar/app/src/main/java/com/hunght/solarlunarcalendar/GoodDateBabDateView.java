package com.hunght.solarlunarcalendar;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.hunght.data.DateItemForGridview;
import com.hunght.utils.DateTools;
import com.hunght.utils.ServiceProcessor;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Lenovo on 4/16/2018.
 */

public class GoodDateBabDateView extends LinearLayout {

    int date, month, year;
    WebView wvGoodDateBadDateInfoDate;
    TextView tvGoodDateBadDateInfoDate;
    PerformServiceProcessBackgroundTask currentPerformServiceProcessBackgroundTask;

    DateItemForGridview selectedDate;
    DateItemAdapter adapter;
    Button btMonth, btYear;

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

        btMonth = (Button)view.findViewById(R.id.btMonth);
        btYear = (Button)view.findViewById(R.id.btYear);
        selectedDate = new DateItemForGridview("", new Date(), false);

        ArrayList<DateItemForGridview> lstDateItemForGridview = DateTools.GetDateItemsForGridviewFromDate();

        GridView grvDates = (GridView) findViewById(R.id.grvDates);
        adapter = new DateItemAdapter(getContext(), lstDateItemForGridview, getResources(), true);
        grvDates.setAdapter(adapter);
        grvDates.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                DateItemForGridview item = (DateItemForGridview)view.getTag();
                if(item!=null)
                {
                    if(item.getMonth()!=selectedDate.getMonth() || item.getYear()!=selectedDate.getYear())
                    {
                        selectedDate = item;
                        adapter.updateSelectedDate(selectedDate.getDate(), DateTools.GetDateItemsForGridviewFromDate(selectedDate.getDate()));
                    }else {
                        selectedDate = item;
                        adapter.updateSelectedDate(selectedDate.getDate());
                    }
                    showDetailDate();

                }
            }
        });

        Button [] btButtons = {(Button) view.findViewById(R.id.btMonth),
                (Button) view.findViewById(R.id.btYear),
                (Button) view.findViewById(R.id.btNextMonth),
                (Button) view.findViewById(R.id.btBackMonth)};

        for (Button button:btButtons) {
            button.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {
                    btClick(arg0);
                }
            });
        }

        showDetailDate();
    }

    public void btClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btBackMonth:
                selectedDate.addMonth(-1);
                adapter.updateSelectedDate(selectedDate.getDate(), DateTools.GetDateItemsForGridviewFromDate(selectedDate.getDate()));
                showDetailDate();
                break;
            case R.id.btNextMonth:
                selectedDate.addMonth(1);
                adapter.updateSelectedDate(selectedDate.getDate(), DateTools.GetDateItemsForGridviewFromDate(selectedDate.getDate()));
                showDetailDate();
                break;
            case R.id.btMonth:
                callPopupWindowGetMonth();
                break;
            case R.id.btYear:
                callPopupWindowGetYear();
                break;

        }
    }

    private void setMonthAndYear(int month, int year)
    {
        selectedDate.setMonthYear(month, year);
        adapter.updateSelectedDate(selectedDate.getDate(), DateTools.GetDateItemsForGridviewFromDate(selectedDate.getDate()));
        showDetailDate();
    }

    PopupWindow popupWindowGetMonth;
    private void callPopupWindowGetMonth() {

        LayoutInflater layoutInflater = (LayoutInflater) getContext()
                .getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);

        View popupView = layoutInflater.inflate(R.layout.get_month_layout, null);

        popupWindowGetMonth = new PopupWindow(popupView,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                true);
        popupWindowGetMonth.setTouchable(true);
        popupWindowGetMonth.setFocusable(true);

        popupWindowGetMonth.showAtLocation(popupView, Gravity.BOTTOM, 0, 0);

        Button [] btGetMonths = {(Button) popupView.findViewById(R.id.btGetMonth01),
                (Button) popupView.findViewById(R.id.btGetMonth02),
                (Button) popupView.findViewById(R.id.btGetMonth03),
                (Button) popupView.findViewById(R.id.btGetMonth04),
                (Button) popupView.findViewById(R.id.btGetMonth05),
                (Button) popupView.findViewById(R.id.btGetMonth06),
                (Button) popupView.findViewById(R.id.btGetMonth07),
                (Button) popupView.findViewById(R.id.btGetMonth08),
                (Button) popupView.findViewById(R.id.btGetMonth09),
                (Button) popupView.findViewById(R.id.btGetMonth10),
                (Button) popupView.findViewById(R.id.btGetMonth11),
                (Button) popupView.findViewById(R.id.btGetMonth12)};

        btGetMonths[selectedDate.getMonth() - 1].setTextColor(Color.GREEN);
        for (Button button:btGetMonths) {
            button.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {

                    switch (arg0.getId())
                    {
                        case R.id.btGetMonth01:
                            setMonthAndYear(0, selectedDate.getYear());
                            break;
                        case R.id.btGetMonth02:
                            setMonthAndYear(1, selectedDate.getYear());
                            break;
                        case R.id.btGetMonth03:
                            setMonthAndYear(2, selectedDate.getYear());
                            break;
                        case R.id.btGetMonth04:
                            setMonthAndYear(3, selectedDate.getYear());
                            break;
                        case R.id.btGetMonth05:
                            setMonthAndYear(4, selectedDate.getYear());
                            break;
                        case R.id.btGetMonth06:
                            setMonthAndYear(5, selectedDate.getYear());
                            break;
                        case R.id.btGetMonth07:
                            setMonthAndYear(6, selectedDate.getYear());
                            break;
                        case R.id.btGetMonth08:
                            setMonthAndYear(7, selectedDate.getYear());
                            break;
                        case R.id.btGetMonth09:
                            setMonthAndYear(8, selectedDate.getYear());
                            break;
                        case R.id.btGetMonth10:
                            setMonthAndYear(9, selectedDate.getYear());
                            break;
                        case R.id.btGetMonth11:
                            setMonthAndYear(10, selectedDate.getYear());
                            break;
                        case R.id.btGetMonth12:
                            setMonthAndYear(11, selectedDate.getYear());
                            break;
                    }
                    popupWindowGetMonth.dismiss();

                }

            });
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

        if(selectedDate != null){
            int num = selectedDate.getYear();
            npGetYear4.setValue(num%10);
            num = (num/10);
            npGetYear3.setValue(num%10);
            num = (num/10);
            npGetYear2.setValue(num%10);
            num = num/10;
            npGetYear1.setValue(num);
        }
        popupWindowGetYear = new PopupWindow(popupView,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                true);
        popupWindowGetYear.setTouchable(true);
        popupWindowGetYear.setFocusable(true);

        popupWindowGetYear.showAtLocation(popupView, Gravity.BOTTOM, 0, 0);
        ((Button) popupView.findViewById(R.id.btGetYearUpdate)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = npGetYear1.getValue() * 1000 + npGetYear2.getValue() * 100 + npGetYear3.getValue() * 10 + npGetYear4.getValue();
                setMonthAndYear(selectedDate.getMonth() - 1, year);
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

    private void showDetailDate()
    {
        if(selectedDate!=null && !selectedDate.isTitle())
        {
            if(currentPerformServiceProcessBackgroundTask!=null)
            {
                currentPerformServiceProcessBackgroundTask.cancel(true);
                currentPerformServiceProcessBackgroundTask = null;
            }
            currentPerformServiceProcessBackgroundTask = new PerformServiceProcessBackgroundTask();
            currentPerformServiceProcessBackgroundTask.execute(ServiceProcessor.SERVICE_GET_INFO_OF_DATE, selectedDate.getDisplaySolarDate(), selectedDate.getThapNhiBatTu());

            String str = "";
            str += selectedDate.getDayOfWeekInString()+", "+selectedDate.getSolarInfo(false) + " dương lịch";
            str += "\nNhằm "+selectedDate.getLunarInfo(false) + "\n" + selectedDate.getLunarInfo1(false);
            str += selectedDate.isGoodDay()?"\nLà ngày hoàng đạo":( selectedDate.isBadDay()?"\nLà ngày hắc đạo":"");
            str += "\n"+selectedDate.getLunarGoodTime();
            tvGoodDateBadDateInfoDate.setText(str);
            btMonth.setText("Tháng " + selectedDate.getMonth());
            btYear.setText("" + selectedDate.getYear());
        }else{
            wvGoodDateBadDateInfoDate.loadDataWithBaseURL("", "<div style='text-align: center;'>Ngày không hợp lệ</div>", "text/html", "UTF-8", "");
        }
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
