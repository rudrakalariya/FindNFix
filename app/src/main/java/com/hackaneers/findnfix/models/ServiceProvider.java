package com.hackaneers.findnfix.models;

public class ServiceProvider {
    private String id;
    private String name;
    private String city;
    private String profession;

    private double rating;

    // Default constructor (Required for Firestore)
    public ServiceProvider() {}

    // Constructor
    public ServiceProvider(String id, String name, String city, String profession,double rating) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.profession = profession;
        this.rating = rating;
    }


    public ServiceProvider(String name, String city, double rating) {
        this.name = name;
        this.city = city;
        this.rating = rating;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getProfession() { return profession; }
    public void setProfession(String profession) { this.profession = profession; }



    public double getRating() {
        return rating;
    }

    // Setter for Rating
    public void setRating(double rating) {
        this.rating = rating;
    }
}
