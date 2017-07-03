package com.hunght.numberlink;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import com.hunght.data.LevelItem;
import com.hunght.data.StaticData;

public class LevelActivity extends AppCompatActivity {

    GridView grvGameItems;
    GridviewGameItemAdapter gridviewGameItemAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        grvGameItems = (GridView) findViewById(R.id.grvGameItems);

        LevelItem levelItem = StaticData.getCurrentLevel();
        gridviewGameItemAdapter = new GridviewGameItemAdapter(this, levelItem.getGameItems());
        grvGameItems.setAdapter(gridviewGameItemAdapter);

    }
    protected void onResume() {
        super.onResume();
        if(gridviewGameItemAdapter != null) gridviewGameItemAdapter.notifyDataSetChanged();
        System.gc();
    }
}
