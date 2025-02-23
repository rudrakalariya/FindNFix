package com.hackaneers.findnfix;
public class Booking {
    private String service; // Match Firestore field name
    private String providerName;
    private String bookingDate;
    private String status;
    private String bookingId;

    public Booking() {
        // Default constructor for Firebase
    }

    public Booking(String service, String providerName, String bookingDate, String status) {
        this.service = service;
        this.providerName = providerName;
        this.bookingDate = bookingDate;
        this.status = status;
    }

    public String getService() {
        return service;
    }

    public String getProviderName() {
        return providerName;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public String getStatus() {
        return status;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }
}
