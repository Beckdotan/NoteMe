package com.example.noteme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class NoteActivity extends AppCompatActivity {

    TextView HeadLine;
    TextView Likes;
    TextView Lon;
    TextView Lat;
    TextView Body;
    TextView Id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        HeadLine = (TextView) findViewById(R.id.HeadLine);
        Likes = (TextView) findViewById(R.id.Likes);
        Lon = (TextView) findViewById(R.id.Lon);
        Lat = (TextView) findViewById(R.id.Lat);
        Body = (TextView) findViewById(R.id.Body);
        Id = (TextView) findViewById(R.id.Id);

        Note note = new Note(false, "kjh35gvk7756ll67", "Dotan Beck ","is the king!\ncannot believe that it works!!", 37.4244618058266, -122.08005726358829, 60);
        //cosmetics for splash screen
        getSupportActionBar().hide();

        fetchNotePage(note);


    }


    public void fetchNotePage(final Note note) {
        HeadLine.setText(note.head);
        Likes.setText("Likes: " + note.numLikes);
        Lon.setText("Lon: " + note.lon );
        Lat.setText("Lat: "+ note.lat);
        Body.setText(note.body);
        Id.setText(note.id);

    }
}