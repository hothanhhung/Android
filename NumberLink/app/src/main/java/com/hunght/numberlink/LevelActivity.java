package com.hunght.numberlink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.hunght.data.DataProcess;
import com.hunght.data.GameItem;
import com.hunght.data.LevelItem;
import com.hunght.data.StaticData;

import java.util.ArrayList;
import java.util.logging.Logger;

public class LevelActivity extends AppCompatActivity {

    GridView grvGameItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        grvGameItems = (GridView) findViewById(R.id.grvGameItems);

        LevelItem levelItem = StaticData.getCurrentLevelm();
        GridviewGameItemAdapter gridviewGameItemAdapter = new GridviewGameItemAdapter(this, DataProcess.getGameItem(levelItem.getGameIds()));
        grvGameItems.setAdapter(gridviewGameItemAdapter);

    }
}
