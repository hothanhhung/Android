package com.hth.sudoku;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.hth.utils.DataBaseHelper;
import com.hth.utils.MethodsHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    ExpandableProfileListAdapter listAdapter;
    ExpandableListView lvProfiles;
    List<String> listDataHeader;
    HashMap<String, List<SudokuItem>> listDataChild;
    DataBaseHelper dataBaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dataBaseHelper = new DataBaseHelper(this);
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
                SudokuItem sudokuItem = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                if(sudokuItem!=null){
                    Dialog dialog = MethodsHelper.createProfileDetailDialog(sudokuItem, ProfileActivity.this);
                    dialog.show();
                }
                return false;
            }
        });
    }
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<SudokuItem>>();

        // Adding child data
        listDataHeader.add("Easy");
        listDataHeader.add("Medium");
        listDataHeader.add("Hard");
       // listDataHeader.add("Special");
        listDataHeader.add("Your Creation");

        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        // Adding child data
        List<SudokuItem> easy = dataBaseHelper.getUsedSudokus(Data.DIFFICULTY_EASY);
        List<SudokuItem> medium = dataBaseHelper.getUsedSudokus(Data.DIFFICULTY_MEDIUM);
        List<SudokuItem> heard = dataBaseHelper.getUsedSudokus(Data.DIFFICULTY_HARD);
      //  List<SudokuItem> special = dataBaseHelper.getUsedSudokus(Data.DIFFICULTY_SPECIAL);
        List<SudokuItem> creation = dataBaseHelper.getUsedSudokus(Data.DIFFICULTY_CREATE);

        listDataChild.put(listDataHeader.get(0), easy); // Header, Child data
        listDataChild.put(listDataHeader.get(1), medium);
        listDataChild.put(listDataHeader.get(2), heard);
       // listDataChild.put(listDataHeader.get(3), special);
        listDataChild.put(listDataHeader.get(3), creation);
    }
}
