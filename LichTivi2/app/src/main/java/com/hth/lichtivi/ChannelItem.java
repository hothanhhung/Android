package com.hth.lichtivi;

/**
 * Created by Lenovo on 8/26/2016.
 */
public class ChannelItem {
    String id;
    String name;

    public ChannelItem(String id, String name){
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
