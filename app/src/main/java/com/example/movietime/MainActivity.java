package com.example.movietime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.data.Cinema;
import com.example.data.Movie;
import com.example.movietime.events.MyEventMovieDataChanged;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private ApplicationMy app;
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private AdapterMovies adapter;

    private static final int RC_SIGN_IN = 100;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mDatabase;
    private ChildEventListener myDataListener;
    private HashMap<String, Movie> myModelDataHM = new HashMap<>(); //key value

    private void addMovies(){
        Movie tmp1 = new Movie(0,"Joker","Action",95,null, "https://m.media-amazon.com/images/M/MV5BNGVjNWI4ZGUtNzE0MS00YTJmLWE0ZDctN2ZiYTk2YmI3NTYyXkEyXkFqcGdeQXVyMTkxNjUyNQ@@._V1_.jpg");
        Movie tmp2 = new Movie(1,"Lucy","Horror",100,null, "https://upload.wikimedia.org/wikipedia/en/1/14/Lucy_%282014_film%29_poster.jpg");
        Movie tmp3 = new Movie(2,"The maze runner","Action",120,null, "https://upload.wikimedia.org/wikipedia/en/b/be/The_Maze_Runner_poster.jpg");
        Movie tmp4 = new Movie(3,"Hit man","Action",98,null, "https://m.media-amazon.com/images/M/MV5BMjc2ODU1YWYtOGU1MS00ODQ3LTg1YmUtYmRmNjZmNzNiMjA4XkEyXkFqcGdeQXVyNTIzOTk5ODM@._V1_.jpg");
        Movie tmp5 = new Movie(4,"San Andreas","Comedy",110,null, "https://upload.wikimedia.org/wikipedia/en/3/38/San_Andreas_poster.jpg");
        Movie tmp6 = new Movie(5,"Baywatch","Comedy",112,null, "https://i.pinimg.com/originals/0b/99/ed/0b99ed12f97aa0df8a188774b388d4da.jpg");
        Movie tmp7 = new Movie(6,"Fast 5","Action",132,null, "https://flxt.tmsimg.com/assets/p8338313_p_v10_bb.jpg");
        Movie tmp8 = new Movie(7,"Split","Comedy",102,null, "https://m.media-amazon.com/images/M/MV5BZTJiNGM2NjItNDRiYy00ZjY0LTgwNTItZDBmZGRlODQ4YThkL2ltYWdlXkEyXkFqcGdeQXVyMjY5ODI4NDk@._V1_.jpg");
        Movie tmp9 = new Movie(8,"Suicide squad","Comedy",115,null, "https://www.chinadaily.com.cn/culture/attachement/jpg/site1/20160815/b083fe96fb62191b6f4a16.jpg");
        Movie tmp10 = new Movie(9,"Italian job","Action",124,null, "https://upload.wikimedia.org/wikipedia/en/thumb/d/db/Italianjob.jpg/220px-Italianjob.jpg");
        app.addMovieToList(tmp1);
        app.addMovieToList(tmp2);
        app.addMovieToList(tmp3);
        app.addMovieToList(tmp4);
        app.addMovieToList(tmp5);
        app.addMovieToList(tmp6);
        app.addMovieToList(tmp7);
        app.addMovieToList(tmp8);
        app.addMovieToList(tmp9);
        app.addMovieToList(tmp10);
    }

    private void addCinemas(){
        Cinema temp1 = new Cinema("Cineplexx Maribor",46.5525962,15.6275652);
        app.addCinemaToList(temp1);
        Cinema temp2 = new Cinema("Maribox Maribor",46.5565866,15.6527723);
        app.addCinemaToList(temp2);
        Cinema temp3 = new Cinema("Cineplexx Murska Sobota",46.66147,16.1432413);
        app.addCinemaToList(temp3);
        Cinema temp4 = new Cinema("Cineplexx Celje",46.2433339,15.2785115);
        app.addCinemaToList(temp4);
        Cinema temp5 = new Cinema("Kolosej Ljubljana",46.0657552,14.5501123);
        app.addCinemaToList(temp5);
        Cinema temp6 = new Cinema("Mestni kino Metropol",46.230384, 15.265013);
        app.addCinemaToList(temp6);
        Cinema temp7 = new Cinema("Kino Rogaška",46.2379231,15.6351792);
        app.addCinemaToList(temp7);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app = (ApplicationMy) getApplication();
        addMovies();
        Log.i(TAG, "seznam: " + app.getStringOfMovieList());
        addCinemas();
        mAuth = FirebaseAuth.getInstance();
        EventBus.getDefault().register(this);
        recyclerView = findViewById(R.id.recyclerView);
        initAdapter();
        updateUI();
        //getFavoriteMovies();
    }
    private void initAdapter() {
        adapter = new AdapterMovies(app, new AdapterMovies.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Log.i(TAG,"click on: " + position );
                EventBus.getDefault().post(new MyEventMovieDataChanged(app.getMovieAtPos(position).toString(), MyEventMovieDataChanged.MyEnumType.startingToUpdateMovie));
            }

            @Override
            public void onItemLongClick(View itemView, int position) {
                Movie tmp = app.getMovieAtPos(position);
                Log.i(TAG,"long click on: " + position );
                EventBus.getDefault().post(new MyEventMovieDataChanged(tmp.toString(), MyEventMovieDataChanged.MyEnumType.deletedMovie));
                SaveMovie(tmp);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MyEventMovieDataChanged event) {
        Log.i(TAG,event.toString());
    };

    public void onClickMapsOpenMapsActivity(View view){
        Intent i = new Intent(getBaseContext(), ActivityMaps.class);
        startActivity(i);
    }

    public void notification(View view) {
        app.onClickBasic1(view);
    }
    private void updateUI() {
        mDatabase = FirebaseDatabase.getInstance("https://movietime-3ba73-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        initDataListener();
    }

    private void initDataListener() {
        myDataListener = new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                Movie data = dataSnapshot.getValue(Movie.class);
                data.setId(Integer.parseInt(dataSnapshot.getKey()));
                myModelDataHM.put(dataSnapshot.getKey(),data);
                Log.i(TAG, "Add:"+dataSnapshot.getKey()+" "+data);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                Movie data = dataSnapshot.getValue(Movie.class);
                data.setId(Integer.parseInt(dataSnapshot.getKey()));
                myModelDataHM.put(dataSnapshot.getKey(),data);
                Log.i(TAG, "Changed:"+dataSnapshot.getKey()+" "+data);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG, "Removed:"+dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }

    public void onClickShowFavorites(View view) {
        Intent i = new Intent(getBaseContext(), ActivityFavorites.class);
        startActivity(i);
    }

    public void SaveMovie(Movie movie) {
        app.addMovieToFavorites(movie);
        Task ref = mDatabase.setValue(app.getFavorites()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i(TAG,"onSuccess Movie");
                Toast.makeText(MainActivity.this,"Uspesno dodano pod priljubljene",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG,"onFail Movie");
            }
        });
    }
    public void getFavoriteMovies(){
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //app.addMovieToFavorites(snapshot.child("Movie").getValue(Movie.class));
                app.addArrayToFavorites((ArrayList<Movie>) snapshot.getValue());
                Log.i(TAG,"ZACETEK: " + app.getFavorites().toString());
                initAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}