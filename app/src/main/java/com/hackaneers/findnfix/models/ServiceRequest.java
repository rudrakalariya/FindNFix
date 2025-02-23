package com.hackaneers.findnfix.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ServiceRequest implements Serializable {
    private String requestId;
    private String customerId;
    private String providerId;
    private String serviceType;
    private String status;
    private String customerName;
    private long timestamp; // Firestore stores timestamp as a long (milliseconds)

    // Empty constructor for Firestore
    public ServiceRequest() {}

    public ServiceRequest(String requestId, String customerId, String providerId, String serviceType, long timestamp, String status, String customerName) {
        this.requestId = requestId;
        this.customerId = customerId;
        this.providerId = providerId;
        this.serviceType = serviceType;
        this.timestamp = timestamp;
        this.status = status;
        this.customerName = customerName;
    }

    // Getters and setters
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getProviderId() { return providerId; }
    public void setProviderId(String providerId) { this.providerId = providerId; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    // Convert timestamp to readable date format
    public String getFormattedTimestamp() {
        if (timestamp > 0) {
            Date date = new Date(timestamp);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
            return sdf.format(date);
        }
        return "N/A";
    }
}
