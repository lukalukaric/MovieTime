package com.example.movietime.events;

import android.location.Location;

public class MyEventMovieDataChanged {
    private String data;
    private MyEnumType myDataType;
    public enum MyEnumType {addedNewMovie,startingToUpdateMovie, updatedMovie, deletedMovie};

    public MyEventMovieDataChanged(String data, MyEnumType myDataType) {
        this.data = data;
        this.myDataType = myDataType;
    }

    public String getData() {
        return data;
    }

    public MyEnumType getMyDataType() {
        return myDataType;
    }

    @Override
    public String toString() {
        return "MyEventDataChange{" +
                "data='" + data + '\'' +
                ", myDataType=" + myDataType +
                '}';
    }
}
