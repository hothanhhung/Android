package com.hth.lichtivi;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.hth.data.DataFavorite;
import com.hth.utils.MethodsHelper;
import com.hth.utils.ParseJSONScheduleItems;
import com.hth.utils.ScheduleItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity implements
        SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    private AdView mAdView = null;
    private InterstitialAd interstitial = null;


    private Activity activity;
    private SearchProgramView searchProgramView;
    private AlarmManagerView alarmManagerView;

    private DrawerLayout mDrawerLayout;
    private LinearLayout mLeftDrawerList;
    private LinearLayout mRightDrawerList;
    private ListView lvSchedules;

    private Date selectedDate;
    private ChannelItem selectedChannel;
    private TextView tvSelectedDate;
    private TextView tvMessage;
    private TextView tvSelectedChannel;
    private ParseJSONScheduleItems parseJSONScheduleItems;
    private ScheduleAsyncTask scheduleAsyncTask;

    private SearchView search;
    private ChannelsExpandableListAdapter lvChannelsAdapter;
    private FlexibleFavoriteRowAdapter lvFavoritesAdapter;
    FlexibleScheduleRowAdapter flexibleScheduleRowAdapter;

    private TextView tvNoFavorites;
    private ImageButton btFavorite;
    private ExpandableListView lvChannels;
    private ArrayList<ChannelGroup> channelGroupList = new ArrayList<ChannelGroup>();
    private ArrayList<ChannelItem> favoriteslList = new ArrayList<ChannelItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;
        selectedChannel = new ChannelItem("VTV1", "VTV1");
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        selectedDate = new Date(year, month, day);
        parseJSONScheduleItems = new ParseJSONScheduleItems(this);

        ActionBar ab = getActionBar();
        ab.hide();
        /*ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);*/
        searchProgramView = (SearchProgramView) findViewById(R.id.searchProgramView);
        alarmManagerView = (AlarmManagerView) findViewById(R.id.alarmManagerView);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLeftDrawerList = (LinearLayout) findViewById(R.id.leftNavdrawer);
        mRightDrawerList = (LinearLayout) findViewById(R.id.rightNavdrawer);

        tvSelectedChannel = (TextView) findViewById(R.id.tvSelectedChannel);
        lvSchedules = (ListView) findViewById(R.id.lvSchedules);
        tvMessage = (TextView) findViewById(R.id.tvMessage);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        search = (SearchView) findViewById(R.id.search);
        search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        search.setIconifiedByDefault(false);
        search.setOnQueryTextListener(this);
        search.setOnCloseListener(this);
        tvSelectedDate = (TextView) findViewById(R.id.tvSelectedDate);

        flexibleScheduleRowAdapter = new FlexibleScheduleRowAdapter(activity, null, activity.getResources());
        lvSchedules.setAdapter(flexibleScheduleRowAdapter);
        //display the list
        displayList();
        //expand all Groups
        expandOrCollapseAll(false);

        initFavoriteData();
        showDate(selectedDate);

        mAdView = (AdView) this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    private void removeFavorite(ChannelItem item)
    {
        for(int i = 0; i< favoriteslList.size(); i++)
        {
            if(favoriteslList.get(i).getId().equalsIgnoreCase(item.getId()))
            {
                favoriteslList.remove(i);
                i--;
            }
        }
        saveFavorites();

    }

    private void addFavorite(ChannelItem item)
    {
        favoriteslList.add(item);
        saveFavorites();

    }

    private boolean isInFavorites(ChannelItem item)
    {
        for(int i = 0; i< favoriteslList.size(); i++)
        {
            if(favoriteslList.get(i).getId().equalsIgnoreCase(item.getId()))
            {
                return true;
            }
        }
        return false;
    }
    private void saveFavorites()
    {
        DataFavorite.setFavorites(this, favoriteslList);
        lvFavoritesAdapter.updatedData(favoriteslList);
        if(favoriteslList == null || favoriteslList.size() == 0){
            tvNoFavorites.setVisibility(View.VISIBLE);
        }else{
            tvNoFavorites.setVisibility(View.GONE);
        }

        if(isInFavorites((selectedChannel)))
        {
            btFavorite.setImageResource(R.drawable.unfavorite);
        }else{
            btFavorite.setImageResource(R.drawable.favorite);
        }
    }
    private void initFavoriteData()
    {
        favoriteslList = DataFavorite.getFavorite(this);
        ListView lvFavorites = (ListView) findViewById(R.id.lvFavorites);
        btFavorite = (ImageButton) findViewById(R.id.btFavorite);
        tvNoFavorites = (TextView) findViewById(R.id.tvNoFavorites);
        if(favoriteslList == null || favoriteslList.size() == 0){
            tvNoFavorites.setVisibility(View.VISIBLE);
        }else{
            tvNoFavorites.setVisibility(View.GONE);
        }
        lvFavoritesAdapter = new FlexibleFavoriteRowAdapter(MainActivity.this, favoriteslList);
        lvFavorites.setAdapter(lvFavoritesAdapter);
        lvFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChannelItem channelItem = favoriteslList.get(position);
                if (channelItem != null) {
                    selectedChannel = channelItem;
                    if (mDrawerLayout.isDrawerOpen(mRightDrawerList)) {
                        mDrawerLayout.closeDrawer(mRightDrawerList);
                    } else {
                        mDrawerLayout.openDrawer(mRightDrawerList);
                    }
                    updateSchedules();
                }
            }
        });
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
        tvSelectedChannel.setText(selectedChannel.getName());
        if(isInFavorites((selectedChannel)))
        {
            btFavorite.setImageResource(R.drawable.unfavorite);
        }else{
            btFavorite.setImageResource(R.drawable.favorite);
        }
        btFavorite.setVisibility(View.VISIBLE);
        tvMessage.setVisibility(View.VISIBLE);
        tvMessage.setText("Đang tải dữ liệu...");
        lvSchedules.setVisibility(View.GONE);
        if(scheduleAsyncTask!=null)
        {
            scheduleAsyncTask.cancel(true);
        }
        if(searchProgramView.getVisibility() == View.VISIBLE)
        {
            searchProgramView.setVisibility(View.INVISIBLE);
        }
        if(alarmManagerView.getVisibility() == View.VISIBLE)
        {
            alarmManagerView.hideView();
        }
        //if(true) return;
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
                    selectedChannel = channelItem;
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
                        if (selectedDate.getDate() != dp.getDayOfMonth() || selectedDate.getMonth() != dp.getMonth() || selectedDate.getYear() != dp.getYear()) {
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

    public void btUnFavoriteClick(View view)
    {
        final ChannelItem channelItem = (ChannelItem)view.getTag();
        if(channelItem!=null){
            new AlertDialog.Builder(this)
                    .setTitle("Delete")
                    .setMessage("Do you really want to remove " +channelItem.getName()+ " ?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            removeFavorite(channelItem);
                        }})
                    .setNegativeButton(android.R.string.no, null).show();
        }
    }

    public void menuClick(View view) {
        switch (view.getId()){
            case R.id.btMenu:

                if (mDrawerLayout.isDrawerOpen(mRightDrawerList)) {
                    mDrawerLayout.closeDrawer(mRightDrawerList);
                }
                if (mDrawerLayout.isDrawerOpen(mLeftDrawerList)) {
                    mDrawerLayout.closeDrawer(mLeftDrawerList);
                } else {
                    mDrawerLayout.openDrawer(mLeftDrawerList);
                }
                break;
            case R.id.btSearch:
                if(alarmManagerView.getVisibility() == View.VISIBLE)
                {
                    alarmManagerView.hideView();
                }
                searchProgramView.setVisibility(View.VISIBLE);
                btFavorite.setVisibility(View.GONE);
                tvSelectedChannel.setText("Tìm Chương Trình");
                break;
            case R.id.btAlarms:
                if(searchProgramView.getVisibility() == View.VISIBLE)
                {
                    searchProgramView.setVisibility(View.INVISIBLE);
                }
                alarmManagerView.showAndUpdate();
                btFavorite.setVisibility(View.GONE);
                tvSelectedChannel.setText("Lịch Hẹn");
                break;
            case R.id.btFavorite:
                if(isInFavorites(selectedChannel))
                {
                    removeFavorite(selectedChannel);
                }else{
                    addFavorite(selectedChannel);
                }
                break;
            case R.id.btFavorites:
                if (mDrawerLayout.isDrawerOpen(mLeftDrawerList)) {
                    mDrawerLayout.closeDrawer(mLeftDrawerList);
                }
                if (mDrawerLayout.isDrawerOpen(mRightDrawerList)) {
                    mDrawerLayout.closeDrawer(mRightDrawerList);
                } else {
                    mDrawerLayout.openDrawer(mRightDrawerList);
                }
                break;

        }
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
            return parseJSONScheduleItems.getSchedules(selectedChannel.getId(), selectedDate);
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
                flexibleScheduleRowAdapter.updateData(scheduleItems);
                tvMessage.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(searchProgramView.getVisibility() == View.VISIBLE)
        {
            searchProgramView.setVisibility(View.INVISIBLE);
            tvSelectedChannel.setText(selectedChannel.getName());
            btFavorite.setVisibility(View.VISIBLE);
        }else if(alarmManagerView.getVisibility() == View.VISIBLE)
        {
            alarmManagerView.hideView();
            flexibleScheduleRowAdapter.updateUI();
            btFavorite.setVisibility(View.VISIBLE);
            tvSelectedChannel.setText(selectedChannel.getName());
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if(mAdView!=null) mAdView.destroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if(mAdView!=null) mAdView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showInterstitial();

    }

    private void showInterstitial()
    {
        /*long timenow = Calendar.getInstance().getTime().getTime();
        long maxTime = ((countShow*200000 ) + 200000);
        if(maxTime > 900000) maxTime = 900000;
        if(timeForRun > 0 && ((timenow - timeForRun) > maxTime))
        {
            if (interstitial == null) {
                interstitial = new InterstitialAd(this);
                interstitial.setAdUnitId(getResources().getString(R.string.interstitial_unit_id));
                interstitial.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        if(interstitial.isLoaded()){
                            interstitial.show();
                            timeForRun = Calendar.getInstance().getTime().getTime();
                            countShow++;
                        }
                    }

                    @Override
                    public void onAdClosed() {
                    }


                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                    }
                });
            }

            AdRequest adRequest_interstitial = new AdRequest.Builder().build();

            interstitial.loadAd(adRequest_interstitial);
        }*/
    }
}
