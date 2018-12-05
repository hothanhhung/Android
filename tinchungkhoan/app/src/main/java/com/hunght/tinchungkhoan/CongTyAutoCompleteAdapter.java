package com.hunght.tinchungkhoan;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.hunght.data.DoanhNghiepItem;

import java.util.ArrayList;
import java.util.List;

public class CongTyAutoCompleteAdapter extends ArrayAdapter<DoanhNghiepItem> {

    private int maxReturnItems = 5;
    private Context context;
    private int resourceId;
    private List<DoanhNghiepItem> items, tempItems, suggestions;

    public CongTyAutoCompleteAdapter(@NonNull Context context, int resourceId, int maxReturnItems, ArrayList<DoanhNghiepItem> items) {
        super(context, resourceId, items);
        this.items = items;;
        this.context = context;
        this.resourceId = resourceId;
        tempItems = new ArrayList<>(this.items);
        suggestions = new ArrayList<>();
        this.maxReturnItems= maxReturnItems;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                view = inflater.inflate(resourceId, parent, false);
            }
            DoanhNghiepItem doanhNghiepItem = getItem(position);
            TextView name = view.findViewById(R.id.tvNameCongTy);
            name.setText(doanhNghiepItem.getFullInfo());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                name.setText(Html.fromHtml(doanhNghiepItem.getFullInfo(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                name.setText(Html.fromHtml(doanhNghiepItem.getFullInfo()));
            }
            view.setTag(doanhNghiepItem);
            if(position % 2 == 0) {
                view.setBackgroundColor(context.getResources().getColor(R.color.item_odd_color));
            }else {
                view.setBackgroundColor(context.getResources().getColor(R.color.item_even_color));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Nullable
    @Override
    public DoanhNghiepItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return fruitFilter;
    }

    private Filter fruitFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            DoanhNghiepItem fruit = (DoanhNghiepItem) resultValue;
            return fruit.getFullInfo();
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            if (charSequence != null) {
                suggestions.clear();
                ArrayList<DoanhNghiepItem> suggestions1 = new ArrayList<>();
                ArrayList<DoanhNghiepItem> suggestions2 = new ArrayList<>();
                for (DoanhNghiepItem doanhNghiepItem: tempItems) {
                    if (doanhNghiepItem.compare1(charSequence.toString())) {
                        suggestions1.add(doanhNghiepItem);
                    }
                    if (doanhNghiepItem.compare2(charSequence.toString())) {
                        suggestions2.add(doanhNghiepItem);
                    }
                    if(suggestions1.size() == maxReturnItems) break;
                }

                if(suggestions1.size() > 0 || suggestions2.size() > 0){
                    suggestions.addAll(suggestions1);
                    if(suggestions.size() < maxReturnItems){
                        for(int i = 0; i < suggestions2.size() && suggestions.size() < maxReturnItems; i++)
                        suggestions.add(suggestions2.get(i));
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            ArrayList<DoanhNghiepItem> tempValues = (ArrayList<DoanhNghiepItem>) filterResults.values;
            if (filterResults != null && filterResults.count > 0) {
                clear();
                for (DoanhNghiepItem doanhNghiepItem : tempValues) {
                    add(doanhNghiepItem);
                    notifyDataSetChanged();
                }
            } else {
                clear();
                notifyDataSetChanged();
            }
        }
    };
}