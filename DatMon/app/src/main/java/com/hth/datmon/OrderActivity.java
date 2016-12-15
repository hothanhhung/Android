package com.hth.datmon;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hth.service.ChatUser;
import com.hth.data.MenuOption;
import com.hth.data.Photo;
import com.hth.data.ServiceProcess;
import com.hth.service.Areas;
import com.hth.service.ConstData;
import com.hth.service.Conversation;
import com.hth.service.Customer;
import com.hth.service.Desk;
import com.hth.service.ImageData;
import com.hth.service.MenuOrder;
import com.hth.service.Order;
import com.hth.service.OrderCustomer;
import com.hth.service.OrderDetail;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity implements ICallBack {

    final int SERVICE_PROCESS_GET_CHAT_USERS = 10;
    final int SERVICE_PROCESS_GET_MENU_ORDER = SERVICE_PROCESS_GET_CHAT_USERS + 1;
    final int SERVICE_PROCESS_GET_AREAS_ORDER_FROM_CACHE = SERVICE_PROCESS_GET_MENU_ORDER + 1;
    final int SERVICE_PROCESS_GET_AREAS_ORDER_FROM_CACHE_UPDATE_TABLE_LIST = SERVICE_PROCESS_GET_AREAS_ORDER_FROM_CACHE + 1;
    final int SERVICE_PROCESS_GET_AREAS_ORDER_FROM_CACHE_POPUP_CHANGE_DESK = SERVICE_PROCESS_GET_AREAS_ORDER_FROM_CACHE_UPDATE_TABLE_LIST + 1;
    final int SERVICE_PROCESS_CHANGE_DESK = SERVICE_PROCESS_GET_AREAS_ORDER_FROM_CACHE_POPUP_CHANGE_DESK +1;
    final int SERVICE_PROCESS_SAVE_CUSTOMER = SERVICE_PROCESS_CHANGE_DESK + 1;
    final int SERVICE_PROCESS_SAVE_ORDER_CUSTOMER = SERVICE_PROCESS_SAVE_CUSTOMER + 1;
    final int SERVICE_PROCESS_LIST_CUSTOMER_BY_ORDER = SERVICE_PROCESS_SAVE_ORDER_CUSTOMER + 1;
    final int SERVICE_PROCESS_GET_ORDER_BY_DESK_ID = SERVICE_PROCESS_LIST_CUSTOMER_BY_ORDER + 1;
    final int SERVICE_PROCESS_SAVE_ORDER = SERVICE_PROCESS_GET_ORDER_BY_DESK_ID + 1;
    final int SERVICE_PROCESS_REQUEST_PAYMENT = SERVICE_PROCESS_SAVE_ORDER + 1;
    final int SERVICE_PROCESS_GET_ALL_CONVERSATIONS = SERVICE_PROCESS_REQUEST_PAYMENT + 1;

    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_CAMERA = 1;

    private final Context mContext = this;
    private SignalRService mService;
    private boolean mBound = false;

    private boolean isDesksShouldUpdateData = false;
    private GridView grvOrderItems;
    private ExpandableListView lvOrderedItems;
    private MenuOrderGridViewAdapter adapterGrvOrderItems;
    private OrderDetailExpandableListAdapter orderDetailRowAdapter;
    private DrawerLayout mDrawerLayout;
    private LinearLayout mLeftDrawerList;
    private LinearLayout mRightDrawerList;
    private TextView tvSelectedDesk, tvTotalPrice;
    private Spinner spMenuOrderType;
    SearchView search;

    Order orderData;
    OrderCustomer orderCustomerData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        grvOrderItems = (GridView) findViewById(R.id.grvOrderItems);
        lvOrderedItems = (ExpandableListView) findViewById(R.id.lvOrderedItems);
        tvSelectedDesk = (TextView) findViewById(R.id.tvSelectedDesk);
        tvTotalPrice = (TextView) findViewById(R.id.tvTotalPrice);
        spMenuOrderType = (Spinner) findViewById(R.id.spMenuOrderType);
        search = (SearchView) findViewById(R.id.search);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (adapterGrvOrderItems != null) {
                    adapterGrvOrderItems.filterData(query, spMenuOrderType.getSelectedItemPosition());
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapterGrvOrderItems != null) {
                    adapterGrvOrderItems.filterData(newText, spMenuOrderType.getSelectedItemPosition());
                }
                return false;
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                new String[]{"Tất cả", "Món ăn", "Đồ uống", "Khác"});
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spMenuOrderType.setAdapter(adapter);
        spMenuOrderType.setSelection(0);
        spMenuOrderType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (adapterGrvOrderItems != null) {
                    adapterGrvOrderItems.filterData(search.getQuery()==null?"":search.getQuery().toString(), spMenuOrderType.getSelectedItemPosition());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLeftDrawerList = (LinearLayout) findViewById(R.id.leftNavdrawer);
        mRightDrawerList = (LinearLayout) findViewById(R.id.rightNavdrawer);

        (new PerformServiceProcessBackgroundTask()).execute(SERVICE_PROCESS_GET_MENU_ORDER);
        (new PerformServiceProcessBackgroundTask()).execute(SERVICE_PROCESS_GET_AREAS_ORDER_FROM_CACHE);
        //loadOrderedItems(new Order());
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        mDrawerLayout.openDrawer(mLeftDrawerList);
        mDrawerLayout.closeDrawer(mRightDrawerList);
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                if(mDrawerLayout.isDrawerOpen(mLeftDrawerList) && isDesksShouldUpdateData)
                {
                    (new PerformServiceProcessBackgroundTask()).execute(SERVICE_PROCESS_GET_AREAS_ORDER_FROM_CACHE);
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        updatebtChats(ServiceProcess.getLoginUser().countUsersUnread());

        Intent intent = new Intent();
        intent.setClass(mContext, SignalRService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);



        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstData.ACTION_RECEIVE_CONVERSTATION);
        filter.addAction(ConstData.ACTION_REQUEST_DESK_UPDATE);
        DatMonApp.getAppContext().registerReceiver(receiverChat, filter);
    }

    private final ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to SignalRService, cast the IBinder and get SignalRService instance
            SignalRService.LocalBinder binder = (SignalRService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

            Log.d("onServiceDisconnected", "onServiceDisconnected");
            mBound = false;
        }
    };


    private void loadOrderItems(final ArrayList<MenuOrder> orderItems)
    {
        if(orderItems != null) {
            adapterGrvOrderItems = new MenuOrderGridViewAdapter(
                    OrderActivity.this,
                    orderItems, getResources());
            grvOrderItems.setAdapter(adapterGrvOrderItems);
            grvOrderItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    if(orderData.isRequestingPayment()){
                        UIUtils.alert(OrderActivity.this, "Bàn đang được tính tiền", false);
                    }else {
                        MenuOrder menuOrder = (MenuOrder) view.getTag();
                        if (menuOrder.isOutOfStock()) {
                            Toast.makeText(OrderActivity.this, "Hết hàng", Toast.LENGTH_LONG).show();
                        } else {
                            OrderDetail orderedItem = new OrderDetail(menuOrder, 1);
                            orderDetailRowAdapter.addData(orderedItem);
                            mDrawerLayout.openDrawer(mRightDrawerList);
                        }
                    }
                }
            });
        }else{
            Toast.makeText(this, "No order item", Toast.LENGTH_LONG).show();
        }
    }

    private void loadOrderedItems() {
        //this.orderData = order;
        orderDetailRowAdapter = new OrderDetailExpandableListAdapter(
                OrderActivity.this,
                orderData.getOrderDetails(), this);
        lvOrderedItems.setAdapter(orderDetailRowAdapter);
        updateCustomerButtonText();
        updateOrderedView();
        updateUIBasedOnStatusOfOrder();
    }
    //method to expand all groups
    ChannelsExpandableListAdapter lvTablesAdapter;
    private void displayList(ArrayList<Areas> areas) {
        ExpandableListView lvTables = (ExpandableListView) findViewById(R.id.lvTables);
        //create the adapter by passing your ArrayList data
        lvTablesAdapter = new ChannelsExpandableListAdapter(OrderActivity.this, areas);
        //attach the adapter to the list
        lvTables.setAdapter(lvTablesAdapter);
        lvTables.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Desk desk = (Desk) v.getTag();
                (new PerformServiceProcessBackgroundTask()).execute(SERVICE_PROCESS_GET_ORDER_BY_DESK_ID, desk);
                return false;
            }
        });
    }

    private void getOrderByDeskIdResult(ArrayList<Object> objects)
    {
        if (objects == null || objects.size() <3 || objects.get(1) == null) {
            UIUtils.alert(OrderActivity.this, "Lỗi khi kết nối", true);
        } else {
            orderData = (Order) objects.get(1);
            orderData.setDesk((Desk) objects.get(0));
            tvSelectedDesk.setText(orderData.getDesk().getFullName());
            if (orderData.getDesk().IsUsing()) {
                tvSelectedDesk.setTextColor(Color.RED);
            } else {
                tvSelectedDesk.setTextColor(Color.GREEN);
            }
            if(!orderData.isNew())
            {
                orderCustomerData = ServiceProcess.getCustomerByOrder(orderData.getID());
            }else{
                orderCustomerData = new OrderCustomer();
                tvSelectedDesk.setTextColor(Color.GREEN);
            }

            if(objects.get(2) == null || (boolean)objects.get(2)) {
                mDrawerLayout.closeDrawer(mLeftDrawerList);
                mDrawerLayout.openDrawer(mRightDrawerList);
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
            loadOrderedItems();
        }
    }
    private void updateOrderedView()
    {
        tvTotalPrice.setText(String.format("%,.0f", orderDetailRowAdapter.getTotal()));
    }

    private void updatebtChats(int number)
    {
        Button btChats = (Button) findViewById(R.id.btChats);
        btChats.setText(number>0?""+number:"");
    }

    public void menuClick(View view) {
       if(view.getId() != R.id.btChats && (orderData == null || orderData.getDesk() == null)) return;
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

            case R.id.btChats:
                new PerformServiceProcessBackgroundTask().execute(SERVICE_PROCESS_GET_CHAT_USERS);
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
            case R.id.btRefresh:
                updateTableList();
                break;
            case R.id.btExit:
                Intent intent = new Intent(OrderActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;

        }
    }

    public void btInOrderViewClick(View view) {
        switch (view.getId()) {
            case R.id.btSendToCooker:
                (new PerformServiceProcessBackgroundTask()).execute(SERVICE_PROCESS_SAVE_ORDER);
                break;
            case R.id.btCustomer:
                (new PerformServiceProcessBackgroundTask()).execute(SERVICE_PROCESS_LIST_CUSTOMER_BY_ORDER);
                break;
            case R.id.btChangeTable:
                (new PerformServiceProcessBackgroundTask()).execute(SERVICE_PROCESS_GET_AREAS_ORDER_FROM_CACHE_POPUP_CHANGE_DESK);
                break;
            case R.id.btRequestPayment:
                (new PerformServiceProcessBackgroundTask()).execute(SERVICE_PROCESS_REQUEST_PAYMENT);
                break;
            case R.id.btClear:
                if(orderData!=null && orderData.isNew()) {
                    orderData.clearOrderDetail();
                    loadOrderedItems();
                }else{
                    UIUtils.alert(OrderActivity.this, "Không thể hủy bỏ đơn đặt hàng đã gửi", true);
                }
                break;
            case R.id.btSaveCustomer:
                break;
            case R.id.btCancel:
                break;
        }
    }

    private void saveOrderResult(Order order)
    {
        if (order == null) {
            UIUtils.alert(OrderActivity.this, "Có lỗi khi gửi thông tin", true);
        } else {
            UIUtils.alert(OrderActivity.this, "Thông tin gửi thành công", false);
            // if(orderData.isNew())
            {
                updateTableList();
            }
            orderData = order;
            orderData.getDesk().setUsing();
            if (orderData.getDesk().IsUsing()) {
                tvSelectedDesk.setTextColor(Color.RED);
            } else {
                tvSelectedDesk.setTextColor(Color.GREEN);
            }
            orderDetailRowAdapter.updateData(orderData.getOrderDetails());
            updateCustomerButtonText();
        }
    }

    File cameraPhotoFile = null;
    public void btCamera_Click(View paramView) {
        try {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            cameraPhotoFile = File.createTempFile("" + MethodsHelper.getCurrentDateToOrder(), ".jpg", MemoryHelper.getCacheFolder(OrderActivity.this));
            cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraPhotoFile));
            startActivityForResult(cameraIntent, PICK_FROM_CAMERA);
        } catch (Exception ex) {
            ex.printStackTrace();
            //display an error message
            String errorMessage = "Thiết bị của bạn không hổ trợ chụp ảnh!";
            UIUtils.alert(OrderActivity.this, errorMessage, true);
        }
    }
    public void btUploadAvatar_Click(View paramView) {
        if(PhotoManager.hasPhoto()) {
            Photo photo = PhotoManager.getPhoto();
            ImageData imageData = new ImageData(photo.getName(), photo.GetDataInString());
            String path = ServiceProcess.saveImage(imageData);
            if (path != null && !path.trim().isEmpty()) {
                if (selectedCustomer == null) {
                    selectedCustomer = new Customer();
                }
                selectedCustomer.setImage(path);
                ((Button)dialogCustomer.findViewById(R.id.btUploadAvatar)).setVisibility(View.GONE);
            } else {
                UIUtils.alert(OrderActivity.this, "Không thể upload ảnh", true);
            }
        }else{
            UIUtils.alert(OrderActivity.this, "Không dữ liệu hình ảnh", true);
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_FROM_CAMERA:
                    if (cameraPhotoFile != null) {
                        PhotoManager.setPhoto(new Photo(Uri.fromFile(cameraPhotoFile), cameraPhotoFile.getName()));
                        doCrop();
                    }
                    ((ImageView) dialogCustomer.findViewById(R.id.imgAvatar)).setImageBitmap(PhotoManager.getPhoto().getSmallPhoto());
                    ((Button)dialogCustomer.findViewById(R.id.btUploadAvatar)).setVisibility(View.VISIBLE);
                    break;
                case CROP_FROM_CAMERA:
                    if (cameraPhotoFile != null) {
                        cameraPhotoFile.delete();
                        cameraPhotoFile = null;
                    }
                    Bundle localBundle = data.getExtras();
                    if (localBundle == null)
                        break;
                    PhotoManager.getPhoto().cropImage((Bitmap) localBundle.getParcelable("data"));
                    ((ImageView) dialogCustomer.findViewById(R.id.imgAvatar)).setImageBitmap(PhotoManager.getPhoto().getSmallPhoto());
                    ((Button)dialogCustomer.findViewById(R.id.btUploadAvatar)).setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    private void doCrop() {
        final ArrayList cropOptions = new ArrayList();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        List<ResolveInfo> resolveInfos = getPackageManager().queryIntentActivities(intent, 0);
        int size = resolveInfos.size();
        if (size != 0) {
            intent.setData(PhotoManager.getPhoto().getUri());
            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);
            if (size != 1) {
                for (ResolveInfo res : resolveInfos) {
                    MenuOption localMenuOption = new MenuOption();

                    localMenuOption.title = getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                    localMenuOption.icon = getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                    localMenuOption.appIntent = new Intent(intent);
                    localMenuOption.appIntent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                    cropOptions.add(localMenuOption);
                }

                MenuOptionAdapter adapter = new MenuOptionAdapter(getApplicationContext(), cropOptions);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Choose Crop App");
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        startActivityForResult(((MenuOption) cropOptions.get(item)).appIntent, CROP_FROM_CAMERA);
                    }
                });

                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                Intent i = new Intent(intent);
                ResolveInfo res = resolveInfos.get(0);
                i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                startActivityForResult(i, CROP_FROM_CAMERA);
            }
        } else {
            UIUtils.alert(OrderActivity.this, "Không tìm thấy ứng dụng để crop ảnh", true);
        }
    }

    void updateTableList()
    {
        (new PerformServiceProcessBackgroundTask()).execute(SERVICE_PROCESS_GET_AREAS_ORDER_FROM_CACHE_UPDATE_TABLE_LIST);
    }

    void updateUIBasedOnStatusOfOrder()
    {
        if(orderData.isRequestingPayment())
        {
            mRightDrawerList.setEnabled(false);
            mRightDrawerList.setClickable(false);
        }else{
            mRightDrawerList.setEnabled(true);
            mRightDrawerList.setClickable(true);
        }
    }

    OrderedCustomerRowAdapter orderedCustomerRowAdapter;
    Customer selectedCustomer;
    Dialog dialogCustomer;
    private void showPopupCustomer(final Activity context, ArrayList<Customer> customers) {
        if(orderCustomerData == null)
        {
            orderCustomerData = new OrderCustomer();
        }
        selectedCustomer = orderCustomerData.getCustomer();
        dialogCustomer = new Dialog(context);
        dialogCustomer.setContentView(R.layout.customer_view);
        dialogCustomer.setTitle("KHÁCH HÀNG CỦA ĐƠN HÀNG");
        //final LinearLayout llCustomerOfOrder = (LinearLayout)dialog.findViewById(R.id.llCustomerOfOrder);
        final LinearLayout llCustomers = (LinearLayout)dialogCustomer.findViewById(R.id.llCustomers);
        final LinearLayout llAddCustomer = (LinearLayout)dialogCustomer.findViewById(R.id.llAddCustomer);
        final ListView lvCustomers = (ListView)dialogCustomer.findViewById(R.id.lvCustomers);
        final ListView lvOrderedCustomers = (ListView)dialogCustomer.findViewById(R.id.lvOrderedCustomers);
        final EditText etUsername = (EditText)dialogCustomer.findViewById(R.id.etUsername);
        final EditText etAddress = (EditText)dialogCustomer.findViewById(R.id.etAddress);
        final EditText etPhoneNumber = (EditText)dialogCustomer.findViewById(R.id.etPhoneNumber);
        final EditText etEmail = (EditText)dialogCustomer.findViewById(R.id.etEmail);
        final EditText etJob = (EditText)dialogCustomer.findViewById(R.id.etJob);
        final EditText etBirthday = (EditText)dialogCustomer.findViewById(R.id.etBirthday);
        final EditText etFacebook = (EditText)dialogCustomer.findViewById(R.id.etFacebook);
        final EditText etZalo = (EditText)dialogCustomer.findViewById(R.id.etZalo);
        final EditText etCarNumber = (EditText)dialogCustomer.findViewById(R.id.etCarNumber);

        final ImageView imgAvatar = (ImageView)dialogCustomer.findViewById(R.id.imgAvatar);
        final Button btUploadAvatar = (Button)dialogCustomer.findViewById(R.id.btUploadAvatar);

        btUploadAvatar.setVisibility(View.GONE);

        // set the custom dialog components - text, image and button
        orderedCustomerRowAdapter = new OrderedCustomerRowAdapter(
                OrderActivity.this,
                orderData.getCustomers(), getResources());
        lvOrderedCustomers.setAdapter(orderedCustomerRowAdapter);

        final CustomerRowAdapter customerRowAdapter = new CustomerRowAdapter(
                OrderActivity.this,
                customers, getResources());
        lvCustomers.setAdapter(customerRowAdapter);

        lvCustomers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCustomer = (Customer) view.getTag();
                etUsername.setText(selectedCustomer.getFirstName());
                etAddress.setText(selectedCustomer.getAddress());
                etPhoneNumber.setText(selectedCustomer.getPhoneNumber());
                etEmail.setText(selectedCustomer.getEmail());
                etJob.setText(selectedCustomer.getJob());
                etBirthday.setText(selectedCustomer.getBithDay());
                etFacebook.setText(selectedCustomer.getFacebook());
                etZalo.setText(selectedCustomer.getZalo());
                etCarNumber.setText(selectedCustomer.getCarNumber());
                if(selectedCustomer.hasImage()) {
                    Picasso.with(context)
                            .load(selectedCustomer.getFullImage())
                            .into(imgAvatar);
                }
                btUploadAvatar.setVisibility(View.GONE);
                llCustomers.setVisibility(View.GONE);
                llAddCustomer.setVisibility(View.VISIBLE);
            }
        });

        //llCustomerOfOrder.setVisibility(View.GONE);
        llCustomers.setVisibility(View.GONE);
        llAddCustomer.setVisibility(View.VISIBLE);

        Button btAddCustomerToOrder = (Button) dialogCustomer.findViewById(R.id.btAddCustomerToOrder);
        Button btBackToOrderedCustomers = (Button) dialogCustomer.findViewById(R.id.btBackToOrderedCustomers);
        Button btAddNewCustomer = (Button) dialogCustomer.findViewById(R.id.btAddNewCustomer);
        Button btSaveCustomer = (Button) dialogCustomer.findViewById(R.id.btSaveCustomer);
        Button btCancel = (Button) dialogCustomer.findViewById(R.id.btCancel);
        Button btResetCustomer = (Button)dialogCustomer.findViewById(R.id.btResetCustomer);
        Button btGetFromCustomersList = (Button)dialogCustomer.findViewById(R.id.btGetFromCustomersList);

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
                Picasso.with(context)
                        .load(R.drawable.avatar)
                        .into(imgAvatar);
                selectedCustomer = null;
            }
        });

        btGetFromCustomersList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //llCustomerOfOrder.setVisibility(View.GONE);
                llCustomers.setVisibility(View.VISIBLE);
                llAddCustomer.setVisibility(View.GONE);
                dialogCustomer.setTitle("DANH SÁCH KHÁCH HÀNG");
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
                (new PerformServiceProcessBackgroundTask()).execute(SERVICE_PROCESS_SAVE_CUSTOMER, selectedCustomer);
                dialogCustomer.dismiss();

            }
        });
        btBackToOrderedCustomers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //llCustomerOfOrder.setVisibility(View.VISIBLE);
                llCustomers.setVisibility(View.GONE);
                llAddCustomer.setVisibility(View.VISIBLE);
                dialogCustomer.setTitle("KHÁCH HÀNG CỦA ĐƠN HÀNG");
            }
        });
        // if button is clicked, close the custom dialog
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCustomer.dismiss();
            }
        });

        search=(SearchView) dialogCustomer.findViewById(R.id.searchCustomer);
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


        if(selectedCustomer!=null) {
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
            if(selectedCustomer.hasImage()) {
                Picasso.with(OrderActivity.this).load(selectedCustomer.getFullImage()).into(imgAvatar);
            }
        }
        dialogCustomer.setOnDismissListener(new DialogInterface.OnDismissListener(){
            @Override
            public void onDismiss(DialogInterface dialog) {
                updateCustomerButtonText();
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialogCustomer.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        dialogCustomer.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialogCustomer.show();
    }
    private void saveCustomerResult(Customer customer) {
        if(customer!=null){
            orderCustomerData.setOrderId(orderData.getID());
            orderCustomerData.setCustomerId(customer.getID());
            (new PerformServiceProcessBackgroundTask()).execute(SERVICE_PROCESS_SAVE_ORDER_CUSTOMER, orderCustomerData);
        }else{
            UIUtils.alert(OrderActivity.this, "Không thể lưu dữ liệu", true);
        }
    }
    private void showPopupChangeDesk(final Activity context, ArrayList<Areas> areas) {
        selectedCustomer = orderData.getCustomer();
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.change_desk_layout);
        dialog.setTitle("Chọn bàn chuyển đến");

        Button btCancel = (Button) dialog.findViewById(R.id.btCancel);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ExpandableListView lvTables = (ExpandableListView) dialog.findViewById(R.id.lvTables);
        //create the adapter by passing your ArrayList data
        ChannelsExpandableListAdapter lvChannelsAdapter = new ChannelsExpandableListAdapter(OrderActivity.this, areas);
        //attach the adapter to the list
        lvTables.setAdapter(lvChannelsAdapter);
        lvTables.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                final Desk desk = (Desk) v.getTag();
                if (desk.IsUsing()) {
                    UIUtils.alert(context, "Bàn này đang được sử dụng", false);
                } else {
                    new AlertDialog.Builder(OrderActivity.this)
                            .setTitle("Xác nhận chuyển bàn")
                            .setMessage("Bạn thật sự muốn chuyển đến "+desk.getFullName() +" ?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("Chấp nhận", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dlg, int whichButton) {
                                    orderData.setDesk(desk);
                                    (new PerformServiceProcessBackgroundTask()).execute(SERVICE_PROCESS_CHANGE_DESK);
                                    dialog.dismiss();
                                }})
                            .setNegativeButton("Bỏ qua", null).show();
                }
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

    private void changeDeskResult(Order order){
        if(order == null){
            UIUtils.alert(OrderActivity.this, "Lỗi khi đổi bàn", true);
        }else{
            orderData = order;
            tvSelectedDesk.setText(orderData.getDesk().getFullName());
            if(orderData.getDesk().IsUsing()){
                tvSelectedDesk.setTextColor(Color.RED);
            }else{
                tvSelectedDesk.setTextColor(Color.GREEN);
            }
            orderDetailRowAdapter.updateData(orderData.getOrderDetails());
            updateTableList();
        }
    }

    Dialog popupChatUsersDialog;
    private void showPopupChatUsers(ArrayList<ChatUser> chatUsers)
    {
        if(popupChatUsersDialog != null && popupChatUsersDialog.isShowing())
        {
            popupChatUsersDialog.dismiss();
        }
        popupChatUsersDialog= new Dialog(OrderActivity.this);
        popupChatUsersDialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        popupChatUsersDialog.setContentView(R.layout.popup_chat_users);

        ListView lvChatUsers = (ListView)popupChatUsersDialog.findViewById(R.id.lvChatUsers);
        ChatUserRowAdapter chatUserRowAdapter = new ChatUserRowAdapter(OrderActivity.this, chatUsers, getResources(), currentComingUser);
        lvChatUsers.setAdapter(chatUserRowAdapter);
        lvChatUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChatUser toUser = (ChatUser) view.getTag();
                (new PerformServiceProcessBackgroundTask()).execute(SERVICE_PROCESS_GET_ALL_CONVERSATIONS, toUser);
                popupChatUsersDialog.dismiss();
            }
        });
        popupChatUsersDialog.setCancelable(true);
        popupChatUsersDialog.setCanceledOnTouchOutside(true);
        popupChatUsersDialog.setOwnerActivity(OrderActivity.this);

        popupChatUsersDialog.show();
    }

    Dialog popupChatToUserDialog;
    ChatView currentChatView;
    private void showPopupChatToUser(ChatUser toUser, ArrayList<Conversation> conversations)
    {
        if(popupChatToUserDialog != null && popupChatToUserDialog.isShowing())
        {
            popupChatToUserDialog.dismiss();
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OrderActivity.this);

        currentChatView = new ChatView(OrderActivity.this, ServiceProcess.getLoginUser(), toUser, conversations);
        alertDialogBuilder.setView(currentChatView);

        alertDialogBuilder.setCancelable(true);
        popupChatToUserDialog = alertDialogBuilder.create();
        popupChatToUserDialog.setOnDismissListener(new DialogInterface.OnDismissListener(){
            @Override
            public void onDismiss(DialogInterface dialog) {
                currentChatView = null;
            }
        });
        popupChatToUserDialog.show();
        currentComingUser.remove(toUser.getUserId());
        updatebtChats(currentComingUser.size());
    }

    private void updateCustomerButtonText() {
        Button btCustomer = (Button) this.findViewById(R.id.btCustomer);
        Button btChangeTable = (Button) this.findViewById(R.id.btChangeTable);
        if (orderCustomerData != null && btCustomer != null) {
            int number = orderCustomerData.isNew()? 0 : 1;
            btCustomer.setText("(" + number + ") Khách Hàng");
        }
        if(!orderData.isNew() && !orderData.isRequestingPayment())
        {
            btCustomer.setVisibility(View.VISIBLE);
            btChangeTable.setVisibility(View.VISIBLE);
        }else{
            btCustomer.setVisibility(View.INVISIBLE);
            btChangeTable.setVisibility(View.INVISIBLE);
        }

        if(orderData.isRequestingPayment())
        {
            ((Button) this.findViewById(R.id.btClear)).setVisibility(View.INVISIBLE);
            ((Button) this.findViewById(R.id.btSendToCooker)).setVisibility(View.INVISIBLE);
        }else{
            ((Button) this.findViewById(R.id.btRequestPayment)).setVisibility(View.VISIBLE);
            ((Button) this.findViewById(R.id.btClear)).setVisibility(View.VISIBLE);
            ((Button) this.findViewById(R.id.btSendToCooker)).setVisibility(View.VISIBLE);
        }
        if(orderData.isNew() || orderData.isRequestingPayment()){
            ((Button) this.findViewById(R.id.btRequestPayment)).setVisibility(View.INVISIBLE);
        }else{
            ((Button) this.findViewById(R.id.btRequestPayment)).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        Log.d("DatMon", "onResume");
        super.onResume();
        if(orderData == null)
        {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
            mDrawerLayout.openDrawer(mLeftDrawerList);
            mDrawerLayout.closeDrawer(mRightDrawerList);
        }
    }


    @Override
    protected void onDestroy() {
        // Unbind from the service
        Log.d("DatMon", "onDestroy");
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
        //DatMonApp.getAppContext().unregisterReceiver(receiverChat);
        super.onStop();
    }

    @Override
    public void onNumberPikerDialogSave() {

    }

    @Override
    public void onOrderItemGridViewChange() {
        updateOrderedView();
    }

    public class PerformServiceProcessBackgroundTask extends AsyncTask< Object, Object, Object >
    {
        private ProgressDialog loadingDialog = new ProgressDialog(OrderActivity.this);
        private int type;

        protected void onPreExecute()
        {
            loadingDialog.setCancelable(false);
            loadingDialog.setCanceledOnTouchOutside(false);
            loadingDialog.setTitle("Processing");
            loadingDialog.setMessage("Please wait");
            loadingDialog.show();
        }

        protected Object doInBackground(Object... params)
        {
            type = Integer.parseInt(params[0].toString());
            switch (type){
                case SERVICE_PROCESS_GET_CHAT_USERS:
                    return ServiceProcess.getChatUsers();
                case SERVICE_PROCESS_GET_MENU_ORDER:
                    return ServiceProcess.getMenuOrder();
                case SERVICE_PROCESS_GET_AREAS_ORDER_FROM_CACHE:
                    return ServiceProcess.getAreasFromCache(true);
                case SERVICE_PROCESS_GET_AREAS_ORDER_FROM_CACHE_UPDATE_TABLE_LIST:
                    return ServiceProcess.getAreasFromCache(true);
                case  SERVICE_PROCESS_GET_AREAS_ORDER_FROM_CACHE_POPUP_CHANGE_DESK:
                    return ServiceProcess.getAreasFromCache(true);
                case SERVICE_PROCESS_CHANGE_DESK:
                    return ServiceProcess.changeDesk(orderData);
                case SERVICE_PROCESS_SAVE_CUSTOMER:
                    return ServiceProcess.saveCustomer((Customer) params[1]);
                case SERVICE_PROCESS_SAVE_ORDER_CUSTOMER:
                    return ServiceProcess.saveOrderCustomer((OrderCustomer) params[1]);
                case SERVICE_PROCESS_LIST_CUSTOMER_BY_ORDER:
                    return ServiceProcess.getListCustomerByOrderExcept(orderData.getID());
                case SERVICE_PROCESS_GET_ORDER_BY_DESK_ID:
                    ArrayList<Object> deskAndOrder = new ArrayList<Object>();
                    Desk desk = (Desk) params[1];
                    deskAndOrder.add(desk);
                    deskAndOrder.add(ServiceProcess.getOrderByDeskId(desk.getID()));
                    if(params.length > 2){
                        deskAndOrder.add(params[2]);
                    }else{
                        deskAndOrder.add(true);
                    }
                    return deskAndOrder;
                case SERVICE_PROCESS_SAVE_ORDER:
                    return ServiceProcess.saveOrder(orderData);
                case SERVICE_PROCESS_REQUEST_PAYMENT:
                    return ServiceProcess.requestPayment(orderData.getID());
                case SERVICE_PROCESS_GET_ALL_CONVERSATIONS:
                    ArrayList<Object> toUserAndConversations = new ArrayList<Object>();
                    ChatUser toUser = (ChatUser) params[1];
                    toUserAndConversations.add(toUser);
                    toUserAndConversations.add(ServiceProcess.getConversationsWithUser(toUser.getUserId()));
                    return toUserAndConversations;
            }
            return null;
        }

        protected void onPostExecute(Object object)
        {
            loadingDialog.dismiss();
            switch (type){
                case SERVICE_PROCESS_GET_CHAT_USERS:
                    if(object == null) {
                        UIUtils.alert(OrderActivity.this, "Lỗi khi lấy dữ liệu", true);
                    }
                    else{
                        showPopupChatUsers((ArrayList<ChatUser>)object);
                    }
                    break;
                case SERVICE_PROCESS_GET_MENU_ORDER:
                    loadOrderItems((ArrayList<MenuOrder>) object);
                    break;
                case SERVICE_PROCESS_GET_AREAS_ORDER_FROM_CACHE:
                    displayList((ArrayList<Areas>) object);
                    isDesksShouldUpdateData = false;
                    break;
                case SERVICE_PROCESS_GET_AREAS_ORDER_FROM_CACHE_UPDATE_TABLE_LIST:
                    lvTablesAdapter.updateData((ArrayList<Areas>) object);
                    break;
                case  SERVICE_PROCESS_GET_AREAS_ORDER_FROM_CACHE_POPUP_CHANGE_DESK:
                    if(object == null) {
                        UIUtils.alert(OrderActivity.this, "Lỗi khi lấy dữ liệu", true);
                    }
                    else{

                        showPopupChangeDesk(OrderActivity.this, (ArrayList<Areas>) object);
                    }
                    break;
                case SERVICE_PROCESS_CHANGE_DESK:
                    changeDeskResult((Order)object);
                    break;
                case SERVICE_PROCESS_SAVE_CUSTOMER:
                    saveCustomerResult((Customer)object);
                    break;
                case SERVICE_PROCESS_SAVE_ORDER_CUSTOMER:
                    if(object!=null){
                        orderCustomerData = (OrderCustomer) object;
                        updateCustomerButtonText();
                    }else{
                        UIUtils.alert(OrderActivity.this, "Không thể lưu dữ liệu", true);
                    };
                    break;
                case SERVICE_PROCESS_LIST_CUSTOMER_BY_ORDER:
                    if(object!=null){
                        showPopupCustomer(OrderActivity.this, (ArrayList<Customer>) object);
                    }else{
                        UIUtils.alert(OrderActivity.this, "Không thể lấy dữ liệu", true);
                    };
                    break;
                case SERVICE_PROCESS_GET_ORDER_BY_DESK_ID:
                    getOrderByDeskIdResult((ArrayList<Object>) object);
                    break;
                case SERVICE_PROCESS_SAVE_ORDER:
                    saveOrderResult((Order) object);
                    break;
                case SERVICE_PROCESS_REQUEST_PAYMENT:
                    if(object!=null && Boolean.valueOf(object.toString()))
                    {
                        orderData.setIsRequestingPayment(true);
                        updateUIBasedOnStatusOfOrder();
                        updateCustomerButtonText();
                        UIUtils.alert(OrderActivity.this, "Yêu cầu tính tiền đã được gửi", false);
                    }else{
                        UIUtils.alert(OrderActivity.this, "Lỗi khi yêu cầu tính tiền", true);
                    }
                    break;
                case SERVICE_PROCESS_GET_ALL_CONVERSATIONS:
                    ArrayList<Object> objects = (ArrayList<Object>)object;
                    showPopupChatToUser((ChatUser) objects.get(0), (ArrayList<Conversation>) objects.get(1));
                    break;
            }

        }

    }

    ArrayList<String> currentComingUser = new ArrayList<>();
    private BroadcastReceiver receiverChat = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // your code here
            /*long[] dataVibrate = {500,500,1000,1000};
            Vibrator vibrator = (Vibrator) DatMonApp.getAppContext().getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(dataVibrate, -1);*/

            if(ConstData.ACTION_RECEIVE_CONVERSTATION.equalsIgnoreCase(intent.getAction())) {
                String fromUserId = intent.getStringExtra(ConstData.RECEIVE_CONVERSTATION_FROM_USER_ID);
                String toUserId = intent.getStringExtra(ConstData.RECEIVE_CONVERSTATION_TO_USER_ID);
                String message = intent.getStringExtra(ConstData.RECEIVE_CONVERSTATION_MESSAGE);
                String createdDate = intent.getStringExtra(ConstData.RECEIVE_CONVERSTATION_CREATE_TIME);
                ChatUser userLogin = ServiceProcess.getLoginUser();
                if (userLogin.getUserId().equalsIgnoreCase(fromUserId) || userLogin.getUserId().equalsIgnoreCase(toUserId)) {
                    if (currentChatView != null) {
                        currentChatView.addConversation(new Conversation(fromUserId, toUserId, message, createdDate));
                    }

                    if (currentChatView == null || !(currentChatView.isCurrentChat(fromUserId))) {
                        if (currentComingUser.indexOf(fromUserId) == -1) {
                            currentComingUser.add(fromUserId);
                            updatebtChats(currentComingUser.size());
                            long[] dataVibrate = {500, 1000};
                            Vibrator vibrator = (Vibrator) DatMonApp.getAppContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(dataVibrate, -1);
                        }
                    }
                }
            }else if(ConstData.ACTION_REQUEST_DESK_UPDATE.equalsIgnoreCase(intent.getAction())) {
                //(new PerformServiceProcessBackgroundTask()).execute(SERVICE_PROCESS_GET_AREAS_ORDER_FROM_CACHE);
                isDesksShouldUpdateData = true;
                String deskIds = intent.getStringExtra(ConstData.RECEIVE_REQUEST_DESK_UPDATE_DESK_ID);
                if(deskIds!=null && orderData != null && orderData.getDesk() != null && deskIds.contains(orderData.getDesk().getID()))
                {
                    (new PerformServiceProcessBackgroundTask()).execute(SERVICE_PROCESS_GET_ORDER_BY_DESK_ID, orderData.getDesk(), false);
                }
            }
        }

    };
}
