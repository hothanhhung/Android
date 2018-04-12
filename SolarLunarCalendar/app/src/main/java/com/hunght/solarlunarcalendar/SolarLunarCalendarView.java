package com.hunght.solarlunarcalendar;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.hunght.data.DateItemForGridview;
import com.hunght.data.LunarDate;
import com.hunght.utils.DateTools;
import com.hunght.utils.ServiceProcessor;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Lenovo on 4/2/2018.
 */

public class SolarLunarCalendarView extends LinearLayout {
    DateItemForGridview selectedDate;
    DateItemAdapter adapter;
    Button btMonth, btYear;
    TextView tvSpecialDate, tvSolarMonthInfo, tvSolarInfoDate, tvSolarInfoDayInWeek, tvLunarInfoDayInWeek, tvLunarInfoDayInWeek1, tvSolarInfoToday;
    WebView wvSpecialDate;
    ImageView imConGiap;
    PerformServiceProcessBackgroundTask currentPerformServiceProcessBackgroundTask;

    public SolarLunarCalendarView(Context context) {
        super(context);
        initView();
    }

    public SolarLunarCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SolarLunarCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SolarLunarCalendarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {

        selectedDate = new DateItemForGridview("", new Date(), false);
        View view = inflate(getContext(), R.layout.content_main, this);
        btMonth = (Button)view.findViewById(R.id.btMonth);
        btYear = (Button)view.findViewById(R.id.btYear);
        tvSolarMonthInfo = (TextView)view.findViewById(R.id.tvSolarMonthInfo);
        tvSolarInfoDate = (TextView)view.findViewById(R.id.tvSolarInfoDate);
        tvSolarInfoDayInWeek = (TextView)view.findViewById(R.id.tvSolarInfoDayInWeek);
        tvLunarInfoDayInWeek = (TextView)view.findViewById(R.id.tvLunarInfoDayInWeek);
        tvLunarInfoDayInWeek1 = (TextView)view.findViewById(R.id.tvLunarInfoDayInWeek1);
        tvSolarInfoToday = (TextView)view.findViewById(R.id.tvSolarInfoToday);
        tvSpecialDate = (TextView)view.findViewById(R.id.tvSpecialDate);
        wvSpecialDate = (WebView) view.findViewById(R.id.wvSpecialDate);
        imConGiap = (ImageView) view.findViewById(R.id.imConGiap);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ArrayList<DateItemForGridview> lstDateItemForGridview = DateTools.GetDateItemsForGridviewFromDate();

        GridView grvDates = (GridView) findViewById(R.id.grvDates);
        adapter = new DateItemAdapter(getContext(), lstDateItemForGridview, getResources());
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
                    updateMonthYear();

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

        updateMonthYear();
    }

    private void updateMonthYear()
    {
        wvSpecialDate.loadDataWithBaseURL("", "", "text/html", "UTF-8", "");
        if(currentPerformServiceProcessBackgroundTask!=null)
        {
            currentPerformServiceProcessBackgroundTask.cancel(true);
            currentPerformServiceProcessBackgroundTask = null;
        }
        currentPerformServiceProcessBackgroundTask = new PerformServiceProcessBackgroundTask();
        currentPerformServiceProcessBackgroundTask.execute(ServiceProcessor.SERVICE_GET_CHAM_NGON, selectedDate.getDisplaySolarDate(), selectedDate.getDayOfMonth());

        int color;// = getColorForDayOfWeek(selectedDate.getDayOfWeek());
        if(selectedDate.isHoliday())
        {
            color = Color.parseColor("#cc393e");
        }else {
            color = getColorForDayOfWeek(selectedDate.getDayOfWeek());
        }

        tvSolarMonthInfo.setTextColor(color);
        tvSolarInfoDate.setTextColor(color);
        tvSolarInfoDayInWeek.setTextColor(color);
        tvLunarInfoDayInWeek.setTextColor(color);
        tvLunarInfoDayInWeek1.setTextColor(color);
        tvSpecialDate.setTextColor(color);

        btMonth.setText("Tháng " + selectedDate.getMonth());
        btYear.setText("" + selectedDate.getYear());

        tvSolarMonthInfo.setText("Tháng " + selectedDate.getMonth() + " năm " + selectedDate.getYear());
        tvSolarInfoDate.setText(""+selectedDate.getDayOfMonth());
        tvSolarInfoDayInWeek.setText(selectedDate.getDayOfWeekInString());
        tvLunarInfoDayInWeek.setText(selectedDate.getLunarInfo(true));
        tvLunarInfoDayInWeek1.setText(selectedDate.getLunarInfo1(true));
        tvSolarInfoToday.setText(selectedDate.getLunarGoodTime());

        String specialDate = selectedDate.specialDate();
        if(specialDate!=null && specialDate != "")
        {
            tvSpecialDate.setVisibility(VISIBLE);
            tvSpecialDate.setText(specialDate);
        }else{
            tvSpecialDate.setVisibility(GONE);
        }
        //wvSpecialDate.

        int lunarDate = selectedDate.getDateInLunar() % 12;
        switch (lunarDate)
        {
            case LunarDate.TY: imConGiap.setImageResource(R.drawable.ty); break;
            case LunarDate.SUU: imConGiap.setImageResource(R.drawable.suu); break;
            case LunarDate.DAN: imConGiap.setImageResource(R.drawable.dan); break;
            case LunarDate.MAO: imConGiap.setImageResource(R.drawable.meo); break;
            case LunarDate.THIN: imConGiap.setImageResource(R.drawable.thin); break;
            case LunarDate.TI: imConGiap.setImageResource(R.drawable.ti); break;
            case LunarDate.NGO: imConGiap.setImageResource(R.drawable.ngo); break;
            case LunarDate.MUI: imConGiap.setImageResource(R.drawable.mui); break;
            case LunarDate.THAN: imConGiap.setImageResource(R.drawable.than); break;
            case LunarDate.DAU: imConGiap.setImageResource(R.drawable.dau); break;
            case LunarDate.TUAT: imConGiap.setImageResource(R.drawable.tuat); break;
            case LunarDate.HOI: imConGiap.setImageResource(R.drawable.hoi); break;
        }

    }

    private int getColorForDayOfWeek(int dayOfWeek){
        dayOfWeek = dayOfWeek%7;
        switch (dayOfWeek)
        {
            case 0:
                return Color.parseColor("#ba3703");
            case 1:
                return Color.parseColor("#294675");
            case 2:
                return Color.parseColor("#29755f");
            case 3:
                return Color.parseColor("#2e7529");
            case 4:
                return Color.parseColor("#757129");
            case 5:
                return Color.parseColor("#754c29");
            default:
                return Color.DKGRAY;
        }
    }

    public void btClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btBackMonth:
                selectedDate.addMonth(-1);
                adapter.updateSelectedDate(selectedDate.getDate(), DateTools.GetDateItemsForGridviewFromDate(selectedDate.getDate()));
                updateMonthYear();
                break;
            case R.id.btNextMonth:
                selectedDate.addMonth(1);
                adapter.updateSelectedDate(selectedDate.getDate(), DateTools.GetDateItemsForGridviewFromDate(selectedDate.getDate()));
                updateMonthYear();
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
        updateMonthYear();
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

    class PerformServiceProcessBackgroundTask extends AsyncTask< Object, Object, Object >
    {
        private int type;

        protected void onPreExecute()
        {
        }

        protected Object doInBackground(Object... params)
        {
            type = Integer.parseInt(params[0].toString());
            switch (type){
                case ServiceProcessor.SERVICE_GET_CHAM_NGON:
                    return ServiceProcessor.getChamNgon(params[1].toString(),Integer.parseInt(params[2].toString()));

            }
            return null;
        }

        protected void onPostExecute(Object object)
        {
            switch (type){
                case ServiceProcessor.SERVICE_GET_CHAM_NGON:
                    if(object != null) {
                        String str = String.valueOf(object);
                        Log.d("onPostExecute",str);
                        if(str!=null) {
                            wvSpecialDate.loadDataWithBaseURL("", str, "text/html", "UTF-8", "");
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
