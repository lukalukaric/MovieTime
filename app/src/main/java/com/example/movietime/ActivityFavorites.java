package com.example.movietime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.data.Movie;
import com.example.movietime.events.MyEventMovieDataChanged;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ActivityFavorites extends AppCompatActivity {
    private static final String TAG = ActivityFavorites.class.getSimpleName();
    private RecyclerView recyclerView;
    private AdapterFavorites adapter;
    private ApplicationMy app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        EventBus.getDefault().register(this);
        app = (ApplicationMy) getApplication();
        recyclerView = findViewById(R.id.recyclerView);
        initAdapter();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MyEventMovieDataChanged event) {
        Log.i(TAG,event.toString());
    };
    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void initAdapter() {
        adapter = new AdapterFavorites(app, new AdapterFavorites.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Log.i(TAG,"click on: " + position );
                Movie tmp = app.getFavoriteMovieAtPos(position);
                tmp.setWatched(true);
                app.addMovieToWatchedMoviesList(tmp);
                EventBus.getDefault().post(new MyEventMovieDataChanged(tmp.toString(), MyEventMovieDataChanged.MyEnumType.startingToUpdateMovie));
            }

            @Override
            public void onItemLongClick(View itemView, int position) {
                Log.i(TAG,"long click on: " + position );
                EventBus.getDefault().post(new MyEventMovieDataChanged(app.getMovieAtPos(position).toString(), MyEventMovieDataChanged.MyEnumType.deletedMovie));
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void onClickFinish(View view) {
        finish();
    }
}