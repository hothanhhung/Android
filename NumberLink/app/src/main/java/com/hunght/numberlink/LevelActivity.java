package com.hunght.numberlink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

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
        updateSummaryUI();
        System.gc();
    }
    @Override
    public void onBackPressed()
    {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }

    private void updateSummaryUI()
    {
        TextView tvSesion = (TextView)findViewById(R.id.tvSesion);
        TextView tvNumberOfWins = (TextView)findViewById(R.id.tvNumberOfWins);
        TextView tvNumberOfPlayings = (TextView)findViewById(R.id.tvNumberOfPlayings);
        TextView tvNumberOfGames = (TextView)findViewById(R.id.tvNumberOfGames);

        tvSesion.setText(""+StaticData.getCurrentLevel().getSize());
        tvNumberOfWins.setText(""+StaticData.getCurrentLevel().getNumberWinGame());
        tvNumberOfPlayings.setText(""+StaticData.getCurrentLevel().getTotalPlayingGames());
        tvNumberOfGames.setText(""+StaticData.getCurrentLevel().getTotalGames());

    }
}
