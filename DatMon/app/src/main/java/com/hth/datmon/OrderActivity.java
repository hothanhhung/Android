package com.hth.datmon;

import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.hth.data.ChannelGroup;
import com.hth.data.ChannelItem;
import com.hth.data.Customer;
import com.hth.data.Data;
import com.hth.data.OrderData;
import com.hth.data.OrderItem;
import com.hth.data.OrderedItem;
import com.hth.data.ServiceProcess;
import com.hth.service.Areas;
import com.hth.service.Desk;
import com.hth.service.MenuOrder;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {

    private GridView grvOrderItems;
    private ListView lvOrderedItems;
    private OrderItemGridViewAdapter adapterGrvOrderItems;
    private OrderedItemRowAdapter orderedItemRowAdapter;
    private DrawerLayout mDrawerLayout;
    private LinearLayout mLeftDrawerList;
    private LinearLayout mRightDrawerList;
    private Desk selectedDesk;
    private TextView tvSelectedDesk;
    SearchView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        grvOrderItems = (GridView) findViewById(R.id.grvOrderItems);
        lvOrderedItems = (ListView) findViewById(R.id.lvOrderedItems);
        tvSelectedDesk = (TextView) findViewById(R.id.tvSelectedDesk);
        search = (SearchView) findViewById(R.id.search);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(adapterGrvOrderItems!=null) {
                    adapterGrvOrderItems.filterData(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(adapterGrvOrderItems!=null) {
                    adapterGrvOrderItems.filterData(newText);
                }
                return false;
            }
        });
        search.requestFocusFromTouch();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLeftDrawerList = (LinearLayout) findViewById(R.id.leftNavdrawer);
        mRightDrawerList = (LinearLayout) findViewById(R.id.rightNavdrawer);

        loadOrderItems();
        displayList();
        loadOrderedItems(new OrderData());
    }

    private void loadOrderItems()
    {
        ArrayList<MenuOrder> orderItems = ServiceProcess.getMenuOrder();
        if(orderItems != null) {
            adapterGrvOrderItems = new OrderItemGridViewAdapter(
                    OrderActivity.this,
                    orderItems, getResources());
            grvOrderItems.setAdapter(adapterGrvOrderItems);
            grvOrderItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    OrderItem orderItem = (OrderItem) view.getTag();
                    if(orderItem.isOutOfStock())
                    {
                        Toast.makeText(OrderActivity.this, "Hết hàng" , Toast.LENGTH_LONG).show();
                    }else{
                        OrderedItem orderedItem = new OrderedItem(orderItem.getDetail(), 100000, 1);
                        orderedItemRowAdapter.addData(orderedItem);
                        mDrawerLayout.openDrawer(mRightDrawerList);
                    }
                }
            });
        }else{
            Toast.makeText(this, "No order item", Toast.LENGTH_LONG).show();
        }
    }
    OrderData orderData;
    private void loadOrderedItems(OrderData order) {
        this.orderData = order;
        orderedItemRowAdapter = new OrderedItemRowAdapter(
                OrderActivity.this,
                orderData.getOrderedItem(), getResources());
        lvOrderedItems.setAdapter(orderedItemRowAdapter);
        updateCustomerButtonText();
    }
    //method to expand all groups
    private void displayList() {
        ArrayList<Areas> areas = ServiceProcess.getAreas();
        ExpandableListView lvTables = (ExpandableListView) findViewById(R.id.lvTables);
        //create the adapter by passing your ArrayList data
        ChannelsExpandableListAdapter lvChannelsAdapter = new ChannelsExpandableListAdapter(OrderActivity.this, areas);
        //attach the adapter to the list
        lvTables.setAdapter(lvChannelsAdapter);
        lvTables.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                selectedDesk = (Desk) v.getTag();
                if (selectedDesk != null) {
                    tvSelectedDesk.setText(selectedDesk.getName());
                    if (mDrawerLayout.isDrawerOpen(mLeftDrawerList)) {
                        mDrawerLayout.closeDrawer(mLeftDrawerList);
                    } else {
                        mDrawerLayout.openDrawer(mLeftDrawerList);
                    }
                }
                return false;
            }
        });
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

    public void btInOrderViewClick(View view) {
        switch (view.getId()){
            case R.id.btCustomer:
                showPopup(OrderActivity.this);
                break;
            case R.id.btClear:
                loadOrderedItems(new OrderData());
                break;
            case R.id.btSaveCustomer:
                break;
            case R.id.btCancel:
                break;
            case R.id.btAddCustomer:
                if(orderedCustomerRowAdapter!=null){
                    Customer customer = (Customer) view.getTag();
                    orderedCustomerRowAdapter.addData(customer);
                }
                break;
        }
    }
    OrderedCustomerRowAdapter orderedCustomerRowAdapter;
    private void showPopup(final Activity context) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.customer_view);
        dialog.setTitle("KHÁCH HÀNG CỦA ĐƠN HÀNG");
        final LinearLayout llCustomerOfOrder = (LinearLayout)dialog.findViewById(R.id.llCustomerOfOrder);
        final LinearLayout llCustomers = (LinearLayout)dialog.findViewById(R.id.llCustomers);
        final LinearLayout llAddCustomer = (LinearLayout)dialog.findViewById(R.id.llAddCustomer);
        final ListView lvCustomers = (ListView)dialog.findViewById(R.id.lvCustomers);
        final ListView lvOrderedCustomers = (ListView)dialog.findViewById(R.id.lvOrderedCustomers);
        // set the custom dialog components - text, image and button
        orderedCustomerRowAdapter = new OrderedCustomerRowAdapter(
                OrderActivity.this,
                orderData.getCustomers(), getResources());
        lvOrderedCustomers.setAdapter(orderedCustomerRowAdapter);

        final CustomerRowAdapter customerRowAdapter = new CustomerRowAdapter(
                OrderActivity.this,
                Data.getCustomers(), getResources());
        lvCustomers.setAdapter(customerRowAdapter);

        llCustomerOfOrder.setVisibility(View.VISIBLE);
        llCustomers.setVisibility(View.GONE);
        llAddCustomer.setVisibility(View.GONE);

        Button btAddCustomerToOrder = (Button) dialog.findViewById(R.id.btAddCustomerToOrder);
        Button btBackToOrderedCustomers = (Button) dialog.findViewById(R.id.btBackToOrderedCustomers);
        Button btAddNewCustomer = (Button) dialog.findViewById(R.id.btAddNewCustomer);
        Button btSaveCustomer = (Button) dialog.findViewById(R.id.btSaveCustomer);
        Button btCancel = (Button) dialog.findViewById(R.id.btCancel);
        // if button is clicked, close the custom dialog
        btAddCustomerToOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llCustomerOfOrder.setVisibility(View.GONE);
                llCustomers.setVisibility(View.VISIBLE);
                llAddCustomer.setVisibility(View.GONE);
                dialog.setTitle("DANH SÁCH KHÁCH HÀNG");
            }
        });

        // if button is clicked, close the custom dialog
        btAddNewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llCustomerOfOrder.setVisibility(View.GONE);
                llCustomers.setVisibility(View.GONE);
                llAddCustomer.setVisibility(View.VISIBLE);
                dialog.setTitle("KHÁCH HÀNG MỚI");
            }
        });

        // if button is clicked, close the custom dialog
        btSaveCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llCustomerOfOrder.setVisibility(View.VISIBLE);
                llCustomers.setVisibility(View.GONE);
                llAddCustomer.setVisibility(View.GONE);
                dialog.setTitle("KHÁCH HÀNG CỦA ĐƠN HÀNG");
            }
        });
        btBackToOrderedCustomers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llCustomerOfOrder.setVisibility(View.VISIBLE);
                llCustomers.setVisibility(View.GONE);
                llAddCustomer.setVisibility(View.GONE);
                dialog.setTitle("KHÁCH HÀNG CỦA ĐƠN HÀNG");
            }
        });
        // if button is clicked, close the custom dialog
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llCustomerOfOrder.setVisibility(View.GONE);
                llCustomers.setVisibility(View.VISIBLE);
                llAddCustomer.setVisibility(View.GONE);
                dialog.setTitle("DANH SÁCH KHÁCH HÀNG");
            }
        });

        search=(SearchView) dialog.findViewById(R.id.searchCustomer);
        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                customerRowAdapter.filterData(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                customerRowAdapter.filterData(newText);
                return false;
            }
        });


        dialog.setOnDismissListener(new DialogInterface.OnDismissListener(){
            @Override
            public void onDismiss(DialogInterface dialog) {
                updateCustomerButtonText();
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.show();
    }

    private void updateCustomerButtonText()
    {
        Button btCustomer = (Button)this.findViewById(R.id.btCustomer);
        if(orderData!=null && btCustomer!=null){
            int number = orderData.getCustomers().size();
            btCustomer.setText("("+number+") Khách Hàng");
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        search.clearFocus();
        mDrawerLayout.requestFocus();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
