package com.hunght.solarlunarcalendar;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableLayout;
import android.widget.TextView;

import com.hunght.data.DateItemForGridview;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Lenovo on 3/26/2018.
 */

public class DateItemAdapter extends ArrayAdapter<DateItemForGridview> {
    private ArrayList<DateItemForGridview> data;
    private static LayoutInflater inflater=null;
    public Resources res;
    Context context;
    Date selectedDate;

    public DateItemAdapter(Context a, ArrayList<DateItemForGridview> d, Resources resLocal ) {
        super( a, R.layout.detail_info_date_item, R.id.title, d );
        context = a;
        data=d;
        res = resLocal;
        inflater = LayoutInflater.from(a) ;
        selectedDate = new Date();
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
        DateItemViewHolder viewHolder;
        TextView tvTitle, tvSolarDate, tvInfoDate, tvLunarDate;
        TableLayout tblDetailDate;
        if(convertView==null)
        {
            convertView = inflater.inflate(R.layout.detail_info_date_item, null);
            viewHolder = new DateItemViewHolder();

            viewHolder.tvtitle = tvTitle = (TextView)convertView.findViewById(R.id.tvTitle); // title
            viewHolder.tvSolarDate = tvSolarDate = (TextView)convertView.findViewById(R.id.tvSolarDate);// title
            viewHolder.tvInfoDate = tvInfoDate = (TextView)convertView.findViewById(R.id.tvInfoDate);// title
            viewHolder.tvLunarDate = tvLunarDate = (TextView)convertView.findViewById(R.id.tvLunarDate);
            viewHolder.tblDetailDate = tblDetailDate = (TableLayout)convertView.findViewById(R.id.tblDetailDate);

            tvTitle.setBackgroundColor(Color.LTGRAY);
            convertView.setTag(R.layout.detail_info_date_item, viewHolder);
        }
        else{
            viewHolder = (DateItemViewHolder)convertView.getTag(R.layout.detail_info_date_item);
            tvTitle = viewHolder.tvtitle; // title
            tvSolarDate = viewHolder.tvSolarDate;
            tvInfoDate = viewHolder.tvInfoDate;
            tvLunarDate = viewHolder.tvLunarDate;
            tblDetailDate = viewHolder.tblDetailDate;
        }
        DateItemForGridview item = data.get(position);

        if(item!=null) {
            if(item.isTitle())
            {
                tvTitle.setVisibility(View.VISIBLE);
                tblDetailDate.setVisibility(View.GONE);
            }else{
                tvTitle.setVisibility(View.GONE);
                tblDetailDate.setVisibility(View.VISIBLE);
            }
            tvTitle.setText(item.getTitle());
            tvSolarDate.setText(item.getSolarDate());
            tvLunarDate.setText(item.getLunarDateToDisplay());
            if(item.isHoliday()){
                tvTitle.setTextColor(Color.RED);
                tvSolarDate.setTextColor(Color.RED);
                tvLunarDate.setTextColor(Color.RED);
            }

            if(item.isGoodDay())
            {
                tvInfoDate.setTextColor(Color.RED);
                tvInfoDate.setVisibility(View.VISIBLE);
            }else  if(item.isBadDay())
            {
                tvInfoDate.setTextColor(Color.BLACK);
                tvInfoDate.setVisibility(View.VISIBLE);
            }else{
                tvInfoDate.setVisibility(View.GONE);
            }

            if(item.isOutOfMonth())
            {
                convertView.setAlpha(0.7f);
                tvSolarDate.setAlpha(0.5f);
                tvLunarDate.setAlpha(0.5f);
                tvInfoDate.setAlpha(0.5f);
            }else{
                convertView.setAlpha(1);
                tvSolarDate.setAlpha(1);
                tvLunarDate.setAlpha(1);
                tvInfoDate.setAlpha(1);
            }

            if(item.isToday())
            {
                tblDetailDate.setBackgroundColor(Color.YELLOW);
            }else{

                tblDetailDate.setBackgroundColor(Color.WHITE);
            }

            if(item.isTheSame(selectedDate))
            {
                tblDetailDate.setBackgroundColor(Color.GREEN);
            }else{

            }

        }
        convertView.setTag(item);
        return convertView;
    }

    public void updateSelectedDate(Date date, ArrayList<DateItemForGridview> d){
        data = d;
        selectedDate = date;
        notifyDataSetChanged();
    }

    public void updateSelectedDate(Date date){
        selectedDate = date;
        notifyDataSetChanged();
    }
}

class DateItemViewHolder
{
    TextView tvtitle;
    TextView tvSolarDate;
    TextView tvInfoDate;
    TextView tvLunarDate;
    TableLayout tblDetailDate;
}