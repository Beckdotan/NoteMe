package com.example.noteme;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.noteme.databinding.ActivityMapBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapBinding binding;

    private static final int LOCATION_PERMISSION_CODE = 101;
    private FusedLocationProviderClient mfusedLocationProviderClient;
    private static final int DEFAULT_ZOOM = 15;
    private final LatLng mDeaultlocation = new LatLng(-34, 151);
    private Location mLastKnownLocation;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mfusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        /*
        if (mLocationPermissionGranted) {
            Log.i("mapActivity", "onCreate: permission granted");
        } else {
            requestLocationPermission();
        }
         */
    }



        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;


            // Add a marker in Sydney and move the camera
            mMap.addMarker(new MarkerOptions().position(mDeaultlocation).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDeaultlocation, DEFAULT_ZOOM));
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                mMap.setMyLocationEnabled(true);
            }

            updateLocationUI();
            getDeviceLocation();

        }
    //check if location is permitted
    private void isLocationPermissionGranted() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            return;
        } else {
            return;
        }
    }


    //asking for location permission
    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==PackageManager.PERMISSION_GRANTED){
            mLocationPermissionGranted = true;
        }else{
           ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionGranted = false;
        switch(requestCode){
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:{
                //if the request is cancelled the result is ampty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    //getting last known location and moving the map to it.
    private void getDeviceLocation() {

        Log.i("GET DIVICE LOCATION", "In");
        try {
            Log.i("GET DIVICE LOCATION", "In TRY");
            if (mLocationPermissionGranted) {
                Log.i("GET DIVICE LOCATION", "IN IF");
                Task locationResult = mfusedLocationProviderClient.getLastLocation();
                Log.i("GET DIVICE LOCATION", "made task");
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            //set the cameras positions to the current location of device.

                            Log.i("On Complete", "in if");
                            mLastKnownLocation = (Location) task.getResult();

                            Log.i("My Location", mLastKnownLocation.toString());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));

                        } else {
                            Log.d("nullLocation", "current location is null, Using Defult");
                            Log.e("nullLocation", "Exception %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDeaultlocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }

        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }

    }



    private void updateLocationUI() {
        Log.i("UPDATE MY LOCATION", "In ");
        if (mMap == null) {
            Log.i("UPDATE MY LOCATION", "In Null ");
            return;
        }
        try {
            Log.i("UPDATE MY LOCATION", "In Try ");
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.i("UPDATE MY LOCATION", "In Try - IF ");
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            }else{
                Log.i("UPDATE MY LOCATION", "In Try - ELSE ");
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                requestLocationPermission();
            }

        } catch (SecurityException e){
            Log.e("Exception: %s", e.getMessage());
        }
    }




}