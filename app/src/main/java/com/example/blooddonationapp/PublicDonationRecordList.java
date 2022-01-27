package com.example.blooddonationapp;

class PublicDonationRecordList {

    private String date,amount,venue,approvedBy;

    public PublicDonationRecordList(String date, String amount, String venue, String approvedBy) {
        this.date = date;
        this.amount = amount;
        this.venue = venue;
        this.approvedBy = approvedBy;
    }

    public PublicDonationRecordList(){

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }


}
