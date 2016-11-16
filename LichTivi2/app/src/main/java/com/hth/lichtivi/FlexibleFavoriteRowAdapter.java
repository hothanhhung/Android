package com.hth.lichtivi;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hth.utils.MethodsHelper;

import java.util.ArrayList;

public class FlexibleFavoriteRowAdapter extends ArrayAdapter<ArrayList<ChannelItem>> {
	private Context activity;
    private ArrayList<ChannelItem> data;
    private static LayoutInflater inflater=null;

    public FlexibleFavoriteRowAdapter(Context a, ArrayList<ChannelItem> d) {
        super( a, R.layout.favorite_row);
        activity = a;
        if(d == null) data = new ArrayList<ChannelItem>();
        else data=d;
        inflater = LayoutInflater.from(a) ;
    }
 
    public int getCount() {
        return data.size();
    }

    public Object onRetainNonConfigurationInstance() {
        return data ;
    }
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        FavoriteViewHolder viewHolder;
        TextView tvName;
        TextView tvNote;
        ImageButton btEdit;
        ImageButton btUnFavorite;
        LinearLayout llRow;

    	if(convertView==null)
        {
    		convertView = inflater.inflate(R.layout.favorite_row, null);
        	viewHolder = new FavoriteViewHolder();

            //viewHolder.llRow = llRow = (LinearLayout)convertView.findViewById(R.id.llRow); // title
        	viewHolder.tvName = tvName = (TextView)convertView.findViewById(R.id.tvName); // title
            viewHolder.tvNote = tvNote = (TextView)convertView.findViewById(R.id.tvNote); // title
            viewHolder.btEdit = btEdit = (ImageButton)convertView.findViewById(R.id.btEdit);
        	viewHolder.btUnFavorite = btUnFavorite = (ImageButton)convertView.findViewById(R.id.btUnFavorite);
        	convertView.setTag(viewHolder);
        }
    	else{
            viewHolder = (FavoriteViewHolder)convertView.getTag();
            llRow = viewHolder.llRow;
            tvName = viewHolder.tvName; // title
            tvNote = viewHolder.tvNote; // title
            btEdit = viewHolder.btEdit;
            btUnFavorite = viewHolder.btUnFavorite;
        }
        ChannelItem channelItem = data.get(position);
        btUnFavorite.setTag(channelItem);
        btEdit.setTag(channelItem);

        // Setting all values in listview
        if(position % 2 == 0) {
            convertView.setBackgroundColor(activity.getResources().getColor(R.color.group_item_odd_color));
        }else {
            convertView.setBackgroundColor(activity.getResources().getColor(R.color.group_item_even_color));
        }

        tvName.setText(channelItem.getName());
        if(channelItem.getNote() == null || channelItem.getNote() == "")
        {
            tvNote.setText("Chưa có ghi chú");
            tvNote.setTypeface(tvNote.getTypeface(), Typeface.ITALIC);
            tvNote.setTextColor(Color.GRAY);
        }else{
            tvNote.setText(channelItem.getNote());
            tvNote.setTypeface(tvNote.getTypeface(), Typeface.NORMAL);
        }

        return convertView;
    }

    public void updatedData(ArrayList<ChannelItem> newData){
        this.data = newData;
        notifyDataSetChanged();

    }
}

class FavoriteViewHolder
{
    TextView tvName;
    TextView tvNote;
    ImageButton btEdit;
    ImageButton btUnFavorite;
    LinearLayout llRow;
}