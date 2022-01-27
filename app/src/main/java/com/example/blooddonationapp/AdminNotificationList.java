package com.example.blooddonationapp;

public class AdminNotificationList {

    private String notificationId, content, name;

    public AdminNotificationList(String notificationId, String content, String name) {
        this.notificationId = notificationId;
        this.content = content;
        this.name = name;
    }

    public AdminNotificationList(){
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

}
