package com.hackaneers.findnfix;

public class RequestItem {
    private String requestName;
    private String status;

    public RequestItem(String requestName, String status) {
        this.requestName = requestName;
        this.status = status;
    }

    public String getRequestName() {
        return requestName;
    }

    public String getStatus() {
        return status;
    }
}
