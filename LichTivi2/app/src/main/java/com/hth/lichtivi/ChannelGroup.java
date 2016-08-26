package com.hth.lichtivi;

import java.nio.channels.Channel;
import java.util.ArrayList;

/**
 * Created by Lenovo on 8/26/2016.
 */
public class ChannelGroup {
    private String name;
    private ArrayList<ChannelItem> channelList = new ArrayList<ChannelItem>();

    public ChannelGroup(String name, ArrayList<ChannelItem> channelList) {
        super();
        this.name = name;
        this.channelList = channelList;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ArrayList<ChannelItem> getChannelList() {
        return channelList;
    }
    public void setChannelList(ArrayList<ChannelItem> channelList) {
        this.channelList = channelList;
    };
}
