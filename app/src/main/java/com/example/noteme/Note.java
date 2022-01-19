package com.example.noteme;

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
    //getting Json and return extract all notes from it.
    public Note[] JsonToNote (JSONObject json){
        Note[] noteList = new Note[0];
        return noteList;
    }

    //getting Note and creating JSONObject.
    public JSONObject NoteToJSON (Note note){
        JSONObject json = new JSONObject();
        return json;
    }
}