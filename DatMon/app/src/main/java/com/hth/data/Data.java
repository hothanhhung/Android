package com.hth.data;

import java.util.ArrayList;

/**
 * Created by Lenovo on 8/26/2016.
 */
public class Data {


    public static ArrayList<OrderItem> getOrderItems()
    {
        ArrayList<OrderItem> orderItems = new ArrayList<OrderItem>();
        orderItems.add(new OrderItem("Ba chỉ xiên nướng", "95K", "http://toinayangi.vn/wp-content/uploads/2015/10/1.jpg", false));
        orderItems.add(new OrderItem("Bò mỹ xiên nướng", "195K", "http://toinayangi.vn/wp-content/uploads/2015/10/1.jpg", false));
        orderItems.add(new OrderItem("Ba chỉ xiên nướng", "65K", "http://toinayangi.vn/wp-content/uploads/2015/10/1.jpg", false));
        orderItems.add(new OrderItem("Bò mỹ xiên nướng", "95K", "http://toinayangi.vn/wp-content/uploads/2015/10/1.jpg", false));
        orderItems.add(new OrderItem("Ba chỉ xiên nướng", "75K", "http://toinayangi.vn/wp-content/uploads/2015/10/1.jpg", true));
        orderItems.add(new OrderItem("Bò mỹ xiên nướng", "125K", "http://toinayangi.vn/wp-content/uploads/2015/10/1.jpg", false));
        orderItems.add(new OrderItem("Ba chỉ xiên nướng", "135K", "http://toinayangi.vn/wp-content/uploads/2015/10/1.jpg", false));
        orderItems.add(new OrderItem("Bò mỹ xiên nướng", "95K", "http://toinayangi.vn/wp-content/uploads/2015/10/1.jpg", false));
        orderItems.add(new OrderItem("Ba chỉ xiên nướng", "75K", "http://toinayangi.vn/wp-content/uploads/2015/10/1.jpg", true));
        orderItems.add(new OrderItem("Bò mỹ xiên nướng", "125K", "http://toinayangi.vn/wp-content/uploads/2015/10/1.jpg", false));
        orderItems.add(new OrderItem("Ba chỉ xiên nướng", "135K", "http://toinayangi.vn/wp-content/uploads/2015/10/1.jpg", false));

        return orderItems;
    }

    static private ArrayList<ChannelGroup> channelGroups;

    public static ArrayList<Customer> getCustomers()
    {
        ArrayList<Customer> customers = new ArrayList<Customer>();
        customers.add(new Customer("Nguyễn Văn A", "Đà Nẵng", "099999998", "email@email.com","nam"));
        customers.add(new Customer("Nguyễn Văn B", "Đà Nẵng", "099999998", "email@email.com","nam"));
        customers.add(new Customer("Nguyễn Văn C", "Đà Nẵng", "099999998", "email@email.com","nam"));
        customers.add(new Customer("Nguyễn Văn D", "Đà Nẵng", "099999998", "email@email.com","nam"));
        customers.add(new Customer("Trấn Văn C", "Đà Nẵng", "099999998", "email@email.com","nam"));
        customers.add(new Customer("Trấn Văn D", "Đà Nẵng", "099999998", "email@email.com","nam"));
        customers.add(new Customer("Lê Văn C", "Đà Nẵng", "099999998", "email@email.com","nam"));
        customers.add(new Customer("Lê Văn D", "Đà Nẵng", "099999998", "email@email.com","nam"));

        return customers;
    }

    public static String getChannelName(String channelKey)
    {
        for (ChannelGroup channelGroup:getChannelGroup()) {
            for (ChannelItem channelItem:channelGroup.getChannelList()) {
                if(channelKey.equalsIgnoreCase(channelItem.getId()))
                {
                    return channelItem.getName();
                }
            }
        }
        return "";
    }

    static public ArrayList<ChannelGroup> getChannelGroup()
    {
        if(channelGroups == null || channelGroups.size() == 0) {
            channelGroups = new ArrayList<ChannelGroup>();

            //Đài Truyền hình Việt Nam - VTV
            ArrayList<ChannelItem> vtvGroups = new ArrayList<ChannelItem>();
            vtvGroups.add(new ChannelItem("Bàn 5.1", "Bàn 5.1"));
            vtvGroups.add(new ChannelItem("Bàn 5.2", "Bàn 5.2"));
            vtvGroups.add(new ChannelItem("Bàn 5.3", "Bàn 5.3"));
            vtvGroups.add(new ChannelItem("Bàn 5.4", "Bàn 5.4"));
            vtvGroups.add(new ChannelItem("Bàn 5.5", "Bàn 5.5"));
            vtvGroups.add(new ChannelItem("Bàn 5.6", "Bàn 5.6"));
            vtvGroups.add(new ChannelItem("Bàn 5.7", "Bàn 5.7"));
            vtvGroups.add(new ChannelItem("Bàn 5.8", "Bàn 5.8"));
            vtvGroups.add(new ChannelItem("Bàn 5.9", "Bàn 5.9"));
            channelGroups.add(new ChannelGroup("Khu A", vtvGroups));

            //Đài Tiếng nói Việt Nam
            ArrayList<ChannelItem> vovGroups = new ArrayList<ChannelItem>();
            vovGroups.add(new ChannelItem("Bàn 4.1", "Bàn 4.1"));
            vovGroups.add(new ChannelItem("Bàn 4.2", "Bàn 4.2"));
            channelGroups.add(new ChannelGroup("Khu B", vovGroups));


            //Đài Tiếng nói Việt Nam
            ArrayList<ChannelItem> hanoiGroups = new ArrayList<ChannelItem>();
            hanoiGroups.add(new ChannelItem("Bàn 3.1", "Bàn 3.1"));
            hanoiGroups.add(new ChannelItem("Bàn 3.2", "Bàn 3.2"));
            channelGroups.add(new ChannelGroup("Khu C", hanoiGroups));

            //Đài Truyền hình TP Hồ Chí Minh - HTV
            ArrayList<ChannelItem> htvGroups = new ArrayList<ChannelItem>();
            htvGroups.add(new ChannelItem("Bàn 3", "Bàn 3"));
            channelGroups.add(new ChannelGroup("Khu D", htvGroups));


            //Đài truyền hình An Viên AVG
            ArrayList<ChannelItem> avGroups = new ArrayList<ChannelItem>();
            channelGroups.add(new ChannelGroup("Khu E", avGroups));

        }
        return channelGroups;
    }
}
