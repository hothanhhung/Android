package com.hunght.solarlunarcalendar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.hunght.data.DateItemForGridview;
import com.hunght.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.Date;

import static android.view.View.GONE;

/**
 * Created by Lenovo on 3/26/2018.
 */

public class DateItemAdapter extends ArrayAdapter<DateItemForGridview> {
    private ArrayList<DateItemForGridview> data;
    private static LayoutInflater inflater=null;
    public Resources res;
    Context context;
    Date selectedDate;
    boolean needGoodDateLevel = false;

    public DateItemAdapter(Context a, ArrayList<DateItemForGridview> d, Resources resLocal, boolean needGoodDateLevel ) {
        super( a, R.layout.detail_info_date_item, R.id.title, d );
        context = a;
        data=d;
        res = resLocal;
        inflater = LayoutInflater.from(a) ;
        selectedDate = new Date();
        this.needGoodDateLevel = needGoodDateLevel;
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
        ImageView imSelectedDate;

        if(convertView==null)
        {
            convertView = inflater.inflate(R.layout.detail_info_date_item, null);
            viewHolder = new DateItemViewHolder();

            viewHolder.tvtitle = tvTitle = (TextView)convertView.findViewById(R.id.tvTitle); // title
            viewHolder.tvSolarDate = tvSolarDate = (TextView)convertView.findViewById(R.id.tvSolarDate);// title
            viewHolder.tvInfoDate = tvInfoDate = (TextView)convertView.findViewById(R.id.tvInfoDate);// title
            viewHolder.tvLunarDate = tvLunarDate = (TextView)convertView.findViewById(R.id.tvLunarDate);
            viewHolder.tblDetailDate = tblDetailDate = (TableLayout)convertView.findViewById(R.id.tblDetailDate);
            viewHolder.imSelectedDate = imSelectedDate = (ImageView)convertView.findViewById(R.id.imSelectedDate);

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
            imSelectedDate = viewHolder.imSelectedDate;
        }
        DateItemForGridview item = data.get(position);

        if(item!=null) {
            if(item.isTitle())
            {
                tvTitle.setVisibility(View.VISIBLE);
                tblDetailDate.setVisibility(GONE);
                imSelectedDate.setVisibility(GONE);
                tvTitle.setText(item.getTitle());
            }else{
                tvTitle.setVisibility(GONE);
                tblDetailDate.setVisibility(View.VISIBLE);
                tvSolarDate.setText(item.getSolarDate());
                tvLunarDate.setText(item.getLunarDateToDisplay());
                if(item.isWeekend() || item.isHoliday()){
                    int red = Color.RED;
                    if(needGoodDateLevel) {
                        red = Color.parseColor("#890000");
                    }
                    tvTitle.setTextColor(red);
                    tvSolarDate.setTextColor(red);
                    tvLunarDate.setTextColor(red);
                }
                else{
                    tvTitle.setTextColor(Color.DKGRAY);
                    tvSolarDate.setTextColor(Color.DKGRAY);
                    tvLunarDate.setTextColor(Color.DKGRAY);
                }

                if(SharedPreferencesUtils.getShowGoodDayBadDateSetting(getContext()))
                {
                    if(item.isGoodDay())
                    {
                        tvInfoDate.setTextColor(Color.RED);
                        tvInfoDate.setVisibility(View.VISIBLE);
                    }else  if(item.isBadDay())
                    {
                        tvInfoDate.setTextColor(Color.BLACK);
                        tvInfoDate.setVisibility(View.VISIBLE);
                    }else{
                        tvInfoDate.setVisibility(GONE);
                    }
                }else{
                    tvInfoDate.setVisibility(GONE);
                }

                if(item.isOutOfMonth())
                {
                    if(needGoodDateLevel) {
                        convertView.setAlpha(0.3f);
                        tvSolarDate.setAlpha(0.4f);
                        tvLunarDate.setAlpha(0.4f);
                        tvInfoDate.setAlpha(0.4f);
                    }else{
                        convertView.setAlpha(0.7f);
                        tvSolarDate.setAlpha(0.5f);
                        tvLunarDate.setAlpha(0.5f);
                        tvInfoDate.setAlpha(0.5f);
                    }
                }else{
                    convertView.setAlpha(1);
                    tvSolarDate.setAlpha(1);
                    tvLunarDate.setAlpha(1);
                    tvInfoDate.setAlpha(1);
                }


                if(needGoodDateLevel)
                {
                    tblDetailDate.setBackgroundColor(getGoodDateLevelColor(item.getGoodDateLevel()));
                    if(item.isToday())
                    {
                        imSelectedDate.setImageResource(R.drawable.rectangle);
                        imSelectedDate.setVisibility(View.VISIBLE);
                    }else if (item.isTheSame(selectedDate)){
                        imSelectedDate.setImageResource(R.drawable.circle);
                        imSelectedDate.setVisibility(View.VISIBLE);
                    }else
                    {
                        imSelectedDate.setVisibility(GONE);
                    }
                }else {
                    imSelectedDate.setVisibility(GONE);
                    if(item.isToday())
                    {
                        tblDetailDate.setBackgroundColor(Color.YELLOW);
                    }else if (item.isTheSame(selectedDate)){
                        tblDetailDate.setBackgroundColor(Color.GREEN);
                    }else
                    {
                        tblDetailDate.setBackgroundColor(Color.WHITE);
                    }
                }
            }

        }
        convertView.setTag(item);
        return convertView;
    }

    private int getGoodDateLevelColor(int index) {
        switch (index) {
            case 0:
                return Color.parseColor("#9f9f9f");
            case 1:
                return Color.parseColor("#acbcc3");
            case 2:
                return Color.parseColor("#a8dff9");
            case 3:
                return Color.parseColor("#f984eb");
            case 4:
                return Color.parseColor("#fe7ca8");
            case 5:
                return Color.parseColor("#fc4c70");
            default:
                return Color.parseColor("#ffffff");
        }
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
    ImageView imSelectedDate;
}