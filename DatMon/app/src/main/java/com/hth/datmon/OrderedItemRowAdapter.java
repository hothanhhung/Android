package com.hth.datmon;

import android.app.Activity;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.hth.data.OrderItem;
import com.hth.data.OrderedItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderedItemRowAdapter extends ArrayAdapter<OrderedItem> {
    private ArrayList<OrderedItem> data;
    private static LayoutInflater inflater = null;
    public Resources res;
    private Activity context;

    public OrderedItemRowAdapter(Activity a, ArrayList<OrderedItem> d, Resources resLocal) {
        super(a, R.layout.ordered_item_row, R.id.title, d);
        context = a;
        this.data = d;
       // if(d!=null) this.data.addAll(d);

        res = resLocal;
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
        EditText etQuantity;
        TextView tvTotal;
        TextView tvDetail;
        ImageButton btRemove;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.ordered_item_row, null);
        }
        etQuantity = (EditText) convertView.findViewById(R.id.etQuantity);
        tvTotal = (TextView) convertView.findViewById(R.id.tvTotal);
        tvDetail = (TextView) convertView.findViewById(R.id.tvDetail);
        btRemove = (ImageButton) convertView.findViewById(R.id.btRemove);

        btRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderedItem orderedItem = (OrderedItem)v.getTag();
                data.remove(orderedItem);
                notifyDataSetChanged();
            }
        });

        etQuantity.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0)
                {

                }
            }
        });
        OrderedItem orderedItem = data.get(position);
        btRemove.setTag(orderedItem);

        // Setting all values in listview
        etQuantity.setText(""+orderedItem.getQuantity());
        tvTotal.setText(""+orderedItem.getTotal());
        tvDetail.setText(orderedItem.getDetail());

        if(position % 2 == 0) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.ordered_item_odd_color));
        }else {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.ordered_item_even_color));
        }
        return convertView;
    }

    public void addData(OrderedItem orderedItem) {

        data.add(orderedItem);
        notifyDataSetChanged();

    }
}
