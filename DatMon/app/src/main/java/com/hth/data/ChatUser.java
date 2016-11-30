package com.hth.data;

/**
 * Created by Lenovo on 11/30/2016.
 */

public class ChatUser {
    String image;
    String name;
    int numberOfCommingMessage;

    public ChatUser(String image, String name, int numberOfCommingMessage)
    {
        this.image = image;
        this.name = name;
        this.numberOfCommingMessage = numberOfCommingMessage;
    }

    public String getImage() {
        return image;
    }

    public boolean hasImage() {
        return image!= null && !image.trim().isEmpty();
    }

    public String getName() {
        return name;
    }


    public int getNumberOfCommingMessage() {
        return numberOfCommingMessage;
    }

    public String getNumberOfCommingMessageInString() {
        return numberOfCommingMessage > 0? ""+numberOfCommingMessage: "";
    }

    public void setNumberOfCommingMessage(int numberOfCommingMessage) {
        this.numberOfCommingMessage = numberOfCommingMessage;
    }
}
