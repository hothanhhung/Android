package com.hth.lichtivi;

import com.hth.utils.MethodsHelper;

/**
 * Created by Lenovo on 8/26/2016.
 */
public class ChannelItem {
    String id;
    String name;
    String englishName;

    public ChannelItem(String id, String name){
        this.id = id;
        this.name = name;
    }

    public ChannelItem(String id, String name, String englishName){
        this.id = id;
        this.name = name;
        this.englishName = englishName.toLowerCase();
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

    public String getEnglishName()
    {
        if(englishName == null || englishName.isEmpty())
        {
            englishName = MethodsHelper.stripAccents(name);
        }
        return englishName;
    }

}
