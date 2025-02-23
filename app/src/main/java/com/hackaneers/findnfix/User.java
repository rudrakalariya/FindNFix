package com.hackaneers.findnfix;

public class User {
    public String email;
    public String role;

    // Default constructor (needed for Firebase)
    public User() {}

    // Parameterized constructor
    public User(String email, String role) {
        this.email = email;
        this.role = role;
    }
}
