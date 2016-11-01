package com.hth.datmon;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.hth.data.ServiceProcess;
import com.hth.service.Areas;
import com.hth.service.Customer;
import com.hth.service.Desk;
import com.hth.service.MenuOrder;
import com.hth.service.Order;
import com.hth.service.OrderDetail;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity implements ICallBack {

    private GridView grvOrderItems;
    private ListView lvOrderedItems;
    private MenuOrderGridViewAdapter adapterGrvOrderItems;
    private OrderDetailRowAdapter orderDetailRowAdapter;
    private DrawerLayout mDrawerLayout;
    private LinearLayout mLeftDrawerList;
    private LinearLayout mRightDrawerList;
    private TextView tvSelectedDesk, tvTotalPrice;
    SearchView search;

    Order orderData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        grvOrderItems = (GridView) findViewById(R.id.grvOrderItems);
        lvOrderedItems = (ListView) findViewById(R.id.lvOrderedItems);
        tvSelectedDesk = (TextView) findViewById(R.id.tvSelectedDesk);
        tvTotalPrice = (TextView) findViewById(R.id.tvTotalPrice);
        search = (SearchView) findViewById(R.id.search);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (adapterGrvOrderItems != null) {
                    adapterGrvOrderItems.filterData(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapterGrvOrderItems != null) {
                    adapterGrvOrderItems.filterData(newText);
                }
                return false;
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLeftDrawerList = (LinearLayout) findViewById(R.id.leftNavdrawer);
        mRightDrawerList = (LinearLayout) findViewById(R.id.rightNavdrawer);

        loadOrderItems();
        displayList();
        loadOrderedItems(new Order());
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mDrawerLayout.openDrawer(mLeftDrawerList);
    }

    private void loadOrderItems()
    {
        ArrayList<MenuOrder> orderItems = ServiceProcess.getMenuOrder();
        if(orderItems != null) {
            adapterGrvOrderItems = new MenuOrderGridViewAdapter(
                    OrderActivity.this,
                    orderItems, getResources());
            grvOrderItems.setAdapter(adapterGrvOrderItems);
            grvOrderItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    MenuOrder menuOrder = (MenuOrder) view.getTag();
                    if(menuOrder.isOutOfStock())
                    {
                        Toast.makeText(OrderActivity.this, "Hết hàng" , Toast.LENGTH_LONG).show();
                    }else{
                        OrderDetail orderedItem = new OrderDetail(menuOrder, 1);
                        orderDetailRowAdapter.addData(orderedItem);
                        mDrawerLayout.openDrawer(mRightDrawerList);
                    }
                }
            });
        }else{
            Toast.makeText(this, "No order item", Toast.LENGTH_LONG).show();
        }
    }

    private void loadOrderedItems(Order order) {
        this.orderData = order;
        orderDetailRowAdapter = new OrderDetailRowAdapter(
                OrderActivity.this,
                orderData.getOrderDetails(), getResources(), this);
        lvOrderedItems.setAdapter(orderDetailRowAdapter);
        updateCustomerButtonText();
        updateOrderedView();
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
                orderData.setDesk((Desk) v.getTag());
                if (orderData.getDesk() != null) {
                    tvSelectedDesk.setText(orderData.getDesk().getFullName());
                    if (mDrawerLayout.isDrawerOpen(mLeftDrawerList)) {
                        mDrawerLayout.closeDrawer(mLeftDrawerList);
                    } else {
                        mDrawerLayout.openDrawer(mLeftDrawerList);
                    }
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                }
                return false;
            }
        });
    }

    private void updateOrderedView()
    {
        tvTotalPrice.setText(String.format("%,.0f", orderDetailRowAdapter.getTotal()));
    }
    public void menuClick(View view) {
        if(orderData.getDesk() == null) return;
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
                loadOrderedItems(new Order());
                break;
            case R.id.btSaveCustomer:
                break;
            case R.id.btCancel:
                break;
        }
    }
    OrderedCustomerRowAdapter orderedCustomerRowAdapter;
    private void showPopup(final Activity context) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.customer_view);
        dialog.setTitle("KHÁCH HÀNG CỦA ĐƠN HÀNG");
        //final LinearLayout llCustomerOfOrder = (LinearLayout)dialog.findViewById(R.id.llCustomerOfOrder);
        final LinearLayout llCustomers = (LinearLayout)dialog.findViewById(R.id.llCustomers);
        final LinearLayout llAddCustomer = (LinearLayout)dialog.findViewById(R.id.llAddCustomer);
        final ListView lvCustomers = (ListView)dialog.findViewById(R.id.lvCustomers);
        final ListView lvOrderedCustomers = (ListView)dialog.findViewById(R.id.lvOrderedCustomers);
        final EditText etUsername = (EditText)dialog.findViewById(R.id.etUsername);
        final EditText etAddress = (EditText)dialog.findViewById(R.id.etAddress);
        final EditText etPhoneNumber = (EditText)dialog.findViewById(R.id.etPhoneNumber);
        final EditText etEmail = (EditText)dialog.findViewById(R.id.etEmail);
        final EditText etJob = (EditText)dialog.findViewById(R.id.etJob);
        final EditText etBirthday = (EditText)dialog.findViewById(R.id.etBirthday);
        final EditText etFacebook = (EditText)dialog.findViewById(R.id.etFacebook);
        final EditText etZalo = (EditText)dialog.findViewById(R.id.etZalo);
        final EditText etCarNumber = (EditText)dialog.findViewById(R.id.etCarNumber);

        // set the custom dialog components - text, image and button
        orderedCustomerRowAdapter = new OrderedCustomerRowAdapter(
                OrderActivity.this,
                orderData.getCustomers(), getResources());
        lvOrderedCustomers.setAdapter(orderedCustomerRowAdapter);

        final CustomerRowAdapter customerRowAdapter = new CustomerRowAdapter(
                OrderActivity.this,
                ServiceProcess.getCustomers(), getResources());
        lvCustomers.setAdapter(customerRowAdapter);

        lvCustomers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Customer selectedCustomer = (Customer) view.getTag();
                orderData.setCustomer(selectedCustomer);
                etUsername.setText(selectedCustomer.getFirstName());
                etAddress.setText(selectedCustomer.getAddress());
                etPhoneNumber.setText(selectedCustomer.getPhoneNumber());
                etEmail.setText(selectedCustomer.getEmail());
                etJob.setText(selectedCustomer.getJob());
                etBirthday.setText(selectedCustomer.getBithDay());
                etFacebook.setText(selectedCustomer.getFacebook());
                etZalo.setText(selectedCustomer.getZalo());
                etCarNumber.setText(selectedCustomer.getCarNumber());
                llCustomers.setVisibility(View.GONE);
                llAddCustomer.setVisibility(View.VISIBLE);
            }
        });

        //llCustomerOfOrder.setVisibility(View.GONE);
        llCustomers.setVisibility(View.GONE);
        llAddCustomer.setVisibility(View.VISIBLE);

        Button btAddCustomerToOrder = (Button) dialog.findViewById(R.id.btAddCustomerToOrder);
        Button btBackToOrderedCustomers = (Button) dialog.findViewById(R.id.btBackToOrderedCustomers);
        Button btAddNewCustomer = (Button) dialog.findViewById(R.id.btAddNewCustomer);
        Button btSaveCustomer = (Button) dialog.findViewById(R.id.btSaveCustomer);
        Button btCancel = (Button) dialog.findViewById(R.id.btCancel);
        Button btResetCustomer = (Button)dialog.findViewById(R.id.btResetCustomer);
        Button btGetFromCustomersList = (Button)dialog.findViewById(R.id.btGetFromCustomersList);

        btResetCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUsername.setText("");
                etAddress.setText("");
                etPhoneNumber.setText("");
                etEmail.setText("");
                etJob.setText("");
                etBirthday.setText("");
                etFacebook.setText("");
                etZalo.setText("");
                etCarNumber.setText("");
            }
        });

        btGetFromCustomersList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //llCustomerOfOrder.setVisibility(View.GONE);
                llCustomers.setVisibility(View.VISIBLE);
                llAddCustomer.setVisibility(View.GONE);
                dialog.setTitle("DANH SÁCH KHÁCH HÀNG");
            }
        });

       /* btAddCustomerToOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llCustomerOfOrder.setVisibility(View.GONE);
                llCustomers.setVisibility(View.VISIBLE);
                llAddCustomer.setVisibility(View.GONE);
                dialog.setTitle("DANH SÁCH KHÁCH HÀNG");
            }
        });

        btAddNewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llCustomerOfOrder.setVisibility(View.GONE);
                llCustomers.setVisibility(View.GONE);
                llAddCustomer.setVisibility(View.VISIBLE);
                dialog.setTitle("KHÁCH HÀNG MỚI");
            }
        });
*/
        // if button is clicked, close the custom dialog
        btSaveCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Customer selectedCustomer = orderData.getCustomer();
                if(selectedCustomer == null)
                {
                    selectedCustomer = new Customer();
                }
                selectedCustomer.setFirstName(etUsername.getText().toString());
                selectedCustomer.setAddress(etAddress.getText().toString());
                selectedCustomer.setPhoneNumber(etPhoneNumber.getText().toString());
                selectedCustomer.setEmail(etEmail.getText().toString());
                selectedCustomer.setJob(etJob.getText().toString());
                selectedCustomer.setBithDay(etBirthday.getText().toString());
                selectedCustomer.setFacebook(etFacebook.getText().toString());
                selectedCustomer.setZalo(etZalo.getText().toString());
                selectedCustomer.setCarNumber(etCarNumber.getText().toString());
                Customer customer = ServiceProcess.saveCustomer(selectedCustomer);
                if(customer!=null){
                    orderData.setCustomer(customer);
                    dialog.dismiss();
                }else{
                    Toast.makeText(OrderActivity.this, "Không thể lưu dữ liệu", Toast.LENGTH_LONG).show();
                }
            }
        });
        btBackToOrderedCustomers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //llCustomerOfOrder.setVisibility(View.VISIBLE);
                llCustomers.setVisibility(View.GONE);
                llAddCustomer.setVisibility(View.VISIBLE);
                dialog.setTitle("KHÁCH HÀNG CỦA ĐƠN HÀNG");
            }
        });
        // if button is clicked, close the custom dialog
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
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

    private void updateCustomerButtonText() {
        Button btCustomer = (Button) this.findViewById(R.id.btCustomer);
        if (orderData != null && btCustomer != null) {
            int number = orderData.getCustomer() == null ? 0 : 1;
            btCustomer.setText("(" + number + ") Khách Hàng");
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onNumberPikerDialogSave() {

    }

    @Override
    public void onOrderItemGridViewChange() {
        updateOrderedView();
    }
}
