package com.hunght.numberlink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.hunght.data.DataProcess;
import com.hunght.data.GameItem;
import com.hunght.data.LevelItem;
import com.hunght.data.SavedValues;
import com.hunght.data.StaticData;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    GridView grvLevelItems;
    SavedValues savedValues;

    GridviewLevelItemAdapter gridviewLevelItemAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        savedValues= new SavedValues(this);
        grvLevelItems = (GridView) findViewById(R.id.grvLevelItems);

        ArrayList<LevelItem> levelItems = DataProcess.getLevelItems(this);

        if(StaticData.isStart()) {
            String currentGameId = savedValues.getCurrentGameId();
            if (currentGameId != "") {
                for (LevelItem levelItem : levelItems) {
                    for (GameItem gameItem : levelItem.getGameItems()) {
                        if (gameItem.getId().equalsIgnoreCase(currentGameId)) {
                            StaticData.setCurrentLevel(levelItem);
                            StaticData.setCurrentGame(gameItem);
                            Intent intent = new Intent(this, GameActivity.class);
                            this.startActivity(intent);
                            finish();
                            return;
                        }
                    }
                }
            }
        }
        gridviewLevelItemAdapter = new GridviewLevelItemAdapter(this, levelItems);
        grvLevelItems.setAdapter(gridviewLevelItemAdapter);
    }

    protected void onResume() {
        super.onResume();
        if(gridviewLevelItemAdapter != null) gridviewLevelItemAdapter.notifyDataSetChanged();
        System.gc();
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
