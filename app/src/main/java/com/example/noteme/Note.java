package com.example.noteme;

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

}