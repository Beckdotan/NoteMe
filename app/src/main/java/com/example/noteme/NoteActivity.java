package com.example.noteme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NoteActivity extends AppCompatActivity {

    TextView HeadLine;
    TextView Likes;
    TextView Lon;
    TextView Lat;
    TextView Body;
    TextView Id;
    static double givenLat;
    static double givenLon;
    Button likeButton;
    String currentId;
    int currentLikes;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //cosmetics
        getSupportActionBar().hide();


        setContentView(R.layout.activity_note);
        likeButton = (Button) findViewById(R.id.likeButton);
        HeadLine = (TextView) findViewById(R.id.HeadLine);
        Likes = (TextView) findViewById(R.id.Likes);
        Lon = (TextView) findViewById(R.id.Lon);
        Lat = (TextView) findViewById(R.id.Lat);
        Body = (TextView) findViewById(R.id.Body);
        Id = (TextView) findViewById(R.id.Id);

        //Getting the info from the intent and putting it in the text
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        currentId = "" + extras.get("Id");
        currentLikes =  Integer.parseInt("" + extras.get("NumLikes"));

        if (extras != null) {
            HeadLine.setText("" + extras.get("Head"));
            Likes.setText(("Likes: " + extras.get("NumLikes")));
            Lon.setText(("Lon: " + extras.get("Lon")));
            Lat.setText(("Lat: " + extras.get("Lat")));
            Body.setText("" + extras.get("Body"));
            Id.setText("" + extras.get("Id"));


        }

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Notes/" + currentId + "/numLikes");
                //the next 3 line me will be deleted after implementing the logic below which include the users.
                currentLikes += 1;
                myRef.setValue(currentLikes);
                Likes.setText(("Likes: " + currentLikes));

                /*// !!!!!!!!!!!!!!!!!!!!!  should make logic here so that if this note is in the user list,
                // he cannot like it twice (and maybe even deletes his like if he press in the second time !!!!!!!!!!!!!!!!!!!!!!!!!!!!
                //and put that 2 lines in case we want to add..

                if currentId is not on his list run then:
                //adding the like to server and adding is in view.
                currentLikes += 1;
                myRef.setValue(currentLikes);
                Likes.setText(("Likes: " + currentLikes));

                //------
                else if currentId is in his liked list then:
                currentLikes -= 1;
                myRef.setValue(currentLikes);
                Likes.setText(("Likes: " + currentLikes));
                 */

            }
        });

    }
}