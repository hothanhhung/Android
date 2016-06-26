package com.hth.lines;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.hth.utils.MethodsHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    ExpandableProfileListAdapter listAdapter;
    ExpandableListView lvProfiles;
    List<String> listDataHeader;
    HashMap<String, List<Object>> listDataChild;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        lvProfiles = (ExpandableListView) findViewById(R.id.lvProfiles);
        // preparing list data
        prepareListData();
        listAdapter = new ExpandableProfileListAdapter(this, listDataHeader, listDataChild);
        // setting list adapter
        lvProfiles.setAdapter(listAdapter);

        lvProfiles.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                return false;
            }
        });
    }
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<Object>>();

    }
}
