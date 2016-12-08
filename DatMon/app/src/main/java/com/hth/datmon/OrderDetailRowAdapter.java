package com.hth.datmon;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hth.service.OrderDetail;

import java.util.ArrayList;

import static android.view.View.GONE;

public class OrderDetailRowAdapter extends ArrayAdapter<OrderDetail> implements ICallBack{
    private ArrayList<OrderDetail> data;
    private static LayoutInflater inflater = null;
    public Resources res;
    private Activity context;
    private ICallBack callBack;

    public OrderDetailRowAdapter(Activity a, ArrayList<OrderDetail> d, Resources resLocal, ICallBack callBack) {
        super(a, R.layout.ordered_item_row, R.id.title, d);
        context = a;
        this.callBack = callBack;
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
                OrderDetail orderedItem = (OrderDetail) v.getTag();
                data.remove(orderedItem);
                notifyDataSetChanged();
                callBack.onOrderItemGridViewChange();
            }
        });

        etQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderDetail orderedItem = (OrderDetail) v.getTag();
                UIUtils.showNumberPickerDialog(context, orderedItem, OrderDetailRowAdapter.this);
            }
        });

        OrderDetail orderedItem = data.get(position);
        btRemove.setTag(orderedItem);
        etQuantity.setTag(orderedItem);

        if(orderedItem.isPromotion()){
            btRemove.setVisibility(GONE);
        }else{
            btRemove.setVisibility(View.VISIBLE);
        }
        // Setting all values in listview
        etQuantity.setText("" + orderedItem.getQuantity());
        tvTotal.setText(String.format("%,.0f", orderedItem.getTotal()));
        tvDetail.setText(orderedItem.getName());

        if(position % 2 == 0) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.ordered_item_odd_color));
        }else {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.ordered_item_even_color));
        }
        return convertView;
    }

    public void addData(OrderDetail orderedItem) {

        data.add(orderedItem);
        notifyDataSetChanged();
        callBack.onOrderItemGridViewChange();

    }

    public float getTotal()
    {
        float total = 0;
        for (OrderDetail orderedItem:data  ) {
            total += orderedItem.getTotal();
        }
        return total;
    }

    @Override
    public void onNumberPikerDialogSave() {
        notifyDataSetChanged();
        callBack.onOrderItemGridViewChange();
    }

    @Override
    public void onOrderItemGridViewChange() {

    }
}
