package com.hunght.tinchungkhoan;

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

public class FavoritesHistoryItemAdapter extends ArrayAdapter<PriceItem> {
    private ArrayList<PriceItem> data;
    private static LayoutInflater inflater = null;
    private Context context;

    public FavoritesHistoryItemAdapter(Context a, ArrayList<PriceItem> d) {
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
        TextView tvDate, tvPrice, tvChange, tvKhoiLuong, tvPriceAtOpen, tvHighestPrice, tvLowestPrice;

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.favorites_history_item, null);
        }
       // if(position != 0)
        {
            tvDate = convertView.findViewById(R.id.tvDate);
            tvPrice = convertView.findViewById(R.id.tvPrice);
            tvChange = convertView.findViewById(R.id.tvChange);
            tvKhoiLuong = convertView.findViewById(R.id.tvKhoiLuong);
            tvPriceAtOpen = convertView.findViewById(R.id.tvPriceAtOpen);
            tvHighestPrice = convertView.findViewById(R.id.tvHighestPrice);
            tvLowestPrice = convertView.findViewById(R.id.tvLowestPrice);

            tvDate.setTypeface(Typeface.DEFAULT);
            tvDate.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);

            tvPrice.setTypeface(Typeface.DEFAULT);
            tvPrice.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);

            tvChange.setTypeface(Typeface.DEFAULT);
            tvChange.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);

            tvKhoiLuong.setTypeface(Typeface.DEFAULT);
            tvKhoiLuong.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);

            tvPriceAtOpen.setTypeface(Typeface.DEFAULT);
            tvPriceAtOpen.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);

            tvHighestPrice.setTypeface(Typeface.DEFAULT);
            tvHighestPrice.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);

            tvLowestPrice.setTypeface(Typeface.DEFAULT);
            tvLowestPrice.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);

            PriceItem priceItem = data.get(position);
            convertView.setTag(priceItem);

            tvDate.setText(priceItem.getDate().substring(0, 5));
            tvPrice.setText(priceItem.getPrice());
            tvChange.setText(priceItem.getChange());
            tvKhoiLuong.setText(priceItem.getKhoiLuong());
            tvPriceAtOpen.setText(priceItem.getPriceAtOpen());
            tvHighestPrice.setText(priceItem.getHighestPrice());
            tvLowestPrice.setText(priceItem.getLowestPrice());

            switch (priceItem.getOffSet()) {
                case -1:
                    tvPrice.setTextColor(context.getResources().getColor(R.color.giaAm));
                    tvChange.setTextColor(context.getResources().getColor(R.color.giaAm));
                    break;
                case 0:
                    tvPrice.setTextColor(context.getResources().getColor(R.color.giaZero));
                    tvChange.setTextColor(context.getResources().getColor(R.color.giaZero));
                    break;
                case 1:
                    tvPrice.setTextColor(context.getResources().getColor(R.color.giaDuong));
                    tvChange.setTextColor(context.getResources().getColor(R.color.giaDuong));
                    break;
                default:
                    tvPrice.setTextColor(Color.BLACK);
                    tvChange.setTextColor(Color.BLACK);
                    break;
            }

        }
        return convertView;
    }

}
