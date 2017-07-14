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

import java.util.ArrayList;
import com.hunght.data.GameItem;
import com.hunght.data.StaticData;

/**
 * Created by Lenovo on 5/15/2017.
 */

public class GridviewGameItemAdapter extends BaseAdapter {
    private Activity mContext;
    private ArrayList<GameItem> data;

    public GridviewGameItemAdapter(Activity mContext, ArrayList<GameItem> data)
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
            GameItem gameItem = data.get(i);
            if(gameItem!=null)
            {
                gameItem.getId();
            }
        }
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Button btGameItem;
        if (view == null) {
            // if it's not recycled, initialize some attributes
            view = LayoutInflater.from(mContext).inflate(R.layout.game_item, null);

        }
        GameItem gameItem = (GameItem)getItem(i);
        btGameItem = (Button) view.findViewById(R.id.btGameItem);
        btGameItem.setTag(gameItem);
        btGameItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameItem gameItem = (GameItem) view.getTag();
                Intent intent = new Intent(mContext, GameActivity.class);
                StaticData.setCurrentGame(gameItem);
                mContext.startActivity(intent);
                mContext.finish();
            }
        });

        btGameItem.setText(String.valueOf(i + 1));
        btGameItem.setEnabled(gameItem.isEnable());
        if(gameItem.isWin()){
            btGameItem.setBackgroundResource(R.drawable.round_button_win);
        }else if (gameItem.isPlaying()){
            btGameItem.setBackgroundResource(R.drawable.round_button_playing);
        }else {
            btGameItem.setBackgroundResource(R.drawable.round_button);
        }
        return view;

    }
}
