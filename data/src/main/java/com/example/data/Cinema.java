package com.example.data;

public class Cinema {
    private String Name;
    private Double Latitude;
    private Double Longitude;

    public Cinema(String name) {
        Name = name;
    }

    public Cinema(String name, Double latitude, Double longitude) {
        Name = name;
        Latitude = latitude;
        Longitude = longitude;
    }

    public String getName() {
        return Name;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }
}
