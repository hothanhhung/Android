package com.hunght.numberlink;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hunght.data.GameItem;
import com.hunght.data.LevelItem;
import com.hunght.data.StaticData;

import java.util.ArrayList;

/**
 * Created by Lenovo on 5/15/2017.
 */

public class GridviewLevelItemAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<LevelItem> data;

    public GridviewLevelItemAdapter(Context mContext, ArrayList<LevelItem> data)
    {
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public int getCount() {
        if(data!=null) return data.size();
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if(data!=null && data.size() > i){
            return data.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        if(data!=null && data.size() > i){
            LevelItem levelItem = data.get(i);
            if(levelItem!=null)
            {
                levelItem.getLevelId();
            }
        }
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        RelativeLayout btLevelItem;
        TextView tvSize, tvStatus;

        if (view == null) {
            // if it's not recycled, initialize some attributes
            view = LayoutInflater.from(mContext).inflate(R.layout.level_item, null);

        }
        LevelItem levelItem = (LevelItem)getItem(i);
        btLevelItem = (RelativeLayout) view.findViewById(R.id.btLevelItem);
        tvSize = (TextView) view.findViewById(R.id.tvSize);
        tvStatus = (TextView) view.findViewById(R.id.tvStatus);

        btLevelItem.setTag(levelItem);
        btLevelItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LevelItem levelItem = (LevelItem) view.getTag();
                Intent intent = new Intent(mContext, LevelActivity.class);
                StaticData.setCurrentLevel(levelItem);
                mContext.startActivity(intent);
            }
        });

        tvSize.setText(levelItem.getSize());
        tvStatus.setText(levelItem.getStatus());
        if(levelItem.isAllComplete()){
            tvSize.setTextColor(Color.parseColor("#297247"));
            tvStatus.setTextColor(Color.parseColor("#297247"));
        }else{
            tvSize.setTextColor(Color.WHITE);
            tvStatus.setTextColor(Color.WHITE);
        }
        return view;

    }
}
