package com.example.blooddonationapp;

public class AdminData {
    private String adminId;
    private String name;
    private String workplace;
    private String contact;
    private String address;
    private String email;
    private String imageUrl;

    public AdminData(String adminId, String name, String workplace, String contact, String address, String email, String imageURL) {
        this.adminId = adminId;
        this.name = name;
        this.workplace = workplace;
        this.contact = contact;
        this.address = address;
        this.email = email;
        this.imageUrl = imageURL;
    }

    public AdminData() {
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageURL() {
        return imageUrl;
    }

    public void setImageURL(String imageURL) {
        this.imageUrl = imageURL;
    }
}


