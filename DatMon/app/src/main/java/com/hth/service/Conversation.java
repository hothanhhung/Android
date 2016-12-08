package com.hth.service;

/**
 * Created by Lenovo on 12/1/2016.
 */

public class Conversation {
    public String Id;
    public String FromUserId;
    public String FromUserName;
    public String ToUserId;
    public String ToUserName;
    public String Message;
    public String CreatedDate;
    public boolean FromUserIsRead;
    public boolean ToUserIsRead;

    public Conversation(){

    }
    public Conversation(String fromUserId, String toUserId, String message, String createdDate){
        FromUserId = fromUserId;
        ToUserId = toUserId;
        Message = message;
        CreatedDate = createdDate;
    }
    public String getMessage() {
        return Message;
    }

    public String getFromUserId() {
        return FromUserId;
    }

    public void setFromUserId(String fromUserId) {
        FromUserId = fromUserId;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public void setFromUserName(String fromUserName) {
        FromUserName = fromUserName;
    }

    public String getToUserId() {
        return ToUserId;
    }

    public void setToUserId(String toUserId) {
        ToUserId = toUserId;
    }

    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public boolean isFromUserIsRead() {
        return FromUserIsRead;
    }

    public void setFromUserIsRead(boolean fromUserIsRead) {
        FromUserIsRead = fromUserIsRead;
    }

    public boolean isToUserIsRead() {
        return ToUserIsRead;
    }

    public void setToUserIsRead(boolean toUserIsRead) {
        ToUserIsRead = toUserIsRead;
    }
}
