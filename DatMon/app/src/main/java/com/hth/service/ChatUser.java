package com.hth.service;

import android.util.Patterns;

import java.util.ArrayList;

/**
 * Created by Lenovo on 12/1/2016.
 */

public class ChatUser {
    public String UserId;
    public String FirstName;
    public String LastName;
    public String UserName;
    public String Email;
    public String RoleId;
    public String RoleName;
    public String Password;
    public boolean IsOnline;
    public String[] Roles;
    public String PathImage;

    public ArrayList<Conversation> Conversations;

    public String getPathImage() {
        if(hasImage()) {
            if (Patterns.WEB_URL.matcher(PathImage).matches()) {
                return PathImage;
            }
            return "http://quanngonngon.com" + PathImage;
        }

        return "";
    }

    public boolean hasImage() {
        return PathImage!= null && !PathImage.trim().isEmpty();
    }

    public String getFirstName() {
        return FirstName;
    }
    public String getName() {
        if(FirstName!=null && !FirstName.isEmpty()) return FirstName;
        if(LastName!=null && !LastName.isEmpty()) return LastName;
        return UserName;
    }

    public int getNumberOfCommingConversation() {
        return Conversations == null? 0 : Conversations.size();
    }

    public String getNumberOfCommingConversationInString() {
        return Conversations!=null && Conversations.size() > 0? ""+Conversations.size(): "";
    }
    public String getUserId() {
        return UserId;
    }
    public boolean isOnline() {
        return IsOnline;
    }
}
