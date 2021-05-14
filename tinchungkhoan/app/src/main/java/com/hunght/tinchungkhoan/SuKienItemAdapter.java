package com.hunght.tinchungkhoan;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hunght.data.PriceItem;
import com.hunght.data.SuKienItem;

import java.util.ArrayList;

public class SuKienItemAdapter extends ArrayAdapter<SuKienItem> {
    private ArrayList<SuKienItem> data;
    private static LayoutInflater inflater = null;
    private Context context;

    public SuKienItemAdapter(Context a, ArrayList<SuKienItem> d) {
        super(a, R.layout.su_kien_item_row);
        context = a;
        this.data = new ArrayList<>();
        if(d!=null) this.data.addAll(d);
        inflater = LayoutInflater.from(a);
    }

    public int getCount() {
        return data.size();
    }

    public Object onRetainNonConfigurationInstance() {
        return data;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tvDate, tvCode, tvType, tvDesc;

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.su_kien_item_row, null);
        }
       // if(position != 0)
        {
            tvDate = convertView.findViewById(R.id.tvDate);
            tvCode = convertView.findViewById(R.id.tvCode);
            tvType = convertView.findViewById(R.id.tvType);
            tvDesc = convertView.findViewById(R.id.tvDesc);

            tvDate.setTypeface(Typeface.DEFAULT);
            tvDate.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);

            tvCode.setTypeface(Typeface.DEFAULT);
            tvCode.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);

            tvType.setTypeface(Typeface.DEFAULT);
            tvType.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);

            tvDesc.setTypeface(Typeface.DEFAULT);
            tvDesc.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);

            SuKienItem item = data.get(position);
            convertView.setTag(item);

            tvCode.setText(item.code);
            tvDate.setText(item.effectiveDate);
            tvType.setText(item.typeDesc);
            tvDesc.setText(item.note);

            if(position % 2 == 0) {
                convertView.setBackgroundColor(convertView.getResources().getColor(R.color.item_odd_color));
            }else {
                convertView.setBackgroundColor(convertView.getResources().getColor(R.color.item_even_color));
            }

        }
        return convertView;
    }

}
