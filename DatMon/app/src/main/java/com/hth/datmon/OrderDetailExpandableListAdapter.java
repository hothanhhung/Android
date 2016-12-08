package com.hth.datmon;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hth.data.ChannelGroup;
import com.hth.data.ChannelItem;
import com.hth.service.Areas;
import com.hth.service.Desk;
import com.hth.service.Order;
import com.hth.service.OrderDetail;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;

import static android.view.View.GONE;

/**
 * Created by Lenovo on 8/26/2016.
 */
public class OrderDetailExpandableListAdapter extends BaseExpandableListAdapter implements ICallBack{
    private Context context;
    private ArrayList<OrderDetail> orderDetails;
    private HashMap<String, ArrayList<OrderDetail>> groupOrderDetails;
    private ICallBack callBack;

    private void excuteGroupOrderDetails()
    {
        groupOrderDetails = new HashMap<>();
        for (OrderDetail orderDetail:orderDetails) {
            if(!groupOrderDetails.containsKey(orderDetail.getMenuOrderID()))
            {
                groupOrderDetails.put(orderDetail.getMenuOrderID(), new ArrayList<OrderDetail>());
            }
            groupOrderDetails.get(orderDetail.getMenuOrderID()).add(orderDetail);
        }
    }

    public OrderDetailExpandableListAdapter(Context context, ArrayList<OrderDetail> orderDetails, ICallBack callBack) {
        this.context = context;
        this.orderDetails = orderDetails;
        this.callBack = callBack;
        excuteGroupOrderDetails();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String key = groupOrderDetails.keySet().toArray()[groupPosition].toString();
        return groupOrderDetails.get(key).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View view, ViewGroup parent) {

        EditText etQuantity;
        TextView tvTotal;
        TextView tvDetail;
        TextView tvStatus;
        ImageButton btRemove;

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.ordered_item_row, null);
        }
        etQuantity = (EditText) view.findViewById(R.id.etQuantity);
        tvTotal = (TextView) view.findViewById(R.id.tvTotal);
        tvDetail = (TextView) view.findViewById(R.id.tvDetail);
        tvStatus = (TextView)  view.findViewById(R.id.tvStatus);
        btRemove = (ImageButton) view.findViewById(R.id.btRemove);

        btRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final OrderDetail orderedItem = (OrderDetail) v.getTag();
                new AlertDialog.Builder(context)
                        .setTitle("Xác nhận")
                        .setMessage("Bạn thật sự muốn xóa món "+orderedItem.getName() +" ?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Chấp nhận", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                orderDetails.remove(orderedItem);
                                excuteGroupOrderDetails();
                                notifyDataSetChanged();
                                callBack.onOrderItemGridViewChange();
                                dialog.dismiss();
                            }})
                        .setNegativeButton("Bỏ qua", null).show();

            }
        });


        OrderDetail orderedItem = (OrderDetail)getChild(groupPosition, childPosition);
        btRemove.setTag(orderedItem);
        etQuantity.setTag(orderedItem);
        if(orderedItem.getStatus() != 2){
            btRemove.setVisibility(View.VISIBLE);
        }else{
            btRemove.setVisibility(View.INVISIBLE);
        }
        if(orderedItem.isPromotion()){
            btRemove.setVisibility(View.INVISIBLE);
        }else{
            btRemove.setVisibility(View.VISIBLE);
        }
        // Setting all values in listview
        etQuantity.setText("" + orderedItem.getQuantity());
        tvTotal.setText(String.format("%,.0f", orderedItem.getTotal()));
        tvDetail.setText(orderedItem.getName());
        switch (orderedItem.getStatus())
        {
            case 0: tvStatus.setText("Mới"); break;
            case 1: tvStatus.setText("Đợi"); break;
            case 2: tvStatus.setText("Xong"); break;
        }

        if(childPosition % 2 == 0) {
            view.setBackgroundColor(context.getResources().getColor(R.color.ordered_item_odd_color));
        }else {
            view.setBackgroundColor(context.getResources().getColor(R.color.ordered_item_even_color));
        }
        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        ArrayList<OrderDetail> orderDetails = (ArrayList<OrderDetail>)getGroup(groupPosition);
        return orderDetails.size();

    }

    @Override
    public Object getGroup(int groupPosition) {
        String key = groupOrderDetails.keySet().toArray()[groupPosition].toString();
        return groupOrderDetails.get(key);
    }

    @Override
    public int getGroupCount() {
        return groupOrderDetails.keySet().size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view,
                             ViewGroup parent) {

        TextView tvQuantity;
        TextView tvTotal;
        TextView tvDetail;
        ImageButton btAddMore;

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.ordered_group_item_row, null);
        }
        tvQuantity = (TextView) view.findViewById(R.id.tvQuantity);
        tvTotal = (TextView) view.findViewById(R.id.tvTotal);
        tvDetail = (TextView) view.findViewById(R.id.tvDetail);
        btAddMore = (ImageButton) view.findViewById(R.id.btAddMore);

        btAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final OrderDetail orderedItem = (OrderDetail) v.getTag();
                new AlertDialog.Builder(context)
                        .setTitle("Xác nhận")
                        .setMessage("Bạn thật sự muốn thêm món "+orderedItem.getName() +" ?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Chấp nhận", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                orderDetails.add(orderedItem.clone());
                                excuteGroupOrderDetails();
                                notifyDataSetChanged();
                                callBack.onOrderItemGridViewChange();
                                dialog.dismiss();
                            }})
                        .setNegativeButton("Bỏ qua", null).show();

            }
        });
        btAddMore.setFocusable(false);

        ArrayList<OrderDetail> orderedItems = (ArrayList<OrderDetail>)getGroup(groupPosition);
        float totalQuantity = 0;
        for (OrderDetail orderDetail: orderedItems)
        {
            totalQuantity += orderDetail.getQuantity();
        }
        OrderDetail orderedItem = orderedItems.get(0);
        btAddMore.setTag(orderedItem);
        // Setting all values in listview
        tvQuantity.setText("" + totalQuantity);
        tvTotal.setText(String.format("%,.0f", totalQuantity * orderedItem.getPrice()));
        tvDetail.setText(orderedItem.getName());

        if(groupPosition % 2 == 0) {
            view.setBackgroundColor(context.getResources().getColor(R.color.group_item_odd_color));
        }else {
            view.setBackgroundColor(context.getResources().getColor(R.color.group_item_even_color));
        }
        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void updateData(ArrayList<OrderDetail> orderedItems) {

        orderDetails=orderedItems;
        excuteGroupOrderDetails();
        notifyDataSetChanged();
        callBack.onOrderItemGridViewChange();

    }

    public void addData(OrderDetail orderedItem) {

        orderDetails.add(orderedItem);
        excuteGroupOrderDetails();
        notifyDataSetChanged();
        callBack.onOrderItemGridViewChange();

    }

    public float getTotal()
    {
        float total = 0;
        for (OrderDetail orderedItem:orderDetails  ) {
            total += orderedItem.getTotal();
        }
        return total;
    }
    @Override
    public void onNumberPikerDialogSave() {
        notifyDataSetChanged();
        callBack.onOrderItemGridViewChange();
    }

    @Override
    public void onOrderItemGridViewChange() {

    }
}
