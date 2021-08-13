package com.example.data;

import java.util.ArrayList;

public class Movie {
    private int id;
    private String Name;
    private String Genre;
    private int Duration;
    private ArrayList<String> Actors;
    private String ImageLink;
    private boolean Watched = false;

    public Movie() {
        id = 0;
        Name = "name";
        Genre = " ";
        Duration = 0;
    }

    public Movie(String name) {
        id = 0;
        Name = name;
        Genre = " ";
        Duration = 0;
    }

    public Movie(int id, String name, String genre, int duration, ArrayList<String> actors) {
        this.id = id;
        Name = name;
        Genre = genre;
        Duration = duration;
        Actors = actors;
    }

    public Movie(int id, String name, String genre, int duration, ArrayList<String> actors, String imageLink) {
        this.id = id;
        Name = name;
        Genre = genre;
        Duration = duration;
        Actors = actors;
        ImageLink = imageLink;
    }

    public boolean isWatched() {
        return Watched;
    }

    public void setWatched(boolean watched) {
        Watched = watched;
    }

    public void setImageLink(String imageLink) {
        ImageLink = imageLink;
    }

    public String getImageLink() {
        return ImageLink;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }

    public double getDuration() {
        return Duration;
    }

    public void setDuration(int duration) {
        Duration = duration;
    }

    public String getDurationString() {
        return String.valueOf(Duration);
    }
    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", Name='" + Name + '\'' +
                ", Genre='" + Genre + '\'' +
                ", Duration=" + Duration +
                ", Actors=" + Actors +
                '}';
    }

    public int getId() {
        return id;
    }
    public String getIdString(){
        return String.valueOf(id);
    }

    public void setId(int id) {
        this.id = id;
    }
}
