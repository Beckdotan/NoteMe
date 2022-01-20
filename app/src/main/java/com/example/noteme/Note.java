package com.example.noteme;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

public class Note {

    public boolean isError;
    public String id;
    public String head;
    public String body;
    public double lon;
    public double lat;
    public int numLikes;


    public Note(boolean isError, String id, String head, String body, double lon, double lat, int numLikes) {
        this.isError = isError;
        this.id = id;
        this.head = head;
        this.body = body;
        this.lon = lon;
        this.lat = lat;
        this.numLikes = numLikes;
    }

    //important for reading from the database.
    public  Note() {
    }

  /*
    //getting Json and return extract all notes from it.
    public void saveNoteInDB (String ref){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ref");
        myRef.setValue("Hello, World!");
    }
   */

    //getting Note and creating JSONObject.
    public JSONObject NoteToJSON (){
        JSONObject json = new JSONObject();
        return json;
    }


    // -------------- Getters for the firebase API -------- //

    public String getId() {
        return id;
    }

    public String getHead() {
        return head;
    }

    public String getBody() {
        return body;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }

    public int getNumLikes() {
        return numLikes;
    }

    public void setError(boolean error) {
        isError = error;
    }

    // -------------- Setters for the firebase API -------- //


    public void setId(String id) {
        this.id = id;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setNumLikes(int numLikes) {
        this.numLikes = numLikes;
    }
}