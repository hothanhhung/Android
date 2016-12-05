package com.hth.service;

import android.util.Patterns;

import com.hth.data.ServiceProcess;

import java.util.ArrayList;
import java.util.HashSet;

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
            return ServiceProcess.getServerLink(PathImage);
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
        return countUnreadMessage();
    }

    public String getNumberOfCommingConversationInString() {
        int number = countUnreadMessage();
        return number > 0? ""+number: "";
    }
    public String getUserId() {
        return UserId;
    }
    public boolean isOnline() {
        return IsOnline;
    }

    public ArrayList<Conversation> getConversations()
    {
        return Conversations;
    }

    public int countUnreadMessage()
    {
        int number = 0;
        if( Conversations == null) return number;
        for (Conversation conversation:Conversations) {
            if(conversation.isToUserIsRead())number++;
        }
        return number;
    }

    public int countUsersUnread()
    {
        HashSet<String> userIds = new HashSet<>();
        if( Conversations == null) return 0;
        for (Conversation conversation:Conversations) {
            if(conversation.isToUserIsRead() && !userIds.contains(conversation.FromUserId)){
                userIds.add(conversation.FromUserId);
            };
        }
        return userIds.size();
    }
}
