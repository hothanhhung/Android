package com.hth.eyetest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import com.hth.data.HighScoreItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class HighScoreActivity extends AppCompatActivity {
    ExpandableProfileListAdapter listAdapter;
    ExpandableListView lvProfiles;
    List<String> listDataHeader;
    HashMap<String, ArrayList<HighScoreItem>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        lvProfiles = (ExpandableListView) findViewById(R.id.lvProfiles);
        // preparing list data
        prepareListData();
        listAdapter = new ExpandableProfileListAdapter(this, listDataHeader, listDataChild);
        // setting list adapter
        lvProfiles.setAdapter(listAdapter);
        lvProfiles.expandGroup(0);

    }
    private void prepareListData() {
        SavedValues savedValues = new SavedValues(this);
        listDataHeader = new ArrayList<String>();
        listDataHeader.add("Top");
        listDataChild = new HashMap<String, ArrayList<HighScoreItem>>();
        ArrayList<HighScoreItem> highScoreItems = savedValues.getRecordHighScore();
        if(highScoreItems!=null && highScoreItems.size() > 1) {
            Collections.sort(highScoreItems, new Comparator<HighScoreItem>() {
                @Override
                public int compare(HighScoreItem highScoreItem1, HighScoreItem highScoreItem2) {

                    return highScoreItem2.getScore() - highScoreItem1.getScore();
                }
            });
        }
        /*if(highScoreItems.size() > 20){
            listDataChild.put(listDataHeader.get(0), highScoreItems.subList(0, 20));
        }else*/
        {
            listDataChild.put(listDataHeader.get(0), highScoreItems);
        }
    }
}
