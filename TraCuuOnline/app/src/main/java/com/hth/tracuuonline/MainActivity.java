package com.hth.tracuuonline;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hth.data.MenuLookUpItem;
import com.hth.data.StaticData;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView tvSelectedMenuLookUpItem;
    ListView lvMenuLookUpItems;
    MenuLookUpItemAdapter menuLookUpItemAdapter;
    LinearLayout llMainContent;

    private DrawerLayout mDrawerLayout;
    private LinearLayout mLeftDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLeftDrawerList = (LinearLayout) findViewById(R.id.leftNavdrawer);

        llMainContent = (LinearLayout) findViewById(R.id.llMainContent);
        tvSelectedMenuLookUpItem = (TextView) findViewById(R.id.tvSelectedMenuLookUpItem);
        lvMenuLookUpItems = (ListView) findViewById(R.id.lvMenuLookUpItems);
        ArrayList<MenuLookUpItem> menuLookUpItems = StaticData.GetMenuLookUpItems();
        menuLookUpItemAdapter = new MenuLookUpItemAdapter(MainActivity.this, menuLookUpItems);

        lvMenuLookUpItems.setAdapter(menuLookUpItemAdapter);
        lvMenuLookUpItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenuLookUpItem menuLookUpItem = (MenuLookUpItem) view.getTag();
                if(menuLookUpItem.hasAction())
                {
                    tvSelectedMenuLookUpItem.setText(menuLookUpItem.getName());
                   // vwMainContent.ad
                    llMainContent.removeAllViews();
                    mDrawerLayout.closeDrawer(mLeftDrawerList);
                    llMainContent.addView(menuLookUpItem.getView(MainActivity.this), 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                }else{
                    Toast.makeText(MainActivity.this, "Not Implemented Yet", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void menuClick(View view) {
        switch (view.getId()){
            case R.id.btMenuLookUpItems:
                if (mDrawerLayout.isDrawerOpen(mLeftDrawerList)) {
                    mDrawerLayout.closeDrawer(mLeftDrawerList);
                } else {
                    mDrawerLayout.openDrawer(mLeftDrawerList);
                }
                break;
            case R.id.btMoreApp:

                break;

        }
    }
}
