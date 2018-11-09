package com.hunght.tinchungkhoan;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.hunght.data.MenuLookUpItem;

import java.util.ArrayList;

public class MenuLookUpItemAdapter extends ArrayAdapter<MenuLookUpItem> {
    private ArrayList<MenuLookUpItem> data;
    private static LayoutInflater inflater = null;
    private Activity context;

    public MenuLookUpItemAdapter(Activity a, ArrayList<MenuLookUpItem> d) {
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
        TextView tvName;
        TextView tvDetail;
        ImageView ivIcon;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.menu_look_up_item_row, null);
        }
        tvName = (TextView) convertView.findViewById(R.id.tvName);
        tvDetail = (TextView) convertView.findViewById(R.id.tvDetail);
        ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);

        MenuLookUpItem menuLookUpItem = data.get(position);
        convertView.setTag(menuLookUpItem);
        // Setting all values in listview
        tvName.setText(menuLookUpItem.getName());
        if(menuLookUpItem.hasDetail()){
            tvDetail.setText(menuLookUpItem.getDetail());
            tvDetail.setVisibility(View.VISIBLE);
        }else {
            tvDetail.setVisibility(View.GONE);
        }

        if(menuLookUpItem.hasIcon()){
            ivIcon.setImageResource(menuLookUpItem.getDrawableIcon());
            ivIcon.setVisibility(View.VISIBLE);
        }else {
            ivIcon.setVisibility(View.INVISIBLE);
        }

        if(position % 2 == 0) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.item_odd_color));
        }else {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.item_even_color));
        }
        return convertView;
    }

    public void filterData(String query) {

        /*query = query.toLowerCase();
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
        notifyDataSetChanged();*/

    }
}
