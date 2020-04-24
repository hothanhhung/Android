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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hunght.data.DateItemForGridview;
import com.hunght.data.NoteItem;
import com.hunght.utils.SharedPreferencesUtils;

import java.util.Calendar;

/**
 * Created by Lenovo on 4/21/2018.
 */

public class SaveNoteItemView  extends LinearLayout {
    static NoteItem noteItem;
    int date, month, year;
    NumberPicker npSaveNoteItemDate, npSaveNoteItemMonth, npSaveNoteItemYear, npSaveNoteItemRemindType;
    Button btSaveNoteItemUpdate, btSaveNoteItemCancel;
    EditText etSaveNoteItemSubject, etSaveNoteItemContent;
    TextView tvSaveNoteItemInfoDate;

    public SaveNoteItemView(Context context) {
        super(context);
        initView();
    }

    public SaveNoteItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SaveNoteItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SaveNoteItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {

        Calendar today = Calendar.getInstance();
        View view = inflate(getContext(), R.layout.save_note_item_view, this);

        tvSaveNoteItemInfoDate = (TextView) view.findViewById(R.id.tvSaveNoteItemInfoDate);

        npSaveNoteItemDate = (NumberPicker) view.findViewById(R.id.npSaveNoteItemDate);
        npSaveNoteItemMonth = (NumberPicker) view.findViewById(R.id.npSaveNoteItemMonth);
        npSaveNoteItemYear = (NumberPicker) view.findViewById(R.id.npSaveNoteItemYear);
        npSaveNoteItemRemindType = (NumberPicker) view.findViewById(R.id.npSaveNoteItemRemindType);

        etSaveNoteItemSubject = (EditText) view.findViewById(R.id.etSaveNoteItemSubject);
        etSaveNoteItemContent = (EditText) view.findViewById(R.id.etSaveNoteItemContent);

        btSaveNoteItemCancel = (Button) view.findViewById(R.id.btSaveNoteItemCancel);
        btSaveNoteItemUpdate = (Button) view.findViewById(R.id.btSaveNoteItemUpdate);

        npSaveNoteItemRemindType.setDisplayedValues( new String[] { "Không Nhắc Lại", "Nhắc Sau 1 Tháng Dương",
                "Nhắc Sau 1 Tháng Âm", "Nhắc Sau 1 Năm Dương", "Nhắc Sau 1 Năm Âm"} );


        npSaveNoteItemMonth.setDisplayedValues( new String[] { "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4",
                "Tháng 5", "Tháng 6", "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12",} );


        npSaveNoteItemRemindType.setMinValue(0);
        npSaveNoteItemRemindType.setMaxValue(4);

        if(noteItem == null)
        {
            noteItem = new NoteItem();
        }
        if(!noteItem.haveDate()) {
            date = today.get(Calendar.DAY_OF_MONTH);
            month = today.get(Calendar.MONTH);
            year = today.get(Calendar.YEAR);
        }else{
            date = noteItem.getDateItem().getDayOfMonth();
            month = noteItem.getDateItem().getMonth() - 1;
            year = noteItem.getDateItem().getYear();
            etSaveNoteItemContent.setText(noteItem.getContent());
            etSaveNoteItemSubject.setText(noteItem.getSubject());
            npSaveNoteItemRemindType.setValue(noteItem.getRemindType());
        }


        npSaveNoteItemDate.setMinValue(1);
        npSaveNoteItemDate.setMaxValue(31);
        npSaveNoteItemMonth.setMinValue(0);
        npSaveNoteItemMonth.setMaxValue(11);
        npSaveNoteItemYear.setMinValue(1);
        npSaveNoteItemYear.setMaxValue(Integer.MAX_VALUE);
        npSaveNoteItemDate.setValue(date);
        npSaveNoteItemMonth.setValue(month);
        npSaveNoteItemYear.setValue(year);
        npSaveNoteItemYear.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                callPopupWindowGetYear();
            }
        });

        npSaveNoteItemDate.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                validateDate();
            }
        });

        npSaveNoteItemMonth.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                validateDate();
            }
        });
        npSaveNoteItemYear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                validateDate();
            }
        });

        btSaveNoteItemCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                ViewGroup parent = (ViewGroup) SaveNoteItemView.this.getParent();
                parent.removeAllViews();
                parent.addView(new NotesView(getContext()), 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

            }
        });

        btSaveNoteItemUpdate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

                noteItem.updateDate(npSaveNoteItemDate.getValue(), npSaveNoteItemMonth.getValue() + 1, npSaveNoteItemYear.getValue());
                noteItem.setSubjectAndContent(etSaveNoteItemSubject.getText().toString(), etSaveNoteItemContent.getText().toString());
                noteItem.setRemindType(npSaveNoteItemRemindType.getValue());
                SharedPreferencesUtils.updateNoteItems(getContext(), noteItem);

                ViewGroup parent = (ViewGroup) SaveNoteItemView.this.getParent();
                parent.removeAllViews();
                parent.addView(new NotesView(getContext()), 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

            }
        });

        validateDate();
    }

    private void validateDate() {
        int m = npSaveNoteItemMonth.getValue() + 1;
        int y = npSaveNoteItemYear.getValue();


        if (m == 2) {
            if ((y % 400 == 0) || (y % 4 == 0 && y % 100 == 0)) {
                int d = npSaveNoteItemDate.getValue();
                npSaveNoteItemDate.setMaxValue(29);
                if (d > 29) npSaveNoteItemDate.setValue(29);
            } else {
                int d = npSaveNoteItemDate.getValue();
                npSaveNoteItemDate.setMaxValue(28);
                if (d > 28) npSaveNoteItemDate.setValue(28);
            }
        } else if (m == 1 || m == 3 || m == 5 || m == 7 || m == 8 || m == 10 || m == 12) {
            npSaveNoteItemDate.setMaxValue(31);
        } else {
            int d = npSaveNoteItemDate.getValue();
            npSaveNoteItemDate.setMaxValue(30);
            if (d > 30) npSaveNoteItemDate.setValue(30);
        }

        showDetailDate(DateItemForGridview.createDateItemForGridview(npSaveNoteItemDate.getValue(), m, y, false, false));
    }

    private void showDetailDate(DateItemForGridview exchangedDate)
    {
        if(exchangedDate!=null && !exchangedDate.isTitle())
        {
            String str = "";
            str += exchangedDate.getDayOfWeekInString()+", "+exchangedDate.getSolarInfo(false) + " dương lịch";
            str += "\nNhằm "+exchangedDate.getLunarInfo(false) + "\n" + exchangedDate.getLunarInfo1(false);
            tvSaveNoteItemInfoDate.setText(str);
        }else{
            tvSaveNoteItemInfoDate.setText("Ngày không hợp lệ");
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
                npSaveNoteItemYear.setValue(year);
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

    public static void setNoteItem(NoteItem obj)
    {
        noteItem = obj;
    }
}
