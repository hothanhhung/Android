package com.hth.lichtivi;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import com.ikimuhendis.ldrawer.ActionBarDrawerToggle;
import com.ikimuhendis.ldrawer.DrawerArrowDrawable;

import java.util.ArrayList;

public class MainActivity extends Activity implements
        SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    private DrawerLayout mDrawerLayout;
    private LinearLayout mLeftDrawerList;
    private ListView mRightDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerArrowDrawable drawerArrow;
    private boolean drawerArrowColor;


    private SearchView search;
    private ChannelsExpandableListAdapter lvChannelsAdapter;
    private ExpandableListView lvChannels;
    private ArrayList<ChannelGroup> channelGroupList = new ArrayList<ChannelGroup>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar ab = getActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLeftDrawerList = (LinearLayout) findViewById(R.id.leftNavdrawer);
        mRightDrawerList = (ListView) findViewById(R.id.rightNavdrawer);
/*

        drawerArrow = new DrawerArrowDrawable(this) {
            @Override
            public boolean isLayoutRtl() {
                return false;
            }
        };
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                drawerArrow, R.string.drawer_open,
                R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();*/

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        search = (SearchView) findViewById(R.id.search);
        search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        search.setIconifiedByDefault(false);
        search.setOnQueryTextListener(this);
        search.setOnCloseListener(this);

        //display the list
        displayList();
        //expand all Groups
        expandOrCollapseAll(false);
    }
    private void expandOrCollapseAll(boolean isExpand) {
        int count = lvChannelsAdapter.getGroupCount();
        if(isExpand) {
            for (int i = 0; i < count; i++) {
                lvChannels.expandGroup(i);
            }
        }else {
            for (int i = 0; i < count; i++) {
                lvChannels.collapseGroup(i);
            }
        }
    }

    //method to expand all groups
    private void displayList() {
        channelGroupList = Data.getChannelGroup();
        lvChannels = (ExpandableListView) findViewById(R.id.lvChannels);
        //create the adapter by passing your ArrayList data
        lvChannelsAdapter = new ChannelsExpandableListAdapter(MainActivity.this, channelGroupList);
        //attach the adapter to the list
        lvChannels.setAdapter(lvChannelsAdapter);
    }
        @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(mLeftDrawerList)) {
                mDrawerLayout.closeDrawer(mLeftDrawerList);
            } else {
                mDrawerLayout.openDrawer(mLeftDrawerList);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
       // mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
       // mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onClose() {
        lvChannelsAdapter.filterData("");
        expandOrCollapseAll(false);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        lvChannelsAdapter.filterData(query);
        expandOrCollapseAll(!query.isEmpty());
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        lvChannelsAdapter.filterData(query);
        expandOrCollapseAll(!query.isEmpty());
        return false;
    }
}
