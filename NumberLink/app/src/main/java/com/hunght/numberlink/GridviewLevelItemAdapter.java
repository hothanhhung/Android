package com.hunght.numberlink;

import android.app.Activity;
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
import com.hunght.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by Lenovo on 5/15/2017.
 */

public class GridviewLevelItemAdapter extends BaseAdapter {
    private Activity mContext;
    private ArrayList<LevelItem> data;

    public GridviewLevelItemAdapter(Activity mContext, ArrayList<LevelItem> data)
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
        RelativeLayout btLevelItem, btLockLevelItem;
        TextView tvSize, tvNumberWinGames, tvNumberGames;

        if (view == null) {
            // if it's not recycled, initialize some attributes
            view = LayoutInflater.from(mContext).inflate(R.layout.level_item, null);

        }
        final LevelItem levelItem = (LevelItem)getItem(i);
        btLevelItem = (RelativeLayout) view.findViewById(R.id.btLevelItem);
        btLockLevelItem = (RelativeLayout) view.findViewById(R.id.btLockLevelItem);
        tvSize = (TextView) view.findViewById(R.id.tvSize);
        tvNumberWinGames = (TextView) view.findViewById(R.id.tvNumberWinGames);
        tvNumberGames = (TextView) view.findViewById(R.id.tvNumberGames);

        btLevelItem.setTag(levelItem);
        btLockLevelItem.setTag(levelItem);
        btLockLevelItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // UIUtils.showAlertInform(mContext, levelItem.getLockMessage());
                ((MainActivity)mContext).showDialog(view, levelItem.getLockMessage());
            }
        });
        btLevelItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LevelItem levelItem = (LevelItem) view.getTag();
                Intent intent = new Intent(mContext, LevelActivity.class);
                StaticData.setCurrentLevel(levelItem);
                mContext.startActivity(intent);
                mContext.finish();
            }
        });

        btLockLevelItem.setVisibility( levelItem.isLock()? View.VISIBLE : View.GONE);
        tvSize.setText(levelItem.getSize());
        tvNumberWinGames.setText(""+levelItem.getNumberWinGame());
        tvNumberGames.setText(""+levelItem.getTotalGames());
        if(levelItem.isAllWin()){
            btLevelItem.setBackgroundResource(R.drawable.selector_level_win);
        }else if (levelItem.isPlaying()){
            btLevelItem.setBackgroundResource(R.drawable.selector_level_playing);
        }else {
            btLevelItem.setBackgroundResource(R.drawable.selector_level);
        }
        return view;

    }
}
