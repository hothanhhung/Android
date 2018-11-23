package com.hunght.tinchungkhoan;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hunght.data.HistoryPrice;
import com.hunght.data.PriceItem;

import java.util.ArrayList;

public class HistoryItemAdapter extends ArrayAdapter<HistoryPrice> {
    private ArrayList<HistoryPrice> data;
    private static LayoutInflater inflater = null;
    private Context context;

    public HistoryItemAdapter(Context a, ArrayList<HistoryPrice> d) {
        super(a, R.layout.menu_look_up_item_row);
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
        TextView tvMaCK;

      //  if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.favorite_item, null);
        }
        tvMaCK = (TextView) convertView.findViewById(R.id.tvMaCK);

        HistoryPrice historyPrice = data.get(position);
        convertView.setTag(historyPrice);
        tvMaCK.setText(historyPrice.getMaCK());

        HistoryPrice historyPriceTitle = data.get(0);
        for (PriceItem priceTitle:historyPriceTitle.getPrices()) {
            TextView cloned = (TextView) inflater.inflate(R.layout.favorite_item_price, null);;
            if(position == 0)
            {
                cloned.setText(priceTitle.getDate().substring(0,5));
                cloned.setTextColor(Color.BLACK);
                cloned.setTypeface(Typeface.DEFAULT_BOLD);
            }else{
                PriceItem price = historyPrice.getPriceBasedOnDate(priceTitle.getDate());
                switch (price.getOffSet())
                {
                    case -1 : cloned.setTextColor(context.getResources().getColor(R.color.giaAm)); break;
                    case 0: cloned.setTextColor(context.getResources().getColor(R.color.giaZero)); break;
                    case 1: cloned.setTextColor(context.getResources().getColor(R.color.giaDuong)); break;
                    default: cloned.setTextColor(Color.BLACK); break;
                }
                cloned.setText(price.getPrice());
            }
            ((LinearLayout)convertView).addView(cloned, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1));
        }
        convertView.setTag(historyPrice);
        return convertView;
    }

    public void saveItem(HistoryPrice historyPrice)
    {
        if(historyPrice!=null) {
            if (data == null) {
                data = new ArrayList<>();
            }
            int i =0;
            for(; i < data.size(); i++)
            {
                if(data.get(i).getMaCK().equalsIgnoreCase(historyPrice.getMaCK())){
                    data.remove(i);
                    data.add(i, historyPrice); ;
                    break;
                }
            }
            if(i==data.size())
            {
                data.add(historyPrice);
            }
            notifyDataSetInvalidated();
        }
    }

    public void deleteItem(String maCK)
    {
        if(maCK!=null) {
            if (data != null) {
                for (HistoryPrice item: data) {
                    if(item.getMaCK().equalsIgnoreCase(maCK)){
                        data.remove(item);
                        notifyDataSetInvalidated();
                        break;
                    }
                }
            }
        }
    }
}
