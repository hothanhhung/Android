package com.hth.eyetest;

import java.util.ArrayList;
import com.hth.data.Item;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class GameContentGridViewAdapter extends ArrayAdapter<Item> {
    private ArrayList<Item> data;
    private static LayoutInflater inflater = null;
    public Resources res;
    private Activity context;

    public GameContentGridViewAdapter(Activity a, ArrayList<Item> d, Resources resLocal) {
        super(a, R.layout.game_item_of_gridview);
        context = a;
        this.data = d;

        res = resLocal;
        inflater = LayoutInflater.from(a);
    }

    public int getCount() {
        return data.size();
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tvGameItem;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.game_item_of_gridview, null);
        }
        tvGameItem = (TextView) convertView.findViewById(R.id.tvGameItem);
        Item item = data.get(position);
        convertView.setTag(item);
        if(item.isColor())
        {
            tvGameItem.setText("");
            tvGameItem.setBackgroundColor(item.getColor());
        }else{
            tvGameItem.setText(item.getContent());
            tvGameItem.setBackgroundColor(Color.TRANSPARENT);
            //convertView.setBackgroundColor(Color.GRAY);
        }
        return convertView;
    }

    public void updateData(ArrayList<Item> d) {

        data = d;
        notifyDataSetChanged();

    }
}
