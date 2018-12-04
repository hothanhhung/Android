package com.hunght.tinchungkhoan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import java.util.ArrayList;

import com.hunght.data.DoanhNghiepItem;


public class CongTyAutoCompleteAdapter extends ArrayAdapter<DoanhNghiepItem> {
    private ArrayList<DoanhNghiepItem> items;
    private ArrayList<DoanhNghiepItem> itemsAll;
    private ArrayList<DoanhNghiepItem> suggestions;

    public CongTyAutoCompleteAdapter(Context context, ArrayList<DoanhNghiepItem> items) {
        super(context, R.layout.cong_ty_auto_complete_item);
        this.items = items;
        this.itemsAll = (ArrayList<DoanhNghiepItem>) items.clone();
        this.suggestions = new ArrayList<>();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.cong_ty_auto_complete_item, null);
        }
        DoanhNghiepItem doanhNghiepItem = suggestions.get(position);
        if (doanhNghiepItem != null) {
            TextView tvNameCongTy = v.findViewById(R.id.tvNameCongTy);
            if (tvNameCongTy != null) {
                tvNameCongTy.setText(doanhNghiepItem.getFullInfo());
            }
        }
        v.setTag(doanhNghiepItem);
        return v;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((DoanhNghiepItem) (resultValue)).getFullInfo();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (DoanhNghiepItem doanhNghiepItem : itemsAll) {
                    if (doanhNghiepItem.compare(constraint.toString().toLowerCase())) {
                        suggestions.add(doanhNghiepItem);
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
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<DoanhNghiepItem> filteredList = (ArrayList<DoanhNghiepItem>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (DoanhNghiepItem c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };

}