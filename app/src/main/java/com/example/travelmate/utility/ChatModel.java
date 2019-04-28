package com.example.travelmate.utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ChatModel {

    public String messageText;
    public String messageUSer;
    String messageTime;

    public ChatModel(String messageText, String messageUSer) {
        this.messageText = messageText;
        this.messageUSer = messageUSer;

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        messageTime = df.format(c.getTime());
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

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }


}
