package com.example.noteme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class NoteActivity extends AppCompatActivity {

    TextView HeadLine;
    TextView Likes;
    TextView Lon;
    TextView Lat;
    TextView Body;
    TextView Id;
    static double givenLat;
    static double givenLon;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //cosmetics
        getSupportActionBar().hide();

        setContentView(R.layout.activity_note);
        HeadLine = (TextView) findViewById(R.id.HeadLine);
        Likes = (TextView) findViewById(R.id.Likes);
        Lon = (TextView) findViewById(R.id.Lon);
        Lat = (TextView) findViewById(R.id.Lat);
        Body = (TextView) findViewById(R.id.Body);
        Id = (TextView) findViewById(R.id.Id);

        //Getting the info from the intent and putting it in the text
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            HeadLine.setText("" + extras.get("Head"));
            Likes.setText(("Likes: " + extras.get("NumLikes")));
            Lon.setText(("Lon: " + extras.get("Lon")));
            Lat.setText(("Lat: " + extras.get("Lat")));
            Body.setText("" + extras.get("Body"));
            Id.setText("" + extras.get("Id"));
        }

    }


        /*
        //-------------HARD CODE FOR TESTINGS -----------
        Note note = new Note(false, "kjh35gvk7756ll67", "Dotan Beck ","is the king!\ncannot believe that it works!!", 37.4244618058266, -122.08005726358829, 60);
        */

        /*
       ----------DO NOT DELETE!!!! -----
       //That is the right one when the server will be ready.

        //extracting the inforamtion got from other activity about the location that got pressed.
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            givenLat = Double.parseDouble(extras.getString("Lat"));
            givenLon = Double.parseDouble(extras.getString("Lon"));

        }
        //fetch note from server and presenting it in the UI.
        fetchNote();
        */





/*
    }

    public void fetchNote() {
        final SingleNoteFetcher fetcher = new SingleNoteFetcher(this);

        //Loading Box
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Stock...");
        progressDialog.show();

        fetcher.dispatchRequest(new SingleNoteFetcher.SingleNoteResponseListener() {
            @Override
            public void onResponse(SingleNoteFetcher.SingleNoteResponse response) {
                progressDialog.hide();
                if (response.isError) {
                    Log.i("main", "in response.is_err");
                    //Toast.makeText(this, "Error while fetching Note", Toast.LENGTH_LONG).show();
                    return;
                }
                Note note = new Note(response.isError, response.id, response.head, response.body, response.lon, response.lat, response.numLikes);
                fetchNotePage(note);

            }
        });
    }


    public void fetchNotePage (final Note note) {


    }

 */
}