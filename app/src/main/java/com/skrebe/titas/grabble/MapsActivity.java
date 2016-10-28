package com.skrebe.titas.grabble;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.kml.KmlLayer;
import com.google.maps.android.kml.KmlPlacemark;
import com.skrebe.titas.grabble.listeners.PopUpAnimationListener;
import com.skrebe.titas.grabble.notUsed.PlaceMarkParser;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, AsyncResponse, com.google.android.gms.location.LocationListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {


    private GoogleMap mMap;
    private Marker userMarker = null;
    private Map<String, Marker> markers = new HashMap<>();
    private LatLng focusPoint = new LatLng(55.943729, -3.188536);
    private static final float ZOOM_LEVEL = 15.5f;
    private TextView popUp;
    private AnimationSet animation;

    @Override
    protected void onStart() {
        Log.e("START", "START");
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.e("STOP", "STOP");
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        popUp = (TextView) findViewById(R.id.letterView);

        createAnimationForPopUp();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(3000);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "floating button", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Creates letter grab animation.
     * Letter gets bigger and fades away.
     */
    private void createAnimationForPopUp() {
        animation = new AnimationSet(true);

        //Letter gets
        Animation scale = AnimationUtils.loadAnimation(this, R.anim.scale);
        scale.setAnimationListener(new PopUpAnimationListener(popUp));

        //Letter fades away
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setStartOffset(1000);
        fadeOut.setDuration(2000);

        animation.addAnimation(scale);
        animation.addAnimation(fadeOut);
        popUp.setAnimation(animation);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        loadNewMapIfNeeded();
    }


    private void loadNewMapIfNeeded() {

        Calendar currentDate = Calendar.getInstance();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        long prevDate = prefs.getLong("lastDate", -1);
        Calendar previousDate = Calendar.getInstance();
        previousDate.setTimeInMillis(prevDate);

        if (!areDatesSame(currentDate, previousDate) || !prefs.getBoolean("consistentState", false)) {
            //if something goes wrong inside this if statement we have to repeat it
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("consistentState", false);
            editor.apply();
            Log.e("NEW", "NEW");

            try {
                markMap(currentDate.get(Calendar.DAY_OF_WEEK));
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            Log.e("OLD", "OLD");
            markMapFromDatabase();
        }

    }


    /**
     * Starts tracking phone location
     */
    @SuppressWarnings({"MissingPermission"})
    private void startLocationTracking() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    /**
     * This function is called if map
     */

    private void markMapFromDatabase() {
        DatabaseHelper db = new DatabaseHelper(this);
        db.getAllMarkerOptions(mMap, markers);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(focusPoint, ZOOM_LEVEL));
    }


    private void markMap(int day) throws IOException {
        String dayInString = Helper.getDayInString(day);
        String baseUrl = "http://www.inf.ed.ac.uk/teaching/courses/selp/coursework/";
        String fullUrl = baseUrl + dayInString + ".kml";
        PlaceMarkParser pmp = new PlaceMarkParser(this);
        pmp.execute(fullUrl);
    }

    /**
     * @param currentDate
     * @param previousDate
     * @return true if the same day
     */
    private boolean areDatesSame(Calendar currentDate, Calendar previousDate) {
        return currentDate.get(Calendar.DAY_OF_YEAR) == previousDate.get(Calendar.DAY_OF_YEAR) &&
                currentDate.get(Calendar.YEAR) == previousDate.get(Calendar.YEAR);
    }

    /**
     * @param layer
     */
    @Override
    public void processFinish(KmlLayer layer) {
        //if data was not properly loaded
        if (layer == null) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.clayout), "No internet ", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Retry", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadNewMapIfNeeded();
                }
            });
            snackbar.show();
            return;

        }

        DatabaseHelper db = new DatabaseHelper(this);
        db.deleteLocationPoints();
        for (KmlPlacemark mark : layer.getPlacemarks()) {
            LatLng coor = ((LatLng) mark.getGeometry().getGeometryObject());
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(coor)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_red))
                    .title(mark.getProperty("description")));
            String name = mark.getProperty("name");
            db.insertLocationPoint(marker, name);
            markers.put(name, marker);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(focusPoint, ZOOM_LEVEL));

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("consistentState", true);
        editor.putLong("lastDate", Calendar.getInstance().getTimeInMillis());
        editor.apply();
    }

    /**
     * Launches new activity
     *
     * @param view
     */
    public void letterActivity(View view) {
        Intent newIntent = new Intent(this, LetterActivity.class);
        startActivity(newIntent);
    }

    public void statisticsActivity(View view){
        Intent newIntent = new Intent(this, StatsActivity.class);
        startActivity(newIntent);
    }

    //GPS SERVICES BELLOW

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //if user denied GPS permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
            return;
        }

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            mLastLocation.getLatitude();
            mLastLocation.getLongitude();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (userMarker != null) {
            userMarker.remove();
        }
        userMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                .title("YOU")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean consistentState = prefs.getBoolean("consistentState", false);

        //if everything is OK with APP loop all points and check if any of them are within 10 meters
        if (consistentState) {

            DatabaseHelper db = new DatabaseHelper(this);
            for (Map.Entry<String, Marker> e : markers.entrySet()) {
                String name = e.getKey();
                Marker marker = e.getValue();
                Location markPosition = new Location("google");
                markPosition.setLatitude(marker.getPosition().latitude);
                markPosition.setLongitude(marker.getPosition().longitude);
                float distance = location.distanceTo(markPosition);

                if (distance < 10f) {
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_blue));
                    db.setLocationPointVisited(name, popUp, animation);
                }
            }

        }
    }

}
