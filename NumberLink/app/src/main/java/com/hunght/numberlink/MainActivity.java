package com.hunght.numberlink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.hunght.data.DataProcess;
import com.hunght.data.GameItem;
import com.hunght.data.LevelItem;
import com.hunght.data.StaticData;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    GridView grvLevelItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        grvLevelItems = (GridView) findViewById(R.id.grvLevelItems);

        ArrayList<LevelItem> levelItems = DataProcess.getLevelItems();
        GridviewLevelItemAdapter gridviewLevelItemAdapter = new GridviewLevelItemAdapter(this, levelItems);
        grvLevelItems.setAdapter(gridviewLevelItemAdapter);
    }

    public void btOnClick(View view)
    {
        switch (view.getId())
        {
            //case R.id.btGameLevel:
             //   Intent intent = new Intent(this, LevelActivity.class);
           //     startActivity(intent);
            //    break;
        }
    }
}
