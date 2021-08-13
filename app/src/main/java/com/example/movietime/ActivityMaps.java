package com.example.movietime;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.data.Cinema;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;

import java.util.Random;

public class ActivityMaps extends AppCompatActivity {
    private ApplicationMy app;
    private static final String TAG = ActivityMaps.class.getSimpleName();
    private static final int PERMISSION_ALL = 123;
    MapView map = null;
    GeoPoint startPoint;
    IMapController mapController;
    Marker marker;
    Random rnd;


    String[] PERMISSIONS = {
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.ACCESS_NETWORK_STATE,
            android.Manifest.permission.CAMERA
    };
    Location lok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (ApplicationMy) getApplication();

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_maps);

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

    }

    public void initMapStartGPS() {
        mapController = map.getController();
        mapController.setZoom(18.5);
        startPoint = new GeoPoint(46.5565866,15.6527723);
        mapController.setCenter(startPoint);
        map.invalidate();

        for(int i = 0; i < app.getSeznamDvoran().size(); i++){
            Cinema temp = app.getCinemaAtPos(i);
            marker = new Marker(map);
            marker.setTitle(temp.getName());
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setIcon(getResources().getDrawable(R.drawable.ic_position));
            map.getOverlays().add(marker);
            marker.setPosition(new GeoPoint(temp.getLatitude(),temp.getLongitude()));
            map.invalidate();
        }
        CompassOverlay mCompassOverlay = new CompassOverlay(this, new InternalCompassOrientationProvider(this), map);
        mCompassOverlay.enableCompass();
        map.getOverlays().add(mCompassOverlay);
        map.invalidate();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!hasPermissions(this,PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {
            initMapStartGPS();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /*
     Helper function to check permissions
      */
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_ALL: {
                if (grantResults.length >= PERMISSIONS.length) {
                    for (int i=0; i<PERMISSIONS.length; i++) {
                        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this,"Nimaš vseh dovolenj!",Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                    initMapStartGPS();
                }
                else
                    finish();
            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause(); //needed for compass, my location overlays, v6.0.0 and up
        Log.i(TAG,"onPause "+lok);
    }

    public void onResume(){
        super.onResume();
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

}