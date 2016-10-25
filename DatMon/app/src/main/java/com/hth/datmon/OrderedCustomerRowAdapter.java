package com.hth.datmon;

import android.app.Activity;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hth.data.Customer;
import com.hth.data.OrderedItem;

import java.util.ArrayList;

public class OrderedCustomerRowAdapter extends ArrayAdapter<Customer> {
    private ArrayList<Customer> data;
    private static LayoutInflater inflater = null;
    public Resources res;
    private Activity context;

    public OrderedCustomerRowAdapter(Activity a, ArrayList<Customer> d, Resources resLocal) {
        super(a, R.layout.ordered_customer_row, R.id.title, d);
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
        TextView tvFullname;
        TextView tvPhoneNumber;
        TextView tvAddress;
        ImageButton btDelete;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.ordered_customer_row, null);
        }
        tvFullname = (TextView) convertView.findViewById(R.id.tvFullname);
        tvPhoneNumber = (TextView) convertView.findViewById(R.id.tvPhoneNumber);
        tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
        btDelete = (ImageButton) convertView.findViewById(R.id.btDelete);

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Customer customer = (Customer) v.getTag();
                data.remove(customer);
                notifyDataSetChanged();
            }
        });

        Customer customer = data.get(position);
        btDelete.setTag(customer);

        // Setting all values in listview
        tvFullname.setText(customer.getName());
        tvPhoneNumber.setText(customer.getPhoneNumber());
        tvAddress.setText(customer.getAddress());

        if(position % 2 == 0) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.ordered_item_odd_color));
        }else {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.ordered_item_even_color));
        }
        return convertView;
    }

    public void addData(Customer customer) {

        data.add(customer);
        notifyDataSetChanged();

    }
}
