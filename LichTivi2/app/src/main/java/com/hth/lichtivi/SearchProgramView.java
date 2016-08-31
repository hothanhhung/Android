package com.hth.lichtivi;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

/**
 * Created by Lenovo on 8/31/2016.
 */
public class SearchProgramView extends LinearLayout {
    EditText etSearchProgramQuery;
    Button btSearchProgram;
    Spinner spStation;
    EditText etDate;
    ListView lvPrograms;

    public SearchProgramView(Context context) {
        super(context);
        initView();
    }

    public SearchProgramView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SearchProgramView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView()
    {
        inflate(getContext(), R.layout.search_layout, this);
        etSearchProgramQuery = (EditText) this.findViewById(R.id.etSearchProgramQuery);
        btSearchProgram = (Button) this.findViewById(R.id.btSearchProgram);
        spStation = (Spinner) this.findViewById(R.id.spStation);
        etDate = (EditText) this.findViewById(R.id.etDate);
        lvPrograms = (ListView) this.findViewById(R.id.lvPrograms);
        initStation();
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arraySpinner);
        spStation.setAdapter(adapter);
    }


}
