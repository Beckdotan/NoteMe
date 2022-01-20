package com.example.noteme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

public class AddNoteActivity extends AppCompatActivity {

    TextView headText;
    TextView bodyText;
    Button submitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        //cosmetics
        getSupportActionBar().hide();

        //connecting Ui components
        headText = findViewById(R.id.headlineText);
        bodyText = findViewById(R.id.bodyText);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //prepering data
                Log.e("on click", "in on click ");
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("ref");
                myRef.setValue("Hello, World!");

                Log.e("on click", "start prepering data ");
                String head = headText.getText().toString();
                String body = bodyText.getText().toString();
                double lat = MapActivity.mLastKnownLocation.getLatitude();
                double lon = MapActivity.mLastKnownLocation.getLongitude();
                int likes = 0;
                String id = lat +"/"+ lon;


                //creating note
                Note note = new Note(false, id, head, body, lon, lat, likes);
                //making it to Json


                saveNoteInDB(note, "ref");

                /*
                ---------sending JsonNote To Server------------

                 */
            }
        });


    }


    public void saveNoteInDB (Note note, String ref) {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ref");
        myRef.setValue("Hello, World!");
    }




}