package com.example.blooddonationapp;

public class BloodRequestList {

    private String publicId, requestId, recipientName, bloodType, amount, venue, contact, additionalInfo, status;

    public BloodRequestList(String publicId, String requestId, String recipientName, String bloodType, String amount, String venue, String contact, String additionalInfo, String status){
        this.publicId = publicId;
        this.requestId = requestId;
        this.recipientName = recipientName;
        this.bloodType = bloodType;
        this.amount = amount;
        this.venue = venue;
        this.contact = contact;
        this.additionalInfo = additionalInfo;
        this.status = status;
    }

    public BloodRequestList(){

    }

    public String getPublicId(){
        return publicId;
    }

    public void setPublicId(String publicId){
        this.publicId = publicId;
    }

    public String getRequestId(){
        return requestId;
    }

    public void setRequestId(String requestId){
        this.requestId = requestId;
    }

    public String getRecipientName(){
        return recipientName;
    }

    public void setRecipientName(String recipientName){
        this.recipientName = recipientName;
    }

    public String getBloodType(){
        return bloodType;
    }

    public void setBloodType(String bloodType){
        this.bloodType = bloodType;
    }

    public String getAmount(){
        return amount;
    }

    public void setAmount(String amount){
        this.amount = amount;
    }

    public String getVenue(){
        return venue;
    }

    public void setVenue(String venue){
        this.venue = venue;
    }

    public String getContact(){
        return contact;
    }

    public void setContact(String contact){
        this.contact = contact;
    }

    public String getAdditionalInfo(){
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo){
        this.additionalInfo = additionalInfo;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

}
