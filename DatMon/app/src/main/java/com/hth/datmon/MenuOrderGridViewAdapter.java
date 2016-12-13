package com.hth.datmon;

import java.util.ArrayList;

import com.hth.service.MenuOrder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuOrderGridViewAdapter extends ArrayAdapter<MenuOrder> {
    private ArrayList<MenuOrder> originalData;
    private ArrayList<MenuOrder> data;
    private static LayoutInflater inflater = null;
    public Resources res;
    private Activity context;

    public MenuOrderGridViewAdapter(Activity a, ArrayList<MenuOrder> d, Resources resLocal) {
        super(a, R.layout.order_item_gridview);
        context = a;
        this.data = new ArrayList<MenuOrder>();
        this.data.addAll(d);
        this.originalData = new ArrayList<MenuOrder>();
        this.originalData.addAll(d);

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
        TextView tvPrice;
        TextView tvOutOfStock;
        TextView tbDetail;
        final ImageView imgImageView;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.order_item_gridview, null);
        }
        tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
        tvOutOfStock = (TextView) convertView.findViewById(R.id.tvOutOfStock);
        tbDetail = (TextView) convertView.findViewById(R.id.tbDetail);
        imgImageView = (ImageView) convertView.findViewById(R.id.imgImageView);

        MenuOrder orderItem = data.get(position);
        convertView.setTag(orderItem);

        // Setting all values in listview
        tvPrice.setText(String.format("%,.0f", orderItem.getPrice()));
        if (orderItem.isOutOfStock()) {
            tvOutOfStock.setVisibility(View.VISIBLE);
        } else {
            tvOutOfStock.setVisibility(View.INVISIBLE);
        }
        tbDetail.setText(orderItem.getName());
        if(orderItem.hasImage()) {
            Picasso.with(context)
                    .load(orderItem.getPathImage())
                    .into(imgImageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(context)
                                    .load(R.drawable.order_item1)
                                    .into(imgImageView);
                        }
                    });
        }else{
            Picasso.with(context)
                    .load(R.drawable.order_item1)
                    .into(imgImageView);
        }
        return convertView;
    }

    public void filterData(String query, int type) {

        query = query.toLowerCase();
        data.clear();
        if (query.isEmpty() && type == 0) {
            data.addAll(originalData);
        } else {

            String noToneQuery = MethodsHelper.stripAccentsAndD(query);
            for (MenuOrder orderItem : originalData) {

               if ((query.isEmpty() || orderItem.getEnglishDetail().contains(noToneQuery)) && (type == 0 || orderItem.getType() == type)) {
                   data.add(orderItem);
                }
            }
        }
        notifyDataSetChanged();

    }
}
