package com.example.movietime;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.EventLog;
import android.util.Log;
import android.view.View;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Random;

import com.example.data.Cinema;
import com.example.data.Movie;
import com.example.movietime.events.MyEventMovieDataChanged;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ApplicationMy extends Application {
    private ArrayList<Movie> seznam;
    private ArrayList<Cinema> seznamDvoran;
    private ArrayList<Movie> favorites;
    private ArrayList<Movie> watched;
    private static final String TAG = ApplicationMy.class.getSimpleName();

    public static final String CHANNEL_ID = "MyNotifications";
    private static int myID=100;

    public void onCreate() {
        super.onCreate();
        seznam = new ArrayList<Movie>();
        seznamDvoran = new ArrayList<Cinema>();
        favorites = new ArrayList<Movie>();
        watched = new ArrayList<Movie>();
        EventBus.getDefault().register(this);
        regNotChannel();
        myID = (int)(System.currentTimeMillis()%1000000);

    }
    @TargetApi(Build.VERSION_CODES.O)
    private void regNotChannel() {
        // Configure the channel
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "My Channel", importance);
        channel.setDescription("My Notification test");
        NotificationManager mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.createNotificationChannel(channel);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MyEventMovieDataChanged event) {
        Log.i(TAG,event.toString());
    };

    public void addMovieToList(Movie movie){
        seznam.add(movie);
        Log.i(TAG,"movie added: " + movie.toString() );
    }
    public void addMovieToFavorites(Movie movie){
        favorites.add(movie);
        Log.i(TAG,"movie added to favorites: " + movie.toString() );
    }

    public void addMovieToWatchedMoviesList(Movie movie){
        watched.add(movie);
        Log.i(TAG,"movie added to watched: " + movie.toString() );
    }

    public ArrayList<Movie> getFavorites() { return favorites; }


    public ArrayList<Movie> getSeznam() {
        return seznam;
    }

    public void setSeznam(ArrayList<Movie> seznam) {
        this.seznam = seznam;
    }

    public String getStringOfMovieList(){
        return seznam.toString();
    }

    public Movie getMovieAtPos(int position){ return seznam.get(position); }

    public Movie getFavoriteMovieAtPos(int position){ return favorites.get(position); }

    public int getSeznamSize(){ return seznam.size(); }
    public int getFavoritesSize(){ return favorites.size(); }

    public ArrayList<Cinema> getSeznamDvoran() {
        return seznamDvoran;
    }

    public Cinema getCinemaAtPos(int position){ return seznamDvoran.get(position); }

    public void addCinemaToList(Cinema cinema){
        seznamDvoran.add(cinema);
        Log.i(TAG,"cinema added: " + cinema.toString() );
    }
    public Movie getRandomMovie(){
        Random rand = new Random();
        int randomNumber = rand.nextInt(getSeznamSize()+1);
        return seznam.get(randomNumber);
    }
    private void createNotification(int nId, int iconRes, String title, String body, String channelId) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this, channelId).setSmallIcon(iconRes)
                .setContentTitle(title).setContentText(body);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(nId, mBuilder.build());
    }

    public void onClickBasic1(View view) {
        Movie randomMovie = getRandomMovie();
        createNotification(myID++,R.drawable.notification, "Random movie to watch:", "Name: " + randomMovie.getName() + "\n\n\n\n\n Genre: " + randomMovie.getGenre() + "\n\n\n\n\n Duration: " + randomMovie.getDurationString() + " min", ApplicationMy.CHANNEL_ID);
    }

    public void addArrayToFavorites(ArrayList<Movie> movies){
        favorites = movies;
    }

}
