package com.example.blooddonationapp;

public class PublicData {
    private String publicId;
    private String fullname;
    private String gender;
    private String age;
    private String contact;
    private String address;
    private String email;
    private String bloodType;
    private String imageUrl;

    public PublicData(String publicId, String fullname, String gender, String age, String contact,String address, String email, String bloodType, String imageURL) {
        this.publicId = publicId;
        this.fullname = fullname;
        this.gender = gender;
        this.age = age;
        this.contact = contact;
        this.address = address;
        this.email = email;
        this.bloodType = bloodType;
        this.imageUrl = imageURL;
    }

    public PublicData() {
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
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

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getImageURL() {
        return imageUrl;
    }

    public void setImageURL(String imageURL) {
        this.imageUrl = imageURL;
    }
}
