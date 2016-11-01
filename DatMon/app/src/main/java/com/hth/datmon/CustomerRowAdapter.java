package com.hth.datmon;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hth.service.Customer;

import java.util.ArrayList;

public class CustomerRowAdapter extends ArrayAdapter<Customer> {
    private ArrayList<Customer> data, originalData;
    private static LayoutInflater inflater = null;
    public Resources res;
    private Activity context;

    public CustomerRowAdapter(Activity a, ArrayList<Customer> d, Resources resLocal) {
        super(a, R.layout.customer_row, R.id.title, d);
        context = a;
        this.originalData = new ArrayList<>();
        if(d!=null) this.originalData.addAll(d);
        this.data = new ArrayList<>();
        if(d!=null) this.data.addAll(d);

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
        ImageButton btAddCustomer;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.customer_row, null);
        }
        tvFullname = (TextView) convertView.findViewById(R.id.tvFullname);
        tvPhoneNumber = (TextView) convertView.findViewById(R.id.tvPhoneNumber);
        tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
        //btAddCustomer = (ImageButton) convertView.findViewById(R.id.btAddCustomer);

       /* btAddCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Customer customer = (Customer) v.getTag();
                data.remove(customer);
                notifyDataSetChanged();
            }
        });*/

        Customer customer = data.get(position);
        convertView.setTag(customer);

        // Setting all values in listview
        tvFullname.setText(customer.getFirstName());
        tvPhoneNumber.setText(customer.getPhoneNumber());
        tvAddress.setText(customer.getAddress());

        if(position % 2 == 0) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.ordered_item_odd_color));
        }else {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.ordered_item_even_color));
        }
        return convertView;
    }

    public void filterData(String query) {

        query = query.toLowerCase();
        data.clear();
        if (query.isEmpty()) {
            data.addAll(originalData);
        } else {

            String noToneQuery = MethodsHelper.stripAccentsAndD(query);
            for (Customer customer : originalData) {

                if (customer.getEnglishName().contains(noToneQuery)) {
                    data.add(customer);
                }
            }
        }
        notifyDataSetChanged();

    }
}
