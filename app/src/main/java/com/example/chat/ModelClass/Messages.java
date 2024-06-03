package com.example.chat.ModelClass;

public class Messages {
    String message;
    String senderId;
    String timeStamp;

    public Messages()
    {

    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Messages(String message, String senderId, String timeStamp) {
        this.message = message;
        this.senderId = senderId;
        this.timeStamp = timeStamp;
    }
}
