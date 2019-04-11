package com.example.travelmate.utility;

import java.util.Date;

public class ChatModel {

    public String messageText;
    public String messageUSer;
    long messageTime;

    public ChatModel(String messageText, String messageUSer) {
        this.messageText = messageText;
        this.messageUSer = messageUSer;
        messageTime = new Date().getTime();

    }

    public ChatModel() {

    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUSer() {
        return messageUSer;
    }

    public void setMessageUSer(String messageUSer) {
        this.messageUSer = messageUSer;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }


}
