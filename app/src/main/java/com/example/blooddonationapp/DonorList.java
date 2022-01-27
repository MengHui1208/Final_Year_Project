package com.example.blooddonationapp;

public class DonorList {

    private  String fullname;
    private  String bloodType;
    private  String contact;
    private  String address;
    private  String imageUrl;
    private  String uid;

    public DonorList() {}

    public DonorList(String fullname, String bloodType, String contact, String address, String imageUrl, String uid) {
        this.fullname = fullname;
        this.bloodType = bloodType;
        this.contact = contact;
        this.address = address;
        this.imageUrl = imageUrl;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public  String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

