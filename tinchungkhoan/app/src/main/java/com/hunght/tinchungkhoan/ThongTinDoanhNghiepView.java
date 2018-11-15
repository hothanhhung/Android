package com.hunght.tinchungkhoan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.hunght.data.HistoryPrice;
import com.hunght.data.PriceItem;
import com.hunght.utils.MethodsHelper;
import com.hunght.utils.ParserData;
import com.hunght.utils.SavedValues;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * TODO: document your custom view class.
 */
public class ThongTinDoanhNghiepView extends LinearLayout {
    EditText etMaCK;
    SavedValues savedValues;
    public ThongTinDoanhNghiepView(Context context) {
        super(context);
        init(null, 0);
    }

    public ThongTinDoanhNghiepView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ThongTinDoanhNghiepView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final View view = inflate(getContext(), R.layout.thong_tin_doanh_nghiep_view, this);
        savedValues = new SavedValues(getContext());

        etMaCK = view.findViewById(R.id.etMaCK);

        Button btTraCuu = view.findViewById(R.id.btTraCuu);
        btTraCuu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String mack = etMaCK.getText().toString();
                if(mack!=null && !mack.trim().isEmpty())
                {

                }
            }
        });
    }

    private class DownloadContentTask extends AsyncTask<String, Integer, HistoryPrice> {
        protected HistoryPrice doInBackground(String... maCK) {
            return ParserData.getHistoryPrice(maCK[0]);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(HistoryPrice historyPrice) {
        }
    }
}
