package com.example.blooddonationapp;

public class PublicNotificationList {

    private String notificationId, content, name,date;

    public PublicNotificationList(String notificationId, String content, String name, String date) {
        this.notificationId = notificationId;
        this.content = content;
        this.name = name;
        this.date = date;
    }

    public PublicNotificationList(){
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

