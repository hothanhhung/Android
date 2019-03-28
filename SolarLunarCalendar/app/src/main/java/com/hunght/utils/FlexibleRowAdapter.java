package com.hunght.utils;

import java.util.ArrayList;

import com.hunght.solarlunarcalendar.R;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FlexibleRowAdapter extends ArrayAdapter<AdItem> {
    private Context activity;
    private ArrayList<AdItem> data;
    private static LayoutInflater inflater=null;
    public Resources res;

    public FlexibleRowAdapter(Context a, ArrayList<AdItem> d, Resources resLocal ) {
        super( a, R.layout.item_ads_flexible);
        activity = a;
        if(d == null) data = new ArrayList<AdItem>();
        else data=d;
        res = resLocal;
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
        ViewHolder viewHolder;
        TextView tvTitle;
        TextView tvShortContent;
        TextView tvNoteContent;
        ImageView ivImageArticle;
        if(convertView==null)
        {
            convertView = inflater.inflate(R.layout.item_ads_flexible, null);
            viewHolder = new ViewHolder();

            viewHolder.title = tvTitle = (TextView)convertView.findViewById(R.id.title); // title
            viewHolder.shortContent = tvShortContent = (TextView)convertView.findViewById(R.id.shortContent);
            viewHolder.noteContent = tvNoteContent = (TextView)convertView.findViewById(R.id.noteContent);
            viewHolder.imageArticle = ivImageArticle = (ImageView)convertView.findViewById(R.id.imageArticle); // thumb image
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
            tvTitle = viewHolder.title; // title
            tvShortContent = viewHolder.shortContent;
            tvNoteContent = viewHolder.noteContent;
            ivImageArticle = viewHolder.imageArticle;
        }
        AdItem adFlex = data.get(position);

        // Setting all values in listview
        tvTitle.setText(adFlex.getName());
        tvTitle.setTag(adFlex);

        if(adFlex.getDesc().trim() == "")
            tvShortContent.setVisibility(View.GONE);
        else{
            tvShortContent.setVisibility(View.VISIBLE);
            tvShortContent.setText(adFlex.getDesc());
        }


        if(adFlex.getType().trim() == "")
            tvNoteContent.setVisibility(View.GONE);
        else{
            tvNoteContent.setVisibility(View.VISIBLE);
            tvNoteContent.setText(adFlex.getType());
        }


        if(adFlex.getUrlImage().trim()!="")
        {
            ivImageArticle.setVisibility(View.VISIBLE);
            Picasso.with(activity).load(adFlex.getUrlImage()).into(ivImageArticle);
        }else
        {
            ivImageArticle.setVisibility(View.GONE);
        }
        return convertView;
    }

}

class ViewHolder
{
    TextView title;
    TextView shortContent;
    TextView noteContent;
    ImageView imageArticle;
}