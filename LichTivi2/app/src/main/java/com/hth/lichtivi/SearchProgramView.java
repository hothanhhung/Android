package com.hth.lichtivi;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
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

import com.hth.utils.MethodsHelper;
import com.hth.utils.ParseJSONScheduleItems;
import com.hth.utils.SearchProgramItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Lenovo on 8/31/2016.
 */
public class SearchProgramView extends LinearLayout {
    Context context;
    private ParseJSONScheduleItems parseJSONScheduleItems;
    private SearchProgrameAsyncTask searchProgrameAsyncTask;
    EditText etSearchProgramQuery;
    Button btSearchProgram;
    Spinner spStation;
    EditText etDate;
    ListView lvPrograms;
    Date selectedDate;
    TextView tvMessage;

    public SearchProgramView(Context context) {
        super(context);
        initView();
    }

    public SearchProgramView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public SearchProgramView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initView();
    }

    private void showDate(Date date) {
        etDate.setText(MethodsHelper.getStringFromDate(date));
    }

    DatePickerDialog datePickerDialog;
    private DatePickerDialog buildDatePickerDialog()
    {
        datePickerDialog  = new DatePickerDialog(getContext(), null, selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDate());

        datePickerDialog.setCancelable(true);
        datePickerDialog.setCanceledOnTouchOutside(false);
        datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "Chọn ngày",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatePicker dp = datePickerDialog.getDatePicker();
                        datePickerDialog.dismiss();
                        if (selectedDate.getDate() != dp.getDayOfMonth() || selectedDate.getMonth() != dp.getMonth() || selectedDate.getYear() != dp.getYear()) {
                            selectedDate.setYear(dp.getYear());
                            selectedDate.setMonth(dp.getMonth());
                            selectedDate.setDate(dp.getDayOfMonth());
                            showDate(selectedDate);
                        }
                    }
                });
        datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Bỏ qua",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        datePickerDialog.dismiss();
                    }
                });
        return datePickerDialog;
    }
    public void setDate() {
        if(datePickerDialog == null)
        {
            datePickerDialog = buildDatePickerDialog();
        }
        datePickerDialog.updateDate(selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDate());
        datePickerDialog.show();
    }
    private void initView()
    {
        parseJSONScheduleItems = new ParseJSONScheduleItems(context);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        selectedDate = new Date(year, month, day);

        inflate(getContext(), R.layout.search_layout, this);
        etSearchProgramQuery = (EditText) this.findViewById(R.id.etSearchProgramQuery);
        btSearchProgram = (Button) this.findViewById(R.id.btSearchProgram);
        btSearchProgram.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MethodsHelper.hideSoftKeyboard( (Activity) context);
                searchProgram();
            }
        });
        spStation = (Spinner) this.findViewById(R.id.spStation);
        etDate = (EditText) this.findViewById(R.id.etDate);
        etDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
            }
        });
        lvPrograms = (ListView) this.findViewById(R.id.lvPrograms);
        tvMessage = (TextView) this.findViewById(R.id.tvMessage);
        initStation();
        showDate(selectedDate);
    }

    private void initStation()
    {
        String[] arraySpinner = new String[] {
                "Đài Truyền hình Việt Nam",
                "Đài Tiếng nói Việt Nam",
                "Đài Phát thanh và Truyền hình Hà Nội",
                "Đài Truyền hình TP Hồ Chí Minh - HTV",
                "Đài truyền hình An Viên AVG",
                "Đài Truyền hình Cáp TP Hồ Chí Minh - HTVC",
                "Đài Truyền hình Cáp Việt Nam",
                "Đài truyền hình cáp Hà Nội",
                "Đài truyền hình cáp Sài Gòn",
                "Đài truyền hình số VTC",
                "Đài TH địa phương",
                "Kênh TH quốc tế",
                "Truyền hình số vệ tinh k+",
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, arraySpinner);
        spStation.setAdapter(adapter);
    }

    public void searchProgram()
    {
        tvMessage.setVisibility(View.VISIBLE);
        tvMessage.setText("Đang tải dữ liệu...");
        lvPrograms.setVisibility(View.GONE);
        if(searchProgrameAsyncTask!=null)
        {
            searchProgrameAsyncTask.cancel(true);
        }
        searchProgrameAsyncTask = new SearchProgrameAsyncTask();
        searchProgrameAsyncTask.execute(etSearchProgramQuery.getText().toString(), ""+(spStation.getSelectedItemPosition() + 11));
    }

    private class SearchProgrameAsyncTask extends AsyncTask<String, Void, ArrayList<SearchProgramItem>> {
        @Override
        protected ArrayList<SearchProgramItem> doInBackground(String... data) {
            return parseJSONScheduleItems.searchProgram(data[0], data[1], selectedDate);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(ArrayList<SearchProgramItem> searchProgramItems) {
            if(searchProgramItems == null)
            {
                tvMessage.setVisibility(View.VISIBLE);
                tvMessage.setText("Lỗi khi tải dữ liệu");
            }else if(searchProgramItems.size() == 0){
                tvMessage.setVisibility(View.VISIBLE);
                tvMessage.setText("Chưa có dữ liệu");
            }else {
                lvPrograms.setVisibility(View.VISIBLE);
                SearchProgramRowAdapter adapter = new SearchProgramRowAdapter(context, searchProgramItems);
                lvPrograms.setAdapter(adapter);
                tvMessage.setVisibility(View.GONE);
            }
        }
    }
}
