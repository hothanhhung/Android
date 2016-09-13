package com.hth.utils;

/**
 * Created by Lenovo on 9/5/2016.
 */
public class SearchProgramItem {
    public String ChannelName;
    public String ImageUrl;
    public String ProgramName;
    public String DecodedProgramName;
    public String Time;

    public String getChannelName() {
        return ChannelName;
    }

    public void setChannelName(String channelName) {
        ChannelName = channelName;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getProgramName() {
        return ProgramName;
    }

    public String getDecodedProgramName()
    {
        if(DecodedProgramName == null || DecodedProgramName.isEmpty())
        {
            DecodedProgramName= EncrypeString.tryDecode(ProgramName);
        }
        return DecodedProgramName;
    }
    public void setProgramName(String programName) {
        ProgramName = programName;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
