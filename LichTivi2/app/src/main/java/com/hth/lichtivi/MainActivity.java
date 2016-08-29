package com.hth.lichtivi;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.hth.utils.MethodsHelper;
import com.hth.utils.ParseJSONScheduleItems;
import com.hth.utils.ScheduleItem;
import com.ikimuhendis.ldrawer.ActionBarDrawerToggle;
import com.ikimuhendis.ldrawer.DrawerArrowDrawable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity implements
        SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    private Activity activity;
    private DrawerLayout mDrawerLayout;
    private LinearLayout mLeftDrawerList;
    private ListView mRightDrawerList;
    private ListView lvSchedules;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerArrowDrawable drawerArrow;
    private boolean drawerArrowColor;


    private Date selectedDate;
    private String selectedChannelKey = "VTV1";
    private String openKey = "";
    private TextView tvSelectedDate;
    private TextView tvMessage;
    private ParseJSONScheduleItems parseJSONScheduleItems;
    private ScheduleAsyncTask scheduleAsyncTask;

    private SearchView search;
    private ChannelsExpandableListAdapter lvChannelsAdapter;
    private ExpandableListView lvChannels;
    private ArrayList<ChannelGroup> channelGroupList = new ArrayList<ChannelGroup>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        selectedDate = new Date(year, month, day);
        openKey = "" + selectedDate.getTime();
        parseJSONScheduleItems = new ParseJSONScheduleItems(this);

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
        lvSchedules = (ListView) findViewById(R.id.lvSchedules);
        tvMessage = (TextView) findViewById(R.id.tvMessage);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        search = (SearchView) findViewById(R.id.search);
        search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        search.setIconifiedByDefault(false);
        search.setOnQueryTextListener(this);
        search.setOnCloseListener(this);
        tvSelectedDate = (TextView) findViewById(R.id.tvSelectedDate);

        showDate(selectedDate);

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

    private void updateSchedules()
    {
        tvMessage.setVisibility(View.VISIBLE);
        tvMessage.setText("Đang tải dữ liệu...");
        lvSchedules.setVisibility(View.GONE);
        if(scheduleAsyncTask!=null)
        {
            scheduleAsyncTask.cancel(true);
        }
        scheduleAsyncTask = new ScheduleAsyncTask();
        scheduleAsyncTask.execute();
    }

    //method to expand all groups
    private void displayList() {
        channelGroupList = Data.getChannelGroup();
        lvChannels = (ExpandableListView) findViewById(R.id.lvChannels);
        //create the adapter by passing your ArrayList data
        lvChannelsAdapter = new ChannelsExpandableListAdapter(MainActivity.this, channelGroupList);
        //attach the adapter to the list
        lvChannels.setAdapter(lvChannelsAdapter);
        lvChannels.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                ChannelItem channelItem = (ChannelItem) v.getTag();
                if (channelItem != null) {
                    selectedChannelKey = channelItem.getId();
                    if (mDrawerLayout.isDrawerOpen(mLeftDrawerList)) {
                        mDrawerLayout.closeDrawer(mLeftDrawerList);
                    } else {
                        mDrawerLayout.openDrawer(mLeftDrawerList);
                    }
                    updateSchedules();
                }
                return false;
            }
        });
    }

    private void showDate(Date date) {
        tvSelectedDate.setText(MethodsHelper.getStringFromDate(date));
        updateSchedules();
    }

    DatePickerDialog datePickerDialog;
    private DatePickerDialog buildDatePickerDialog()
    {
        datePickerDialog  = new DatePickerDialog(this, null, selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDate());

        datePickerDialog.setCancelable(true);
        datePickerDialog.setCanceledOnTouchOutside(false);
        datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "Xem lịch",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatePicker dp = datePickerDialog.getDatePicker();
                        datePickerDialog.dismiss();
                        if(selectedDate.getDate() != dp.getDayOfMonth() || selectedDate.getMonth() != dp.getMonth() || selectedDate.getYear() != dp.getYear())
                        {
                            selectedDate.setYear(dp.getYear());
                            selectedDate.setMonth(dp.getMonth());
                            selectedDate.setDate(dp.getDayOfMonth());
                            showDate(selectedDate);
                        }
                    }
                });
        datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Bỏ qua",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        datePickerDialog.dismiss();
                    }
                });
        return datePickerDialog;
    }
    public void setDate(View view) {
        if(datePickerDialog == null)
        {
            datePickerDialog = buildDatePickerDialog();
        }
        datePickerDialog.updateDate(selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDate());
        datePickerDialog.show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.acion_menu, menu);
        return true;
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

    private class ScheduleAsyncTask extends AsyncTask<String, Void, ArrayList<ScheduleItem>> {
        @Override
        protected ArrayList<ScheduleItem> doInBackground(String... urls) {
            return parseJSONScheduleItems.getSchedules(selectedChannelKey, selectedDate, openKey);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(ArrayList<ScheduleItem> scheduleItems) {
            if(scheduleItems == null)
            {
                tvMessage.setVisibility(View.VISIBLE);
                tvMessage.setText("Lỗi khi tải dữ liệu");
            }else if(scheduleItems.size() == 0){
                tvMessage.setVisibility(View.VISIBLE);
                tvMessage.setText("Chưa có dữ liệu");
            }else {
                lvSchedules.setVisibility(View.VISIBLE);
                FlexibleScheduleRowAdapter adapter = new FlexibleScheduleRowAdapter(activity, scheduleItems, activity.getResources());
                lvSchedules.setAdapter(adapter);
                tvMessage.setVisibility(View.GONE);
            }
        }
    }
}
