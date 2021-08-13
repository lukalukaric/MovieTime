package com.example.data;

import java.util.ArrayList;

public class User {
    private String Username;
    private String Password;
    private String Email;
    private ArrayList<Movie> MoviesWatched;
    private ArrayList<Movie> MoviesUpcoming;
    private ArrayList<Movie> MoviesToWatch;

    public User(String username) {
        Username = username;
    }

    public User(String username, String password, String email) {
        Username = username;
        Password = password;
        Email = email;
    }

    public User(String username, String password, String email, ArrayList<Movie> moviesWatched, ArrayList<Movie> moviesUpcoming, ArrayList<Movie> moviesToWatch) {
        Username = username;
        Password = password;
        Email = email;
        MoviesWatched = moviesWatched;
        MoviesUpcoming = moviesUpcoming;
        MoviesToWatch = moviesToWatch;
    }
}
