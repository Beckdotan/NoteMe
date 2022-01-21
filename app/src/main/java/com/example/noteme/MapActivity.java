package com.example.noteme;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.noteme.databinding.ActivityMapBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener{

    private GoogleMap mMap;
    private ActivityMapBinding binding;
    Button addNoteButton;

    private static final int LOCATION_PERMISSION_CODE = 101;
    private FusedLocationProviderClient mfusedLocationProviderClient;
    private static final int DEFAULT_ZOOM = 15;
    private final LatLng mDeaultlocation = new LatLng(-34, 151);  //random location - doesnt metter where, just to start things running
    static Location mLastKnownLocation;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    private String isItSecondMarkerClick = "";
    public int defUserRadius = 50;


    // test
    //starting the map window.
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

        addNoteButton = (Button) findViewById(R.id.addnotebutton);

        /*
        calling to get all the pointer will be here
        should add it to the documentation
        .
        .
        .
        .
        .
         */

    }


    // The main logic of the map. executed just after the map loaded
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // Starting the map to fixed location and zoom that will be changed in the  UpdateLocationUI function soon, but its needed for the right loading.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDeaultlocation, DEFAULT_ZOOM));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            mMap.setMyLocationEnabled(true);
        }

        updateLocationUI(); //updating the screen location and zoom to the  mLastKnownLocation. - important that it will come before getting the device location in the first time becouse i askes for permissions as well.
        getDeviceLocation(); //getting Device Current location if all permissions were given and saves it in  mLastKnownLocation, otherwise asking for permissions.


        //Click listener for Add note
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, AddNoteActivity.class);
                startActivity(intent);

            }
        });

        //hardcoded marker for tests
        /*
        LatLng sydney = new LatLng(37.4244618058266, -122.08005726358829);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Dotan Beck"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setOnMarkerClickListener(this);
        */

        //getting lists of Notes and making marker from each one of them and present the markers.
        //setting up listener
        ///prpering list from all relevant notes from servers in a list
        ArrayList<Note> notes = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://notemedb-milab-default-rtdb.firebaseio.com/");
        DatabaseReference myRef = database.getReference("Notes/");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notes.clear();
                for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()){
                    try{
                        Note currentNote = noteSnapshot.getValue(Note.class);
                        notes.add(currentNote);
                    } catch (Exception e){ //might happen if we dont add proper notes to the DB.
                        Log.e("DB to Markers", "something went wrong with converting DB to Notes");
                    }
                }
                //adding markers from a list to the map.
                for (Note note: notes) {
                    Log.i("note list", note.getId());
                    LatLng pos = new LatLng(note.getLat(),note.getLon());
                    //-----------logic of whethere it is in range or not --------------- //
                    Marker marker = mMap.addMarker(new MarkerOptions().position(pos).title(note.getHead()));
                    marker.setTag(note.getId());
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        mMap.setOnMarkerClickListener(this);


    }




    //asking for location permission
    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==PackageManager.PERMISSION_GRANTED){
            mLocationPermissionGranted = true;
        }else{
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

    }

    //Getting the answer of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionGranted = false;
        switch(requestCode){
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:{
                //if the request is cancelled the result is ampty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    mLocationPermissionGranted = true;
                    //couldn't understand how to change the map and everything in that go.. so after givving permissions in the first time need to get out of the app and log back in and then it works fine.
                    //there fore im asking them to quite the app and get back later.
                    Toast.makeText(this, "Thanks for the permission, PLEASE RESTART THE APP", Toast.LENGTH_LONG ).show();
                }
            }
        }
        updateLocationUI();
    }

    //getting last known location and moving the map to it.
    private void getDeviceLocation() {

        Log.i("GET DIVICE LOCATION", "In");  //debug
        try {
            Log.i("GET DIVICE LOCATION", "In TRY");   //debug
            if (mLocationPermissionGranted) {
                Log.i("GET DIVICE LOCATION", "IN IF");    //debug
                Task locationResult = mfusedLocationProviderClient.getLastLocation();
                Log.i("GET DIVICE LOCATION", "made task");    //debug
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) { //then - set the cameras positions to the current location of device.

                            //tests debug
                            Log.i("On Complete", "in if");
                            if (task == null){
                                Log.i("On Complete", "task = null");
                            }else {
                                Log.i("On Complete", "task not null");
                            }
                            //----end of tests-------//

                            mLastKnownLocation = (Location) task.getResult();

                            //tests
                            if (mLastKnownLocation == null){
                                Log.i("On Complete", "last location = null");
                            }else {
                                Log.i("On Complete", "lastlocation not null");
                            }
                            //------end of tests--------//

                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));

                        } else { //if it didnt wored, print the error to log.e and nove to default location.
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


    //Updates the location of the map to phone last known location location
    private void updateLocationUI() {
        Log.i("UPDATE MY LOCATION", "In ");
        if (mMap == null) {
            Log.i("UPDATE MY LOCATION", "In Null ");
            return;
        }
        try {
            Log.i("UPDATE MY LOCATION", "In Try ");
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) { //if permitted
                Log.i("UPDATE MY LOCATION", "In Try - IF ");

                //adding the "location button on the top right of the screen:
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);

            }else{ //if there is still not permission:
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


    //what happens when we click on the markers.
    //the idea is to save which marker was pressed last (saved in isItSecondMarkerClick) using the marker id that is being given automatically by google
    // if the name of the one just clicked and the isItSecondMarkerClick are the same string - it means that this one got pressed again, then open its noteActivity.
    // if its different then its the first time it got pressed in a raw, so only the small marker head opens.
    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.i(marker.getTitle(), "clicked");    //debug
        String noteid = "" + marker.getTag();


        if (marker.getId().equals(isItSecondMarkerClick)){
            Log.i("onMarkerClick", "equals ");    //debug
            isItSecondMarkerClick = "";     //after second click we want to go back to "zero", so 2 more clicks will be needed next time again even if they will click on the same marker again.
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://notemedb-milab-default-rtdb.firebaseio.com/");
            Query myQuery = database.getReference("Notes/").orderByKey().equalTo(noteid);
            myQuery.addListenerForSingleValueEvent(new ValueEventListener() { //this is one time listener only, so even if there will be changes it will not affect. in addition there cannot be any changes in a note because we don't give this option.
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //starting NoteActivity.
                    Note clickedNote = null;

                    for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                        try {
                            clickedNote = noteSnapshot.getValue(Note.class);

                        } catch (Exception e) { //might happen if we dont add proper notes to the DB.
                            Log.e("DB to Markers", "something went wrong with converting DB to Notes");
                        }
                    }


                    //creating intent with the note information for the page.
                    Intent intent = new Intent(MapActivity.this, NoteActivity.class);
                   try{
                       //giving the server the loction of the marker so e can trace it in the database.
                       intent.putExtra("Lat", clickedNote.getLat());
                       intent.putExtra("Lon", clickedNote.getLon());
                       intent.putExtra("Id", clickedNote.getId());
                       intent.putExtra("Head", clickedNote.getHead());
                       intent.putExtra("Body", clickedNote.getBody());
                       intent.putExtra("NumLikes", clickedNote.getNumLikes());
                       //starting the page.


                       startActivity(intent);
                   } catch(Exception e){
                       Log.e("On Marker Click", "try put extra to intent. ");

                       Toast.makeText(MapActivity.this, "Sorry couldn't open this one", Toast.LENGTH_SHORT).show();
                   }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });





            //tests debug
            Log.i("onMarkerClick marker", marker.getId());
            Log.i("onMarkerClick saved", isItSecondMarkerClick);
            //----end of tests-------//


        }else{
            Log.i("onMarkerClick", "Not equals ");    //debug
            isItSecondMarkerClick = marker.getId();
            //tests debug
            Log.i("onMarkerClick marker", marker.getId());
            Log.i("onMarkerClick saved", isItSecondMarkerClick);
            //----end of tests-------//
        }

        return false;
    }




}